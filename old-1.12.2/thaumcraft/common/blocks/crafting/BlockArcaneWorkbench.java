package thaumcraft.common.blocks.crafting;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.container.InventoryArcaneWorkbench;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class BlockArcaneWorkbench extends BlockTCDevice {
   public BlockArcaneWorkbench() {
      super(Material.field_151575_d, TileArcaneWorkbench.class);
      this.func_149672_a(SoundType.field_185848_a);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         player.openGui(Thaumcraft.instance, 13, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         return true;
      }
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      TileEntity tileEntity = world.func_175625_s(pos);
      if (tileEntity != null && tileEntity instanceof TileArcaneWorkbench) {
         InventoryArcaneWorkbench inventory = ((TileArcaneWorkbench)tileEntity).inventory;

         for(int i = 0; i < inventory.func_70302_i_(); ++i) {
            ItemStack item = inventory.func_70301_a(i);
            if (item != null && item.field_77994_a > 0) {
               float rx = world.field_73012_v.nextFloat() * 0.8F + 0.1F;
               float ry = world.field_73012_v.nextFloat() * 0.8F + 0.1F;
               float rz = world.field_73012_v.nextFloat() * 0.8F + 0.1F;
               EntityItem entityItem = new EntityItem(world, (double)((float)pos.func_177958_n() + rx), (double)((float)pos.func_177956_o() + ry), (double)((float)pos.func_177952_p() + rz), item.func_77946_l());
               float factor = 0.05F;
               entityItem.field_70159_w = world.field_73012_v.nextGaussian() * (double)factor;
               entityItem.field_70181_x = world.field_73012_v.nextGaussian() * (double)factor + 0.20000000298023224D;
               entityItem.field_70179_y = world.field_73012_v.nextGaussian() * (double)factor;
               world.func_72838_d(entityItem);
               inventory.func_70299_a(i, (ItemStack)null);
            }
         }
      }

      super.func_180663_b(world, pos, state);
      world.func_175713_t(pos);
   }
}
