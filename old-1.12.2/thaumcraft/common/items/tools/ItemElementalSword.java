package thaumcraft.common.items.tools;

import java.util.List;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemElementalSword extends ItemSword implements IThaumcraftItems {
   public ItemElementalSword(ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.func_77637_a(ConfigItems.TABTC);
      this.setRegistryName("elemental_sword");
      this.func_77655_b("elemental_sword");
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

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      ItemStack w1 = new ItemStack(this);
      EnumInfusionEnchantment.addInfusionEnchantment(w1, EnumInfusionEnchantment.ARCING, 2);
      par3List.add(w1);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.RARE;
   }

   public boolean func_82789_a(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.func_77969_a(new ItemStack(ItemsTC.ingots, 1, 0)) ? true : super.func_82789_a(par1ItemStack, par2ItemStack);
   }

   public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
      super.onUsingTick(stack, player, count);
      int ticks = this.func_77626_a(stack) - count;
      if (player.field_70181_x < 0.0D) {
         player.field_70181_x /= 1.2000000476837158D;
         player.field_70143_R /= 1.2F;
      }

      player.field_70181_x += 0.07999999821186066D;
      if (player.field_70181_x > 0.5D) {
         player.field_70181_x = 0.20000000298023224D;
      }

      if (player instanceof EntityPlayerMP) {
         EntityUtils.resetFloatCounter((EntityPlayerMP)player);
      }

      List targets = player.field_70170_p.func_72839_b(player, player.func_174813_aQ().func_72314_b(2.5D, 2.5D, 2.5D));
      int miny;
      if (targets.size() > 0) {
         for(miny = 0; miny < targets.size(); ++miny) {
            Entity entity = (Entity)targets.get(miny);
            if (!(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && !entity.field_70128_L && (player.func_184187_bx() == null || player.func_184187_bx() != entity)) {
               Vec3d p = new Vec3d(player.field_70165_t, player.field_70163_u, player.field_70161_v);
               Vec3d t = new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);
               double distance = p.func_72438_d(t) + 0.1D;
               Vec3d r = new Vec3d(t.field_72450_a - p.field_72450_a, t.field_72448_b - p.field_72448_b, t.field_72449_c - p.field_72449_c);
               entity.field_70159_w += r.field_72450_a / 2.5D / distance;
               entity.field_70181_x += r.field_72448_b / 2.5D / distance;
               entity.field_70179_y += r.field_72449_c / 2.5D / distance;
            }
         }
      }

      if (player.field_70170_p.field_72995_K) {
         miny = (int)(player.func_174813_aQ().field_72338_b - 2.0D);
         if (player.field_70122_E) {
            miny = MathHelper.func_76128_c(player.func_174813_aQ().field_72338_b);
         }

         for(int a = 0; a < 5; ++a) {
            FXDispatcher.INSTANCE.smokeSpiral(player.field_70165_t, player.func_174813_aQ().field_72338_b + (double)(player.field_70131_O / 2.0F), player.field_70161_v, 1.5F, player.field_70170_p.field_73012_v.nextInt(360), miny, 14540253);
         }

         if (player.field_70122_E) {
            float r1 = player.field_70170_p.field_73012_v.nextFloat() * 360.0F;
            float mx = -MathHelper.func_76126_a(r1 / 180.0F * 3.1415927F) / 5.0F;
            float mz = MathHelper.func_76134_b(r1 / 180.0F * 3.1415927F) / 5.0F;
            player.field_70170_p.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, player.field_70165_t, player.func_174813_aQ().field_72338_b + 0.10000000149011612D, player.field_70161_v, (double)mx, 0.0D, (double)mz, new int[0]);
         }
      } else if (ticks == 0 || ticks % 20 == 0) {
         player.func_184185_a(SoundsTC.wind, 0.5F, 0.9F + player.field_70170_p.field_73012_v.nextFloat() * 0.2F);
      }

      if (ticks % 20 == 0) {
         stack.func_77972_a(1, player);
      }

   }
}
