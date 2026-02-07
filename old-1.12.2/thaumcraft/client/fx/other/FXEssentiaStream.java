package thaumcraft.client.fx.other;

import com.sasmaster.glelwjgl.java.CoreGLE;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.codechicken.lib.vec.Quat;
import thaumcraft.common.blocks.essentia.BlockEssentiaTransport;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class FXEssentiaStream extends Particle {
   private double targetX;
   private double targetY;
   private double targetZ;
   private double startX;
   private double startY;
   private double startZ;
   private int count = 0;
   public int length = 20;
   private String key = "";
   private BlockPos startPos = null;
   private BlockPos endPos = null;
   static HashMap<String, FXEssentiaStream> pt = new HashMap();
   CoreGLE gle = new CoreGLE();
   private static final ResourceLocation TEX0 = new ResourceLocation("thaumcraft", "textures/misc/essentia.png");
   int layer = 1;
   double[][] points;
   float[][] colours;
   double[] radii;
   int growing = -1;
   ArrayList<Quat> vecs = new ArrayList();

   public FXEssentiaStream(World w, double par2, double par4, double par6, double tx, double ty, double tz, int count, int color, float scale, int extend, double my) {
      super(w, par2, par4, par6, 0.0D, 0.0D, 0.0D);
      this.field_70544_f = (float)((double)scale * (1.0D + this.field_187136_p.nextGaussian() * 0.15000000596046448D));
      this.length = Math.max(20, extend);
      this.count = count;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      BlockPos bp1 = new BlockPos(this.field_187126_f, this.field_187127_g, this.field_187128_h);
      BlockPos bp2 = new BlockPos(this.targetX, this.targetY, this.targetZ);
      IBlockState bs = w.func_180495_p(bp1);
      if (bs.func_177230_c() instanceof BlockEssentiaTransport) {
         EnumFacing f = BlockStateUtils.getFacing(bs);
         this.field_187126_f += (double)((float)f.func_82601_c() * 0.05F);
         this.field_187127_g += (double)((float)f.func_96559_d() * 0.05F);
         this.field_187128_h += (double)((float)f.func_82599_e() * 0.05F);
      }

      double dx = tx - this.field_187126_f;
      double dy = ty - this.field_187127_g;
      double dz = tz - this.field_187128_h;
      int base = (int)(MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) * 21.0F);
      if (base < 1) {
         base = 1;
      }

      this.field_70547_e = base;
      String k = bp1.func_177986_g() + "" + bp2.func_177986_g() + "" + color;
      if (pt.containsKey(k)) {
         FXEssentiaStream trail2 = (FXEssentiaStream)pt.get(k);
         if (!trail2.field_187133_m && trail2.vecs.size() < trail2.length) {
            trail2.length += Math.max(extend, 5);
            trail2.field_70547_e += Math.max(extend, 5);
            this.field_70547_e = 0;
         }
      }

      if (this.field_70547_e > 0) {
         pt.put(k, this);
         this.key = k;
      }

      this.field_187129_i = (double)(MathHelper.func_76126_a((float)count / 4.0F) * 0.015F);
      this.field_187130_j = my + (double)(MathHelper.func_76126_a((float)count / 3.0F) * 0.015F);
      this.field_187131_k = (double)(MathHelper.func_76126_a((float)count / 2.0F) * 0.015F);
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
      this.endPos = bp2;
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
         new Vec3d(this.startPos);
         new Vec3d(this.endPos);
         this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
         this.field_187129_i *= 0.985D;
         this.field_187130_j *= 0.985D;
         this.field_187131_k *= 0.985D;
         this.field_187129_i = (double)MathHelper.func_76131_a((float)this.field_187129_i, -0.05F, 0.05F);
         this.field_187130_j = (double)MathHelper.func_76131_a((float)this.field_187130_j, -0.05F, 0.05F);
         this.field_187131_k = (double)MathHelper.func_76131_a((float)this.field_187131_k, -0.05F, 0.05F);
         double dx = this.targetX - this.field_187126_f;
         double dy = this.targetY - this.field_187127_g;
         double dz = this.targetZ - this.field_187128_h;
         double d13 = 0.01D;
         double d11 = (double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz);
         dx /= d11;
         dy /= d11;
         dz /= d11;
         this.field_187129_i += dx * (d13 / Math.min(1.0D, d11));
         this.field_187130_j += dy * (d13 / Math.min(1.0D, d11));
         this.field_187131_k += dz * (d13 / Math.min(1.0D, d11));
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
            FXDispatcher.INSTANCE.essentiaDropFx(this.targetX + this.field_187136_p.nextGaussian() * 0.07500000298023224D, this.targetY + this.field_187136_p.nextGaussian() * 0.07500000298023224D, this.targetZ + this.field_187136_p.nextGaussian() * 0.07500000298023224D, this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.5F);
         }

         if (this.vecs.size() > this.length) {
            this.vecs.remove(0);
         }

         this.points = new double[this.vecs.size()][3];
         this.colours = new float[this.vecs.size()][3];
         this.radii = new double[this.vecs.size()];
         int c = this.vecs.size();

         float v2;
         for(Iterator var15 = this.vecs.iterator(); var15.hasNext(); this.colours[c][2] = this.field_70551_j * v2) {
            Quat v = (Quat)var15.next();
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

         if (this.vecs.size() > 2 && this.field_187136_p.nextBoolean()) {
            int q = this.field_187136_p.nextInt(3);
            if (this.field_187136_p.nextBoolean()) {
               q = this.vecs.size() - 2;
            }

            FXDispatcher.INSTANCE.essentiaDropFx(((Quat)this.vecs.get(q)).x + this.startX, ((Quat)this.vecs.get(q)).y + this.startY, ((Quat)this.vecs.get(q)).z + this.startZ, this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.5F);
         }

      } else {
         this.func_187112_i();
         if (pt.containsKey(this.key) && ((FXEssentiaStream)pt.get(this.key)).field_187133_m) {
            pt.remove(this.key);
         }

      }
   }

   public void setGravity(float value) {
      this.field_70545_g = value;
   }
}
