package thaumcraft.common.blocks.basic;

import java.util.List;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.items.consumables.ItemPhial;
import thaumcraft.common.tiles.misc.TileBanner;

public class BlockBannerTC extends BlockTC implements ITileEntityProvider {
   public BlockBannerTC() {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185848_a);
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 1));

      for(int a = 0; a < 16; ++a) {
         ItemStack banner = new ItemStack(par1, 1, 0);
         banner.func_77982_d(new NBTTagCompound());
         banner.func_77978_p().func_74774_a("color", (byte)a);
         par3List.add(banner);
      }

   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      TileEntity tile = source.func_175625_s(pos);
      if (tile != null && tile instanceof TileBanner) {
         if (!((TileBanner)tile).getWall()) {
            return new AxisAlignedBB(0.33000001311302185D, 0.0D, 0.33000001311302185D, 0.6600000262260437D, 2.0D, 0.6600000262260437D);
         }

         switch(((TileBanner)tile).getBannerFacing()) {
         case 0:
            return new AxisAlignedBB(0.0D, -1.0D, 0.0D, 1.0D, 1.0D, 0.25D);
         case 4:
            return new AxisAlignedBB(0.75D, -1.0D, 0.0D, 1.0D, 1.0D, 1.0D);
         case 8:
            return new AxisAlignedBB(0.0D, -1.0D, 0.75D, 1.0D, 1.0D, 1.0D);
         case 12:
            return new AxisAlignedBB(0.0D, -1.0D, 0.0D, 0.25D, 1.0D, 1.0D);
         }
      }

      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   }

   public AxisAlignedBB func_180646_a(IBlockState worldIn, World pos, BlockPos state) {
      return null;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_180639_a(World w, BlockPos pos, IBlockState state, EntityPlayer p, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (p.func_70093_af() || heldItem != null && heldItem.func_77973_b() instanceof ItemPhial) {
         TileBanner te = (TileBanner)w.func_175625_s(pos);
         if (te != null && te.getColor() >= 0) {
            if (p.func_70093_af()) {
               te.setAspect((Aspect)null);
            } else if (((IEssentiaContainerItem)((IEssentiaContainerItem)heldItem.func_77973_b())).getAspects(heldItem) != null) {
               te.setAspect(((IEssentiaContainerItem)((IEssentiaContainerItem)heldItem.func_77973_b())).getAspects(heldItem).getAspects()[0]);
               --heldItem.field_77994_a;
            }

            w.markAndNotifyBlock(pos, w.func_175726_f(pos), state, state, 3);
            te.func_70296_d();
            w.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187550_ag, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }
      }

      return true;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileBanner();
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      TileEntity te = world.func_175625_s(pos);
      if (te instanceof TileBanner) {
         ItemStack drop = new ItemStack(this, 1, ((TileBanner)te).getColor() >= 0 ? 0 : 1);
         if (((TileBanner)te).getColor() >= 0 || ((TileBanner)te).getAspect() != null) {
            drop.func_77982_d(new NBTTagCompound());
            if (((TileBanner)te).getAspect() != null) {
               drop.func_77978_p().func_74778_a("aspect", ((TileBanner)te).getAspect().getTag());
            }

            drop.func_77978_p().func_74774_a("color", ((TileBanner)te).getColor());
         }

         return drop;
      } else {
         return super.getPickBlock(state, target, world, pos, player);
      }
   }

   public void func_180653_a(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (te instanceof TileBanner) {
         ItemStack drop = new ItemStack(this, 1, ((TileBanner)te).getColor() >= 0 ? 0 : 1);
         if (((TileBanner)te).getColor() >= 0 || ((TileBanner)te).getAspect() != null) {
            drop.func_77982_d(new NBTTagCompound());
            if (((TileBanner)te).getAspect() != null) {
               drop.func_77978_p().func_74778_a("aspect", ((TileBanner)te).getAspect().getTag());
            }

            drop.func_77978_p().func_74774_a("color", ((TileBanner)te).getColor());
         }

         func_180635_a(worldIn, pos, drop);
      } else {
         super.func_180653_a(worldIn, pos, state, chance, fortune);
      }

   }

   public void func_180657_a(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
      if (te instanceof TileBanner) {
         ItemStack drop = new ItemStack(this, 1, ((TileBanner)te).getColor() >= 0 ? 0 : 1);
         if (((TileBanner)te).getColor() >= 0 || ((TileBanner)te).getAspect() != null) {
            drop.func_77982_d(new NBTTagCompound());
            if (((TileBanner)te).getAspect() != null) {
               drop.func_77978_p().func_74778_a("aspect", ((TileBanner)te).getAspect().getTag());
            }

            drop.func_77978_p().func_74774_a("color", ((TileBanner)te).getColor());
         }

         func_180635_a(worldIn, pos, drop);
      } else {
         super.func_180657_a(worldIn, player, pos, state, (TileEntity)null, stack);
      }

   }
}
