package thaumcraft.common.entities.construct.golem.gui;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealConfigFilter;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.ISealGui;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.container.slot.SlotGhost;

public class SealBaseContainer extends Container {
   private World world;
   ISealEntity seal = null;
   EntityPlayer player = null;
   InventoryFake temp;
   int[] categories;
   int category = -1;
   InventoryPlayer pinv;
   int t = 0;
   private byte lastPriority;
   private byte lastColor;
   private int lastAreaX;
   private int lastAreaY;
   private int lastAreaZ;

   public SealBaseContainer(InventoryPlayer iinventory, World par2World, ISealEntity seal) {
      this.world = par2World;
      this.player = iinventory.field_70458_d;
      this.pinv = iinventory;
      this.seal = seal;
      if (seal.getSeal() instanceof ISealGui) {
         this.categories = ((ISealGui)seal.getSeal()).getGuiCategories();
      } else {
         this.categories = new int[]{0};
      }

      this.setupCategories();
   }

   void setupCategories() {
      this.field_75153_a = Lists.newArrayList();
      this.field_75151_b = Lists.newArrayList();
      this.t = 0;
      if (this.category < 0) {
         this.category = this.categories[0];
      }

      switch(this.category) {
      case 1:
         this.setupFilterInventory();
      default:
         this.bindPlayerInventory(this.pinv);
      }
   }

   private void setupFilterInventory() {
      if (this.seal.getSeal() instanceof ISealConfigFilter) {
         int s = ((ISealConfigFilter)this.seal.getSeal()).getFilterSize();
         int sx = 16 + (s - 1) % 3 * 12;
         int sy = 16 + (s - 1) / 3 * 12;
         int middleX = 88;
         int middleY = 72;
         this.temp = new InventoryFake(((ISealConfigFilter)this.seal.getSeal()).getInv());

         for(int a = 0; a < s; ++a) {
            int x = a % 3;
            int y = a / 3;
            this.func_75146_a(new SlotGhost(this.temp, a, middleX + x * 24 - sx + 8, middleY + y * 24 - sy + 8));
            ++this.t;
         }
      }

   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 150 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 8 + i * 18, 208));
      }

   }

   public boolean func_75145_c(EntityPlayer var1) {
      return true;
   }

   public boolean func_75140_a(EntityPlayer player, int par2) {
      if (par2 >= 0 && par2 < this.categories.length) {
         this.category = this.categories[par2];
         this.setupCategories();
         return true;
      } else {
         ISealConfigToggles cp;
         if (this.category == 3 && this.seal.getSeal() instanceof ISealConfigToggles && par2 >= 30 && par2 < 30 + ((ISealConfigToggles)this.seal.getSeal()).getToggles().length) {
            cp = (ISealConfigToggles)this.seal.getSeal();
            cp.setToggle(par2 - 30, true);
            return true;
         } else if (this.category == 3 && this.seal.getSeal() instanceof ISealConfigToggles && par2 >= 60 && par2 < 60 + ((ISealConfigToggles)this.seal.getSeal()).getToggles().length) {
            cp = (ISealConfigToggles)this.seal.getSeal();
            cp.setToggle(par2 - 60, false);
            return true;
         } else if (this.category == 0 && par2 >= 25 && par2 <= 26) {
            this.seal.setLocked(par2 == 25);
            return true;
         } else if (par2 >= 27 && par2 <= 28) {
            this.seal.setRedstoneSensitive(par2 == 27);
            return true;
         } else if (this.category == 1 && this.seal.getSeal() instanceof ISealConfigFilter && par2 >= 20 && par2 <= 21) {
            ISealConfigFilter cp = (ISealConfigFilter)this.seal.getSeal();
            cp.setBlacklist(par2 == 20);
            return true;
         } else if (par2 == 80 && this.seal.getPriority() > -5) {
            this.seal.setPriority((byte)(this.seal.getPriority() - 1));
            return true;
         } else if (par2 == 81 && this.seal.getPriority() < 5) {
            this.seal.setPriority((byte)(this.seal.getPriority() + 1));
            return true;
         } else if (par2 == 82 && this.seal.getColor() > 0) {
            this.seal.setColor((byte)(this.seal.getColor() - 1));
            return true;
         } else if (par2 == 83 && this.seal.getColor() < 16) {
            this.seal.setColor((byte)(this.seal.getColor() + 1));
            return true;
         } else {
            if (this.seal.getSeal() instanceof ISealConfigArea) {
               if (par2 == 90 && this.seal.getArea().func_177956_o() > 1) {
                  this.seal.setArea(this.seal.getArea().func_177982_a(0, -1, 0));
                  return true;
               }

               if (par2 == 91 && this.seal.getArea().func_177956_o() < 8) {
                  this.seal.setArea(this.seal.getArea().func_177982_a(0, 1, 0));
                  return true;
               }

               if (par2 == 92 && this.seal.getArea().func_177958_n() > 1) {
                  this.seal.setArea(this.seal.getArea().func_177982_a(-1, 0, 0));
                  return true;
               }

               if (par2 == 93 && this.seal.getArea().func_177958_n() < 8) {
                  this.seal.setArea(this.seal.getArea().func_177982_a(1, 0, 0));
                  return true;
               }

               if (par2 == 94 && this.seal.getArea().func_177952_p() > 1) {
                  this.seal.setArea(this.seal.getArea().func_177982_a(0, 0, -1));
                  return true;
               }

               if (par2 == 95 && this.seal.getArea().func_177952_p() < 8) {
                  this.seal.setArea(this.seal.getArea().func_177982_a(0, 0, 1));
                  return true;
               }
            }

            return super.func_75140_a(player, par2);
         }
      }
   }

   public void func_75132_a(IContainerListener crafting) {
      super.func_75132_a(crafting);
      crafting.func_71112_a(this, 0, this.seal.getPriority());
      crafting.func_71112_a(this, 4, this.seal.getColor());
   }

   public void func_75142_b() {
      super.func_75142_b();

      for(int i = 0; i < this.field_75149_d.size(); ++i) {
         IContainerListener icrafting = (IContainerListener)this.field_75149_d.get(i);
         if (this.lastPriority != this.seal.getPriority()) {
            icrafting.func_71112_a(this, 0, this.seal.getPriority());
         }

         if (this.lastAreaX != this.seal.getArea().func_177958_n()) {
            icrafting.func_71112_a(this, 1, this.seal.getArea().func_177958_n());
         }

         if (this.lastAreaY != this.seal.getArea().func_177956_o()) {
            icrafting.func_71112_a(this, 2, this.seal.getArea().func_177956_o());
         }

         if (this.lastAreaZ != this.seal.getArea().func_177952_p()) {
            icrafting.func_71112_a(this, 3, this.seal.getArea().func_177952_p());
         }

         if (this.lastColor != this.seal.getColor()) {
            icrafting.func_71112_a(this, 4, this.seal.getColor());
         }
      }

      this.lastPriority = this.seal.getPriority();
      this.lastColor = this.seal.getColor();
      this.lastAreaX = this.seal.getArea().func_177958_n();
      this.lastAreaY = this.seal.getArea().func_177956_o();
      this.lastAreaZ = this.seal.getArea().func_177952_p();
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int par1, int par2) {
      if (par1 == 0) {
         this.seal.setPriority((byte)par2);
      }

      if (par1 == 1) {
         this.seal.setArea(new BlockPos(par2, this.seal.getArea().func_177956_o(), this.seal.getArea().func_177952_p()));
      }

      if (par1 == 2) {
         this.seal.setArea(new BlockPos(this.seal.getArea().func_177958_n(), par2, this.seal.getArea().func_177952_p()));
      }

      if (par1 == 3) {
         this.seal.setArea(new BlockPos(this.seal.getArea().func_177958_n(), this.seal.getArea().func_177956_o(), par2));
      }

      if (par1 == 4) {
         this.seal.setColor((byte)par2);
      }

   }

   public ItemStack func_184996_a(int slotId, int clickedButton, ClickType mode, EntityPlayer playerIn) {
      if (slotId >= 0) {
         Slot slot = (Slot)this.field_75151_b.get(slotId);
         InventoryPlayer inventoryplayer = playerIn.field_71071_by;
         ItemStack ic = null;
         if (inventoryplayer.func_70445_o() != null) {
            ic = inventoryplayer.func_70445_o().func_77946_l();
         }

         if (slot != null && slot instanceof SlotGhost) {
            boolean filter = ((ISealConfigFilter)this.seal.getSeal()).hasStacksizeLimiters();
            if (!playerIn.field_70170_p.field_72995_K) {
               if (clickedButton == 1) {
                  if (!filter) {
                     slot.func_75215_d((ItemStack)null);
                  } else if (ic == null) {
                     if (mode == ClickType.QUICK_MOVE) {
                        slot.func_75215_d((ItemStack)null);
                     } else if (slot.func_75216_d()) {
                        --slot.func_75211_c().field_77994_a;
                        if (slot.func_75211_c().field_77994_a < 0) {
                           slot.func_75215_d((ItemStack)null);
                        }
                     }
                  } else if (slot.func_75216_d() && slot.func_75211_c().field_77994_a == 0) {
                     slot.func_75215_d((ItemStack)null);
                  } else if (slot.func_75216_d() && ItemStack.func_179545_c(ic, slot.func_75211_c()) && ItemStack.func_77970_a(ic, slot.func_75211_c())) {
                     ItemStack var10000 = slot.func_75211_c();
                     var10000.field_77994_a -= ic.field_77994_a;
                  }
               } else if (ic == null) {
                  if (filter && slot.func_75216_d()) {
                     ++slot.func_75211_c().field_77994_a;
                  }
               } else {
                  if (!filter) {
                     ic.field_77994_a = 1;
                  } else if (slot.func_75216_d() && ItemStack.func_179545_c(ic, slot.func_75211_c()) && ItemStack.func_77970_a(ic, slot.func_75211_c())) {
                     ic.field_77994_a += slot.func_75211_c().field_77994_a;
                  } else {
                     ic.field_77994_a = 0;
                  }

                  slot.func_75215_d(ic);
               }

               if (slot.func_75216_d() && slot.func_75211_c().field_77994_a < 0) {
                  slot.func_75211_c().field_77994_a = 0;
               }

               this.func_75142_b();
            }

            return null;
         }
      }

      return super.func_184996_a(slotId, clickedButton, mode, playerIn);
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(par2);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
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
