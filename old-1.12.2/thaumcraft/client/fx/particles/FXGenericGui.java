package thaumcraft.client.fx.particles;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXGenericGui extends FXGeneric {
   public FXGenericGui(World world, double x, double y, double z) {
      super(world, x, y, z);
   }

   public FXGenericGui(World world, double x, double y, double z, double xx, double yy, double zz) {
      super(world, x, y, z, xx, yy, zz);
   }

   public void draw(VertexBuffer wr, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
      float tx1 = (float)this.field_94054_b / (float)this.gridSize;
      float tx2 = tx1 + 1.0F / (float)this.gridSize;
      float ty1 = (float)this.field_94055_c / (float)this.gridSize;
      float ty2 = ty1 + 1.0F / (float)this.gridSize;
      float ts = 0.1F * this.field_70544_f;
      if (this.field_187119_C != null) {
         tx1 = this.field_187119_C.func_94209_e();
         tx2 = this.field_187119_C.func_94212_f();
         ty1 = this.field_187119_C.func_94206_g();
         ty2 = this.field_187119_C.func_94210_h();
      }

      float fs;
      if (this.flipped) {
         fs = tx1;
         tx1 = tx2;
         tx2 = fs;
      }

      fs = MathHelper.func_76131_a(((float)this.field_70546_d + partialTicks) / (float)this.field_70547_e, 0.0F, 1.0F);
      float pr = this.field_70552_h + (this.dr - this.field_70552_h) * fs;
      float pg = this.field_70553_i + (this.dg - this.field_70553_i) * fs;
      float pb = this.field_70551_j + (this.db - this.field_70551_j) * fs;
      int i = this.func_189214_a(partialTicks);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      float f5 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks);
      float f6 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks);
      float f7 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks);
      GL11.glPushMatrix();
      GL11.glTranslatef(f5, f6, -90.0F + f7);
      if (this.angled) {
         GL11.glRotatef(-this.angleYaw + 90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.anglePitch + 90.0F, 1.0F, 0.0F, 0.0F);
      }

      if (this.field_190014_F != 0.0F) {
         float f8 = this.field_190014_F + (this.field_190014_F - this.field_190015_G) * partialTicks;
         GL11.glRotated((double)f8 * 57.29577951308232D, 0.0D, 0.0D, 1.0D);
      }

      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
      wr.func_181662_b((double)(-ts), (double)(-ts), 0.0D).func_187315_a((double)tx2, (double)ty2).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(-ts), (double)ts, 0.0D).func_187315_a((double)tx2, (double)ty1).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)ts, (double)ts, 0.0D).func_187315_a((double)tx1, (double)ty1).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)ts, (double)(-ts), 0.0D).func_187315_a((double)tx1, (double)ty2).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPopMatrix();
   }
}
