package thaumcraft.common.items.armor;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemThaumiumArmor extends ItemArmor implements IThaumcraftItems {
   public ItemThaumiumArmor(String name, ArmorMaterial enumarmormaterial, int j, EntityEquipmentSlot k) {
      super(enumarmormaterial, j, k);
      this.setRegistryName(name);
      this.func_77655_b(name);
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
      if (stack.func_77973_b() != ItemsTC.thaumiumHelm && stack.func_77973_b() != ItemsTC.thaumiumChest && stack.func_77973_b() != ItemsTC.thaumiumBoots) {
         return stack.func_77973_b() == ItemsTC.thaumiumLegs ? "thaumcraft:textures/models/armor/thaumium_2.png" : "thaumcraft:textures/models/armor/thaumium_1.png";
      } else {
         return "thaumcraft:textures/models/armor/thaumium_1.png";
      }
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }
}
