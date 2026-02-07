package thaumcraft.codechicken.lib.vec;

public class Rectangle4i {
   public int x;
   public int y;
   public int w;
   public int h;

   public Rectangle4i() {
   }

   public Rectangle4i(int x, int y, int w, int h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
   }

   public int x1() {
      return this.x;
   }

   public int y1() {
      return this.y;
   }

   public int x2() {
      return this.x + this.w - 1;
   }

   public int y2() {
      return this.y + this.h - 1;
   }

   public void set(int x, int y, int w, int h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
   }

   public Rectangle4i offset(int dx, int dy) {
      this.x += dx;
      this.y += dy;
      return this;
   }

   public Rectangle4i include(int px, int py) {
      if (px < this.x) {
         this.expand(px - this.x, 0);
      }

      if (px >= this.x + this.w) {
         this.expand(px - this.x - this.w + 1, 0);
      }

      if (py < this.y) {
         this.expand(0, py - this.y);
      }

      if (py >= this.y + this.h) {
         this.expand(0, py - this.y - this.h + 1);
      }

      return this;
   }

   public Rectangle4i include(Rectangle4i r) {
      this.include(r.x, r.y);
      return this.include(r.x2(), r.y2());
   }

   public Rectangle4i expand(int px, int py) {
      if (px > 0) {
         this.w += px;
      } else {
         this.x += px;
         this.w -= px;
      }

      if (py > 0) {
         this.h += py;
      } else {
         this.y += py;
         this.h -= py;
      }

      return this;
   }

   public boolean contains(int px, int py) {
      return this.x <= px && px < this.x + this.w && this.y <= py && py < this.y + this.h;
   }

   public boolean intersects(Rectangle4i r) {
      return r.x + r.w > this.x && r.x < this.x + this.w && r.y + r.h > this.y && r.y < this.y + this.h;
   }

   public int area() {
      return this.w * this.h;
   }
}
