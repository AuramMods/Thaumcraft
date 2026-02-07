package thaumcraft.common.items;

import java.util.List;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.ConfigItems;

public class ItemTCBase extends Item implements IThaumcraftItems {
   protected final String BASE_NAME;
   protected String[] VARIANTS;
   protected int[] VARIANTS_META;

   public ItemTCBase(String name, String... variants) {
      this.setRegistryName(name);
      this.func_77655_b(name);
      this.func_77637_a(ConfigItems.TABTC);
      this.setNoRepair();
      this.func_77627_a(variants.length > 1);
      this.BASE_NAME = name;
      if (variants.length == 0) {
         this.VARIANTS = new String[]{name};
      } else {
         this.VARIANTS = variants;
      }

      this.VARIANTS_META = new int[this.VARIANTS.length];

      for(int m = 0; m < this.VARIANTS.length; this.VARIANTS_META[m] = m++) {
      }

      ConfigItems.ITEM_VARIANT_HOLDERS.add(this);
   }

   public String func_77667_c(ItemStack itemStack) {
      return this.field_77787_bX && itemStack.func_77960_j() < this.VARIANTS.length && this.VARIANTS[itemStack.func_77960_j()] != this.BASE_NAME ? String.format(super.func_77658_a() + ".%s", this.VARIANTS[itemStack.func_77960_j()]) : super.func_77667_c(itemStack);
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item item, CreativeTabs creativeTab, List<ItemStack> list) {
      if (!this.func_77614_k()) {
         super.func_150895_a(item, creativeTab, list);
      } else {
         for(int meta = 0; meta < this.VARIANTS.length; ++meta) {
            list.add(new ItemStack(this, 1, meta));
         }
      }

   }

   public Item getItem() {
      return this;
   }

   public String[] getVariantNames() {
      return this.VARIANTS;
   }

   public int[] getVariantMeta() {
      return this.VARIANTS_META;
   }

   public ItemMeshDefinition getCustomMesh() {
      return null;
   }

   public ModelResourceLocation getCustomModelResourceLocation(String variant) {
      return variant.equals(this.BASE_NAME) ? new ModelResourceLocation("thaumcraft:" + this.BASE_NAME) : new ModelResourceLocation("thaumcraft:" + this.BASE_NAME, variant);
   }
}
