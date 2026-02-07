package thaumcraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerThaumatorium;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.crafting.TileThaumatorium;

@SideOnly(Side.CLIENT)
public class GuiThaumatorium extends GuiContainer {
   private TileThaumatorium inventory;
   private ContainerThaumatorium container = null;
   private int index = 0;
   private int lastSize = 0;
   private EntityPlayer player = null;
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_thaumatorium.png");
   int startAspect = 0;

   public GuiThaumatorium(InventoryPlayer par1InventoryPlayer, TileThaumatorium par2TileEntityFurnace) {
      super(new ContainerThaumatorium(par1InventoryPlayer, par2TileEntityFurnace));
      this.inventory = par2TileEntityFurnace;
      this.container = (ContainerThaumatorium)this.field_147002_h;
      this.container.updateRecipes();
      this.lastSize = this.container.recipes.size();
      this.player = par1InventoryPlayer.field_70458_d;
      this.refreshIndex();
   }

   void refreshIndex() {
      if (this.inventory.recipeHash != null && this.container.recipes.size() > 0) {
         for(int a = 0; a < this.container.recipes.size(); ++a) {
            if (this.inventory.recipeHash.contains(((CrucibleRecipe)this.container.recipes.get(a)).hash)) {
               this.index = a;
               break;
            }
         }
      }

      this.startAspect = 0;
   }

   protected void func_146976_a(float par1, int mx, int my) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      GL11.glEnable(3042);
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      if (this.index >= this.container.recipes.size()) {
         this.index = this.container.recipes.size() - 1;
      }

      if (this.container.recipes.size() > 0) {
         if (this.lastSize != this.container.recipes.size()) {
            this.lastSize = this.container.recipes.size();
            this.refreshIndex();
         }

         if (this.index < 0) {
            this.index = 0;
         }

         if (this.container.recipes.size() > 1) {
            if (this.index > 0) {
               this.func_73729_b(k + 128, l + 16, 192, 16, 16, 8);
            } else {
               this.func_73729_b(k + 128, l + 16, 176, 16, 16, 8);
            }

            if (this.index < this.container.recipes.size() - 1) {
               this.func_73729_b(k + 128, l + 24, 192, 24, 16, 8);
            } else {
               this.func_73729_b(k + 128, l + 24, 176, 24, 16, 8);
            }
         }

         if (((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.size() > 6) {
            if (this.startAspect > 0) {
               this.func_73729_b(k + 32, l + 40, 192, 32, 8, 16);
            } else {
               this.func_73729_b(k + 32, l + 40, 176, 32, 8, 16);
            }

            if (this.startAspect < ((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.size() - 1) {
               this.func_73729_b(k + 136, l + 40, 200, 32, 8, 16);
            } else {
               this.func_73729_b(k + 136, l + 40, 184, 32, 8, 16);
            }
         } else {
            this.startAspect = 0;
         }

         int ll;
         if (this.inventory.recipeHash != null && this.inventory.recipeHash.size() > 0) {
            int x = mx - (k + 112);
            ll = my - (l + 16);
            if (x >= 0 && ll >= 0 && x < 16 && ll < 16 || this.inventory.recipeHash.contains(((CrucibleRecipe)this.container.recipes.get(this.index)).hash)) {
               GL11.glPushMatrix();
               GL11.glEnable(3042);
               this.func_73729_b(k + 104, l + 8, 176, 96, 48, 48);
               GL11.glDisable(3042);
               GL11.glPopMatrix();
            }

            GL11.glPushMatrix();
            GL11.glEnable(3042);
            float alpha = 0.6F + MathHelper.func_76126_a((float)this.field_146297_k.field_71439_g.field_70173_aa / 5.0F) * 0.4F + 0.4F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            this.func_73729_b(k + 88, l + 16, 176, 56, 24, 24);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         }

         this.drawAspects(k, l);
         this.drawOutput(k, l, mx, my);
         if (this.inventory.maxRecipes > 1) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(k + 136), (float)(l + 33), 0.0F);
            GL11.glScalef(0.5F, 0.5F, 0.0F);
            String text = this.inventory.recipeHash.size() + "/" + this.inventory.maxRecipes;
            ll = this.field_146289_q.func_78256_a(text) / 2;
            this.field_146289_q.func_78276_b(text, -ll, 0, 16777215);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }

      GL11.glDisable(3042);
   }

   private void drawAspects(int k, int l) {
      int count = 0;
      int pos = 0;
      Aspect[] var5;
      int var6;
      int var7;
      Aspect aspect;
      if (this.inventory.recipeHash.contains(((CrucibleRecipe)this.container.recipes.get(this.index)).hash)) {
         var5 = ((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.getAspectsSortedByName();
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            aspect = var5[var7];
            if (count >= this.startAspect) {
               GL11.glPushMatrix();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               this.func_73729_b(k + 41 + 16 * pos, l + 57, 176, 8, 14, 6);
               int i1 = (int)((float)this.inventory.essentia.getAmount(aspect) / (float)((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.getAmount(aspect) * 12.0F);
               Color c = new Color(aspect.getColor());
               GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
               this.func_73729_b(k + 42 + 16 * pos, l + 58, 176, 0, i1, 4);
               GL11.glPopMatrix();
               ++pos;
            }

            ++count;
            if (count >= 6 + this.startAspect) {
               break;
            }
         }
      }

      count = 0;
      pos = 0;
      var5 = ((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.getAspectsSortedByName();
      var6 = var5.length;

      for(var7 = 0; var7 < var6; ++var7) {
         aspect = var5[var7];
         if (count >= this.startAspect) {
            UtilsFX.drawTag(k + 40 + 16 * pos, l + 40, aspect, (float)((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.getAmount(aspect), 0, (double)this.field_73735_i);
            ++pos;
         }

         ++count;
         if (count >= 6 + this.startAspect) {
            break;
         }
      }

   }

   private void drawOutput(int x, int y, int mx, int my) {
      GL11.glPushMatrix();
      boolean dull = false;
      if (this.inventory.recipeHash.size() < this.inventory.maxRecipes || this.inventory.recipeHash.contains(((CrucibleRecipe)this.container.recipes.get(this.index)).hash)) {
         dull = true;
         float alpha = 0.3F + MathHelper.func_76126_a((float)this.field_146297_k.field_71439_g.field_70173_aa / 4.0F) * 0.3F + 0.3F;
         GL11.glColor4f(0.5F, 0.5F, 0.5F, alpha);
      }

      GlStateManager.func_179094_E();
      RenderHelper.func_74520_c();
      GlStateManager.func_179140_f();
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
      GlStateManager.func_179145_e();
      this.field_146296_j.field_77023_b = 100.0F;
      this.field_146296_j.func_180450_b(((CrucibleRecipe)this.container.recipes.get(this.index)).getRecipeOutput(), x + 112, y + 16);
      this.field_146296_j.func_175030_a(this.field_146289_q, ((CrucibleRecipe)this.container.recipes.get(this.index)).getRecipeOutput(), x + 112, y + 16);
      this.field_146296_j.field_77023_b = 0.0F;
      GlStateManager.func_179140_f();
      GlStateManager.func_179121_F();
      int xx = mx - (x + 112);
      int yy = my - (y + 16);
      if (xx >= 0 && yy >= 0 && xx < 16 && yy < 16) {
         this.func_146285_a(((CrucibleRecipe)this.container.recipes.get(this.index)).getRecipeOutput(), mx, my);
      }

      GL11.glDisable(3042);
      GL11.glDisable(2896);
      GL11.glPopMatrix();
   }

   protected void func_73864_a(int mx, int my, int par3) throws IOException {
      super.func_73864_a(mx, my, par3);
      int gx = (this.field_146294_l - this.field_146999_f) / 2;
      int gy = (this.field_146295_m - this.field_147000_g) / 2;
      int x = mx - (gx + 112);
      int y = my - (gy + 16);
      if (this.container.recipes.size() > 0 && this.index >= 0 && this.index < this.container.recipes.size()) {
         if (x >= 0 && y >= 0 && x < 16 && y < 16 && (this.inventory.recipeHash.size() < this.inventory.maxRecipes || this.inventory.recipeHash.contains(((CrucibleRecipe)this.container.recipes.get(this.index)).hash))) {
            this.field_146297_k.field_71442_b.func_78756_a(this.field_147002_h.field_75152_c, this.index);
            this.playButtonSelect();
         }

         if (this.container.recipes.size() > 1) {
            if (this.index > 0) {
               x = mx - (gx + 128);
               y = my - (gy + 16);
               if (x >= 0 && y >= 0 && x < 16 && y < 8) {
                  --this.index;
                  this.playButtonClick();
               }
            }

            if (this.index < this.container.recipes.size() - 1) {
               x = mx - (gx + 128);
               y = my - (gy + 24);
               if (x >= 0 && y >= 0 && x < 16 && y < 8) {
                  ++this.index;
                  this.playButtonClick();
               }
            }
         }

         if (((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.size() > 6) {
            if (this.startAspect > 0) {
               x = mx - (gx + 32);
               y = my - (gy + 40);
               if (x >= 0 && y >= 0 && x < 8 && y < 16) {
                  --this.startAspect;
                  this.playButtonClick();
               }
            }

            if (this.startAspect < ((CrucibleRecipe)this.container.recipes.get(this.index)).aspects.size() - 1) {
               x = mx - (gx + 136);
               y = my - (gy + 40);
               if (x >= 0 && y >= 0 && x < 8 && y < 16) {
                  ++this.startAspect;
                  this.playButtonClick();
               }
            }
         }
      }

   }

   private void playButtonSelect() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.hhon, 0.3F, 1.0F);
   }

   private void playButtonClick() {
      this.field_146297_k.func_175606_aa().func_184185_a(SoundsTC.clack, 0.4F, 1.0F);
   }
}
