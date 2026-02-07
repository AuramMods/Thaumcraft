package thaumcraft.common.items.consumables;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.items.ItemTCBase;

public class ItemBucketPure extends ItemTCBase {
   public ItemBucketPure() {
      super("bucket_pure");
      this.func_77627_a(false);
      this.func_77625_d(1);
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      boolean flag = false;
      RayTraceResult rayTraceResult = this.func_77621_a(worldIn, playerIn, flag);
      if (rayTraceResult == null) {
         return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
      } else {
         ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, rayTraceResult);
         if (ret != null) {
            return ret;
         } else {
            if (rayTraceResult.field_72313_a == Type.BLOCK) {
               BlockPos blockpos = rayTraceResult.func_178782_a();
               if (!worldIn.func_175660_a(playerIn, blockpos)) {
                  return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
               }

               BlockPos blockpos1 = blockpos.func_177972_a(rayTraceResult.field_178784_b);
               if (!playerIn.func_175151_a(blockpos1, rayTraceResult.field_178784_b, itemStackIn)) {
                  return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
               }

               if (this.tryPlaceContainedLiquid(worldIn, blockpos1) && !playerIn.field_71075_bZ.field_75098_d) {
                  return new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Items.field_151133_ar));
               }
            }

            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
         }
      }
   }

   public boolean tryPlaceContainedLiquid(World world, BlockPos pos) {
      Material material = world.func_180495_p(pos).func_185904_a();
      boolean flag = !material.func_76220_a();
      if (!world.func_175623_d(pos) && !flag) {
         return false;
      } else {
         if (!world.field_72995_K && flag && !material.func_76224_d()) {
            world.func_175655_b(pos, true);
         }

         world.func_175656_a(pos, BlocksTC.purifyingFluid.func_176223_P());
         return true;
      }
   }
}
