package thaumcraft.common.lib.research.theorycraft;

import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class AidBasicThaumaturgy implements ITheorycraftAid {
   public Object getAidObject() {
      return BlocksTC.wandWorkbench;
   }

   public Class<TheorycraftCard>[] getCards() {
      return new Class[]{CardFocus.class, CardAwareness.class, CardSpellbinding.class};
   }
}
