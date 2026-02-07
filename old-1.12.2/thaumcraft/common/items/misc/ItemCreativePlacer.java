package thaumcraft.common.items.misc;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.world.ThaumcraftWorldGenerator;

public class ItemCreativePlacer extends ItemTCBase {
   public ItemCreativePlacer() {
      super("creative_placer", "obelisk", "node", "caster");
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.func_77624_a(stack, player, list, par4);
      list.add(TextFormatting.DARK_PURPLE + "Creative only");
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      IBlockState bs = world.func_180495_p(pos);
      if (!bs.func_185904_a().func_76220_a()) {
         return EnumActionResult.FAIL;
      } else if (world.field_72995_K) {
         return EnumActionResult.PASS;
      } else {
         pos = pos.func_177972_a(side);
         bs = world.func_180495_p(pos);
         if (!player.func_175151_a(pos, side, stack)) {
            return EnumActionResult.FAIL;
         } else if (!bs.func_177230_c().func_176200_f(world, pos)) {
            return EnumActionResult.FAIL;
         } else if (stack.func_77952_i() == 0 && !world.func_180495_p(pos.func_177977_b()).func_185904_a().func_76220_a()) {
            return EnumActionResult.FAIL;
         } else {
            world.func_175698_g(pos);
            switch(stack.func_77952_i()) {
            case 1:
               ThaumcraftWorldGenerator.spawnNode(world, pos, -1, 1.0F, false);
            default:
               return EnumActionResult.SUCCESS;
            }
         }
      }
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.EPIC;
   }
}
