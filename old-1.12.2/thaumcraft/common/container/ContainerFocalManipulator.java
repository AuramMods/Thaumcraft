package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.container.slot.SlotFocus;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;

public class ContainerFocalManipulator extends Container {
   private TileFocalManipulator table;
   private int lastBreakTime;

   public ContainerFocalManipulator(InventoryPlayer par1InventoryPlayer, TileFocalManipulator tileEntity) {
      this.table = tileEntity;
      this.func_75146_a(new SlotFocus(tileEntity, 0, 128, 104));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(par1InventoryPlayer, j + i * 9 + 9, 16 + j * 18, 151 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(par1InventoryPlayer, i, 16 + i * 18, 209));
      }

   }

   public boolean func_75140_a(EntityPlayer p, int button) {
      if (button >= 0 && !this.table.startCraft(button, p)) {
         this.table.func_145831_w().func_184133_a(p, this.table.func_174877_v(), SoundsTC.craftfail, SoundCategory.BLOCKS, 0.33F, 1.0F);
      }

      return false;
   }

   public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
      return this.table.func_70300_a(par1EntityPlayer);
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(par2);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         if (par2 != 0) {
            if (itemstack1.func_77973_b() == ItemsTC.focusBlank) {
               if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                  return null;
               }
            } else if (par2 >= 1 && par2 < 28) {
               if (!this.func_75135_a(itemstack1, 28, 37, false)) {
                  return null;
               }
            } else if (par2 >= 28 && par2 < 37 && !this.func_75135_a(itemstack1, 1, 28, false)) {
               return null;
            }
         } else if (!this.func_75135_a(itemstack1, 1, 37, false)) {
            return null;
         }

         if (itemstack1.field_77994_a == 0) {
            slot.func_75215_d((ItemStack)null);
         } else {
            slot.func_75218_e();
         }

         if (itemstack1.field_77994_a == itemstack.field_77994_a) {
            return null;
         }

         slot.func_82870_a(par1EntityPlayer, itemstack1);
      }

      return itemstack;
   }
}
