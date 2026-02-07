package thaumcraft.common.entities.construct.golem.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityMoveHelper.Action;
import net.minecraft.util.math.MathHelper;

public class FlightMoveHelper extends EntityMoveHelper {
   private static final String __OBFID = "CL_00002209";

   public FlightMoveHelper(EntityLiving entity) {
      super(entity);
   }

   public void func_75641_c() {
      if (this.field_188491_h == Action.MOVE_TO && !this.field_75648_a.func_70661_as().func_75500_f()) {
         this.field_188491_h = Action.WAIT;
         double d0 = this.field_75646_b - this.field_75648_a.field_70165_t;
         double d1 = this.field_75647_c - this.field_75648_a.field_70163_u;
         double d2 = this.field_75644_d - this.field_75648_a.field_70161_v;
         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
         d3 = (double)MathHelper.func_76133_a(d3);
         d1 /= d3;
         float f = (float)(Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
         this.field_75648_a.field_70177_z = this.func_75639_a(this.field_75648_a.field_70177_z, f, 30.0F);
         this.field_75648_a.field_70761_aq = this.field_75648_a.field_70177_z;
         float f1 = (float)(this.field_75645_e * this.field_75648_a.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e());
         this.field_75648_a.func_70659_e(this.field_75648_a.func_70689_ay() + (f1 - this.field_75648_a.func_70689_ay()) * 0.125F);
         double d4 = Math.sin((double)(this.field_75648_a.field_70173_aa + this.field_75648_a.func_145782_y()) * 0.5D) * 0.05D;
         double d5 = Math.cos((double)(this.field_75648_a.field_70177_z * 3.1415927F / 180.0F));
         double d6 = Math.sin((double)(this.field_75648_a.field_70177_z * 3.1415927F / 180.0F));
         EntityLiving var10000 = this.field_75648_a;
         var10000.field_70159_w += d4 * d5;
         var10000 = this.field_75648_a;
         var10000.field_70179_y += d4 * d6;
         d4 = Math.sin((double)(this.field_75648_a.field_70173_aa + this.field_75648_a.func_145782_y()) * 0.75D) * 0.05D;
         var10000 = this.field_75648_a;
         var10000.field_70181_x += d4 * (d6 + d5) * 0.25D;
         var10000 = this.field_75648_a;
         var10000.field_70181_x += (double)this.field_75648_a.func_70689_ay() * d1 * 0.1D;
         EntityLookHelper entitylookhelper = this.field_75648_a.func_70671_ap();
         double d7 = this.field_75648_a.field_70165_t + d0 / d3 * 2.0D;
         double d8 = (double)this.field_75648_a.func_70047_e() + this.field_75648_a.field_70163_u + d1 / d3 * 1.0D;
         double d9 = this.field_75648_a.field_70161_v + d2 / d3 * 2.0D;
         double d10 = entitylookhelper.func_180423_e();
         double d11 = entitylookhelper.func_180422_f();
         double d12 = entitylookhelper.func_180421_g();
         if (!entitylookhelper.func_180424_b()) {
            d10 = d7;
            d11 = d8;
            d12 = d9;
         }

         this.field_75648_a.func_70671_ap().func_75650_a(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
      } else {
         this.field_75648_a.func_70659_e(0.0F);
      }

   }
}
