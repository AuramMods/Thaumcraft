package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;

public class FModChain implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/chain.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.CHAIN";
   }

   public String getResearch() {
      return "FOCUSBOLT";
   }

   public Aspect getAspect() {
      return Aspect.MOTION;
   }

   public int getGemColor() {
      return 5789784;
   }

   public int getIconColor() {
      return 16109737;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 2.0F;
   }

   public float getEffectMultiplier() {
      return 0.6F;
   }
}
