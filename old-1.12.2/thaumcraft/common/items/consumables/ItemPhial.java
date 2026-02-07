package thaumcraft.common.items.consumables;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.items.ItemTCEssentiaContainer;
import thaumcraft.common.tiles.essentia.TileAlembic;
import thaumcraft.common.tiles.essentia.TileJarFillable;

public class ItemPhial extends ItemTCEssentiaContainer {
   public ItemPhial() {
      super("phial", 10, "empty", "filled");
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      Iterator var4 = Aspect.aspects.values().iterator();

      while(var4.hasNext()) {
         Aspect tag = (Aspect)var4.next();
         ItemStack i = new ItemStack(this, 1, 1);
         this.setAspects(i, (new AspectList()).add(tag, this.base));
         par3List.add(i);
      }

   }

   public String func_77667_c(ItemStack stack) {
      return super.func_77658_a() + "." + this.getVariantNames()[stack.func_77952_i()];
   }

   public void func_77663_a(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
      if (!world.field_72995_K && !stack.func_77942_o() && stack.func_77952_i() == 1) {
         stack.func_77964_b(0);
      }

   }

   public void func_77622_d(ItemStack stack, World world, EntityPlayer player) {
      if (!world.field_72995_K && !stack.func_77942_o() && stack.func_77952_i() == 1) {
         stack.func_77964_b(0);
      }

   }

   public EnumActionResult onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float f1, float f2, float f3, EnumHand hand) {
      IBlockState bi = world.func_180495_p(pos);
      if (itemstack.func_77952_i() == 0 && bi.func_177230_c() == BlocksTC.alembic) {
         TileAlembic tile = (TileAlembic)world.func_175625_s(pos);
         if (tile.amount >= this.base) {
            if (world.field_72995_K) {
               player.func_184609_a(hand);
               return EnumActionResult.PASS;
            }

            ItemStack phial = new ItemStack(this, 1, 1);
            this.setAspects(phial, (new AspectList()).add(tile.aspect, this.base));
            if (tile.takeFromContainer(tile.aspect, this.base)) {
               --itemstack.field_77994_a;
               if (!player.field_71071_by.func_70441_a(phial)) {
                  world.func_72838_d(new EntityItem(world, player.field_70165_t, player.field_70163_u, player.field_70161_v, phial));
               }

               player.func_184185_a(SoundEvents.field_187615_H, 0.25F, 1.0F);
               player.field_71069_bz.func_75142_b();
               return EnumActionResult.SUCCESS;
            }
         }
      }

      Aspect aspect;
      if (itemstack.func_77952_i() == 0 && (bi.func_177230_c() == BlocksTC.jarNormal || bi.func_177230_c() == BlocksTC.jarVoid)) {
         TileJarFillable tile = (TileJarFillable)world.func_175625_s(pos);
         if (tile.amount >= this.base) {
            if (world.field_72995_K) {
               player.func_184609_a(hand);
               return EnumActionResult.PASS;
            }

            aspect = Aspect.getAspect(tile.aspect.getTag());
            if (tile.takeFromContainer(aspect, this.base)) {
               --itemstack.field_77994_a;
               ItemStack phial = new ItemStack(this, 1, 1);
               this.setAspects(phial, (new AspectList()).add(aspect, this.base));
               if (!player.field_71071_by.func_70441_a(phial)) {
                  world.func_72838_d(new EntityItem(world, (double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 0.5F), (double)((float)pos.func_177952_p() + 0.5F), phial));
               }

               player.func_184185_a(SoundEvents.field_187615_H, 0.25F, 1.0F);
               player.field_71069_bz.func_75142_b();
               return EnumActionResult.SUCCESS;
            }
         }
      }

      AspectList al = this.getAspects(itemstack);
      if (al != null && al.size() == 1) {
         aspect = al.getAspects()[0];
         if (itemstack.func_77952_i() != 0 && (bi.func_177230_c() == BlocksTC.jarNormal || bi.func_177230_c() == BlocksTC.jarVoid)) {
            TileJarFillable tile = (TileJarFillable)world.func_175625_s(pos);
            if (tile.amount <= tile.maxAmount - this.base && tile.doesContainerAccept(aspect)) {
               if (world.field_72995_K) {
                  player.func_184609_a(hand);
                  return EnumActionResult.PASS;
               }

               if (tile.addToContainer(aspect, this.base) == 0) {
                  world.markAndNotifyBlock(pos, world.func_175726_f(pos), bi, bi, 3);
                  tile.func_70296_d();
                  --itemstack.field_77994_a;
                  if (!player.field_71071_by.func_70441_a(new ItemStack(this, 1, 0))) {
                     world.func_72838_d(new EntityItem(world, (double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 0.5F), (double)((float)pos.func_177952_p() + 0.5F), new ItemStack(this, 1, 0)));
                  }

                  player.func_184185_a(SoundEvents.field_187615_H, 0.25F, 1.0F);
                  player.field_71069_bz.func_75142_b();
                  return EnumActionResult.SUCCESS;
               }
            }
         }
      }

      return EnumActionResult.PASS;
   }
}
