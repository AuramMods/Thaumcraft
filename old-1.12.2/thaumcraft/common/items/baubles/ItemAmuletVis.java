package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.common.items.ItemTCBase;

public class ItemAmuletVis extends ItemTCBase implements IBauble {
   public ItemAmuletVis() {
      super("amulet_vis", "found", "crafted");
      this.field_77777_bU = 1;
      this.func_77656_e(0);
      this.func_77627_a(true);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return itemstack.func_77952_i() == 0 ? EnumRarity.UNCOMMON : EnumRarity.RARE;
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.AMULET;
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
      if (player instanceof EntityPlayer && !player.field_70170_p.field_72995_K && player.field_70173_aa % (itemstack.func_77952_i() == 0 ? 40 : 5) == 0) {
         ItemStack[] inv = ((EntityPlayer)player).field_71071_by.field_70462_a;
         int a = 0;

         while(true) {
            InventoryPlayer var10001 = ((EntityPlayer)player).field_71071_by;
            if (a >= InventoryPlayer.func_70451_h()) {
               IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer)player);

               int a;
               for(a = 0; a < baubles.getSlots(); ++a) {
                  if (RechargeHelper.rechargeItem(player.field_70170_p, baubles.getStackInSlot(a), player.func_180425_c(), (EntityPlayer)player, 1) > 0.0F) {
                     return;
                  }
               }

               inv = ((EntityPlayer)player).field_71071_by.field_70460_b;

               for(a = 0; a < inv.length; ++a) {
                  if (RechargeHelper.rechargeItem(player.field_70170_p, inv[a], player.func_180425_c(), (EntityPlayer)player, 1) > 0.0F) {
                     return;
                  }
               }
               break;
            }

            if (RechargeHelper.rechargeItem(player.field_70170_p, inv[a], player.func_180425_c(), (EntityPlayer)player, 1) > 0.0F) {
               return;
            }

            ++a;
         }
      }

   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      list.add(TextFormatting.AQUA + I18n.func_74838_a("item.amulet_vis.text"));
   }
}
