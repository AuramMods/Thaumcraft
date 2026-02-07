package thaumcraft.common.items.consumables;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.potions.PotionWarpWard;

public class ItemSanitySoap extends ItemTCBase {
   public ItemSanitySoap() {
      super("sanity_soap");
      this.func_77627_a(false);
   }

   public int func_77626_a(ItemStack p_77626_1_) {
      return 200;
   }

   public EnumAction func_77661_b(ItemStack p_77661_1_) {
      return EnumAction.BLOCK;
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand) {
      p_77659_3_.func_184598_c(hand);
      return new ActionResult(EnumActionResult.SUCCESS, p_77659_1_);
   }

   public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
      int ticks = this.func_77626_a(stack) - count;
      if (ticks > 195) {
         player.func_184597_cx();
      }

      if (player.field_70170_p.field_72995_K) {
         if (player.field_70170_p.field_73012_v.nextFloat() < 0.2F) {
            player.field_70170_p.func_184134_a(player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundsTC.roots, SoundCategory.PLAYERS, 0.1F, 1.5F + player.field_70170_p.field_73012_v.nextFloat() * 0.2F, false);
         }

         for(int a = 0; a < FXDispatcher.INSTANCE.particleCount(5); ++a) {
            FXDispatcher.INSTANCE.crucibleBubble((float)player.field_70165_t - 0.5F + player.field_70170_p.field_73012_v.nextFloat(), (float)player.func_174813_aQ().field_72338_b + player.field_70170_p.field_73012_v.nextFloat() * player.field_70131_O, (float)player.field_70161_v - 0.5F + player.field_70170_p.field_73012_v.nextFloat(), 1.0F, 0.8F, 0.9F);
         }
      }

   }

   public void func_77615_a(ItemStack stack, World world, EntityLivingBase player, int timeLeft) {
      int qq = this.func_77626_a(stack) - timeLeft;
      if (qq > 195 && player instanceof EntityPlayer) {
         --stack.field_77994_a;
         if (!world.field_72995_K) {
            IPlayerWarp warp = ThaumcraftCapabilities.getWarp((EntityPlayer)player);
            float chance = 0.33F;
            if (player.func_70644_a(PotionWarpWard.instance)) {
               chance += 0.25F;
            }

            int i = MathHelper.func_76128_c(player.field_70165_t);
            int j = MathHelper.func_76128_c(player.field_70163_u);
            int k = MathHelper.func_76128_c(player.field_70161_v);
            if (world.func_180495_p(new BlockPos(i, j, k)).func_177230_c() == BlocksTC.purifyingFluid) {
               chance += 0.25F;
            }

            if (world.field_73012_v.nextFloat() < chance && warp.get(IPlayerWarp.EnumWarpType.NORMAL) > 0) {
               ThaumcraftApi.internalMethods.addWarpToPlayer((EntityPlayer)player, -1, IPlayerWarp.EnumWarpType.NORMAL);
            }

            if (warp.get(IPlayerWarp.EnumWarpType.TEMPORARY) > 0) {
               ThaumcraftApi.internalMethods.addWarpToPlayer((EntityPlayer)player, -warp.get(IPlayerWarp.EnumWarpType.TEMPORARY), IPlayerWarp.EnumWarpType.TEMPORARY);
            }
         } else {
            player.field_70170_p.func_184134_a(player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundsTC.craftstart, SoundCategory.PLAYERS, 0.25F, 1.0F, false);

            for(int a = 0; a < FXDispatcher.INSTANCE.particleCount(20); ++a) {
               FXDispatcher.INSTANCE.crucibleBubble((float)player.field_70165_t - 0.5F + player.field_70170_p.field_73012_v.nextFloat() * 1.5F, (float)player.func_174813_aQ().field_72338_b + player.field_70170_p.field_73012_v.nextFloat() * player.field_70131_O, (float)player.field_70161_v - 0.5F + player.field_70170_p.field_73012_v.nextFloat() * 1.5F, 1.0F, 0.7F, 0.9F);
            }
         }
      }

   }
}
