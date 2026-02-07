package thaumcraft.common.container;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.common.tiles.crafting.TileThaumatorium;

public class ContainerThaumatorium extends Container {
   private TileThaumatorium thaumatorium;
   private EntityPlayer player = null;
   public ArrayList<CrucibleRecipe> recipes = new ArrayList();

   public ContainerThaumatorium(InventoryPlayer par1InventoryPlayer, TileThaumatorium tileEntity) {
      this.player = par1InventoryPlayer.field_70458_d;
      this.thaumatorium = tileEntity;
      this.thaumatorium.eventHandler = this;
      this.func_75146_a(new Slot(tileEntity, 0, 48, 16));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
      }

      this.func_75130_a(this.thaumatorium);
   }

   public void func_75130_a(IInventory par1iInventory) {
      super.func_75130_a(par1iInventory);
      this.updateRecipes();
   }

   public void func_75134_a(EntityPlayer par1EntityPlayer) {
      super.func_75134_a(par1EntityPlayer);
      if (!this.thaumatorium.func_145831_w().field_72995_K) {
         this.thaumatorium.eventHandler = null;
      }

   }

   public void updateRecipes() {
      this.recipes.clear();
      if (this.thaumatorium.inputStack != null || this.thaumatorium.recipeHash != null) {
         Iterator var1 = ThaumcraftApi.getCraftingRecipes().values().iterator();

         while(true) {
            Object r;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               r = var1.next();
            } while(!(r instanceof CrucibleRecipe[]));

            CrucibleRecipe[] creps = (CrucibleRecipe[])((CrucibleRecipe[])r);
            CrucibleRecipe[] var4 = creps;
            int var5 = creps.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               CrucibleRecipe cr = var4[var6];
               if (ThaumcraftCapabilities.knowsResearchStrict(this.player, cr.research) && cr.catalystMatches(this.thaumatorium.inputStack)) {
                  this.recipes.add(cr);
               } else if (this.thaumatorium.recipeHash != null && this.thaumatorium.recipeHash.size() > 0) {
                  Iterator var8 = this.thaumatorium.recipeHash.iterator();

                  while(var8.hasNext()) {
                     Integer hash = (Integer)var8.next();
                     if (cr.hash == hash) {
                        this.recipes.add(cr);
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public boolean func_75140_a(EntityPlayer par1EntityPlayer, int button) {
      if (this.recipes.size() > 0 && button >= 0 && button < this.recipes.size()) {
         boolean found = false;

         for(int a = 0; a < this.thaumatorium.recipeHash.size(); ++a) {
            if (((CrucibleRecipe)this.recipes.get(button)).hash == (Integer)this.thaumatorium.recipeHash.get(a)) {
               found = true;
               this.thaumatorium.recipeEssentia.remove(a);
               this.thaumatorium.recipePlayer.remove(a);
               this.thaumatorium.recipeHash.remove(a);
               this.thaumatorium.currentCraft = -1;
               break;
            }
         }

         if (!found) {
            this.thaumatorium.recipeEssentia.add(((CrucibleRecipe)this.recipes.get(button)).aspects.copy());
            this.thaumatorium.recipePlayer.add(par1EntityPlayer.func_70005_c_());
            this.thaumatorium.recipeHash.add(((CrucibleRecipe)this.recipes.get(button)).hash);
         }

         this.thaumatorium.func_70296_d();
         this.thaumatorium.syncTile(false);
         return true;
      } else {
         return false;
      }
   }

   public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
      return this.thaumatorium.func_70300_a(par1EntityPlayer);
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(par2);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         if (par2 != 0) {
            if (!this.func_75135_a(itemstack1, 0, 1, false)) {
               return null;
            }
         } else if (par2 >= 1 && par2 < 28) {
            if (!this.func_75135_a(itemstack1, 28, 37, false)) {
               return null;
            }
         } else {
            if (par2 >= 28 && par2 < 37 && !this.func_75135_a(itemstack1, 1, 28, false)) {
               return null;
            }

            if (!this.func_75135_a(itemstack1, 1, 37, false)) {
               return null;
            }
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
