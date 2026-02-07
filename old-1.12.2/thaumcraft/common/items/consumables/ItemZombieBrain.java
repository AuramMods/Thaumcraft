package thaumcraft.common.items.consumables;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemZombieBrain extends ItemFood implements IThaumcraftItems {
   public ItemZombieBrain() {
      super(4, 0.2F, true);
      this.func_185070_a(new PotionEffect(MobEffects.field_76438_s, 30, 0), 0.8F);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("brain");
      this.func_77655_b("brain");
      ConfigItems.ITEM_VARIANT_HOLDERS.add(this);
   }

   public void func_77849_c(ItemStack stack, World world, EntityPlayer player) {
      if (!world.field_72995_K && player instanceof EntityPlayerMP) {
         if (world.field_73012_v.nextFloat() < 0.1F) {
            ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.NORMAL, 1);
         } else {
            ThaumcraftCapabilities.getWarp(player).add(IPlayerWarp.EnumWarpType.TEMPORARY, 1 + world.field_73012_v.nextInt(3));
         }
      }

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
