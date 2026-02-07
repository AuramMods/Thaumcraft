package thaumcraft.common.lib.utils;

import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApiHelper;

public class BlockUtils {
   static BlockPos lastPos;
   static int lasty;
   static int lastz;
   static double lastdistance;
   public static ArrayList<String> portableHoleBlackList;

   public static boolean harvestBlock(World world, EntityPlayer player, BlockPos pos) {
      return harvestBlock(world, player, pos, false, false, 0);
   }

   public static boolean harvestBlock(World world, EntityPlayer player, BlockPos pos, boolean alwaysDrop, boolean silkOverride, int fortuneOverride) {
      if (!world.field_72995_K) {
         int res = ForgeHooks.onBlockBreakEvent(world, GameType.NOT_SET, (EntityPlayerMP)player, pos);
         if (res == -1) {
            return false;
         }
      }

      IBlockState bs = world.func_180495_p(pos);
      if (bs.func_177230_c().func_176195_g(bs, world, pos) < 0.0F) {
         return false;
      } else {
         world.func_180498_a(player, 2001, pos, Block.func_176210_f(bs));
         boolean flag = false;
         if (player.field_71075_bZ.field_75098_d) {
            removeBlock(world, pos, player, false);
         } else {
            boolean flag1 = false;
            if (bs != null) {
               flag1 = alwaysDrop || bs.func_177230_c().canHarvestBlock(world, pos, player);
            }

            flag = removeBlock(world, pos, player, true);
            if (flag && flag1) {
               harvestBlockVanilla(world, player, pos, bs, player.func_184614_ca(), silkOverride, fortuneOverride);

               try {
                  if (!silkOverride && (!bs.func_177230_c().canSilkHarvest(world, pos, bs, player) || EnchantmentHelper.func_185284_a(Enchantments.field_185306_r, player) <= 0)) {
                     int fortune = EnchantmentHelper.func_185284_a(Enchantments.field_185308_t, player);
                     if (fortuneOverride > fortune) {
                        fortune = fortuneOverride;
                     }

                     int exp = bs.func_177230_c().getExpDrop(bs, world, pos, fortune);
                     if (exp > 0) {
                        bs.func_177230_c().func_180637_b(world, pos, exp);
                     }
                  }
               } catch (Exception var11) {
               }
            }
         }

         return true;
      }
   }

   public static void harvestBlockVanilla(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable ItemStack stack, boolean silkOverride, int fortuneOverride) {
      player.func_71029_a(StatList.func_188055_a(state.func_177230_c()));
      if (state.func_177230_c().canSilkHarvest(worldIn, pos, state, player) && (EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, stack) > 0 || silkOverride)) {
         List<ItemStack> items = new ArrayList();
         ItemStack itemstack = getSilkTouchDrop(state);
         if (itemstack != null) {
            items.add(itemstack);
         }

         ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0F, true, player);
         Iterator var9 = items.iterator();

         while(var9.hasNext()) {
            ItemStack item = (ItemStack)var9.next();
            state.func_177230_c();
            Block.func_180635_a(worldIn, pos, item);
         }
      } else {
         int i = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, stack);
         if (fortuneOverride > i) {
            i = fortuneOverride;
         }

         state.func_177230_c().func_176226_b(worldIn, pos, state, i);
      }

   }

   public static void destroyBlockPartially(World world, int par1, BlockPos pos, int par5) {
      Iterator iterator = world.field_73010_i.iterator();

      while(iterator.hasNext()) {
         EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
         if (entityplayermp != null && entityplayermp.field_70170_p == FMLCommonHandler.instance().getMinecraftServerInstance().func_130014_f_() && entityplayermp.func_145782_y() != par1) {
            double d0 = (double)pos.func_177958_n() - entityplayermp.field_70165_t;
            double d1 = (double)pos.func_177956_o() - entityplayermp.field_70163_u;
            double d2 = (double)pos.func_177952_p() - entityplayermp.field_70161_v;
            if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
               entityplayermp.field_71135_a.func_147359_a(new SPacketBlockBreakAnim(par1, pos, par5));
            }
         }
      }

   }

   public static boolean removeBlock(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      IBlockState block = world.func_180495_p(pos);
      if (block != null) {
         block.func_177230_c().func_176208_a(world, pos, world.func_180495_p(pos), player);
      }

      boolean flag = block != null && block.func_177230_c().removedByPlayer(block, world, pos, player, willHarvest);
      if (block != null && flag) {
         block.func_177230_c().func_176206_d(world, pos, world.func_180495_p(pos));
      }

      return flag;
   }

   public static void findBlocks(World world, BlockPos pos, IBlockState block, int reach) {
      for(int xx = -reach; xx <= reach; ++xx) {
         for(int yy = reach; yy >= -reach; --yy) {
            for(int zz = -reach; zz <= reach; ++zz) {
               if (Math.abs(lastPos.func_177958_n() + xx - pos.func_177958_n()) > 24) {
                  return;
               }

               if (Math.abs(lastPos.func_177956_o() + yy - pos.func_177956_o()) > 48) {
                  return;
               }

               if (Math.abs(lastPos.func_177952_p() + zz - pos.func_177952_p()) > 24) {
                  return;
               }

               IBlockState bs = world.func_180495_p(lastPos.func_177982_a(xx, yy, zz));
               boolean same = bs.func_177230_c() == block.func_177230_c() && bs.func_177230_c().func_180651_a(bs) == block.func_177230_c().func_180651_a(block);
               if (same && bs.func_177230_c().func_176195_g(bs, world, lastPos.func_177982_a(xx, yy, zz)) >= 0.0F) {
                  double xd = (double)(lastPos.func_177958_n() + xx - pos.func_177958_n());
                  double yd = (double)(lastPos.func_177956_o() + yy - pos.func_177956_o());
                  double zd = (double)(lastPos.func_177952_p() + zz - pos.func_177952_p());
                  double d = xd * xd + yd * yd + zd * zd;
                  if (d > lastdistance) {
                     lastdistance = d;
                     lastPos = lastPos.func_177982_a(xx, yy, zz);
                     findBlocks(world, pos, block, reach);
                     return;
                  }
               }
            }
         }
      }

   }

   public static boolean breakFurthestBlock(World world, BlockPos pos, IBlockState block, EntityPlayer player) {
      lastPos = new BlockPos(pos);
      lastdistance = 0.0D;
      int reach = Utils.isWoodLog(world, pos) ? 2 : 1;
      findBlocks(world, pos, block, reach);
      boolean worked = harvestBlock(world, player, lastPos);
      world.markAndNotifyBlock(pos, world.func_175726_f(pos), block, block, 3);
      if (worked && Utils.isWoodLog(world, pos)) {
         world.markAndNotifyBlock(pos, world.func_175726_f(pos), block, block, 3);

         for(int xx = -3; xx <= 3; ++xx) {
            for(int yy = -3; yy <= 3; ++yy) {
               for(int zz = -3; zz <= 3; ++zz) {
                  world.func_175684_a(lastPos.func_177982_a(xx, yy, zz), world.func_180495_p(lastPos.func_177982_a(xx, yy, zz)).func_177230_c(), 50 + world.field_73012_v.nextInt(75));
               }
            }
         }
      }

      return worked;
   }

   public static RayTraceResult getTargetBlock(World world, Entity entity, boolean par3) {
      return getTargetBlock(world, entity, par3, par3, 10.0D);
   }

   public static RayTraceResult getTargetBlock(World world, Entity entity, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, double range) {
      float var4 = 1.0F;
      float var5 = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * var4;
      float var6 = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * var4;
      double var7 = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)var4;
      double var9 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)var4 + (double)entity.func_70047_e();
      double var11 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)var4;
      Vec3d var13 = new Vec3d(var7, var9, var11);
      float var14 = MathHelper.func_76134_b(-var6 * 0.017453292F - 3.1415927F);
      float var15 = MathHelper.func_76126_a(-var6 * 0.017453292F - 3.1415927F);
      float var16 = -MathHelper.func_76134_b(-var5 * 0.017453292F);
      float var17 = MathHelper.func_76126_a(-var5 * 0.017453292F);
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      Vec3d var23 = var13.func_72441_c((double)var18 * range, (double)var17 * range, (double)var20 * range);
      return world.func_147447_a(var13, var23, stopOnLiquid, !ignoreBlockWithoutBoundingBox, false);
   }

   public static int countExposedSides(World world, BlockPos pos) {
      int count = 0;
      EnumFacing[] var3 = EnumFacing.field_82609_l;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing dir = var3[var5];
         if (world.func_175623_d(pos.func_177972_a(dir))) {
            ++count;
         }
      }

      return count;
   }

   public static boolean isBlockExposed(World world, BlockPos pos) {
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing face = var2[var4];
         if (!world.func_180495_p(pos.func_177972_a(face)).func_185914_p()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isAdjacentToSolidBlock(World world, BlockPos pos) {
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing face = var2[var4];
         if (world.isSideSolid(pos.func_177972_a(face), face.func_176734_d())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isBlockTouching(IBlockAccess world, BlockPos pos, IBlockState bs) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing face = var3[var5];
         if (world.func_180495_p(pos.func_177972_a(face)) == bs) {
            return true;
         }
      }

      return false;
   }

   public static boolean isBlockTouching(IBlockAccess world, BlockPos pos, Block bs) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing face = var3[var5];
         if (world.func_180495_p(pos.func_177972_a(face)).func_177230_c() == bs) {
            return true;
         }
      }

      return false;
   }

   public static boolean isBlockTouching(IBlockAccess world, BlockPos pos, Material mat, boolean solid) {
      EnumFacing[] var4 = EnumFacing.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumFacing face = var4[var6];
         if (world.func_180495_p(pos.func_177972_a(face)).func_185904_a() == mat && (!solid || world.func_180495_p(pos.func_177972_a(face)).isSideSolid(world, pos.func_177972_a(face), face.func_176734_d()))) {
            return true;
         }
      }

      return false;
   }

   public static EnumFacing getFaceBlockTouching(IBlockAccess world, BlockPos pos, Block bs) {
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing face = var3[var5];
         if (world.func_180495_p(pos.func_177972_a(face)).func_177230_c() == bs) {
            return face;
         }
      }

      return null;
   }

   public static boolean isPortableHoleBlackListed(IBlockState blockstate) {
      return isBlockListed(blockstate, portableHoleBlackList);
   }

   public static boolean isBlockListed(IBlockState blockstate, List<String> list) {
      String stateString = blockstate.toString();
      Iterator var3 = list.iterator();

      String[] splitString;
      int matches;
      do {
         label44:
         do {
            while(var3.hasNext()) {
               String key = (String)var3.next();
               splitString = key.split(";");
               if (splitString[0].contains(":")) {
                  continue label44;
               }

               ItemStack bs = new ItemStack(Item.func_150898_a(blockstate.func_177230_c()), 1, blockstate.func_177230_c().func_176201_c(blockstate));
               Iterator var7 = OreDictionary.getOres(splitString[0], false).iterator();

               while(var7.hasNext()) {
                  ItemStack stack = (ItemStack)var7.next();
                  if (OreDictionary.itemMatches(stack, bs, false)) {
                     return true;
                  }
               }
            }

            return false;
         } while(!((ResourceLocation)Block.field_149771_c.func_177774_c(blockstate.func_177230_c())).toString().equals(splitString[0]));

         if (splitString.length <= 1) {
            return true;
         }

         matches = 0;

         for(int a = 1; a < splitString.length; ++a) {
            if (stateString.contains(splitString[a])) {
               ++matches;
            }
         }
      } while(matches != splitString.length - 1);

      return true;
   }

   public static double distance(BlockPos b1, BlockPos b2) {
      double d3 = (double)(b1.func_177958_n() - b2.func_177958_n());
      double d4 = (double)(b1.func_177956_o() - b2.func_177956_o());
      double d5 = (double)(b1.func_177952_p() - b2.func_177952_p());
      return d3 * d3 + d4 * d4 + d5 * d5;
   }

   public static Axis getBlockAxis(World world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      Axis ax = Axis.Y;
      UnmodifiableIterator var4 = state.func_177228_b().keySet().iterator();

      while(var4.hasNext()) {
         IProperty prop = (IProperty)var4.next();
         if (prop.func_177701_a().equals("axis")) {
            if (state.func_177229_b(prop) instanceof EnumAxis) {
               ax = (EnumAxis)state.func_177229_b(prop) == EnumAxis.X ? Axis.X : ((EnumAxis)state.func_177229_b(prop) == EnumAxis.Y ? Axis.Y : ((EnumAxis)state.func_177229_b(prop) == EnumAxis.Z ? Axis.Z : Axis.Y));
               break;
            }

            if (state.func_177229_b(prop) instanceof Axis) {
               ax = (Axis)state.func_177229_b(prop);
               break;
            }
         }
      }

      if (ax == null) {
         ax = Axis.Y;
      }

      return ax;
   }

   public static boolean hasLOS(World world, BlockPos source, BlockPos target) {
      RayTraceResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(world, new Vec3d((double)source.func_177958_n() + 0.5D, (double)source.func_177956_o() + 0.5D, (double)source.func_177952_p() + 0.5D), new Vec3d((double)target.func_177958_n() + 0.5D, (double)target.func_177956_o() + 0.5D, (double)target.func_177952_p() + 0.5D), false, true, false);
      return mop == null || mop.field_72313_a == Type.BLOCK && mop.func_178782_a().func_177958_n() == target.func_177958_n() && mop.func_178782_a().func_177956_o() == target.func_177956_o() && mop.func_178782_a().func_177952_p() == target.func_177952_p();
   }

   public static ItemStack getSilkTouchDrop(IBlockState bs) {
      ItemStack dropped = null;

      try {
         Method m = ReflectionHelper.findMethod(Block.class, bs.func_177230_c(), new String[]{"getSilkTouchDrop", "func_180643_i", "u"}, new Class[]{IBlockState.class});
         dropped = (ItemStack)m.invoke(bs.func_177230_c(), bs);
      } catch (Exception var3) {
         Thaumcraft.log.warn("Could not invoke net.minecraft.block.Block method getSilkTouchDrop");
      }

      return dropped;
   }

   static {
      lastPos = BlockPos.field_177992_a;
      lasty = 0;
      lastz = 0;
      lastdistance = 0.0D;
      portableHoleBlackList = new ArrayList();
   }
}
