package thaumcraft.common.blocks;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public interface IBlockFacing {
   PropertyDirection FACING = PropertyDirection.func_177712_a("facing", new Predicate() {
      public boolean apply(EnumFacing facing) {
         return true;
      }

      public boolean apply(Object p_apply_1_) {
         return this.apply((EnumFacing)p_apply_1_);
      }
   });
}
