package thaumcraft.common.tiles.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileBanner extends TileThaumcraft {
   private byte facing = 0;
   private byte color = -1;
   private Aspect aspect = null;
   private boolean onWall = false;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)(this.func_174877_v().func_177956_o() - 1), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 2), (double)(this.func_174877_v().func_177952_p() + 1));
   }

   public byte getBannerFacing() {
      return this.facing;
   }

   public void setBannerFacing(byte face) {
      this.facing = face;
      this.func_70296_d();
   }

   public boolean getWall() {
      return this.onWall;
   }

   public void setWall(boolean b) {
      this.onWall = b;
      this.func_70296_d();
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.facing = nbttagcompound.func_74771_c("facing");
      this.setColor(nbttagcompound.func_74771_c("color"));
      String as = nbttagcompound.func_74779_i("aspect");
      if (as != null && as.length() > 0) {
         this.setAspect(Aspect.getAspect(as));
      } else {
         this.aspect = null;
      }

      this.onWall = nbttagcompound.func_74767_n("wall");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74774_a("facing", this.facing);
      nbttagcompound.func_74774_a("color", this.getColor());
      nbttagcompound.func_74778_a("aspect", this.getAspect() == null ? "" : this.getAspect().getTag());
      nbttagcompound.func_74757_a("wall", this.onWall);
      return nbttagcompound;
   }

   public Aspect getAspect() {
      return this.aspect;
   }

   public void setAspect(Aspect aspect) {
      this.aspect = aspect;
   }

   public byte getColor() {
      return this.color;
   }

   public void setColor(byte color) {
      this.color = color;
   }
}
