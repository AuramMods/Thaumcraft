package thaumcraft.common.blocks.basic;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.tiles.misc.TileBarrierStone;

public class BlockPavingStone extends BlockTC {
   public BlockPavingStone() {
      super(Material.field_151576_e, BlockPavingStone.PavingStoneType.class);
      this.func_149711_c(2.5F);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149675_a(true);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
   }

   public boolean hasTileEntity(IBlockState state) {
      return state.func_177230_c().func_176201_c(state) == 0;
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return state.func_177230_c().func_176201_c(state) == 0 ? new TileBarrierStone() : null;
   }

   public int func_149750_m(IBlockState state) {
      return (BlockPavingStone.PavingStoneType)state.func_177229_b(this.TYPE) == BlockPavingStone.PavingStoneType.TRAVEL_LIT ? 15 : super.func_149750_m(state);
   }

   public void func_176199_a(World worldIn, BlockPos pos, Entity e) {
      IBlockState state = worldIn.func_180495_p(pos);
      if (!worldIn.field_72995_K && this.func_176201_c(state) > 0 && e instanceof EntityLivingBase) {
         ((EntityLivingBase)e).func_70690_d(new PotionEffect(MobEffects.field_76424_c, 40, 1, false, false));
         ((EntityLivingBase)e).func_70690_d(new PotionEffect(MobEffects.field_76430_j, 40, 0, false, false));
      }

      super.func_176199_a(worldIn, pos, e);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random random) {
      if (state.func_177230_c().func_176201_c(state) == 0) {
         int a;
         if (world.func_175687_A(pos) > 0) {
            for(a = 0; a < FXDispatcher.INSTANCE.particleCount(2); ++a) {
               FXDispatcher.INSTANCE.blockRunes((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + 0.7F), (double)pos.func_177952_p(), 0.2F + random.nextFloat() * 0.4F, random.nextFloat() * 0.3F, 0.8F + random.nextFloat() * 0.2F, 20, -0.02F);
            }
         } else if (world.func_180495_p(pos.func_177981_b(1)) == BlocksTC.barrier.func_176223_P() && world.func_180495_p(pos.func_177981_b(1)).func_177230_c().func_176205_b(world, pos.func_177981_b(1)) || world.func_180495_p(pos.func_177981_b(2)) == BlocksTC.barrier.func_176223_P() && world.func_180495_p(pos.func_177981_b(2)).func_177230_c().func_176205_b(world, pos.func_177981_b(2))) {
            for(a = 0; a < FXDispatcher.INSTANCE.particleCount(3); ++a) {
               FXDispatcher.INSTANCE.blockRunes((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + 0.7F), (double)pos.func_177952_p(), 0.9F + random.nextFloat() * 0.1F, random.nextFloat() * 0.3F, random.nextFloat() * 0.3F, 24, -0.02F);
            }
         } else {
            List list = world.func_72839_b((Entity)null, (new AxisAlignedBB((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), (double)(pos.func_177958_n() + 1), (double)(pos.func_177956_o() + 1), (double)(pos.func_177952_p() + 1))).func_72314_b(1.0D, 1.0D, 1.0D));
            if (!list.isEmpty()) {
               Iterator iterator = list.iterator();

               while(iterator.hasNext()) {
                  Entity entity = (Entity)iterator.next();
                  if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                     FXDispatcher.INSTANCE.blockRunes((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + 0.6F + random.nextFloat() * Math.max(0.8F, entity.func_70047_e())), (double)pos.func_177952_p(), 0.6F + random.nextFloat() * 0.4F, 0.0F, 0.3F + random.nextFloat() * 0.7F, 20, 0.0F);
                     break;
                  }
               }
            }
         }
      }

      if (state.func_177230_c().func_176201_c(state) == 2 && random.nextBoolean()) {
         FXDispatcher.INSTANCE.drawWispyMotesOnBlock(pos.func_177984_a(), 50, -0.01F);
      }

   }

   public static enum PavingStoneType implements IStringSerializable {
      BARRIER,
      TRAVEL,
      TRAVEL_LIT;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
