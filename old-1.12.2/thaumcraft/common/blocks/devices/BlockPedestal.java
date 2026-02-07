package thaumcraft.common.blocks.devices;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockTCTile;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.crafting.TilePedestal;

public class BlockPedestal extends BlockTCTile {
   public static BlockPedestal instance;

   public BlockPedestal() {
      super(Material.field_151576_e, TilePedestal.class, BlockPedestal.PedestalType.class);
      this.func_149672_a(SoundType.field_185851_d);
      instance = this;
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
         TileEntity tile = world.func_175625_s(pos);
         if (tile != null && tile instanceof TilePedestal) {
            TilePedestal ped = (TilePedestal)tile;
            if (ped.func_70301_a(0) == null && player.field_71071_by.func_70448_g() != null && player.field_71071_by.func_70448_g().field_77994_a > 0) {
               ItemStack i = heldItem.func_77946_l();
               i.field_77994_a = 1;
               ped.func_70299_a(0, i);
               --heldItem.field_77994_a;
               if (heldItem.field_77994_a == 0) {
                  heldItem = null;
               }

               player.field_71071_by.func_70296_d();
               world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187638_cR, SoundCategory.BLOCKS, 0.2F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 1.6F);
               return true;
            }

            if (ped.func_70301_a(0) != null) {
               InventoryUtils.dropItemsAtEntity(world, pos, player);
               world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187638_cR, SoundCategory.BLOCKS, 0.2F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 1.5F);
               return true;
            }
         }

         return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
      }
   }

   public static enum PedestalType implements IStringSerializable {
      NORMAL,
      ELDRITCH,
      ANCIENT;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
