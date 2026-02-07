package thaumcraft.common.blocks.essentia;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.ItemBlockTC;
import thaumcraft.common.tiles.devices.TileJarBrain;
import thaumcraft.common.tiles.essentia.TileAlembic;
import thaumcraft.common.tiles.essentia.TileJarFillable;

public class BlockJarItem extends ItemBlockTC implements IEssentiaContainerItem {
   public BlockJarItem(Block block) {
      super(block);
   }

   public EnumActionResult onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      Block bi = world.func_180495_p(pos).func_177230_c();
      if (bi == BlocksTC.alembic && !world.field_72995_K) {
         TileAlembic tile = (TileAlembic)world.func_175625_s(pos);
         if (tile.amount > 0) {
            if (this.getFilter(itemstack) != null && this.getFilter(itemstack) != tile.aspect) {
               return EnumActionResult.FAIL;
            }

            if (this.getAspects(itemstack) != null && this.getAspects(itemstack).getAspects()[0] != tile.aspect) {
               return EnumActionResult.FAIL;
            }

            int amt = tile.amount;
            if (this.getAspects(itemstack) != null && this.getAspects(itemstack).visSize() + amt > 64) {
               amt = Math.abs(this.getAspects(itemstack).visSize() - 64);
            }

            if (amt <= 0) {
               return EnumActionResult.FAIL;
            }

            Aspect a = tile.aspect;
            if (tile.takeFromContainer(tile.aspect, amt)) {
               int base = this.getAspects(itemstack) == null ? 0 : this.getAspects(itemstack).visSize();
               if (itemstack.field_77994_a > 1) {
                  ItemStack stack = itemstack.func_77946_l();
                  this.setAspects(stack, (new AspectList()).add(a, base + amt));
                  --itemstack.field_77994_a;
                  stack.field_77994_a = 1;
                  if (!player.field_71071_by.func_70441_a(stack)) {
                     world.func_72838_d(new EntityItem(world, player.field_70165_t, player.field_70163_u, player.field_70161_v, stack));
                  }
               } else {
                  this.setAspects(itemstack, (new AspectList()).add(a, base + amt));
               }

               player.func_184185_a(SoundEvents.field_187615_H, 0.25F, 1.0F);
               player.field_71069_bz.func_75142_b();
               return EnumActionResult.SUCCESS;
            }
         }
      }

      return EnumActionResult.PASS;
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
      boolean b = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
      if (b && !world.field_72995_K) {
         TileEntity te = world.func_175625_s(pos);
         if (te != null && te instanceof TileJarFillable) {
            TileJarFillable jar = (TileJarFillable)te;
            jar.setAspects(this.getAspects(stack));
            if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("AspectFilter")) {
               jar.aspectFilter = Aspect.getAspect(stack.func_77978_p().func_74779_i("AspectFilter"));
            }

            te.func_70296_d();
            world.markAndNotifyBlock(pos, world.func_175726_f(pos), newState, newState, 3);
         }

         if (te != null && te instanceof TileJarBrain) {
            TileJarBrain jar = (TileJarBrain)te;
            if (stack.func_77942_o()) {
               jar.xp = stack.func_77978_p().func_74762_e("xp");
            }

            te.func_70296_d();
            world.markAndNotifyBlock(pos, world.func_175726_f(pos), newState, newState, 3);
         }
      }

      return b;
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("AspectFilter")) {
         String tf = stack.func_77978_p().func_74779_i("AspectFilter");
         Aspect tag = Aspect.getAspect(tf);
         list.add("§5" + tag.getName());
      }

      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("xp")) {
         int tf = stack.func_77978_p().func_74762_e("xp");
         list.add("§5" + tf + " xp");
      }

      super.func_77624_a(stack, player, list, par4);
   }

   public AspectList getAspects(ItemStack itemstack) {
      if (itemstack.func_77942_o()) {
         AspectList aspects = new AspectList();
         aspects.readFromNBT(itemstack.func_77978_p());
         return aspects.size() > 0 ? aspects : null;
      } else {
         return null;
      }
   }

   public Aspect getFilter(ItemStack itemstack) {
      return itemstack.func_77942_o() ? Aspect.getAspect(itemstack.func_77978_p().func_74779_i("AspectFilter")) : null;
   }

   public void setAspects(ItemStack itemstack, AspectList aspects) {
      if (!itemstack.func_77942_o()) {
         itemstack.func_77982_d(new NBTTagCompound());
      }

      aspects.writeToNBT(itemstack.func_77978_p());
   }

   public boolean ignoreContainedAspects() {
      return false;
   }
}
