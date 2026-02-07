package thaumcraft.common.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.container.ContainerDummy;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class SlotCraftingArcaneWorkbench extends Slot {
   private final InventoryCrafting craftMatrix;
   private EntityPlayer thePlayer;
   private int amountCrafted;
   private TileArcaneWorkbench tile;

   public SlotCraftingArcaneWorkbench(TileArcaneWorkbench te, EntityPlayer par1EntityPlayer, InventoryCrafting par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
      super(par3IInventory, par4, par5, par6);
      this.thePlayer = par1EntityPlayer;
      this.craftMatrix = par2IInventory;
      this.tile = te;
   }

   public boolean func_75214_a(ItemStack stack) {
      return false;
   }

   public ItemStack func_75209_a(int amount) {
      if (this.func_75216_d()) {
         this.amountCrafted += Math.min(amount, this.func_75211_c().field_77994_a);
      }

      return super.func_75209_a(amount);
   }

   protected void func_75210_a(ItemStack stack, int amount) {
      this.amountCrafted += amount;
      this.func_75208_c(stack);
   }

   protected void func_75208_c(ItemStack stack) {
      if (this.amountCrafted > 0) {
         stack.func_77980_a(this.thePlayer.field_70170_p, this.thePlayer, this.amountCrafted);
      }

      this.amountCrafted = 0;
      if (stack.func_77973_b() == Item.func_150898_a(Blocks.field_150462_ai)) {
         this.thePlayer.func_71029_a(AchievementList.field_187984_h);
      }

      if (stack.func_77973_b() instanceof ItemPickaxe) {
         this.thePlayer.func_71029_a(AchievementList.field_187985_i);
      }

      if (stack.func_77973_b() == Item.func_150898_a(Blocks.field_150460_al)) {
         this.thePlayer.func_71029_a(AchievementList.field_187986_j);
      }

      if (stack.func_77973_b() instanceof ItemHoe) {
         this.thePlayer.func_71029_a(AchievementList.field_76013_l);
      }

      if (stack.func_77973_b() == Items.field_151025_P) {
         this.thePlayer.func_71029_a(AchievementList.field_187988_m);
      }

      if (stack.func_77973_b() == Items.field_151105_aU) {
         this.thePlayer.func_71029_a(AchievementList.field_76011_n);
      }

      if (stack.func_77973_b() instanceof ItemPickaxe && ((ItemPickaxe)stack.func_77973_b()).func_150913_i() != ToolMaterial.WOOD) {
         this.thePlayer.func_71029_a(AchievementList.field_187989_o);
      }

      if (stack.func_77973_b() instanceof ItemSword) {
         this.thePlayer.func_71029_a(AchievementList.field_187991_r);
      }

      if (stack.func_77973_b() == Item.func_150898_a(Blocks.field_150381_bn)) {
         this.thePlayer.func_71029_a(AchievementList.field_187972_E);
      }

      if (stack.func_77973_b() == Item.func_150898_a(Blocks.field_150342_X)) {
         this.thePlayer.func_71029_a(AchievementList.field_187974_G);
      }

      if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77960_j() == 1) {
         this.thePlayer.func_71029_a(AchievementList.field_187980_M);
      }

   }

   public void func_82870_a(EntityPlayer playerIn, ItemStack stack) {
      FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, this.craftMatrix);
      this.func_75208_c(stack);
      ForgeHooks.setCraftingPlayer(playerIn);
      IArcaneRecipe recipe = ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.craftMatrix, this.thePlayer);
      int vis = 0;
      ItemStack[] crystals = null;
      InventoryCrafting ic = null;
      if (recipe != null) {
         vis = recipe.getVis(this.craftMatrix);
         crystals = recipe.getCrystals();
      } else {
         ic = new InventoryCrafting(new ContainerDummy(), 3, 3);

         for(int a = 0; a < 9; ++a) {
            ic.func_70299_a(a, this.craftMatrix.func_70301_a(a));
         }
      }

      if (vis > 0) {
         this.tile.getAura();
         this.tile.spendAura(vis);
      }

      ItemStack[] aitemstack = CraftingManager.func_77594_a().func_180303_b(ic == null ? this.craftMatrix : ic, playerIn.field_70170_p);
      ForgeHooks.setCraftingPlayer((EntityPlayer)null);

      for(int i = 0; i < Math.min(9, aitemstack.length); ++i) {
         ItemStack itemstack = this.craftMatrix.func_70301_a(i);
         ItemStack itemstack1 = aitemstack[i];
         if (itemstack != null) {
            this.craftMatrix.func_70298_a(i, 1);
            itemstack = this.craftMatrix.func_70301_a(i);
         }

         if (itemstack1 != null) {
            if (itemstack == null) {
               this.craftMatrix.func_70299_a(i, itemstack1);
            } else if (ItemStack.func_179545_c(itemstack, itemstack1) && ItemStack.func_77970_a(itemstack, itemstack1)) {
               itemstack1.field_77994_a += itemstack.field_77994_a;
               this.craftMatrix.func_70299_a(i, itemstack1);
            } else if (!this.thePlayer.field_71071_by.func_70441_a(itemstack1)) {
               this.thePlayer.func_71019_a(itemstack1, false);
            }
         }
      }

      if (crystals != null) {
         ItemStack[] var15 = crystals;
         int var16 = crystals.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            ItemStack cs = var15[var17];

            for(int i = 9; i < 15; ++i) {
               ItemStack itemstack1 = this.craftMatrix.func_70301_a(i);
               if (itemstack1 != null && ItemStack.func_77970_a(cs, itemstack1)) {
                  this.craftMatrix.func_70298_a(i, cs.field_77994_a);
               }
            }
         }
      }

   }
}
