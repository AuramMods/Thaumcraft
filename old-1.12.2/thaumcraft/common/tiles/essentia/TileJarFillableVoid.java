package thaumcraft.common.tiles.essentia;

import net.minecraft.util.EnumFacing;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aura.AuraHelper;

public class TileJarFillableVoid extends TileJarFillable {
   int count = 0;

   public int addToContainer(Aspect tt, int am) {
      boolean up = this.amount < this.maxAmount;
      if (am == 0) {
         return am;
      } else {
         if (tt == this.aspect || this.amount == 0) {
            this.aspect = tt;
            this.amount += am;
            am = 0;
            if (this.amount > this.maxAmount) {
               if (this.field_145850_b.field_73012_v.nextInt(this.maxAmount) == 0) {
                  AuraHelper.polluteAura(this.func_145831_w(), this.func_174877_v(), 1.0F, true);
               }

               this.amount = this.maxAmount;
            }
         }

         if (up) {
            this.syncTile(false);
            this.func_70296_d();
         }

         return am;
      }
   }

   public int getMinimumSuction() {
      return this.aspectFilter != null ? 48 : 32;
   }

   public int getSuctionAmount(EnumFacing loc) {
      return this.aspectFilter != null && this.amount < this.maxAmount ? 48 : 32;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && ++this.count % 5 == 0) {
         this.fillJar();
      }

   }
}
