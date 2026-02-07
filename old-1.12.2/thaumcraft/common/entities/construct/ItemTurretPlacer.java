package thaumcraft.common.entities.construct;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.items.ItemTCBase;

public class ItemTurretPlacer extends ItemTCBase {
   public ItemTurretPlacer() {
      super("turret", "basic", "advanced", "bore");
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (side == EnumFacing.DOWN) {
         return EnumActionResult.PASS;
      } else {
         boolean flag = world.func_180495_p(pos).func_177230_c().func_176200_f(world, pos);
         BlockPos blockpos = flag ? pos : pos.func_177972_a(side);
         if (!player.func_175151_a(blockpos, side, stack)) {
            return EnumActionResult.PASS;
         } else {
            BlockPos blockpos1 = blockpos.func_177984_a();
            boolean flag1 = !world.func_175623_d(blockpos) && !world.func_180495_p(blockpos).func_177230_c().func_176200_f(world, blockpos);
            flag1 |= !world.func_175623_d(blockpos1) && !world.func_180495_p(blockpos1).func_177230_c().func_176200_f(world, blockpos1);
            if (flag1) {
               return EnumActionResult.PASS;
            } else {
               double d0 = (double)blockpos.func_177958_n();
               double d1 = (double)blockpos.func_177956_o();
               double d2 = (double)blockpos.func_177952_p();
               List<Entity> list = world.func_72839_b((Entity)null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
               if (!list.isEmpty()) {
                  return EnumActionResult.PASS;
               } else if (!world.field_72995_K) {
                  world.func_175698_g(blockpos);
                  world.func_175698_g(blockpos1);
                  EntityOwnedConstruct turret = null;
                  switch(stack.func_77952_i()) {
                  case 0:
                     turret = new EntityTurretCrossbow(world, blockpos);
                     break;
                  case 1:
                     turret = new EntityTurretCrossbowAdvanced(world, blockpos);
                     break;
                  case 2:
                     turret = new EntityArcaneBore(world, blockpos, player.func_174811_aO());
                  }

                  if (turret != null) {
                     world.func_72838_d((Entity)turret);
                     ((EntityOwnedConstruct)turret).setOwned(true);
                     ((EntityOwnedConstruct)turret).setValidSpawn();
                     ((EntityOwnedConstruct)turret).setOwnerId(player.func_110124_au());
                     world.func_184148_a((EntityPlayer)null, ((EntityOwnedConstruct)turret).field_70165_t, ((EntityOwnedConstruct)turret).field_70163_u, ((EntityOwnedConstruct)turret).field_70161_v, SoundEvents.field_187710_m, SoundCategory.BLOCKS, 0.75F, 0.8F);
                  }

                  return EnumActionResult.SUCCESS;
               } else {
                  if (!player.field_71075_bZ.field_75098_d) {
                     --stack.field_77994_a;
                  }

                  return EnumActionResult.PASS;
               }
            }
         }
      }
   }
}
