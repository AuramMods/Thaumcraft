package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartMedium;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;

public class FMediumTouch implements IFocusPartMedium {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/touch.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MEDIUM;
   }

   public IFocusPartMedium.EnumFocusCastMethod getCastMethod() {
      return IFocusPartMedium.EnumFocusCastMethod.INSTANT;
   }

   public String getKey() {
      return "thaumcraft.TOUCH";
   }

   public String getResearch() {
      return "BASETHAUMATURGY@3";
   }

   public Aspect getAspect() {
      return Aspect.EARTH;
   }

   public int getGemColor() {
      return 5811450;
   }

   public int getIconColor() {
      return 12706559;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 0.5F;
   }

   public float getEffectMultiplier() {
      return 1.1F;
   }

   public boolean onMediumTrigger(World world, Entity caster, ItemStack casterStack, FocusCore focus, float charge) {
      Entity pointedEntity = EntityUtils.getPointedEntity(world, caster, 0.0D, 2.0D, 0.1F);
      RayTraceResult mop;
      if (pointedEntity != null) {
         mop = new RayTraceResult(pointedEntity);
      } else {
         mop = Utils.rayTrace(world, caster, true);
      }

      if (mop == null) {
         return false;
      } else {
         boolean b = false;
         boolean unhurt = mop.field_72308_g == null || mop.field_72308_g.field_70172_ad <= 0;
         int resetHurt = 0;
         FocusCore.FocusEffect[] var11 = focus.effects;
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            FocusCore.FocusEffect effect = var11[var13];
            if (mop.field_72308_g != null && unhurt && mop.field_72308_g.field_70172_ad > 0) {
               resetHurt = mop.field_72308_g.field_70172_ad;
               mop.field_72308_g.field_70172_ad = 0;
            }

            if (effect.effect.onEffectTrigger(world, mop, caster, casterStack, caster, effect, charge)) {
               b = true;
            }
         }

         if (!world.field_72995_K) {
            CasterManager.sendImpact(world, mop.field_72307_f.field_72450_a, mop.field_72307_f.field_72448_b, mop.field_72307_f.field_72449_c, focus);
         }

         if (resetHurt > 0) {
            mop.field_72308_g.field_70172_ad = resetHurt;
         }

         return b;
      }
   }
}
