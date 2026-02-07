package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartMedium;

public class FModCharge implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/charge.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.CHARGE";
   }

   public String getResearch() {
      return "FOCUSCHARGE";
   }

   public Aspect getAspect() {
      return Aspect.FLUX;
   }

   public int getGemColor() {
      return 52479;
   }

   public int getIconColor() {
      return 16777190;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public boolean canConnectTo(IFocusPart part) {
      return part instanceof IFocusPartMedium;
   }
}
