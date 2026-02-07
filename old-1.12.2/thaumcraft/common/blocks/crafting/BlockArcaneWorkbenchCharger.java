package thaumcraft.common.blocks.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class BlockArcaneWorkbenchCharger extends BlockTC {
   public BlockArcaneWorkbenchCharger() {
      super(Material.field_151575_d);
      this.func_149672_a(SoundType.field_185848_a);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return super.func_176196_c(worldIn, pos) && worldIn.func_180495_p(pos.func_177977_b()).func_177230_c() == BlocksTC.arcaneWorkbench;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      TileEntity te = worldIn.func_175625_s(pos.func_177977_b());
      if (te != null && te instanceof TileArcaneWorkbench) {
         ((TileArcaneWorkbench)te).syncTile(true);
      }

      return super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (worldIn.func_180495_p(pos.func_177977_b()).func_177230_c() != BlocksTC.arcaneWorkbench) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_175698_g(pos);
      }

   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         if (world.func_180495_p(pos.func_177977_b()).func_177230_c() == BlocksTC.arcaneWorkbench) {
            player.openGui(Thaumcraft.instance, 13, world, pos.func_177958_n(), pos.func_177977_b().func_177956_o(), pos.func_177952_p());
         }

         return true;
      }
   }
}
