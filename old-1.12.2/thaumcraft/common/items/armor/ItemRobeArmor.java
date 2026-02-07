package thaumcraft.common.items.armor;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;

public class ItemRobeArmor extends ItemArmor implements IVisDiscountGear, IThaumcraftItems {
   public ItemRobeArmor(String name, ArmorMaterial enumarmormaterial, int j, EntityEquipmentSlot k) {
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

   public boolean func_82816_b_(ItemStack par1ItemStack) {
      return true;
   }

   public int func_82814_b(ItemStack par1ItemStack) {
      NBTTagCompound nbttagcompound = par1ItemStack.func_77978_p();
      if (nbttagcompound == null) {
         return 6961280;
      } else {
         NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("display");
         return nbttagcompound1 == null ? 6961280 : (nbttagcompound1.func_74764_b("color") ? nbttagcompound1.func_74762_e("color") : 6961280);
      }
   }

   public void func_82815_c(ItemStack par1ItemStack) {
      NBTTagCompound nbttagcompound = par1ItemStack.func_77978_p();
      if (nbttagcompound != null) {
         NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("display");
         if (nbttagcompound1.func_74764_b("color")) {
            nbttagcompound1.func_82580_o("color");
         }
      }

   }

   public void func_82813_b(ItemStack par1ItemStack, int par2) {
      NBTTagCompound nbttagcompound = par1ItemStack.func_77978_p();
      if (nbttagcompound == null) {
         nbttagcompound = new NBTTagCompound();
         par1ItemStack.func_77982_d(nbttagcompound);
      }

      NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("display");
      if (!nbttagcompound.func_74764_b("display")) {
         nbttagcompound.func_74782_a("display", nbttagcompound1);
      }

      nbttagcompound1.func_74768_a("color", par2);
   }

   public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
      if (stack.func_77973_b() != ItemsTC.clothChest && stack.func_77973_b() != ItemsTC.clothBoots) {
         if (stack.func_77973_b() == ItemsTC.clothLegs) {
            return type == null ? "thaumcraft:textures/models/armor/robes_2.png" : "thaumcraft:textures/models/armor/robes_2_overlay.png";
         } else {
            return type == null ? "thaumcraft:textures/models/armor/robes_1.png" : "thaumcraft:textures/models/armor/robes_1_overlay.png";
         }
      } else {
         return type == null ? "thaumcraft:textures/models/armor/robes_1.png" : "thaumcraft:textures/models/armor/robes_1_overlay.png";
      }
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.fabric)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player) {
      return this.field_77881_a == EntityEquipmentSlot.FEET ? 2 : 3;
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      IBlockState bs = world.func_180495_p(pos);
      if (bs.func_177230_c() == Blocks.field_150383_bp) {
         BlockCauldron var10001 = Blocks.field_150383_bp;
         int i = (Integer)bs.func_177229_b(BlockCauldron.field_176591_a);
         if (!world.field_72995_K && i > 0) {
            this.func_82815_c(stack);
            Blocks.field_150383_bp.func_176590_a(world, pos, bs, i - 1);
            return EnumActionResult.SUCCESS;
         }
      }

      return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
   }
}
