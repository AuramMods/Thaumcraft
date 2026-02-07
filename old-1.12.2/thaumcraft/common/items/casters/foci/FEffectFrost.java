package thaumcraft.common.items.casters.foci;

import java.util.Iterator;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
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

public class FEffectFrost implements IFocusPartEffect {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/frost.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.FROST";
   }

   public String getResearch() {
      return "FOCUSFROST";
   }

   public Aspect getAspect() {
      return Aspect.COLD;
   }

   public int getGemColor() {
      return 3836158;
   }

   public int getIconColor() {
      return 13299966;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 2.0F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      float f;
      if (ray.field_72313_a == Type.ENTITY && ray.field_72308_g != null) {
         if (world.field_72995_K) {
            return true;
         } else {
            f = 3.0F * effect.effectMultipler * charge;
            int cold = 40;
            int frost = 1;
            if (effect.modifiers != null) {
               IFocusPart[] var17 = effect.modifiers;
               int var12 = var17.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  IFocusPart mod = var17[var13];
                  if (mod instanceof FModLingering) {
                     cold *= 3;
                     frost *= 2;
                  }
               }
            }

            ray.field_72308_g.func_70097_a(DamageSource.func_76356_a(mediumEntity, caster), f);
            if (ray.field_72308_g instanceof EntityLivingBase) {
               ((EntityLivingBase)ray.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_76421_d, cold, frost));
            }

            return true;
         }
      } else if (ray.field_72313_a == Type.BLOCK) {
         if (world.field_72995_K) {
            return true;
         } else {
            f = Math.min(16.0F, 2.0F * effect.effectMultipler);
            Iterator var9 = BlockPos.func_177975_b(ray.func_178782_a().func_177963_a((double)(-f), (double)(-f), (double)(-f)), ray.func_178782_a().func_177963_a((double)f, (double)f, (double)f)).iterator();

            while(var9.hasNext()) {
               MutableBlockPos blockpos$mutableblockpos1 = (MutableBlockPos)var9.next();
               if (blockpos$mutableblockpos1.func_177957_d(mediumEntity.field_70165_t, mediumEntity.field_70163_u, mediumEntity.field_70161_v) <= (double)(f * f)) {
                  IBlockState iblockstate1 = world.func_180495_p(blockpos$mutableblockpos1);
                  if (iblockstate1.func_185904_a() == Material.field_151586_h && (Integer)iblockstate1.func_177229_b(BlockLiquid.field_176367_b) == 0 && world.func_175716_a(Blocks.field_185778_de, blockpos$mutableblockpos1, false, EnumFacing.DOWN, (Entity)null, (ItemStack)null)) {
                     world.func_175656_a(blockpos$mutableblockpos1, Blocks.field_185778_de.func_176223_P());
                     world.func_175684_a(blockpos$mutableblockpos1.func_185334_h(), Blocks.field_185778_de, MathHelper.func_76136_a(world.field_73012_v, 60, 120));
                  }
               }
            }

            return true;
         }
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
      fb.func_187114_a(40 + world.field_73012_v.nextInt(40));
      fb.setAlphaF(1.0F, 0.0F);
      fb.setParticles(8, 1, 1);
      fb.setGravity(0.033F);
      fb.setSlowDown(0.8D);
      fb.setRandomMovementScale(0.0025F, 1.0E-4F, 0.0025F);
      fb.setScale((float)(0.699999988079071D + world.field_73012_v.nextGaussian() * 0.30000001192092896D));
      fb.setRotationSpeed(world.field_73012_v.nextFloat() * 3.0F, (float)world.field_73012_v.nextGaussian() / 4.0F);
      ParticleEngine.INSTANCE.addEffectWithDelay(world, fb, 0);
   }
}
