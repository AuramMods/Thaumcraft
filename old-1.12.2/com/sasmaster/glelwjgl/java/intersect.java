package com.sasmaster.glelwjgl.java;

public class intersect {
   public static final String VERSION = new String("$Id: intersect.java,v 1.1 1998/05/05 23:31:21 descarte Exp descarte $");
   private static final double DEGENERATE_TOLERANCE = 2.0E-6D;

   public static final int FIND_NON_DEGENERATE_POINT(int index, int npoints, double len, double[] diff, double[][] point_array) {
      double[] summa = null;
      int i = index;
      Object var12 = null;

      double slen;
      double tlen;
      do {
         double[] tdiff = matrix.VEC_DIFF(point_array[i + 1], point_array[i]);
         diff[0] = tdiff[0];
         diff[1] = tdiff[1];
         diff[2] = tdiff[2];
         tlen = matrix.VEC_LENGTH(diff);
         double[] summa = matrix.VEC_SUM(point_array[i + 1], point_array[i]);
         slen = matrix.VEC_LENGTH(summa);
         slen *= 2.0E-6D;
         ++i;
      } while(tlen <= slen && i < npoints - 1);

      return i;
   }

   public static final double[] INNERSECT(double[] p, double[] n, double[] v1, double[] v2) {
      double deno = 0.0D;
      double numer = 0.0D;
      double t = 0.0D;
      double omt = 0.0D;
      boolean valid = false;
      double[] sect = new double[3];
      deno = (v1[0] - v2[0]) * n[0];
      deno += (v1[1] - v2[1]) * n[1];
      deno += (v1[2] - v2[2]) * n[2];
      if (deno == 0.0D) {
         valid = false;
         n = matrix.VEC_COPY(v1);
      } else {
         valid = true;
         numer = (p[0] - v2[0]) * n[0];
         numer += (p[1] - v2[1]) * n[1];
         numer += (p[2] - v2[2]) * n[2];
         t = numer / deno;
         omt = 1.0D - t;
         if (1.0D < t * 2.0E-6D || -1.0D > t * 2.0E-6D) {
            valid = false;
         }

         sect[0] = t * v1[0] + omt * v2[0];
         sect[1] = t * v1[1] + omt * v2[1];
         sect[2] = t * v1[2] + omt * v2[2];
      }

      return sect;
   }

   public static final double[] bisecting_plane(double[] v1, double[] v2, double[] v3) {
      boolean valid = false;
      double[] v21 = null;
      double[] v32 = null;
      double[] n = new double[3];
      double[] v21 = matrix.VEC_DIFF(v2, v1);
      double[] v32 = matrix.VEC_DIFF(v3, v2);
      double len21 = matrix.VEC_LENGTH(v21);
      double len32 = matrix.VEC_LENGTH(v32);
      if (len21 <= 2.0E-6D * len32) {
         if (len32 == 0.0D) {
            n = matrix.VEC_ZERO();
            valid = false;
         } else {
            len32 = 1.0D / len32;
            n = matrix.VEC_SCALE(len32, v32);
            valid = true;
         }
      } else {
         valid = true;
         if (len32 <= 2.0E-6D * len21) {
            len21 = 1.0D / len21;
            n = matrix.VEC_SCALE(len21, v21);
         } else {
            len21 = 1.0D / len21;
            v21 = matrix.VEC_SCALE(len21, v21);
            len32 = 1.0D / len32;
            v32 = matrix.VEC_SCALE(len32, v32);
            double dot = matrix.VEC_DOT_PRODUCT(v32, v21);
            if (!(dot >= 0.999998D) && !(dot <= -0.999998D)) {
               n[0] = dot * (v32[0] + v21[0]) - v32[0] - v21[0];
               n[1] = dot * (v32[1] + v21[1]) - v32[1] - v21[1];
               n[2] = dot * (v32[2] + v21[2]) - v32[2] - v21[2];
               n = matrix.VEC_NORMALIZE(n);
            } else {
               n = matrix.VEC_COPY(v21);
            }
         }
      }

      return n;
   }

   public static final boolean CUTTING_PLANE(double[] n, double[] v1, double[] v2, double[] v3) {
      double[] v21 = new double[3];
      double[] v32 = new double[3];
      double len21 = 0.0D;
      double len32 = 0.0D;
      double lendiff = 0.0D;
      boolean valid = false;
      double[] vtmp = matrix.VEC_COPY(n);
      v21 = matrix.VEC_DIFF(v2, v1);
      v32 = matrix.VEC_DIFF(v3, v2);
      len21 = matrix.VEC_LENGTH(v21);
      len32 = matrix.VEC_LENGTH(v32);
      if (len21 <= 2.0E-6D * len32) {
         if (len32 == 0.0D) {
            vtmp = matrix.VEC_ZERO();
            valid = false;
         } else {
            len32 = 1.0D / len32;
            vtmp = matrix.VEC_SCALE(len32, v32);
            valid = true;
         }
      } else {
         valid = true;
         if (len32 <= 2.0E-6D * len21) {
            len21 = 1.0D / len21;
            vtmp = matrix.VEC_SCALE(len21, v21);
         } else {
            len21 = 1.0D / len21;
            v21 = matrix.VEC_SCALE(len21, v21);
            len32 = 1.0D / len32;
            v32 = matrix.VEC_SCALE(len32, v32);
            vtmp = matrix.VEC_DIFF(v21, v32);
            lendiff = matrix.VEC_LENGTH(vtmp);
            if (lendiff < 2.0E-6D) {
               vtmp = matrix.VEC_ZERO();
               valid = false;
            } else {
               lendiff = 1.0D / lendiff;
               vtmp = matrix.VEC_SCALE(lendiff, vtmp);
            }
         }
      }

      n[0] = vtmp[0];
      n[1] = vtmp[1];
      n[2] = vtmp[2];
      return valid;
   }

   public static boolean COLINEAR(double[] v1, double[] v2, double[] v3) {
      double[] v21 = new double[3];
      double[] v32 = new double[3];
      boolean rv = false;
      double len21 = 0.0D;
      double len32 = 0.0D;
      double dot = 0.0D;
      v21 = matrix.VEC_DIFF(v2, v1);
      v32 = matrix.VEC_DIFF(v3, v2);
      len21 = matrix.VEC_DOT_PRODUCT(v21, v21);
      len32 = matrix.VEC_DOT_PRODUCT(v32, v32);
      rv = len32 <= 2.0E-6D * len21;
      rv = rv || len21 <= 2.0E-6D * len32;
      dot = matrix.VEC_DOT_PRODUCT(v21, v32);
      rv = rv || len21 * len32 - dot * dot <= len21 * len32 * 2.0E-6D * 2.0E-6D;
      return rv;
   }
}
