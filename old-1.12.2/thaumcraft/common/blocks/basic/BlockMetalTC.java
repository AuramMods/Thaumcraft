package thaumcraft.common.blocks.basic;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.blocks.BlockTC;

public class BlockMetalTC extends BlockTC {
   public BlockMetalTC() {
      super(Material.field_151573_f, BlockMetalTC.MetalType.class);
      this.func_149711_c(4.0F);
      this.func_149752_b(10.0F);
      this.func_149672_a(SoundType.field_185852_e);
   }

   public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
      return true;
   }

   public static enum MetalType implements IStringSerializable {
      THAUMIUM,
      VOID,
      ALCHEMICAL,
      ADVANCED_ALCHEMICAL,
      BRASS;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
