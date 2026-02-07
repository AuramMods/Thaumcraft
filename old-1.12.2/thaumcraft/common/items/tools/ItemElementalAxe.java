package thaumcraft.common.items.tools;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemElementalAxe extends ItemAxe implements IThaumcraftItems {
   public ItemElementalAxe(ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial, 8.0F, -3.0F);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("elemental_axe");
      this.func_77655_b("elemental_axe");
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
      return ImmutableSet.of("axe");
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public EnumAction func_77661_b(ItemStack itemstack) {
      return EnumAction.BOW;
   }

   public int func_77626_a(ItemStack p_77626_1_) {
      return 72000;
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      playerIn.func_184598_c(hand);
      return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
   }

   public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
      List<EntityItem> stuff = EntityUtils.getEntitiesInRange(player.field_70170_p, player.field_70165_t, player.field_70163_u, player.field_70161_v, player, EntityItem.class, 10.0D);
      if (stuff != null && stuff.size() > 0) {
         Iterator var5 = stuff.iterator();

         while(var5.hasNext()) {
            EntityItem e = (EntityItem)var5.next();
            if (!e.field_70128_L) {
               double d6 = e.field_70165_t - player.field_70165_t;
               double d8 = e.field_70163_u - player.field_70163_u + (double)(player.field_70131_O / 2.0F);
               double d10 = e.field_70161_v - player.field_70161_v;
               double d11 = (double)MathHelper.func_76133_a(d6 * d6 + d8 * d8 + d10 * d10);
               d6 /= d11;
               d8 /= d11;
               d10 /= d11;
               double d13 = 0.3D;
               e.field_70159_w -= d6 * d13;
               e.field_70181_x -= d8 * d13 - 0.1D;
               e.field_70179_y -= d10 * d13;
               if (e.field_70159_w > 0.25D) {
                  e.field_70159_w = 0.25D;
               }

               if (e.field_70159_w < -0.25D) {
                  e.field_70159_w = -0.25D;
               }

               if (e.field_70181_x > 0.25D) {
                  e.field_70181_x = 0.25D;
               }

               if (e.field_70181_x < -0.25D) {
                  e.field_70181_x = -0.25D;
               }

               if (e.field_70179_y > 0.25D) {
                  e.field_70179_y = 0.25D;
               }

               if (e.field_70179_y < -0.25D) {
                  e.field_70179_y = -0.25D;
               }

               if (player.field_70170_p.field_72995_K) {
                  FXDispatcher.INSTANCE.crucibleBubble((float)e.field_70165_t + (player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 0.2F, (float)e.field_70163_u + e.field_70131_O + (player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 0.2F, (float)e.field_70161_v + (player.field_70170_p.field_73012_v.nextFloat() - player.field_70170_p.field_73012_v.nextFloat()) * 0.2F, 0.33F, 0.33F, 1.0F);
               }
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      ItemStack w1 = new ItemStack(this);
      EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.BURROWING, 1);
      EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.COLLECTOR, 1);
      par3List.add(w1);
   }
}
