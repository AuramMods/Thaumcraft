package thaumcraft.common.blocks.basic;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.config.ConfigItems;

public class BlockTranslucent extends BlockTC {
   public BlockTranslucent() {
      super(Material.field_151592_s, BlockTranslucent.TransType.class);
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149711_c(0.5F);
      this.func_149672_a(SoundType.field_185851_d);
   }

   public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
      return true;
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
   }

   public EnumPushReaction func_149656_h(IBlockState state) {
      return EnumPushReaction.NORMAL;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_176225_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      IBlockState iblockstate = blockAccess.func_180495_p(pos.func_177972_a(side));
      Block block = iblockstate.func_177230_c();
      return block == this ? false : super.func_176225_a(blockState, blockAccess, pos, side);
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   public boolean func_149662_c(IBlockState iblockstate) {
      return false;
   }

   public static enum TransType implements IStringSerializable {
      AMBER_BLOCK,
      AMBER_BRICK,
      EMPTY;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
