package thaumcraft.api.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aura.AuraHelper;

public class RechargeHelper {
   public static final String NBT_TAG = "tc.charge";

   public static float rechargeItem(World world, ItemStack is, BlockPos pos, EntityPlayer player, int amt) {
      if (is != null && is.func_77973_b() instanceof IRechargable) {
         IRechargable chargeItem = (IRechargable)is.func_77973_b();
         if (player != null && AuraHelper.shouldPreserveAura(world, player, pos)) {
            return 0.0F;
         } else {
            amt = Math.min(amt, chargeItem.getMaxCharge(is, player) - getCharge(is));
            int drained = (int)AuraHelper.drainVis(world, pos, (float)amt, false);
            if (drained > 0) {
               addCharge(is, player, drained);
               return (float)drained;
            } else {
               return 0.0F;
            }
         }
      } else {
         return 0.0F;
      }
   }

   public static float rechargeItemBlindly(ItemStack is, EntityPlayer player, int amt) {
      if (is != null && is.func_77973_b() instanceof IRechargable) {
         IRechargable chargeItem = (IRechargable)is.func_77973_b();
         amt = Math.min(amt, chargeItem.getMaxCharge(is, player) - getCharge(is));
         if (amt > 0) {
            addCharge(is, player, amt);
         }

         return (float)amt;
      } else {
         return 0.0F;
      }
   }

   private static void addCharge(ItemStack is, EntityLivingBase player, int amt) {
      if (is != null && is.func_77973_b() instanceof IRechargable) {
         IRechargable chargeItem = (IRechargable)is.func_77973_b();
         int amount = Math.min(chargeItem.getMaxCharge(is, player), amt + getCharge(is));
         is.func_77983_a("tc.charge", new NBTTagInt(amount));
      }
   }

   public static int getCharge(ItemStack is) {
      if (is != null && is.func_77973_b() instanceof IRechargable) {
         return is.func_77942_o() ? is.func_77978_p().func_74762_e("tc.charge") : 0;
      } else {
         return -1;
      }
   }

   public static float getChargePercentage(ItemStack is, EntityPlayer player) {
      if (is != null && is.func_77973_b() instanceof IRechargable) {
         float c = (float)getCharge(is);
         float m = (float)((IRechargable)is.func_77973_b()).getMaxCharge(is, player);
         return c / m;
      } else {
         return -1.0F;
      }
   }

   public static boolean consumeCharge(ItemStack is, EntityLivingBase player, int amt) {
      if (is != null && is.func_77973_b() instanceof IRechargable) {
         if (is.func_77942_o()) {
            int charge = is.func_77978_p().func_74762_e("tc.charge");
            if (charge >= amt) {
               charge -= amt;
               is.func_77983_a("tc.charge", new NBTTagInt(charge));
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
