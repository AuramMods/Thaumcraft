package thaumcraft.codechicken.lib.raytracer;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Vector3;

public class ExtendedMOP extends RayTraceResult implements Comparable<ExtendedMOP> {
   public double dist;

   public ExtendedMOP(Entity entity, Vector3 hit, Object data, double dist) {
      super(entity, hit.vec3());
      this.setData(data);
      this.dist = dist;
   }

   public ExtendedMOP(Vector3 hit, int side, BlockCoord pos, Object data, double dist) {
      super(hit.vec3(), side >= 0 ? EnumFacing.values()[side] : EnumFacing.UP, pos.pos());
      this.setData(data);
      this.dist = dist;
   }

   public void setData(Object data) {
      if (data instanceof Integer) {
         this.subHit = (Integer)data;
      }

      this.hitInfo = data;
   }

   public int compareTo(ExtendedMOP o) {
      return this.dist == o.dist ? 0 : (this.dist < o.dist ? -1 : 1);
   }
}
