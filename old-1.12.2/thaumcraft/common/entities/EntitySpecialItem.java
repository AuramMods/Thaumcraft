package thaumcraft.common.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySpecialItem extends EntityItem {
   public EntitySpecialItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
      super(par1World);
      this.func_70105_a(0.25F, 0.25F);
      this.func_70107_b(par2, par4, par6);
      this.func_92058_a(par8ItemStack);
      this.field_70177_z = (float)(Math.random() * 360.0D);
      this.field_70159_w = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
      this.field_70181_x = 0.20000000298023224D;
      this.field_70179_y = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
   }

   public EntitySpecialItem(World par1World) {
      super(par1World);
      this.func_70105_a(0.25F, 0.25F);
   }

   public void func_70071_h_() {
      if (this.field_70173_aa > 1) {
         if (this.field_70181_x > 0.0D) {
            this.field_70181_x *= 0.8999999761581421D;
         }

         this.field_70181_x += 0.03999999910593033D;
         super.func_70071_h_();
      }

   }

   public boolean func_70097_a(DamageSource p_70097_1_, float p_70097_2_) {
      return p_70097_1_.func_94541_c() ? false : super.func_70097_a(p_70097_1_, p_70097_2_);
   }
}
