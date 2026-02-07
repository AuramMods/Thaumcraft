package thaumcraft.common.blocks.crafting;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;

public class BlockInfusionMatrix extends BlockTCDevice {
   public BlockInfusionMatrix() {
      super(Material.field_151576_e, TileInfusionMatrix.class);
      this.func_149672_a(SoundType.field_185851_d);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }
}
