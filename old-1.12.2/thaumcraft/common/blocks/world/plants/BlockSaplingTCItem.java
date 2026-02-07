package thaumcraft.common.blocks.world.plants;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockSaplingTCItem extends ItemBlock {
   public BlockSaplingTCItem(Block par1) {
      super(par1);
      this.func_77627_a(true);
   }

   public int func_77647_b(int metadata) {
      return metadata;
   }

   public String func_77667_c(ItemStack stack) {
      return super.func_77658_a() + "." + ((BlockSaplingTC)this.field_150939_a).getTypeName(this.field_150939_a.func_176203_a(stack.func_77960_j()));
   }
}
