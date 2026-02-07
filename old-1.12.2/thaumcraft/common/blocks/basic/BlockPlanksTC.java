package thaumcraft.common.blocks.basic;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.blocks.BlockTC;

public class BlockPlanksTC extends BlockTC {
   public BlockPlanksTC() {
      super(Material.field_151575_d, BlockPlanksTC.PlankType.class);
      this.setHarvestLevel("axe", 0);
      this.func_149711_c(2.0F);
      this.func_149672_a(SoundType.field_185848_a);
   }

   public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 20;
   }

   public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 5;
   }

   public static enum PlankType implements IStringSerializable {
      GREATWOOD,
      SILVERWOOD;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
