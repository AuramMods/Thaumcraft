package thaumcraft.common.blocks.world.taint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.internal.WeightedRandomLoot;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.basic.BlockStoneTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntityFallingTaint;
import thaumcraft.common.entities.monster.tainted.EntityTaintSwarm;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.Utils;

public class BlockTaint extends BlockTC implements ITaintBlock {
   static Random r = new Random(System.currentTimeMillis());
   static ArrayList<WeightedRandomLoot> pdrops = null;

   public BlockTaint() {
      super(ThaumcraftMaterials.MATERIAL_TAINT, BlockTaint.TaintType.class);
      this.func_149711_c(10.0F);
      this.func_149752_b(100.0F);
      this.func_149672_a(SoundsTC.GORE);
      this.func_149675_a(true);
   }

   public MapColor func_180659_g(IBlockState state) {
      return MapColor.field_151678_z;
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public void die(World world, BlockPos pos, IBlockState blockState) {
      if ((BlockTaint.TaintType)blockState.func_177229_b(this.TYPE) == BlockTaint.TaintType.ROCK) {
         world.func_175656_a(pos, BlocksTC.stone.func_176203_a(BlockStoneTC.StoneType.POROUS.ordinal()));
      } else if ((BlockTaint.TaintType)blockState.func_177229_b(this.TYPE) == BlockTaint.TaintType.SOIL) {
         world.func_175656_a(pos, Blocks.field_150346_d.func_176223_P());
      } else if ((BlockTaint.TaintType)blockState.func_177229_b(this.TYPE) == BlockTaint.TaintType.CRUST) {
         world.func_175656_a(pos, BlocksTC.fluxGoo.func_176223_P());
      } else if ((BlockTaint.TaintType)blockState.func_177229_b(this.TYPE) == BlockTaint.TaintType.GEYSER) {
         world.func_175656_a(pos, BlocksTC.fluxGoo.func_176223_P());
      } else {
         world.func_175698_g(pos);
      }

   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random random) {
      if (!world.field_72995_K) {
         if (!TaintHelper.isNearTaintSeed(world, pos) && random.nextInt(10) == 0) {
            this.die(world, pos, state);
            return;
         }

         if ((BlockTaint.TaintType)state.func_177229_b(this.TYPE) != BlockTaint.TaintType.ROCK) {
            TaintHelper.spreadFibres(world, pos);
         }

         if ((BlockTaint.TaintType)state.func_177229_b(this.TYPE) == BlockTaint.TaintType.CRUST) {
            new Random(pos.func_177986_g());
            if (this.tryToFall(world, pos, pos)) {
               return;
            }

            if (world.func_175623_d(pos.func_177984_a())) {
               boolean doIt = true;
               EnumFacing dir = EnumFacing.field_176754_o[random.nextInt(4)];

               for(int a = 1; a < 4; ++a) {
                  if (!world.func_175623_d(pos.func_177972_a(dir).func_177979_c(a))) {
                     doIt = false;
                     break;
                  }

                  if (world.func_180495_p(pos.func_177979_c(a)).func_177230_c() != this) {
                     doIt = false;
                     break;
                  }
               }

               if (doIt && this.tryToFall(world, pos, pos.func_177972_a(dir))) {
                  return;
               }
            }
         } else if ((BlockTaint.TaintType)state.func_177229_b(this.TYPE) == BlockTaint.TaintType.GEYSER) {
            if ((double)world.field_73012_v.nextFloat() < 0.2D && world.func_175636_b((double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 0.5F), (double)((float)pos.func_177952_p() + 0.5F), 24.0D)) {
               Entity e = new EntityTaintSwarm(world);
               e.func_70012_b((double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 1.25F), (double)((float)pos.func_177952_p() + 0.5F), (float)world.field_73012_v.nextInt(360), 0.0F);
               world.func_72838_d(e);
            } else if (AuraHelper.getFlux(world, pos) < 125.0F) {
               AuraHelper.polluteAura(world, pos, 1.0F, true);
            }
         }
      }

   }

   public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      return true;
   }

   public void func_176199_a(World world, BlockPos pos, Entity entity) {
      if (!world.field_72995_K && entity instanceof EntityLivingBase && !((EntityLivingBase)entity).func_70662_br() && world.field_73012_v.nextInt(750) == 0) {
         ((EntityLivingBase)entity).func_70690_d(new PotionEffect(PotionFluxTaint.instance, 200, 0, false, true));
      }

   }

   public boolean func_189539_a(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
      if (eventID == 1) {
         if (worldIn.field_72995_K) {
            worldIn.func_184133_a((EntityPlayer)null, pos, SoundsTC.roots, SoundCategory.BLOCKS, 0.1F, 0.9F + worldIn.field_73012_v.nextFloat() * 0.2F);
         }

         return true;
      } else {
         return super.func_189539_a(state, worldIn, pos, eventID, eventParam);
      }
   }

   public static boolean canFallBelow(World world, BlockPos pos) {
      IBlockState bs = world.func_180495_p(pos);
      Block l = bs.func_177230_c();

      for(int xx = -1; xx <= 1; ++xx) {
         for(int zz = -1; zz <= 1; ++zz) {
            for(int yy = -1; yy <= 1; ++yy) {
               if (Utils.isWoodLog(world, pos.func_177982_a(xx, yy, zz))) {
                  return false;
               }
            }
         }
      }

      if (l.isAir(bs, world, pos)) {
         return true;
      } else if (l == BlocksTC.fluxGoo && (Integer)bs.func_177229_b(BlockFluidFinite.LEVEL) >= 4) {
         return false;
      } else if (l != Blocks.field_150480_ab && l != BlocksTC.taintFibre) {
         if (l.func_176200_f(world, pos)) {
            return true;
         } else {
            return bs.func_185904_a() == Material.field_151586_h ? true : bs.func_185904_a() == Material.field_151587_i;
         }
      } else {
         return true;
      }
   }

   private boolean tryToFall(World world, BlockPos pos, BlockPos pos2) {
      if (!BlockTaintFibre.isOnlyAdjacentToTaint(world, pos)) {
         return false;
      } else {
         if (canFallBelow(world, pos2.func_177977_b()) && pos2.func_177956_o() >= 0) {
            byte b0 = 32;
            if (world.func_175707_a(pos2.func_177982_a(-b0, -b0, -b0), pos2.func_177982_a(b0, b0, b0))) {
               if (!world.field_72995_K) {
                  EntityFallingTaint entityfalling = new EntityFallingTaint(world, (double)((float)pos2.func_177958_n() + 0.5F), (double)((float)pos2.func_177956_o() + 0.5F), (double)((float)pos2.func_177952_p() + 0.5F), world.func_180495_p(pos), pos);
                  world.func_72838_d(entityfalling);
                  return true;
               }
            } else {
               world.func_175698_g(pos);

               BlockPos p2;
               for(p2 = new BlockPos(pos2); canFallBelow(world, p2.func_177977_b()) && p2.func_177956_o() > 0; p2 = p2.func_177977_b()) {
               }

               if (p2.func_177956_o() > 0) {
                  world.func_175656_a(p2, BlocksTC.taintBlock.func_176223_P());
               }
            }
         }

         return false;
      }
   }

   public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      if (state.func_177230_c() == this && (BlockTaint.TaintType)state.func_177229_b(this.TYPE) == BlockTaint.TaintType.ROCK) {
         int rr = r.nextInt(15) + fortune;
         if (rr > 13) {
            List<ItemStack> ret = new ArrayList();
            ret.add(ConfigItems.FLUX_CRYSTAL.func_77946_l());
            return ret;
         }
      }

      return super.getDrops(world, pos, state, fortune);
   }

   protected boolean func_149700_E() {
      return false;
   }

   public static enum TaintType implements IStringSerializable {
      CRUST,
      SOIL,
      GEYSER,
      ROCK;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
