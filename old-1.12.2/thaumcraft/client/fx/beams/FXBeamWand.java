package thaumcraft.client.fx.beams;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;

public class FXBeamWand extends Particle {
   public int particle = 16;
   EntityLivingBase sourceEntity = null;
   private double offset = 0.0D;
   private float length = 0.0F;
   private float rotYaw = 0.0F;
   private float rotPitch = 0.0F;
   private float prevYaw = 0.0F;
   private float prevPitch = 0.0F;
   private Entity targetEntity = null;
   private double tX = 0.0D;
   private double tY = 0.0D;
   private double tZ = 0.0D;
   private double ptX = 0.0D;
   private double ptY = 0.0D;
   private double ptZ = 0.0D;
   private int type = 0;
   private float endMod = 1.0F;
   private boolean reverse = false;
   private boolean pulse = true;
   private int rotationspeed = 5;
   private float prevSize = 0.0F;
   public int impact;
   ResourceLocation beam = new ResourceLocation("thaumcraft", "textures/misc/beam.png");
   ResourceLocation beam1 = new ResourceLocation("thaumcraft", "textures/misc/beam1.png");
   ResourceLocation beam2 = new ResourceLocation("thaumcraft", "textures/misc/beam2.png");
   ResourceLocation beam3 = new ResourceLocation("thaumcraft", "textures/misc/beam3.png");

   public FXBeamWand(World par1World, EntityLivingBase p, double tx, double ty, double tz, float red, float green, float blue, int age) {
      super(par1World, p.field_70165_t, p.field_70163_u, p.field_70161_v, 0.0D, 0.0D, 0.0D);
      this.offset = (double)(p.field_70131_O / 2.0F) + 0.25D;
      this.field_70552_h = red;
      this.field_70553_i = green;
      this.field_70551_j = blue;
      this.sourceEntity = p;
      this.func_187115_a(0.02F, 0.02F);
      this.field_187129_i = 0.0D;
      this.field_187130_j = 0.0D;
      this.field_187131_k = 0.0D;
      this.tX = tx;
      this.tY = ty;
      this.tZ = tz;
      float xd = (float)(p.field_70165_t - this.tX);
      float yd = (float)(p.field_70163_u + this.offset - this.tY);
      float zd = (float)(p.field_70161_v - this.tZ);
      this.length = MathHelper.func_76129_c(xd * xd + yd * yd + zd * zd);
      double var7 = (double)MathHelper.func_76129_c(xd * xd + zd * zd);
      this.rotYaw = (float)(Math.atan2((double)xd, (double)zd) * 180.0D / 3.141592653589793D);
      this.rotPitch = (float)(Math.atan2((double)yd, var7) * 180.0D / 3.141592653589793D);
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      this.field_70547_e = age;
      Entity renderentity = FMLClientHandler.instance().getClient().func_175606_aa();
      int visibleDistance = 50;
      if (!FMLClientHandler.instance().getClient().field_71474_y.field_74347_j) {
         visibleDistance = 25;
      }

      if (renderentity.func_70011_f(p.field_70165_t, p.field_70163_u, p.field_70161_v) > (double)visibleDistance) {
         this.field_70547_e = 0;
      }

   }

   public void updateBeam(double x, double y, double z) {
      this.tX = x;
      this.tY = y;

      for(this.tZ = z; this.field_70547_e - this.field_70546_d < 4; ++this.field_70547_e) {
      }

   }

   public void func_189213_a() {
      this.field_187123_c = this.sourceEntity.field_70165_t;
      this.field_187124_d = this.sourceEntity.field_70163_u + this.offset;
      this.field_187125_e = this.sourceEntity.field_70161_v;
      this.ptX = this.tX;
      this.ptY = this.tY;
      this.ptZ = this.tZ;
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      float xd = (float)(this.sourceEntity.field_70165_t - this.tX);
      float yd = (float)(this.sourceEntity.field_70163_u + this.offset - this.tY);
      float zd = (float)(this.sourceEntity.field_70161_v - this.tZ);
      this.length = MathHelper.func_76129_c(xd * xd + yd * yd + zd * zd);
      double var7 = (double)MathHelper.func_76129_c(xd * xd + zd * zd);
      this.rotYaw = (float)(Math.atan2((double)xd, (double)zd) * 180.0D / 3.141592653589793D);

      for(this.rotPitch = (float)(Math.atan2((double)yd, var7) * 180.0D / 3.141592653589793D); this.rotPitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {
      }

      while(this.rotPitch - this.prevPitch >= 180.0F) {
         this.prevPitch += 360.0F;
      }

      while(this.rotYaw - this.prevYaw < -180.0F) {
         this.prevYaw -= 360.0F;
      }

      while(this.rotYaw - this.prevYaw >= 180.0F) {
         this.prevYaw += 360.0F;
      }

      if (this.impact > 0) {
         --this.impact;
      }

      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

   }

   public void setRGB(float r, float g, float b) {
      this.field_70552_h = r;
      this.field_70553_i = g;
      this.field_70551_j = b;
   }

   public void setType(int type) {
      this.type = type;
   }

   public void setEndMod(float endMod) {
      this.endMod = endMod;
   }

   public void setReverse(boolean reverse) {
      this.reverse = reverse;
   }

   public void setPulse(boolean pulse) {
      this.pulse = pulse;
   }

   public void setRotationspeed(int rotationspeed) {
      this.rotationspeed = rotationspeed;
   }

   public void func_180434_a(VertexBuffer wr, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      float var9 = 1.0F;
      float slide = (float)Minecraft.func_71410_x().field_71439_g.field_70173_aa;
      float rot = (float)(this.field_187122_b.field_73011_w.getWorldTime() % (long)(360 / this.rotationspeed) * (long)this.rotationspeed) + (float)this.rotationspeed * f;
      float size = 1.0F;
      if (this.pulse) {
         size = Math.min((float)this.field_70546_d / 4.0F, 1.0F);
         size = (float)((double)this.prevSize + (double)(size - this.prevSize) * (double)f);
      }

      float op = 0.4F;
      if (this.pulse && this.field_70547_e - this.field_70546_d <= 4) {
         op = 0.4F - (float)(4 - (this.field_70547_e - this.field_70546_d)) * 0.1F;
      }

      switch(this.type) {
      case 1:
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam1);
         break;
      case 2:
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam2);
         break;
      case 3:
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam3);
         break;
      default:
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam);
      }

      GL11.glTexParameterf(3553, 10242, 10497.0F);
      GL11.glTexParameterf(3553, 10243, 10497.0F);
      GL11.glDisable(2884);
      float var11 = slide + f;
      if (this.reverse) {
         var11 *= -1.0F;
      }

      float var12 = -var11 * 0.2F - (float)MathHelper.func_76141_d(-var11 * 0.1F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDepthMask(false);
      double prex = this.sourceEntity.field_70169_q;
      double prey = this.sourceEntity.field_70167_r + this.offset;
      double prez = this.sourceEntity.field_70166_s;
      double px = this.sourceEntity.field_70165_t;
      double py = this.sourceEntity.field_70163_u + this.offset;
      double pz = this.sourceEntity.field_70161_v;
      prex -= (double)(MathHelper.func_76134_b(this.sourceEntity.field_70126_B / 180.0F * 3.141593F) * 0.066F);
      prey -= 0.06D;
      prez -= (double)(MathHelper.func_76126_a(this.sourceEntity.field_70126_B / 180.0F * 3.141593F) * 0.04F);
      Vec3d vec3d = this.sourceEntity.func_70676_i(1.0F);
      prex += vec3d.field_72450_a * 0.3D;
      prey += vec3d.field_72448_b * 0.3D;
      prez += vec3d.field_72449_c * 0.3D;
      px -= (double)(MathHelper.func_76134_b(this.sourceEntity.field_70177_z / 180.0F * 3.141593F) * 0.066F);
      py -= 0.06D;
      pz -= (double)(MathHelper.func_76126_a(this.sourceEntity.field_70177_z / 180.0F * 3.141593F) * 0.04F);
      vec3d = this.sourceEntity.func_70676_i(1.0F);
      px += vec3d.field_72450_a * 0.3D;
      py += vec3d.field_72448_b * 0.3D;
      pz += vec3d.field_72449_c * 0.3D;
      float xx = (float)(prex + (px - prex) * (double)f - field_70556_an);
      float yy = (float)(prey + (py - prey) * (double)f - field_70554_ao);
      float zz = (float)(prez + (pz - prez) * (double)f - field_70555_ap);
      GL11.glTranslated((double)xx, (double)yy, (double)zz);
      float ry = (float)((double)this.prevYaw + (double)(this.rotYaw - this.prevYaw) * (double)f);
      float rp = (float)((double)this.prevPitch + (double)(this.rotPitch - this.prevPitch) * (double)f);
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
      GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);
      double var44 = -0.15D * (double)size;
      double var17 = 0.15D * (double)size;
      double var44b = -0.15D * (double)size * (double)this.endMod;
      double var17b = 0.15D * (double)size * (double)this.endMod;
      int i = 200;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);

      for(int t = 0; t < 3; ++t) {
         double var29 = (double)(this.length * size * var9);
         double var31 = 0.0D;
         double var33 = 1.0D;
         double var35 = (double)(-1.0F + var12 + (float)t / 3.0F);
         double var37 = (double)(this.length * size * var9) + var35;
         GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
         wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
         wr.func_181662_b(var44b, var29, 0.0D).func_187315_a(var33, var37).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, op).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b(var44, 0.0D, 0.0D).func_187315_a(var33, var35).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, op).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b(var17, 0.0D, 0.0D).func_187315_a(var31, var35).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, op).func_187314_a(j, k).func_181675_d();
         wr.func_181662_b(var17b, var29, 0.0D).func_187315_a(var31, var37).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, op).func_187314_a(j, k).func_181675_d();
         Tessellator.func_178181_a().func_78381_a();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDepthMask(true);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glPopMatrix();
      if (this.impact > 0) {
         this.renderImpact(Tessellator.func_178181_a(), f, f1, f2, f3, f4, f5);
      }

      Minecraft.func_71410_x().field_71446_o.func_110577_a(ParticleManager.field_110737_b);
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
      this.prevSize = size;
   }

   public void renderImpact(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(ParticleEngine.particleTexture);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F);
      int part = this.field_70546_d % 16;
      float var8 = (float)part / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.3125F;
      float var11 = var10 + 0.0624375F;
      float var12 = this.endMod / 2.0F / (float)(6 - this.impact);
      float var13 = (float)(this.ptX + (this.tX - this.ptX) * (double)f - field_70556_an);
      float var14 = (float)(this.ptY + (this.tY - this.ptY) * (double)f - field_70554_ao);
      float var15 = (float)(this.ptZ + (this.tZ - this.ptZ) * (double)f - field_70555_ap);
      float var16 = 1.0F;
      int i = 200;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181704_d);
      tessellator.func_178180_c().func_181662_b((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12)).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.66F).func_187314_a(j, k).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12)).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.66F).func_187314_a(j, k).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12)).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.66F).func_187314_a(j, k).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12)).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 0.66F).func_187314_a(j, k).func_181675_d();
      tessellator.func_78381_a();
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }
}
