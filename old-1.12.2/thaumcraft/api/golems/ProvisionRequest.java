package thaumcraft.api.golems;

import net.minecraft.item.ItemStack;
import thaumcraft.api.golems.seals.ISealEntity;

public class ProvisionRequest {
   private ISealEntity seal;
   private ItemStack stack;

   ProvisionRequest(ISealEntity seal, ItemStack stack) {
      this.seal = seal;
      this.stack = stack.func_77946_l();
      this.stack.field_77994_a = this.stack.func_77976_d();
   }

   public ISealEntity getSeal() {
      return this.seal;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof ProvisionRequest)) {
         return false;
      } else {
         ProvisionRequest pr = (ProvisionRequest)p_equals_1_;
         return !this.seal.getSealPos().equals(pr.getSeal().getSealPos()) ? false : this.isItemStackEqual(this.stack, pr.getStack());
      }
   }

   private boolean isItemStackEqual(ItemStack first, ItemStack other) {
      return first.field_77994_a != other.field_77994_a ? false : (first.func_77973_b() != other.func_77973_b() ? false : (first.func_77952_i() != other.func_77952_i() ? false : (first.func_77978_p() == null && other.func_77978_p() != null ? false : first.func_77978_p() == null || first.func_77978_p().equals(other.func_77978_p()))));
   }
}
