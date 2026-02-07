package thaumcraft.common.lib.events;

import com.google.common.base.Predicate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockBamf;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.devices.TileArcaneEar;
import thaumcraft.common.world.ThaumcraftWorldGenerator;
import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.AuraThread;

public class ServerEvents {
   long lastcheck = 0L;
   HashMap<Integer, Integer> serverTicks = new HashMap();
   public static ConcurrentHashMap<Integer, AuraThread> auraThreads = new ConcurrentHashMap();
   DecimalFormat myFormatter = new DecimalFormat("#######.##");
   public static HashMap<Integer, LinkedBlockingQueue<ServerEvents.VirtualSwapper>> swapList = new HashMap();
   public static HashMap<Integer, ArrayList<ChunkPos>> chunksToGenerate = new HashMap();
   public static final Predicate<ServerEvents.SwapperPredicate> DEFAULT_PREDICATE = new Predicate<ServerEvents.SwapperPredicate>() {
      public boolean apply(@Nullable ServerEvents.SwapperPredicate pred) {
         return true;
      }
   };
   private static HashMap<Integer, LinkedBlockingQueue<ServerEvents.RunnableEntry>> serverRunList = new HashMap();
   private static LinkedBlockingQueue<ServerEvents.RunnableEntry> clientRunList = new LinkedBlockingQueue();

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void clientWorldTick(ClientTickEvent event) {
      if (event.side != Side.SERVER) {
         if (event.phase == Phase.END && !clientRunList.isEmpty()) {
            LinkedBlockingQueue temp = new LinkedBlockingQueue();

            while(!clientRunList.isEmpty()) {
               ServerEvents.RunnableEntry current = (ServerEvents.RunnableEntry)clientRunList.poll();
               if (current != null) {
                  if (current.delay > 0) {
                     --current.delay;
                     temp.offer(current);
                  } else {
                     try {
                        current.runnable.run();
                     } catch (Exception var5) {
                     }
                  }
               }
            }

            while(!temp.isEmpty()) {
               clientRunList.offer(temp.poll());
            }
         }

      }
   }

   @SubscribeEvent
   public void worldTick(WorldTickEvent event) {
      if (event.side != Side.CLIENT) {
         int dim = event.world.field_73011_w.getDimension();
         if (event.phase == Phase.START) {
            if (!auraThreads.containsKey(dim) && AuraHandler.getAuraWorld(dim) != null) {
               AuraThread at = new AuraThread(dim);
               Thread thread = new Thread(at);
               thread.start();
               auraThreads.put(dim, at);
            }
         } else {
            if (!this.serverTicks.containsKey(dim)) {
               this.serverTicks.put(dim, 0);
            }

            LinkedBlockingQueue<ServerEvents.RunnableEntry> rlist = (LinkedBlockingQueue)serverRunList.get(dim);
            if (rlist == null) {
               serverRunList.put(dim, new LinkedBlockingQueue());
            } else if (!rlist.isEmpty()) {
               LinkedBlockingQueue temp = new LinkedBlockingQueue();

               while(!rlist.isEmpty()) {
                  ServerEvents.RunnableEntry current = (ServerEvents.RunnableEntry)rlist.poll();
                  if (current != null) {
                     if (current.delay > 0) {
                        --current.delay;
                        temp.offer(current);
                     } else {
                        try {
                           current.runnable.run();
                        } catch (Exception var9) {
                        }
                     }
                  }
               }

               while(!temp.isEmpty()) {
                  rlist.offer(temp.poll());
               }
            }

            int ticks = (Integer)this.serverTicks.get(dim);
            this.tickChunkRegeneration(event);
            this.tickBlockSwap(event.world);
            ArrayList<Integer[]> nbe = (ArrayList)TileArcaneEar.noteBlockEvents.get(dim);
            if (nbe != null) {
               nbe.clear();
            }

            if (ticks % 20 == 0) {
               CopyOnWriteArrayList<ChunkPos> dc = (CopyOnWriteArrayList)AuraHandler.dirtyChunks.get(dim);
               if (dc != null && dc.size() > 0) {
                  Iterator var7 = dc.iterator();

                  while(var7.hasNext()) {
                     ChunkPos pos = (ChunkPos)var7.next();
                     event.world.func_175646_b(pos.func_180619_a(5), (TileEntity)null);
                  }

                  dc.clear();
               }

               TaskHandler.clearSuspendedOrExpiredTasks(event.world);
            }

            SealHandler.tickSealEntities(event.world);
            this.serverTicks.put(dim, ticks + 1);
         }

      }
   }

   public void tickChunkRegeneration(WorldTickEvent event) {
      int dim = event.world.field_73011_w.getDimension();
      int count = 0;
      ArrayList<ChunkPos> chunks = (ArrayList)chunksToGenerate.get(dim);
      if (chunks != null && chunks.size() > 0) {
         for(int a = 0; a < 10; ++a) {
            chunks = (ArrayList)chunksToGenerate.get(dim);
            if (chunks == null || chunks.size() <= 0) {
               break;
            }

            ++count;
            ChunkPos loc = (ChunkPos)chunks.get(0);
            long worldSeed = event.world.func_72905_C();
            Random fmlRandom = new Random(worldSeed);
            long xSeed = fmlRandom.nextLong() >> 3;
            long zSeed = fmlRandom.nextLong() >> 3;
            fmlRandom.setSeed(xSeed * (long)loc.field_77276_a + zSeed * (long)loc.field_77275_b ^ worldSeed);
            ThaumcraftWorldGenerator.INSTANCE.worldGeneration(fmlRandom, loc.field_77276_a, loc.field_77275_b, event.world, false);
            chunks.remove(0);
            chunksToGenerate.put(dim, chunks);
         }
      }

      if (count > 0) {
         FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[Thaumcraft] Regenerated " + count + " chunks. " + Math.max(0, chunks.size()) + " chunks left");
      }

   }

   private void tickBlockSwap(World world) {
      int dim = world.field_73011_w.getDimension();
      LinkedBlockingQueue<ServerEvents.VirtualSwapper> queue = (LinkedBlockingQueue)swapList.get(dim);
      if (queue != null) {
         while(true) {
            if (queue.isEmpty()) {
               swapList.put(dim, queue);
               break;
            }

            ServerEvents.VirtualSwapper vs = (ServerEvents.VirtualSwapper)queue.poll();
            if (vs != null) {
               IBlockState bs = world.func_180495_p(vs.pos);
               boolean allow = bs.func_185887_b(world, vs.pos) >= 0.0F;
               if (vs.source != null && vs.source instanceof IBlockState && (IBlockState)vs.source != bs || vs.source != null && vs.source instanceof Material && (Material)vs.source != bs.func_185904_a()) {
                  allow = false;
               }

               if (vs.visCost > 0.0F && AuraHelper.getVis(world, vs.pos) < vs.visCost) {
                  allow = false;
               }

               if (world.canMineBlockBody(vs.player, vs.pos) && allow && (vs.target == null || !vs.target.func_77969_a(new ItemStack(bs.func_177230_c(), 1, bs.func_177230_c().func_176201_c(bs)))) && !ForgeEventFactory.onPlayerBlockPlace(vs.player, new BlockSnapshot(world, vs.pos, bs), EnumFacing.UP).isCanceled() && vs.allowSwap.apply(new ServerEvents.SwapperPredicate(world, vs.player, vs.pos))) {
                  int slot = true;
                  int slot;
                  if (vs.consumeTarget && vs.target != null) {
                     slot = InventoryUtils.isPlayerCarrying(vs.player, vs.target);
                  } else {
                     slot = 1;
                  }

                  if (vs.player.field_71075_bZ.field_75098_d) {
                     slot = 1;
                  }

                  boolean matches = false;
                  if (vs.source instanceof Material) {
                     matches = bs.func_185904_a() == (Material)vs.source;
                  }

                  if (vs.source instanceof IBlockState) {
                     matches = bs == (IBlockState)vs.source;
                  }

                  if ((vs.source == null || matches) && slot >= 0) {
                     if (!vs.player.field_71075_bZ.field_75098_d) {
                        if (vs.consumeTarget) {
                           vs.player.field_71071_by.func_70298_a(slot, 1);
                        }

                        if (vs.pickup) {
                           List<ItemStack> ret = new ArrayList();
                           if (vs.silk && bs.func_177230_c().canSilkHarvest(world, vs.pos, bs, vs.player)) {
                              ItemStack itemstack = BlockUtils.getSilkTouchDrop(bs);
                              if (itemstack != null) {
                                 ((List)ret).add(itemstack);
                              }
                           } else {
                              ret = bs.func_177230_c().getDrops(world, vs.pos, bs, vs.fortune);
                           }

                           if (((List)ret).size() > 0) {
                              Iterator var16 = ((List)ret).iterator();

                              while(var16.hasNext()) {
                                 ItemStack is = (ItemStack)var16.next();
                                 if (!vs.player.field_71071_by.func_70441_a(is)) {
                                    world.func_72838_d(new EntityItem(world, (double)vs.pos.func_177958_n() + 0.5D, (double)vs.pos.func_177956_o() + 0.5D, (double)vs.pos.func_177952_p() + 0.5D, is));
                                 }
                              }
                           }
                        }

                        if (vs.visCost > 0.0F) {
                           ThaumcraftApi.internalMethods.drainVis(world, vs.pos, vs.visCost, false);
                        }
                     }

                     if (vs.target == null) {
                        world.func_175698_g(vs.pos);
                     } else {
                        Block tb = Block.func_149634_a(vs.target.func_77973_b());
                        if (tb != null) {
                           world.func_180501_a(vs.pos, tb.func_176203_a(vs.target.func_77952_i()), 3);
                        } else {
                           world.func_175698_g(vs.pos);
                           EntitySpecialItem entityItem = new EntitySpecialItem(world, (double)vs.pos.func_177958_n() + 0.5D, (double)vs.pos.func_177956_o() + 0.1D, (double)vs.pos.func_177952_p() + 0.5D, vs.target.func_77946_l());
                           entityItem.field_70181_x = 0.0D;
                           entityItem.field_70159_w = 0.0D;
                           entityItem.field_70179_y = 0.0D;
                           world.func_72838_d(entityItem);
                        }
                     }

                     if (vs.fx) {
                        PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockBamf(vs.pos, vs.color, true, vs.fancy, (EnumFacing)null), new TargetPoint(world.field_73011_w.getDimension(), (double)vs.pos.func_177958_n(), (double)vs.pos.func_177956_o(), (double)vs.pos.func_177952_p(), 32.0D));
                     }

                     if (vs.lifespan > 0) {
                        for(int xx = -1; xx <= 1; ++xx) {
                           for(int yy = -1; yy <= 1; ++yy) {
                              for(int zz = -1; zz <= 1; ++zz) {
                                 matches = false;
                                 if (vs.source instanceof Material) {
                                    IBlockState bb = world.func_180495_p(vs.pos.func_177982_a(xx, yy, zz));
                                    matches = bb.func_177230_c().func_149688_o(bb) == vs.source;
                                 }

                                 if (vs.source instanceof IBlockState) {
                                    matches = world.func_180495_p(vs.pos.func_177982_a(xx, yy, zz)) == vs.source;
                                 }

                                 if ((xx != 0 || yy != 0 || zz != 0) && matches && BlockUtils.isBlockExposed(world, vs.pos.func_177982_a(xx, yy, zz))) {
                                    queue.offer(new ServerEvents.VirtualSwapper(vs.pos.func_177982_a(xx, yy, zz), vs.source, vs.target, vs.consumeTarget, vs.lifespan - 1, vs.player, vs.fx, vs.fancy, vs.color, vs.pickup, vs.silk, vs.fortune, vs.allowSwap, vs.visCost));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public static void addSwapper(World world, BlockPos pos, Object source, ItemStack target, boolean consumeTarget, int life, EntityPlayer player, boolean fx, boolean fancy, int color, boolean pickup, boolean silk, int fortune, Predicate<ServerEvents.SwapperPredicate> allowSwap, float visCost) {
      int dim = world.field_73011_w.getDimension();
      LinkedBlockingQueue<ServerEvents.VirtualSwapper> queue = (LinkedBlockingQueue)swapList.get(dim);
      if (queue == null) {
         swapList.put(dim, new LinkedBlockingQueue());
         queue = (LinkedBlockingQueue)swapList.get(dim);
      }

      queue.offer(new ServerEvents.VirtualSwapper(pos, source, target, consumeTarget, life, player, fx, fancy, color, pickup, silk, fortune, allowSwap, visCost));
      swapList.put(dim, queue);
   }

   public static void addRunnableServer(World world, Runnable runnable, int delay) {
      if (!world.field_72995_K) {
         LinkedBlockingQueue<ServerEvents.RunnableEntry> rlist = (LinkedBlockingQueue)serverRunList.get(world.field_73011_w.getDimension());
         if (rlist == null) {
            serverRunList.put(world.field_73011_w.getDimension(), rlist = new LinkedBlockingQueue());
         }

         rlist.add(new ServerEvents.RunnableEntry(runnable, delay));
      }
   }

   public static void addRunnableClient(World world, Runnable runnable, int delay) {
      if (world.field_72995_K) {
         clientRunList.add(new ServerEvents.RunnableEntry(runnable, delay));
      }
   }

   public static class RunnableEntry {
      Runnable runnable;
      int delay;

      public RunnableEntry(Runnable runnable, int delay) {
         this.runnable = runnable;
         this.delay = delay;
      }
   }

   public static class VirtualSwapper {
      int color;
      boolean fancy;
      Predicate<ServerEvents.SwapperPredicate> allowSwap;
      int lifespan = 0;
      BlockPos pos;
      Object source;
      ItemStack target;
      EntityPlayer player = null;
      boolean fx;
      boolean silk;
      boolean pickup;
      boolean consumeTarget;
      int fortune;
      float visCost;

      VirtualSwapper(BlockPos pos, Object source, ItemStack t, boolean consumeTarget, int life, EntityPlayer p, boolean fx, boolean fancy, int color, boolean pickup, boolean silk, int fortune, Predicate<ServerEvents.SwapperPredicate> allowSwap, float cost) {
         this.pos = pos;
         this.source = source;
         this.target = t;
         this.lifespan = life;
         this.player = p;
         this.consumeTarget = consumeTarget;
         this.fx = fx;
         this.fancy = fancy;
         this.allowSwap = allowSwap;
         this.silk = silk;
         this.fortune = fortune;
         this.pickup = pickup;
         this.color = color;
         this.visCost = cost;
      }
   }

   public static class SwapperPredicate {
      public World world;
      public EntityPlayer player;
      public BlockPos pos;

      public SwapperPredicate(World world, EntityPlayer player, BlockPos pos) {
         this.world = world;
         this.player = player;
         this.pos = pos;
      }
   }
}
