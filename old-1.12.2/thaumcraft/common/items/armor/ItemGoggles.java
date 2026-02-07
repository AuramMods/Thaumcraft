package thaumcraft.common.items.armor;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import baubles.api.render.IRenderBauble.Helper;
import baubles.api.render.IRenderBauble.RenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemGoggles extends ItemArmor implements IVisDiscountGear, IRevealer, IGoggles, IThaumcraftItems, IBauble, IRenderBauble {
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/items/goggles_bauble.png");

   public ItemGoggles() {
      super(ThaumcraftMaterials.ARMORMAT_SPECIAL, 4, EntityEquipmentSlot.HEAD);
      this.func_77656_e(350);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("goggles");
      this.func_77655_b("goggles");
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
      return "thaumcraft:textures/models/armor/goggles.png";
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 2)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player) {
      return 5;
   }

   public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public BaubleType getBaubleType(ItemStack arg0) {
      return BaubleType.HEAD;
   }

   @SideOnly(Side.CLIENT)
   public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float ticks) {
      if (type == RenderType.HEAD) {
         boolean armor = player.func_184582_a(EntityEquipmentSlot.HEAD) != null;
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.tex);
         Helper.translateToHeadLevel(player);
         Helper.translateToFace();
         Helper.defaultTransforms();
         GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179137_b(-0.5D, -0.5D, armor ? 0.11999999731779099D : 0.0D);
         UtilsFX.renderTextureIn3D(0.0F, 0.0F, 1.0F, 1.0F, 16, 26, 0.1F);
      }

   }
}
