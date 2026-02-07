package thaumcraft.common.items.tools;

import java.util.List;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemCrimsonBlade extends ItemSword implements IWarpingGear, IThaumcraftItems {
   public static ToolMaterial toolMatCrimsonVoid;

   public ItemCrimsonBlade() {
      super(toolMatCrimsonVoid);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("crimson_blade");
      this.func_77655_b("crimson_blade");
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

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.EPIC;
   }

   public void func_77663_a(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
      super.func_77663_a(stack, world, entity, p_77663_4_, p_77663_5_);
      if (stack.func_77951_h() && entity != null && entity.field_70173_aa % 20 == 0 && entity instanceof EntityLivingBase) {
         stack.func_77972_a(-1, (EntityLivingBase)entity);
      }

   }

   public boolean func_77644_a(ItemStack is, EntityLivingBase target, EntityLivingBase hitter) {
      if (!target.field_70170_p.field_72995_K && (!(target instanceof EntityPlayer) || !(hitter instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W())) {
         try {
            target.func_70690_d(new PotionEffect(MobEffects.field_76437_t, 60));
            target.func_70690_d(new PotionEffect(MobEffects.field_76438_s, 120));
         } catch (Exception var5) {
         }
      }

      return super.func_77644_a(is, target, hitter);
   }

   public int getWarp(ItemStack itemstack, EntityPlayer player) {
      return 2;
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      list.add(TextFormatting.GOLD + I18n.func_74838_a("enchantment.special.sapgreat"));
      super.func_77624_a(stack, player, list, par4);
   }

   static {
      toolMatCrimsonVoid = EnumHelper.addToolMaterial("CVOID", 4, 200, 8.0F, 3.5F, 20).setRepairItem(new ItemStack(ItemsTC.ingots, 1, 1));
   }
}
