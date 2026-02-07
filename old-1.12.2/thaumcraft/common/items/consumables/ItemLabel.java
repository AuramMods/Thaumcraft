package thaumcraft.common.items.consumables;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.ILabelable;
import thaumcraft.common.items.ItemTCEssentiaContainer;

public class ItemLabel extends ItemTCEssentiaContainer {
   public ItemLabel() {
      super("label", 1, "blank", "filled");
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (world.field_72995_K) {
         return EnumActionResult.PASS;
      } else {
         IBlockState bs = world.func_180495_p(pos);
         if (bs.func_177230_c() instanceof ILabelable) {
            if (((ILabelable)bs.func_177230_c()).applyLabel(player, pos, side, stack)) {
               --stack.field_77994_a;
               player.field_71069_bz.func_75142_b();
            }

            return EnumActionResult.SUCCESS;
         } else {
            TileEntity te = world.func_175625_s(pos);
            if (te instanceof ILabelable) {
               if (((ILabelable)te).applyLabel(player, pos, side, stack)) {
                  --stack.field_77994_a;
                  player.field_71069_bz.func_75142_b();
               }

               return EnumActionResult.SUCCESS;
            } else {
               return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
            }
         }
      }
   }

   public void func_77663_a(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
   }

   public void func_77622_d(ItemStack stack, World world, EntityPlayer player) {
   }

   public boolean ignoreContainedAspects() {
      return true;
   }
}
