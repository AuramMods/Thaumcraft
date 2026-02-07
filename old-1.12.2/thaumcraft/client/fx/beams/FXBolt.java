package thaumcraft.client.fx.beams;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.codechicken.lib.vec.Vector3;

public class FXBolt extends Particle {
   ArrayList<Vec3d> points = new ArrayList();
   ArrayList<Float> pointsWidth = new ArrayList();
   float dr = 0.0F;
   long seed = 0L;
   private Entity targetEntity = null;
   private double tX = 0.0D;
   private double tY = 0.0D;
   private double tZ = 0.0D;
   ResourceLocation beam = new ResourceLocation("thaumcraft", "textures/misc/beaml.png");
   public float length = 1.0F;

   public FXBolt(World par1World, double x, double y, double z, double tx, double ty, double tz, float red, float green, float blue, float width) {
      super(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
      this.field_70552_h = red;
      this.field_70553_i = green;
      this.field_70551_j = blue;
      this.func_187115_a(0.02F, 0.02F);
      this.field_187129_i = 0.0D;
      this.field_187130_j = 0.0D;
      this.field_187131_k = 0.0D;
      this.tX = tx - x;
      this.tY = ty - y;
      this.tZ = tz - z;
      this.field_70547_e = 3;
      Vec3d vs = new Vec3d(0.0D, 0.0D, 0.0D);
      Vec3d ve = new Vec3d(this.tX, this.tY, this.tZ);
      this.length = (float)(ve.func_72433_c() * 3.141592653589793D);
      int steps = (int)this.length;
      this.points.add(vs);
      this.pointsWidth.add(width);
      this.dr = (float)((double)this.field_187136_p.nextInt(50) * 3.141592653589793D);
      float ampl = 0.1F;

      for(int a = 1; a < steps - 1; ++a) {
         float dist = (float)a * (this.length / (float)steps) + this.dr;
         double dx = this.tX / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 4.0F) * ampl);
         double dy = this.tY / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 3.0F) * ampl);
         double dz = this.tZ / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 2.0F) * ampl);
         dx += (double)((this.field_187136_p.nextFloat() - this.field_187136_p.nextFloat()) * 0.1F);
         dy += (double)((this.field_187136_p.nextFloat() - this.field_187136_p.nextFloat()) * 0.1F);
         dz += (double)((this.field_187136_p.nextFloat() - this.field_187136_p.nextFloat()) * 0.1F);
         Vec3d vp = new Vec3d(dx, dy, dz);
         this.points.add(vp);
         this.pointsWidth.add(width);
      }

      this.pointsWidth.add(width);
      this.points.add(ve);
      this.seed = (long)this.field_187136_p.nextInt(1000);
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      Random rr = new Random(this.seed);
      this.points.clear();
      this.pointsWidth.clear();
      Vec3d vs = new Vec3d(0.0D, 0.0D, 0.0D);
      Vec3d ve = new Vec3d(this.tX, this.tY, this.tZ);
      int steps = (int)this.length;
      this.points.add(vs);
      this.pointsWidth.add(this.field_187134_n);
      float ampl = 0.15F * (float)this.field_70546_d;

      for(int a = 1; a < steps - 1; ++a) {
         float dist = (float)a * (this.length / (float)steps) + this.dr;
         double dx = this.tX / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 4.0F) * ampl);
         double dy = this.tY / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 3.0F) * ampl);
         double dz = this.tZ / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 2.0F) * ampl);
         dx += (double)((this.field_187136_p.nextFloat() - this.field_187136_p.nextFloat()) * 0.1F);
         dy += (double)((this.field_187136_p.nextFloat() - this.field_187136_p.nextFloat()) * 0.1F);
         dz += (double)((this.field_187136_p.nextFloat() - this.field_187136_p.nextFloat()) * 0.1F);
         Vec3d vp = new Vec3d(dx, dy, dz);
         this.points.add(vp);
         this.pointsWidth.add(rr.nextInt(4) == 0 ? 1.0F - (float)this.field_70546_d * 0.25F : 1.0F);
      }

      this.pointsWidth.add(this.field_187134_n);
      this.points.add(ve);
   }

   public void setRGB(float r, float g, float b) {
      this.field_70552_h = r;
      this.field_70553_i = g;
      this.field_70551_j = b;
   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float cosyaw, float cospitch, float sinyaw, float cossinpitch, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      double ePX = this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an;
      double ePY = this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao;
      double ePZ = this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap;
      GL11.glTranslated(ePX, ePY, ePZ);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam);
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(2884);
      int i = 220;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181668_a(5, DefaultVertexFormats.field_181704_d);
      float f9 = 0.0F;
      float f10 = 1.0F;

      int c;
      float size;
      float f13;
      Vec3d vc;
      Vec3d vp;
      Vec3d vn;
      Vec3d v1;
      Vec3d v2;
      Vec3d v;
      Vector3 vf;
      for(c = 0; c < this.points.size(); ++c) {
         size = 0.15F * (Float)this.pointsWidth.get(c);
         f13 = (float)c / this.length;
         vc = (Vec3d)this.points.get(c);
         vp = c == 0 ? (Vec3d)this.points.get(c) : (Vec3d)this.points.get(c - 1);
         vn = c == this.points.size() - 1 ? (Vec3d)this.points.get(c) : (Vec3d)this.points.get(c + 1);
         v1 = vp.func_178788_d(vc);
         v2 = vc.func_178788_d(vn);
         v = v1.func_178787_e(v2).func_72432_b();
         v = v.func_178789_a(1.5707964F);
         vf = (new Vector3(v)).multiply((double)size);
         wr.func_181662_b(vc.field_72450_a + vf.x, vc.field_72448_b + vf.y, vc.field_72449_c + vf.z).func_187315_a((double)f13, (double)f10).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F / (float)Math.max(1, this.field_70546_d)).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b(vc.field_72450_a - vf.x, vc.field_72448_b - vf.y, vc.field_72449_c - vf.z).func_187315_a((double)f13, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F / (float)Math.max(1, this.field_70546_d)).func_187314_a(j, k).func_181675_d();
      }

      Tessellator.func_178181_a().func_78381_a();
      wr.func_181668_a(5, DefaultVertexFormats.field_181704_d);

      for(c = 0; c < this.points.size(); ++c) {
         size = 0.15F * (Float)this.pointsWidth.get(c);
         f13 = (float)c / this.length;
         vc = (Vec3d)this.points.get(c);
         vp = c == 0 ? (Vec3d)this.points.get(c) : (Vec3d)this.points.get(c - 1);
         vn = c == this.points.size() - 1 ? (Vec3d)this.points.get(c) : (Vec3d)this.points.get(c + 1);
         v1 = vp.func_178788_d(vc);
         v2 = vc.func_178788_d(vn);
         v = v1.func_178787_e(v2).func_72432_b();
         v = v.func_178785_b(1.5707964F);
         vf = (new Vector3(v)).multiply((double)size);
         wr.func_181662_b(vc.field_72450_a + vf.x, vc.field_72448_b + vf.y, vc.field_72449_c + vf.z).func_187315_a((double)f13, (double)f10).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F / (float)Math.max(1, this.field_70546_d)).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b(vc.field_72450_a - vf.x, vc.field_72448_b - vf.y, vc.field_72449_c - vf.z).func_187315_a((double)f13, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F / (float)Math.max(1, this.field_70546_d)).func_187314_a(j, k).func_181675_d();
      }

      Tessellator.func_178181_a().func_78381_a();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(2884);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(ParticleManager.field_110737_b);
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
   }
}
