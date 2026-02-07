package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;

public class FModScatter implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/scatter.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.SCATTER";
   }

   public String getResearch() {
      return "FOCUSPROJECTILE";
   }

   public Aspect getAspect() {
      return Aspect.AIR;
   }

   public int getGemColor() {
      return 5789784;
   }

   public int getIconColor() {
      return 14808489;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 1.5F;
   }

   public float getEffectMultiplier() {
      return 0.3F;
   }
}
