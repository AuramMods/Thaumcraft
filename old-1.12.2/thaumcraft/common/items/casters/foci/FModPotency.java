package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;

public class FModPotency implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/potency.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.POTENCY";
   }

   public String getResearch() {
      return "FOCUSBASICMODS@3";
   }

   public Aspect getAspect() {
      return Aspect.ENERGY;
   }

   public int getGemColor() {
      return 6227124;
   }

   public int getIconColor() {
      return 13563637;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 2.0F;
   }

   public float getEffectMultiplier() {
      return 1.5F;
   }

   public boolean canConnectTo(IFocusPart part) {
      return part instanceof IFocusPartEffect && ((IFocusPartEffect)part).getBaseCost() > 0.0F;
   }
}
