package thaumcraft.api.golems.seals;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SealPos {
   public BlockPos pos;
   public EnumFacing face;

   public SealPos(BlockPos pos, EnumFacing face) {
      this.pos = pos;
      this.face = face;
   }

   public int hashCode() {
      byte b0 = (byte)(this.face.ordinal() + 1);
      int i = 31 * b0 + this.pos.func_177958_n();
      i = 31 * i + this.pos.func_177956_o();
      i = 31 * i + this.pos.func_177952_p();
      return i;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof SealPos)) {
         return false;
      } else {
         SealPos sp = (SealPos)p_equals_1_;
         return !this.pos.equals(sp.pos) ? false : this.face.equals(sp.face);
      }
   }
}
