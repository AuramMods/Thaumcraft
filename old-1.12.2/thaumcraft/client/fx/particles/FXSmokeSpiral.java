package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXSmokeSpiral extends Particle {
   private float radius = 1.0F;
   private int start = 0;
   private int miny = 0;

   public FXSmokeSpiral(World world, double d, double d1, double d2, float radius, int start, int miny) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      this.field_70545_g = -0.01F;
      this.field_187129_i = this.field_187130_j = this.field_187131_k = 0.0D;
      this.field_70544_f *= 1.0F;
      this.field_70547_e = 20 + world.field_73012_v.nextInt(10);
      this.func_187115_a(0.01F, 0.01F);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.radius = radius;
      this.start = start;
      this.miny = miny;
   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F * this.field_82339_as);
      int particle = (int)(1.0F + (float)this.field_70546_d / (float)this.field_70547_e * 4.0F);
      float r1 = (float)this.start + 720.0F * (((float)this.field_70546_d + f) / (float)this.field_70547_e);
      float r2 = 90.0F - 180.0F * (((float)this.field_70546_d + f) / (float)this.field_70547_e);
      float mX = -MathHelper.func_76126_a(r1 / 180.0F * 3.1415927F) * MathHelper.func_76134_b(r2 / 180.0F * 3.1415927F);
      float mZ = MathHelper.func_76134_b(r1 / 180.0F * 3.1415927F) * MathHelper.func_76134_b(r2 / 180.0F * 3.1415927F);
      float mY = -MathHelper.func_76126_a(r2 / 180.0F * 3.1415927F);
      mX *= this.radius;
      mY *= this.radius;
      mZ *= this.radius;
      float var8 = (float)(particle % 16) / 64.0F;
      float var9 = var8 + 0.015625F;
      float var10 = (float)(particle / 16) / 64.0F;
      float var11 = var10 + 0.015625F;
      float var12 = 0.15F * this.field_70544_f;
      float var13 = (float)(this.field_187126_f + (double)mX - field_70556_an);
      float var14 = (float)(Math.max(this.field_187127_g + (double)mY, (double)((float)this.miny + 0.1F)) - field_70554_ao);
      float var15 = (float)(this.field_187128_h + (double)mZ - field_70555_ap);
      float var16 = 1.0F;
      int i = this.func_189214_a(f);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181662_b((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12)).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.66F * this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12)).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.66F * this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12)).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.66F * this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12)).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.66F * this.field_82339_as).func_187314_a(j, k).func_181675_d();
   }

   public int func_70537_b() {
      return 1;
   }

   public void func_189213_a() {
      this.func_82338_g((float)(this.field_70547_e - this.field_70546_d) / (float)this.field_70547_e);
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

   }
}
