package thaumcraft.common.items.casters;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.common.items.ItemTCBase;

public class ItemFocusPouch extends ItemTCBase implements IBauble {
   public ItemFocusPouch() {
      super("focus_pouch");
      this.func_77625_d(1);
      this.func_77627_a(false);
      this.func_77656_e(0);
   }

   public boolean func_77651_p() {
      return true;
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public boolean func_77636_d(ItemStack par1ItemStack) {
      return false;
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      if (!worldIn.field_72995_K) {
         playerIn.openGui(Thaumcraft.instance, 5, worldIn, MathHelper.func_76128_c(playerIn.field_70165_t), MathHelper.func_76128_c(playerIn.field_70163_u), MathHelper.func_76128_c(playerIn.field_70161_v));
      }

      return super.func_77659_a(itemStackIn, worldIn, playerIn, hand);
   }

   public ItemStack[] getInventory(ItemStack item) {
      ItemStack[] stackList = new ItemStack[18];
      if (item.func_77942_o()) {
         NBTTagList var2 = item.func_77978_p().func_150295_c("Inventory", 10);

         for(int var3 = 0; var3 < var2.func_74745_c(); ++var3) {
            NBTTagCompound var4 = var2.func_150305_b(var3);
            int var5 = var4.func_74771_c("Slot") & 255;
            if (var5 >= 0 && var5 < stackList.length) {
               stackList[var5] = ItemStack.func_77949_a(var4);
            }
         }
      }

      return stackList;
   }

   public void setInventory(ItemStack item, ItemStack[] stackList) {
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < stackList.length; ++var3) {
         if (stackList[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.func_74774_a("Slot", (byte)var3);
            stackList[var3].func_77955_b(var4);
            var2.func_74742_a(var4);
         }
      }

      item.func_77983_a("Inventory", var2);
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.BELT;
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }
}
