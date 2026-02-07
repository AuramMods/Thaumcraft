package thaumcraft.common.entities.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.SoundsTC;

public class EntityGolemOrb extends EntityThrowable implements IEntityAdditionalSpawnData {
   int targetID = 0;
   EntityLivingBase target;
   public boolean red = false;

   public EntityGolemOrb(World par1World) {
      super(par1World);
   }

   public EntityGolemOrb(World par1World, EntityLivingBase par2EntityLiving, EntityLivingBase t, boolean r) {
      super(par1World, par2EntityLiving);
      this.target = t;
      this.red = r;
   }

   protected float func_70185_h() {
      return 0.0F;
   }

   public void writeSpawnData(ByteBuf data) {
      int id = -1;
      if (this.target != null) {
         id = this.target.func_145782_y();
      }

      data.writeInt(id);
      data.writeBoolean(this.red);
   }

   public void readSpawnData(ByteBuf data) {
      int id = data.readInt();

      try {
         if (id >= 0) {
            this.target = (EntityLivingBase)this.field_70170_p.func_73045_a(id);
         }
      } catch (Exception var4) {
      }

      this.red = data.readBoolean();
   }

   protected void func_70184_a(RayTraceResult mop) {
      if (!this.field_70170_p.field_72995_K && this.func_85052_h() != null && mop.field_72313_a == Type.ENTITY) {
         mop.field_72308_g.func_70097_a(DamageSource.func_76354_b(this, this.func_85052_h()), (float)this.func_85052_h().func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e() * (this.red ? 1.0F : 0.6F));
      }

      this.func_184185_a(SoundsTC.shock, 1.0F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
      if (this.field_70170_p.field_72995_K) {
         FXDispatcher.INSTANCE.burst(this.field_70165_t, this.field_70163_u, this.field_70161_v, 1.0F);
      }

      this.func_70106_y();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.field_70173_aa > (this.red ? 240 : 160)) {
         this.func_70106_y();
      }

      if (this.target != null) {
         double d = this.func_70068_e(this.target);
         double dx = this.target.field_70165_t - this.field_70165_t;
         double var10001 = (double)this.target.field_70131_O;
         double dy = this.target.func_174813_aQ().field_72338_b + var10001 * 0.6D - this.field_70163_u;
         double dz = this.target.field_70161_v - this.field_70161_v;
         double d13 = 0.2D;
         dx /= d;
         dy /= d;
         dz /= d;
         this.field_70159_w += dx * d13;
         this.field_70181_x += dy * d13;
         this.field_70179_y += dz * d13;
         this.field_70159_w = (double)MathHelper.func_76131_a((float)this.field_70159_w, -0.25F, 0.25F);
         this.field_70181_x = (double)MathHelper.func_76131_a((float)this.field_70181_x, -0.25F, 0.25F);
         this.field_70179_y = (double)MathHelper.func_76131_a((float)this.field_70179_y, -0.25F, 0.25F);
      }

   }

   public boolean func_70097_a(DamageSource p_70097_1_, float p_70097_2_) {
      if (this.func_180431_b(p_70097_1_)) {
         return false;
      } else {
         this.func_70018_K();
         if (p_70097_1_.func_76346_g() != null) {
            Vec3d vec3 = p_70097_1_.func_76346_g().func_70040_Z();
            if (vec3 != null) {
               this.field_70159_w = vec3.field_72450_a;
               this.field_70181_x = vec3.field_72448_b;
               this.field_70179_y = vec3.field_72449_c;
               this.field_70159_w *= 0.9D;
               this.field_70181_x *= 0.9D;
               this.field_70179_y *= 0.9D;
               this.func_184185_a(SoundsTC.zap, 1.0F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
