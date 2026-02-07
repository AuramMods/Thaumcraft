package thaumcraft.common.blocks.basic;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.ItemBlockTC;
import thaumcraft.common.tiles.misc.TileBanner;

public class BlockBannerTCItem extends ItemBlockTC {
   public BlockBannerTCItem(Block block) {
      super(block);
   }

   public void func_77624_a(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74779_i("aspect") != null && Aspect.getAspect(stack.func_77978_p().func_74779_i("aspect")) != null) {
         list.add(Aspect.getAspect(stack.func_77978_p().func_74779_i("aspect")).getName());
      }

   }

   public String func_77667_c(ItemStack stack) {
      return stack.func_77942_o() && stack.func_77978_p().func_74764_b("color") ? super.func_77658_a() + "." + EnumDyeColor.func_176766_a(stack.func_77978_p().func_74771_c("color")).func_176610_l() : super.func_77658_a() + ".cultist";
   }

   public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (side == EnumFacing.DOWN) {
         return EnumActionResult.FAIL;
      } else if (!worldIn.func_180495_p(pos).func_185904_a().func_76220_a()) {
         return EnumActionResult.FAIL;
      } else {
         pos = pos.func_177972_a(side);
         if (!playerIn.func_175151_a(pos, side, stack)) {
            return EnumActionResult.FAIL;
         } else if (!Blocks.field_180393_cK.func_176196_c(worldIn, pos)) {
            return EnumActionResult.FAIL;
         } else if (worldIn.field_72995_K) {
            return EnumActionResult.FAIL;
         } else {
            worldIn.func_180501_a(pos, BlocksTC.banner.func_176223_P(), 3);
            TileBanner tile = (TileBanner)worldIn.func_175625_s(pos);
            if (tile != null) {
               if (side == EnumFacing.UP) {
                  int i = MathHelper.func_76128_c((double)((playerIn.field_70177_z + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                  tile.setBannerFacing((byte)i);
               } else {
                  tile.setWall(true);
                  int i = 0;
                  if (side == EnumFacing.NORTH) {
                     i = 8;
                  }

                  if (side == EnumFacing.WEST) {
                     i = 4;
                  }

                  if (side == EnumFacing.EAST) {
                     i = 12;
                  }

                  tile.setBannerFacing((byte)i);
               }

               if (stack.func_77942_o()) {
                  if (stack.func_77978_p().func_74779_i("aspect") != null) {
                     tile.setAspect(Aspect.getAspect(stack.func_77978_p().func_74779_i("aspect")));
                  }

                  if (stack.func_77978_p().func_74764_b("color")) {
                     tile.setColor(stack.func_77978_p().func_74771_c("color"));
                  }
               }

               tile.func_70296_d();
               worldIn.markAndNotifyBlock(pos, worldIn.func_175726_f(pos), BlocksTC.banner.func_176223_P(), BlocksTC.banner.func_176223_P(), 3);
            }

            --stack.field_77994_a;
            return EnumActionResult.SUCCESS;
         }
      }
   }
}
