package thaumcraft.common.tiles.essentia;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileEssentiaInput extends TileThaumcraft implements IEssentiaTransport, ITickable {
   int count = 0;

   public boolean isConnectable(EnumFacing face) {
      return face == this.getFacing().func_176734_d();
   }

   public boolean canInputFrom(EnumFacing face) {
      return face == this.getFacing().func_176734_d();
   }

   public boolean canOutputTo(EnumFacing face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(EnumFacing loc) {
      return null;
   }

   public int getSuctionAmount(EnumFacing loc) {
      return 128;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return null;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
      return amount;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && ++this.count % 5 == 0) {
         this.fillJar();
      }

   }

   void fillJar() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.func_174877_v(), this.getFacing().func_176734_d());
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(this.getFacing())) {
            return;
         }

         if (ic.getEssentiaAmount(this.getFacing()) > 0 && ic.getSuctionAmount(this.getFacing()) < this.getSuctionAmount(this.getFacing().func_176734_d()) && this.getSuctionAmount(this.getFacing().func_176734_d()) >= ic.getMinimumSuction()) {
            Aspect ta = ic.getEssentiaType(this.getFacing());
            if (EssentiaHandler.addEssentia(this, ta, this.getFacing(), 16, false, 5)) {
               ic.takeEssentia(ta, 1, this.getFacing());
            }
         }
      }

   }
}
