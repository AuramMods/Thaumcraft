package thaumcraft.client.fx.particles;

import java.awt.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class FXVent extends Particle {
   float psm = 1.0F;

   public FXVent(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, int color) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      this.func_187115_a(0.02F, 0.02F);
      this.field_70544_f = this.field_187136_p.nextFloat() * 0.1F + 0.05F;
      this.field_187129_i = par8;
      this.field_187130_j = par10;
      this.field_187131_k = par12;
      Color c = new Color(color);
      this.field_70552_h = (float)c.getRed() / 255.0F;
      this.field_70551_j = (float)c.getBlue() / 255.0F;
      this.field_70553_i = (float)c.getGreen() / 255.0F;
      this.setHeading(this.field_187129_i, this.field_187130_j, this.field_187131_k, 0.125F, 5.0F);
      Entity renderentity = FMLClientHandler.instance().getClient().func_175606_aa();
      int visibleDistance = 50;
      if (!FMLClientHandler.instance().getClient().field_71474_y.field_74347_j) {
         visibleDistance = 25;
      }

      if (renderentity.func_70011_f(this.field_187126_f, this.field_187127_g, this.field_187128_h) > (double)visibleDistance) {
         this.field_70547_e = 0;
      }

      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
   }

   public void setScale(float f) {
      this.field_70544_f *= f;
      this.psm *= f;
   }

   public void setHeading(double par1, double par3, double par5, float par7, float par8) {
      float f2 = MathHelper.func_76133_a(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= (double)f2;
      par3 /= (double)f2;
      par5 /= (double)f2;
      par1 += this.field_187136_p.nextGaussian() * (double)(this.field_187136_p.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
      par3 += this.field_187136_p.nextGaussian() * (double)(this.field_187136_p.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
      par5 += this.field_187136_p.nextGaussian() * (double)(this.field_187136_p.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
      par1 *= (double)par7;
      par3 *= (double)par7;
      par5 *= (double)par7;
      this.field_187129_i = par1;
      this.field_187130_j = par3;
      this.field_187131_k = par5;
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      ++this.field_70546_d;
      if (this.field_70544_f >= this.psm) {
         this.func_187112_i();
      }

      this.field_187130_j += 0.0025D;
      this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
      this.field_187129_i *= 0.8500000190734863D;
      this.field_187130_j *= 0.8500000190734863D;
      this.field_187131_k *= 0.8500000190734863D;
      if (this.field_70544_f < this.psm) {
         this.field_70544_f = (float)((double)this.field_70544_f * 1.15D);
      }

      if (this.field_70544_f > this.psm) {
         this.field_70544_f = this.psm;
      }

      if (this.field_187132_l) {
         this.field_187129_i *= 0.699999988079071D;
         this.field_187131_k *= 0.699999988079071D;
      }

   }

   public void setRGB(float r, float g, float b) {
      this.field_70552_h = r;
      this.field_70553_i = g;
      this.field_70551_j = b;
   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.33F);
      int part = (int)(1.0F + this.field_70544_f / this.psm * 4.0F);
      float var8 = (float)(part % 16) / 64.0F;
      float var9 = var8 + 0.015625F;
      float var10 = (float)(part / 64) / 64.0F;
      float var11 = var10 + 0.015625F;
      float var12 = 0.3F * this.field_70544_f;
      float var13 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float var14 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float var15 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      float var16 = 1.0F;
      int i = this.func_189214_a(f);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      float alpha = this.field_82339_as * ((this.psm - this.field_70544_f) / this.psm);
      wr.func_181662_b((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12)).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, alpha).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12)).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, alpha).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12)).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, alpha).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12)).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, alpha).func_187314_a(j, k).func_181675_d();
   }

   public int func_70537_b() {
      return 1;
   }
}
