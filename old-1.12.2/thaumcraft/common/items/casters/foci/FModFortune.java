package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;

public class FModFortune implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/fortune.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.FORTUNE";
   }

   public String getResearch() {
      return "FOCUSBREAK";
   }

   public Aspect getAspect() {
      return Aspect.DESIRE;
   }

   public int getGemColor() {
      return 15121988;
   }

   public int getIconColor() {
      return 16640695;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 1.5F;
   }

   public boolean hasCustomParticle() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void drawCustomParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
      FXGeneric fb = new FXGeneric(world, posX, posY, posZ, motionX, motionY, motionZ);
      fb.func_187114_a(13 + world.field_73012_v.nextInt(13));
      fb.setParticles(21 + world.field_73012_v.nextInt(4), 1, 1);
      fb.setSlowDown(0.9D);
      fb.setScale((float)(0.699999988079071D + world.field_73012_v.nextGaussian() * 0.10000000149011612D));
      fb.setGravity(0.33F);
      fb.setLayer(1);
      fb.setAlphaF(1.0F, 1.0F, 1.0F, 0.0F);
      fb.func_70538_b(0.2F + world.field_73012_v.nextFloat() * 0.5F, 0.2F + world.field_73012_v.nextFloat() * 0.5F, 0.2F + world.field_73012_v.nextFloat() * 0.5F);
      fb.setRotationSpeed(world.field_73012_v.nextFloat() * 3.0F, (float)world.field_73012_v.nextGaussian() * 2.0F);
      ParticleEngine.INSTANCE.addEffectWithDelay(world, fb, 0);
   }
}
