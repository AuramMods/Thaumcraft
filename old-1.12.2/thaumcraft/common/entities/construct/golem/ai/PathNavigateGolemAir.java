package thaumcraft.common.entities.construct.golem.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class PathNavigateGolemAir extends PathNavigate {
   public PathNavigateGolemAir(EntityLiving p_i45873_1_, World worldIn) {
      super(p_i45873_1_, worldIn);
   }

   protected PathFinder func_179679_a() {
      return new PathFinder(new FlightNodeProcessor());
   }

   protected boolean func_75485_k() {
      return true;
   }

   protected Vec3d func_75502_i() {
      return new Vec3d(this.field_75515_a.field_70165_t, this.field_75515_a.field_70163_u + (double)this.field_75515_a.field_70131_O * 0.5D, this.field_75515_a.field_70161_v);
   }

   protected void func_75508_h() {
      Vec3d vec3 = this.func_75502_i();
      float f = this.field_75515_a.field_70130_N * this.field_75515_a.field_70130_N;
      byte b0 = 6;
      if (vec3.func_72436_e(this.field_75514_c.func_75881_a(this.field_75515_a, this.field_75514_c.func_75873_e())) < (double)f) {
         this.field_75514_c.func_75875_a();
      }

      for(int i = Math.min(this.field_75514_c.func_75873_e() + b0, this.field_75514_c.func_75874_d() - 1); i > this.field_75514_c.func_75873_e(); --i) {
         Vec3d vec31 = this.field_75514_c.func_75881_a(this.field_75515_a, i);
         if (vec31.func_72436_e(vec3) <= 36.0D && this.func_75493_a(vec3, vec31, 0, 0, 0)) {
            this.field_75514_c.func_75872_c(i);
            break;
         }
      }

      this.func_179677_a(vec3);
   }

   protected void func_75487_m() {
      super.func_75487_m();
   }

   protected boolean func_75493_a(Vec3d p_75493_1_, Vec3d p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_) {
      RayTraceResult RayTraceResult = this.field_75513_b.func_147447_a(p_75493_1_, new Vec3d(p_75493_2_.field_72450_a, p_75493_2_.field_72448_b + (double)this.field_75515_a.field_70131_O * 0.5D, p_75493_2_.field_72449_c), false, true, false);
      return RayTraceResult == null || RayTraceResult.field_72313_a == Type.MISS;
   }
}
