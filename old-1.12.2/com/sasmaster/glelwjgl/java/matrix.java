package com.sasmaster.glelwjgl.java;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class matrix {
   public static final String VERSION = new String("$Id: matrix.java,v 1.2 1998/05/05 23:31:09 descarte Exp descarte $");

   private static final double[][] ROTX_CS(double cosine, double sine) {
      double[][] m = new double[4][4];
      m[0][0] = 1.0D;
      m[0][1] = 0.0D;
      m[0][2] = 0.0D;
      m[0][3] = 0.0D;
      m[1][0] = 0.0D;
      m[1][1] = cosine;
      m[1][2] = sine;
      m[1][3] = 0.0D;
      m[2][0] = 0.0D;
      m[2][1] = -sine;
      m[2][2] = cosine;
      m[2][3] = 0.0D;
      m[3][0] = 0.0D;
      m[3][1] = 0.0D;
      m[3][2] = 0.0D;
      m[3][3] = 1.0D;
      return m;
   }

   private static final double[][] ROTY_CS(double cosine, double sine) {
      double[][] m = new double[4][4];
      m[0][0] = cosine;
      m[0][1] = 0.0D;
      m[0][2] = -sine;
      m[0][3] = 0.0D;
      m[1][0] = 0.0D;
      m[1][1] = 1.0D;
      m[1][2] = 0.0D;
      m[1][3] = 0.0D;
      m[2][0] = sine;
      m[2][1] = 0.0D;
      m[2][2] = cosine;
      m[2][3] = 0.0D;
      m[3][0] = 0.0D;
      m[3][1] = 0.0D;
      m[3][2] = 0.0D;
      m[3][3] = 1.0D;
      return m;
   }

   private static final double[][] ROTZ_CS(double cosine, double sine) {
      double[][] m = new double[4][4];
      m[0][0] = cosine;
      m[0][1] = sine;
      m[0][2] = 0.0D;
      m[0][3] = 0.0D;
      m[1][0] = -sine;
      m[1][1] = cosine;
      m[1][2] = 0.0D;
      m[1][3] = 0.0D;
      m[2][0] = 0.0D;
      m[2][1] = 0.0D;
      m[2][2] = 1.0D;
      m[2][3] = 0.0D;
      m[3][0] = 0.0D;
      m[3][1] = 0.0D;
      m[3][2] = 0.0D;
      m[3][3] = 1.0D;
      return m;
   }

   private static DoubleBuffer getBufferedMatrix(double[][] m) {
      DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
      mbuffer.put(new double[]{m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3]});
      mbuffer.flip();
      return mbuffer;
   }

   public static final double[][] urotx_cs_d(double cosine, double sine) {
      return ROTX_CS(cosine, sine);
   }

   public static final void rotx_cs_d(double cosine, double sine) {
      GL11.glMultMatrix(getBufferedMatrix(urotx_cs_d(cosine, sine)));
   }

   public static final double[][] uroty_cs_d(double cosine, double sine) {
      return ROTX_CS(cosine, sine);
   }

   public static final void roty_cs_d(double cosine, double sine) {
      GL11.glMultMatrix(getBufferedMatrix(uroty_cs_d(cosine, sine)));
   }

   public static final double[][] urotz_cs_d(double cosine, double sine) {
      return ROTX_CS(cosine, sine);
   }

   public static final void rotz_cs_d(double cosine, double sine) {
      GL11.glMultMatrix(getBufferedMatrix(urotz_cs_d(cosine, sine)));
   }

   public static final double[][] urot_cs_d(double cosine, double sine, char axis) {
      switch(axis) {
      case 'X':
      case 'x':
         return urotx_cs_d(cosine, sine);
      case 'Y':
      case 'y':
         return uroty_cs_d(cosine, sine);
      case 'Z':
      case 'z':
         return urotz_cs_d(cosine, sine);
      default:
         return (double[][])null;
      }
   }

   public static final void rot_cs_d(double cosine, double sine, char axis) {
      GL11.glMultMatrix(getBufferedMatrix(urot_cs_d(cosine, sine, axis)));
   }

   public static final double[][] urot_prince_d(double theta, char axis) {
      return urot_cs_d(Math.cos(theta), Math.sin(theta), axis);
   }

   public static final void rot_prince_d(double theta, char axis) {
      GL11.glMultMatrix(getBufferedMatrix(urot_prince_d(theta, axis)));
   }

   public static final void rot_axis_d(double omega, double[] axis) {
      GL11.glMultMatrix(getBufferedMatrix(urot_axis_d(omega, axis)));
   }

   public static final void rot_about_axis_d(double angle, double[] axis) {
      GL11.glMultMatrix(getBufferedMatrix(urot_about_axis_d(angle, axis)));
   }

   public static final void rot_omega_d(double[] axis) {
      GL11.glMultMatrix(getBufferedMatrix(urot_omega_d(axis)));
   }

   public static final double[][] urot_axis_d(double omega, double[] axis) {
      double[][] m = new double[4][4];
      if (axis.length != 3) {
         throw new GLEException("Length of axis parameter != 3. This is not a valid vector!");
      } else {
         double tmp = omega / 2.0D;
         double s = Math.sin(tmp);
         double c = Math.cos(tmp);
         double ssq = s * s;
         double csq = c * c;
         m[0][0] = m[1][1] = m[2][2] = csq - ssq;
         ssq *= 2.0D;
         m[0][0] += ssq * axis[0] * axis[0];
         m[1][1] += ssq * axis[1] * axis[1];
         m[2][2] += ssq * axis[2] * axis[2];
         m[0][1] = m[1][0] = axis[0] * axis[1] * ssq;
         m[1][2] = m[2][1] = axis[1] * axis[2] * ssq;
         m[2][0] = m[0][2] = axis[2] * axis[0] * ssq;
         double cts = 2.0D * c * s;
         tmp = cts * axis[2];
         m[0][1] += tmp;
         m[1][0] -= tmp;
         tmp = cts * axis[0];
         m[1][2] += tmp;
         m[2][1] -= tmp;
         tmp = cts * axis[1];
         m[2][0] += tmp;
         m[0][2] -= tmp;
         m[0][3] = m[1][3] = m[2][3] = m[3][2] = m[3][1] = m[3][0] = 0.0D;
         m[3][3] = 1.0D;
         return m;
      }
   }

   public static final double[][] urot_about_axis_d(double angle, double[] axis) {
      double[][] m = (double[][])null;
      double[] ax = new double[3];
      if (axis.length != 3) {
         throw new GLEException("Length of axis parameter != 3. This is not a valid vector!");
      } else {
         double ang = angle * 0.017453292519943295D;
         double len = axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2];
         if (len != 1.0D) {
            len = 1.0D / Math.sqrt(len);
            ax[0] = axis[0] * len;
            ax[1] = axis[1] * len;
            ax[2] = axis[2] * len;
            m = urot_axis_d(ang, ax);
         } else {
            m = urot_axis_d(ang, axis);
         }

         return m;
      }
   }

   public static final double[][] urot_omega_d(double[] axis) {
      double[][] m = (double[][])null;
      double[] ax = new double[3];
      double len = axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2];
      len = 1.0D / Math.sqrt(len);
      ax[0] = axis[0] * len;
      ax[1] = axis[1] * len;
      ax[2] = axis[2] * len;
      return urot_axis_d(len, ax);
   }

   public static final double[] VEC_ZERO() {
      double[] vtmp = new double[3];
      vtmp[0] = vtmp[1] = vtmp[2] = 0.0D;
      return vtmp;
   }

   public static final double[] VEC_NORMALIZE(double[] v) {
      double[] vtmp = new double[3];
      double vlen = VEC_LENGTH(v);
      if (vlen != 0.0D) {
         vlen = 1.0D / vlen;
         vtmp[0] = v[0] * vlen;
         vtmp[1] = v[1] * vlen;
         vtmp[2] = v[2] * vlen;
      }

      return vtmp;
   }

   public static final double[] VEC_REFLECT(double[] v, double[] n) {
      double[] vtmp = new double[3];
      double dot = VEC_DOT_PRODUCT(v, n);
      vtmp[0] = v[0] - 2.0D * dot * n[0];
      vtmp[1] = v[1] - 2.0D * dot * n[1];
      vtmp[2] = v[2] - 2.0D * dot * n[2];
      return vtmp;
   }

   public static final double[] VEC_COPY_2(double[] v) {
      double[] vtmp = new double[]{v[0], v[1], 0.0D};
      return vtmp;
   }

   public static final double[] VEC_COPY(double[] v) {
      double[] vtmp = new double[]{v[0], v[1], v[2]};
      return vtmp;
   }

   public static final double VEC_LENGTH_2(double[] v) {
      double length = v[0] * v[0] + v[1] * v[1];
      return length;
   }

   public static final double VEC_LENGTH(double[] v) {
      double length = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
      return length;
   }

   public static final double[] VEC_SCALE(double scale, double[] v) {
      double[] vtmp = new double[]{scale * v[0], scale * v[1], scale * v[2]};
      return vtmp;
   }

   public static final double[] VEC_CROSS_PRODUCT(double[] v1, double[] v2) {
      double[] vtmp = new double[]{v1[1] * v2[2] - v1[2] * v2[1], v1[2] * v2[0] - v1[0] * v2[2], v1[0] * v2[1] - v1[1] * v2[0]};
      return vtmp;
   }

   public static final double VEC_DOT_PRODUCT(double[] v1, double[] v2) {
      double dot = 0.0D;
      if (v1.length == 3 && v2.length == 3) {
         dot = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
         return dot;
      } else {
         throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
      }
   }

   public static final double[] VEC_PERP(double[] v, double[] n) {
      double[] vtmp = new double[3];
      double dot = VEC_DOT_PRODUCT(v, n);
      if (v.length == 3 && n.length == 3) {
         vtmp[0] = v[0] - dot * n[0];
         vtmp[1] = v[1] - dot * n[1];
         vtmp[2] = v[2] - dot * n[2];
         return vtmp;
      } else {
         throw new GLEException("Length of v or n !=3. Invalid vectors!");
      }
   }

   public static final double[] VEC_DIFF(double[] v2, double[] v1) {
      double[] vtmp = new double[3];
      if (v1.length == 3 && v2.length == 3) {
         vtmp[0] = v2[0] - v1[0];
         vtmp[1] = v2[1] - v1[1];
         vtmp[2] = v2[2] - v1[2];
         return vtmp;
      } else {
         throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
      }
   }

   public static final double[] VEC_SUM(double[] v1, double[] v2) {
      double[] vtmp = new double[3];
      if (v1.length == 3 && v2.length == 3) {
         vtmp[0] = v2[0] + v1[0];
         vtmp[1] = v2[1] + v1[1];
         vtmp[2] = v2[2] + v1[2];
         return vtmp;
      } else {
         throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
      }
   }

   public static final double[][] IDENTIFY_MATRIX_3X3() {
      double[][] m = new double[3][3];
      m[0][0] = 1.0D;
      m[0][1] = 0.0D;
      m[0][2] = 0.0D;
      m[1][0] = 0.0D;
      m[1][1] = 1.0D;
      m[1][2] = 0.0D;
      m[2][0] = 0.0D;
      m[2][1] = 0.0D;
      m[2][2] = 1.0D;
      return m;
   }

   public static final double[][] IDENTIFY_MATRIX_4X4() {
      double[][] m = new double[4][4];
      m[0][0] = 1.0D;
      m[0][1] = 0.0D;
      m[0][2] = 0.0D;
      m[0][3] = 0.0D;
      m[1][0] = 0.0D;
      m[1][1] = 1.0D;
      m[1][2] = 0.0D;
      m[1][3] = 0.0D;
      m[2][0] = 0.0D;
      m[2][1] = 0.0D;
      m[2][2] = 1.0D;
      m[2][3] = 0.0D;
      m[3][0] = 0.0D;
      m[3][1] = 0.0D;
      m[3][2] = 0.0D;
      m[3][3] = 1.0D;
      return m;
   }

   public static final double[][] COPY_MATRIX_2X2(double[][] a) {
      double[][] b = new double[2][2];
      b[0][0] = a[0][0];
      b[0][1] = a[0][1];
      b[1][0] = a[1][0];
      b[1][1] = a[1][1];
      return b;
   }

   public static final double[][] COPY_MATRIX_2X3(double[][] a) {
      double[][] b = new double[2][3];
      b[0][0] = a[0][0];
      b[0][1] = a[0][1];
      b[0][2] = a[0][2];
      b[1][0] = a[1][0];
      b[1][1] = a[1][1];
      b[1][2] = a[1][2];
      return b;
   }

   public static final double[][] COPY_MATRIX_4X4(double[][] a) {
      double[][] b = new double[4][4];
      b[0][0] = a[0][0];
      b[0][1] = a[0][1];
      b[0][2] = a[0][2];
      b[0][3] = a[0][3];
      b[1][0] = a[1][0];
      b[1][1] = a[1][1];
      b[1][2] = a[1][2];
      b[1][3] = a[1][3];
      b[2][0] = a[2][0];
      b[2][1] = a[2][1];
      b[2][2] = a[2][2];
      b[2][3] = a[2][3];
      b[3][0] = a[3][0];
      b[3][1] = a[3][1];
      b[3][2] = a[3][2];
      b[3][3] = a[3][3];
      return b;
   }

   public static final double[][] MATRIX_PRODUCT_2X2(double[][] a, double[][] b) {
      double[][] c = new double[2][2];
      c[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
      c[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
      c[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
      c[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
      return c;
   }

   public static final double[][] MATRIX_PRODUCT_4X4(double[][] a, double[][] b) {
      double[][] c = new double[4][4];
      c[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0] + a[0][2] * b[2][0] + a[0][3] * b[3][0];
      c[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1] + a[0][2] * b[2][1] + a[0][3] * b[3][1];
      c[0][2] = a[0][0] * b[0][2] + a[0][1] * b[1][2] + a[0][2] * b[2][2] + a[0][3] * b[3][2];
      c[0][3] = a[0][0] * b[0][3] + a[0][1] * b[1][3] + a[0][2] * b[2][3] + a[0][3] * b[3][3];
      c[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0] + a[1][2] * b[2][0] + a[1][3] * b[3][0];
      c[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1] + a[1][2] * b[2][1] + a[1][3] * b[3][1];
      c[1][2] = a[1][0] * b[0][2] + a[1][1] * b[1][2] + a[1][2] * b[2][2] + a[1][3] * b[3][2];
      c[1][3] = a[1][0] * b[0][3] + a[1][1] * b[1][3] + a[1][2] * b[2][3] + a[1][3] * b[3][3];
      c[2][0] = a[2][0] * b[0][0] + a[2][1] * b[1][0] + a[2][2] * b[2][0] + a[2][3] * b[3][0];
      c[2][1] = a[2][0] * b[0][1] + a[2][1] * b[1][1] + a[2][2] * b[2][1] + a[2][3] * b[3][1];
      c[2][2] = a[2][0] * b[0][2] + a[2][1] * b[1][2] + a[2][2] * b[2][2] + a[2][3] * b[3][2];
      c[2][3] = a[2][0] * b[0][3] + a[2][1] * b[1][3] + a[2][2] * b[2][3] + a[2][3] * b[3][3];
      c[3][0] = a[3][0] * b[0][0] + a[3][1] * b[1][0] + a[3][2] * b[2][0] + a[3][3] * b[3][0];
      c[3][1] = a[3][0] * b[0][1] + a[3][1] * b[1][1] + a[3][2] * b[2][1] + a[3][3] * b[3][1];
      c[3][2] = a[3][0] * b[0][2] + a[3][1] * b[1][2] + a[3][2] * b[2][2] + a[3][3] * b[3][2];
      c[3][3] = a[3][0] * b[0][3] + a[3][1] * b[1][3] + a[3][2] * b[2][3] + a[3][3] * b[3][3];
      return c;
   }

   public static final double[] MAT_DOT_VEC_2X3(double[][] m, double[] v) {
      double[] vtmp = new double[]{m[0][0] * v[0] + m[0][1] * v[1] + m[0][2], m[1][0] * v[0] + m[1][1] * v[1] + m[1][2], 0.0D};
      return vtmp;
   }

   public static final double[] MAT_DOT_VEC_3X3(double[][] m, double[] v) {
      double[] vtmp = new double[]{m[0][0] * v[0] + m[0][1] * v[1] + m[0][2] * v[2], m[1][0] * v[0] + m[1][1] * v[1] + m[1][2] * v[2], m[2][0] * v[0] + m[2][1] * v[1] + m[2][2] * v[2]};
      return vtmp;
   }

   public static final double[] NORM_XFORM_2X2(double[][] m, double[] v) {
      double len = 0.0D;
      double[] p = new double[3];
      if (m[0][1] == 0.0D && m[1][0] == 0.0D && m[0][0] == m[1][1]) {
         p = VEC_COPY_2(v);
      } else {
         p[0] = m[1][1] * v[0] - m[1][0] * v[1];
         p[1] = -m[0][1] * v[0] + m[0][0] * v[1];
         len = p[0] * p[0] + p[1] * p[1];
         len = 1.0D / Math.sqrt(len);
         p[0] *= len;
         p[1] *= len;
      }

      return p;
   }

   public static final double[][] uview_direction_d(double[] v21, double[] up) {
      double[][] amat = (double[][])null;
      double[][] bmat = (double[][])null;
      double[][] cmat = (double[][])null;
      double[] v_hat_21 = new double[3];
      double[] v_xy = new double[3];
      double[] up_proj = new double[3];
      double[] tmp = new double[3];
      double[][] m = (double[][])null;
      v_hat_21 = VEC_COPY(v21);
      double len = VEC_LENGTH(v_hat_21);
      double sine;
      if (len != 0.0D) {
         len = 1.0D / len;
         v_hat_21 = VEC_SCALE(len, v_hat_21);
         sine = Math.sqrt(1.0D - v_hat_21[2] * v_hat_21[2]);
         amat = ROTY_CS(-v_hat_21[2], -sine);
      } else {
         amat = IDENTIFY_MATRIX_4X4();
      }

      v_xy[0] = v21[0];
      v_xy[1] = v21[1];
      v_xy[2] = 0.0D;
      len = VEC_LENGTH(v_xy);
      if (len != 0.0D) {
         len = 1.0D / len;
         v_xy = VEC_SCALE(len, v_xy);
         bmat = ROTZ_CS(v_xy[0], v_xy[1]);
         cmat = MATRIX_PRODUCT_4X4(amat, bmat);
      } else {
         cmat = COPY_MATRIX_4X4(amat);
      }

      up_proj = VEC_PERP(up, v_hat_21);
      len = VEC_LENGTH(up_proj);
      if (len != 0.0D) {
         len = 1.0D / len;
         up_proj = VEC_SCALE(len, up_proj);
         tmp[0] = cmat[1][0];
         tmp[1] = cmat[1][1];
         tmp[2] = cmat[1][2];
         double cosine = VEC_DOT_PRODUCT(tmp, up_proj);
         tmp[0] = cmat[0][0];
         tmp[1] = cmat[0][1];
         tmp[2] = cmat[0][2];
         sine = VEC_DOT_PRODUCT(tmp, up_proj);
         amat = ROTZ_CS(cosine, -sine);
         m = MATRIX_PRODUCT_4X4(amat, cmat);
      } else {
         m = COPY_MATRIX_4X4(cmat);
      }

      return m;
   }

   public static final double[][] uviewpoint_d(double[] v1, double[] v2, double[] up) {
      double[] v_hat_21 = null;
      double[][] trans_mat = (double[][])null;
      double[][] rot_mat = (double[][])null;
      double[][] m = (double[][])null;
      double[] v_hat_21 = VEC_DIFF(v2, v1);
      rot_mat = uview_direction_d(v_hat_21, up);
      trans_mat = IDENTIFY_MATRIX_4X4();
      trans_mat[3][0] = v1[0];
      trans_mat[3][1] = v1[1];
      trans_mat[3][2] = v1[2];
      m = MATRIX_PRODUCT_4X4(rot_mat, trans_mat);
      return m;
   }
}
