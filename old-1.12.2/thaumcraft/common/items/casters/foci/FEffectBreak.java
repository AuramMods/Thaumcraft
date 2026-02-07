package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.utils.BlockUtils;

public class FEffectBreak implements IFocusPartEffect {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/break.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.BREAK";
   }

   public String getResearch() {
      return "FOCUSBREAK@2";
   }

   public Aspect getAspect() {
      return Aspect.ENTROPY;
   }

   public int getGemColor() {
      return 9063176;
   }

   public int getIconColor() {
      return 16249568;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 0.5F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      if (ray.field_72313_a == Type.BLOCK && caster instanceof EntityPlayer && !world.field_72995_K) {
         boolean silk = false;
         int fortune = 0;
         if (effect.modifiers != null) {
            IFocusPart[] var10 = effect.modifiers;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               IFocusPart mod = var10[var12];
               if (mod instanceof FModSilkTouch) {
                  silk = true;
               }

               if (mod instanceof FModFortune) {
                  fortune = 2;
               }
            }
         }

         return BlockUtils.harvestBlock(world, (EntityPlayer)caster, ray.func_178782_a(), true, silk, fortune);
      } else {
         return false;
      }
   }

   public boolean hasCustomParticle() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void drawCustomParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
      FXGeneric fb = new FXGeneric(world, posX, posY, posZ, motionX, motionY, motionZ);
      fb.func_187114_a(6 + world.field_73012_v.nextInt(6));
      int q = world.field_73012_v.nextInt(4);
      fb.setParticles(704 + q * 3, 3, 1);
      fb.setSlowDown(0.8D);
      fb.setScale((float)(0.699999988079071D + world.field_73012_v.nextGaussian() * 0.30000001192092896D));
      ParticleEngine.INSTANCE.addEffectWithDelay(world, fb, 0);
   }
}
