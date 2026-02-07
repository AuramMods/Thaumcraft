package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTC extends ItemBlock {
   public ItemBlockTC(Block block) {
      super(block);
      this.func_77656_e(0);
      this.func_77627_a(true);
   }

   public int func_77647_b(int metadata) {
      return metadata;
   }

   public String func_77667_c(ItemStack stack) {
      BlockTC block = (BlockTC)this.field_150939_a;
      return block.hasTypes() ? super.func_77658_a() + "." + block.getTypeName(block.func_176203_a(stack.func_77960_j())) : super.func_77658_a();
   }
}
