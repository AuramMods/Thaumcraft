package thaumcraft.common.blocks.basic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import thaumcraft.api.blocks.BlocksTC;

public class BlockWoodSlabTCItem extends ItemSlab {
   public BlockWoodSlabTCItem(Block par1) {
      super(par1, (BlockSlab)BlocksTC.slabWood, (BlockSlab)BlocksTC.doubleSlabWood);
   }

   public String func_77667_c(ItemStack stack) {
      return super.func_77658_a() + "." + ((BlockWoodSlabTC)this.field_150939_a).getTypeName(this.field_150939_a.func_176203_a(stack.func_77960_j()));
   }
}
