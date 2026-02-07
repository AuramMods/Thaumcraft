package thaumcraft.client.fx;

import java.awt.Color;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleLava.Factory;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.beams.FXArc;
import thaumcraft.client.fx.beams.FXBeamBore;
import thaumcraft.client.fx.beams.FXBeamWand;
import thaumcraft.client.fx.beams.FXBolt;
import thaumcraft.client.fx.other.FXBlockWard;
import thaumcraft.client.fx.other.FXBoreStream;
import thaumcraft.client.fx.other.FXEssentiaStream;
import thaumcraft.client.fx.particles.FXBlockRunes;
import thaumcraft.client.fx.particles.FXBoreParticles;
import thaumcraft.client.fx.particles.FXBoreSparkle;
import thaumcraft.client.fx.particles.FXBreakingFade;
import thaumcraft.client.fx.particles.FXFireMote;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.client.fx.particles.FXGenericGui;
import thaumcraft.client.fx.particles.FXGenericP2E;
import thaumcraft.client.fx.particles.FXPlane;
import thaumcraft.client.fx.particles.FXSmokeSpiral;
import thaumcraft.client.fx.particles.FXSwarm;
import thaumcraft.client.fx.particles.FXVent;
import thaumcraft.client.fx.particles.FXVisSparkle;
import thaumcraft.client.fx.particles.FXWispEG;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.crafting.TileCrucible;

public class FXDispatcher {
   public static FXDispatcher INSTANCE = new FXDispatcher();
   static int q = 0;

   public World getWorld() {
      return FMLClientHandler.instance().getClient().field_71441_e;
   }

   public int particleCount(int base) {
      return FMLClientHandler.instance().getClient().field_71474_y.field_74362_aa == 2 ? base / 2 : (FMLClientHandler.instance().getClient().field_71474_y.field_74362_aa == 1 ? base * 1 : base * 2);
   }

   public void drawFireMote(float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float alpha, float scale) {
      FXFireMote glow = new FXFireMote(this.getWorld(), (double)x, (double)y, (double)z, (double)vx, (double)vy, (double)vz, r, g, b, scale, 0);
      glow.func_82338_g(alpha);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), glow);
   }

   public void drawAlumentum(float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float alpha, float scale) {
      FXFireMote glow = new FXFireMote(this.getWorld(), (double)x, (double)y, (double)z, (double)vx, (double)vy, (double)vz, r, g, b, scale, 1);
      glow.func_82338_g(alpha);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), glow);
   }

   public void drawTaintParticles(float x, float y, float z, float vx, float vy, float vz, float scale) {
      FXGeneric fb = new FXGeneric(this.getWorld(), (double)x, (double)y, (double)z, (double)vx, (double)vy, (double)vz);
      fb.func_187114_a(80 + this.getWorld().field_73012_v.nextInt(20));
      fb.func_70538_b(0.4F + this.getWorld().field_73012_v.nextFloat() * 0.2F, 0.1F + this.getWorld().field_73012_v.nextFloat() * 0.3F, 0.5F + this.getWorld().field_73012_v.nextFloat() * 0.2F);
      fb.setAlphaF(0.75F, 0.0F);
      fb.setGridSize(16);
      fb.setParticles(57 + this.getWorld().field_73012_v.nextInt(3), 1, 1);
      fb.setScale(scale, scale / 4.0F);
      fb.setLayer(1);
      fb.setSlowDown(0.9750000238418579D);
      fb.setGravity(0.2F);
      fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawLightningFlash(double x, double y, double z, float r, float g, float b, float alpha, float scale) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, 0.0D, 0.0D, 0.0D);
      fb.func_187114_a(5 + this.getWorld().field_73012_v.nextInt(5));
      fb.setGridSize(16);
      fb.func_70538_b(r, g, b);
      fb.setAlphaF(alpha, 0.0F);
      fb.setParticles(108 + this.getWorld().field_73012_v.nextInt(4), 1, 1);
      fb.setScale(scale);
      fb.setLayer(0);
      fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), 0.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawGenericParticles(double x, double y, double z, double x2, double y2, double z2, float r, float g, float b, float alpha, boolean loop, int start, int num, int inc, int age, int delay, float scale, float rot, int layer) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
      fb.func_187114_a(age);
      fb.func_70538_b(r, g, b);
      fb.func_82338_g(alpha);
      fb.setLoop(loop);
      fb.setParticles(start, num, inc);
      fb.setScale(scale);
      fb.setLayer(layer);
      fb.setRotationSpeed(rot);
      ParticleEngine.INSTANCE.addEffectWithDelay(this.getWorld(), fb, delay);
   }

   public void drawLevitatorParticles(double x, double y, double z, double x2, double y2, double z2) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
      fb.func_187114_a(200 + this.getWorld().field_73012_v.nextInt(100));
      fb.func_70538_b(0.5F, 0.5F, 0.2F);
      fb.setAlphaF(0.3F, 0.0F);
      fb.setGridSize(16);
      fb.setParticles(56, 1, 1);
      fb.setScale(2.0F, 5.0F);
      fb.setLayer(0);
      fb.setSlowDown(1.0D);
      fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawGolemFlyParticles(double x, double y, double z, double x2, double y2, double z2) {
      try {
         FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
         fb.func_187114_a(40 + this.getWorld().field_73012_v.nextInt(20));
         fb.setAlphaF(0.2F, 0.0F);
         fb.setGridSize(8);
         fb.setParticles(24, 1, 1);
         fb.setScale(2.0F, 8.0F);
         fb.setLayer(0);
         fb.setSlowDown(1.0D);
         fb.setWind(0.001D);
         fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
      } catch (Exception var14) {
      }

   }

   public void drawPollutionParticles(BlockPos p) {
      float x = (float)p.func_177958_n() + 0.2F + this.getWorld().field_73012_v.nextFloat() * 0.6F;
      float y = (float)p.func_177956_o() + 0.2F + this.getWorld().field_73012_v.nextFloat() * 0.6F;
      float z = (float)p.func_177952_p() + 0.2F + this.getWorld().field_73012_v.nextFloat() * 0.6F;
      FXGeneric fb = new FXGeneric(this.getWorld(), (double)x, (double)y, (double)z, (double)(this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.005D, 0.02D, (double)(this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.005D);
      fb.func_187114_a(400 + this.getWorld().field_73012_v.nextInt(100));
      fb.func_70538_b(1.0F, 0.3F, 0.9F);
      fb.setAlphaF(0.5F, 0.0F);
      fb.setGridSize(16);
      fb.setParticles(56, 1, 1);
      fb.setScale(2.0F, 5.0F);
      fb.setLayer(1);
      fb.setSlowDown(1.0D);
      fb.setWind(0.001D);
      fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawBlockSparkles(BlockPos p, Vec3d start) {
      AxisAlignedBB bs = this.getWorld().func_180495_p(p).func_185900_c(this.getWorld(), p);
      bs.func_72314_b(0.1D, 0.1D, 0.1D);
      int num = (int)(bs.func_72320_b() * 20.0D);
      EnumFacing[] var5 = EnumFacing.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumFacing face = var5[var7];
         IBlockState state = this.getWorld().func_180495_p(p.func_177972_a(face));
         if (!state.func_185914_p() && !state.isSideSolid(this.getWorld(), p.func_177972_a(face), face.func_176734_d())) {
            boolean rx = face.func_82601_c() == 0;
            boolean ry = face.func_96559_d() == 0;
            boolean rz = face.func_82599_e() == 0;
            double mx = 0.5D + (double)face.func_82601_c() * 0.51D;
            double my = 0.5D + (double)face.func_96559_d() * 0.51D;
            double mz = 0.5D + (double)face.func_82599_e() * 0.51D;

            for(int a = 0; a < this.particleCount(num); ++a) {
               double x = mx;
               double y = my;
               double z = mz;
               if (rx) {
                  x = mx + this.getWorld().field_73012_v.nextGaussian() * 0.6D;
               }

               if (ry) {
                  y = my + this.getWorld().field_73012_v.nextGaussian() * 0.6D;
               }

               if (rz) {
                  z = mz + this.getWorld().field_73012_v.nextGaussian() * 0.6D;
               }

               x = MathHelper.func_151237_a(x, bs.field_72340_a, bs.field_72336_d);
               y = MathHelper.func_151237_a(y, bs.field_72338_b, bs.field_72337_e);
               z = MathHelper.func_151237_a(z, bs.field_72339_c, bs.field_72334_f);
               float r = (float)MathHelper.func_76136_a(this.getWorld().field_73012_v, 255, 255) / 255.0F;
               float g = (float)MathHelper.func_76136_a(this.getWorld().field_73012_v, 189, 255) / 255.0F;
               float b = (float)MathHelper.func_76136_a(this.getWorld().field_73012_v, 64, 255) / 255.0F;
               Vec3d v1 = new Vec3d((double)p.func_177958_n() + x, (double)p.func_177956_o() + y, (double)p.func_177952_p() + z);
               double delay = (double)this.getWorld().field_73012_v.nextInt(5) + v1.func_72438_d(start) * 16.0D;
               this.drawSimpleSparkle(this.getWorld().field_73012_v, (double)p.func_177958_n() + x, (double)p.func_177956_o() + y, (double)p.func_177952_p() + z, 0.0D, 0.0025D, 0.0D, 0.4F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.1F, r, g, b, (int)delay, 1.0F, 0.01F, false, 16);
            }
         }
      }

   }

   public void drawSimpleSparkle(Random rand, double x, double y, double z, double x2, double y2, double z2, float scale, float r, float g, float b, int delay, float decay, float grav, boolean thaumclamp, int baseAge) {
      boolean sp = (double)rand.nextFloat() < 0.2D;
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
      int age = baseAge * 4 + this.getWorld().field_73012_v.nextInt(baseAge);
      fb.func_187114_a(age);
      fb.func_70538_b(r, g, b);
      float[] alphas = new float[6 + rand.nextInt(age / 3)];

      for(int a = 1; a < alphas.length - 1; ++a) {
         alphas[a] = rand.nextFloat();
      }

      fb.setAlphaF(alphas);
      fb.setParticles(sp ? 320 : 512, 16, 1);
      fb.setLoop(true);
      fb.setGravity(grav);
      fb.setScale(scale, scale * 2.0F);
      fb.setLayer(0);
      fb.setSlowDown((double)decay);
      fb.setRandomMovementScale(5.0E-4F, 0.001F, 5.0E-4F);
      fb.setWind(5.0E-4D);
      fb.setThaumClamp(thaumclamp);
      ParticleEngine.INSTANCE.addEffectWithDelay(this.getWorld(), fb, delay);
   }

   public void drawSimpleSparkleGui(Random rand, double x, double y, double x2, double y2, float scale, float r, float g, float b, int delay, float decay, float grav) {
      boolean sp = (double)rand.nextFloat() < 0.2D;
      FXGenericGui fb = new FXGenericGui(this.getWorld(), x, y, 0.0D, x2, y2, 0.0D);
      fb.func_187114_a(32 + this.getWorld().field_73012_v.nextInt(8));
      fb.func_70538_b(r, g, b);
      fb.setAlphaF(new float[]{0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F});
      fb.setParticles(sp ? 320 : 512, 16, 1);
      fb.setLoop(true);
      fb.setGravity(grav);
      fb.setScale(new float[]{scale, scale * 2.0F});
      fb.setNoClip(false);
      fb.setLayer(4);
      fb.setSlowDown((double)decay);
      fb.setRandomMovementScale(0.025F, 0.025F, 0.0F);
      ParticleEngine.INSTANCE.addEffectWithDelay(this.getWorld(), fb, delay);
   }

   public void drawBlockMistParticles(BlockPos p, int c) {
      AxisAlignedBB bs = this.getWorld().func_180495_p(p).func_185900_c(this.getWorld(), p);
      Color color = new Color(c);

      for(int a = 0; a < this.particleCount(4); ++a) {
         double x = (double)p.func_177958_n() + bs.field_72340_a + (double)this.getWorld().field_73012_v.nextFloat() * (bs.field_72336_d - bs.field_72340_a);
         double y = (double)p.func_177956_o() + bs.field_72338_b + (double)this.getWorld().field_73012_v.nextFloat() * (bs.field_72337_e - bs.field_72338_b);
         double z = (double)p.func_177952_p() + bs.field_72339_c + (double)this.getWorld().field_73012_v.nextFloat() * (bs.field_72334_f - bs.field_72339_c);
         FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, this.getWorld().field_73012_v.nextGaussian() * 0.01D, (double)this.getWorld().field_73012_v.nextFloat() * 0.075D, this.getWorld().field_73012_v.nextGaussian() * 0.01D);
         fb.func_187114_a(50 + this.getWorld().field_73012_v.nextInt(25));
         fb.func_70538_b((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
         fb.setAlphaF(0.0F, 0.5F, 0.4F, 0.3F, 0.2F, 0.1F, 0.0F);
         fb.setGridSize(16);
         fb.setParticles(56, 1, 1);
         fb.setScale(5.0F, 1.0F);
         fb.setLayer(0);
         fb.setSlowDown(1.0D);
         fb.setGravity(0.1F);
         fb.setWind(0.001D);
         fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
      }

   }

   public void drawWispyMotesOnBlock(BlockPos pp, int age, float grav) {
      this.drawWispyMotes((double)((float)pp.func_177958_n() + this.getWorld().field_73012_v.nextFloat()), (double)pp.func_177956_o(), (double)((float)pp.func_177952_p() + this.getWorld().field_73012_v.nextFloat()), 0.0D, 0.0D, 0.0D, age, 0.4F + this.getWorld().field_73012_v.nextFloat() * 0.6F, 0.6F + this.getWorld().field_73012_v.nextFloat() * 0.4F, 0.6F + this.getWorld().field_73012_v.nextFloat() * 0.4F, grav);
   }

   public void drawWispyMotes(double d, double e, double f, double vx, double vy, double vz, int age, float grav) {
      this.drawWispyMotes(d, e, f, vx, vy, vz, age, 0.25F + this.getWorld().field_73012_v.nextFloat() * 0.75F, 0.25F + this.getWorld().field_73012_v.nextFloat() * 0.75F, 0.25F + this.getWorld().field_73012_v.nextFloat() * 0.75F, grav);
   }

   public void drawWispyMotes(double d, double e, double f, double vx, double vy, double vz, int age, float r, float g, float b, float grav) {
      FXGeneric fb = new FXGeneric(this.getWorld(), d, e, f, vx, vy, vz);
      fb.func_187114_a((int)((float)age + (float)(age / 2) * this.getWorld().field_73012_v.nextFloat()));
      fb.func_70538_b(r, g, b);
      fb.setAlphaF(0.0F, 0.6F, 0.6F, 0.0F);
      fb.setGridSize(64);
      fb.setParticles(512, 16, 1);
      fb.setScale(1.0F, 0.5F);
      fb.setLoop(true);
      fb.setWind(0.001D);
      fb.setGravity(grav);
      fb.setRandomMovementScale(0.0025F, 0.0F, 0.0025F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawBlockMistParticlesFlat(BlockPos p, int c) {
      this.getWorld().func_180495_p(p).func_177230_c();
      Color color = new Color(c);

      for(int a = 0; a < this.particleCount(3); ++a) {
         double x = (double)((float)p.func_177958_n() + this.getWorld().field_73012_v.nextFloat());
         double y = (double)((float)p.func_177956_o() + this.getWorld().field_73012_v.nextFloat() * 0.125F);
         double z = (double)((float)p.func_177952_p() + this.getWorld().field_73012_v.nextFloat());
         FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, (double)(this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.005D, 0.005D, (double)(this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.005D);
         fb.func_187114_a(400 + this.getWorld().field_73012_v.nextInt(100));
         fb.func_70538_b((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
         fb.setAlphaF(1.0F, 0.0F);
         fb.setGridSize(8);
         fb.setParticles(24, 1, 1);
         fb.setScale(2.0F, 5.0F);
         fb.setLayer(0);
         fb.setSlowDown(1.0D);
         fb.setWind(0.001D);
         fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
      }

   }

   public void crucibleBubble(float x, float y, float z, float cr, float cg, float cb) {
      FXGeneric fb = new FXGeneric(this.getWorld(), (double)x, (double)y, (double)z, 0.0D, 0.0D, 0.0D);
      fb.func_187114_a(15 + this.getWorld().field_73012_v.nextInt(10));
      fb.setScale(this.getWorld().field_73012_v.nextFloat() * 0.3F + 0.3F);
      fb.func_70538_b(cr, cg, cb);
      fb.setRandomMovementScale(0.002F, 0.002F, 0.002F);
      fb.setGravity(-0.001F);
      fb.setParticle(64);
      fb.setFinalFrames(65, 66, 66);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void crucibleBoil(BlockPos pos, TileCrucible tile, int j) {
      for(int a = 0; a < this.particleCount(1); ++a) {
         FXGeneric fb = new FXGeneric(this.getWorld(), (double)((float)pos.func_177958_n() + 0.2F + this.getWorld().field_73012_v.nextFloat() * 0.6F), (double)((float)pos.func_177956_o() + 0.1F + tile.getFluidHeight()), (double)((float)pos.func_177952_p() + 0.2F + this.getWorld().field_73012_v.nextFloat() * 0.6F), 0.0D, 0.002D, 0.0D);
         fb.func_187114_a((int)(7.0D + 8.0D / (Math.random() * 0.8D + 0.2D)));
         fb.setScale(this.getWorld().field_73012_v.nextFloat() * 0.3F + 0.2F);
         if (tile.aspects.size() == 0) {
            fb.func_70538_b(1.0F, 1.0F, 1.0F);
         } else {
            Color color = new Color(tile.aspects.getAspects()[this.getWorld().field_73012_v.nextInt(tile.aspects.getAspects().length)].getColor());
            fb.func_70538_b((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
         }

         fb.setRandomMovementScale(0.001F, 0.001F, 0.001F);
         fb.setGravity(-0.025F * (float)j);
         fb.setParticle(64);
         fb.setFinalFrames(65, 66);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
      }

   }

   public void crucibleFroth(float x, float y, float z) {
      FXGeneric fb = new FXGeneric(this.getWorld(), (double)x, (double)y, (double)z, 0.0D, 0.0D, 0.0D);
      fb.func_187114_a(4 + this.getWorld().field_73012_v.nextInt(3));
      fb.setScale(this.getWorld().field_73012_v.nextFloat() * 0.2F + 0.2F);
      fb.func_70538_b(0.5F, 0.5F, 0.7F);
      fb.setRandomMovementScale(0.001F, 0.001F, 0.001F);
      fb.setGravity(0.1F);
      fb.setParticle(64);
      fb.setFinalFrames(65, 66);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void crucibleFrothDown(float x, float y, float z) {
      FXGeneric fb = new FXGeneric(this.getWorld(), (double)x, (double)y, (double)z, 0.0D, 0.0D, 0.0D);
      fb.func_187114_a(12 + this.getWorld().field_73012_v.nextInt(12));
      fb.setScale(this.getWorld().field_73012_v.nextFloat() * 0.2F + 0.4F);
      fb.func_70538_b(0.25F, 0.0F, 0.75F);
      fb.func_82338_g(0.8F);
      fb.setRandomMovementScale(0.001F, 0.001F, 0.001F);
      fb.setGravity(0.05F);
      fb.setNoClip(false);
      fb.setParticle(73);
      fb.setFinalFrames(65, 66);
      fb.setLayer(1);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawBamf(BlockPos p, boolean sound, boolean flair, EnumFacing side) {
      this.drawBamf((double)p.func_177958_n() + 0.5D, (double)p.func_177956_o() + 0.5D, (double)p.func_177952_p() + 0.5D, sound, flair, side);
   }

   public void drawBamf(BlockPos p, float r, float g, float b, boolean sound, boolean flair, EnumFacing side) {
      this.drawBamf((double)p.func_177958_n() + 0.5D, (double)p.func_177956_o() + 0.5D, (double)p.func_177952_p() + 0.5D, r, g, b, sound, flair, side);
   }

   public void drawBamf(BlockPos p, int color, boolean sound, boolean flair, EnumFacing side) {
      this.drawBamf((double)p.func_177958_n() + 0.5D, (double)p.func_177956_o() + 0.5D, (double)p.func_177952_p() + 0.5D, color, sound, flair, side);
   }

   public void drawBamf(double x, double y, double z, int color, boolean sound, boolean flair, EnumFacing side) {
      Color c = new Color(color);
      float r = (float)c.getRed() / 255.0F;
      float g = (float)c.getGreen() / 255.0F;
      float b = (float)c.getBlue() / 255.0F;
      this.drawBamf(x, y, z, r, g, b, sound, flair, side);
   }

   public void drawBamf(double x, double y, double z, boolean sound, boolean flair, EnumFacing side) {
      this.drawBamf(x, y, z, 0.5F, 0.1F, 0.6F, sound, flair, side);
   }

   public void drawBamf(double x, double y, double z, float r, float g, float b, boolean sound, boolean flair, EnumFacing side) {
      if (sound) {
         this.getWorld().func_184134_a(x, y, z, SoundsTC.poof, SoundCategory.BLOCKS, 0.4F, 1.0F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.05F, false);
      }

      int a;
      double vx;
      double vy;
      double vz;
      FXGeneric fb2;
      for(a = 0; a < this.particleCount(3) + this.getWorld().field_73012_v.nextInt(3) + 2; ++a) {
         vx = (double)((0.05F + this.getWorld().field_73012_v.nextFloat() * 0.05F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
         vy = (double)((0.05F + this.getWorld().field_73012_v.nextFloat() * 0.05F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
         vz = (double)((0.05F + this.getWorld().field_73012_v.nextFloat() * 0.05F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
         if (side != null) {
            vx += (double)((float)side.func_82601_c() * 0.1F);
            vy += (double)((float)side.func_96559_d() * 0.1F);
            vz += (double)((float)side.func_82599_e() * 0.1F);
         }

         fb2 = new FXGeneric(this.getWorld(), x + vx * 2.0D, y + vy * 2.0D, z + vz * 2.0D, vx / 2.0D, vy / 2.0D, vz / 2.0D);
         fb2.func_187114_a(20 + this.getWorld().field_73012_v.nextInt(15));
         fb2.func_70538_b(MathHelper.func_76131_a(r * (1.0F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.1F), 0.0F, 1.0F), MathHelper.func_76131_a(g * (1.0F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.1F), 0.0F, 1.0F), MathHelper.func_76131_a(b * (1.0F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.1F), 0.0F, 1.0F));
         fb2.setAlphaF(1.0F, 0.5F);
         fb2.setGridSize(16);
         fb2.setParticles(123, 5, 1);
         fb2.setScale(3.0F, 4.0F + this.getWorld().field_73012_v.nextFloat() * 3.0F);
         fb2.setLayer(1);
         fb2.setSlowDown(0.7D);
         fb2.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -1.0F : 1.0F);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb2);
      }

      if (flair) {
         for(a = 0; a < this.particleCount(1) + this.getWorld().field_73012_v.nextInt(3); ++a) {
            vx = (double)((0.025F + this.getWorld().field_73012_v.nextFloat() * 0.025F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
            vy = (double)((0.025F + this.getWorld().field_73012_v.nextFloat() * 0.025F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
            vz = (double)((0.025F + this.getWorld().field_73012_v.nextFloat() * 0.025F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
            this.drawWispyMotes(x + vx * 2.0D, y + vy * 2.0D, z + vz * 2.0D, vx, vy, vz, 15 + this.getWorld().field_73012_v.nextInt(10), -0.01F);
         }

         FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, 0.0D, 0.0D, 0.0D);
         fb.func_187114_a(10 + this.getWorld().field_73012_v.nextInt(5));
         fb.func_70538_b(1.0F, 0.9F, 1.0F);
         fb.setAlphaF(1.0F, 0.0F);
         fb.setGridSize(16);
         fb.setParticles(77, 1, 1);
         fb.setScale(10.0F + this.getWorld().field_73012_v.nextFloat() * 2.0F, 0.0F);
         fb.setLayer(0);
         fb.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), (float)this.getWorld().field_73012_v.nextGaussian());
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
      }

      for(a = 0; a < this.particleCount(flair ? 1 : 0) + this.getWorld().field_73012_v.nextInt(3); ++a) {
         vx = (double)((0.0025F + this.getWorld().field_73012_v.nextFloat() * 0.005F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
         vy = (double)((0.0025F + this.getWorld().field_73012_v.nextFloat() * 0.005F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
         vz = (double)((0.0025F + this.getWorld().field_73012_v.nextFloat() * 0.005F) * (float)(this.getWorld().field_73012_v.nextBoolean() ? -1 : 1));
         if (side != null) {
            vx += (double)((float)side.func_82601_c() * 0.025F);
            vy += (double)((float)side.func_96559_d() * 0.025F);
            vz += (double)((float)side.func_82599_e() * 0.025F);
         }

         fb2 = new FXGeneric(this.getWorld(), x + vx * 5.0D, y + vy * 5.0D, z + vz * 5.0D, vx, vy, vz);
         if (a > 0 && this.getWorld().field_73012_v.nextBoolean()) {
            fb2.setAngles(90.0F * (float)this.getWorld().field_73012_v.nextGaussian(), 90.0F * (float)this.getWorld().field_73012_v.nextGaussian());
         }

         fb2.func_187114_a(25 + this.getWorld().field_73012_v.nextInt(20 + 20 * a));
         fb2.setRBGColorF((0.9F + this.getWorld().field_73012_v.nextFloat() * 0.1F + r) / 2.0F, (0.1F + g) / 2.0F, (0.5F + this.getWorld().field_73012_v.nextFloat() * 0.1F + b) / 2.0F, 0.1F + this.getWorld().field_73012_v.nextFloat() * 0.1F, 0.0F, 0.5F + this.getWorld().field_73012_v.nextFloat() * 0.1F);
         fb2.setAlphaF(0.75F, 0.0F);
         fb2.setGridSize(16);
         fb2.setParticles(60 + this.getWorld().field_73012_v.nextInt(4), 1, 1);
         fb2.setScale(5.0F, 10.0F + this.getWorld().field_73012_v.nextFloat() * 4.0F);
         fb2.setLayer(0);
         fb2.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -2.0F - this.getWorld().field_73012_v.nextFloat() * 2.0F : 2.0F + this.getWorld().field_73012_v.nextFloat() * 2.0F);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb2);
      }

   }

   public void pechsCurseTick(double posX, double posY, double posZ) {
      FXGeneric fb2 = new FXGeneric(this.getWorld(), posX, posY, posZ, 0.0D, 0.0D, 0.0D);
      fb2.setAngles(90.0F * (float)this.getWorld().field_73012_v.nextGaussian(), 90.0F * (float)this.getWorld().field_73012_v.nextGaussian());
      fb2.func_187114_a(50 + this.getWorld().field_73012_v.nextInt(50));
      fb2.setRBGColorF(0.9F, 0.1F, 0.5F, 0.1F + this.getWorld().field_73012_v.nextFloat() * 0.1F, 0.0F, 0.5F + this.getWorld().field_73012_v.nextFloat() * 0.1F);
      fb2.setAlphaF(0.75F, 0.0F);
      fb2.setGridSize(8);
      fb2.setParticles(28 + this.getWorld().field_73012_v.nextInt(4), 1, 1);
      fb2.setScale(3.0F, 5.0F + this.getWorld().field_73012_v.nextFloat() * 2.0F);
      fb2.setLayer(0);
      fb2.setRotationSpeed(this.getWorld().field_73012_v.nextFloat(), this.getWorld().field_73012_v.nextBoolean() ? -3.0F - this.getWorld().field_73012_v.nextFloat() * 3.0F : 3.0F + this.getWorld().field_73012_v.nextFloat() * 3.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb2);
      this.drawWispyMotes(posX, posY, posZ, 0.0D, 0.0D, 0.0D, 10 + this.getWorld().field_73012_v.nextInt(10), -0.01F);
   }

   public void scanHighlight(BlockPos p) {
      AxisAlignedBB bb = this.getWorld().func_180495_p(p).func_185900_c(this.getWorld(), p);
      bb = bb.func_186670_a(p);
      this.scanHighlight(bb);
   }

   public void scanHighlight(Entity e) {
      AxisAlignedBB bb = e.func_174813_aQ();
      this.scanHighlight(bb);
   }

   public void scanHighlight(AxisAlignedBB bb) {
      int num = MathHelper.func_76143_f(bb.func_72320_b() * 2.0D);
      double ax = (bb.field_72340_a + bb.field_72336_d) / 2.0D;
      double ay = (bb.field_72338_b + bb.field_72337_e) / 2.0D;
      double az = (bb.field_72339_c + bb.field_72334_f) / 2.0D;
      EnumFacing[] var9 = EnumFacing.values();
      int var10 = var9.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         EnumFacing face = var9[var11];
         boolean rx = face.func_82601_c() == 0;
         boolean ry = face.func_96559_d() == 0;
         boolean rz = face.func_82599_e() == 0;
         double mx = 0.5D + (double)face.func_82601_c() * 0.51D;
         double my = 0.5D + (double)face.func_96559_d() * 0.51D;
         double mz = 0.5D + (double)face.func_82599_e() * 0.51D;

         for(int a = 0; a < this.particleCount(num); ++a) {
            double x = mx + this.getWorld().field_73012_v.nextGaussian() * (bb.field_72336_d - bb.field_72340_a);
            double y = my + this.getWorld().field_73012_v.nextGaussian() * (bb.field_72337_e - bb.field_72338_b);
            double z = mz + this.getWorld().field_73012_v.nextGaussian() * (bb.field_72334_f - bb.field_72339_c);
            x = MathHelper.func_151237_a(x, bb.field_72340_a - ax, bb.field_72336_d - ax);
            y = MathHelper.func_151237_a(y, bb.field_72338_b - ay, bb.field_72337_e - ay);
            z = MathHelper.func_151237_a(z, bb.field_72339_c - az, bb.field_72334_f - az);
            float r = (float)MathHelper.func_76136_a(this.getWorld().field_73012_v, 16, 32) / 255.0F;
            float g = (float)MathHelper.func_76136_a(this.getWorld().field_73012_v, 132, 165) / 255.0F;
            float b = (float)MathHelper.func_76136_a(this.getWorld().field_73012_v, 223, 239) / 255.0F;
            this.drawSimpleSparkle(this.getWorld().field_73012_v, ax + x, ay + y, az + z, 0.0D, 0.0D, 0.0D, 0.4F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.1F, r, g, b, this.getWorld().field_73012_v.nextInt(10), 1.0F, 0.0F, true, 4);
         }
      }

   }

   public void sparkle(float x, float y, float z, float r, float g, float b) {
      if (this.getWorld().field_73012_v.nextInt(6) < this.particleCount(2)) {
         this.drawGenericParticles((double)x, (double)y, (double)z, 0.0D, 0.0D, 0.0D, r, g, b, 0.9F, true, 320, 16, 1, 6 + this.getWorld().field_73012_v.nextInt(4), 0, 0.6F + this.getWorld().field_73012_v.nextFloat() * 0.2F, 0.0F, 0);
      }

   }

   public void visSparkle(int x, int y, int z, int x2, int y2, int z2, int color) {
      FXVisSparkle fb = new FXVisSparkle(this.getWorld(), (double)((float)x + this.getWorld().field_73012_v.nextFloat()), (double)((float)y + this.getWorld().field_73012_v.nextFloat()), (double)((float)z + this.getWorld().field_73012_v.nextFloat()), (double)x2 + 0.4D + (double)(this.getWorld().field_73012_v.nextFloat() * 0.2F), (double)y2 + 0.4D + (double)(this.getWorld().field_73012_v.nextFloat() * 0.2F), (double)z2 + 0.4D + (double)(this.getWorld().field_73012_v.nextFloat() * 0.2F));
      Color c = new Color(color);
      fb.func_70538_b((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void splooshFX(Entity e) {
      float f = this.getWorld().field_73012_v.nextFloat() * 3.1415927F * 2.0F;
      float f1 = this.getWorld().field_73012_v.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.func_76126_a(f) * 2.0F * 0.5F * f1;
      float f3 = MathHelper.func_76134_b(f) * 2.0F * 0.5F * f1;
      FXBreakingFade fx = new FXBreakingFade(this.getWorld(), e.field_70165_t + (double)f2, e.field_70163_u + (double)(this.getWorld().field_73012_v.nextFloat() * e.field_70131_O), e.field_70161_v + (double)f3, Items.field_151123_aH, 0);
      if (this.getWorld().field_73012_v.nextBoolean()) {
         fx.func_70538_b(0.6F, 0.0F, 0.3F);
         fx.func_82338_g(0.4F);
      } else {
         fx.func_70538_b(0.3F, 0.0F, 0.3F);
         fx.func_82338_g(0.6F);
      }

      fx.setParticleMaxAge((int)(66.0F / (this.getWorld().field_73012_v.nextFloat() * 0.9F + 0.1F)));
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fx);
   }

   public void taintsplosionFX(Entity e) {
      FXBreakingFade fx = new FXBreakingFade(this.getWorld(), e.field_70165_t, e.field_70163_u + (double)(this.getWorld().field_73012_v.nextFloat() * e.field_70131_O), e.field_70161_v, Items.field_151123_aH);
      if (this.getWorld().field_73012_v.nextBoolean()) {
         fx.func_70538_b(0.6F, 0.0F, 0.3F);
         fx.func_82338_g(0.4F);
      } else {
         fx.func_70538_b(0.3F, 0.0F, 0.3F);
         fx.func_82338_g(0.6F);
      }

      fx.setSpeed(Math.random() * 2.0D - 1.0D, Math.random() * 2.0D - 1.0D, Math.random() * 2.0D - 1.0D);
      fx.boom();
      fx.setParticleMaxAge((int)(66.0F / (this.getWorld().field_73012_v.nextFloat() * 0.9F + 0.1F)));
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fx);
   }

   public void tentacleAriseFX(Entity e) {
      for(int j = 0; (float)j < 2.0F * e.field_70131_O; ++j) {
         float f = this.getWorld().field_73012_v.nextFloat() * 3.1415927F * e.field_70131_O;
         float f1 = this.getWorld().field_73012_v.nextFloat() * 0.5F + 0.5F;
         float f2 = MathHelper.func_76126_a(f) * e.field_70131_O * 0.25F * f1;
         float f3 = MathHelper.func_76134_b(f) * e.field_70131_O * 0.25F * f1;
         FXBreakingFade fx = new FXBreakingFade(this.getWorld(), e.field_70165_t + (double)f2, e.field_70163_u, e.field_70161_v + (double)f3, Items.field_151123_aH);
         fx.func_70538_b(0.4F, 0.0F, 0.4F);
         fx.func_82338_g(0.5F);
         fx.setParticleMaxAge((int)(66.0F / (this.getWorld().field_73012_v.nextFloat() * 0.9F + 0.1F)));
         FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fx);
         if (!this.getWorld().func_175623_d(e.func_180425_c().func_177977_b())) {
            f = this.getWorld().field_73012_v.nextFloat() * 3.1415927F * e.field_70131_O;
            f1 = this.getWorld().field_73012_v.nextFloat() * 0.5F + 0.5F;
            f2 = MathHelper.func_76126_a(f) * e.field_70131_O * 0.25F * f1;
            f3 = MathHelper.func_76134_b(f) * e.field_70131_O * 0.25F * f1;
            this.getWorld().func_175688_a(EnumParticleTypes.BLOCK_CRACK, e.field_70165_t + (double)f2, e.field_70163_u, e.field_70161_v + (double)f3, 0.0D, 0.0D, 0.0D, new int[]{Block.func_176210_f(this.getWorld().func_180495_p(e.func_180425_c().func_177977_b()))});
         }
      }

   }

   public void slimeJumpFX(Entity e, int i) {
      float f = this.getWorld().field_73012_v.nextFloat() * 3.1415927F * 2.0F;
      float f1 = this.getWorld().field_73012_v.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.func_76126_a(f) * (float)i * 0.5F * f1;
      float f3 = MathHelper.func_76134_b(f) * (float)i * 0.5F * f1;
      FXBreakingFade fx = new FXBreakingFade(this.getWorld(), e.field_70165_t + (double)f2, (e.func_174813_aQ().field_72338_b + e.func_174813_aQ().field_72337_e) / 2.0D, e.field_70161_v + (double)f3, Items.field_151123_aH, 0);
      fx.func_70538_b(0.7F, 0.0F, 1.0F);
      fx.func_82338_g(0.4F);
      fx.setParticleMaxAge((int)(66.0F / (this.getWorld().field_73012_v.nextFloat() * 0.9F + 0.1F)));
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fx);
   }

   public void taintLandFX(Entity e) {
      float f = this.getWorld().field_73012_v.nextFloat() * 3.1415927F * 2.0F;
      float f1 = this.getWorld().field_73012_v.nextFloat() * 0.5F + 0.5F;
      float f2 = MathHelper.func_76126_a(f) * 2.0F * 0.5F * f1;
      float f3 = MathHelper.func_76134_b(f) * 2.0F * 0.5F * f1;
      if (this.getWorld().field_72995_K) {
         FXBreakingFade fx = new FXBreakingFade(this.getWorld(), e.field_70165_t + (double)f2, (e.func_174813_aQ().field_72338_b + e.func_174813_aQ().field_72337_e) / 2.0D, e.field_70161_v + (double)f3, Items.field_151123_aH);
         fx.func_70538_b(0.1F, 0.0F, 0.1F);
         fx.func_82338_g(0.4F);
         fx.setParticleMaxAge((int)(66.0F / (this.getWorld().field_73012_v.nextFloat() * 0.9F + 0.1F)));
         FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fx);
      }

   }

   public void drawInfusionParticles1(double x, double y, double z, BlockPos pos, Item id, int md) {
      FXBoreParticles fb = (new FXBoreParticles(this.getWorld(), x, y, z, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() - 0.5D, (double)pos.func_177952_p() + 0.5D, (double)((float)this.getWorld().field_73012_v.nextGaussian() * 0.03F), (double)((float)this.getWorld().field_73012_v.nextGaussian() * 0.03F), (double)((float)this.getWorld().field_73012_v.nextGaussian() * 0.03F), id, md)).getObjectColor(pos);
      fb.func_82338_g(0.3F);
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
   }

   public void drawInfusionParticles2(double x, double y, double z, BlockPos pos, IBlockState id, int md) {
      FXBoreParticles fb = (new FXBoreParticles(this.getWorld(), x, y, z, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() - 0.5D, (double)pos.func_177952_p() + 0.5D, id, md)).getObjectColor(pos);
      fb.func_82338_g(0.3F);
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
   }

   public void drawInfusionParticles3(double x, double y, double z, int x2, int y2, int z2) {
      FXBoreSparkle fb = new FXBoreSparkle(this.getWorld(), x, y, z, (double)x2 + 0.5D, (double)y2 - 0.5D, (double)z2 + 0.5D);
      fb.func_70538_b(0.4F + this.getWorld().field_73012_v.nextFloat() * 0.2F, 0.2F, 0.6F + this.getWorld().field_73012_v.nextFloat() * 0.3F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawInfusionParticles4(double x, double y, double z, int x2, int y2, int z2) {
      FXBoreSparkle fb = new FXBoreSparkle(this.getWorld(), x, y, z, (double)x2 + 0.5D, (double)y2 - 0.5D, (double)z2 + 0.5D);
      fb.func_70538_b(0.2F, 0.6F + this.getWorld().field_73012_v.nextFloat() * 0.3F, 0.3F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawVentParticles(double x, double y, double z, double x2, double y2, double z2, int color) {
      FXVent fb = new FXVent(this.getWorld(), x, y, z, x2, y2, z2, color);
      fb.func_82338_g(0.4F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawVentParticles(double x, double y, double z, double x2, double y2, double z2, int color, float scale) {
      FXVent fb = new FXVent(this.getWorld(), x, y, z, x2, y2, z2, color);
      fb.func_82338_g(0.4F);
      fb.setScale(scale);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void spark(double d, double e, double f, float size, float r, float g, float b, float a) {
      FXGeneric fb = new FXGeneric(this.getWorld(), d, e, f, 0.0D, 0.0D, 0.0D);
      fb.func_187114_a(5 + this.getWorld().field_73012_v.nextInt(5));
      fb.func_82338_g(a);
      fb.func_70538_b(r, g, b);
      fb.setGridSize(16);
      fb.setParticles(8 + this.getWorld().field_73012_v.nextInt(3) * 16, 8, 1);
      fb.setScale(size);
      fb.setFlipped(this.getWorld().field_73012_v.nextBoolean());
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void smokeSpiral(double x, double y, double z, float rad, int start, int miny, int color) {
      FXSmokeSpiral fx = new FXSmokeSpiral(this.getWorld(), x, y, z, rad, start, miny);
      Color c = new Color(color);
      fx.func_70538_b((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fx);
   }

   public void wispFXEG(double posX, double posY, double posZ, Entity target) {
      for(int a = 0; a < this.particleCount(1); ++a) {
         FXWispEG ef = new FXWispEG(this.getWorld(), posX, posY, posZ, target);
         ParticleEngine.INSTANCE.addEffect(this.getWorld(), ef);
      }

   }

   public void burst(double sx, double sy, double sz, float size) {
      FXGeneric fb = new FXGeneric(this.getWorld(), sx, sy, sz, 0.0D, 0.0D, 0.0D);
      fb.func_187114_a(31);
      fb.setGridSize(16);
      fb.setParticles(208, 31, 1);
      fb.setScale(size);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void excavateFX(BlockPos pos, EntityLivingBase p, int progress) {
      RenderGlobal rg = Minecraft.func_71410_x().field_71438_f;
      rg.func_180441_b(p.func_145782_y(), pos, progress);
   }

   public Object beamCont(EntityLivingBase p, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
      FXBeamWand beamcon = null;
      Color c = new Color(color);
      if (input instanceof FXBeamWand) {
         beamcon = (FXBeamWand)input;
      }

      if (beamcon != null && beamcon.func_187113_k()) {
         beamcon.updateBeam(tx, ty, tz);
         beamcon.setEndMod(endmod);
         beamcon.impact = impact;
      } else {
         beamcon = new FXBeamWand(this.getWorld(), p, tx, ty, tz, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 8);
         beamcon.setType(type);
         beamcon.setEndMod(endmod);
         beamcon.setReverse(reverse);
         FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(beamcon);
      }

      return beamcon;
   }

   public Object beamBore(double px, double py, double pz, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
      FXBeamBore beamcon = null;
      Color c = new Color(color);
      if (input instanceof FXBeamBore) {
         beamcon = (FXBeamBore)input;
      }

      if (beamcon != null && beamcon.func_187113_k()) {
         beamcon.updateBeam(px, py, pz, tx, ty, tz);
         beamcon.setEndMod(endmod);
         beamcon.impact = impact;
      } else {
         beamcon = new FXBeamBore(this.getWorld(), px, py, pz, tx, ty, tz, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 8);
         beamcon.setType(type);
         beamcon.setEndMod(endmod);
         beamcon.setReverse(reverse);
         FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(beamcon);
      }

      return beamcon;
   }

   public void boreDigFx(int x, int y, int z, Entity e, IBlockState bi, int md, int delay) {
      float p = (float)this.particleCount(30);

      for(int a = 0; (float)a < p / (float)delay; ++a) {
         if (this.getWorld().field_73012_v.nextInt(4) == 0) {
            FXBoreSparkle fb = new FXBoreSparkle(this.getWorld(), (double)((float)x + this.getWorld().field_73012_v.nextFloat()), (double)((float)y + this.getWorld().field_73012_v.nextFloat()), (double)((float)z + this.getWorld().field_73012_v.nextFloat()), e);
            ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
         } else {
            FXBoreParticles fb = new FXBoreParticles(this.getWorld(), (double)((float)x + this.getWorld().field_73012_v.nextFloat()), (double)((float)y + this.getWorld().field_73012_v.nextFloat()), (double)((float)z + this.getWorld().field_73012_v.nextFloat()), e.field_70165_t, e.field_70163_u, e.field_70161_v, bi, md);
            fb.setTarget(e);
            FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
         }
      }

   }

   public void essentiaTrailFx(BlockPos p1, BlockPos p2, int count, int color, float scale, int ext) {
      FXEssentiaStream fb = new FXEssentiaStream(this.getWorld(), (double)p1.func_177958_n() + 0.5D, (double)p1.func_177956_o() + 0.5D, (double)p1.func_177952_p() + 0.5D, (double)p2.func_177958_n() + 0.5D, (double)p2.func_177956_o() + 0.5D, (double)p2.func_177952_p() + 0.5D, count, color, scale, ext, 0.0D);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void boreTrailFx(BlockPos p1, Entity e, int count, int color, float scale, int ext) {
      FXBoreStream fb = new FXBoreStream(this.getWorld(), (double)p1.func_177958_n() + 0.5D, (double)p1.func_177956_o() + 0.5D, (double)p1.func_177952_p() + 0.5D, e, count, color, scale, ext, 0.0D);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void essentiaDropFx(double x, double y, double z, float r, float g, float b, float alpha) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, this.getWorld().field_73012_v.nextGaussian() * 0.004999999888241291D, this.getWorld().field_73012_v.nextGaussian() * 0.004999999888241291D, this.getWorld().field_73012_v.nextGaussian() * 0.004999999888241291D);
      fb.func_187114_a(20 + this.getWorld().field_73012_v.nextInt(10));
      fb.func_70538_b(r, g, b);
      fb.func_82338_g(alpha);
      fb.setLoop(false);
      fb.setParticles(25, 1, 1);
      fb.setScale(0.4F + this.getWorld().field_73012_v.nextFloat() * 0.2F, 0.2F);
      fb.setLayer(1);
      fb.setGravity(0.01F);
      fb.setRotationSpeed(0.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void jarSplashFx(double x, double y, double z) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x + this.getWorld().field_73012_v.nextGaussian() * 0.10000000149011612D, y, z + this.getWorld().field_73012_v.nextGaussian() * 0.10000000149011612D, this.getWorld().field_73012_v.nextGaussian() * 0.014999999664723873D, (double)(this.getWorld().field_73012_v.nextFloat() * 0.05F), this.getWorld().field_73012_v.nextGaussian() * 0.014999999664723873D);
      fb.func_187114_a(20 + this.getWorld().field_73012_v.nextInt(10));
      Color c = new Color(2650102);
      fb.func_70538_b((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      fb.func_82338_g(0.5F);
      fb.setLoop(false);
      fb.setParticles(25, 1, 1);
      fb.setScale(0.4F + this.getWorld().field_73012_v.nextFloat() * 0.2F);
      fb.setLayer(1);
      fb.setGravity(0.1F);
      fb.setRotationSpeed(0.0F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void waterTrailFx(BlockPos p1, BlockPos p2, int count, int color, float scale) {
      FXEssentiaStream fb = new FXEssentiaStream(this.getWorld(), (double)p1.func_177958_n() + 0.5D, (double)p1.func_177956_o() + 0.66D, (double)p1.func_177952_p() + 0.5D, (double)p2.func_177958_n() + 0.5D, (double)p2.func_177956_o() + 0.5D, (double)p2.func_177952_p() + 0.5D, count, color, scale, 0, 0.2D);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void furnaceLavaFx(int x, int y, int z, int facingX, int facingZ) {
      float qx = facingX == 0 ? (this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.5F : (float)facingX * this.getWorld().field_73012_v.nextFloat();
      float qz = facingZ == 0 ? (this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.5F : (float)facingZ * this.getWorld().field_73012_v.nextFloat();
      Particle fb = (new Factory()).func_178902_a(0, this.getWorld(), (double)((float)x + 0.5F + (this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.3F + (float)facingX * 1.0F), (double)((float)y + 0.3F), (double)((float)z + 0.5F + (this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 0.3F + (float)facingZ * 1.0F), (double)(0.15F * qx), (double)(0.2F * this.getWorld().field_73012_v.nextFloat()), (double)(0.15F * qz), new int[0]);
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
   }

   public void blockRunes(double x, double y, double z, float r, float g, float b, int dur, float grav) {
      FXBlockRunes fb = new FXBlockRunes(this.getWorld(), x + 0.5D, y + 0.5D, z + 0.5D, r, g, b, dur);
      fb.setGravity(grav);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawSlash(double x, double y, double z, double x2, double y2, double z2, int dur) {
      FXPlane fb = new FXPlane(this.getWorld(), x, y, z, x2, y2, z2, dur);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void blockWard(double x, double y, double z, EnumFacing side, float f, float f1, float f2) {
      FXBlockWard fb = new FXBlockWard(this.getWorld(), x + 0.5D, y + 0.5D, z + 0.5D, side, f, f1, f2);
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
   }

   public Object swarmParticleFX(Entity targetedEntity, float f1, float f2, float pg) {
      FXSwarm fx = new FXSwarm(this.getWorld(), targetedEntity.field_70165_t + (double)((this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 2.0F), targetedEntity.field_70163_u + (double)((this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 2.0F), targetedEntity.field_70161_v + (double)((this.getWorld().field_73012_v.nextFloat() - this.getWorld().field_73012_v.nextFloat()) * 2.0F), targetedEntity, 0.8F + this.getWorld().field_73012_v.nextFloat() * 0.2F, this.getWorld().field_73012_v.nextFloat() * 0.4F, 1.0F - this.getWorld().field_73012_v.nextFloat() * 0.2F, f1, f2, pg);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fx);
      return fx;
   }

   public void bottleTaintBreak(double x, double y, double z) {
      for(int k1 = 0; k1 < 8; ++k1) {
         this.getWorld().func_175688_a(EnumParticleTypes.ITEM_CRACK, x, y, z, this.getWorld().field_73012_v.nextGaussian() * 0.15D, this.getWorld().field_73012_v.nextDouble() * 0.2D, this.getWorld().field_73012_v.nextGaussian() * 0.15D, new int[]{Item.func_150891_b(ItemsTC.bottleTaint)});
      }

      this.getWorld().func_184134_a(x, y, z, SoundEvents.field_187825_fO, SoundCategory.NEUTRAL, 1.0F, this.getWorld().field_73012_v.nextFloat() * 0.1F + 0.9F, false);
   }

   public void arcLightning(double x, double y, double z, double tx, double ty, double tz, float r, float g, float b, float h) {
      FXArc efa = new FXArc(this.getWorld(), x, y, z, tx, ty, tz, r, g, b, (double)h);
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(efa);
   }

   public void arcBolt(double x, double y, double z, double tx, double ty, double tz, float r, float g, float b, float width) {
      FXBolt efa = new FXBolt(this.getWorld(), x, y, z, tx, ty, tz, r, g, b, width);
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(efa);
   }

   public void cultistSpawn(double x, double y, double z, double a, double b, double c) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, a, b, c);
      fb.func_187114_a(10 + this.getWorld().field_73012_v.nextInt(10));
      fb.setRBGColorF(1.0F, 1.0F, 1.0F, 0.6F, 0.0F, 0.0F);
      fb.func_82338_g(0.8F);
      fb.setGridSize(16);
      fb.setParticles(160, 6, 1);
      fb.setScale(3.0F + this.getWorld().field_73012_v.nextFloat() * 2.0F);
      fb.setLayer(1);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawWispyMotesEntity(double x, double y, double z, Entity e, float r, float g, float b) {
      FXGenericP2E fb = new FXGenericP2E(this.getWorld(), x, y, z, e);
      fb.func_70538_b(r, g, b);
      fb.func_82338_g(0.6F);
      fb.setParticles(512, 16, 1);
      fb.setLoop(true);
      fb.setWind(0.001D);
      fb.setRandomMovementScale(0.0025F, 0.0F, 0.0025F);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawWispParticles(double x, double y, double z, double x2, double y2, double z2, int color, int a) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
      fb.func_187114_a(10 + this.getWorld().field_73012_v.nextInt(5));
      Color c = new Color(color);
      fb.func_70538_b((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      fb.func_82338_g(0.5F);
      fb.setLoop(true);
      fb.setGridSize(64);
      fb.setParticles(264, 8, 1);
      fb.setScale(1.0F + this.getWorld().field_73012_v.nextFloat() * 0.25F, 0.05F);
      fb.setWind(2.5E-4D);
      fb.setRandomMovementScale(0.0025F, 0.0F, 0.0025F);
      ParticleEngine.INSTANCE.addEffectWithDelay(this.getWorld(), fb, a);
   }

   public void drawNitorCore(double x, double y, double z, double x2, double y2, double z2) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
      fb.func_187114_a(10 + this.getWorld().field_73012_v.nextInt(5));
      fb.func_70538_b(1.0F, 1.0F, 1.0F);
      fb.func_82338_g(1.0F);
      fb.setParticles(457, 1, 1);
      fb.setScale(1.0F, 1.0F + (float)this.getWorld().field_73012_v.nextGaussian() * 0.1F, 1.0F);
      fb.setLayer(1);
      ParticleEngine.INSTANCE.addEffect(this.getWorld(), fb);
   }

   public void drawNitorFlames(double x, double y, double z, double x2, double y2, double z2, int color, int a) {
      FXGeneric fb = new FXGeneric(this.getWorld(), x, y, z, x2, y2, z2);
      fb.func_187114_a(10 + this.getWorld().field_73012_v.nextInt(5));
      Color c = new Color(color);
      fb.func_70538_b((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      fb.func_82338_g(0.5F);
      fb.setLoop(true);
      fb.setGridSize(64);
      fb.setParticles(264, 8, 1);
      fb.setScale(3.0F + this.getWorld().field_73012_v.nextFloat(), 0.05F);
      fb.setRandomMovementScale(0.0025F, 0.0F, 0.0025F);
      ParticleEngine.INSTANCE.addEffectWithDelay(this.getWorld(), fb, a);
   }
}
