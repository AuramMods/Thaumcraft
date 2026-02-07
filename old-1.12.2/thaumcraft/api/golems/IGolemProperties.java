package thaumcraft.api.golems;

import java.util.Set;
import net.minecraft.item.ItemStack;
import thaumcraft.api.golems.parts.GolemAddon;
import thaumcraft.api.golems.parts.GolemArm;
import thaumcraft.api.golems.parts.GolemHead;
import thaumcraft.api.golems.parts.GolemLeg;
import thaumcraft.api.golems.parts.GolemMaterial;

public interface IGolemProperties {
   Set<EnumGolemTrait> getTraits();

   boolean hasTrait(EnumGolemTrait var1);

   long toLong();

   ItemStack[] generateComponents();

   void setMaterial(GolemMaterial var1);

   GolemMaterial getMaterial();

   void setHead(GolemHead var1);

   GolemHead getHead();

   void setArms(GolemArm var1);

   GolemArm getArms();

   void setLegs(GolemLeg var1);

   GolemLeg getLegs();

   void setAddon(GolemAddon var1);

   GolemAddon getAddon();

   void setRank(int var1);

   int getRank();
}
