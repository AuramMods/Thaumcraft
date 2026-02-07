package thaumcraft.common.blocks.basic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import thaumcraft.api.blocks.BlocksTC;

public class BlockStoneSlabTCItem extends ItemSlab {
   public BlockStoneSlabTCItem(Block par1) {
      super(par1, (BlockSlab)BlocksTC.slabStone, (BlockSlab)BlocksTC.doubleSlabStone);
   }

   public String func_77667_c(ItemStack stack) {
      return super.func_77658_a() + "." + ((BlockStoneSlabTC)this.field_150939_a).getTypeName(this.field_150939_a.func_176203_a(stack.func_77960_j()));
   }
}
