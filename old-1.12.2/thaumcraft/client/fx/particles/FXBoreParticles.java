package thaumcraft.client.fx.particles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXBoreParticles extends Particle {
   private IBlockState blockInstance;
   private Item itemInstance;
   private int metadata;
   private int side;
   private Entity target;
   private double targetX;
   private double targetY;
   private double targetZ;

   public FXBoreParticles(World par1World, double par2, double par4, double par6, double tx, double ty, double tz, IBlockState par14Block, int par15) {
      super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
      this.blockInstance = par14Block;

      try {
         this.func_187117_a(Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178087_a(Item.func_150898_a(par14Block.func_177230_c()), par15));
      } catch (Exception var24) {
         this.func_187117_a(Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178087_a(Item.func_150898_a(Blocks.field_150348_b), 0));
         this.field_70547_e = 0;
      }

      this.field_70545_g = par14Block.func_177230_c().field_149763_I;
      this.field_70552_h = this.field_70553_i = this.field_70551_j = 0.6F;
      this.field_70544_f = this.field_187136_p.nextFloat() * 0.3F + 0.4F;
      this.side = par15;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      double dx = tx - this.field_187126_f;
      double dy = ty - this.field_187127_g;
      double dz = tz - this.field_187128_h;
      int base = (int)(MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) * 10.0F);
      if (base < 1) {
         base = 1;
      }

      this.field_70547_e = base / 2 + this.field_187136_p.nextInt(base);
      float f3 = 0.01F;
      this.field_187129_i = (double)((float)this.field_187122_b.field_73012_v.nextGaussian() * f3);
      this.field_187130_j = (double)((float)this.field_187122_b.field_73012_v.nextGaussian() * f3);
      this.field_187131_k = (double)((float)this.field_187122_b.field_73012_v.nextGaussian() * f3);
      this.field_70545_g = 0.01F;
   }

   public FXBoreParticles(World par1World, double par2, double par4, double par6, double tx, double ty, double tz, double sx, double sy, double sz, Item item, int par15) {
      super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
      this.itemInstance = item;
      this.func_187117_a(Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178087_a(item, par15));
      this.field_70545_g = Blocks.field_150431_aC.field_149763_I;
      this.field_70552_h = this.field_70553_i = this.field_70551_j = 0.6F;
      this.field_70544_f = this.field_187136_p.nextFloat() * 0.3F + 0.4F;
      this.side = par15;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      double dx = tx - this.field_187126_f;
      double dy = ty - this.field_187127_g;
      double dz = tz - this.field_187128_h;
      int base = (int)(MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) * 10.0F);
      if (base < 1) {
         base = 1;
      }

      this.field_70547_e = base / 2 + this.field_187136_p.nextInt(base);
      float f3 = 0.01F;
      this.field_187129_i = sx + (double)((float)this.field_187122_b.field_73012_v.nextGaussian() * f3);
      this.field_187130_j = sy + (double)((float)this.field_187122_b.field_73012_v.nextGaussian() * f3);
      this.field_187131_k = sz + (double)((float)this.field_187122_b.field_73012_v.nextGaussian() * f3);
      this.field_70545_g = 0.01F;
      Entity renderentity = FMLClientHandler.instance().getClient().func_175606_aa();
      int visibleDistance = 64;
      if (!FMLClientHandler.instance().getClient().field_71474_y.field_74347_j) {
         visibleDistance = 32;
      }

      if (renderentity.func_70011_f(this.field_187126_f, this.field_187127_g, this.field_187128_h) > (double)visibleDistance) {
         this.field_70547_e = 0;
      }

   }

   public void setTarget(Entity target) {
      this.target = target;
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.target != null) {
         this.targetX = this.target.field_70165_t;
         this.targetY = this.target.field_70163_u + (double)this.target.func_70047_e();
         this.targetZ = this.target.field_70161_v;
      }

      if (this.field_70546_d++ < this.field_70547_e && (MathHelper.func_76128_c(this.field_187126_f) != MathHelper.func_76128_c(this.targetX) || MathHelper.func_76128_c(this.field_187127_g) != MathHelper.func_76128_c(this.targetY) || MathHelper.func_76128_c(this.field_187128_h) != MathHelper.func_76128_c(this.targetZ))) {
         this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
         this.field_187129_i *= 0.985D;
         this.field_187130_j *= 0.95D;
         this.field_187131_k *= 0.985D;
         double dx = this.targetX - this.field_187126_f;
         double dy = this.targetY - this.field_187127_g;
         double dz = this.targetZ - this.field_187128_h;
         double d11 = (double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz);
         double clamp = Math.min(0.25D, d11 / 15.0D);
         if (d11 < 2.0D) {
            this.field_70544_f *= 0.9F;
         }

         dx /= d11;
         dy /= d11;
         dz /= d11;
         this.field_187129_i += dx * clamp;
         this.field_187130_j += dy * clamp;
         this.field_187131_k += dz * clamp;
         this.field_187129_i = MathHelper.func_151237_a((double)((float)this.field_187129_i), -clamp, clamp);
         this.field_187130_j = MathHelper.func_151237_a((double)((float)this.field_187130_j), -clamp, clamp);
         this.field_187131_k = MathHelper.func_151237_a((double)((float)this.field_187131_k), -clamp, clamp);
         this.field_187129_i += this.field_187136_p.nextGaussian() * 0.005D;
         this.field_187130_j += this.field_187136_p.nextGaussian() * 0.005D;
         this.field_187131_k += this.field_187136_p.nextGaussian() * 0.005D;
      } else {
         this.func_187112_i();
      }
   }

   public int func_70537_b() {
      return 1;
   }

   public FXBoreParticles getObjectColor(BlockPos pos) {
      int var4;
      if (this.blockInstance != null && this.field_187122_b.func_180495_p(pos) == this.blockInstance) {
         if (this.blockInstance == Blocks.field_150349_c && this.side != 1) {
            return this;
         } else {
            try {
               var4 = Minecraft.func_71410_x().func_184125_al().func_186724_a(this.blockInstance, this.field_187122_b, pos, 0);
               this.field_70552_h *= (float)(var4 >> 16 & 255) / 255.0F;
               this.field_70553_i *= (float)(var4 >> 8 & 255) / 255.0F;
               this.field_70551_j *= (float)(var4 & 255) / 255.0F;
            } catch (Exception var3) {
            }

            return this;
         }
      } else {
         try {
            var4 = ((IItemColor)this.itemInstance).func_186726_a(new ItemStack(this.itemInstance, 1, this.metadata), 0);
            this.field_70552_h *= (float)(var4 >> 16 & 255) / 255.0F;
            this.field_70553_i *= (float)(var4 >> 8 & 255) / 255.0F;
            this.field_70551_j *= (float)(var4 & 255) / 255.0F;
         } catch (Exception var4) {
         }

         return this;
      }
   }

   public void func_180434_a(VertexBuffer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      float f6 = ((float)this.field_94054_b + this.field_70548_b / 4.0F) / 16.0F;
      float f7 = f6 + 0.015609375F;
      float f8 = ((float)this.field_94055_c + this.field_70549_c / 4.0F) / 16.0F;
      float f9 = f8 + 0.015609375F;
      float f10 = 0.1F * this.field_70544_f;
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
      p_180434_1_.func_181662_b((double)(f11 - p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
      p_180434_1_.func_181662_b((double)(f11 - p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
      p_180434_1_.func_181662_b((double)(f11 + p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
      p_180434_1_.func_181662_b((double)(f11 + p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
   }
}
