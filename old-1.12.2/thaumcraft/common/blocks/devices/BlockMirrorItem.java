package thaumcraft.common.blocks.devices;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.devices.TileMirror;
import thaumcraft.common.tiles.devices.TileMirrorEssentia;

public class BlockMirrorItem extends ItemBlock {
   public BlockMirrorItem(Block par1) {
      super(par1);
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (world.func_180495_p(pos).func_177230_c() instanceof BlockMirror) {
         if (world.field_72995_K) {
            player.func_184609_a(hand);
            return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
         }

         TileEntity tm;
         ItemStack st;
         if (this.field_150939_a == BlocksTC.mirror) {
            tm = world.func_175625_s(pos);
            if (tm != null && tm instanceof TileMirror && !((TileMirror)tm).isLinkValid()) {
               st = stack.func_77946_l();
               st.field_77994_a = 1;
               st.func_77964_b(1);
               st.func_77983_a("linkX", new NBTTagInt(tm.func_174877_v().func_177958_n()));
               st.func_77983_a("linkY", new NBTTagInt(tm.func_174877_v().func_177956_o()));
               st.func_77983_a("linkZ", new NBTTagInt(tm.func_174877_v().func_177952_p()));
               st.func_77983_a("linkDim", new NBTTagInt(world.field_73011_w.getDimension()));
               world.func_184133_a((EntityPlayer)null, pos, SoundsTC.jar, SoundCategory.BLOCKS, 1.0F, 2.0F);
               if (!player.field_71071_by.func_70441_a(st) && !world.field_72995_K) {
                  world.func_72838_d(new EntityItem(world, player.field_70165_t, player.field_70163_u, player.field_70161_v, st));
               }

               if (!player.field_71075_bZ.field_75098_d) {
                  --stack.field_77994_a;
               }

               player.field_71069_bz.func_75142_b();
            } else if (tm != null && tm instanceof TileMirror) {
               player.func_145747_a(new TextComponentTranslation("§5§oThat mirror is already linked to a valid destination.", new Object[0]));
            }
         } else {
            tm = world.func_175625_s(pos);
            if (tm != null && tm instanceof TileMirrorEssentia && !((TileMirrorEssentia)tm).isLinkValid()) {
               st = stack.func_77946_l();
               st.field_77994_a = 1;
               st.func_77964_b(1);
               st.func_77983_a("linkX", new NBTTagInt(tm.func_174877_v().func_177958_n()));
               st.func_77983_a("linkY", new NBTTagInt(tm.func_174877_v().func_177956_o()));
               st.func_77983_a("linkZ", new NBTTagInt(tm.func_174877_v().func_177952_p()));
               st.func_77983_a("linkDim", new NBTTagInt(world.field_73011_w.getDimension()));
               world.func_184133_a((EntityPlayer)null, pos, SoundsTC.jar, SoundCategory.BLOCKS, 1.0F, 2.0F);
               if (!player.field_71071_by.func_70441_a(st) && !world.field_72995_K) {
                  world.func_72838_d(new EntityItem(world, player.field_70165_t, player.field_70163_u, player.field_70161_v, st));
               }

               if (!player.field_71075_bZ.field_75098_d) {
                  --stack.field_77994_a;
               }

               player.field_71069_bz.func_75142_b();
            } else if (tm != null && tm instanceof TileMirrorEssentia) {
               player.func_145747_a(new TextComponentTranslation("§5§oThat mirror is already linked to a valid destination.", new Object[0]));
            }
         }
      }

      return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
      boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
      if (ret && !world.field_72995_K) {
         TileEntity te;
         if (this.field_150939_a == BlocksTC.mirror) {
            te = world.func_175625_s(pos);
            if (te != null && te instanceof TileMirror && stack.func_77942_o()) {
               ((TileMirror)te).linkX = stack.func_77978_p().func_74762_e("linkX");
               ((TileMirror)te).linkY = stack.func_77978_p().func_74762_e("linkY");
               ((TileMirror)te).linkZ = stack.func_77978_p().func_74762_e("linkZ");
               ((TileMirror)te).linkDim = stack.func_77978_p().func_74762_e("linkDim");
               ((TileMirror)te).restoreLink();
            }
         } else {
            te = world.func_175625_s(pos);
            if (te != null && te instanceof TileMirrorEssentia && stack.func_77942_o()) {
               ((TileMirrorEssentia)te).linkX = stack.func_77978_p().func_74762_e("linkX");
               ((TileMirrorEssentia)te).linkY = stack.func_77978_p().func_74762_e("linkY");
               ((TileMirrorEssentia)te).linkZ = stack.func_77978_p().func_74762_e("linkZ");
               ((TileMirrorEssentia)te).linkDim = stack.func_77978_p().func_74762_e("linkDim");
               ((TileMirrorEssentia)te).restoreLink();
            }
         }
      }

      return ret;
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      if (item.func_77942_o()) {
         int lx = item.func_77978_p().func_74762_e("linkX");
         int ly = item.func_77978_p().func_74762_e("linkY");
         int lz = item.func_77978_p().func_74762_e("linkZ");
         int ldim = item.func_77978_p().func_74762_e("linkDim");
         String desc = "" + ldim;
         World world = DimensionManager.getWorld(ldim);
         if (world != null) {
            desc = world.field_73011_w.func_186058_p().func_186065_b();
         }

         list.add("Linked to " + lx + "," + ly + "," + lz + " in " + desc);
      }

   }
}
