package thaumcraft.common.blocks.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.lib.utils.Utils;

public class BlockLoot extends BlockTC {
   Random rand = new Random();

   public BlockLoot(Material mat, SoundType soundType) {
      super(mat, BlockLoot.LootType.class);
      this.func_149711_c(0.15F);
      this.func_149752_b(0.0F);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(this.TYPE, BlockLoot.LootType.COMMON));
      this.func_149672_a(soundType);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   protected boolean func_149700_E() {
      return true;
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return this.func_149688_o(state) == Material.field_151576_e ? new AxisAlignedBB(0.125D, 0.0625D, 0.125D, 0.875D, 0.8125D, 0.875D) : new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
   }

   public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      int md = this.func_176201_c(state);
      ArrayList<ItemStack> ret = new ArrayList();
      int q = 1 + md + this.rand.nextInt(3);

      for(int a = 0; a < q; ++a) {
         ItemStack is = Utils.generateLoot(md, this.rand);
         if (is != null) {
            ret.add(is.func_77946_l());
         }
      }

      return ret;
   }

   public static enum LootType implements IStringSerializable {
      COMMON,
      UNCOMMON,
      RARE;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
