package thaumcraft.codechicken.lib.render;

import thaumcraft.codechicken.lib.vec.Matrix4;
import thaumcraft.codechicken.lib.vec.Quat;
import thaumcraft.codechicken.lib.vec.Rotation;
import thaumcraft.codechicken.lib.vec.Scale;
import thaumcraft.codechicken.lib.vec.Transformation;
import thaumcraft.codechicken.lib.vec.Vector3;

public class CCModelLibrary {
   public static CCModel icosahedron4;
   public static CCModel icosahedron7;
   private static int i;

   private static void generateIcosahedron() {
      Vector3[] verts = new Vector3[]{new Vector3(-1.0D, 1.618033988749894D, 0.0D), new Vector3(1.0D, 1.618033988749894D, 0.0D), new Vector3(1.0D, -1.618033988749894D, 0.0D), new Vector3(-1.0D, -1.618033988749894D, 0.0D), new Vector3(0.0D, -1.0D, 1.618033988749894D), new Vector3(0.0D, 1.0D, 1.618033988749894D), new Vector3(0.0D, 1.0D, -1.618033988749894D), new Vector3(0.0D, -1.0D, -1.618033988749894D), new Vector3(1.618033988749894D, 0.0D, -1.0D), new Vector3(1.618033988749894D, 0.0D, 1.0D), new Vector3(-1.618033988749894D, 0.0D, 1.0D), new Vector3(-1.618033988749894D, 0.0D, -1.0D)};
      Quat quat = Quat.aroundAxis(0.0D, 0.0D, 1.0D, Math.atan(0.6180339887498951D));
      Vector3[] var2 = verts;
      int var3 = verts.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Vector3 vec = var2[var4];
         quat.rotate(vec);
      }

      icosahedron4 = CCModel.newModel(4, 60);
      icosahedron7 = CCModel.newModel(7, 80);
      i = 0;
      addIcosahedronTriangle(verts[1], 0.5D, 0.0D, verts[0], 0.0D, 0.25D, verts[5], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[1], 0.5D, 0.0D, verts[5], 0.0D, 0.25D, verts[9], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[1], 0.5D, 0.0D, verts[9], 0.0D, 0.25D, verts[8], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[1], 0.5D, 0.0D, verts[8], 0.0D, 0.25D, verts[6], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[1], 0.5D, 0.0D, verts[6], 0.0D, 0.25D, verts[0], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[0], 0.5D, 0.25D, verts[11], 0.0D, 0.75D, verts[10], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[5], 0.5D, 0.25D, verts[10], 0.0D, 0.75D, verts[4], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[9], 0.5D, 0.25D, verts[4], 0.0D, 0.75D, verts[2], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[8], 0.5D, 0.25D, verts[2], 0.0D, 0.75D, verts[7], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[6], 0.5D, 0.25D, verts[7], 0.0D, 0.75D, verts[11], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[2], 0.5D, 0.75D, verts[8], 0.0D, 0.25D, verts[9], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[7], 0.5D, 0.75D, verts[6], 0.0D, 0.25D, verts[8], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[11], 0.5D, 0.75D, verts[0], 0.0D, 0.25D, verts[6], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[10], 0.5D, 0.75D, verts[5], 0.0D, 0.25D, verts[0], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[4], 0.5D, 0.75D, verts[9], 0.0D, 0.25D, verts[5], 1.0D, 0.25D);
      addIcosahedronTriangle(verts[3], 0.5D, 1.0D, verts[2], 0.0D, 0.75D, verts[4], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[3], 0.5D, 1.0D, verts[7], 0.0D, 0.75D, verts[2], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[3], 0.5D, 1.0D, verts[11], 0.0D, 0.75D, verts[7], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[3], 0.5D, 1.0D, verts[10], 0.0D, 0.75D, verts[11], 1.0D, 0.75D);
      addIcosahedronTriangle(verts[3], 0.5D, 1.0D, verts[4], 0.0D, 0.75D, verts[10], 1.0D, 0.75D);
      icosahedron4.computeNormals().smoothNormals();
      icosahedron7.computeNormals().smoothNormals();
   }

   private static void addIcosahedronTriangle(Vector3 vec1, double u1, double v1, Vector3 vec2, double u2, double v2, Vector3 vec3, double u3, double v3) {
      icosahedron4.verts[i * 3] = icosahedron7.verts[i * 4] = new Vertex5(vec1, u1, v1);
      icosahedron4.verts[i * 3 + 1] = icosahedron7.verts[i * 4 + 1] = new Vertex5(vec2, u2, v2);
      icosahedron4.verts[i * 3 + 2] = icosahedron7.verts[i * 4 + 2] = icosahedron7.verts[i * 4 + 3] = new Vertex5(vec3, u3, v3);
      ++i;
   }

   public static Matrix4 getRenderMatrix(Vector3 position, Rotation rotation, double scale) {
      return (new Matrix4()).translate(position).apply((Transformation)(new Scale(scale))).apply((Transformation)rotation);
   }

   static {
      generateIcosahedron();
   }
}
