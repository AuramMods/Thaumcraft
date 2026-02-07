package thaumcraft.api.casters;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IFocusPartMedium extends IFocusPart {
   default IFocusPartMedium.EnumFocusCastMethod getCastMethod() {
      return IFocusPartMedium.EnumFocusCastMethod.DEFAULT;
   }

   default int getChargeTime() {
      return 10;
   }

   default boolean onMediumTrigger(World world, Entity caster, @Nullable ItemStack casterStack, FocusCore focus, float charge) {
      return true;
   }

   public static enum EnumFocusCastMethod {
      DEFAULT,
      CHARGE,
      INSTANT;
   }
}
