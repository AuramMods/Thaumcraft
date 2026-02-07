package thaumcraft.api.casters;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

public interface IFocusPart {
   String getKey();

   String getResearch();

   Aspect getAspect();

   default String getName() {
      return I18n.func_74838_a("focuspart." + this.getKey() + ".name");
   }

   default String getText() {
      return I18n.func_74838_a("focuspart." + this.getKey() + ".text");
   }

   default float getCostMultiplier() {
      return 1.0F;
   }

   default float getEffectMultiplier() {
      return 1.0F;
   }

   IFocusPart.EnumFocusPartType getType();

   ResourceLocation getIcon();

   int getGemColor();

   int getIconColor();

   default boolean canConnectTo(IFocusPart part) {
      return true;
   }

   default boolean hasCustomParticle() {
      return false;
   }

   default void drawCustomParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
   }

   public static enum EnumFocusPartType {
      MEDIUM,
      EFFECT,
      MODIFIER;
   }
}
