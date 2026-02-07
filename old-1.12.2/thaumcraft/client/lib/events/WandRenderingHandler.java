package thaumcraft.client.lib.events;

import baubles.api.BaublesApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.items.IArchitect;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.casters.ItemFocusPouch;
import thaumcraft.common.lib.events.KeyHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;

public class WandRenderingHandler {
   static float radialHudScale = 0.0F;
   TreeMap<String, Integer> foci = new TreeMap();
   HashMap<String, ItemStack> fociItem = new HashMap();
   HashMap<String, Boolean> fociHover = new HashMap();
   HashMap<String, Float> fociScale = new HashMap();
   long lastTime = 0L;
   boolean lastState = false;
   final ResourceLocation R1 = new ResourceLocation("thaumcraft", "textures/misc/radial.png");
   final ResourceLocation R2 = new ResourceLocation("thaumcraft", "textures/misc/radial2.png");
   int lastArcHash = 0;
   ArrayList<BlockPos> architectBlocks = new ArrayList();
   HashMap<BlockPos, boolean[]> bmCache = new HashMap();
   final ResourceLocation CFRAME = new ResourceLocation("thaumcraft", "textures/misc/frame_corner.png");
   final ResourceLocation SFRAME = new ResourceLocation("thaumcraft", "textures/misc/frame_side.png");
   int[][] mos = new int[][]{{4, 5, 6, 7}, {0, 1, 2, 3}, {0, 1, 4, 5}, {2, 3, 6, 7}, {0, 2, 4, 6}, {1, 3, 5, 7}};
   int[][] rotmat = new int[][]{{0, 90, 270, 180}, {270, 180, 0, 90}, {180, 90, 270, 0}, {0, 270, 90, 180}, {270, 180, 0, 90}, {180, 270, 90, 0}};
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/misc/architect_arrows.png");

   @SideOnly(Side.CLIENT)
   public void handleFociRadial(Minecraft mc, long time, RenderGameOverlayEvent event) {
      if (KeyHandler.radialActive || radialHudScale > 0.0F) {
         if (!KeyHandler.radialActive) {
            if (mc.field_71462_r == null && this.lastState) {
               if (Display.isActive() && !mc.field_71415_G) {
                  mc.field_71415_G = true;
                  mc.field_71417_B.func_74372_a();
               }

               this.lastState = false;
            }
         } else {
            if (mc.field_71462_r != null) {
               KeyHandler.radialActive = false;
               KeyHandler.radialLock = true;
               mc.func_71381_h();
               mc.func_71364_i();
               return;
            }

            if (radialHudScale == 0.0F) {
               this.foci.clear();
               this.fociItem.clear();
               this.fociHover.clear();
               this.fociScale.clear();
               int pouchcount = 0;
               ItemStack item = null;
               IInventory baubles = BaublesApi.getBaubles(mc.field_71439_g);
               int a = 0;

               while(true) {
                  ItemStack[] inv;
                  int q;
                  if (a >= 4) {
                     for(a = 0; a < 36; ++a) {
                        item = mc.field_71439_g.field_71071_by.field_70462_a[a];
                        if (item != null && item.func_77973_b() instanceof ItemFocus) {
                           this.foci.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), a);
                           this.fociItem.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), item.func_77946_l());
                           this.fociScale.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), 1.0F);
                           this.fociHover.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), false);
                        }

                        if (item != null && item.func_77973_b() instanceof ItemFocusPouch) {
                           ++pouchcount;
                           inv = ((ItemFocusPouch)item.func_77973_b()).getInventory(item);

                           for(q = 0; q < inv.length; ++q) {
                              item = inv[q];
                              if (item != null && item.func_77973_b() instanceof ItemFocus) {
                                 this.foci.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), q + pouchcount * 1000);
                                 this.fociItem.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), item.func_77946_l());
                                 this.fociScale.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), 1.0F);
                                 this.fociHover.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), false);
                              }
                           }
                        }
                     }

                     if (this.foci.size() > 0 && mc.field_71415_G) {
                        mc.field_71415_G = false;
                        mc.field_71417_B.func_74373_b();
                     }
                     break;
                  }

                  if (baubles.func_70301_a(a) != null && baubles.func_70301_a(a).func_77973_b() instanceof ItemFocusPouch) {
                     ++pouchcount;
                     item = baubles.func_70301_a(a);
                     inv = ((ItemFocusPouch)item.func_77973_b()).getInventory(item);

                     for(q = 0; q < inv.length; ++q) {
                        item = inv[q];
                        if (item != null && item.func_77973_b() instanceof ItemFocus) {
                           this.foci.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), q + pouchcount * 1000);
                           this.fociItem.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), item.func_77946_l());
                           this.fociScale.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), 1.0F);
                           this.fociHover.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), false);
                        }
                     }
                  }

                  ++a;
               }
            }
         }

         this.renderFocusRadialHUD(event.getResolution().func_78327_c(), event.getResolution().func_78324_d(), time, event.getPartialTicks());
         if (time > this.lastTime) {
            Iterator var11 = this.fociHover.keySet().iterator();

            while(var11.hasNext()) {
               String key = (String)var11.next();
               if ((Boolean)this.fociHover.get(key)) {
                  if (!KeyHandler.radialActive && !KeyHandler.radialLock) {
                     PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(key));
                     KeyHandler.radialLock = true;
                  }

                  if ((Float)this.fociScale.get(key) < 1.3F) {
                     this.fociScale.put(key, (Float)this.fociScale.get(key) + 0.025F);
                  }
               } else if ((Float)this.fociScale.get(key) > 1.0F) {
                  this.fociScale.put(key, (Float)this.fociScale.get(key) - 0.025F);
               }
            }

            if (!KeyHandler.radialActive) {
               radialHudScale -= 0.05F;
            } else if (KeyHandler.radialActive && radialHudScale < 1.0F) {
               radialHudScale += 0.05F;
            }

            if (radialHudScale > 1.0F) {
               radialHudScale = 1.0F;
            }

            if (radialHudScale < 0.0F) {
               radialHudScale = 0.0F;
               KeyHandler.radialLock = false;
            }

            this.lastTime = time + 5L;
            this.lastState = KeyHandler.radialActive;
         }
      }

   }

   @SideOnly(Side.CLIENT)
   private void renderFocusRadialHUD(double sw, double sh, long time, float partialTicks) {
      Minecraft mc = Minecraft.func_71410_x();
      ItemStack s = mc.field_71439_g.func_184614_ca();
      if (s == null || !(s.func_77973_b() instanceof ICaster)) {
         s = mc.field_71439_g.func_184592_cb();
      }

      if (s != null && s.func_77973_b() instanceof ICaster) {
         ICaster wand = (ICaster)s.func_77973_b();
         ItemFocus focus = wand.getFocus(s);
         int i = (int)((double)Mouse.getEventX() * sw / (double)mc.field_71443_c);
         int j = (int)(sh - (double)Mouse.getEventY() * sh / (double)mc.field_71440_d - 1.0D);
         int k = Mouse.getEventButton();
         if (this.fociItem.size() != 0) {
            GL11.glPushMatrix();
            GL11.glClear(256);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();
            GL11.glTranslated(sw / 2.0D, sh / 2.0D, 0.0D);
            ItemStack tt = null;
            float width = 16.0F + (float)this.fociItem.size() * 2.5F;
            mc.field_71446_o.func_110577_a(this.R1);
            GL11.glPushMatrix();
            GL11.glRotatef(partialTicks + (float)(mc.field_71439_g.field_70173_aa % 720) / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            UtilsFX.renderQuadCentered(1, 1, 0, width * 2.75F * radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            mc.field_71446_o.func_110577_a(this.R2);
            GL11.glPushMatrix();
            GL11.glRotatef(-(partialTicks + (float)(mc.field_71439_g.field_70173_aa % 720) / 2.0F), 0.0F, 0.0F, 1.0F);
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            UtilsFX.renderQuadCentered(1, 1, 0, width * 2.55F * radialHudScale, 0.5F, 0.5F, 0.5F, 200, 771, 0.5F);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            if (focus != null) {
               ItemStack item = wand.getFocusStack(s).func_77946_l();
               UtilsFX.renderItemInGUI(-8, -8, 100, item);
               int mx = (int)((double)i - sw / 2.0D);
               int my = (int)((double)j - sh / 2.0D);
               if (mx >= -10 && mx <= 10 && my >= -10 && my <= 10) {
                  tt = item;
               }
            }

            GL11.glScaled((double)radialHudScale, (double)radialHudScale, (double)radialHudScale);
            float currentRot = -90.0F * radialHudScale;
            float pieSlice = 360.0F / (float)this.fociItem.size();
            String key = (String)this.foci.firstKey();

            for(int a = 0; a < this.fociItem.size(); ++a) {
               double xx = (double)(MathHelper.func_76134_b(currentRot / 180.0F * 3.1415927F) * width);
               double yy = (double)(MathHelper.func_76126_a(currentRot / 180.0F * 3.1415927F) * width);
               currentRot += pieSlice;
               GL11.glPushMatrix();
               GL11.glTranslated(xx, yy, 100.0D);
               GL11.glScalef((Float)this.fociScale.get(key), (Float)this.fociScale.get(key), (Float)this.fociScale.get(key));
               GL11.glEnable(32826);
               ItemStack item = ((ItemStack)this.fociItem.get(key)).func_77946_l();
               UtilsFX.renderItemInGUI(-8, -8, 100, item);
               GL11.glDisable(32826);
               GL11.glPopMatrix();
               if (!KeyHandler.radialLock && KeyHandler.radialActive) {
                  int mx = (int)((double)i - sw / 2.0D - xx);
                  int my = (int)((double)j - sh / 2.0D - yy);
                  if (mx >= -10 && mx <= 10 && my >= -10 && my <= 10) {
                     this.fociHover.put(key, true);
                     tt = (ItemStack)this.fociItem.get(key);
                     if (k == 0) {
                        KeyHandler.radialActive = false;
                        KeyHandler.radialLock = true;
                        PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(key));
                        break;
                     }
                  } else {
                     this.fociHover.put(key, false);
                  }
               }

               key = (String)this.foci.higherKey(key);
            }

            GL11.glPopMatrix();
            if (tt != null) {
               UtilsFX.drawCustomTooltip(mc.field_71462_r, mc.field_71466_p, tt.func_82840_a(mc.field_71439_g, mc.field_71474_y.field_82882_x), -4, 20, 11);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean handleArchitectOverlay(ItemStack stack, EntityPlayer player, float partialTicks, int playerticks, RayTraceResult target) {
      if (target == null) {
         return false;
      } else {
         Minecraft mc = Minecraft.func_71410_x();
         IArchitect af = (IArchitect)stack.func_77973_b();
         String h = target.func_178782_a().func_177958_n() + "" + target.func_178782_a().func_177956_o() + "" + target.func_178782_a().func_177952_p() + "" + target.field_178784_b + "" + playerticks / 5;
         int hc = h.hashCode();
         if (hc != this.lastArcHash) {
            this.lastArcHash = hc;
            this.bmCache.clear();
            this.architectBlocks = af.getArchitectBlocks(stack, mc.field_71441_e, target.func_178782_a(), target.field_178784_b, player);
         }

         if (this.architectBlocks != null && this.architectBlocks.size() != 0) {
            this.drawArchitectAxis(target.func_178782_a(), partialTicks, af.showAxis(stack, mc.field_71441_e, player, target.field_178784_b, IArchitect.EnumAxis.X), af.showAxis(stack, mc.field_71441_e, player, target.field_178784_b, IArchitect.EnumAxis.Y), af.showAxis(stack, mc.field_71441_e, player, target.field_178784_b, IArchitect.EnumAxis.Z));
            Iterator var10 = this.architectBlocks.iterator();

            while(var10.hasNext()) {
               BlockPos cc = (BlockPos)var10.next();
               this.drawOverlayBlock(cc, playerticks, mc, partialTicks);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean isConnectedBlock(World world, BlockPos pos) {
      return this.architectBlocks.contains(pos);
   }

   @SideOnly(Side.CLIENT)
   private boolean[] getConnectedSides(World world, BlockPos pos) {
      if (this.bmCache.containsKey(pos)) {
         return (boolean[])this.bmCache.get(pos);
      } else {
         boolean[] bitMatrix = new boolean[]{!this.isConnectedBlock(world, pos.func_177982_a(-1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, -1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, -1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(-1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, 1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, 1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(-1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, -1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, -1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, -1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, -1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(-1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, 1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, -1, 0)), !this.isConnectedBlock(world, pos.func_177982_a(1, 0, 0)) && !this.isConnectedBlock(world, pos.func_177982_a(0, 0, 1)) && !this.isConnectedBlock(world, pos.func_177982_a(0, -1, 0))};
         this.bmCache.put(pos, bitMatrix);
         return bitMatrix;
      }
   }

   @SideOnly(Side.CLIENT)
   public void drawOverlayBlock(BlockPos pos, int ticks, Minecraft mc, float partialTicks) {
      boolean[] bitMatrix = this.getConnectedSides(mc.field_71441_e, pos);
      GL11.glPushMatrix();
      GlStateManager.func_179112_b(770, 771);
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glDisable(2884);
      EntityPlayer player = (EntityPlayer)mc.func_175606_aa();
      double iPX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)partialTicks;
      double iPY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)partialTicks;
      double iPZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)partialTicks;
      GL11.glTranslated(-iPX + (double)pos.func_177958_n() + 0.5D, -iPY + (double)pos.func_177956_o() + 0.5D, -iPZ + (double)pos.func_177952_p() + 0.5D);
      EnumFacing[] var13 = EnumFacing.values();
      int var14 = var13.length;

      for(int var15 = 0; var15 < var14; ++var15) {
         EnumFacing face = var13[var15];
         if (!this.isConnectedBlock(mc.field_71441_e, pos.func_177972_a(face))) {
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, (float)(-face.func_96559_d()), (float)face.func_82601_c(), (float)(-face.func_82599_e()));
            if (face.func_82599_e() < 0) {
               GL11.glTranslated(0.0D, 0.0D, -0.5D);
            } else {
               GL11.glTranslated(0.0D, 0.0D, 0.5D);
            }

            GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
            GL11.glPushMatrix();
            UtilsFX.renderQuadCentered(this.SFRAME, 1, 1, 0, 1.0F, 1.0F, 1.0F, 1.0F, 200, 1, 0.33F);
            GL11.glPopMatrix();

            for(int a = 0; a < 4; ++a) {
               if (bitMatrix[this.mos[face.ordinal()][a]]) {
                  GL11.glPushMatrix();
                  GL11.glRotatef((float)this.rotmat[face.ordinal()][a], 0.0F, 0.0F, 1.0F);
                  UtilsFX.renderQuadCentered(this.CFRAME, 1, 1, 0, 1.0F, 1.0F, 1.0F, 1.0F, 200, 1, 0.66F);
                  GL11.glPopMatrix();
               }
            }

            GL11.glPopMatrix();
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(2884);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GlStateManager.func_179084_k();
      GlStateManager.func_179092_a(516, 0.1F);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public void drawArchitectAxis(BlockPos pos, float partialTicks, boolean dx, boolean dy, boolean dz) {
      if (dx || dy || dz) {
         EntityPlayer player = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
         double iPX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)partialTicks;
         double iPY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)partialTicks;
         double iPZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)partialTicks;
         float r = MathHelper.func_76126_a((float)player.field_70173_aa / 4.0F + (float)pos.func_177958_n()) * 0.2F + 0.3F;
         float g = MathHelper.func_76126_a((float)player.field_70173_aa / 3.0F + (float)pos.func_177956_o()) * 0.2F + 0.3F;
         float b = MathHelper.func_76126_a((float)player.field_70173_aa / 2.0F + (float)pos.func_177952_p()) * 0.2F + 0.8F;
         GL11.glPushMatrix();
         GL11.glDepthMask(false);
         GL11.glDisable(2929);
         GL11.glDisable(2884);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);
         GL11.glTranslated(-iPX + (double)pos.func_177958_n() + 0.5D, -iPY + (double)pos.func_177956_o() + 0.5D, -iPZ + (double)pos.func_177952_p() + 0.5D);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.33F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         if (dx) {
            GL11.glPushMatrix();
            UtilsFX.renderQuadCentered(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            UtilsFX.renderQuadCentered(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glPopMatrix();
         }

         if (dz) {
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            UtilsFX.renderQuadCentered(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            UtilsFX.renderQuadCentered(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glPopMatrix();
         }

         if (dy) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            UtilsFX.renderQuadCentered(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            UtilsFX.renderQuadCentered(this.tex, 1.0F, r, g, b, 200, 1, 1.0F);
         }

         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(3042);
         GL11.glEnable(2884);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         GL11.glPopMatrix();
      }
   }
}
