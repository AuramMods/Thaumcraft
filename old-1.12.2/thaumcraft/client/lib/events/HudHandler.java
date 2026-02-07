package thaumcraft.client.lib.events;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusBlockPicker;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartMedium;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.tools.ItemSanityChecker;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.world.aura.AuraChunk;

public class HudHandler {
   final ResourceLocation HUD = new ResourceLocation("thaumcraft", "textures/gui/hud.png");
   public LinkedBlockingQueue<HudHandler.KnowledgeGainTracker> knowledgeGainTrackers = new LinkedBlockingQueue();
   public static final ResourceLocation BOOK = new ResourceLocation("thaumcraft", "textures/items/thaumonomicon.png");
   public static final ResourceLocation[] KNOW_TYPE = new ResourceLocation[]{new ResourceLocation("thaumcraft", "textures/research/knowledge_theory.png"), new ResourceLocation("thaumcraft", "textures/research/knowledge_observation.png"), new ResourceLocation("thaumcraft", "textures/research/knowledge_epiphany.png")};
   float kgFade = 0.0F;
   public static AuraChunk currentAura = new AuraChunk((Chunk)null, (short)0, 0.0F, 0.0F);
   private final float VISCON = 525.0F;
   HashMap<Integer, Integer> oldvals = new HashMap();
   long nextsync = 0L;
   DecimalFormat secondsFormatter = new DecimalFormat("#######.#");
   ItemStack lastItem = null;
   int lastCount = 0;
   final ResourceLocation TAGBACK = new ResourceLocation("thaumcraft", "textures/aspects/_back.png");

   @SideOnly(Side.CLIENT)
   void renderHuds(Minecraft mc, float renderTickTime, EntityPlayer player, long time) {
      GL11.glPushMatrix();
      ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, sr.func_78327_c(), sr.func_78324_d(), 0.0D, 1000.0D, 3000.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
      int ww = sr.func_78326_a();
      int hh = sr.func_78328_b();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.renderHudsInGUI(mc, renderTickTime, player, time, ww, hh);
      if (mc.field_71415_G && Minecraft.func_71382_s()) {
         mc.field_71446_o.func_110577_a(this.HUD);
         if (RenderEventHandler.chargedItems.size() > 0) {
            this.renderChargeMeters(mc, renderTickTime, player, time, ww, hh);
         }

         if (player.func_184614_ca() != null) {
            if (player.func_184614_ca().func_77973_b() instanceof ItemThaumometer) {
               this.renderThaumometerHud(mc, renderTickTime, player, time, ww, hh);
            } else if (player.func_184614_ca().func_77973_b() instanceof ICaster) {
               this.renderCastingWandHud(mc, renderTickTime, player, time, player.func_184614_ca());
            } else if (player.func_184614_ca().func_77973_b() instanceof ItemSanityChecker) {
               this.renderSanityHud(mc, renderTickTime, player, time);
            }
         }
      }

      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   void renderHudsInGUI(Minecraft mc, float renderTickTime, EntityPlayer player, long time, int ww, int hh) {
      if (this.kgFade > 0.0F) {
         this.renderKnowledgeGains(mc, renderTickTime, player, time, ww, hh);
      }

   }

   @SideOnly(Side.CLIENT)
   void renderKnowledgeGains(Minecraft mc, float renderTickTime, EntityPlayer player, long time, int ww, int hh) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.kgFade / 40.0F);
      mc.field_71446_o.func_110577_a(BOOK);
      UtilsFX.drawTexturedQuadFull((float)(ww - 17), (float)(hh - 17), -90.0D);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      LinkedBlockingQueue<HudHandler.KnowledgeGainTracker> temp = new LinkedBlockingQueue();

      for(int a = 0; !this.knowledgeGainTrackers.isEmpty(); ++a) {
         HudHandler.KnowledgeGainTracker current = (HudHandler.KnowledgeGainTracker)this.knowledgeGainTrackers.poll();
         if (current != null) {
            mc.field_71446_o.func_110577_a(KNOW_TYPE[current.type.ordinal()]);
            Random rand = new Random(current.seed);
            GL11.glPushMatrix();
            float s = 16.0F;
            float x = (float)(ww / 4 + rand.nextInt(32));
            float y = (float)(hh / 3 + rand.nextInt(32));
            float wot = 0.0F;
            float wot2;
            float m;
            float wot3;
            if ((float)current.progress < (float)current.max * 0.66F) {
               wot2 = ((float)current.progress - renderTickTime) / ((float)current.max * 0.66F);
               s *= wot2;
               m = (float)Math.sin((double)wot2 * 3.141592653589793D - 1.5707963267948966D) * 0.5F + 0.5F;
               y *= m;
               wot3 = (float)Math.sin((double)m * 3.141592653589793D * 0.5D);
               x *= wot3;
            } else {
               wot = (float)(current.max - current.progress) + renderTickTime;
               wot2 = wot / ((float)current.max * 0.33F);
               m = (float)Math.sin((double)wot2 * 3.141592653589793D * 2.0D - 1.5707963267948966D) * 0.5F + 1.5F;
               if ((double)wot2 < 0.5D) {
                  s *= wot2 * 2.0F;
               }

               s *= m;
            }

            wot2 = (float)(ww - 12 + rand.nextInt(8)) - x;
            m = (float)(hh - 12 + rand.nextInt(8)) - y;
            GL11.glTranslatef(wot2, m, (float)(-80 + a));
            GL11.glRotatef((float)(84 + rand.nextInt(12)), 0.0F, 0.0F, -1.0F);
            UtilsFX.renderQuadCentered(1, 1, 0, s, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
            if (current.category != null) {
               mc.field_71446_o.func_110577_a(current.category.icon);
               GL11.glTranslatef(0.0F, 0.0F, 1.0F);
               UtilsFX.renderQuadCentered(1, 1, 0, s * 0.75F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
            }

            float m2;
            float size;
            float r;
            float g;
            float b;
            if ((float)current.progress > (float)current.max * 0.9F) {
               wot3 = wot / ((float)current.max * 0.1F);
               m2 = (float)Math.sin((double)wot3 * 3.141592653589793D * 2.0D - 1.5707963267948966D) * 0.25F + 0.25F;
               size = 64.0F * m2;
               GL11.glRotatef((float)rand.nextInt(360), 0.0F, 0.0F, -1.0F);
               mc.field_71446_o.func_110577_a(ParticleEngine.particleTexture);
               r = (float)MathHelper.func_76136_a(rand, 255, 255) / 255.0F;
               g = (float)MathHelper.func_76136_a(rand, 189, 255) / 255.0F;
               b = (float)MathHelper.func_76136_a(rand, 64, 255) / 255.0F;
               UtilsFX.renderQuadCentered(64, 64, 320 + rand.nextInt(16), size, r, g, b, 200, 1, 1.0F);
            }

            if ((float)current.progress < (float)current.max * 0.1F) {
               wot3 = 1.0F - ((float)current.progress - renderTickTime) / ((float)current.max * 0.1F);
               m2 = (float)Math.sin((double)wot3 * 3.141592653589793D * 2.0D - 1.5707963267948966D) * 0.25F + 0.25F;
               size = 32.0F * m2;
               GL11.glRotatef((float)rand.nextInt(360), 0.0F, 0.0F, -1.0F);
               mc.field_71446_o.func_110577_a(ParticleEngine.particleTexture);
               r = (float)MathHelper.func_76136_a(rand, 255, 255) / 255.0F;
               g = (float)MathHelper.func_76136_a(rand, 189, 255) / 255.0F;
               b = (float)MathHelper.func_76136_a(rand, 64, 255) / 255.0F;
               UtilsFX.renderQuadCentered(64, 64, 320 + rand.nextInt(16), size, r, g, b, 200, 1, 1.0F);
            }

            temp.offer(current);
            GL11.glPopMatrix();
         }
      }

      while(!temp.isEmpty()) {
         this.knowledgeGainTrackers.offer(temp.poll());
      }

   }

   @SideOnly(Side.CLIENT)
   void renderThaumometerHud(Minecraft mc, float partialTicks, EntityPlayer player, long time, int ww, int hh) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float base = MathHelper.func_76131_a((float)currentAura.getBase() / 525.0F, 0.0F, 1.0F);
      float vis = MathHelper.func_76131_a(currentAura.getVis() / 525.0F, 0.0F, 1.0F);
      float flux = MathHelper.func_76131_a(currentAura.getFlux() / 525.0F, 0.0F, 1.0F);
      float count = (float)Minecraft.func_71410_x().func_175606_aa().field_70173_aa + partialTicks;
      float count2 = (float)Minecraft.func_71410_x().func_175606_aa().field_70173_aa / 3.0F + partialTicks;
      float start;
      if (flux + vis > 1.0F) {
         start = 1.0F / (flux + vis);
         base *= start;
         vis *= start;
         flux *= start;
      }

      start = 10.0F + (1.0F - vis) * 64.0F;
      String msg;
      if (vis > 0.0F) {
         GL11.glPushMatrix();
         GL11.glColor4f(0.7F, 0.4F, 0.9F, 1.0F);
         GL11.glTranslated(5.0D, (double)start, 0.0D);
         GL11.glScaled(1.0D, (double)vis, 1.0D);
         UtilsFX.drawTexturedQuad(0.0F, 0.0F, 88.0F, 56.0F, 8.0F, 64.0F, -90.0D);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glBlendFunc(770, 1);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
         GL11.glTranslated(5.0D, (double)start, 0.0D);
         UtilsFX.drawTexturedQuad(0.0F, 0.0F, 96.0F, 56.0F + count % 64.0F, 8.0F, vis * 64.0F, -90.0D);
         GL11.glBlendFunc(770, 771);
         GL11.glPopMatrix();
         if (player.func_70093_af()) {
            GL11.glPushMatrix();
            GL11.glTranslated(16.0D, (double)start, 0.0D);
            GL11.glScaled(0.5D, 0.5D, 0.5D);
            msg = currentAura.getVis() + "";
            mc.field_71456_v.func_73731_b(mc.field_71466_p, msg, 0, 0, 15641343);
            GL11.glPopMatrix();
            mc.field_71446_o.func_110577_a(this.HUD);
         }
      }

      if (flux > 0.0F) {
         start = 10.0F + (1.0F - flux - vis) * 64.0F;
         GL11.glPushMatrix();
         GL11.glColor4f(0.25F, 0.1F, 0.3F, 1.0F);
         GL11.glTranslated(5.0D, (double)start, 0.0D);
         GL11.glScaled(1.0D, (double)flux, 1.0D);
         UtilsFX.drawTexturedQuad(0.0F, 0.0F, 88.0F, 56.0F, 8.0F, 64.0F, -90.0D);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glBlendFunc(770, 1);
         GL11.glColor4f(0.7F, 0.4F, 1.0F, 0.5F);
         GL11.glTranslated(5.0D, (double)start, 0.0D);
         UtilsFX.drawTexturedQuad(0.0F, 0.0F, 104.0F, 120.0F - count2 % 64.0F, 8.0F, flux * 64.0F, -90.0D);
         GL11.glBlendFunc(770, 771);
         GL11.glPopMatrix();
         if (player.func_70093_af()) {
            GL11.glPushMatrix();
            GL11.glTranslated(16.0D, (double)(start - 4.0F), 0.0D);
            GL11.glScaled(0.5D, 0.5D, 0.5D);
            msg = currentAura.getFlux() + "";
            mc.field_71456_v.func_73731_b(mc.field_71466_p, msg, 0, 0, 11145659);
            GL11.glPopMatrix();
            mc.field_71446_o.func_110577_a(this.HUD);
         }
      }

      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.drawTexturedQuad(1.0F, 1.0F, 72.0F, 48.0F, 16.0F, 80.0F, -90.0D);
      GL11.glPopMatrix();
      start = 8.0F + (1.0F - base) * 64.0F;
      GL11.glPushMatrix();
      UtilsFX.drawTexturedQuad(2.0F, start, 117.0F, 61.0F, 14.0F, 5.0F, -90.0D);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   void renderSanityHud(Minecraft mc, Float partialTicks, EntityPlayer player, long time) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.drawTexturedQuad(1.0F, 1.0F, 152.0F, 0.0F, 20.0F, 76.0F, -90.0D);
      int p = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT);
      int s = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL);
      int t = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY);
      float tw = (float)(p + s + t);
      float mod = 1.0F;
      if (tw > 100.0F) {
         mod = 100.0F / tw;
         tw = 100.0F;
      }

      int gap = (int)((100.0F - tw) / 100.0F * 48.0F);
      int wt = (int)((float)t / 100.0F * 48.0F * mod);
      int ws = (int)((float)s / 100.0F * 48.0F * mod);
      if (t > 0) {
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 0.5F, 1.0F, 1.0F);
         UtilsFX.drawTexturedQuad(7.0F, (float)(21 + gap), 200.0F, (float)gap, 8.0F, (float)(wt + gap), -90.0D);
         GL11.glPopMatrix();
      }

      if (s > 0) {
         GL11.glPushMatrix();
         GL11.glColor4f(0.75F, 0.0F, 0.75F, 1.0F);
         UtilsFX.drawTexturedQuad(7.0F, (float)(21 + wt + gap), 200.0F, (float)(wt + gap), 8.0F, (float)(wt + ws + gap), -90.0D);
         GL11.glPopMatrix();
      }

      if (p > 0) {
         GL11.glPushMatrix();
         GL11.glColor4f(0.5F, 0.0F, 0.5F, 1.0F);
         UtilsFX.drawTexturedQuad(7.0F, (float)(21 + wt + ws + gap), 200.0F, (float)(wt + ws + gap), 8.0F, 48.0F, -90.0D);
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.drawTexturedQuad(1.0F, 1.0F, 176.0F, 0.0F, 20.0F, 76.0F, -90.0D);
      GL11.glPopMatrix();
      if (tw >= 100.0F) {
         GL11.glPushMatrix();
         UtilsFX.drawTexturedQuad(1.0F, 1.0F, 216.0F, 0.0F, 20.0F, 16.0F, -90.0D);
         GL11.glPopMatrix();
      }

   }

   @SideOnly(Side.CLIENT)
   void renderChargeMeters(Minecraft mc, float renderTickTime, EntityPlayer player, long time, int ww, int hh) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int start = 0;
      int total = 0;
      if (Config.chargeBarPos == 2) {
         Iterator var10 = RenderEventHandler.chargedItems.values().iterator();

         while(var10.hasNext()) {
            RenderEventHandler.ChargeEntry ce = (RenderEventHandler.ChargeEntry)var10.next();
            if (ce.time >= time - 10000L) {
               ++total;
            }
         }

         total *= 10;
      }

      float shift = 1.0F;
      boolean drewItem = false;
      Iterator var12 = RenderEventHandler.chargedItems.values().iterator();

      while(var12.hasNext()) {
         RenderEventHandler.ChargeEntry ce = (RenderEventHandler.ChargeEntry)var12.next();
         float level = ce.charge * 16.0F;
         shift = 1.0F;
         if (level > 0.0F) {
            if (ce.time < time - 10000L) {
               continue;
            }

            if (ce.time > time - 500L) {
               shift = (float)(time - ce.time) / 500.0F;
            }

            if (ce.time < time - 9500L) {
               shift = 1.0F - (float)(time - ce.time - 9500L) / 500.0F;
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2929);
         GL11.glDepthMask(false);
         GL11.glDisable(3008);
         if (level == 0.0F && player.field_70173_aa / 10 % 2 == 0) {
            GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
         }

         int x = 0;
         int y = 0;
         switch(Config.chargeBarPos) {
         case 0:
            x = (int)(-20.0F + 22.0F * shift);
            y = hh / 2 + 17 + start * 20;
            break;
         case 1:
            x = ww - (int)(-2.0F + 22.0F * shift);
            y = hh / 2 + 17 + start * 20;
            break;
         case 2:
            x = ww / 2 - 18 - start * 20 + total;
            y = (int)(-4.0F + 22.0F * shift);
         }

         UtilsFX.drawTexturedQuad((float)x, (float)y, 144.0F, 99.0F, 16.0F, 3.0F, -91.0D);
         UtilsFX.drawTexturedQuad((float)x, (float)y, 144.0F, 96.0F, (float)Math.round(level), 3.0F, -91.0D);
         if (ce.diff != 0) {
            GL11.glBlendFunc(770, 1);
            float f = (float)(time - ce.tickTime) / 1000.0F;
            int d = Math.round(f * level);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F - f);
            if (ce.diff > 0) {
               UtilsFX.drawTexturedQuad((float)(x + d - 3), (float)(y - 2), 160.0F, 94.0F, 5.0F, 7.0F, -91.0D);
            } else {
               UtilsFX.drawTexturedQuad((float)(x - d - 5 + Math.round(level)), (float)(y - 2), 160.0F, 94.0F, 5.0F, 7.0F, -91.0D);
            }

            GL11.glBlendFunc(770, 771);
         }

         GL11.glDepthMask(true);
         GL11.glEnable(2929);
         GL11.glEnable(3008);
         UtilsFX.renderItemInGUI(x, y - 16, 100, ce.item);
         RenderHelper.func_74520_c();
         GlStateManager.func_179140_f();
         drewItem = true;
         ++start;
         if (hh / 2 + 50 + start * 21 > hh) {
            break;
         }
      }

      if (drewItem) {
         mc.field_71446_o.func_110577_a(this.HUD);
      }

   }

   @SideOnly(Side.CLIENT)
   void renderCastingWandHud(Minecraft mc, float partialTicks, EntityPlayer player, long time, ItemStack wandstack) {
      ICaster wand = (ICaster)wandstack.func_77973_b();
      if (this.oldvals.get(player.field_71071_by.field_70461_c) == null) {
         this.oldvals.put(player.field_71071_by.field_70461_c, RechargeHelper.getCharge(wandstack));
      } else if (this.nextsync <= time) {
         this.oldvals.put(player.field_71071_by.field_70461_c, RechargeHelper.getCharge(wandstack));
         this.nextsync = time + 1000L;
      }

      short short1 = 240;
      short short2 = 240;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)short1 / 1.0F, (float)short2 / 1.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, sr.func_78327_c(), sr.func_78324_d(), 0.0D, 1000.0D, 3000.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      int l = sr.func_78328_b();
      int dailLocation = Config.dialBottom ? l - 32 : 0;
      GL11.glTranslatef(0.0F, (float)dailLocation, -2000.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      mc.field_71446_o.func_110577_a(this.HUD);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      UtilsFX.drawTexturedQuad(0.0F, 0.0F, 0.0F, 0.0F, 64.0F, 64.0F, -90.0D);
      GL11.glPopMatrix();
      GL11.glTranslatef(16.0F, 16.0F, 0.0F);
      int max = currentAura.getBase();
      int amt = (int)currentAura.getVis();
      ItemFocus focus = wand.getFocus(wandstack);
      ItemStack focusStack = wand.getFocusStack(wandstack);
      GL11.glPushMatrix();
      if (!Config.dialBottom) {
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      }

      GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(16.0F, -10.0F, 0.0F);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int loc = (int)(30.0F * (float)amt / (float)max);
      GL11.glPushMatrix();
      Color ac = new Color(Aspect.ENERGY.getColor());
      GL11.glColor4f((float)ac.getRed() / 255.0F, (float)ac.getGreen() / 255.0F, (float)ac.getBlue() / 255.0F, 0.8F);
      UtilsFX.drawTexturedQuad(-4.0F, (float)(35 - loc), 104.0F, 0.0F, 8.0F, (float)loc, -90.0D);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      UtilsFX.drawTexturedQuad(-8.0F, -3.0F, 72.0F, 0.0F, 16.0F, 42.0F, -90.0D);
      GL11.glPopMatrix();
      int sh = 0;
      if ((Integer)this.oldvals.get(player.field_71071_by.field_70461_c) > amt) {
         GL11.glPushMatrix();
         UtilsFX.drawTexturedQuad(-4.0F, (float)(-8 - sh), 128.0F, 0.0F, 8.0F, 8.0F, -90.0D);
         GL11.glPopMatrix();
      } else if ((Integer)this.oldvals.get(player.field_71071_by.field_70461_c) < amt) {
         GL11.glPushMatrix();
         UtilsFX.drawTexturedQuad(-4.0F, (float)(-8 - sh), 120.0F, 0.0F, 8.0F, 8.0F, -90.0D);
         GL11.glPopMatrix();
      }

      if (player.func_70093_af()) {
         GL11.glPushMatrix();
         GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
         String msg = amt + "";
         mc.field_71456_v.func_73731_b(mc.field_71466_p, msg, -32, -4, 16777215);
         GL11.glPopMatrix();
         if (focus != null && focus.getVisCost(focusStack) > 0.0F) {
            float mod = wand.getConsumptionModifier(wandstack, player, false);
            GL11.glPushMatrix();
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            msg = "" + this.secondsFormatter.format((double)(focus.getVisCost(focusStack) * mod));
            mc.field_71456_v.func_73731_b(mc.field_71466_p, msg, 8, -4, 16777215);
            GL11.glPopMatrix();
         }

         mc.field_71446_o.func_110577_a(this.HUD);
      }

      GL11.glPopMatrix();
      if (focus != null) {
         ItemStack picked = null;
         FocusCore core = ItemFocus.getCore(focusStack);
         Iterator var22 = core.partsRaw.values().iterator();

         while(var22.hasNext()) {
            IFocusPart part = (IFocusPart)var22.next();
            if (part instanceof IFocusBlockPicker) {
               picked = wand.getPickedBlock(player.field_71071_by.func_70448_g());
               if (picked != null) {
                  this.renderWandTradeHud(partialTicks, player, time, picked);
               }
               break;
            }
         }

         if (picked == null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-24.0F, -24.0F, 90.0F);
            RenderHelper.func_74520_c();
            GL11.glDisable(2896);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            GL11.glEnable(2896);

            try {
               mc.func_175599_af().func_180450_b(wand.getFocusStack(wandstack), 16, 16);
            } catch (Exception var33) {
            }

            GL11.glDisable(2896);
            GL11.glPopMatrix();
         }

         if (core.getFinalCastMethod() == IFocusPartMedium.EnumFocusCastMethod.CHARGE && player.func_184605_cv() > 0) {
            mc.field_71446_o.func_110577_a(this.HUD);
            float maxAt = (float)focus.getActivationTime(focusStack);
            float count = (float)player.func_184612_cw();
            if (count > maxAt) {
               count = maxAt;
            }

            float progress = count / maxAt;
            double rad = (double)(100.0F + progress * 80.0F) * 0.017453292519943D;
            float charge = (float)thaumcraft.codechicken.lib.math.MathHelper.clip(2.0D + 1.5D * Math.tan(rad) / 5.671D, 0.5D, 2.0D) - 0.5F;
            int r = Math.round(charge * 60.0F);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 150.0F);

            for(int a = 0; a < r; ++a) {
               float rr = 0.0F;
               float gg = 0.0F;
               float bb = 0.0F;
               if (r == 90) {
                  rr = 0.54F;
                  gg = 0.98F;
                  bb = 0.96F;
               } else if (a < 20) {
                  rr = 1.0F;
                  gg = (float)a / 20.0F;
                  bb = 0.25F * ((float)a / 20.0F);
               } else {
                  rr = 1.0F - (float)(a - 20) / 40.0F;
                  gg = 1.0F;
                  bb = 0.25F * (1.0F - (float)(a - 20) / 40.0F);
               }

               GL11.glPushMatrix();
               GL11.glColor4f(rr, gg, bb, 0.5F);
               GL11.glRotatef((float)(-a * 4 + 4), 0.0F, 0.0F, 1.0F);
               GL11.glTranslated(0.0D, 7.0D, 0.0D);
               UtilsFX.drawTexturedQuad(0.0F, 0.0F, 176.0F, 94.0F, 3.0F, 7.0F, 0.0D);
               GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
         }
      }

      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public void renderWandTradeHud(float partialTicks, EntityPlayer player, long time, ItemStack picked) {
      if (picked != null) {
         Minecraft mc = Minecraft.func_71410_x();
         int amount = this.lastCount;
         int sw;
         int a;
         if (this.lastItem == null || player.field_71071_by.field_70459_e || !picked.func_77969_a(this.lastItem)) {
            amount = 0;
            ItemStack[] var8 = player.field_71071_by.field_70462_a;
            sw = var8.length;

            for(a = 0; a < sw; ++a) {
               ItemStack is = var8[a];
               if (is != null && is.func_77969_a(picked)) {
                  amount += is.field_77994_a;
               }
            }

            this.lastItem = picked;
            player.field_71071_by.field_70459_e = false;
         }

         this.lastCount = amount;
         GL11.glPushMatrix();
         RenderHelper.func_74520_c();
         GL11.glDisable(2896);
         GL11.glEnable(32826);
         GL11.glEnable(2903);
         GL11.glEnable(2896);

         try {
            mc.func_175599_af().func_180450_b(picked, -8, -8);
         } catch (Exception var12) {
         }

         GL11.glDisable(2896);
         GL11.glPushMatrix();
         String am = "" + amount;
         sw = mc.field_71466_p.func_78256_a(am);
         GL11.glTranslatef(0.0F, (float)(-mc.field_71466_p.field_78288_b), 500.0F);
         GL11.glScalef(0.5F, 0.5F, 0.5F);

         for(a = -1; a <= 1; ++a) {
            for(int b = -1; b <= 1; ++b) {
               if ((a == 0 || b == 0) && (a != 0 || b != 0)) {
                  mc.field_71466_p.func_78276_b(am, a + 16 - sw, b + 24, 0);
               }
            }
         }

         mc.field_71466_p.func_78276_b(am, 16 - sw, 24, 16777215);
         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }
   }

   public void renderAspectsInGui(GuiContainer gui, EntityPlayer player, ItemStack stack, int sd, int sx, int sy) {
      AspectList tags = ThaumcraftCraftingManager.getObjectTags(stack);
      if (tags != null) {
         GL11.glPushMatrix();
         int x = false;
         int y = false;
         int index = 0;
         if (tags.size() > 0) {
            Aspect[] var11 = tags.getAspectsSortedByAmount();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               Aspect tag = var11[var13];
               if (tag != null) {
                  int x = sx + index * 18;
                  int y = sy + sd - 16;
                  UtilsFX.drawTag(x, y, tag, (float)tags.getAmount(tag), 0, (double)gui.field_73735_i);
                  ++index;
               }
            }
         }

         GL11.glPopMatrix();
      }
   }

   private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3, int par4, int par5) {
      par2 -= par4;
      par3 -= par5;
      return par2 >= par1Slot.field_75223_e - 1 && par2 < par1Slot.field_75223_e + 16 + 1 && par3 >= par1Slot.field_75221_f - 1 && par3 < par1Slot.field_75221_f + 16 + 1;
   }

   public static class KnowledgeGainTracker {
      IPlayerKnowledge.EnumKnowledgeType type;
      ResearchCategory category;
      int progress;
      int max;
      long seed;

      public KnowledgeGainTracker(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category, int progress, long seed) {
         this.type = type;
         this.category = category;
         if (type == IPlayerKnowledge.EnumKnowledgeType.THEORY) {
            progress += 10;
         }

         if (type == IPlayerKnowledge.EnumKnowledgeType.EPIPHANY) {
            progress += 20;
         }

         this.progress = progress;
         this.max = progress;
         this.seed = seed;
      }
   }
}
