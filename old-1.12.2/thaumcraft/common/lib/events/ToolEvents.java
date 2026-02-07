package thaumcraft.common.lib.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.EntityFollowingItem;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXScanSource;
import thaumcraft.common.lib.network.fx.PacketFXSlash;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.utils.Utils;

public class ToolEvents {
   HashMap<Integer, EnumFacing> lastFaceClicked = new HashMap();
   public static HashMap<Integer, ArrayList<BlockPos>> blockedBlocks = new HashMap();
   boolean blockDestructiveRecursion = false;

   @SubscribeEvent
   public void playerAttack(AttackEntityEvent event) {
      if (event.getEntityPlayer().func_184600_cs() != null) {
         ItemStack heldItem = event.getEntityPlayer().func_184586_b(event.getEntityPlayer().func_184600_cs());
         if (heldItem != null) {
            List<EnumInfusionEnchantment> list = EnumInfusionEnchantment.getInfusionEnchantments(heldItem);
            if (list.contains(EnumInfusionEnchantment.ARCING) && event.getTarget().func_70089_S()) {
               int rank = EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldItem, EnumInfusionEnchantment.ARCING);
               List targets = event.getEntityPlayer().field_70170_p.func_72839_b(event.getEntityPlayer(), event.getTarget().func_174813_aQ().func_72314_b(1.5D + (double)rank, (double)(1.0F + (float)rank / 2.0F), 1.5D + (double)rank));
               int count = 0;
               if (targets.size() > 1) {
                  for(int var9 = 0; var9 < targets.size(); ++var9) {
                     Entity var10 = (Entity)targets.get(var9);
                     if (!var10.field_70128_L && (!(var10 instanceof IEntityOwnable) || ((IEntityOwnable)var10).func_70902_q() == null || !((IEntityOwnable)var10).func_70902_q().equals(event.getEntityPlayer()))) {
                        if (var10 instanceof EntityLiving && var10.func_145782_y() != event.getTarget().func_145782_y() && (!(var10 instanceof EntityPlayer) || ((EntityPlayer)var10).func_70005_c_() != event.getEntityPlayer().func_70005_c_()) && var10.func_70089_S()) {
                           float f = (float)event.getEntityPlayer().func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
                           event.getEntityPlayer().func_70652_k(var10);
                           if (var10.func_70097_a(DamageSource.func_76365_a(event.getEntityPlayer()), f * 0.5F)) {
                              try {
                                 if (var10 instanceof EntityLivingBase) {
                                    EnchantmentHelper.func_151384_a((EntityLivingBase)var10, event.getEntityPlayer());
                                 }
                              } catch (Exception var11) {
                              }

                              var10.func_70024_g((double)(-MathHelper.func_76126_a(event.getEntityPlayer().field_70177_z * 3.1415927F / 180.0F) * 0.5F), 0.1D, (double)(MathHelper.func_76134_b(event.getEntityPlayer().field_70177_z * 3.1415927F / 180.0F) * 0.5F));
                              ++count;
                              if (!event.getEntityPlayer().field_70170_p.field_72995_K) {
                                 PacketHandler.INSTANCE.sendToAllAround(new PacketFXSlash(event.getTarget().func_145782_y(), var10.func_145782_y()), new TargetPoint(event.getEntityPlayer().field_70170_p.field_73011_w.getDimension(), event.getTarget().field_70165_t, event.getTarget().field_70163_u, event.getTarget().field_70161_v, 64.0D));
                              }
                           }
                        }

                        if (count >= rank) {
                           break;
                        }
                     }
                  }

                  if (count > 0 && !event.getEntityPlayer().field_70170_p.field_72995_K) {
                     event.getEntityPlayer().func_184185_a(SoundsTC.wind, 1.0F, 0.9F + event.getEntityPlayer().field_70170_p.field_73012_v.nextFloat() * 0.2F);
                     PacketHandler.INSTANCE.sendToAllAround(new PacketFXSlash(event.getEntityPlayer().func_145782_y(), event.getTarget().func_145782_y()), new TargetPoint(event.getEntityPlayer().field_70170_p.field_73011_w.getDimension(), event.getEntityPlayer().field_70165_t, event.getEntityPlayer().field_70163_u, event.getEntityPlayer().field_70161_v, 64.0D));
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void playerRightClickBlock(RightClickBlock event) {
      if (!event.getWorld().field_72995_K && event.getEntityPlayer() != null) {
         ItemStack heldItem = event.getEntityPlayer().func_184586_b(event.getEntityPlayer().func_184600_cs());
         if (heldItem != null) {
            List<EnumInfusionEnchantment> list = EnumInfusionEnchantment.getInfusionEnchantments(heldItem);
            if (list.contains(EnumInfusionEnchantment.SOUNDING) && !event.getEntityPlayer().func_70093_af()) {
               heldItem.func_77972_a(5, event.getEntityPlayer());
               event.getWorld().func_184148_a((EntityPlayer)null, (double)event.getPos().func_177958_n() + 0.5D, (double)event.getPos().func_177956_o() + 0.5D, (double)event.getPos().func_177952_p() + 0.5D, SoundsTC.wandfail, SoundCategory.BLOCKS, 0.2F, 0.2F + event.getWorld().field_73012_v.nextFloat() * 0.2F);
               PacketHandler.INSTANCE.sendTo(new PacketFXScanSource(event.getPos(), EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldItem, EnumInfusionEnchantment.SOUNDING)), (EntityPlayerMP)event.getEntityPlayer());
            }
         }
      }

   }

   @SubscribeEvent
   public void playerInteract(LeftClickBlock event) {
      if (event.getEntityPlayer() != null) {
         this.lastFaceClicked.put(event.getEntityPlayer().func_145782_y(), event.getFace());
      }

   }

   public static void addBlockedBlock(World world, BlockPos pos) {
      if (!blockedBlocks.containsKey(world.field_73011_w.getDimension())) {
         blockedBlocks.put(world.field_73011_w.getDimension(), new ArrayList());
      }

      ArrayList<BlockPos> list = (ArrayList)blockedBlocks.get(world.field_73011_w.getDimension());
      if (!list.contains(pos)) {
         list.add(pos);
      }

   }

   public static void clearBlockedBlock(World world, BlockPos pos) {
      if (!blockedBlocks.containsKey(world.field_73011_w.getDimension())) {
         blockedBlocks.put(world.field_73011_w.getDimension(), new ArrayList());
      } else {
         ArrayList<BlockPos> list = (ArrayList)blockedBlocks.get(world.field_73011_w.getDimension());
         list.remove(pos);
      }
   }

   @SubscribeEvent
   public void breakBlockEvent(BreakEvent event) {
      if (blockedBlocks.containsKey(event.getWorld().field_73011_w.getDimension())) {
         ArrayList<BlockPos> list = (ArrayList)blockedBlocks.get(event.getWorld().field_73011_w.getDimension());
         if (list == null) {
            list = new ArrayList();
            blockedBlocks.put(event.getWorld().field_73011_w.getDimension(), list);
         }

         if (list.contains(event.getPos())) {
            event.setCanceled(true);
         }
      }

      if (!event.getWorld().field_72995_K && event.getPlayer() != null) {
         ItemStack heldItem = event.getPlayer().func_184586_b(event.getPlayer().func_184600_cs());
         if (heldItem != null) {
            List<EnumInfusionEnchantment> list = EnumInfusionEnchantment.getInfusionEnchantments(heldItem);
            if (ForgeHooks.isToolEffective(event.getWorld(), event.getPos(), heldItem) && list.contains(EnumInfusionEnchantment.BURROWING) && !event.getPlayer().func_70093_af() && this.isValidBurrowBlock(event.getWorld(), event.getPos())) {
               event.setCanceled(true);
               BlockUtils.breakFurthestBlock(event.getWorld(), event.getPos(), event.getState(), event.getPlayer());
            }
         }
      }

   }

   private boolean isValidBurrowBlock(World world, BlockPos pos) {
      return Utils.isWoodLog(world, pos) || Utils.isOreBlock(world, pos);
   }

   @SubscribeEvent
   public void harvestBlockEvent(HarvestDropsEvent event) {
      if (!event.getWorld().field_72995_K && event.getHarvester() != null) {
         ItemStack heldItem = event.getHarvester().func_184586_b(event.getHarvester().func_184600_cs());
         if (heldItem != null) {
            List<EnumInfusionEnchantment> list = EnumInfusionEnchantment.getInfusionEnchantments(heldItem);
            if (event.isSilkTouching() || ForgeHooks.isToolEffective(event.getWorld(), event.getPos(), heldItem) || heldItem.func_77973_b() instanceof ItemTool && ((ItemTool)heldItem.func_77973_b()).func_150893_a(heldItem, event.getState()) > 1.0F) {
               int xx;
               if (list.contains(EnumInfusionEnchantment.REFINING)) {
                  int fortune = 1 + EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldItem, EnumInfusionEnchantment.REFINING);
                  float chance = (float)fortune * 0.125F;
                  boolean b = false;

                  for(xx = 0; xx < event.getDrops().size(); ++xx) {
                     ItemStack is = (ItemStack)event.getDrops().get(xx);
                     ItemStack smr = Utils.findSpecialMiningResult(is, chance, event.getWorld().field_73012_v);
                     if (!is.func_77969_a(smr)) {
                        event.getDrops().set(xx, smr);
                        b = true;
                     }
                  }

                  if (b) {
                     event.getWorld().func_184133_a((EntityPlayer)null, event.getPos(), SoundEvents.field_187607_bg, SoundCategory.PLAYERS, 0.2F, 0.7F + event.getWorld().field_73012_v.nextFloat() * 0.2F);
                  }
               }

               if (!this.blockDestructiveRecursion && list.contains(EnumInfusionEnchantment.DESTRUCTIVE) && !event.getHarvester().func_70093_af()) {
                  this.blockDestructiveRecursion = true;
                  EnumFacing face = (EnumFacing)this.lastFaceClicked.get(event.getHarvester().func_145782_y());
                  if (face == null) {
                     face = BlockPistonBase.func_185647_a(event.getPos(), event.getHarvester());
                  }

                  int aa = -1;

                  while(true) {
                     if (aa > 1) {
                        this.blockDestructiveRecursion = false;
                        break;
                     }

                     for(int bb = -1; bb <= 1; ++bb) {
                        if (aa != 0 || bb != 0) {
                           xx = 0;
                           int yy = 0;
                           int zz = 0;
                           if (face.ordinal() <= 1) {
                              xx = aa;
                              zz = bb;
                           } else if (face.ordinal() <= 3) {
                              xx = aa;
                              yy = bb;
                           } else {
                              zz = aa;
                              yy = bb;
                           }

                           IBlockState bl = event.getWorld().func_180495_p(event.getPos().func_177982_a(xx, yy, zz));
                           if (bl.func_185887_b(event.getWorld(), event.getPos().func_177982_a(xx, yy, zz)) >= 0.0F && (ForgeHooks.isToolEffective(event.getWorld(), event.getPos().func_177982_a(xx, yy, zz), heldItem) || heldItem.func_77973_b() instanceof ItemTool && ((ItemTool)heldItem.func_77973_b()).func_150893_a(heldItem, bl) > 1.0F)) {
                              heldItem.func_77972_a(1, event.getHarvester());
                              BlockUtils.harvestBlock(event.getWorld(), event.getHarvester(), event.getPos().func_177982_a(xx, yy, zz));
                           }
                        }
                     }

                     ++aa;
                  }
               }

               if (list.contains(EnumInfusionEnchantment.COLLECTOR) && !event.getHarvester().func_70093_af()) {
                  InventoryUtils.dropHarvestsAtPos(event.getWorld(), event.getPos(), event.getDrops(), true, 10, event.getHarvester());
                  event.getDrops().clear();
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void fillBucket(FillBucketEvent event) {
      if (event.getTarget() != null && event.getTarget().field_72313_a == Type.BLOCK) {
         IBlockState bs = event.getWorld().func_180495_p(event.getTarget().func_178782_a());
         if (bs.func_177230_c() == BlocksTC.purifyingFluid && ((BlockFluidClassic)bs.func_177230_c()).isSourceBlock(event.getWorld(), event.getTarget().func_178782_a())) {
            event.getWorld().func_175698_g(event.getTarget().func_178782_a());
            event.setFilledBucket(new ItemStack(ItemsTC.bucketPure));
            event.setResult(Result.ALLOW);
            return;
         }

         if (bs.func_177230_c() == BlocksTC.liquidDeath && ((BlockFluidClassic)bs.func_177230_c()).isSourceBlock(event.getWorld(), event.getTarget().func_178782_a())) {
            event.getWorld().func_175698_g(event.getTarget().func_178782_a());
            event.setFilledBucket(new ItemStack(ItemsTC.bucketDeath));
            event.setResult(Result.ALLOW);
            return;
         }
      }

   }

   @SubscribeEvent
   public void livingDrops(LivingDropsEvent event) {
      if (event.getSource().func_76346_g() != null && event.getSource().func_76346_g() instanceof EntityPlayer) {
         ItemStack heldItem = ((EntityPlayer)event.getSource().func_76346_g()).func_184607_cu();
         if (heldItem != null) {
            List<EnumInfusionEnchantment> list = EnumInfusionEnchantment.getInfusionEnchantments(heldItem);
            int q;
            if (list.contains(EnumInfusionEnchantment.COLLECTOR)) {
               boolean b = false;

               for(q = 0; q < event.getDrops().size(); ++q) {
                  EntityItem ei = (EntityItem)event.getDrops().get(q);
                  ItemStack is = ei.func_92059_d().func_77946_l();
                  EntityItem nei = new EntityFollowingItem(event.getEntity().field_70170_p, ei.field_70165_t, ei.field_70163_u, ei.field_70161_v, is, event.getSource().func_76346_g(), 10);
                  nei.field_70159_w = ei.field_70159_w;
                  nei.field_70181_x = ei.field_70181_x;
                  nei.field_70179_y = ei.field_70179_y;
                  nei.func_174869_p();
                  ei.func_70106_y();
                  event.getDrops().set(q, nei);
               }
            }

            if (list.contains(EnumInfusionEnchantment.ESSENCE)) {
               AspectList aspects = AspectHelper.getEntityAspects(event.getEntityLiving()).copy();
               if (aspects != null && aspects.size() > 0) {
                  q = EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldItem, EnumInfusionEnchantment.ESSENCE);
                  Aspect[] al = aspects.getAspects();
                  int b = event.getEntity().field_70170_p.field_73012_v.nextInt(5) < q ? 0 : 99;

                  while(b < q && al != null && al.length > 0) {
                     Aspect aspect = al[event.getEntity().field_70170_p.field_73012_v.nextInt(al.length)];
                     if (aspects.getAmount(aspect) > 0) {
                        aspects.remove(aspect, 1);
                        ItemStack stack = ThaumcraftApiHelper.makeCrystal(aspect);
                        if (list.contains(EnumInfusionEnchantment.COLLECTOR)) {
                           event.getDrops().add(new EntityFollowingItem(event.getEntity().field_70170_p, event.getEntityLiving().field_70165_t, event.getEntityLiving().field_70163_u + (double)event.getEntityLiving().func_70047_e(), event.getEntityLiving().field_70161_v, stack, event.getSource().func_76346_g(), 10));
                        } else {
                           event.getDrops().add(new EntityItem(event.getEntity().field_70170_p, event.getEntityLiving().field_70165_t, event.getEntityLiving().field_70163_u + (double)event.getEntityLiving().func_70047_e(), event.getEntityLiving().field_70161_v, stack));
                        }

                        ++b;
                     }

                     al = aspects.getAspects();
                     if (event.getEntity().field_70170_p.field_73012_v.nextInt(q) == 0) {
                        b += 1 + event.getEntity().field_70170_p.field_73012_v.nextInt(2);
                     }
                  }
               }
            }
         }
      }

   }
}
