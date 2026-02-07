package thaumcraft.common.items.tools;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemVoidShovel extends ItemSpade implements IWarpingGear, IThaumcraftItems {
   public ItemVoidShovel(ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("void_shovel");
      this.func_77655_b("void_shovel");
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
      return ImmutableSet.of("shovel");
   }

   public void func_77663_a(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
      super.func_77663_a(stack, world, entity, p_77663_4_, p_77663_5_);
      if (stack.func_77951_h() && entity != null && entity.field_70173_aa % 20 == 0 && entity instanceof EntityLivingBase) {
         stack.func_77972_a(-1, (EntityLivingBase)entity);
      }

   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (!player.field_70170_p.field_72995_K && entity instanceof EntityLivingBase && (!(entity instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W())) {
         ((EntityLivingBase)entity).func_70690_d(new PotionEffect(MobEffects.field_76437_t, 80));
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   public int getWarp(ItemStack itemstack, EntityPlayer player) {
      return 1;
   }
}
