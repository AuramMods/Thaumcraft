package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXBreakingFade extends ParticleBreaking {
   public FXBreakingFade(World worldIn, double p_i1197_2_, double p_i1197_4_, double p_i1197_6_, double p_i1197_8_, double p_i1197_10_, double p_i1197_12_, Item p_i1197_14_, int p_i1197_15_) {
      super(worldIn, p_i1197_2_, p_i1197_4_, p_i1197_6_, p_i1197_8_, p_i1197_10_, p_i1197_12_, p_i1197_14_, p_i1197_15_);
   }

   public FXBreakingFade(World worldIn, double p_i1196_2_, double p_i1196_4_, double p_i1196_6_, Item p_i1196_8_, int p_i1196_9_) {
      super(worldIn, p_i1196_2_, p_i1196_4_, p_i1196_6_, p_i1196_8_, p_i1196_9_);
   }

   public FXBreakingFade(World worldIn, double p_i1195_2_, double p_i1195_4_, double p_i1195_6_, Item p_i1195_8_) {
      super(worldIn, p_i1195_2_, p_i1195_4_, p_i1195_6_, p_i1195_8_);
   }

   public void setParticleMaxAge(int particleMaxAge) {
      this.field_70547_e = particleMaxAge;
   }

   public void setParticleGravity(float f) {
      this.field_70545_g = f;
   }

   public int func_70537_b() {
      return 1;
   }

   public void setSpeed(double x, double y, double z) {
      this.field_187129_i = x;
      this.field_187130_j = y;
      this.field_187131_k = z;
   }

   public void func_180434_a(VertexBuffer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      GlStateManager.func_179132_a(false);
      float f6 = ((float)this.field_94054_b + this.field_70548_b / 4.0F) / 16.0F;
      float f7 = f6 + 0.015609375F;
      float f8 = ((float)this.field_94055_c + this.field_70549_c / 4.0F) / 16.0F;
      float f9 = f8 + 0.015609375F;
      float f10 = 0.1F * this.field_70544_f;
      float fade = 1.0F - (float)this.field_70546_d / (float)this.field_70547_e;
      if (this.field_187119_C != null) {
         f6 = this.field_187119_C.func_94214_a((double)(this.field_70548_b / 4.0F * 16.0F));
         f7 = this.field_187119_C.func_94214_a((double)((this.field_70548_b + 1.0F) / 4.0F * 16.0F));
         f8 = this.field_187119_C.func_94207_b((double)(this.field_70549_c / 4.0F * 16.0F));
         f9 = this.field_187119_C.func_94207_b((double)((this.field_70549_c + 1.0F) / 4.0F * 16.0F));
      }

      int i = this.func_189214_a(p_180434_3_);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      float f11 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)p_180434_3_ - field_70556_an);
      float f12 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)p_180434_3_ - field_70554_ao);
      float f13 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)p_180434_3_ - field_70555_ap);
      p_180434_1_.func_181662_b((double)(f11 - p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * fade).func_187314_a(j, k).func_181675_d();
      p_180434_1_.func_181662_b((double)(f11 - p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * fade).func_187314_a(j, k).func_181675_d();
      p_180434_1_.func_181662_b((double)(f11 + p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * fade).func_187314_a(j, k).func_181675_d();
      p_180434_1_.func_181662_b((double)(f11 + p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * fade).func_187314_a(j, k).func_181675_d();
      GlStateManager.func_179132_a(true);
   }

   public void boom() {
      float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
      float f1 = MathHelper.func_76133_a(this.field_187129_i * this.field_187129_i + this.field_187130_j * this.field_187130_j + this.field_187131_k * this.field_187131_k);
      this.field_187129_i = this.field_187129_i / (double)f1 * (double)f * 0.9640000000596046D;
      this.field_187130_j = this.field_187130_j / (double)f1 * (double)f * 0.9640000000596046D + 0.10000000149011612D;
      this.field_187131_k = this.field_187131_k / (double)f1 * (double)f * 0.9640000000596046D;
   }
}
