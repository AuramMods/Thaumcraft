package thaumcraft.common.entities.projectile;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityGrapple extends EntityThrowable implements IEntityAdditionalSpawnData {
   private int potency;
   public boolean sticky = false;
   EntityLivingBase cthrower;
   boolean p = false;
   boolean boost;
   int prevDist = 0;
   int count = 0;
   boolean added = false;
   public float ampl = 0.0F;
   private static HashMap<Integer, Integer> grapples = new HashMap();

   public EntityGrapple(World par1World) {
      super(par1World);
      this.func_70105_a(0.1F, 0.1F);
   }

   public void func_70186_c(double x, double y, double z, float velocity, float inaccuracy) {
      super.func_70186_c(x, y, z, velocity, 0.0F);
   }

   public EntityGrapple(World par1World, EntityLivingBase par2EntityLiving, int potency, boolean sticky) {
      super(par1World, par2EntityLiving);
      this.potency = potency;
      this.sticky = sticky;
      this.func_70105_a(0.1F, 0.1F);
   }

   public EntityGrapple(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
      this.func_70105_a(0.1F, 0.1F);
   }

   public void writeSpawnData(ByteBuf data) {
      int id = -1;
      if (this.func_85052_h() != null) {
         id = this.func_85052_h().func_145782_y();
      }

      data.writeInt(id);
      data.writeByte(this.potency);
      data.writeBoolean(this.sticky);
   }

   public void readSpawnData(ByteBuf data) {
      int id = data.readInt();
      this.potency = data.readByte();
      this.sticky = data.readBoolean();

      try {
         if (id >= 0) {
            this.cthrower = (EntityLivingBase)this.field_70170_p.func_73045_a(id);
         }
      } catch (Exception var4) {
      }

   }

   public EntityLivingBase func_85052_h() {
      return this.cthrower != null ? this.cthrower : super.func_85052_h();
   }

   protected float func_70185_h() {
      return this.getPulling() ? 0.0F : 0.03F;
   }

   public void func_70088_a() {
      super.func_70088_a();
   }

   public void setPulling() {
      this.p = true;
   }

   public boolean getPulling() {
      return this.p;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.getPulling() && !this.field_70128_L && (this.field_70173_aa > 10 + this.potency * 5 || this.func_85052_h() == null)) {
         this.func_70106_y();
      }

      if (this.func_85052_h() != null) {
         if (!this.field_70170_p.field_72995_K && !this.field_70128_L && !this.added) {
            if (grapples.containsKey(this.func_85052_h().func_145782_y())) {
               int ii = (Integer)grapples.get(this.func_85052_h().func_145782_y());
               if (ii != this.func_145782_y()) {
                  Entity e = this.field_70170_p.func_73045_a(ii);
                  if (e != null) {
                     e.func_70106_y();
                  }
               }
            }

            grapples.put(this.func_85052_h().func_145782_y(), this.func_145782_y());
            this.added = true;
         }

         double dis = (double)this.func_85052_h().func_70032_d(this);
         if (this.getPulling() && !this.field_70128_L) {
            if ((this.sticky || !(dis < 2.0D)) && !this.func_85052_h().func_70093_af() && (this.sticky || this.count <= 20)) {
               if (!this.field_70170_p.field_72995_K && this.func_85052_h() instanceof EntityPlayerMP) {
                  ((EntityPlayerMP)this.func_85052_h()).field_71135_a.field_147365_f = 0;
               }

               this.func_85052_h().field_70143_R = 0.0F;
               double mx = this.field_70165_t - this.func_85052_h().field_70165_t;
               double my = this.field_70163_u - this.func_85052_h().field_70163_u;
               double mz = this.field_70161_v - this.func_85052_h().field_70161_v;
               double dd = dis;
               if (this.sticky && dis < 8.0D) {
                  dd = dis * (8.0D - dis);
               }

               mx /= dd * 5.0D;
               my /= dd * 5.0D;
               mz /= dd * 5.0D;
               Vec3d v2 = new Vec3d(mx, my, mz);
               if (v2.func_72433_c() > 0.25D) {
                  v2 = v2.func_72432_b();
                  mx = v2.field_72450_a / 4.0D;
                  my = v2.field_72448_b / 4.0D;
                  mz = v2.field_72449_c / 4.0D;
               }

               EntityLivingBase var10000 = this.func_85052_h();
               var10000.field_70159_w += mx;
               var10000 = this.func_85052_h();
               var10000.field_70181_x += my + 0.033D;
               var10000 = this.func_85052_h();
               var10000.field_70179_y += mz;
               if (!this.boost) {
                  var10000 = this.func_85052_h();
                  var10000.field_70181_x += 0.4000000059604645D;
                  this.boost = true;
               }

               int d = (int)(dis / 2.0D);
               if (d == this.prevDist) {
                  ++this.count;
               } else {
                  this.count = 0;
               }

               this.prevDist = d;
            } else {
               this.func_70106_y();
            }
         }

         if (this.field_70170_p.field_72995_K) {
            if (!this.getPulling()) {
               this.ampl += 0.02F;
            } else {
               this.ampl *= 0.66F;
            }
         }
      }

   }

   protected void func_70184_a(RayTraceResult mop) {
      this.setPulling();
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.field_70165_t = mop.field_72307_f.field_72450_a;
      this.field_70163_u = mop.field_72307_f.field_72448_b;
      this.field_70161_v = mop.field_72307_f.field_72449_c;
   }
}
