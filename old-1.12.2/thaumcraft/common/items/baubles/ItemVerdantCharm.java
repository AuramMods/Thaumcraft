package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.common.items.ItemTCBase;

public class ItemVerdantCharm extends ItemTCBase implements IBauble, IRechargable {
   public ItemVerdantCharm() {
      super("verdant_charm");
      this.field_77777_bU = 1;
      this.canRepair = false;
      this.func_77656_e(0);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.CHARM;
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this));
      ItemStack vhbl = new ItemStack(this);
      vhbl.func_77983_a("type", new NBTTagByte((byte)1));
      par3List.add(vhbl);
      ItemStack vhbl2 = new ItemStack(this);
      vhbl2.func_77983_a("type", new NBTTagByte((byte)2));
      par3List.add(vhbl2);
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74771_c("type") == 1) {
         list.add(TextFormatting.GOLD + I18n.func_74838_a("item.verdant_charm.life.text"));
      }

      if (stack.func_77942_o() && stack.func_77978_p().func_74771_c("type") == 2) {
         list.add(TextFormatting.GOLD + I18n.func_74838_a("item.verdant_charm.sustain.text"));
      }

   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
      if (!player.field_70170_p.field_72995_K && player.field_70173_aa % 20 == 0 && player instanceof EntityPlayer) {
         if (player.func_70660_b(MobEffects.field_82731_v) != null && RechargeHelper.consumeCharge(itemstack, player, 20)) {
            player.func_184589_d(MobEffects.field_82731_v);
            return;
         }

         if (player.func_70660_b(MobEffects.field_76436_u) != null && RechargeHelper.consumeCharge(itemstack, player, 10)) {
            player.func_184589_d(MobEffects.field_76436_u);
            return;
         }

         if (player.func_70660_b(PotionFluxTaint.instance) != null && RechargeHelper.consumeCharge(itemstack, player, 5)) {
            player.func_184589_d(PotionFluxTaint.instance);
            return;
         }

         if (itemstack.func_77942_o() && itemstack.func_77978_p().func_74771_c("type") == 1 && player.func_110143_aJ() < player.func_110138_aP() && RechargeHelper.consumeCharge(itemstack, player, 5)) {
            player.func_70691_i(1.0F);
            return;
         }

         if (itemstack.func_77942_o() && itemstack.func_77978_p().func_74771_c("type") == 2) {
            if (player.func_70086_ai() < 100 && RechargeHelper.consumeCharge(itemstack, player, 1)) {
               player.func_70050_g(300);
               return;
            }

            if (player instanceof EntityPlayer && ((EntityPlayer)player).func_71043_e(false) && RechargeHelper.consumeCharge(itemstack, player, 1)) {
               ((EntityPlayer)player).func_71024_bL().func_75122_a(1, 0.3F);
               return;
            }
         }
      }

   }

   public int getMaxCharge(ItemStack stack, EntityLivingBase player) {
      return 200;
   }

   public IRechargable.EnumChargeDisplay showInHud(ItemStack stack, EntityLivingBase player) {
      return IRechargable.EnumChargeDisplay.NORMAL;
   }

   public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }
}
