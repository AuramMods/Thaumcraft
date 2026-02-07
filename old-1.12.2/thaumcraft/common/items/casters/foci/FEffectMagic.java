package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
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

public class FEffectMagic implements IFocusPartEffect {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/magic.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.MAGIC";
   }

   public String getResearch() {
      return "FOCUSMAGIC";
   }

   public Aspect getAspect() {
      return Aspect.MAGIC;
   }

   public int getGemColor() {
      return 13566207;
   }

   public int getIconColor() {
      return 16761087;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 2.0F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      if (!world.field_72995_K) {
         double hm = 0.0D;
         if (ray.field_72308_g != null) {
            hm = (double)(ray.field_72308_g.field_70131_O / 2.0F);
         }

         PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockBamf(ray.field_72307_f.field_72450_a, Math.max(ray.field_72307_f.field_72448_b + hm, mediumEntity.field_70163_u), ray.field_72307_f.field_72449_c, this.getGemColor(), true, true, (EnumFacing)null), new TargetPoint(world.field_73011_w.getDimension(), mediumEntity.field_70165_t, mediumEntity.field_70163_u, mediumEntity.field_70161_v, 32.0D));
      }

      if (ray.field_72313_a == Type.ENTITY && ray.field_72308_g != null) {
         if (world.field_72995_K) {
            return true;
         }

         float damage = 2.0F * effect.effectMultipler * charge;
         ray.field_72308_g.func_70097_a(DamageSource.func_76354_b(mediumEntity, caster), damage);
      }

      return false;
   }
}
