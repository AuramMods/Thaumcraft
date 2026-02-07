package thaumcraft.api.casters;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IFocusPartEffect extends IFocusPart {
   default float getBaseCost() {
      return 0.0F;
   }

   default boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, @Nullable ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      return true;
   }
}
