package thaumcraft.common.lib.research.theorycraft;

import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class AidBasicArtifice implements ITheorycraftAid {
   public Object getAidObject() {
      return BlocksTC.infusionMatrix;
   }

   public Class<TheorycraftCard>[] getCards() {
      return new Class[]{CardCalibrate.class, CardTinker.class, CardMindOverMatter.class};
   }
}
