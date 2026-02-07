package thaumcraft.common.items.tools;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class ItemElementalPickaxe extends ItemPickaxe implements IThaumcraftItems {
   public ItemElementalPickaxe(ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("elemental_pick");
      this.func_77655_b("elemental_pick");
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

   public Set<String> getToolClasses(ItemStack stack) {
      return ImmutableSet.of("pickaxe");
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (!player.field_70170_p.field_72995_K && (!(entity instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W())) {
         entity.func_70015_d(2);
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      ItemStack w1 = new ItemStack(this);
      EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.REFINING, 1);
      EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.SOUNDING, 2);
      par3List.add(w1);
   }
}
