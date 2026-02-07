package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockBamf;

public class FEffectCurse implements IFocusPartEffect {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/curse.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.CURSE";
   }

   public String getResearch() {
      return "FOCUSCURSE";
   }

   public Aspect getAspect() {
      return Aspect.DEATH;
   }

   public int getGemColor() {
      return 2815273;
   }

   public int getIconColor() {
      return 8409216;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 5.0F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      if (!world.field_72995_K) {
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockBamf(ray.field_72307_f.field_72450_a, Math.max(ray.field_72307_f.field_72448_b, mediumEntity.field_70163_u), ray.field_72307_f.field_72449_c, this.getGemColor(), true, true, (EnumFacing)null), new TargetPoint(world.field_73011_w.getDimension(), mediumEntity.field_70165_t, mediumEntity.field_70163_u, mediumEntity.field_70161_v, 32.0D));
      }

      if (ray.field_72313_a == Type.ENTITY && ray.field_72308_g != null) {
         if (world.field_72995_K) {
            return true;
         } else {
            int duration = 80;
            float eff = 1.0F * effect.effectMultipler * charge - 1.0F;
            if (eff < 0.0F) {
               eff = 0.0F;
            }

            if (effect.modifiers != null) {
               IFocusPart[] var10 = effect.modifiers;
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  IFocusPart mod = var10[var12];
                  if (mod instanceof FModLingering) {
                     duration *= 2;
                  }
               }
            }

            if (ray.field_72308_g instanceof EntityLivingBase) {
               ((EntityLivingBase)ray.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_76421_d, duration / 2, Math.round(eff)));
               ((EntityLivingBase)ray.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_76437_t, duration, Math.round(eff)));
               ((EntityLivingBase)ray.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_76436_u, duration * 2, Math.round(eff)));
               ((EntityLivingBase)ray.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_189112_A, duration * 3, Math.round(eff)));
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
