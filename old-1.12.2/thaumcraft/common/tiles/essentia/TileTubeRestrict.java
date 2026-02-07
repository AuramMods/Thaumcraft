package thaumcraft.common.tiles.essentia;

import thaumcraft.api.aspects.Aspect;

public class TileTubeRestrict extends TileTube {
   void calculateSuction(Aspect filter, boolean restrict, boolean dir) {
      super.calculateSuction(filter, true, dir);
   }
}
