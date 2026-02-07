package thaumcraft.common.items.consumables;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemTripleMeatTreat extends ItemFood implements IThaumcraftItems {
   public ItemTripleMeatTreat() {
      super(6, 0.8F, true);
      this.func_77848_i();
      this.setRegistryName("triple_meat_treat");
      this.func_77655_b("triple_meat_treat");
      this.func_77637_a(ConfigItems.TABTC);
      this.func_185070_a(new PotionEffect(MobEffects.field_76428_l, 5, 0), 0.66F);
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
}
