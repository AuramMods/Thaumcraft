package thaumcraft.common.items.armor;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemCultistBoots extends ItemArmor implements IWarpingGear, IVisDiscountGear, IThaumcraftItems {
   public ItemCultistBoots() {
      super(ArmorMaterial.IRON, 2, EntityEquipmentSlot.FEET);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("crimson_boots");
      this.func_77655_b("crimson_boots");
      ConfigItems.ITEM_VARIANT_HOLDERS.add(this);
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
      return "thaumcraft:textures/models/armor/cultistboots.png";
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(Items.field_151042_j)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public int getWarp(ItemStack itemstack, EntityPlayer player) {
      return 1;
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player) {
      return 1;
   }
}
