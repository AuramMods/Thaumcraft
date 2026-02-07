package thaumcraft.codechicken.lib.vec;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class VariableTransformation extends Transformation {
   public Matrix4 mat;

   public VariableTransformation(Matrix4 mat) {
      this.mat = mat;
   }

   public void applyN(Vector3 normal) {
      this.apply(normal);
   }

   public void apply(Matrix4 mat) {
      mat.multiply(this.mat);
   }

   @SideOnly(Side.CLIENT)
   public void glApply() {
      this.mat.glApply();
   }
}
