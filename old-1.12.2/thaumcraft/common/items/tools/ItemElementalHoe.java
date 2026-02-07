package thaumcraft.common.items.tools;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.utils.Utils;

public class ItemElementalHoe extends ItemHoe implements IThaumcraftItems {
   public ItemElementalHoe(ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("elemental_hoe");
      this.func_77655_b("elemental_hoe");
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

   public int func_77619_b() {
      return 5;
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (player.func_70093_af()) {
         return super.func_180614_a(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
      } else {
         boolean did = false;

         for(int xx = -1; xx <= 1; ++xx) {
            for(int zz = -1; zz <= 1; ++zz) {
               if (super.func_180614_a(stack, player, world, pos.func_177982_a(xx, 0, zz), hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
                  if (world.field_72995_K) {
                     BlockPos pp = pos.func_177982_a(xx, 0, zz);
                     FXDispatcher.INSTANCE.drawBamf((double)pp.func_177958_n() + 0.5D, (double)pp.func_177956_o() + 1.01D, (double)pp.func_177952_p() + 0.5D, 0.3F, 0.12F, 0.1F, xx == 0 && zz == 0, false, EnumFacing.UP);
                  }

                  if (!did) {
                     did = true;
                  }
               }
            }
         }

         if (!did) {
            did = Utils.useBonemealAtLoc(world, player, pos);
            if (did) {
               stack.func_77972_a(3, player);
               if (!world.field_72995_K) {
                  world.func_175669_a(2005, pos, 0);
               } else {
                  FXDispatcher.INSTANCE.drawBlockMistParticles(pos, 4259648);
               }
            }
         }

         return EnumActionResult.SUCCESS;
      }
   }
}
