package thaumcraft.client.renderers.models.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class ModelRendererTaintacle extends ModelRenderer {
   private int textureOffsetX;
   private int textureOffsetY;
   private boolean compiled;
   private int displayList;
   private ModelBase baseModel;

   public ModelRendererTaintacle(ModelBase par1ModelBase) {
      super(par1ModelBase);
   }

   public ModelRendererTaintacle(ModelBase par1ModelBase, int par2, int par3) {
      this(par1ModelBase);
      this.func_78784_a(par2, par3);
   }

   @SideOnly(Side.CLIENT)
   public void render(float par1, float scale) {
      if (!this.field_78807_k && this.field_78806_j) {
         if (!this.compiled) {
            this.compileDisplayList(par1);
         }

         GL11.glTranslatef(this.field_82906_o, this.field_82908_p, this.field_82907_q);
         int i;
         if (this.field_78795_f == 0.0F && this.field_78796_g == 0.0F && this.field_78808_h == 0.0F) {
            if (this.field_78800_c == 0.0F && this.field_78797_d == 0.0F && this.field_78798_e == 0.0F) {
               GL11.glCallList(this.displayList);
               if (this.field_78805_m != null) {
                  for(i = 0; i < this.field_78805_m.size(); ++i) {
                     GL11.glPushMatrix();
                     GL11.glScalef(scale, scale, scale);
                     ((ModelRendererTaintacle)this.field_78805_m.get(i)).render(par1, scale);
                     GL11.glPopMatrix();
                  }
               }
            } else {
               GL11.glTranslatef(this.field_78800_c * par1, this.field_78797_d * par1, this.field_78798_e * par1);
               GL11.glCallList(this.displayList);
               if (this.field_78805_m != null) {
                  for(i = 0; i < this.field_78805_m.size(); ++i) {
                     GL11.glPushMatrix();
                     GL11.glScalef(scale, scale, scale);
                     ((ModelRendererTaintacle)this.field_78805_m.get(i)).render(par1, scale);
                     GL11.glPopMatrix();
                  }
               }

               GL11.glTranslatef(-this.field_78800_c * par1, -this.field_78797_d * par1, -this.field_78798_e * par1);
            }
         } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.field_78800_c * par1, this.field_78797_d * par1, this.field_78798_e * par1);
            if (this.field_78808_h != 0.0F) {
               GL11.glRotatef(this.field_78808_h * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.field_78796_g != 0.0F) {
               GL11.glRotatef(this.field_78796_g * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.field_78795_f != 0.0F) {
               GL11.glRotatef(this.field_78795_f * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glCallList(this.displayList);
            if (this.field_78805_m != null) {
               for(i = 0; i < this.field_78805_m.size(); ++i) {
                  GL11.glPushMatrix();
                  GL11.glScalef(scale, scale, scale);
                  ((ModelRendererTaintacle)this.field_78805_m.get(i)).render(par1, scale);
                  GL11.glPopMatrix();
               }
            }

            GL11.glPopMatrix();
         }

         GL11.glTranslatef(-this.field_82906_o, -this.field_82908_p, -this.field_82907_q);
      }

   }

   @SideOnly(Side.CLIENT)
   private void compileDisplayList(float par1) {
      this.displayList = GLAllocation.func_74526_a(1);
      GL11.glNewList(this.displayList, 4864);
      Tessellator tessellator = Tessellator.func_178181_a();

      for(int i = 0; i < this.field_78804_l.size(); ++i) {
         ((ModelBox)this.field_78804_l.get(i)).func_178780_a(tessellator.func_178180_c(), par1);
      }

      GL11.glEndList();
      this.compiled = true;
   }
}
