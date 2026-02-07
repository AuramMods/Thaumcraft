package thaumcraft.common.blocks.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.internal.WeightedRandomLoot;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.config.Config;

public class BlockStoneTC extends BlockTC {
   public static BlockStoneTC instance;
   private static int cc = 0;
   static Random r = new Random(System.currentTimeMillis());
   static ArrayList<WeightedRandomLoot> pdrops = null;

   public BlockStoneTC() {
      super(Material.field_151576_e, BlockStoneTC.StoneType.class);
      this.func_149711_c(2.0F);
      this.func_149752_b(10.0F);
      this.func_149672_a(SoundType.field_185851_d);
      instance = this;
   }

   public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
      return true;
   }

   public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
      if (state.func_177230_c() != this) {
         return super.canCreatureSpawn(state, world, pos, type);
      } else {
         return (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) != BlockStoneTC.StoneType.ANCIENT_ROCK && (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) != BlockStoneTC.StoneType.ANCIENT_DOORWAY && (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) != BlockStoneTC.StoneType.GLYPHED && (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) != BlockStoneTC.StoneType.ANCIENT_TILE ? super.canCreatureSpawn(state, world, pos, type) : false;
      }
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      if (state.func_177230_c() != this) {
         return super.getLightValue(state, world, pos);
      } else {
         return (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.CRUST_GLOW ? 9 : super.getLightValue(state, world, pos);
      }
   }

   public float func_176195_g(IBlockState state, World worldIn, BlockPos pos) {
      if (state.func_177230_c() != this) {
         return super.func_176195_g(state, worldIn, pos);
      } else {
         return (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.ANCIENT_ROCK ? 6.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.CRUST ? 1.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.POROUS ? 1.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.CRUST_GLOW ? 1.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.ELDRITCH ? 15.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.ANCIENT_DOORWAY ? -1.0F : super.func_176195_g(state, worldIn, pos))))));
      }
   }

   public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
      IBlockState state = world.func_180495_p(pos);
      if (state.func_177230_c() != this) {
         return super.getExplosionResistance(world, pos, exploder, explosion);
      } else {
         return (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.ANCIENT_ROCK ? 20.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.CRUST ? 5.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.CRUST_GLOW ? 5.0F : ((BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.ELDRITCH ? 1000.0F : super.getExplosionResistance(world, pos, exploder, explosion))));
      }
   }

   private boolean isBlockExposed(World world, BlockPos pos) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing face = var3[var5];
         if (world.func_175623_d(pos.func_177972_a(face))) {
            return true;
         }
      }

      return false;
   }

   public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      if (state.func_177230_c() == this && (BlockStoneTC.StoneType)state.func_177229_b(this.TYPE) == BlockStoneTC.StoneType.POROUS) {
         List<ItemStack> ret = new ArrayList();
         int rr = r.nextInt(15) + fortune;
         if (rr > 13) {
            if (pdrops == null || pdrops.size() <= 0) {
               this.createDrops();
            }

            ItemStack s = ((WeightedRandomLoot)WeightedRandom.func_76271_a(r, pdrops)).item.func_77946_l();
            ret.add(s);
         } else {
            ret.add(new ItemStack(Blocks.field_150347_e));
         }

         return ret;
      } else {
         return super.getDrops(world, pos, state, fortune);
      }
   }

   private void createDrops() {
      pdrops = new ArrayList();
      Iterator var1 = Aspect.getCompoundAspects().iterator();

      while(var1.hasNext()) {
         Aspect aspect = (Aspect)var1.next();
         ItemStack is = new ItemStack(ItemsTC.crystalEssence);
         ((ItemGenericEssentiaContainer)ItemsTC.crystalEssence).setAspects(is, (new AspectList()).add(aspect, aspect == Aspect.FLUX ? 100 : (aspect.isPrimal() ? 20 : 1)));
         pdrops.add(new WeightedRandomLoot(is, 1));
      }

      pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.amber), 20));
      pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 0), 20));
      pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 1), 10));
      pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 6), 10));
      if (Config.foundCopperIngot) {
         pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 2), 10));
      }

      if (Config.foundTinIngot) {
         pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 3), 10));
      }

      if (Config.foundSilverIngot) {
         pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 4), 8));
      }

      if (Config.foundLeadIngot) {
         pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 5), 10));
      }

      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_151045_i), 2));
      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_151166_bC), 4));
      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_151137_ax), 8));
      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_179563_cD), 3));
      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_179562_cC), 3));
      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_151119_aD), 30));
      pdrops.add(new WeightedRandomLoot(new ItemStack(Items.field_151128_bU), 15));
   }

   public static enum StoneType implements IStringSerializable {
      ARCANE,
      ARCANE_BRICK,
      ANCIENT,
      ANCIENT_ROCK,
      ELDRITCH,
      ANCIENT_TILE,
      CRUST,
      CRUST_GLOW,
      MATRIX_SPEED,
      MATRIX_COST,
      ANCIENT_DOORWAY,
      GLYPHED,
      POROUS;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
