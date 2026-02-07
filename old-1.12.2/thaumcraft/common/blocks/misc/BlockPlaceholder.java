package thaumcraft.common.blocks.misc;

import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.crafting.BlockGolemBuilder;
import thaumcraft.common.blocks.devices.BlockInfernalFurnace;

public class BlockPlaceholder extends BlockTC {
   public static final PropertyEnum VARIANT = PropertyEnum.func_177709_a("type", BlockPlaceholder.PlaceholderType.class);
   private final Random rand = new Random();

   public BlockPlaceholder() {
      super(Material.field_151576_e);
      this.func_149711_c(2.5F);
      this.func_149672_a(SoundType.field_185851_d);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(VARIANT, BlockPlaceholder.PlaceholderType.FURNACE_BRICK);
      this.func_180632_j(bs);
      this.func_149647_a((CreativeTabs)null);
   }

   protected boolean func_149700_E() {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      int t = ((BlockPlaceholder.PlaceholderType)state.func_177229_b(VARIANT)).ordinal();
      if (t == 0) {
         return Item.func_150898_a(Blocks.field_150385_bj);
      } else if (t == 1) {
         return Item.func_150898_a(Blocks.field_150343_Z);
      } else if (t == 2) {
         return Item.func_150898_a(Blocks.field_150411_aY);
      } else if (t == 3) {
         return Item.func_150898_a(Blocks.field_150467_bQ);
      } else if (t == 4) {
         return Item.func_150898_a(Blocks.field_150383_bp);
      } else {
         return t == 5 ? Item.func_150898_a(BlocksTC.tableStone) : Item.func_150899_d(0);
      }
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         int t = ((BlockPlaceholder.PlaceholderType)state.func_177229_b(VARIANT)).ordinal();
         if (t > 1 && t <= 5) {
            for(int a = -1; a <= 1; ++a) {
               for(int b = -1; b <= 1; ++b) {
                  for(int c = -1; c <= 1; ++c) {
                     IBlockState s = world.func_180495_p(pos.func_177982_a(a, b, c));
                     if (s.func_177230_c() == BlocksTC.golemBuilder) {
                        player.openGui(Thaumcraft.instance, 19, world, pos.func_177982_a(a, b, c).func_177958_n(), pos.func_177982_a(a, b, c).func_177956_o(), pos.func_177982_a(a, b, c).func_177952_p());
                        return true;
                     }
                  }
               }
            }
         }

         return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
      }
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      int t = ((BlockPlaceholder.PlaceholderType)state.func_177229_b(VARIANT)).ordinal();
      int a;
      int b;
      int c;
      IBlockState s;
      if (t <= 1 && !BlockInfernalFurnace.ignore && !worldIn.field_72995_K) {
         label73:
         for(a = -1; a <= 1; ++a) {
            for(b = -1; b <= 1; ++b) {
               for(c = -1; c <= 1; ++c) {
                  s = worldIn.func_180495_p(pos.func_177982_a(a, b, c));
                  if (s.func_177230_c() == BlocksTC.infernalFurnace) {
                     BlockInfernalFurnace.destroyFurnace(worldIn, pos.func_177982_a(a, b, c), s, pos);
                     break label73;
                  }
               }
            }
         }
      } else if (t <= 5 && !BlockGolemBuilder.ignore && !worldIn.field_72995_K) {
         label51:
         for(a = -1; a <= 1; ++a) {
            for(b = -1; b <= 1; ++b) {
               for(c = -1; c <= 1; ++c) {
                  s = worldIn.func_180495_p(pos.func_177982_a(a, b, c));
                  if (s.func_177230_c() == BlocksTC.golemBuilder) {
                     BlockGolemBuilder.destroy(worldIn, pos.func_177982_a(a, b, c), s, pos);
                     break label51;
                  }
               }
            }
         }
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(VARIANT, BlockPlaceholder.PlaceholderType.values()[meta]);
   }

   public int func_176201_c(IBlockState state) {
      int meta = ((BlockPlaceholder.PlaceholderType)state.func_177229_b(VARIANT)).ordinal();
      return meta;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{VARIANT});
   }

   public static enum PlaceholderType implements IStringSerializable {
      FURNACE_BRICK,
      FURNACE_OBSIDIAN,
      GB_BARS,
      GB_ANVIL,
      GB_CAULDRON,
      GB_TABLE;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
