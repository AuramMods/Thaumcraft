package thaumcraft.common.tiles.devices;

import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Rotation;
import thaumcraft.codechicken.lib.vec.Transformation;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileRedstoneRelay extends TileThaumcraft {
   private int in = 1;
   private int out = 15;

   public void readSyncNBT(NBTTagCompound nbt) {
      this.setIn(nbt.func_74771_c("in"));
      this.setOut(nbt.func_74771_c("out"));
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
      nbt.func_74774_a("in", (byte)this.getIn());
      nbt.func_74774_a("out", (byte)this.getOut());
      return nbt;
   }

   public void increaseIn() {
      if (!this.field_145850_b.field_72995_K) {
         this.setIn(this.getIn() + 1);
         if (this.getIn() > 15) {
            this.setIn(1);
         }

         this.func_70296_d();
         this.syncTile(false);
      }

   }

   public void increaseOut() {
      if (!this.field_145850_b.field_72995_K) {
         this.setOut(this.getOut() + 1);
         if (this.getOut() > 15) {
            this.setOut(1);
         }

         this.func_70296_d();
         this.syncTile(false);
      }

   }

   public RayTraceResult rayTrace(World world, Vec3d vec3d, Vec3d vec3d1, RayTraceResult fullblock) {
      return fullblock;
   }

   public void addTraceableCuboids(List<IndexedCuboid6> cuboids) {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
      cuboids.add(new IndexedCuboid6(0, this.getCuboid0(facing)));
      cuboids.add(new IndexedCuboid6(1, this.getCuboid1(facing)));
   }

   public Cuboid6 getCuboid0(EnumFacing facing) {
      Transformation rot = Rotation.quarterRotations[0];
      switch(facing) {
      case WEST:
         rot = Rotation.quarterRotations[1];
         break;
      case NORTH:
         rot = Rotation.quarterRotations[2];
         break;
      case EAST:
         rot = Rotation.quarterRotations[3];
      }

      return (new Cuboid6(-0.375D, 0.0625D, -0.375D, -0.125D, 0.25D, -0.125D)).apply(rot).add(new Vector3((double)this.func_174877_v().func_177958_n() + 0.5D, (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p() + 0.5D));
   }

   public Cuboid6 getCuboid1(EnumFacing facing) {
      Transformation rot = Rotation.quarterRotations[0];
      switch(facing) {
      case WEST:
         rot = Rotation.quarterRotations[1];
         break;
      case NORTH:
         rot = Rotation.quarterRotations[2];
         break;
      case EAST:
         rot = Rotation.quarterRotations[3];
      }

      return (new Cuboid6(-0.125D, 0.0625D, 0.125D, 0.125D, 0.25D, 0.375D)).apply(rot).add(new Vector3((double)this.func_174877_v().func_177958_n() + 0.5D, (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p() + 0.5D));
   }

   public int getOut() {
      return this.out;
   }

   public void setOut(int out) {
      this.out = out;
   }

   public int getIn() {
      return this.in;
   }

   public void setIn(int in) {
      this.in = in;
   }
}
