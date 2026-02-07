package thaumcraft.codechicken.lib.vec;

public class SwapYZ extends VariableTransformation {
   public SwapYZ() {
      super(new Matrix4(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D));
   }

   public void apply(Vector3 vec) {
      double vz = vec.z;
      vec.z = vec.y;
      vec.y = vz;
   }

   public Transformation inverse() {
      return this;
   }
}
