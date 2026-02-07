package thaumcraft.common.items.casters.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IFocusPart;

public class FModLingering implements IFocusPart {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/lingering.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MODIFIER;
   }

   public String getKey() {
      return "thaumcraft.LINGERING";
   }

   public String getResearch() {
      return "FOCUSBASICMODS";
   }

   public Aspect getAspect() {
      return Aspect.WATER;
   }

   public int getGemColor() {
      return 8421567;
   }

   public int getIconColor() {
      return 16366673;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 1.1F;
   }
}
