package thaumcraft.common.blocks;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public interface IBlockFacingHorizontal {
   PropertyDirection FACING = PropertyDirection.func_177712_a("facing", new Predicate() {
      public boolean apply(EnumFacing facing) {
         return facing != EnumFacing.UP && facing != EnumFacing.DOWN;
      }

      public boolean apply(Object p_apply_1_) {
         return this.apply((EnumFacing)p_apply_1_);
      }
   });
}
