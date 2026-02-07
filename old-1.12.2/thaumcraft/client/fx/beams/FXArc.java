package thaumcraft.client.fx.beams;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.lib.utils.Utils;

public class FXArc extends Particle {
   public int particle = 16;
   ArrayList<Vec3d> points = new ArrayList();
   private Entity targetEntity = null;
   private double tX = 0.0D;
   private double tY = 0.0D;
   private double tZ = 0.0D;
   ResourceLocation beam = new ResourceLocation("thaumcraft", "textures/misc/beamh.png");
   public int blendmode = 1;
   public float length = 1.0F;

   public FXArc(World par1World, double x, double y, double z, double tx, double ty, double tz, float red, float green, float blue, double hg) {
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
      this.field_70547_e = 1;
      double xx = 0.0D;
      double yy = 0.0D;
      double zz = 0.0D;
      double gravity = 0.115D;
      double noise = 0.25D;
      Vec3d vs = new Vec3d(xx, yy, zz);
      Vec3d ve = new Vec3d(this.tX, this.tY, this.tZ);
      Vec3d vc = new Vec3d(xx, yy, zz);
      this.length = (float)ve.func_72433_c();
      Vec3d vv = Utils.calculateVelocity(vs, ve, hg, gravity);
      double l = Utils.distanceSquared3d(new Vec3d(0.0D, 0.0D, 0.0D), vv);
      this.points.add(vs);

      for(int c = 0; Utils.distanceSquared3d(ve, vc) > l && c < 50; ++c) {
         Vec3d vt = vc.func_72441_c(vv.field_72450_a, vv.field_72448_b, vv.field_72449_c);
         vc = new Vec3d(vt.field_72450_a, vt.field_72448_b, vt.field_72449_c);
         vt = vt.func_72441_c((this.field_187136_p.nextDouble() - this.field_187136_p.nextDouble()) * noise, (this.field_187136_p.nextDouble() - this.field_187136_p.nextDouble()) * noise, (this.field_187136_p.nextDouble() - this.field_187136_p.nextDouble()) * noise);
         this.points.add(vt);
         vv = vv.func_178786_a(0.0D, gravity / 1.9D, 0.0D);
      }

      this.points.add(ve);
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

   }

   public void setRGB(float r, float g, float b) {
      this.field_70552_h = r;
      this.field_70553_i = g;
      this.field_70551_j = b;
   }

   public void func_180434_a(VertexBuffer wr, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      double ePX = this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an;
      double ePY = this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao;
      double ePZ = this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap;
      GL11.glTranslated(ePX, ePY, ePZ);
      float size = 0.25F;
      Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam);
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(2884);
      int i = 220;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181668_a(5, DefaultVertexFormats.field_181711_k);
      float f9 = 0.0F;
      float f10 = 1.0F;

      int c;
      Vec3d v;
      float f13;
      double dx;
      double dy;
      double dz;
      for(c = 0; c < this.points.size(); ++c) {
         v = (Vec3d)this.points.get(c);
         f13 = (float)c / this.length;
         dx = v.field_72450_a;
         dy = v.field_72448_b;
         dz = v.field_72449_c;
         wr.func_181662_b(dx, dy - (double)size, dz).func_187315_a((double)f13, (double)f10).func_187314_a(j, k).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F).func_181675_d();
         wr.func_181662_b(dx, dy + (double)size, dz).func_187315_a((double)f13, (double)f9).func_187314_a(j, k).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F).func_181675_d();
      }

      Tessellator.func_178181_a().func_78381_a();
      wr.func_181668_a(5, DefaultVertexFormats.field_181711_k);

      for(c = 0; c < this.points.size(); ++c) {
         v = (Vec3d)this.points.get(c);
         f13 = (float)c / this.length;
         dx = v.field_72450_a;
         dy = v.field_72448_b;
         dz = v.field_72449_c;
         wr.func_181662_b(dx - (double)size, dy, dz - (double)size).func_187315_a((double)f13, (double)f10).func_187314_a(j, k).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F).func_181675_d();
         wr.func_181662_b(dx + (double)size, dy, dz + (double)size).func_187315_a((double)f13, (double)f9).func_187314_a(j, k).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.8F).func_181675_d();
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
