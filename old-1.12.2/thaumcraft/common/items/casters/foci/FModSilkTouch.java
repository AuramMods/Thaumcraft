package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;

public class FModSilkTouch implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/silk.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.SILKTOUCH";
   }

   public String getResearch() {
      return "FOCUSBREAK@3";
   }

   public Aspect getAspect() {
      return Aspect.EXCHANGE;
   }

   public int getGemColor() {
      return 9046120;
   }

   public int getIconColor() {
      return 16099809;
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
      fb.func_187114_a(10 + world.field_73012_v.nextInt(5));
      fb.setParticles(16, 5, 1);
      fb.setSlowDown(0.85D);
      fb.setScale((float)(0.6000000238418579D + world.field_73012_v.nextGaussian() * 0.20000000298023224D));
      fb.setGravity(0.2F);
      fb.func_70538_b(0.96F, 0.66F, 0.98F);
      fb.setRotationSpeed(world.field_73012_v.nextFloat() * 3.0F, (float)world.field_73012_v.nextGaussian());
      ParticleEngine.INSTANCE.addEffectWithDelay(world, fb, world.field_73012_v.nextInt(10));
   }
}
