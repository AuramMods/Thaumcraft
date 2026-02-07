package thaumcraft.common.items.consumables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityAlumentum;
import thaumcraft.common.items.ItemTCBase;

public class ItemAlumentum extends ItemTCBase {
   public ItemAlumentum() {
      super("alumentum");
      this.func_77625_d(64);
      this.func_77627_a(true);
      this.func_77656_e(0);
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemstack, World world, EntityPlayer player, EnumHand hand) {
      if (!player.field_71075_bZ.field_75098_d) {
         --itemstack.field_77994_a;
      }

      player.func_184185_a(SoundEvents.field_187511_aA, 0.3F, 0.4F / (field_77697_d.nextFloat() * 0.4F + 0.8F));
      if (!world.field_72995_K) {
         EntityAlumentum alumentum = new EntityAlumentum(world, player);
         alumentum.func_184538_a(player, player.field_70125_A, player.field_70177_z, -5.0F, 0.4F, 2.0F);
         world.func_72838_d(alumentum);
      }

      return new ActionResult(EnumActionResult.SUCCESS, itemstack);
   }
}
