package thaumcraft.api.research.theorycraft;

import net.minecraft.init.Blocks;

public class AidBookshelf implements ITheorycraftAid {
   public Object getAidObject() {
      return Blocks.field_150342_X;
   }

   public Class<TheorycraftCard>[] getCards() {
      return new Class[]{CardBalance.class, CardNotation.class, CardNotation.class};
   }
}
