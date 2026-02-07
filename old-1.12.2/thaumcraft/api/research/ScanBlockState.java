package thaumcraft.api.research;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ScanBlockState implements IScanThing {
   String research;
   IBlockState blockState;

   public ScanBlockState(IBlockState blockState) {
      this.research = "!" + blockState.toString();
      this.blockState = blockState;
   }

   public ScanBlockState(String research, IBlockState blockState) {
      this.research = research;
      this.blockState = blockState;
   }

   public ScanBlockState(String research, IBlockState blockState, boolean item) {
      this.research = research;
      this.blockState = blockState;
      if (item) {
         ScanningManager.addScannableThing(new ScanItem(research, new ItemStack(blockState.func_177230_c(), 1, blockState.func_177230_c().func_176201_c(blockState))));
      }

   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      return obj != null && obj instanceof BlockPos && player.field_70170_p.func_180495_p((BlockPos)obj) == this.blockState;
   }

   public String getResearchKey(EntityPlayer player, Object object) {
      return this.research;
   }
}
