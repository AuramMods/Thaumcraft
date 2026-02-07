package thaumcraft.common.items.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ScanningManager;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketAuraToClient;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

public class ItemThaumometer extends ItemTCBase {
   public ItemThaumometer() {
      super("thaumometer");
      this.func_77625_d(1);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack stack, World world, EntityPlayer p, EnumHand hand) {
      if (world.field_72995_K) {
         this.drawFX(world, p);
         p.field_70170_p.func_184134_a(p.field_70165_t, p.field_70163_u, p.field_70161_v, SoundsTC.scan, SoundCategory.PLAYERS, 0.5F, 1.0F, false);
         return new ActionResult(EnumActionResult.PASS, stack);
      } else {
         this.doScan(world, p);
         return new ActionResult(EnumActionResult.SUCCESS, stack);
      }
   }

   public void func_77663_a(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
      if (isSelected && !world.field_72995_K && entity.field_70173_aa % 20 == 0 && entity instanceof EntityPlayerMP) {
         this.updateAura(stack, world, (EntityPlayerMP)entity);
      }

      if (isSelected && world.field_72995_K && entity.field_70173_aa % 3 == 0 && entity instanceof EntityPlayer) {
         Entity target = EntityUtils.getPointedEntity(world, entity, 1.0D, 16.0D, 5.0F, true);
         if (target != null && ScanningManager.isThingStillScannable((EntityPlayer)entity, target)) {
            FXDispatcher.INSTANCE.scanHighlight(target);
         }

         RayTraceResult mop = this.getRayTraceResultFromPlayerWild(world, (EntityPlayer)entity, true);
         if (mop != null && mop.func_178782_a() != null && ScanningManager.isThingStillScannable((EntityPlayer)entity, mop.func_178782_a())) {
            FXDispatcher.INSTANCE.scanHighlight(mop.func_178782_a());
         }
      }

   }

   protected RayTraceResult getRayTraceResultFromPlayerWild(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
      float f = playerIn.field_70127_C + (playerIn.field_70125_A - playerIn.field_70127_C) + (float)worldIn.field_73012_v.nextInt(33) - (float)worldIn.field_73012_v.nextInt(33);
      float f1 = playerIn.field_70126_B + (playerIn.field_70177_z - playerIn.field_70126_B) + (float)worldIn.field_73012_v.nextInt(33) - (float)worldIn.field_73012_v.nextInt(33);
      double d0 = playerIn.field_70169_q + (playerIn.field_70165_t - playerIn.field_70169_q);
      double d1 = playerIn.field_70167_r + (playerIn.field_70163_u - playerIn.field_70167_r) + (double)playerIn.func_70047_e();
      double d2 = playerIn.field_70166_s + (playerIn.field_70161_v - playerIn.field_70166_s);
      Vec3d vec3 = new Vec3d(d0, d1, d2);
      float f2 = MathHelper.func_76134_b(-f1 * 0.017453292F - 3.1415927F);
      float f3 = MathHelper.func_76126_a(-f1 * 0.017453292F - 3.1415927F);
      float f4 = -MathHelper.func_76134_b(-f * 0.017453292F);
      float f5 = MathHelper.func_76126_a(-f * 0.017453292F);
      float f6 = f3 * f4;
      float f7 = f2 * f4;
      double d3 = 16.0D;
      Vec3d vec31 = vec3.func_72441_c((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
      return worldIn.func_147447_a(vec3, vec31, useLiquids, !useLiquids, false);
   }

   private void updateAura(ItemStack stack, World world, EntityPlayerMP player) {
      AuraChunk ac = AuraHandler.getAuraChunk(world.field_73011_w.getDimension(), (int)player.field_70165_t >> 4, (int)player.field_70161_v >> 4);
      if (ac != null) {
         if ((ac.getFlux() > ac.getVis() || ac.getFlux() > (float)(ac.getBase() / 3)) && !ThaumcraftCapabilities.knowsResearch(player, "FLUX")) {
            ResearchManager.startResearchWithPopup(player, "FLUX");
            player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("research.FLUX.warn")));
         }

         PacketHandler.INSTANCE.sendTo(new PacketAuraToClient(ac), player);
      }

   }

   private void drawFX(World worldIn, EntityPlayer playerIn) {
      Entity target = EntityUtils.getPointedEntity(worldIn, playerIn, 1.0D, 9.0D, 0.0F, true);
      if (target != null) {
         for(int a = 0; a < FXDispatcher.INSTANCE.particleCount(5); ++a) {
            FXDispatcher.INSTANCE.blockRunes(target.field_70165_t - 0.5D, target.field_70163_u + (double)(target.func_70047_e() / 2.0F), target.field_70161_v - 0.5D, 0.3F + worldIn.field_73012_v.nextFloat() * 0.7F, 0.0F, 0.3F + worldIn.field_73012_v.nextFloat() * 0.7F, (int)(target.field_70131_O * 15.0F), 0.03F);
         }
      } else {
         RayTraceResult mop = this.func_77621_a(worldIn, playerIn, true);
         if (mop != null && mop.func_178782_a() != null) {
            for(int a = 0; a < FXDispatcher.INSTANCE.particleCount(5); ++a) {
               FXDispatcher.INSTANCE.blockRunes((double)mop.func_178782_a().func_177958_n(), (double)mop.func_178782_a().func_177956_o() + 0.25D, (double)mop.func_178782_a().func_177952_p(), 0.3F + worldIn.field_73012_v.nextFloat() * 0.7F, 0.0F, 0.3F + worldIn.field_73012_v.nextFloat() * 0.7F, 15, 0.03F);
            }
         }
      }

   }

   public void doScan(World worldIn, EntityPlayer playerIn) {
      if (!worldIn.field_72995_K) {
         Entity target = EntityUtils.getPointedEntity(worldIn, playerIn, 1.0D, 9.0D, 0.0F, true);
         if (target != null) {
            ScanningManager.scanTheThing(playerIn, target);
         } else {
            RayTraceResult mop = this.func_77621_a(worldIn, playerIn, true);
            if (mop != null && mop.func_178782_a() != null) {
               ScanningManager.scanTheThing(playerIn, mop.func_178782_a());
            } else {
               ScanningManager.scanTheThing(playerIn, (Object)null);
            }
         }
      }

   }
}
