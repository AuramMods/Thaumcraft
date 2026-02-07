package thaumcraft.common.lib.utils;

public class PosXY implements Comparable {
   public int x;
   public int y;

   public PosXY() {
   }

   public PosXY(int x, int z) {
      this.x = x;
      this.y = z;
   }

   public PosXY(PosXY c) {
      this.x = c.x;
      this.y = c.y;
   }

   public boolean equals(Object o) {
      if (!(o instanceof PosXY)) {
         return false;
      } else {
         PosXY chunkcoordinates = (PosXY)o;
         return this.x == chunkcoordinates.x && this.y == chunkcoordinates.y;
      }
   }

   public int hashCode() {
      return this.x + this.y << 8;
   }

   public int compareTo(PosXY c) {
      return this.y == c.y ? this.x - c.x : this.y - c.y;
   }

   public void set(int x, int z) {
      this.x = x;
      this.y = z;
   }

   public float getDistanceSquared(int x, int z) {
      float f = (float)(this.x - x);
      float f2 = (float)(this.y - z);
      return f * f + f2 * f2;
   }

   public float getDistanceSquaredToChunkCoordinates(PosXY c) {
      return this.getDistanceSquared(c.x, c.y);
   }

   public String toString() {
      return "Pos{x=" + this.x + ", y=" + this.y + '}';
   }

   public int compareTo(Object o) {
      return this.compareTo((PosXY)o);
   }
}
