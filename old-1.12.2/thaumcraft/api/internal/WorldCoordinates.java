package thaumcraft.api.internal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class WorldCoordinates implements Comparable {
   public BlockPos pos;
   public int dim;

   public WorldCoordinates() {
   }

   public WorldCoordinates(BlockPos pos, int d) {
      this.pos = pos;
      this.dim = d;
   }

   public WorldCoordinates(TileEntity tile) {
      this.pos = tile.func_174877_v();
      this.dim = tile.func_145831_w().field_73011_w.getDimension();
   }

   public WorldCoordinates(WorldCoordinates par1ChunkCoordinates) {
      this.pos = par1ChunkCoordinates.pos;
      this.dim = par1ChunkCoordinates.dim;
   }

   public boolean equals(Object par1Obj) {
      if (!(par1Obj instanceof WorldCoordinates)) {
         return false;
      } else {
         WorldCoordinates coordinates = (WorldCoordinates)par1Obj;
         return this.pos.equals(coordinates.pos) && this.dim == coordinates.dim;
      }
   }

   public int hashCode() {
      return this.pos.func_177958_n() + this.pos.func_177956_o() << 8 + this.pos.func_177952_p() << 16 + this.dim << 24;
   }

   public int compareWorldCoordinate(WorldCoordinates par1) {
      return this.dim == par1.dim ? this.pos.compareTo(par1.pos) : -1;
   }

   public void set(BlockPos pos, int d) {
      this.pos = pos;
      this.dim = d;
   }

   public double getDistanceSquared(BlockPos pos) {
      return this.pos.func_177951_i(pos);
   }

   public double getDistanceSquaredToWorldCoordinates(WorldCoordinates par1ChunkCoordinates) {
      return this.getDistanceSquared(par1ChunkCoordinates.pos);
   }

   public int compareTo(Object par1Obj) {
      return this.compareWorldCoordinate((WorldCoordinates)par1Obj);
   }

   public void readNBT(NBTTagCompound nbt) {
      int x = nbt.func_74762_e("w_x");
      int y = nbt.func_74762_e("w_y");
      int z = nbt.func_74762_e("w_z");
      this.pos = new BlockPos(x, y, z);
      this.dim = nbt.func_74762_e("w_d");
   }

   public void writeNBT(NBTTagCompound nbt) {
      nbt.func_74768_a("w_x", this.pos.func_177958_n());
      nbt.func_74768_a("w_y", this.pos.func_177956_o());
      nbt.func_74768_a("w_z", this.pos.func_177952_p());
      nbt.func_74768_a("w_d", this.dim);
   }
}
