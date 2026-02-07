package thaumcraft.client.fx.other;

import com.sasmaster.glelwjgl.java.CoreGLE;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.codechicken.lib.vec.Quat;

public class FXBoreStream extends Particle {
   private Entity target;
   private double startX;
   private double startY;
   private double startZ;
   private int count = 0;
   public int length = 5;
   private String key = "";
   private BlockPos startPos = null;
   CoreGLE gle = new CoreGLE();
   private static final ResourceLocation TEX0 = new ResourceLocation("thaumcraft", "textures/misc/essentia.png");
   int layer = 1;
   double[][] points;
   float[][] colours;
   double[] radii;
   int growing = -1;
   ArrayList<Quat> vecs = new ArrayList();

   public FXBoreStream(World w, double par2, double par4, double par6, Entity target, int count, int color, float scale, int extend, double my) {
      super(w, par2, par4, par6, 0.0D, 0.0D, 0.0D);
      this.field_70544_f = (float)((double)scale * (1.0D + this.field_187136_p.nextGaussian() * 0.15000000596046448D));
      this.length = Math.max(5, extend);
      this.count = count;
      this.target = target;
      this.field_70547_e = this.length * 10;
      this.field_187129_i = (double)(MathHelper.func_76126_a((float)count / 4.0F) * 0.15F);
      this.field_187130_j = my + (double)(MathHelper.func_76126_a((float)count / 3.0F) * 0.15F);
      this.field_187131_k = (double)(MathHelper.func_76126_a((float)count / 2.0F) * 0.15F);
      Color c = new Color(color);
      this.field_70552_h = (float)c.getRed() / 255.0F;
      this.field_70553_i = (float)c.getGreen() / 255.0F;
      this.field_70551_j = (float)c.getBlue() / 255.0F;
      this.field_70545_g = 0.2F;
      this.vecs.add(new Quat(0.0D, 0.0D, 0.0D, 0.001D));
      this.vecs.add(new Quat(0.0D, 0.0D, 0.0D, 0.001D));
      this.startX = this.field_187126_f;
      this.startY = this.field_187127_g;
      this.startZ = this.field_187128_h;
      this.startPos = new BlockPos(this.startX, this.startY, this.startZ);
   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      double ePX = this.startX - field_70556_an;
      double ePY = this.startY - field_70554_ao;
      double ePZ = this.startZ - field_70555_ap;
      GL11.glTranslated(ePX, ePY, ePZ);
      if (this.points != null && this.points.length > 2) {
         Minecraft.func_71410_x().field_71446_o.func_110577_a(TEX0);
         this.gle.set_POLYCYL_TESS(8);
         this.gle.set__ROUND_TESS_PIECES(1);
         this.gle.gleSetJoinStyle(1042);
         this.gle.glePolyCone(this.points.length, this.points, this.colours, this.radii, 0.075F, this.growing < 0 ? 0.0F : 0.075F * ((float)(this.field_70546_d - this.growing) + f));
      }

      GL11.glPopMatrix();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(ParticleEngine.particleTexture);
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
   }

   public void setFXLayer(int l) {
      this.layer = l;
   }

   public int func_70537_b() {
      return this.layer;
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ < this.field_70547_e && this.length >= 1) {
         this.field_187130_j += 0.01D * (double)this.field_70545_g;
         this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
         this.field_187129_i *= 0.985D;
         this.field_187130_j *= 0.985D;
         this.field_187131_k *= 0.985D;
         double dx = this.target.field_70165_t - this.field_187126_f;
         double dy = this.target.field_70163_u + (double)this.target.func_70047_e() - this.field_187127_g;
         double dz = this.target.field_70161_v - this.field_187128_h;
         double d11 = (double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz);
         double clamp = d11 / 10.0D;
         this.field_187129_i = MathHelper.func_151237_a((double)((float)this.field_187129_i), -clamp, clamp);
         this.field_187130_j = MathHelper.func_151237_a((double)((float)this.field_187130_j), -clamp, clamp);
         this.field_187131_k = MathHelper.func_151237_a((double)((float)this.field_187131_k), -clamp, clamp);
         dx /= d11;
         dy /= d11;
         dz /= d11;
         this.field_187129_i += dx * (clamp / Math.min(1.0D, d11));
         this.field_187130_j += dy * (clamp / Math.min(1.0D, d11));
         this.field_187131_k += dz * (clamp / Math.min(1.0D, d11));
         float scale = this.field_70544_f * (0.75F + MathHelper.func_76126_a((float)(this.count + this.field_70546_d) / 2.0F) * 0.25F);
         if (d11 < 1.0D) {
            float f = MathHelper.func_76126_a((float)(d11 * 1.5707963267948966D));
            scale *= f;
            this.field_70544_f *= f;
         }

         if ((double)this.field_70544_f > 0.001D) {
            this.vecs.add(new Quat((double)scale, this.field_187126_f - this.startX, this.field_187127_g - this.startY, this.field_187128_h - this.startZ));
         } else {
            if (this.growing < 0) {
               this.growing = this.field_70546_d;
            }

            --this.length;
         }

         if (this.vecs.size() > this.length) {
            this.vecs.remove(0);
         }

         this.points = new double[this.vecs.size()][3];
         this.colours = new float[this.vecs.size()][3];
         this.radii = new double[this.vecs.size()];
         int c = this.vecs.size();

         float v2;
         for(Iterator var13 = this.vecs.iterator(); var13.hasNext(); this.colours[c][2] = this.field_70551_j * v2) {
            Quat v = (Quat)var13.next();
            --c;
            float variance = 1.0F + MathHelper.func_76126_a((float)(c + this.field_70546_d) / 3.0F) * 0.2F;
            float xx = MathHelper.func_76126_a((float)(c + this.field_70546_d) / 6.0F) * 0.03F;
            float yy = MathHelper.func_76126_a((float)(c + this.field_70546_d) / 7.0F) * 0.03F;
            float zz = MathHelper.func_76126_a((float)(c + this.field_70546_d) / 8.0F) * 0.03F;
            this.points[c][0] = v.x + (double)xx;
            this.points[c][1] = v.y + (double)yy;
            this.points[c][2] = v.z + (double)zz;
            this.radii[c] = v.s * (double)variance;
            if (c > this.vecs.size() - 10) {
               double[] var10000 = this.radii;
               var10000[c] *= (double)MathHelper.func_76134_b((float)((double)((float)(c - (this.vecs.size() - 12)) / 10.0F) * 1.5707963267948966D));
            }

            if (c == 0) {
               this.radii[c] = 0.0D;
            } else if (c == 1) {
               this.radii[c] = 0.0D;
            } else if (c == 2) {
               this.radii[c] = ((double)this.field_70544_f * 0.5D + this.radii[c]) / 2.0D;
            } else if (c == 3) {
               this.radii[c] = ((double)this.field_70544_f + this.radii[c]) / 2.0D;
            } else if (c == 4) {
               this.radii[c] = ((double)this.field_70544_f + this.radii[c] * 2.0D) / 3.0D;
            }

            v2 = 1.0F - MathHelper.func_76126_a((float)(c + this.field_70546_d) / 2.0F) * 0.1F;
            this.colours[c][0] = this.field_70552_h * v2;
            this.colours[c][1] = this.field_70553_i * v2;
         }

      } else {
         this.func_187112_i();
      }
   }

   public void setGravity(float value) {
      this.field_70545_g = value;
   }
}
