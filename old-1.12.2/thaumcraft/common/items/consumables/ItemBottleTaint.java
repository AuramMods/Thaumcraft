package thaumcraft.common.items.consumables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.projectile.EntityBottleTaint;
import thaumcraft.common.items.ItemTCBase;

public class ItemBottleTaint extends ItemTCBase {
   public ItemBottleTaint() {
      super("bottle_taint");
      this.field_77777_bU = 8;
      this.func_77656_e(0);
      this.func_77637_a(ConfigItems.TABTC);
      this.func_77627_a(false);
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
      if (!player.field_71075_bZ.field_75098_d) {
         --stack.field_77994_a;
      }

      player.func_184185_a(SoundEvents.field_187511_aA, 0.5F, 0.4F / (field_77697_d.nextFloat() * 0.4F + 0.8F));
      if (!world.field_72995_K) {
         EntityBottleTaint entityBottle = new EntityBottleTaint(world, player);
         entityBottle.func_184538_a(player, player.field_70125_A, player.field_70177_z, 0.0F, 0.66F, 1.0F);
         world.func_72838_d(entityBottle);
      }

      return new ActionResult(EnumActionResult.SUCCESS, stack);
   }
}
