package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.client.fx.FXDispatcher;

public class FEffectFire implements IFocusPartEffect {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/fire.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.FIRE";
   }

   public String getResearch() {
      return "BASETHAUMATURGY@3";
   }

   public Aspect getAspect() {
      return Aspect.FIRE;
   }

   public int getGemColor() {
      return 16519424;
   }

   public int getIconColor() {
      return 16366673;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 2.0F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      if (ray.field_72313_a == Type.ENTITY && ray.field_72308_g != null) {
         if (ray.field_72308_g.func_70045_F()) {
            return false;
         } else if (world.field_72995_K) {
            return true;
         } else {
            float fire = 2.0F;
            float damage = 3.0F * effect.effectMultipler * charge;
            if (effect.modifiers != null) {
               IFocusPart[] var10 = effect.modifiers;
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  IFocusPart mod = var10[var12];
                  if (mod instanceof FModLingering) {
                     fire *= 3.0F;
                  }
               }
            }

            ray.field_72308_g.func_70097_a((new EntityDamageSourceIndirect("fireball", mediumEntity, caster)).func_76361_j(), damage);
            ray.field_72308_g.func_70015_d(Math.round(fire));
            return true;
         }
      } else {
         if (ray.field_72313_a == Type.BLOCK) {
            if (world.field_72995_K) {
               return true;
            }

            BlockPos pos = ray.func_178782_a();
            pos = pos.func_177972_a(ray.field_178784_b);
            if (world.func_175623_d(pos)) {
               world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187649_bu, SoundCategory.BLOCKS, 1.0F, world.field_73012_v.nextFloat() * 0.4F + 0.8F);
               world.func_180501_a(pos, Blocks.field_150480_ab.func_176223_P(), 11);
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasCustomParticle() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void drawCustomParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
      FXDispatcher.INSTANCE.drawGenericParticles(posX, posY, posZ, motionX, motionY, motionZ, 1.0F, 1.0F, 1.0F, 0.7F, false, 640, 10, 1, 10, 0, (float)(1.0D + world.field_73012_v.nextGaussian() * 0.20000000298023224D), 0.0F, 0);
   }
}
