package thaumcraft.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

public class BlockTCTile extends BlockTC implements ITileEntityProvider {
   protected final Class tileClass;
   protected static boolean keepInventory = false;
   protected static boolean spillEssentia = true;

   public BlockTCTile(Material mat, Class tc) {
      this(mat, tc, Object.class);
   }

   public BlockTCTile(Material mat, Class tc, Class types) {
      super(mat, types);
      this.func_149711_c(2.0F);
      this.func_149752_b(20.0F);
      this.tileClass = tc;
      this.field_149758_A = true;
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      if (this.tileClass == null) {
         return null;
      } else {
         try {
            return (TileEntity)this.tileClass.newInstance();
         } catch (InstantiationException var4) {
            Thaumcraft.log.catching(var4);
         } catch (IllegalAccessException var5) {
            Thaumcraft.log.catching(var5);
         }

         return null;
      }
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity tileentity = worldIn.func_175625_s(pos);
      if (tileentity != null && tileentity instanceof IInventory && !keepInventory) {
         InventoryUtils.dropItems(worldIn, pos);
      }

      if (tileentity != null && tileentity instanceof IEssentiaTransport && spillEssentia && !worldIn.field_72995_K) {
         int ess = ((IEssentiaTransport)tileentity).getEssentiaAmount(EnumFacing.UP);
         if (ess > 0) {
            AuraHelper.polluteAura(worldIn, pos, (float)ess, true);
         }
      }

      super.func_180663_b(worldIn, pos, state);
      worldIn.func_175713_t(pos);
   }

   public boolean func_189539_a(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
      super.func_189539_a(state, worldIn, pos, id, param);
      TileEntity tileentity = worldIn.func_175625_s(pos);
      return tileentity == null ? false : tileentity.func_145842_c(id, param);
   }
}
