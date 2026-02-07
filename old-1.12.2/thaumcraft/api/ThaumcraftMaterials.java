package thaumcraft.api;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import thaumcraft.api.items.ItemsTC;

public class ThaumcraftMaterials {
   public static ToolMaterial TOOLMAT_THAUMIUM;
   public static ToolMaterial TOOLMAT_VOID;
   public static ToolMaterial TOOLMAT_ELEMENTAL;
   public static ArmorMaterial ARMORMAT_THAUMIUM;
   public static ArmorMaterial ARMORMAT_SPECIAL;
   public static ArmorMaterial ARMORMAT_VOID;
   public static ArmorMaterial ARMORMAT_VOIDROBE;
   public static ArmorMaterial ARMORMAT_FORTRESS;
   public static final Material MATERIAL_TAINT;

   static {
      TOOLMAT_THAUMIUM = EnumHelper.addToolMaterial("THAUMIUM", 3, 500, 7.0F, 2.5F, 22).setRepairItem(new ItemStack(ItemsTC.ingots));
      TOOLMAT_VOID = EnumHelper.addToolMaterial("VOID", 4, 150, 8.0F, 3.0F, 10).setRepairItem(new ItemStack(ItemsTC.ingots, 1, 1));
      TOOLMAT_ELEMENTAL = EnumHelper.addToolMaterial("THAUMIUM_ELEMENTAL", 3, 1500, 9.0F, 3.0F, 18).setRepairItem(new ItemStack(ItemsTC.ingots));
      ARMORMAT_THAUMIUM = EnumHelper.addArmorMaterial("THAUMIUM", "THAUMIUM", 25, new int[]{2, 5, 6, 2}, 25, SoundEvents.field_187725_r, 1.0F);
      ARMORMAT_SPECIAL = EnumHelper.addArmorMaterial("SPECIAL", "SPECIAL", 25, new int[]{1, 2, 3, 1}, 25, SoundEvents.field_187728_s, 1.0F);
      ARMORMAT_VOID = EnumHelper.addArmorMaterial("VOID", "VOID", 10, new int[]{3, 6, 8, 3}, 10, SoundEvents.field_187713_n, 1.0F);
      ARMORMAT_VOIDROBE = EnumHelper.addArmorMaterial("VOIDROBE", "VOIDROBE", 18, new int[]{4, 7, 9, 4}, 10, SoundEvents.field_187728_s, 2.0F);
      ARMORMAT_FORTRESS = EnumHelper.addArmorMaterial("FORTRESS", "FORTRESS", 40, new int[]{3, 6, 7, 3}, 25, SoundEvents.field_187725_r, 3.0F);
      MATERIAL_TAINT = new ThaumcraftMaterials.MaterialTaint();
   }

   public static class MaterialTaint extends Material {
      public MaterialTaint() {
         super(MapColor.field_151678_z);
         this.func_76219_n();
      }

      public boolean func_76230_c() {
         return true;
      }
   }
}
