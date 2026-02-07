package thaumcraft.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.tiles.essentia.TileTubeBuffer;

public class ItemResonator extends ItemTCBase {
   public ItemResonator() {
      super("resonator");
      this.func_77625_d(1);
      this.func_77625_d(1);
      this.func_77637_a(ConfigItems.TABTC);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public boolean func_77636_d(ItemStack par1ItemStack) {
      return par1ItemStack.func_77942_o();
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && tile instanceof IEssentiaTransport) {
         if (world.field_72995_K) {
            player.func_184609_a(hand);
            return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
         } else {
            IEssentiaTransport et = (IEssentiaTransport)tile;
            RayTraceResult hit = RayTracer.retraceBlock(world, player, pos);
            if (hit != null && hit.subHit >= 0 && hit.subHit < 6) {
               side = EnumFacing.field_82609_l[hit.subHit];
            }

            if (!(tile instanceof TileTubeBuffer) && et.getEssentiaType(side) != null) {
               player.func_145747_a(new TextComponentTranslation("tc.resonator1", new Object[]{"" + et.getEssentiaAmount(side), et.getEssentiaType(side).getName()}));
            } else if (tile instanceof TileTubeBuffer && ((IAspectContainer)tile).getAspects().size() > 0) {
               Aspect[] var13 = ((IAspectContainer)tile).getAspects().getAspectsSortedByName();
               int var14 = var13.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  Aspect aspect = var13[var15];
                  player.func_145747_a(new TextComponentTranslation("tc.resonator1", new Object[]{"" + ((IAspectContainer)tile).getAspects().getAmount(aspect), aspect.getName()}));
               }
            }

            String s = I18n.func_74838_a("tc.resonator3");
            if (et.getSuctionType(side) != null) {
               s = et.getSuctionType(side).getName();
            }

            player.func_145747_a(new TextComponentTranslation("tc.resonator2", new Object[]{"" + et.getSuctionAmount(side), s}));
            world.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), SoundEvents.field_187767_eL, SoundCategory.BLOCKS, 0.5F, 1.9F + world.field_73012_v.nextFloat() * 0.1F);
            return EnumActionResult.SUCCESS;
         }
      } else {
         return EnumActionResult.FAIL;
      }
   }
}
