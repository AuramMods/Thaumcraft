package thaumcraft.client.lib.events;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderTooltipEvent.PostText;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.golems.ISealDisplayer;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.items.IArchitect;
import thaumcraft.api.items.IArchitectExtended;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.gui.GuiResearchPopup;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.construct.golem.seals.SealEntity;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketNote;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;
import thaumcraft.common.tiles.devices.TileArcaneEar;
import thaumcraft.common.tiles.devices.TileRedstoneRelay;

public class RenderEventHandler {
   public static RenderEventHandler INSTANCE = new RenderEventHandler();
   @SideOnly(Side.CLIENT)
   public HudHandler hudHandler = new HudHandler();
   @SideOnly(Side.CLIENT)
   public WandRenderingHandler wandHandler = new WandRenderingHandler();
   @SideOnly(Side.CLIENT)
   ShaderHandler shaderhandler = new ShaderHandler();
   public static List blockTags = new ArrayList();
   public static float tagscale = 0.0F;
   public static GuiResearchPopup researchPopup = null;
   public int tickCount = 0;
   boolean checkedDate = false;
   private Random random = new Random();
   public static TreeMap<Integer, RenderEventHandler.ChargeEntry> chargedItems = new TreeMap();
   public static boolean resetShaders = false;
   private static int oldDisplayWidth = 0;
   private static int oldDisplayHeight = 0;
   final ResourceLocation CFRAME = new ResourceLocation("thaumcraft", "textures/misc/frame_corner.png");
   final ResourceLocation MIDDLE = new ResourceLocation("thaumcraft", "textures/misc/seal_area.png");
   EnumFacing[][] rotfaces;
   int[][] rotmat;
   public static HashMap<Integer, ShaderGroup> shaderGroups = new HashMap();
   public static boolean fogFiddled = false;
   public static float fogTarget = 0.0F;
   public static int fogDuration = 0;
   public static float prevVignetteBrightness = 0.0F;
   public static float targetBrightness = 1.0F;
   protected static final ResourceLocation vignetteTexPath = new ResourceLocation("thaumcraft", "textures/misc/vignette.png");

   public RenderEventHandler() {
      this.rotfaces = new EnumFacing[][]{{EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.WEST}, {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.WEST}, {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST}, {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST}, {EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.EAST}, {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.EAST}, {EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.WEST}, {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.WEST}};
      this.rotmat = new int[][]{{0, 270, 0}, {270, 180, 270}, {90, 0, 90}, {180, 90, 180}, {180, 180, 0}, {90, 270, 270}, {270, 90, 90}, {0, 0, 180}};
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void playerTick(PlayerTickEvent event) {
      if (event.side != Side.SERVER) {
         Minecraft mc = Minecraft.func_71410_x();
         if (event.phase == Phase.START) {
            if (event.player.field_70173_aa % 20 == 0) {
               this.checkChargedItems(event.player);
            }

            try {
               if (event.player.func_145782_y() == mc.field_71439_g.func_145782_y()) {
                  this.shaderhandler.checkShaders(event, mc);
                  if (ShaderHandler.warpVignette > 0) {
                     --ShaderHandler.warpVignette;
                     targetBrightness = 0.0F;
                  } else {
                     targetBrightness = 1.0F;
                  }

                  if (fogFiddled) {
                     if (fogDuration < 100) {
                        fogTarget = 0.1F * ((float)fogDuration / 100.0F);
                     } else if (fogTarget < 0.1F) {
                        fogTarget += 0.001F;
                     }

                     --fogDuration;
                     if (fogDuration < 0) {
                        fogFiddled = false;
                     }
                  }
               }
            } catch (Exception var4) {
            }
         }

      }
   }

   private void checkChargedItems(EntityPlayer player) {
      long time = System.currentTimeMillis();
      int count = 0;
      ItemStack[] inv = player.field_71071_by.field_70462_a;
      int a = 0;

      while(true) {
         InventoryPlayer var10001 = player.field_71071_by;
         if (a >= InventoryPlayer.func_70451_h()) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);

            int a;
            for(a = 0; a < baubles.getSlots(); ++a) {
               if (baubles.getStackInSlot(a) != null && baubles.getStackInSlot(a).func_77973_b() instanceof IRechargable) {
                  this.addItemToChargedList(baubles.getStackInSlot(a), count, time, player);
               } else {
                  chargedItems.remove(count);
               }

               ++count;
            }

            inv = player.field_71071_by.field_70460_b;

            for(a = 0; a < inv.length; ++a) {
               if (inv[a] != null && inv[a].func_77973_b() instanceof IRechargable) {
                  this.addItemToChargedList(inv[a], count, time, player);
               } else {
                  chargedItems.remove(count);
               }

               ++count;
            }

            return;
         }

         if (inv[a] != null && inv[a].func_77973_b() instanceof IRechargable) {
            this.addItemToChargedList(inv[a], count, time, player);
         } else {
            chargedItems.remove(count);
         }

         ++count;
         ++a;
      }
   }

   private void addItemToChargedList(ItemStack item, int count, long time, EntityPlayer player) {
      IRechargable.EnumChargeDisplay ecd = ((IRechargable)item.func_77973_b()).showInHud(item, player);
      if (ecd != IRechargable.EnumChargeDisplay.NEVER) {
         float nc = RechargeHelper.getChargePercentage(item, player);
         if (nc < 0.0F) {
            chargedItems.remove(count);
         } else {
            if (ecd == IRechargable.EnumChargeDisplay.PERIODIC) {
               int q = ((IRechargable)item.func_77973_b()).getMaxCharge(item, player) / 4;
               int c = RechargeHelper.getCharge(item);
               if (c != 0 && c % q != 0) {
                  return;
               }
            }

            RenderEventHandler.ChargeEntry ce = (RenderEventHandler.ChargeEntry)chargedItems.get(count);
            if (ce != null && ce.item.func_77973_b() == item.func_77973_b() && ce.charge != nc) {
               if (nc > ce.charge) {
                  ce.diff = 1;
               } else {
                  ce.diff = -1;
               }

               ce.tickTime = time;
               ce.charge = nc;
               if (ce.time < time - 9500L) {
                  ce.time = time;
               } else {
                  ce.time = time - 500L;
               }
            } else if (ce == null) {
               chargedItems.put(count, new RenderEventHandler.ChargeEntry(time, item, nc));
            } else {
               ce.diff = 0;
            }

         }
      }
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void clientWorldTick(ClientTickEvent event) {
      if (event.side != Side.SERVER) {
         Minecraft mc = FMLClientHandler.instance().getClient();
         World world = mc.field_71441_e;
         if (event.phase == Phase.START) {
            ++this.tickCount;
            String[] var4 = (String[])EssentiaHandler.sourceFX.keySet().toArray(new String[0]);
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String fxk = var4[var6];
               EssentiaHandler.EssentiaSourceFX fx = (EssentiaHandler.EssentiaSourceFX)EssentiaHandler.sourceFX.get(fxk);
               if (world != null) {
                  int mod = 0;
                  TileEntity tile = world.func_175625_s(fx.start);
                  if (tile != null && tile instanceof TileInfusionMatrix) {
                     mod = -1;
                  }

                  FXDispatcher.INSTANCE.essentiaTrailFx(fx.end, fx.start.func_177981_b(mod), this.tickCount, fx.color, 0.1F, fx.ext);
                  EssentiaHandler.sourceFX.remove(fxk);
               }
            }
         } else {
            LinkedBlockingQueue<HudHandler.KnowledgeGainTracker> temp = new LinkedBlockingQueue();
            if (this.hudHandler.knowledgeGainTrackers.isEmpty()) {
               if (this.hudHandler.kgFade > 0.0F) {
                  --this.hudHandler.kgFade;
               }
            } else {
               HudHandler var10000 = this.hudHandler;
               var10000.kgFade += 10.0F;
               if (this.hudHandler.kgFade > 40.0F) {
                  this.hudHandler.kgFade = 40.0F;
               }

               while(!this.hudHandler.knowledgeGainTrackers.isEmpty()) {
                  HudHandler.KnowledgeGainTracker current = (HudHandler.KnowledgeGainTracker)this.hudHandler.knowledgeGainTrackers.poll();
                  if (current != null && current.progress > 0) {
                     --current.progress;
                     temp.offer(current);
                  }
               }

               while(!temp.isEmpty()) {
                  this.hudHandler.knowledgeGainTrackers.offer(temp.poll());
               }
            }

            if (mc.field_71441_e != null && !this.checkedDate) {
               this.checkedDate = true;
               Calendar calendar = mc.field_71441_e.func_83015_S();
               if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
                  Config.isHalloween = true;
               }
            }
         }

      }
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderTick(RenderTickEvent event) {
      if (event.phase == Phase.START) {
         UtilsFX.sysPartialTicks = event.renderTickTime;
      } else {
         Minecraft mc = FMLClientHandler.instance().getClient();
         if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
            long time = System.currentTimeMillis();
            if (researchPopup == null) {
               researchPopup = new GuiResearchPopup(mc);
            }

            researchPopup.updateResearchWindow();
            if (player != null) {
               this.hudHandler.renderHuds(mc, event.renderTickTime, player, time);
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void tooltipEvent(ItemTooltipEvent event) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      GuiScreen gui = mc.field_71462_r;
      if (gui instanceof GuiContainer && GuiScreen.func_146272_n() != Config.showTags && !Mouse.isGrabbed()) {
         AspectList tags = ThaumcraftCraftingManager.getObjectTags(event.getItemStack());
         int index = 0;
         if (tags.size() > 0) {
            Aspect[] var6 = tags.getAspects();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Aspect tag = var6[var8];
               if (tag != null) {
                  ++index;
               }
            }
         }

         int width = index * 18;
         if (width > 0) {
            double sw = (double)mc.field_71466_p.func_78256_a(" ");
            int t = MathHelper.func_76143_f((double)width / sw);
            int l = MathHelper.func_76143_f(18.0D / (double)mc.field_71466_p.field_78288_b);
            String is = "                                                                                                                 ";
            StringBuilder sb = new StringBuilder(is);
            sb.setLength(t);
            sb.append(TextFormatting.RESET);

            for(int a = 0; a < l; ++a) {
               event.getToolTip().add(sb.toString());
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void tooltipEvent(PostText event) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      GuiScreen gui = mc.field_71462_r;
      if (gui instanceof GuiContainer && GuiScreen.func_146272_n() != Config.showTags && !Mouse.isGrabbed()) {
         this.hudHandler.renderAspectsInGui((GuiContainer)gui, mc.field_71439_g, event.getStack(), event.getHeight(), event.getX(), event.getY());
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderOverlay(RenderGameOverlayEvent event) {
      Minecraft mc = Minecraft.func_71410_x();
      long time = System.nanoTime() / 1000000L;
      if (event.getType() == ElementType.TEXT) {
         this.wandHandler.handleFociRadial(mc, time, event);
      }

      if (event.getType() == ElementType.PORTAL) {
         this.renderVignette(targetBrightness, event.getResolution().func_78327_c(), event.getResolution().func_78324_d());
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderShaders(Pre event) {
      if (Config.shaders && event.getType() == ElementType.ALL) {
         Minecraft mc = Minecraft.func_71410_x();
         if (OpenGlHelper.field_148824_g && shaderGroups.size() > 0) {
            this.updateShaderFrameBuffers(mc);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();

            for(Iterator var3 = shaderGroups.values().iterator(); var3.hasNext(); GL11.glPopMatrix()) {
               ShaderGroup sg = (ShaderGroup)var3.next();
               GL11.glPushMatrix();

               try {
                  sg.func_148018_a(event.getPartialTicks());
               } catch (Exception var6) {
               }
            }

            mc.func_147110_a().func_147610_a(true);
         }
      }

   }

   private void updateShaderFrameBuffers(Minecraft mc) {
      if (resetShaders || mc.field_71443_c != oldDisplayWidth || oldDisplayHeight != mc.field_71440_d) {
         Iterator var2 = shaderGroups.values().iterator();

         while(var2.hasNext()) {
            ShaderGroup sg = (ShaderGroup)var2.next();
            sg.func_148026_a(mc.field_71443_c, mc.field_71440_d);
         }

         oldDisplayWidth = mc.field_71443_c;
         oldDisplayHeight = mc.field_71440_d;
         resetShaders = false;
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void blockHighlight(DrawBlockHighlightEvent event) {
      int ticks = event.getPlayer().field_70173_aa;
      RayTraceResult target = event.getTarget();
      if (blockTags.size() > 0) {
         int x = (Integer)blockTags.get(0);
         int y = (Integer)blockTags.get(1);
         int z = (Integer)blockTags.get(2);
         AspectList ot = (AspectList)blockTags.get(3);
         EnumFacing dir = EnumFacing.field_82609_l[(Integer)blockTags.get(4)];
         if (x == target.func_178782_a().func_177958_n() && y == target.func_178782_a().func_177956_o() && z == target.func_178782_a().func_177952_p()) {
            if (tagscale < 0.5F) {
               tagscale += 0.031F - tagscale / 10.0F;
            }

            drawTagsOnContainer((double)((float)target.func_178782_a().func_177958_n() + (float)dir.func_82601_c() / 2.0F), (double)((float)target.func_178782_a().func_177956_o() + (float)dir.func_96559_d() / 2.0F), (double)((float)target.func_178782_a().func_177952_p() + (float)dir.func_82599_e() / 2.0F), ot, 220, dir, event.getPartialTicks());
         }
      }

      RayTraceResult hit;
      if (target != null && target.func_178782_a() != null) {
         TileEntity te = event.getPlayer().field_70170_p.func_175625_s(target.func_178782_a());
         if (te != null && te instanceof TileRedstoneRelay) {
            hit = RayTracer.retraceBlock(event.getPlayer().field_70170_p, event.getPlayer(), target.func_178782_a());
            if (hit != null) {
               if (hit.subHit == 0) {
                  this.drawTextInAir((double)target.func_178782_a().func_177958_n(), (double)target.func_178782_a().func_177956_o() + 0.3D, (double)target.func_178782_a().func_177952_p(), event.getPartialTicks(), "Out: " + ((TileRedstoneRelay)te).getOut());
               } else if (hit.subHit == 1) {
                  this.drawTextInAir((double)target.func_178782_a().func_177958_n(), (double)target.func_178782_a().func_177956_o() + 0.3D, (double)target.func_178782_a().func_177952_p(), event.getPartialTicks(), "In: " + ((TileRedstoneRelay)te).getIn());
               }
            }
         }

         if (EntityUtils.hasGoggles(event.getPlayer())) {
            boolean spaceAbove = event.getPlayer().field_70170_p.func_175623_d(target.func_178782_a().func_177984_a());
            if (te != null) {
               int note = -1;
               if (te instanceof TileEntityNote) {
                  note = ((TileEntityNote)te).field_145879_a;
               } else if (te instanceof TileArcaneEar) {
                  note = ((TileArcaneEar)te).note;
               } else if (te instanceof IAspectContainer && ((IAspectContainer)te).getAspects() != null && ((IAspectContainer)te).getAspects().size() > 0) {
                  float shift = 0.0F;
                  if (tagscale < 0.3F) {
                     tagscale += 0.031F - tagscale / 10.0F;
                  }

                  drawTagsOnContainer((double)target.func_178782_a().func_177958_n(), (double)((float)target.func_178782_a().func_177956_o() + (spaceAbove ? 0.4F : 0.0F) + shift), (double)target.func_178782_a().func_177952_p(), ((IAspectContainer)te).getAspects(), 220, spaceAbove ? EnumFacing.UP : event.getTarget().field_178784_b, event.getPartialTicks());
               }

               if (note >= 0) {
                  if (ticks % 5 == 0) {
                     PacketHandler.INSTANCE.sendToServer(new PacketNote(target.func_178782_a().func_177958_n(), target.func_178782_a().func_177956_o(), target.func_178782_a().func_177952_p(), event.getPlayer().field_70170_p.field_73011_w.getDimension()));
                  }

                  this.drawTextInAir((double)target.func_178782_a().func_177958_n(), (double)(target.func_178782_a().func_177956_o() + 1), (double)target.func_178782_a().func_177952_p(), event.getPartialTicks(), "Note: " + note);
               }
            }
         }
      }

      if (target.field_72313_a == Type.BLOCK && event.getPlayer().func_184614_ca() != null && event.getPlayer().func_184614_ca().func_77973_b() instanceof IArchitect) {
         boolean proceed = true;
         hit = null;
         if (event.getPlayer().func_184614_ca().func_77973_b() instanceof IArchitectExtended) {
            proceed = ((IArchitectExtended)event.getPlayer().func_184614_ca().func_77973_b()).useBlockHighlight(event.getPlayer().func_184614_ca());
            hit = ((IArchitectExtended)event.getPlayer().func_184614_ca().func_77973_b()).getArchitectMOP(event.getPlayer().func_184614_ca(), event.getPlayer().field_70170_p, event.getPlayer());
         }

         if (proceed && this.wandHandler.handleArchitectOverlay(event.getPlayer().func_184614_ca(), event.getPlayer(), event.getPartialTicks(), ticks, hit == null ? target : hit)) {
            event.setCanceled(true);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void renderLast(RenderWorldLastEvent event) {
      if (tagscale > 0.0F) {
         tagscale -= 0.005F;
      }

      float partialTicks = event.getPartialTicks();
      Minecraft mc = Minecraft.func_71410_x();
      if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)mc.func_175606_aa();
         if (player.func_184614_ca() != null) {
            if (player.func_184614_ca().func_77973_b() instanceof ISealDisplayer) {
               this.drawSeals(partialTicks, player);
            }

            if (player.func_184614_ca().func_77973_b() instanceof IArchitectExtended) {
               RayTraceResult target = ((IArchitectExtended)player.func_184614_ca().func_77973_b()).getArchitectMOP(player.func_184614_ca(), player.field_70170_p, player);
               this.wandHandler.handleArchitectOverlay(player.func_184614_ca(), player, partialTicks, player.field_70173_aa, target);
            }
         }
      }

   }

   private void drawSeals(float partialTicks, EntityPlayer player) {
      ConcurrentHashMap<SealPos, SealEntity> seals = (ConcurrentHashMap)SealHandler.sealEntities.get(player.field_70170_p.field_73011_w.getDimension());
      if (seals != null && seals.size() > 0) {
         GL11.glPushMatrix();
         if (player.func_70093_af()) {
            GL11.glDisable(2929);
         }

         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2884);
         double iPX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)partialTicks;
         double iPY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)partialTicks;
         double iPZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)partialTicks;
         GL11.glTranslated(-iPX, -iPY, -iPZ);
         Iterator var10 = seals.values().iterator();

         while(var10.hasNext()) {
            ISealEntity seal = (ISealEntity)var10.next();
            double dis = player.func_174831_c(seal.getSealPos().pos);
            if (dis <= 256.0D) {
               float alpha = 1.0F - (float)(dis / 256.0D);
               boolean ia = false;
               if (seal.isStoppedByRedstone(player.field_70170_p)) {
                  ia = true;
                  if (player.field_70170_p.field_73012_v.nextFloat() < partialTicks / 12.0F) {
                     FXDispatcher.INSTANCE.spark((double)((float)seal.getSealPos().pos.func_177958_n() + 0.5F + (float)seal.getSealPos().face.func_82601_c() * 0.66F), (double)((float)seal.getSealPos().pos.func_177956_o() + 0.5F + (float)seal.getSealPos().face.func_96559_d() * 0.66F), (double)((float)seal.getSealPos().pos.func_177952_p() + 0.5F + (float)seal.getSealPos().face.func_82599_e() * 0.66F), 0.3F, 0.75F - player.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.0F, 0.0F, 1.0F);
                     ia = false;
                  }
               }

               this.renderSeal(seal.getSealPos().pos.func_177958_n(), seal.getSealPos().pos.func_177956_o(), seal.getSealPos().pos.func_177952_p(), alpha, seal.getSealPos().face, seal.getSeal().getSealIcon(), ia);
               this.drawSealArea(player, seal, alpha, partialTicks);
            }
         }

         GL11.glDisable(3042);
         GL11.glEnable(2884);
         if (player.func_70093_af()) {
            GL11.glEnable(2929);
         }

         GL11.glPopMatrix();
      }

   }

   private void drawSealArea(EntityPlayer player, ISealEntity seal, float alpha, float partialTicks) {
      GL11.glPushMatrix();
      float r = 0.0F;
      float g = 0.0F;
      float b = 0.0F;
      if (seal.getColor() > 0) {
         Color c = new Color(EnumDyeColor.func_176764_b(seal.getColor() - 1).func_176768_e().field_76291_p);
         r = (float)c.getRed() / 255.0F;
         g = (float)c.getGreen() / 255.0F;
         b = (float)c.getBlue() / 255.0F;
      } else {
         r = 0.7F + MathHelper.func_76126_a(((float)player.field_70173_aa + partialTicks + (float)seal.getSealPos().pos.func_177958_n()) / 4.0F) * 0.1F;
         g = 0.7F + MathHelper.func_76126_a(((float)player.field_70173_aa + partialTicks + (float)seal.getSealPos().pos.func_177956_o()) / 5.0F) * 0.1F;
         b = 0.7F + MathHelper.func_76126_a(((float)player.field_70173_aa + partialTicks + (float)seal.getSealPos().pos.func_177952_p()) / 6.0F) * 0.1F;
      }

      GL11.glPushMatrix();
      GL11.glTranslated((double)seal.getSealPos().pos.func_177958_n() + 0.5D, (double)seal.getSealPos().pos.func_177956_o() + 0.5D, (double)seal.getSealPos().pos.func_177952_p() + 0.5D);
      GL11.glRotatef(90.0F, (float)(-seal.getSealPos().face.func_96559_d()), (float)seal.getSealPos().face.func_82601_c(), (float)(-seal.getSealPos().face.func_82599_e()));
      if (seal.getSealPos().face.func_82599_e() < 0) {
         GL11.glTranslated(0.0D, 0.0D, -0.5099999904632568D);
      } else {
         GL11.glTranslated(0.0D, 0.0D, 0.5099999904632568D);
      }

      GL11.glRotatef((float)(player.field_70173_aa % 360) + partialTicks, 0.0F, 0.0F, 1.0F);
      UtilsFX.renderQuadCentered(this.MIDDLE, 0.9F, r, g, b, 200, 771, alpha * 0.8F);
      GL11.glPopMatrix();
      if (seal.getSeal() instanceof ISealConfigArea) {
         GL11.glDepthMask(false);
         AxisAlignedBB area = (new AxisAlignedBB((double)seal.getSealPos().pos.func_177958_n(), (double)seal.getSealPos().pos.func_177956_o(), (double)seal.getSealPos().pos.func_177952_p(), (double)(seal.getSealPos().pos.func_177958_n() + 1), (double)(seal.getSealPos().pos.func_177956_o() + 1), (double)(seal.getSealPos().pos.func_177952_p() + 1))).func_72317_d((double)seal.getSealPos().face.func_82601_c(), (double)seal.getSealPos().face.func_96559_d(), (double)seal.getSealPos().face.func_82599_e()).func_72321_a(seal.getSealPos().face.func_82601_c() != 0 ? (double)((seal.getArea().func_177958_n() - 1) * seal.getSealPos().face.func_82601_c()) : 0.0D, seal.getSealPos().face.func_96559_d() != 0 ? (double)((seal.getArea().func_177956_o() - 1) * seal.getSealPos().face.func_96559_d()) : 0.0D, seal.getSealPos().face.func_82599_e() != 0 ? (double)((seal.getArea().func_177952_p() - 1) * seal.getSealPos().face.func_82599_e()) : 0.0D).func_72314_b(seal.getSealPos().face.func_82601_c() == 0 ? (double)(seal.getArea().func_177958_n() - 1) : 0.0D, seal.getSealPos().face.func_96559_d() == 0 ? (double)(seal.getArea().func_177956_o() - 1) : 0.0D, seal.getSealPos().face.func_82599_e() == 0 ? (double)(seal.getArea().func_177952_p() - 1) : 0.0D);
         double[][] locs = new double[][]{{area.field_72340_a, area.field_72338_b, area.field_72339_c}, {area.field_72340_a, area.field_72337_e - 1.0D, area.field_72339_c}, {area.field_72336_d - 1.0D, area.field_72338_b, area.field_72339_c}, {area.field_72336_d - 1.0D, area.field_72337_e - 1.0D, area.field_72339_c}, {area.field_72336_d - 1.0D, area.field_72338_b, area.field_72334_f - 1.0D}, {area.field_72336_d - 1.0D, area.field_72337_e - 1.0D, area.field_72334_f - 1.0D}, {area.field_72340_a, area.field_72338_b, area.field_72334_f - 1.0D}, {area.field_72340_a, area.field_72337_e - 1.0D, area.field_72334_f - 1.0D}};
         int q = 0;
         double[][] var11 = locs;
         int var12 = locs.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            double[] loc = var11[var13];
            GL11.glPushMatrix();
            GL11.glTranslated(loc[0] + 0.5D, loc[1] + 0.5D, loc[2] + 0.5D);
            int w = 0;
            EnumFacing[] var16 = this.rotfaces[q];
            int var17 = var16.length;

            for(int var18 = 0; var18 < var17; ++var18) {
               EnumFacing face = var16[var18];
               GL11.glPushMatrix();
               GL11.glRotatef(90.0F, (float)(-face.func_96559_d()), (float)face.func_82601_c(), (float)(-face.func_82599_e()));
               if (face.func_82599_e() < 0) {
                  GL11.glTranslated(0.0D, 0.0D, -0.49000000953674316D);
               } else {
                  GL11.glTranslated(0.0D, 0.0D, 0.49000000953674316D);
               }

               GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
               GL11.glRotatef((float)this.rotmat[q][w], 0.0F, 0.0F, 1.0F);
               UtilsFX.renderQuadCentered(this.CFRAME, 1.0F, r, g, b, 200, 771, alpha * 0.7F);
               GL11.glPopMatrix();
               ++w;
            }

            GL11.glPopMatrix();
            ++q;
         }

         GL11.glDepthMask(true);
      }

      GL11.glPopMatrix();
   }

   void renderSeal(int x, int y, int z, float alpha, EnumFacing face, ResourceLocation resourceLocation, boolean ia) {
      GL11.glPushMatrix();
      GL11.glColor4f(ia ? 0.5F : 1.0F, ia ? 0.5F : 1.0F, ia ? 0.5F : 1.0F, alpha);
      this.translateSeal((float)x, (float)y, (float)z, face.ordinal(), -0.05F);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      UtilsFX.renderItemIn2D(resourceLocation.toString(), 0.1F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   private void translateSeal(float x, float y, float z, int orientation, float off) {
      if (orientation == 1) {
         GL11.glTranslatef(x + 0.25F, y + 1.0F, z + 0.75F);
         GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 0) {
         GL11.glTranslatef(x + 0.25F, y, z + 0.25F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 3) {
         GL11.glTranslatef(x + 0.25F, y + 0.25F, z + 1.0F);
      } else if (orientation == 2) {
         GL11.glTranslatef(x + 0.75F, y + 0.25F, z);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 5) {
         GL11.glTranslatef(x + 1.0F, y + 0.25F, z + 0.75F);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 4) {
         GL11.glTranslatef(x, y + 0.25F, z + 0.25F);
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, 0.0F, -off);
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void fogDensityEvent(RenderFogEvent event) {
      if (fogFiddled && fogTarget > 0.0F) {
         GL11.glFogi(2917, 2048);
         GL11.glFogf(2914, fogTarget);
      }

   }

   @SubscribeEvent
   public void livingTick(LivingUpdateEvent event) {
      if (event.getEntity().field_70170_p.field_72995_K && event.getEntity() instanceof EntityMob && !event.getEntity().field_70128_L) {
         EntityMob mob = (EntityMob)event.getEntity();
         if (mob.func_110148_a(EntityUtils.CHAMPION_MOD) != null) {
            Integer t = (int)mob.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e();
            if (t != null && t >= 0 && t < ChampionModifier.mods.length) {
               ChampionModifier.mods[t].effect.showFX(mob);
            }
         }
      }

   }

   public static void drawTagsOnContainer(double x, double y, double z, AspectList tags, int bright, EnumFacing dir, float partialTicks) {
      if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer && tags != null && tags.size() > 0 && dir != null) {
         EntityPlayer player = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
         double iPX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)partialTicks;
         double iPY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)partialTicks;
         double iPZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)partialTicks;
         int rowsize = 5;
         int current = 0;
         float shifty = 0.0F;
         int left = tags.size();
         Aspect[] var21 = tags.getAspects();
         int var22 = var21.length;

         for(int var23 = 0; var23 < var22; ++var23) {
            Aspect tag = var21[var23];
            int div = Math.min(left, rowsize);
            if (current >= rowsize) {
               current = 0;
               shifty -= tagscale * 1.05F;
               left -= rowsize;
               if (left < rowsize) {
                  div = left % rowsize;
               }
            }

            float shift = ((float)current - (float)div / 2.0F + 0.5F) * tagscale * 4.0F;
            shift *= tagscale;
            Color color = new Color(tag.getColor());
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            GL11.glTranslated(-iPX + x + 0.5D + (double)(tagscale * 2.0F * (float)dir.func_82601_c()), -iPY + y - (double)shifty + 0.5D + (double)(tagscale * 2.0F * (float)dir.func_96559_d()), -iPZ + z + 0.5D + (double)(tagscale * 2.0F * (float)dir.func_82599_e()));
            float xd = (float)(iPX - (x + 0.5D));
            float zd = (float)(iPZ - (z + 0.5D));
            float rotYaw = (float)(Math.atan2((double)xd, (double)zd) * 180.0D / 3.141592653589793D);
            GL11.glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated((double)shift, 0.0D, 0.0D);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(tagscale, tagscale, tagscale);
            UtilsFX.renderQuadCentered(tag.getImage(), 1.0F, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, bright, 771, 0.75F);
            if (tags.getAmount(tag) >= 0) {
               GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
               String am = "" + tags.getAmount(tag);
               GL11.glScalef(0.04F, 0.04F, 0.04F);
               GL11.glTranslated(0.0D, 6.0D, -0.1D);
               int sw = Minecraft.func_71410_x().field_71466_p.func_78256_a(am);
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               Minecraft.func_71410_x().field_71466_p.func_78276_b(am, 14 - sw, 1, 1118481);
               GL11.glTranslated(0.0D, 0.0D, -0.1D);
               Minecraft.func_71410_x().field_71466_p.func_78276_b(am, 13 - sw, 0, 16777215);
            }

            GL11.glEnable(2929);
            GL11.glPopMatrix();
            ++current;
         }
      }

   }

   public void drawTextInAir(double x, double y, double z, float partialTicks, String text) {
      if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
         double iPX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)partialTicks;
         double iPY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)partialTicks;
         double iPZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)partialTicks;
         GL11.glPushMatrix();
         GL11.glTranslated(-iPX + x + 0.5D, -iPY + y + 0.5D, -iPZ + z + 0.5D);
         float xd = (float)(iPX - (x + 0.5D));
         float zd = (float)(iPZ - (z + 0.5D));
         float rotYaw = (float)(Math.atan2((double)xd, (double)zd) * 180.0D / 3.141592653589793D);
         GL11.glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glScalef(0.02F, 0.02F, 0.02F);
         int sw = Minecraft.func_71410_x().field_71466_p.func_78256_a(text);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         Minecraft.func_71410_x().field_71466_p.func_78276_b(text, 1 - sw / 2, 1, 1118481);
         GL11.glTranslated(0.0D, 0.0D, -0.1D);
         Minecraft.func_71410_x().field_71466_p.func_78276_b(text, -sw / 2, 0, 16777215);
         GL11.glPopMatrix();
      }

   }

   protected void renderVignette(float brightness, double sw, double sh) {
      int k = (int)sw;
      int l = (int)sh;
      brightness = 1.0F - brightness;
      prevVignetteBrightness = (float)((double)prevVignetteBrightness + (double)(brightness - prevVignetteBrightness) * 0.01D);
      if (prevVignetteBrightness > 0.0F) {
         float b = prevVignetteBrightness * (1.0F + MathHelper.func_76126_a((float)Minecraft.func_71410_x().field_71439_g.field_70173_aa / 2.0F) * 0.1F);
         GL11.glPushMatrix();
         GL11.glClear(256);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
         Minecraft.func_71410_x().func_110434_K().func_110577_a(vignetteTexPath);
         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
         GL11.glDisable(2929);
         GL11.glDepthMask(false);
         OpenGlHelper.func_148821_a(0, 769, 1, 0);
         GL11.glColor4f(b, b, b, 1.0F);
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         tessellator.func_178180_c().func_181662_b(0.0D, (double)l, -90.0D).func_187315_a(0.0D, 1.0D).func_181675_d();
         tessellator.func_178180_c().func_181662_b((double)k, (double)l, -90.0D).func_187315_a(1.0D, 1.0D).func_181675_d();
         tessellator.func_178180_c().func_181662_b((double)k, 0.0D, -90.0D).func_187315_a(1.0D, 0.0D).func_181675_d();
         tessellator.func_178180_c().func_181662_b(0.0D, 0.0D, -90.0D).func_187315_a(0.0D, 0.0D).func_181675_d();
         tessellator.func_78381_a();
         GL11.glDepthMask(true);
         GL11.glEnable(2929);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         OpenGlHelper.func_148821_a(770, 771, 1, 0);
         GL11.glPopMatrix();
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void textureStitchEventPre(net.minecraftforge.client.event.TextureStitchEvent.Pre event) {
      event.getMap().func_174942_a(new ResourceLocation("thaumcraft", "research/quill"));
      event.getMap().func_174942_a(new ResourceLocation("thaumcraft", "blocks/crystal"));
      event.getMap().func_174942_a(new ResourceLocation("thaumcraft", "blocks/taint_growth_1"));
      event.getMap().func_174942_a(new ResourceLocation("thaumcraft", "blocks/taint_growth_2"));
      event.getMap().func_174942_a(new ResourceLocation("thaumcraft", "blocks/taint_growth_3"));
      event.getMap().func_174942_a(new ResourceLocation("thaumcraft", "blocks/taint_growth_4"));
   }

   public class ChargeEntry {
      public long time;
      public long tickTime;
      public ItemStack item;
      float charge = 0.0F;
      byte diff = 0;

      public ChargeEntry(long time, ItemStack item, float charge) {
         this.time = time;
         this.item = item;
         this.charge = charge;
      }
   }
}
