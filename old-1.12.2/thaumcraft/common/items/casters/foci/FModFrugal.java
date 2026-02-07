package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;

public class FModFrugal implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/frugal.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.FRUGAL";
   }

   public String getResearch() {
      return "FOCUSBASICMODS@2";
   }

   public Aspect getAspect() {
      return Aspect.DESIRE;
   }

   public int getGemColor() {
      return 6227124;
   }

   public int getIconColor() {
      return 11138512;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 0.75F;
   }

   public boolean canConnectTo(IFocusPart part) {
      return part instanceof IFocusPartEffect && ((IFocusPartEffect)part).getBaseCost() > 0.0F;
   }
}
