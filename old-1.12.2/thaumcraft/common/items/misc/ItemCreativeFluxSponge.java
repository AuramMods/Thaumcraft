package thaumcraft.common.items.misc;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.lib.SoundsTC;

public class ItemCreativeFluxSponge extends ItemTCBase {
   public ItemCreativeFluxSponge() {
      super("creative_flux_sponge");
      this.func_77625_d(1);
      this.func_77627_a(false);
      this.func_77656_e(0);
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.func_77624_a(stack, player, list, par4);
      list.add(TextFormatting.GREEN + "Right-click to drain all");
      list.add(TextFormatting.GREEN + "flux from 9x9 chunk area");
      list.add(TextFormatting.DARK_PURPLE + "Creative only");
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.EPIC;
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      if (worldIn.field_72995_K) {
         playerIn.func_184609_a(hand);
         playerIn.field_70170_p.func_184134_a(playerIn.field_70165_t, playerIn.field_70163_u, playerIn.field_70161_v, SoundsTC.craftstart, SoundCategory.PLAYERS, 0.15F, 1.0F, false);
      } else {
         int q = 0;
         BlockPos p = playerIn.func_180425_c();

         for(int x = -4; x <= 4; ++x) {
            for(int z = -4; z <= 4; ++z) {
               q = (int)((float)q + AuraHelper.drainFlux(worldIn, p.func_177982_a(16 * x, 0, 16 * z), 500.0F, false));
            }
         }

         playerIn.func_145747_a(new TextComponentString(TextFormatting.GREEN + "" + q + " flux drained from 81 chunks."));
      }

      return super.func_77659_a(itemStackIn, worldIn, playerIn, hand);
   }
}
