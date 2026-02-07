package thaumcraft.common.tiles.misc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileMemory extends TileEntity {
   public IBlockState oldblock;
   public NBTTagCompound tileEntityCompound;

   public TileMemory() {
      this.oldblock = Blocks.field_150350_a.func_176223_P();
   }

   public TileMemory(IBlockState bi) {
      this.oldblock = Blocks.field_150350_a.func_176223_P();
      this.oldblock = bi;
   }

   public void func_145839_a(NBTTagCompound nbttagcompound) {
      super.func_145839_a(nbttagcompound);
      Block b = Block.func_149729_e(nbttagcompound.func_74762_e("oldblock"));
      int meta = nbttagcompound.func_74762_e("oldmeta");
      this.oldblock = b.func_176203_a(meta);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
      super.func_189515_b(nbttagcompound);
      nbttagcompound.func_74768_a("oldblock", Block.func_149682_b(this.oldblock.func_177230_c()));
      nbttagcompound.func_74768_a("oldmeta", this.oldblock.func_177230_c().func_176201_c(this.oldblock));
      return nbttagcompound;
   }
}
