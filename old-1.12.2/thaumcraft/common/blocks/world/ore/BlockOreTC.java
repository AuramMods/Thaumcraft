package thaumcraft.common.blocks.world.ore;

import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.BlockTC;

public class BlockOreTC extends BlockTC {
   public BlockOreTC() {
      super(Material.field_151576_e);
      this.func_149752_b(5.0F);
      this.func_149672_a(SoundType.field_185851_d);
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return this == BlocksTC.oreQuartz ? Items.field_151128_bU : (this == BlocksTC.oreAmber ? ItemsTC.amber : Item.func_150898_a(this));
   }

   public int func_149745_a(Random random) {
      return this == BlocksTC.oreAmber ? 1 + random.nextInt(2) : 1;
   }

   public void func_176208_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
      if (this == BlocksTC.oreAmber && !worldIn.field_72995_K && worldIn.field_73012_v.nextFloat() < 0.125F) {
         func_180635_a(worldIn, pos, new ItemStack(ItemsTC.curio, 1, 1));
      }

      super.func_176208_a(worldIn, pos, state, player);
   }

   public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
      Random rand = world instanceof World ? ((World)world).field_73012_v : new Random();
      if (this.func_180660_a(state, rand, fortune) == Item.func_150898_a(this)) {
         return 0;
      } else {
         int j = 0;
         if (this == BlocksTC.oreAmber || this == BlocksTC.oreQuartz) {
            j = MathHelper.func_76136_a(rand, 1, 4);
         }

         return j;
      }
   }

   public int func_149679_a(int fortune, Random random) {
      if (fortune > 0 && Item.func_150898_a(this) != this.func_180660_a((IBlockState)this.func_176194_O().func_177619_a().iterator().next(), random, fortune)) {
         int j = random.nextInt(fortune + 2) - 1;
         if (j < 0) {
            j = 0;
         }

         return this.func_149745_a(random) * (j + 1);
      } else {
         return this.func_149745_a(random);
      }
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }
}
