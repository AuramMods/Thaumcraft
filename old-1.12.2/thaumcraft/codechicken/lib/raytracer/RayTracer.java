package thaumcraft.codechicken.lib.raytracer;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;

public class RayTracer {
   private Vector3 vec = new Vector3();
   private Vector3 vec2 = new Vector3();
   private Vector3 s_vec = new Vector3();
   private double s_dist;
   private int s_side;
   private IndexedCuboid6 c_cuboid;
   private static ThreadLocal<RayTracer> t_inst = new ThreadLocal();

   public static RayTracer instance() {
      RayTracer inst = (RayTracer)t_inst.get();
      if (inst == null) {
         t_inst.set(inst = new RayTracer());
      }

      return inst;
   }

   private void traceSide(int side, Vector3 start, Vector3 end, Cuboid6 cuboid) {
      this.vec.set(start);
      Vector3 hit = null;
      switch(side) {
      case 0:
         hit = this.vec.XZintercept(end, cuboid.min.y);
         break;
      case 1:
         hit = this.vec.XZintercept(end, cuboid.max.y);
         break;
      case 2:
         hit = this.vec.XYintercept(end, cuboid.min.z);
         break;
      case 3:
         hit = this.vec.XYintercept(end, cuboid.max.z);
         break;
      case 4:
         hit = this.vec.YZintercept(end, cuboid.min.x);
         break;
      case 5:
         hit = this.vec.YZintercept(end, cuboid.max.x);
      }

      if (hit != null) {
         switch(side) {
         case 0:
         case 1:
            if (MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) && MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) {
               break;
            }

            return;
         case 2:
         case 3:
            if (!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y)) {
               return;
            }
            break;
         case 4:
         case 5:
            if (!MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) {
               return;
            }
         }

         double dist = this.vec2.set(hit).subtract(start).magSquared();
         if (dist < this.s_dist) {
            this.s_side = side;
            this.s_dist = dist;
            this.s_vec.set(this.vec);
         }

      }
   }

   private boolean rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid) {
      this.s_dist = Double.MAX_VALUE;
      this.s_side = -1;

      for(int i = 0; i < 6; ++i) {
         this.traceSide(i, start, end, cuboid);
      }

      return this.s_side >= 0;
   }

   public ExtendedMOP rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, BlockCoord pos, Object data) {
      return this.rayTraceCuboid(start, end, cuboid) ? new ExtendedMOP(this.s_vec, this.s_side, pos, data, this.s_dist) : null;
   }

   public ExtendedMOP rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, Entity entity, Object data) {
      return this.rayTraceCuboid(start, end, cuboid) ? new ExtendedMOP(entity, this.s_vec, data, this.s_dist) : null;
   }

   public void rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockCoord pos, Block block, List<ExtendedMOP> hitList) {
      Iterator var7 = cuboids.iterator();

      while(var7.hasNext()) {
         IndexedCuboid6 cuboid = (IndexedCuboid6)var7.next();
         ExtendedMOP mop = this.rayTraceCuboid(start, end, cuboid, (BlockCoord)pos, cuboid.data);
         if (mop != null) {
            hitList.add(mop);
         }
      }

   }

   public static RayTraceResult retraceBlock(World world, EntityPlayer player, BlockPos pos) {
      IBlockState b = world.func_180495_p(pos);
      Vec3d headVec = getCorrectedHeadVec(player);
      Vec3d lookVec = player.func_70676_i(1.0F);
      double reach = getBlockReachDistance(player);
      Vec3d endVec = headVec.func_72441_c(lookVec.field_72450_a * reach, lookVec.field_72448_b * reach, lookVec.field_72449_c * reach);
      return b.func_185910_a(world, pos, headVec, endVec);
   }

   private static double getBlockReachDistance_server(EntityPlayerMP player) {
      return player.field_71134_c.getBlockReachDistance();
   }

   @SideOnly(Side.CLIENT)
   private static double getBlockReachDistance_client() {
      return (double)Minecraft.func_71410_x().field_71442_b.func_78757_d();
   }

   public static RayTraceResult retrace(EntityPlayer player) {
      return retrace(player, getBlockReachDistance(player));
   }

   public static RayTraceResult retrace(EntityPlayer player, double reach) {
      Vec3d headVec = getCorrectedHeadVec(player);
      Vec3d lookVec = player.func_70676_i(1.0F);
      Vec3d endVec = headVec.func_72441_c(lookVec.field_72450_a * reach, lookVec.field_72448_b * reach, lookVec.field_72449_c * reach);
      return player.field_70170_p.func_147447_a(headVec, endVec, true, false, true);
   }

   public static Vec3d getCorrectedHeadVec(EntityPlayer player) {
      Vector3 v = Vector3.fromEntity(player);
      if (player.field_70170_p.field_72995_K) {
         v.y += (double)player.func_70047_e();
      } else {
         v.y += (double)player.func_70047_e();
         if (player instanceof EntityPlayerMP && player.func_70093_af()) {
            v.y -= 0.08D;
         }
      }

      return v.vec3();
   }

   public static Vec3d getStartVec(EntityPlayer player) {
      return getCorrectedHeadVec(player);
   }

   public static double getBlockReachDistance(EntityPlayer player) {
      return player.field_70170_p.field_72995_K ? getBlockReachDistance_client() : (player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP)player) : 5.0D);
   }

   public static Vec3d getEndVec(EntityPlayer player) {
      Vec3d headVec = getCorrectedHeadVec(player);
      Vec3d lookVec = player.func_70676_i(1.0F);
      double reach = getBlockReachDistance(player);
      return headVec.func_72441_c(lookVec.field_72450_a * reach, lookVec.field_72448_b * reach, lookVec.field_72449_c * reach);
   }
}
