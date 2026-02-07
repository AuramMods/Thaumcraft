package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.item.ItemStack;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class AidGlyphedStone implements ITheorycraftAid {
   public Object getAidObject() {
      return new ItemStack(BlocksTC.stone, 1, 11);
   }

   public Class<TheorycraftCard>[] getCards() {
      return new Class[]{CardGlyphs.class};
   }
}
