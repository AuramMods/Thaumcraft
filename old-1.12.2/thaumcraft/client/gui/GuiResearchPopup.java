package thaumcraft.client.gui;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.utils.InventoryUtils;

@SideOnly(Side.CLIENT)
public class GuiResearchPopup extends Gui {
   private Minecraft theGame;
   private int windowWidth;
   private int windowHeight;
   private ArrayList<ResearchEntry> theResearch = new ArrayList();
   private long researchTime;
   private static final ResourceLocation texture = new ResourceLocation("textures/gui/achievement/achievement_background.png");

   public GuiResearchPopup(Minecraft par1Minecraft) {
      this.theGame = par1Minecraft;
   }

   public void queueResearchInformation(ResearchEntry research) {
      if (this.researchTime == 0L) {
         this.researchTime = Minecraft.func_71386_F();
      }

      this.theResearch.add(research);
   }

   private void updateResearchWindowScale() {
      GL11.glViewport(0, 0, this.theGame.field_71443_c, this.theGame.field_71440_d);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      this.windowWidth = this.theGame.field_71443_c;
      this.windowHeight = this.theGame.field_71440_d;
      ScaledResolution var1 = new ScaledResolution(Minecraft.func_71410_x());
      this.windowWidth = var1.func_78326_a();
      this.windowHeight = var1.func_78328_b();
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, (double)this.windowWidth, (double)this.windowHeight, 0.0D, 1000.0D, 3000.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
   }

   public void updateResearchWindow() {
      if (this.theResearch.size() > 0 && this.researchTime != 0L) {
         double var1 = (double)(Minecraft.func_71386_F() - this.researchTime) / 3000.0D;
         if (!(var1 < 0.0D) && !(var1 > 1.0D)) {
            this.updateResearchWindowScale();
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            double var3 = var1 * 2.0D;
            if (var3 > 1.0D) {
               var3 = 2.0D - var3;
            }

            var3 *= 4.0D;
            var3 = 1.0D - var3;
            if (var3 < 0.0D) {
               var3 = 0.0D;
            }

            var3 *= var3;
            var3 *= var3;
            int var5 = 0;
            int var6 = 0 - (int)(var3 * 36.0D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553);
            this.theGame.func_110434_K().func_110577_a(texture);
            GL11.glDisable(2896);
            this.func_73729_b(var5, var6, 96, 202, 160, 32);
            this.theGame.field_71466_p.func_78276_b("Research Completed!", var5 + 30, var6 + 7, -256);
            int offset = this.theGame.field_71466_p.func_78256_a(((ResearchEntry)this.theResearch.get(0)).getLocalizedName());
            if (offset <= 125) {
               this.theGame.field_71466_p.func_78276_b(((ResearchEntry)this.theResearch.get(0)).getLocalizedName(), var5 + 30, var6 + 18, -1);
            } else {
               float vv = 125.0F / (float)offset;
               GL11.glPushMatrix();
               GL11.glTranslatef((float)(var5 + 30), (float)(var6 + 16) + 2.0F / vv, 0.0F);
               GL11.glScalef(vv, vv, vv);
               this.theGame.field_71466_p.func_78276_b(((ResearchEntry)this.theResearch.get(0)).getLocalizedName(), 0, 0, -1);
               GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            RenderHelper.func_74520_c();
            GL11.glDisable(2896);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            GL11.glEnable(2896);
            if (((ResearchEntry)this.theResearch.get(0)).getIcons() != null && ((ResearchEntry)this.theResearch.get(0)).getIcons().length > 0) {
               int idx = (int)(System.currentTimeMillis() / 1000L % (long)((ResearchEntry)this.theResearch.get(0)).getIcons().length);
               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               if (((ResearchEntry)this.theResearch.get(0)).getIcons()[idx] instanceof ResourceLocation) {
                  Minecraft.func_71410_x().field_71446_o.func_110577_a((ResourceLocation)((ResearchEntry)this.theResearch.get(0)).getIcons()[idx]);
                  UtilsFX.drawTexturedQuadFull((float)(var5 + 8), (float)(var6 + 8), (double)this.field_73735_i);
               } else if (((ResearchEntry)this.theResearch.get(0)).getIcons()[idx] instanceof ItemStack) {
                  RenderHelper.func_74520_c();
                  GL11.glDisable(2896);
                  GL11.glEnable(32826);
                  GL11.glEnable(2903);
                  GL11.glEnable(2896);
                  Minecraft.func_71410_x().func_175599_af().func_180450_b(InventoryUtils.cycleItemStack(((ResearchEntry)this.theResearch.get(0)).getIcons()[idx]), var5 + 8, var6 + 8);
                  GL11.glDisable(2896);
                  GL11.glDepthMask(true);
                  GL11.glEnable(2929);
               }

               GL11.glDisable(3042);
               GL11.glPopMatrix();
            }

            GL11.glDisable(2896);
         } else {
            this.theResearch.remove(0);
            if (this.theResearch.size() > 0) {
               this.researchTime = Minecraft.func_71386_F();
            } else {
               this.researchTime = 0L;
            }
         }
      }

   }
}
