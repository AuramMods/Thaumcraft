package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXSlimyBubble extends Particle {
   int particle = 144;

   public FXSlimyBubble(World world, double d, double d1, double d2, float f) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      this.field_70552_h = 1.0F;
      this.field_70553_i = 1.0F;
      this.field_70551_j = 1.0F;
      this.field_70545_g = 0.0F;
      this.field_187129_i = this.field_187130_j = this.field_187131_k = 0.0D;
      this.field_70544_f = f;
      this.field_70547_e = 15 + world.field_73012_v.nextInt(5);
      this.func_187115_a(0.01F, 0.01F);
   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.field_82339_as);
      float var8 = (float)(this.particle % 16) / 64.0F;
      float var9 = var8 + 0.015625F;
      float var10 = (float)(this.particle / 16) / 64.0F;
      float var11 = var10 + 0.015625F;
      float var12 = this.field_70544_f;
      float var13 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float var14 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float var15 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      int i = this.func_189214_a(f);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181662_b((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12)).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12)).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12)).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12)).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181675_d();
   }

   public int func_70537_b() {
      return 1;
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      if (this.field_70546_d - 1 < 6) {
         this.particle = 144 + this.field_70546_d / 2;
         if (this.field_70546_d == 5) {
            this.field_187127_g += 0.1D;
         }
      } else if (this.field_70546_d < this.field_70547_e - 4) {
         this.field_187130_j += 0.005D;
         this.particle = 147 + this.field_70546_d % 4 / 2;
      } else {
         this.field_187130_j /= 2.0D;
         this.particle = 150 - (this.field_70547_e - this.field_70546_d) / 2;
      }

      this.field_187127_g += this.field_187130_j;
   }
}
