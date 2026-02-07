package thaumcraft.client.fx.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;

public class FXGeneric extends Particle {
   boolean doneFrames = false;
   boolean flipped = false;
   double windX;
   double windZ;
   int layer = 0;
   float dr = 0.0F;
   float dg = 0.0F;
   float db = 0.0F;
   boolean loop = false;
   float rotationSpeed = 0.0F;
   int startParticle = 0;
   int numParticles = 1;
   int particleInc = 1;
   float[] scaleKeys = new float[]{1.0F};
   float[] scaleFrames = new float[]{0.0F};
   float[] alphaKeys = new float[]{1.0F};
   float[] alphaFrames = new float[]{0.0F};
   double slowDown = 0.9800000190734863D;
   float randomX;
   float randomY;
   float randomZ;
   int[] finalFrames = null;
   boolean angled = false;
   float angleYaw;
   float anglePitch;
   int gridSize = 64;
   boolean thaumClamp = false;

   public FXGeneric(World world, double x, double y, double z, double xx, double yy, double zz) {
      super(world, x, y, z, xx, yy, zz);
      this.func_187115_a(0.1F, 0.1F);
      this.func_187109_b(x, y, z);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.field_187123_c = x;
      this.field_187124_d = y;
      this.field_187125_e = z;
      this.field_70548_b = 0.0F;
      this.field_70549_c = 0.0F;
      this.field_187129_i = xx;
      this.field_187130_j = yy;
      this.field_187131_k = zz;
   }

   public FXGeneric(World world, double x, double y, double z) {
      super(world, x, y, z, 0.0D, 0.0D, 0.0D);
      this.func_187115_a(0.1F, 0.1F);
      this.func_187109_b(x, y, z);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.field_187123_c = x;
      this.field_187124_d = y;
      this.field_187125_e = z;
      this.field_70548_b = 0.0F;
      this.field_70549_c = 0.0F;
   }

   void calculateFrames() {
      this.doneFrames = true;
      if (this.alphaKeys == null) {
         this.func_82338_g(1.0F);
      }

      this.alphaFrames = new float[this.field_70547_e + 1];
      float inc = (float)(this.alphaKeys.length - 1) / (float)this.field_70547_e;
      float is = 0.0F;

      int a;
      int isF;
      float diff;
      float pa;
      for(a = 0; a <= this.field_70547_e; ++a) {
         isF = MathHelper.func_76141_d(is);
         diff = isF < this.alphaKeys.length - 1 ? this.alphaKeys[isF + 1] - this.alphaKeys[isF] : 0.0F;
         pa = is - (float)isF;
         this.alphaFrames[a] = this.alphaKeys[isF] + diff * pa;
         is += inc;
      }

      if (this.scaleKeys == null) {
         this.setScale(1.0F);
      }

      this.scaleFrames = new float[this.field_70547_e + 1];
      inc = (float)(this.scaleKeys.length - 1) / (float)this.field_70547_e;
      is = 0.0F;

      for(a = 0; a <= this.field_70547_e; ++a) {
         isF = MathHelper.func_76141_d(is);
         diff = isF < this.scaleKeys.length - 1 ? this.scaleKeys[isF + 1] - this.scaleKeys[isF] : 0.0F;
         pa = is - (float)isF;
         this.scaleFrames[a] = this.scaleKeys[isF] + diff * pa;
         is += inc;
      }

   }

   public void func_189213_a() {
      if (!this.doneFrames) {
         this.calculateFrames();
      }

      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      this.field_190015_G = this.field_190014_F;
      this.field_190014_F += 3.1415927F * this.rotationSpeed * 2.0F;
      this.field_187130_j -= 0.04D * (double)this.field_70545_g;
      this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
      this.field_187129_i *= this.slowDown;
      this.field_187130_j *= this.slowDown;
      this.field_187131_k *= this.slowDown;
      this.field_187129_i += this.field_187122_b.field_73012_v.nextGaussian() * (double)this.randomX;
      this.field_187130_j += this.field_187122_b.field_73012_v.nextGaussian() * (double)this.randomY;
      this.field_187131_k += this.field_187122_b.field_73012_v.nextGaussian() * (double)this.randomZ;
      this.field_187129_i += this.windX;
      this.field_187131_k += this.windZ;
      if (this.field_187132_l && this.slowDown != 1.0D) {
         this.field_187129_i *= 0.699999988079071D;
         this.field_187131_k *= 0.699999988079071D;
      }

   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      if (this.thaumClamp) {
         boolean canSee = (Minecraft.func_71410_x().field_71439_g.func_184614_ca() != null && Minecraft.func_71410_x().field_71439_g.func_184614_ca().func_77973_b() instanceof ItemThaumometer || Minecraft.func_71410_x().field_71439_g.func_184592_cb() != null && Minecraft.func_71410_x().field_71439_g.func_184592_cb().func_77973_b() instanceof ItemThaumometer) && EntityUtils.isVisibleTo(1.15F, Minecraft.func_71410_x().field_71439_g, this.field_187126_f, this.field_187127_g, this.field_187128_h, 24.0F);
         if (!canSee) {
            return;
         }
      }

      if (this.loop) {
         this.func_70536_a(this.startParticle + this.field_70546_d / this.particleInc % this.numParticles);
      } else {
         float fs = (float)this.field_70546_d / (float)this.field_70547_e;
         this.func_70536_a((int)((float)this.startParticle + Math.min((float)this.numParticles * fs, (float)(this.numParticles - 1))));
      }

      if (this.finalFrames != null && this.field_70546_d > this.field_70547_e - this.finalFrames.length) {
         int frame = this.field_70547_e - this.field_70546_d;
         this.func_70536_a(this.finalFrames[frame]);
      }

      this.field_82339_as = this.alphaFrames[Math.min(this.field_70546_d, this.alphaFrames.length - 1)];
      this.field_70544_f = this.scaleFrames[Math.min(this.field_70546_d, this.scaleFrames.length - 1)];
      this.draw(wr, entity, f, f1, f2, f3, f4, f5);
   }

   public boolean isFlipped() {
      return this.flipped;
   }

   public void setFlipped(boolean flip) {
      this.flipped = flip;
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
      float f5 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks - field_70556_an);
      float f6 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks - field_70554_ao);
      float f7 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks - field_70555_ap);
      if (this.angled) {
         Tessellator.func_178181_a().func_78381_a();
         GL11.glPushMatrix();
         GL11.glTranslated((double)f5, (double)f6, (double)f7);
         GL11.glRotatef(-this.angleYaw + 90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.anglePitch + 90.0F, 1.0F, 0.0F, 0.0F);
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
         wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
      } else {
         Vec3d[] avec3d = new Vec3d[]{new Vec3d((double)(-rotationX * ts - rotationXY * ts), (double)(-rotationZ * ts), (double)(-rotationYZ * ts - rotationXZ * ts)), new Vec3d((double)(-rotationX * ts + rotationXY * ts), (double)(rotationZ * ts), (double)(-rotationYZ * ts + rotationXZ * ts)), new Vec3d((double)(rotationX * ts + rotationXY * ts), (double)(rotationZ * ts), (double)(rotationYZ * ts + rotationXZ * ts)), new Vec3d((double)(rotationX * ts - rotationXY * ts), (double)(-rotationZ * ts), (double)(rotationYZ * ts - rotationXZ * ts))};
         if (this.field_190014_F != 0.0F) {
            float f8 = this.field_190014_F + (this.field_190014_F - this.field_190015_G) * partialTicks;
            float f9 = MathHelper.func_76134_b(f8 * 0.5F);
            float f10 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72450_a;
            float f11 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72448_b;
            float f12 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72449_c;
            Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

            for(int l = 0; l < 4; ++l) {
               avec3d[l] = vec3d.func_186678_a(2.0D * avec3d[l].func_72430_b(vec3d)).func_178787_e(avec3d[l].func_186678_a((double)(f9 * f9) - vec3d.func_72430_b(vec3d))).func_178787_e(vec3d.func_72431_c(avec3d[l]).func_186678_a((double)(2.0F * f9)));
            }
         }

         wr.func_181662_b((double)f5 + avec3d[0].field_72450_a, (double)f6 + avec3d[0].field_72448_b, (double)f7 + avec3d[0].field_72449_c).func_187315_a((double)tx2, (double)ty2).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b((double)f5 + avec3d[1].field_72450_a, (double)f6 + avec3d[1].field_72448_b, (double)f7 + avec3d[1].field_72449_c).func_187315_a((double)tx2, (double)ty1).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b((double)f5 + avec3d[2].field_72450_a, (double)f6 + avec3d[2].field_72448_b, (double)f7 + avec3d[2].field_72449_c).func_187315_a((double)tx1, (double)ty1).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b((double)f5 + avec3d[3].field_72450_a, (double)f6 + avec3d[3].field_72448_b, (double)f7 + avec3d[3].field_72449_c).func_187315_a((double)tx1, (double)ty2).func_181666_a(pr, pg, pb, this.field_82339_as).func_187314_a(j, k).func_181675_d();
      }

   }

   public void setWind(double d) {
      int m = this.field_187122_b.func_72853_d();
      Vec3d vsource = new Vec3d(0.0D, 0.0D, 0.0D);
      Vec3d vtar = new Vec3d(0.1D, 0.0D, 0.0D);
      vtar = Utils.rotateAroundY(vtar, (float)(m * (40 + this.field_187122_b.field_73012_v.nextInt(10))) / 180.0F * 3.1415927F);
      Vec3d vres = vsource.func_72441_c(vtar.field_72450_a, vtar.field_72448_b, vtar.field_72449_c);
      this.windX = vres.field_72450_a * d;
      this.windZ = vres.field_72449_c * d;
   }

   public void setLayer(int layer) {
      this.layer = layer;
   }

   public void func_70538_b(float particleRedIn, float particleGreenIn, float particleBlueIn) {
      super.func_70538_b(particleRedIn, particleGreenIn, particleBlueIn);
      this.dr = particleRedIn;
      this.dg = particleGreenIn;
      this.db = particleBlueIn;
   }

   public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn, float r2, float g2, float b2) {
      super.func_70538_b(particleRedIn, particleGreenIn, particleBlueIn);
      this.dr = r2;
      this.dg = g2;
      this.db = b2;
   }

   public int func_70537_b() {
      return this.layer;
   }

   public void setLoop(boolean loop) {
      this.loop = loop;
   }

   public void setRotationSpeed(float rot) {
      this.rotationSpeed = (float)((double)rot * 0.017453292519943D);
   }

   public void setRotationSpeed(float start, float rot) {
      this.field_190014_F = (float)((double)start * 3.141592653589793D * 2.0D);
      this.rotationSpeed = (float)((double)rot * 0.017453292519943D);
   }

   public void func_187114_a(int max) {
      this.field_70547_e = max;
   }

   public void setParticles(int startParticle, int numParticles, int particleInc) {
      this.numParticles = numParticles;
      this.particleInc = particleInc;
      this.startParticle = startParticle;
      this.func_70536_a(startParticle);
   }

   public void setParticle(int startParticle) {
      this.numParticles = 1;
      this.particleInc = 1;
      this.startParticle = startParticle;
      this.func_70536_a(startParticle);
   }

   public void setScale(float... scale) {
      this.field_70544_f = scale[0];
      this.scaleKeys = scale;
   }

   public void setAlphaF(float... a1) {
      super.func_82338_g(a1[0]);
      this.alphaKeys = a1;
   }

   public void func_82338_g(float a1) {
      super.func_82338_g(a1);
      this.alphaKeys = new float[1];
      this.alphaKeys[0] = a1;
   }

   public void setSlowDown(double slowDown) {
      this.slowDown = slowDown;
   }

   public void setRandomMovementScale(float x, float y, float z) {
      this.randomX = x;
      this.randomY = y;
      this.randomZ = z;
   }

   public void setFinalFrames(int... frames) {
      this.finalFrames = frames;
   }

   public void setAngles(float yaw, float pitch) {
      this.angleYaw = yaw;
      this.anglePitch = pitch;
      this.angled = true;
   }

   public void setGravity(float g) {
      this.field_70545_g = g;
   }

   public void func_70536_a(int p_70536_1_) {
      this.field_94054_b = p_70536_1_ % this.gridSize;
      this.field_94055_c = p_70536_1_ / this.gridSize;
   }

   public void setGridSize(int gridSize) {
      this.gridSize = gridSize;
   }

   public void setNoClip(boolean clip) {
      this.field_190017_n = clip;
   }

   public void setThaumClamp(boolean thaumclamp) {
      this.thaumClamp = thaumclamp;
   }
}
