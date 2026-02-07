package thaumcraft.api.research;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class ScanBlock implements IScanThing {
   String research;
   Block[] blocks;

   public ScanBlock(Block block) {
      this.research = "!" + block.getRegistryName().toString();
      this.blocks = new Block[]{block};
   }

   public ScanBlock(String research, Block... blocks) {
      this.research = research;
      this.blocks = blocks;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      if (obj != null && obj instanceof BlockPos) {
         Block[] var3 = this.blocks;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Block block = var3[var5];
            if (player.field_70170_p.func_180495_p((BlockPos)obj).func_177230_c() == block) {
               return true;
            }
         }
      }

      return false;
   }

   public String getResearchKey(EntityPlayer player, Object object) {
      return this.research;
   }
}
