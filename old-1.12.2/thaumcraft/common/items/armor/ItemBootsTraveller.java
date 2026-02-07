package thaumcraft.common.items.armor;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.events.PlayerEvents;

public class ItemBootsTraveller extends ItemArmor implements IThaumcraftItems, IRechargable {
   public ItemBootsTraveller() {
      super(ThaumcraftMaterials.ARMORMAT_SPECIAL, 4, EntityEquipmentSlot.FEET);
      this.func_77656_e(350);
      this.setRegistryName("traveller_boots");
      this.func_77655_b("traveller_boots");
      ConfigItems.ITEM_VARIANT_HOLDERS.add(this);
      this.func_77637_a(ConfigItems.TABTC);
   }

   public Item getItem() {
      return this;
   }

   public String[] getVariantNames() {
      return new String[]{"normal"};
   }

   public int[] getVariantMeta() {
      return new int[]{0};
   }

   public ItemMeshDefinition getCustomMesh() {
      return null;
   }

   public ModelResourceLocation getCustomModelResourceLocation(String variant) {
      return new ModelResourceLocation("thaumcraft:" + variant);
   }

   public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
      return "thaumcraft:textures/models/armor/bootstraveler.png";
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(Items.field_151116_aA)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
      boolean hasCharge = RechargeHelper.getCharge(itemStack) > 0;
      if (!world.field_72995_K && player.field_70173_aa % 20 == 0) {
         int e = 0;
         if (itemStack.func_77942_o()) {
            e = itemStack.func_77978_p().func_74762_e("energy");
         }

         if (e > 0) {
            --e;
         } else if (e <= 0 && RechargeHelper.consumeCharge(itemStack, player, 1)) {
            e = 60;
         }

         itemStack.func_77983_a("energy", new NBTTagInt(e));
      }

      if (hasCharge && !player.field_71075_bZ.field_75100_b && player.field_70701_bs > 0.0F) {
         if (player.field_70170_p.field_72995_K && !player.func_70093_af()) {
            if (!PlayerEvents.INSTANCE.prevStep.containsKey(player.func_145782_y())) {
               PlayerEvents.INSTANCE.prevStep.put(player.func_145782_y(), player.field_70138_W);
            }

            player.field_70138_W = 1.0F;
         }

         if (player.field_70122_E) {
            float bonus = 0.055F;
            if (player.func_70090_H()) {
               bonus /= 4.0F;
            }

            player.func_70060_a(0.0F, 1.0F, bonus);
         } else {
            player.field_70747_aH = 0.05F;
         }
      }

      if (player.field_70143_R > 0.0F) {
         player.field_70143_R -= 0.25F;
      }

   }

   public int getMaxCharge(ItemStack stack, EntityLivingBase player) {
      return 240;
   }

   public IRechargable.EnumChargeDisplay showInHud(ItemStack stack, EntityLivingBase player) {
      return IRechargable.EnumChargeDisplay.PERIODIC;
   }
}
