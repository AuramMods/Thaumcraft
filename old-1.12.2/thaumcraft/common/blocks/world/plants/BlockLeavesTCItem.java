package thaumcraft.common.blocks.world.plants;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockLeavesTCItem extends ItemBlock {
   private final BlockLeavesTC leaves;

   public BlockLeavesTCItem(Block block) {
      super(block);
      this.leaves = (BlockLeavesTC)block;
      this.func_77656_e(0);
      this.func_77627_a(true);
   }

   public int func_77647_b(int damage) {
      return damage | 4;
   }

   public String func_77667_c(ItemStack stack) {
      return super.func_77658_a() + "." + this.leaves.getWoodTCType(stack.func_77960_j()).func_176610_l();
   }
}
