package thaumcraft.common.container;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.items.IScribeTools;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.common.container.slot.SlotLimitedByClass;
import thaumcraft.common.container.slot.SlotLimitedByItemstack;
import thaumcraft.common.tiles.crafting.TileResearchTable;

public class ContainerResearchTable extends Container {
   public TileResearchTable tileEntity;
   String[] aspects;
   EntityPlayer player;

   public ContainerResearchTable(InventoryPlayer iinventory, TileResearchTable iinventory1) {
      this.player = iinventory.field_70458_d;
      this.tileEntity = iinventory1;
      this.aspects = (String[])Aspect.aspects.keySet().toArray(new String[0]);
      this.func_75146_a(new SlotLimitedByClass(IScribeTools.class, iinventory1, 0, 16, 15));
      this.func_75146_a(new SlotLimitedByItemstack(new ItemStack(Items.field_151121_aF), iinventory1, 1, 224, 16));
      this.bindPlayerInventory(iinventory);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      int i;
      int j;
      for(i = 0; i < 3; ++i) {
         for(j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 77 + j * 18, 190 + i * 18));
         }
      }

      for(i = 0; i < 3; ++i) {
         for(j = 0; j < 3; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, i + j * 3, 20 + i * 18, 190 + j * 18));
         }
      }

   }

   public boolean func_75140_a(EntityPlayer par1EntityPlayer, int button) {
      if (button == 1) {
         if (this.tileEntity.data.lastDraw != null) {
            this.tileEntity.data.savedCards.add(this.tileEntity.data.lastDraw.card.getSeed());
         }

         Iterator var3 = this.tileEntity.data.cardChoices.iterator();

         while(var3.hasNext()) {
            ResearchTableData.CardChoice cc = (ResearchTableData.CardChoice)var3.next();
            if (cc.selected) {
               this.tileEntity.data.lastDraw = cc;
               break;
            }
         }

         this.tileEntity.data.cardChoices.clear();
         this.tileEntity.syncTile(false);
         return true;
      } else if ((button == 4 || button == 5 || button == 6) && ((ResearchTableData.CardChoice)this.tileEntity.data.cardChoices.get(button - 4)).card.activate(par1EntityPlayer, this.tileEntity.data)) {
         this.tileEntity.consumeInkFromTable();
         ((ResearchTableData.CardChoice)this.tileEntity.data.cardChoices.get(button - 4)).selected = true;
         this.tileEntity.data.addInspiration(-((ResearchTableData.CardChoice)this.tileEntity.data.cardChoices.get(button - 4)).card.getInspirationCost());
         this.tileEntity.syncTile(false);
         return true;
      } else if (button == 7 && this.tileEntity.data.isComplete()) {
         this.tileEntity.finishTheory(par1EntityPlayer);
         this.tileEntity.syncTile(false);
         return true;
      } else if (button != 2 && button != 3) {
         return false;
      } else {
         if (this.tileEntity.data != null && !this.tileEntity.data.isComplete() && this.tileEntity.consumepaperFromTable()) {
            this.tileEntity.data.drawCards(button, par1EntityPlayer);
            this.tileEntity.syncTile(false);
         }

         return true;
      }
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.field_75151_b.get(slot);
      if (slotObject != null && slotObject.func_75216_d()) {
         ItemStack stackInSlot = slotObject.func_75211_c();
         stack = stackInSlot.func_77946_l();
         if (slot < 2) {
            if (!this.func_75135_a(stackInSlot, 2, this.field_75151_b.size(), true)) {
               return null;
            }
         } else if (!this.func_75135_a(stackInSlot, 0, 2, false)) {
            return null;
         }

         if (stackInSlot.field_77994_a == 0) {
            slotObject.func_75215_d((ItemStack)null);
         } else {
            slotObject.func_75218_e();
         }
      }

      return stack;
   }

   protected boolean func_75135_a(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
      boolean var5 = false;
      int var6 = par2;
      if (par4) {
         var6 = par3 - 1;
      }

      Slot var7;
      ItemStack var8;
      if (par1ItemStack.func_77985_e()) {
         while(par1ItemStack.field_77994_a > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
            var7 = (Slot)this.field_75151_b.get(var6);
            var8 = var7.func_75211_c();
            if (var8 != null && var7.func_75214_a(par1ItemStack) && var8.func_77973_b() == par1ItemStack.func_77973_b() && (!par1ItemStack.func_77981_g() || par1ItemStack.func_77952_i() == var8.func_77952_i()) && ItemStack.func_77970_a(par1ItemStack, var8)) {
               int var9 = var8.field_77994_a + par1ItemStack.field_77994_a;
               if (var9 <= par1ItemStack.func_77976_d()) {
                  par1ItemStack.field_77994_a = 0;
                  var8.field_77994_a = var9;
                  var7.func_75218_e();
                  var5 = true;
               } else if (var8.field_77994_a < par1ItemStack.func_77976_d()) {
                  par1ItemStack.field_77994_a -= par1ItemStack.func_77976_d() - var8.field_77994_a;
                  var8.field_77994_a = par1ItemStack.func_77976_d();
                  var7.func_75218_e();
                  var5 = true;
               }
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if (par1ItemStack.field_77994_a > 0) {
         if (par4) {
            var6 = par3 - 1;
         } else {
            var6 = par2;
         }

         while(!par4 && var6 < par3 || par4 && var6 >= par2) {
            var7 = (Slot)this.field_75151_b.get(var6);
            var8 = var7.func_75211_c();
            if (var8 == null && var7.func_75214_a(par1ItemStack)) {
               var7.func_75215_d(par1ItemStack.func_77946_l());
               var7.func_75218_e();
               par1ItemStack.field_77994_a = 0;
               var5 = true;
               break;
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }

   public boolean func_75145_c(EntityPlayer player) {
      return this.tileEntity.func_70300_a(player);
   }
}
