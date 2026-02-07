package thaumcraft.common.items.tools;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IArchitect;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.utils.InventoryUtils;

public class ItemElementalShovel extends ItemSpade implements IArchitect, IThaumcraftItems {
   private static final Block[] isEffective;
   EnumFacing side;

   public ItemElementalShovel(ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.side = EnumFacing.DOWN;
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("elemental_shovel");
      this.func_77655_b("elemental_shovel");
      ConfigItems.ITEM_VARIANT_HOLDERS.add(this);
   }

   public Item getItem() {
      return this;
   }

   public String[] getVariantNames() {
      return new String[]{"normal"};
   }

   public int[] getVariantMeta() {
      return new int[]{0};
   }

   public ItemMeshDefinition getCustomMesh() {
      return null;
   }

   public ModelResourceLocation getCustomModelResourceLocation(String variant) {
      return new ModelResourceLocation("thaumcraft:" + variant);
   }

   public Set<String> getToolClasses(ItemStack stack) {
      return ImmutableSet.of("shovel");
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public EnumActionResult func_180614_a(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
      IBlockState bs = world.func_180495_p(pos);
      TileEntity te = world.func_175625_s(pos);
      if (te == null) {
         for(int aa = -1; aa <= 1; ++aa) {
            for(int bb = -1; bb <= 1; ++bb) {
               int xx = 0;
               int yy = 0;
               int zz = 0;
               byte o = getOrientation(itemstack);
               int l;
               if (o == 1) {
                  yy = bb;
                  if (side.ordinal() <= 1) {
                     l = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
                     if (l != 0 && l != 2) {
                        zz = aa;
                     } else {
                        xx = aa;
                     }
                  } else if (side.ordinal() <= 3) {
                     zz = aa;
                  } else {
                     xx = aa;
                  }
               } else if (o == 2) {
                  if (side.ordinal() <= 1) {
                     l = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
                     yy = bb;
                     if (l != 0 && l != 2) {
                        zz = aa;
                     } else {
                        xx = aa;
                     }
                  } else {
                     zz = bb;
                     xx = aa;
                  }
               } else if (side.ordinal() <= 1) {
                  xx = aa;
                  zz = bb;
               } else if (side.ordinal() <= 3) {
                  xx = aa;
                  yy = bb;
               } else {
                  zz = aa;
                  yy = bb;
               }

               BlockPos p2 = pos.func_177972_a(side).func_177982_a(xx, yy, zz);
               IBlockState b2 = world.func_180495_p(p2);
               if (world.func_175623_d(p2) || b2 == Blocks.field_150395_bd || b2 == Blocks.field_150329_H || b2.func_185904_a() == Material.field_151586_h || b2 == Blocks.field_150330_I || b2.func_177230_c().func_176200_f(world, p2)) {
                  if (!player.field_71075_bZ.field_75098_d && !InventoryUtils.consumeInventoryItem(player, Item.func_150898_a(bs.func_177230_c()), bs.func_177230_c().func_176201_c(bs))) {
                     if (bs.func_177230_c() == Blocks.field_150349_c && (player.field_71075_bZ.field_75098_d || InventoryUtils.consumeInventoryItem(player, Item.func_150898_a(Blocks.field_150346_d), 0))) {
                        world.func_184134_a((double)p2.func_177958_n(), (double)p2.func_177956_o(), (double)p2.func_177952_p(), bs.func_177230_c().func_185467_w().func_185845_c(), SoundCategory.BLOCKS, 0.6F, 0.9F + world.field_73012_v.nextFloat() * 0.2F, false);
                        world.func_175656_a(p2, Blocks.field_150346_d.func_176223_P());
                        itemstack.func_77972_a(1, player);
                        if (world.field_72995_K) {
                           FXDispatcher.INSTANCE.drawBamf(p2, 8401408, false, false, side);
                        }

                        player.func_184609_a(hand);
                     }
                  } else {
                     world.func_184134_a((double)p2.func_177958_n(), (double)p2.func_177956_o(), (double)p2.func_177952_p(), bs.func_177230_c().func_185467_w().func_185845_c(), SoundCategory.BLOCKS, 0.6F, 0.9F + world.field_73012_v.nextFloat() * 0.2F, false);
                     world.func_175656_a(p2, bs);
                     itemstack.func_77972_a(1, player);
                     if (world.field_72995_K) {
                        FXDispatcher.INSTANCE.drawBamf(p2, 8401408, false, false, side);
                     }

                     player.func_184609_a(hand);
                  }
               }
            }
         }
      }

      return EnumActionResult.FAIL;
   }

   private boolean isEffectiveAgainst(Block block) {
      for(int var3 = 0; var3 < isEffective.length; ++var3) {
         if (isEffective[var3] == block) {
            return true;
         }
      }

      return false;
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      ItemStack w1 = new ItemStack(this);
      EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.DESTRUCTIVE, 1);
      par3List.add(w1);
   }

   public ArrayList<BlockPos> getArchitectBlocks(ItemStack focusstack, World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
      ArrayList<BlockPos> b = new ArrayList();
      if (!player.func_70093_af()) {
         return b;
      } else {
         for(int aa = -1; aa <= 1; ++aa) {
            for(int bb = -1; bb <= 1; ++bb) {
               int xx = 0;
               int yy = 0;
               int zz = 0;
               byte o = getOrientation(focusstack);
               int l;
               if (o == 1) {
                  yy = bb;
                  if (side.ordinal() <= 1) {
                     l = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
                     if (l != 0 && l != 2) {
                        zz = aa;
                     } else {
                        xx = aa;
                     }
                  } else if (side.ordinal() <= 3) {
                     zz = aa;
                  } else {
                     xx = aa;
                  }
               } else if (o == 2) {
                  if (side.ordinal() <= 1) {
                     l = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
                     yy = bb;
                     if (l != 0 && l != 2) {
                        zz = aa;
                     } else {
                        xx = aa;
                     }
                  } else {
                     zz = bb;
                     xx = aa;
                  }
               } else if (side.ordinal() <= 1) {
                  xx = aa;
                  zz = bb;
               } else if (side.ordinal() <= 3) {
                  xx = aa;
                  yy = bb;
               } else {
                  zz = aa;
                  yy = bb;
               }

               BlockPos p2 = pos.func_177972_a(side).func_177982_a(xx, yy, zz);
               IBlockState b2 = world.func_180495_p(p2);
               if (world.func_175623_d(p2) || b2 == Blocks.field_150395_bd || b2 == Blocks.field_150329_H || b2.func_185904_a() == Material.field_151586_h || b2 == Blocks.field_150330_I || b2.func_177230_c().func_176200_f(world, p2)) {
                  b.add(p2);
               }
            }
         }

         return b;
      }
   }

   public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing side, IArchitect.EnumAxis axis) {
      return false;
   }

   public static byte getOrientation(ItemStack stack) {
      return stack.func_77942_o() && stack.func_77978_p().func_74764_b("or") ? stack.func_77978_p().func_74771_c("or") : 0;
   }

   public static void setOrientation(ItemStack stack, byte o) {
      if (!stack.func_77942_o()) {
         stack.func_77982_d(new NBTTagCompound());
      }

      if (stack.func_77942_o()) {
         stack.func_77978_p().func_74774_a("or", (byte)(o % 3));
      }

   }

   static {
      isEffective = new Block[]{Blocks.field_150349_c, Blocks.field_150346_d, Blocks.field_150354_m, Blocks.field_150351_n, Blocks.field_150431_aC, Blocks.field_150433_aE, Blocks.field_150435_aG, Blocks.field_150458_ak, Blocks.field_150425_aM, Blocks.field_150391_bh};
   }
}
