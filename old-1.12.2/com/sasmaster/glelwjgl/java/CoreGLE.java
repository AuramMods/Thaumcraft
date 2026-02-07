package com.sasmaster.glelwjgl.java;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallback;

public class CoreGLE implements GLE {
   public static final String VERSION = new String("$Id: CoreGLE.java,v 1.5 1998/05/20 00:19:43 descarte Exp descarte $");
   private static final String GLE_VERSION = new String("095");
   private final GLEContext context_ = new GLEContext();
   private int _POLYCYL_TESS = 20;
   private int __ROUND_TESS_PIECES = 5;
   private static GLU glu_ = null;
   private CoreGLE.tessellCallBack tessCallback;
   private float SLICE;
   private float SLICE_PROGRESS;

   public void set_POLYCYL_TESS(int _POLYCYL_TESS) {
      this._POLYCYL_TESS = _POLYCYL_TESS;
   }

   public void set__ROUND_TESS_PIECES(int __ROUND_TESS_PIECES) {
      this.__ROUND_TESS_PIECES = __ROUND_TESS_PIECES;
   }

   public CoreGLE() {
      this.tessCallback = new CoreGLE.tessellCallBack(glu_);
      this.SLICE = 1.0F;
      this.SLICE_PROGRESS = 0.0F;
   }

   public int gleGetJoinStyle() {
      return this.context_.getJoinStyle();
   }

   public void gleSetJoinStyle(int style) {
      this.context_.setJoinStyle(style);
   }

   public void gleTextureMode(int mode) {
   }

   private void gen_polycone(int npoints, double[][] pointArray, float[][] colourArray, double radius, double[][][] xformArray, float texSlice, float start) {
      this.SLICE = texSlice;
      this.SLICE_PROGRESS = start;
      double[][] circle = new double[this._POLYCYL_TESS][2];
      double[][] norm = new double[this._POLYCYL_TESS][2];
      double[] v21 = new double[3];
      double len = 0.0D;
      double[] up = new double[3];
      if (xformArray != null) {
         radius = 1.0D;
      }

      double s = Math.sin(6.283185307179586D / (double)this._POLYCYL_TESS);
      double c = Math.cos(6.283185307179586D / (double)this._POLYCYL_TESS);
      norm[0][0] = 1.0D;
      norm[0][1] = 0.0D;
      circle[0][0] = radius;
      circle[0][1] = 0.0D;

      int i;
      for(i = 1; i < this._POLYCYL_TESS; ++i) {
         norm[i][0] = norm[i - 1][0] * c - norm[i - 1][1] * s;
         norm[i][1] = norm[i - 1][0] * s + norm[i - 1][1] * c;
         circle[i][0] = radius * norm[i][0];
         circle[i][1] = radius * norm[i][1];
      }

      int i = 0;
      i = intersect.FIND_NON_DEGENERATE_POINT(i, npoints, len, v21, pointArray);
      len = matrix.VEC_LENGTH(v21);
      if (i != npoints) {
         if (v21[0] == 0.0D && v21[2] == 0.0D) {
            up[0] = up[1] = up[2] = 1.0D;
         } else {
            up[0] = up[2] = 0.0D;
            up[1] = 1.0D;
         }

         int savedStyle = this.gleGetJoinStyle();
         this.gleSetJoinStyle(savedStyle | 4096);
         if (!GL11.glIsEnabled(2896)) {
            this.gleSuperExtrusion(this._POLYCYL_TESS, circle, (double[][])null, up, npoints, pointArray, colourArray, xformArray);
         } else {
            this.gleSuperExtrusion(this._POLYCYL_TESS, circle, norm, up, npoints, pointArray, colourArray, xformArray);
         }

         this.gleSetJoinStyle(savedStyle);
      }
   }

   public void glePolyCylinder(int npoints, double[][] pointArray, float[][] colourArray, double radius, float texSlice, float start) throws GLEException {
      this.gen_polycone(npoints, pointArray, colourArray, radius, (double[][][])null, texSlice, start);
   }

   public void glePolyCone(int npoints, double[][] pointArray, float[][] colourArray, double[] radiusArray, float texSlice, float start) throws GLEException {
      double[][][] xforms = new double[npoints][2][3];

      for(int i = 0; i < npoints; ++i) {
         xforms[i][0][0] = radiusArray[i];
         xforms[i][0][1] = 0.0D;
         xforms[i][0][2] = 0.0D;
         xforms[i][1][0] = 0.0D;
         xforms[i][1][1] = radiusArray[i];
         xforms[i][1][2] = 0.0D;
      }

      this.gen_polycone(npoints, pointArray, colourArray, 1.0D, xforms, texSlice, start);
   }

   public void gleExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray) throws GLEException {
      this.gleSuperExtrusion(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, (double[][][])null);
   }

   public void gleTwistExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[] twistArray) throws GLEException {
      double[][][] xforms = new double[npoints][2][3];
      double angle = 0.0D;
      double si = 0.0D;
      double co = 0.0D;

      for(int j = 0; j < npoints; ++j) {
         angle = 0.017453292519943295D * twistArray[j];
         si = Math.sin(angle);
         co = Math.cos(angle);
         xforms[j][0][0] = co;
         xforms[j][0][1] = -si;
         xforms[j][0][2] = 0.0D;
         xforms[j][1][0] = si;
         xforms[j][1][1] = co;
         xforms[j][1][2] = 0.0D;
      }

      this.gleSuperExtrusion(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xforms);
   }

   public void gleSuperExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[][][] xformArray) throws GLEException {
      this.context_.ncp = ncp;
      this.context_.contour = contour;
      this.context_.contourNormal = contourNormal;
      this.context_.up = up;
      this.context_.npoints = npoints;
      this.context_.pointArray = pointArray;
      this.context_.colourArray = colourArray;
      this.context_.xformArray = xformArray;
      switch(this.gleGetJoinStyle() & 15) {
      case 1:
         this.extrusion_raw_join(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xformArray);
         break;
      case 2:
         this.extrusion_angle_join(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xformArray);
         break;
      case 3:
      case 4:
         this.extrusion_round_or_cut_join(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xformArray);
         break;
      default:
         throw new GLEException("Join style is complete rubbish!");
      }

   }

   public void gleSpiral(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException {
      int npoints = (int)((double)this._POLYCYL_TESS / 360.0D * Math.abs(sweepTheta) + 4.0D);
      double[][] points = (double[][])null;
      double[][][] xforms = (double[][][])null;
      double delta = 0.0D;
      double deltaAngle = 0.0D;
      double cdelta = 0.0D;
      double sdelta = 0.0D;
      double sprev = 0.0D;
      double cprev = 0.0D;
      double scurr = 0.0D;
      double ccurr = 0.0D;
      double[][] mA = new double[2][2];
      double[][] mB = new double[2][2];
      double[][] run = new double[2][2];
      double[] deltaTrans = new double[2];
      double[] trans = new double[2];
      points = new double[npoints][3];
      if (startTransform == null) {
         xforms = (double[][][])null;
      } else {
         xforms = new double[npoints][2][3];
      }

      deltaAngle = 0.017453292519943295D * sweepTheta / (double)(npoints - 3);
      startTheta *= 0.017453292519943295D;
      startTheta -= deltaAngle;
      cprev = Math.cos(startTheta);
      sprev = Math.sin(startTheta);
      cdelta = Math.cos(deltaAngle);
      sdelta = Math.sin(deltaAngle);
      delta = deltaAngle / 6.283185307179586D;
      dzdTheta *= delta;
      drdTheta *= delta;
      startZ -= dzdTheta;
      startRadius -= drdTheta;

      int j;
      for(j = 0; j < npoints; ++j) {
         points[j][0] = startRadius * cprev;
         points[j][1] = startRadius * sprev;
         points[j][2] = startZ;
         startZ += dzdTheta;
         startRadius += drdTheta;
         ccurr = cprev * cdelta - sprev * sdelta;
         scurr = cprev * sdelta + sprev * cdelta;
         cprev = ccurr;
         sprev = scurr;
      }

      if (startTransform != null) {
         if (dTransformdTheta == null) {
            for(j = 0; j < npoints; ++j) {
               xforms[j][0][0] = startTransform[0][0];
               xforms[j][0][1] = startTransform[0][1];
               xforms[j][0][2] = startTransform[0][2];
               xforms[j][1][0] = startTransform[1][0];
               xforms[j][1][1] = startTransform[1][1];
               xforms[j][1][2] = startTransform[1][2];
            }
         } else {
            deltaTrans[0] = delta * dTransformdTheta[0][2];
            deltaTrans[1] = delta * dTransformdTheta[1][2];
            trans[0] = startTransform[0][2];
            trans[1] = startTransform[1][2];
            delta /= 32.0D;
            mA[0][0] = 1.0D + delta * dTransformdTheta[0][0];
            mA[0][1] = delta * dTransformdTheta[0][1];
            mA[1][0] = delta * dTransformdTheta[1][0];
            mA[1][1] = 1.0D + delta * dTransformdTheta[1][1];
            mB = matrix.MATRIX_PRODUCT_2X2(mA, mA);
            mA = matrix.MATRIX_PRODUCT_2X2(mB, mB);
            mB = matrix.MATRIX_PRODUCT_2X2(mA, mA);
            mA = matrix.MATRIX_PRODUCT_2X2(mB, mB);
            mB = matrix.MATRIX_PRODUCT_2X2(mA, mA);
            run = matrix.COPY_MATRIX_2X2(startTransform);
            xforms[0][0][0] = startTransform[0][0];
            xforms[0][0][1] = startTransform[0][1];
            xforms[0][0][2] = startTransform[0][2];
            xforms[0][1][0] = startTransform[1][0];
            xforms[0][1][1] = startTransform[1][1];
            xforms[0][1][2] = startTransform[1][2];

            for(j = 0; j < npoints; ++j) {
               xforms[j][0][0] = run[0][0];
               xforms[j][0][1] = run[0][1];
               xforms[j][1][0] = run[1][0];
               xforms[j][1][1] = run[1][1];
               mA = matrix.MATRIX_PRODUCT_2X2(mB, run);
               run = matrix.COPY_MATRIX_2X2(mA);
               xforms[j][0][2] = trans[0];
               xforms[j][1][2] = trans[1];
               trans[0] += deltaTrans[0];
               trans[1] += deltaTrans[1];
            }
         }
      }

      j = this.gleGetJoinStyle();
      int style = j & -16;
      style |= 2;
      this.gleSetJoinStyle(style);
      this.gleSuperExtrusion(ncp, contour, contourNormal, up, npoints, points, (float[][])null, xforms);
      this.gleSetJoinStyle(j);
   }

   public void gleLathe(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException {
      double[] localup = new double[3];
      double len = 0.0D;
      double[] trans = new double[2];
      double[][] start = new double[2][3];
      double[][] delt = new double[2][3];
      if (up != null) {
         if (up[1] != 0.0D) {
            localup[0] = up[0];
            localup[1] = 0.0D;
            localup[2] = up[2];
            len = matrix.VEC_LENGTH(localup);
            if (len != 0.0D) {
               len = 1.0D / len;
               localup[0] *= len;
               localup[2] *= len;
               localup = matrix.VEC_SCALE(len, localup);
            } else {
               localup[0] = 0.0D;
               localup[2] = 1.0D;
            }
         } else {
            localup = matrix.VEC_COPY(up);
         }
      } else {
         localup[0] = 0.0D;
         localup[2] = 1.0D;
      }

      trans[0] = localup[2] * drdTheta - localup[0] * dzdTheta;
      trans[1] = localup[0] * drdTheta + localup[2] * dzdTheta;
      if (startTransform != null) {
         if (dTransformdTheta != null) {
            delt = matrix.COPY_MATRIX_2X3(dTransformdTheta);
            delt[0][2] += trans[0];
            delt[1][2] += trans[1];
         } else {
            delt[0][0] = 0.0D;
            delt[0][1] = 0.0D;
            delt[0][2] = trans[0];
            delt[1][0] = 0.0D;
            delt[1][1] = 0.0D;
            delt[1][2] = trans[1];
         }

         this.gleSpiral(ncp, contour, contourNormal, up, startRadius, 0.0D, startZ, 0.0D, startTransform, delt, startTheta, sweepTheta);
      } else {
         start[0][0] = 1.0D;
         start[0][1] = 0.0D;
         start[0][2] = 0.0D;
         start[1][0] = 0.0D;
         start[1][1] = 1.0D;
         start[1][2] = 0.0D;
         delt[0][0] = 0.0D;
         delt[0][1] = 0.0D;
         delt[0][2] = trans[0];
         delt[1][0] = 0.0D;
         delt[1][1] = 0.0D;
         delt[1][2] = trans[1];
         this.gleSpiral(ncp, contour, contourNormal, up, startRadius, 0.0D, startZ, 0.0D, start, delt, startTheta, sweepTheta);
      }

   }

   public void gleHelicoid(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException {
      this.super_helix(rToroid, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta, "Spiral");
   }

   public void gleToroid(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException {
      this.super_helix(rToroid, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta, "Lathe");
   }

   public void gleScrew(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startz, double endz, double twist) throws GLEException {
      int numsegs = (int)Math.abs(twist / 18.0D) + 4;
      double[][] path = new double[numsegs][3];
      double[] twarr = new double[numsegs];
      double delta = 0.0D;
      double currz = 0.0D;
      double currang = 0.0D;
      double delang = 0.0D;
      delta = (endz - startz) / (double)(numsegs - 3);
      currz = startz - delta;
      delang = twist / (double)(numsegs - 3);
      currang = -delang;

      for(int i = 0; i < numsegs; ++i) {
         path[i][0] = 0.0D;
         path[i][1] = 0.0D;
         path[i][2] = currz;
         currz += delta;
         twarr[i] = currang;
         currang += delang;
      }

      this.gleTwistExtrusion(ncp, contour, contourNormal, up, numsegs, path, (float[][])null, twarr);
   }

   private final void super_helix(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta, String callback) {
      double[][] circle = new double[this._POLYCYL_TESS][2];
      double[][] norm = new double[this._POLYCYL_TESS][2];
      double c = 0.0D;
      double s = 0.0D;
      double[] up = new double[3];
      s = Math.sin(6.283185307179586D / (double)this._POLYCYL_TESS);
      c = Math.cos(6.283185307179586D / (double)this._POLYCYL_TESS);
      norm[0][0] = 1.0D;
      norm[0][1] = 0.0D;
      circle[0][0] = rToroid;
      circle[0][1] = 0.0D;

      int saveStyle;
      for(saveStyle = 1; saveStyle < this._POLYCYL_TESS; ++saveStyle) {
         norm[saveStyle][0] = norm[saveStyle - 1][0] * c - norm[saveStyle - 1][1] * s;
         norm[saveStyle][1] = norm[saveStyle - 1][0] * s + norm[saveStyle - 1][1] * c;
         circle[saveStyle][0] = rToroid * norm[saveStyle][0];
         circle[saveStyle][1] = rToroid * norm[saveStyle][1];
      }

      up[1] = up[2] = 0.0D;
      up[0] = 1.0D;
      saveStyle = this.gleGetJoinStyle();
      int style = saveStyle | 4096;
      style |= 1024;
      this.gleSetJoinStyle(style);
      if (!GL11.glIsEnabled(2896)) {
         if (callback.equals("Spiral")) {
            this.gleSpiral(this._POLYCYL_TESS, circle, (double[][])null, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
         } else {
            if (!callback.equals("Lathe")) {
               throw new GLEException("Specified callback " + callback + " is not registered. Use either ``Spiral'' or ``Lathe''");
            }

            this.gleLathe(this._POLYCYL_TESS, circle, (double[][])null, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
         }
      } else if (callback.equals("Spiral")) {
         this.gleSpiral(this._POLYCYL_TESS, circle, norm, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
      } else {
         if (!callback.equals("Lathe")) {
            throw new GLEException("Specified callback " + callback + " is not registered. Use either ``Spiral'' or ``Lathe''");
         }

         this.gleLathe(this._POLYCYL_TESS, circle, norm, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
      }

      this.gleSetJoinStyle(saveStyle);
   }

   private double[] up_sanity_check(double[] up, int npoints, double[][] pointArray) {
      double len = 0.0D;
      double[] diff = null;
      double[] vtmp = null;
      double[] diff = matrix.VEC_DIFF(pointArray[1], pointArray[0]);
      len = matrix.VEC_LENGTH(diff);
      if (len == 0.0D) {
         for(int i = 1; i < npoints - 2; ++i) {
            diff = matrix.VEC_DIFF(pointArray[i + 1], pointArray[i]);
            len = matrix.VEC_LENGTH(diff);
            if (len != 0.0D) {
               break;
            }
         }
      }

      len = 1.0D / len;
      diff = matrix.VEC_SCALE(len, diff);
      double[] vtmp = matrix.VEC_PERP(up, diff);
      len = matrix.VEC_LENGTH(vtmp);
      if (len == 0.0D) {
         System.err.println("Extrusion: Warning: ");
         System.err.println("contour up vector parallel to tubing direction");
         vtmp = matrix.VEC_COPY(diff);
      }

      return vtmp;
   }

   private final void extrusion_raw_join(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[][][] xformArray) {
      int i = false;
      int j = false;
      int inext = false;
      double[][] m = (double[][])null;
      double len = 0.0D;
      double[] diff = new double[3];
      double[] bi_0 = new double[3];
      double[] yup = new double[3];
      double[] nrmv = new double[3];
      boolean no_norm = contourNormal == null;
      boolean no_cols = colourArray == null;
      boolean no_xform = xformArray == null;
      double[][] mem_anchor = (double[][])null;
      double[][] front_loop = (double[][])null;
      double[][] back_loop = (double[][])null;
      double[][] front_norm = (double[][])null;
      double[][] back_norm = (double[][])null;
      double[][] tmp = (double[][])null;
      nrmv[0] = nrmv[1] = 0.0D;
      if (!no_xform) {
         front_loop = new double[ncp][3];
         back_loop = new double[ncp][3];
         front_norm = new double[ncp][3];
         back_norm = new double[ncp][3];
      }

      if (up == null) {
         yup[0] = 0.0D;
         yup[1] = 1.0D;
         yup[2] = 0.0D;
      } else {
         yup = matrix.VEC_COPY(up);
      }

      up = matrix.VEC_COPY(yup);
      yup = this.up_sanity_check(up, npoints, pointArray);
      int i = 1;
      int inext = intersect.FIND_NON_DEGENERATE_POINT(i, npoints, len, diff, pointArray);
      len = matrix.VEC_LENGTH(diff);
      int j;
      if (!no_xform) {
         for(j = 0; j < ncp; ++j) {
            front_loop[j] = matrix.MAT_DOT_VEC_2X3(xformArray[inext - 1], contour[j]);
            front_loop[j][2] = 0.0D;
         }

         if (!no_norm) {
            for(j = 0; j < ncp; ++j) {
               front_norm[j] = matrix.NORM_XFORM_2X2(xformArray[inext - 1], contourNormal[j]);
               front_norm[j][2] = 0.0D;
               back_norm[j][2] = 0.0D;
            }
         }
      }

      while(inext < npoints - 1) {
         bi_0 = intersect.bisecting_plane(pointArray[i - 1], pointArray[i], pointArray[inext]);
         yup = matrix.VEC_REFLECT(yup, bi_0);
         m = matrix.uviewpoint_d(pointArray[i], pointArray[inext], yup);
         DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
         mbuffer.put(new double[]{m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3]});
         mbuffer.flip();
         GL11.glPushMatrix();
         GL11.glMultMatrix(mbuffer);
         if (no_xform) {
            if (no_cols) {
               if (no_norm) {
                  this.draw_raw_segment_plain(ncp, contour, inext, len);
               } else if ((this.gleGetJoinStyle() & 256) == 256) {
                  this.draw_raw_segment_facet_n(ncp, contour, contourNormal, inext, len);
               } else {
                  this.draw_raw_segment_edge_n(ncp, contour, contourNormal, inext, len);
               }
            } else if (no_norm) {
               this.draw_raw_segment_color(ncp, contour, colourArray, inext, len);
            } else if ((this.gleGetJoinStyle() & 256) == 256) {
               this.draw_raw_segment_c_and_facet_n(ncp, contour, colourArray, contourNormal, inext, len);
            } else {
               this.draw_raw_segment_c_and_edge_n(ncp, contour, colourArray, contourNormal, inext, len);
            }
         } else {
            for(j = 0; j < ncp; ++j) {
               back_loop[j] = matrix.MAT_DOT_VEC_2X3(xformArray[inext], contour[j]);
               back_loop[j][2] = -len;
               front_loop[j][2] = 0.0D;
            }

            if (!no_norm) {
               for(j = 0; j < ncp; ++j) {
                  back_norm[j] = matrix.NORM_XFORM_2X2(xformArray[inext], contourNormal[j]);
               }
            }

            if (no_cols) {
               if (no_norm) {
                  this.draw_segment_plain(ncp, front_loop, back_loop, inext, len);
               } else if ((this.gleGetJoinStyle() & 256) == 256) {
                  this.draw_binorm_segment_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len);
               } else {
                  this.draw_binorm_segment_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len);
               }

               if ((this.gleGetJoinStyle() & 16) == 16) {
                  nrmv[2] = 1.0D;
                  GL11.glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
                  this.draw_front_contour_cap(ncp, front_loop);
                  nrmv[2] = -1.0D;
                  GL11.glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
                  this.draw_back_contour_cap(ncp, back_loop);
               }
            } else {
               if (no_norm) {
                  this.draw_segment_color(ncp, front_loop, back_loop, colourArray[inext - 1], colourArray[inext], inext, len);
               } else if ((this.gleGetJoinStyle() & 256) == 256) {
                  this.draw_binorm_segment_c_and_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, colourArray[inext - 1], colourArray[inext], inext, len);
               } else {
                  this.draw_binorm_segment_c_and_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, colourArray[inext - 1], colourArray[inext], inext, len);
               }

               if ((this.gleGetJoinStyle() & 16) == 16) {
                  GL11.glColor3f(colourArray[inext - 1][0], colourArray[inext - 1][1], colourArray[inext - 1][2]);
                  nrmv[2] = 1.0D;
                  GL11.glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
                  this.draw_front_contour_cap(ncp, front_loop);
                  GL11.glColor3f(colourArray[inext][0], colourArray[inext][1], colourArray[inext][2]);
                  nrmv[2] = -1.0D;
                  GL11.glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
                  this.draw_back_contour_cap(ncp, back_loop);
               }
            }
         }

         GL11.glPopMatrix();
         tmp = front_loop;
         front_loop = back_loop;
         back_loop = tmp;
         tmp = front_norm;
         front_norm = back_norm;
         back_norm = tmp;
         i = inext;
         inext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, len, diff, pointArray);
         len = matrix.VEC_LENGTH(diff);
      }

   }

   private final void draw_raw_segment_plain(int ncp, double[][] contour, int inext, double len) {
      double[] point = new double[3];
      System.err.println("draw_raw_segment_plain()");
      GL11.glBegin(5);

      for(int j = 0; j < ncp; ++j) {
         point[0] = contour[j][0];
         point[1] = contour[j][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         point[0] = contour[0][0];
         point[1] = contour[0][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      GL11.glEnd();
      if ((this.gleGetJoinStyle() & 16) == 16) {
         this.draw_raw_style_end_cap(ncp, contour, 0.0D, true);
         this.draw_raw_style_end_cap(ncp, contour, -len, false);
      }

   }

   private final void draw_raw_segment_color(int ncp, double[][] contour, float[][] color_array, int inext, double len) {
      double[] point = new double[3];
      System.out.println("draw_raw_segment_color");
      GL11.glBegin(5);
      double tc = 0.0D;

      for(int j = 0; j < ncp; ++j) {
         tc = (double)j / (double)ncp;
         point[0] = contour[j][0];
         point[1] = contour[j][1];
         point[2] = 0.0D;
         GL11.glTexCoord2d(tc, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glTexCoord2d(tc, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         point[0] = contour[0][0];
         point[1] = contour[0][1];
         point[2] = 0.0D;
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      GL11.glEnd();
      if ((this.gleGetJoinStyle() & 16) == 16) {
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         this.draw_raw_style_end_cap(ncp, contour, 0.0D, true);
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         this.draw_raw_style_end_cap(ncp, contour, -len, false);
      }

      this.SLICE_PROGRESS += this.SLICE;
   }

   private final void draw_raw_segment_edge_n(int ncp, double[][] contour, double[][] cont_normal, int inext, double len) {
      double[] point = new double[3];
      double[] norm = new double[3];
      System.err.println("draw_raw_segment_edge_n");
      norm[2] = 0.0D;
      GL11.glBegin(5);

      for(int j = 0; j < ncp; ++j) {
         norm[0] = cont_normal[j][0];
         norm[1] = cont_normal[j][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[j][0];
         point[1] = contour[j][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         norm[0] = cont_normal[0][0];
         norm[1] = cont_normal[0][1];
         norm[2] = 0.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[0][0];
         point[1] = contour[0][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      GL11.glEnd();
      if ((this.gleGetJoinStyle() & 16) == 16) {
         norm[0] = norm[1] = 0.0D;
         norm[2] = 1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, 0.0D, true);
         norm[2] = -1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, -len, false);
      }

   }

   private final void draw_raw_segment_c_and_edge_n(int ncp, double[][] contour, float[][] color_array, double[][] cont_normal, int inext, double len) {
      double[] point = new double[3];
      double[] norm = new double[3];
      System.out.println("draw_raw_segment_c_and_edge_n");
      norm[2] = 0.0D;
      GL11.glBegin(5);

      for(int j = 0; j < ncp; ++j) {
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = cont_normal[j][0];
         norm[1] = cont_normal[j][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[j][0];
         point[1] = contour[j][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = cont_normal[0][0];
         norm[1] = cont_normal[0][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[0][0];
         point[1] = contour[0][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         norm[0] = cont_normal[0][0];
         norm[1] = cont_normal[0][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      GL11.glEnd();
      if ((this.gleGetJoinStyle() & 16) == 16) {
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = norm[1] = 0.0D;
         norm[2] = 1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, 0.0D, true);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         norm[2] = -1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, -len, false);
      }

   }

   private final void draw_raw_segment_facet_n(int ncp, double[][] contour, double[][] cont_normal, int inext, double len) {
      double[] point = new double[3];
      double[] norm = new double[3];
      System.out.println("draw_raw_segment_facet_n");
      norm[2] = 0.0D;
      GL11.glBegin(5);

      for(int j = 0; j < ncp - 1; ++j) {
         norm[0] = cont_normal[j][0];
         norm[1] = cont_normal[j][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[j][0];
         point[1] = contour[j][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[0] = contour[j + 1][0];
         point[1] = contour[j + 1][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         norm[0] = cont_normal[ncp - 1][0];
         norm[1] = cont_normal[ncp - 1][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[ncp - 1][0];
         point[1] = contour[ncp - 1][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[0] = contour[0][0];
         point[1] = contour[0][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      GL11.glEnd();
      if ((this.gleGetJoinStyle() & 16) == 16) {
         norm[0] = norm[1] = 0.0D;
         norm[2] = 1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, 0.0D, true);
         norm[2] = -1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, -len, false);
      }

   }

   private final void draw_raw_segment_c_and_facet_n(int ncp, double[][] contour, float[][] color_array, double[][] cont_normal, int inext, double len) {
      System.out.println("draw_raw_segment_c_and_facet_n");
      double[] point = new double[3];
      double[] norm = new double[]{0.0D, 0.0D, 0.0D};
      GL11.glBegin(5);

      for(int j = 0; j < ncp - 1; ++j) {
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = cont_normal[j][0];
         norm[1] = cont_normal[j][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[j][0];
         point[1] = contour[j][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[j + 1][0];
         point[1] = contour[j + 1][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         point[0] = contour[ncp - 1][0];
         point[1] = contour[ncp - 1][1];
         point[2] = 0.0D;
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = cont_normal[ncp - 1][0];
         norm[1] = cont_normal[ncp - 1][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = cont_normal[0][0];
         norm[1] = cont_normal[0][1];
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[0] = contour[0][0];
         point[1] = contour[0][1];
         point[2] = 0.0D;
         GL11.glVertex3d(point[0], point[1], point[2]);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         point[2] = -len;
         GL11.glVertex3d(point[0], point[1], point[2]);
      }

      GL11.glEnd();
      if ((this.gleGetJoinStyle() & 16) == 16) {
         GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
         norm[0] = norm[1] = 0.0D;
         norm[2] = 1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, 0.0D, true);
         GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
         norm[2] = -1.0D;
         GL11.glNormal3d(norm[0], norm[1], norm[2]);
         this.draw_raw_style_end_cap(ncp, contour, -len, false);
      }

   }

   private final void draw_raw_style_end_cap(int ncp, double[][] contour, double zval, boolean frontwards) {
      System.out.println("draw_raw_style_end_cap");
      GLUtessellator tobj = GLU.gluNewTess();
      tobj.gluTessProperty(100140, 100130.0D);
      tobj.gluTessCallback(100101, this.tessCallback);
      tobj.gluTessCallback(100100, this.tessCallback);
      tobj.gluTessCallback(100102, this.tessCallback);
      tobj.gluTessCallback(100103, this.tessCallback);
      tobj.gluTessBeginPolygon((Object)null);
      tobj.gluTessBeginContour();
      int j;
      double[] vertex;
      if (frontwards) {
         for(j = 0; j < ncp; ++j) {
            vertex = new double[]{contour[j][0], contour[j][1], zval};
            tobj.gluTessVertex(vertex, 0, vertex);
         }
      } else {
         for(j = ncp - 1; j > -1; --j) {
            vertex = new double[]{contour[j][0], contour[j][1], zval};
            tobj.gluTessVertex(vertex, 0, vertex);
         }
      }

      tobj.gluTessEndContour();
      tobj.gluTessEndPolygon();
      tobj.gluDeleteTess();
   }

   private final void draw_front_contour_cap(int ncp, double[][] contour) {
      GLUtessellator tobj = GLU.gluNewTess();
      tobj.gluTessProperty(100140, 100130.0D);
      tobj.gluTessCallback(100101, this.tessCallback);
      tobj.gluTessCallback(100100, this.tessCallback);
      tobj.gluTessCallback(100102, this.tessCallback);
      tobj.gluTessCallback(100103, this.tessCallback);
      tobj.gluTessBeginPolygon((Object)null);
      tobj.gluTessBeginContour();

      for(int j = 0; j < ncp; ++j) {
         tobj.gluTessVertex(contour[j], 0, contour[j]);
      }

      tobj.gluTessEndContour();
      tobj.gluTessEndPolygon();
      tobj.gluDeleteTess();
   }

   private final void draw_back_contour_cap(int ncp, double[][] contour) {
      GLUtessellator tobj = GLU.gluNewTess();
      tobj.gluTessProperty(100140, 100132.0D);
      tobj.gluTessCallback(100101, this.tessCallback);
      tobj.gluTessCallback(100100, this.tessCallback);
      tobj.gluTessCallback(100102, this.tessCallback);
      tobj.gluTessCallback(100103, this.tessCallback);
      tobj.gluTessBeginPolygon((Object)null);
      tobj.gluTessBeginContour();

      for(int j = ncp - 1; j > -1; --j) {
         tobj.gluTessVertex(contour[j], 0, contour[j]);
      }

      tobj.gluTessEndContour();
      tobj.gluTessEndPolygon();
      tobj.gluDeleteTess();
   }

   private final void extrusion_angle_join(int ncp, double[][] contour, double[][] cont_normal, double[] up, int npoints, double[][] point_array, float[][] color_array, double[][][] xform_array) {
      int i = false;
      int j = false;
      int inext = false;
      int inextnext = false;
      double[][] m = new double[4][4];
      double len = 0.0D;
      double len_seg = 0.0D;
      double[] diff = new double[3];
      double[] bi_0 = new double[3];
      double[] bi_1 = new double[3];
      double[] bisector_0 = new double[3];
      double[] bisector_1 = new double[3];
      double[] end_point_0 = new double[3];
      double[] end_point_1 = new double[3];
      double[] origin = new double[3];
      double[] neg_z = new double[3];
      double[] yup = new double[3];
      if (up == null) {
         yup[0] = 0.0D;
         yup[1] = 1.0D;
         yup[2] = 0.0D;
      } else {
         yup = matrix.VEC_COPY(up);
      }

      yup = this.up_sanity_check(yup, npoints, point_array);
      origin[0] = 0.0D;
      origin[1] = 0.0D;
      origin[2] = 0.0D;
      neg_z[0] = 0.0D;
      neg_z[1] = 0.0D;
      neg_z[2] = 1.0D;
      int i = 1;
      int inext = intersect.FIND_NON_DEGENERATE_POINT(i, npoints, len, diff, point_array);
      len = matrix.VEC_LENGTH(diff);
      len_seg = len;
      bi_0 = intersect.bisecting_plane(point_array[0], point_array[1], point_array[inext]);
      yup = matrix.VEC_REFLECT(yup, bi_0);
      double[][] front_loop = new double[ncp][3];
      double[][] back_loop = new double[ncp][3];
      double[][] front_norm = new double[ncp][3];
      double[][] back_norm = new double[ncp][3];
      double[][] norm_loop = front_norm;
      int j;
      if (cont_normal != null) {
         if (xform_array == null) {
            for(j = 0; j < ncp; ++j) {
               norm_loop[j][0] = cont_normal[j][0];
               norm_loop[j][1] = cont_normal[j][1];
               norm_loop[j][2] = 0.0D;
            }
         } else {
            for(j = 0; j < ncp; ++j) {
               front_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext - 1], cont_normal[j]);
               front_norm[j][2] = 0.0D;
               back_norm[j][2] = 0.0D;
            }
         }
      }

      for(boolean first_time = true; inext < npoints - 1; yup = matrix.VEC_REFLECT(yup, bi_0)) {
         int inextnext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, len, diff, point_array);
         len = matrix.VEC_LENGTH(diff);
         bi_1 = intersect.bisecting_plane(point_array[i], point_array[inext], point_array[inextnext]);
         m = matrix.uviewpoint_d(point_array[i], point_array[inext], yup);
         DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
         mbuffer.put(new double[]{m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3]});
         mbuffer.flip();
         GL11.glPushMatrix();
         GL11.glMultMatrix(mbuffer);
         bisector_0 = matrix.MAT_DOT_VEC_3X3(m, bi_0);
         bisector_1 = matrix.MAT_DOT_VEC_3X3(m, bi_1);
         neg_z[2] = -len_seg;

         for(j = 0; j < ncp; ++j) {
            if (cont_normal != null) {
               if (xform_array != null) {
                  back_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext], cont_normal[j]);
               }

               if ((this.gleGetJoinStyle() & 1024) == 1024) {
                  if (xform_array == null) {
                     back_norm[j][0] = cont_normal[j][0];
                     back_norm[j][1] = cont_normal[j][1];
                  }

                  front_norm[j][2] = 0.0D;
                  front_norm[j] = matrix.VEC_PERP(front_norm[j], bisector_0);
                  front_norm[j] = matrix.VEC_NORMALIZE(front_norm[j]);
                  back_norm[j][2] = 0.0D;
                  back_norm[j] = matrix.VEC_PERP(back_norm[j], bisector_1);
                  back_norm[j] = matrix.VEC_NORMALIZE(back_norm[j]);
               }
            }

            if (xform_array == null) {
               end_point_0[0] = contour[j][0];
               end_point_0[1] = contour[j][1];
               end_point_1[0] = contour[j][0];
               end_point_1[1] = contour[j][1];
            } else {
               end_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
               end_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
            }

            end_point_0[2] = 0.0D;
            end_point_1[2] = -len_seg;
            front_loop[j] = intersect.INNERSECT(origin, bisector_0, end_point_0, end_point_1);
            if (xform_array != null) {
               end_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
               end_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
            }

            end_point_0[2] = 0.0D;
            end_point_1[2] = -len_seg;
            back_loop[j] = intersect.INNERSECT(neg_z, bisector_1, end_point_0, end_point_1);
         }

         if ((this.gleGetJoinStyle() & 16) == 16) {
            if (first_time) {
               if (color_array != null) {
                  GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
               }

               first_time = false;
               this.draw_angle_style_front_cap(ncp, bisector_0, front_loop);
            }

            if (inext == npoints - 2) {
               if (color_array != null) {
                  GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
               }

               this.draw_angle_style_back_cap(ncp, bisector_1, back_loop);
            }
         }

         if (xform_array == null && (this.gleGetJoinStyle() & 1024) != 1024) {
            if (color_array == null) {
               if (cont_normal == null) {
                  this.draw_segment_plain(ncp, front_loop, back_loop, inext, len_seg);
               } else if ((this.gleGetJoinStyle() & 256) == 256) {
                  this.draw_segment_facet_n(ncp, front_loop, back_loop, norm_loop, inext, len_seg);
               } else {
                  this.draw_segment_edge_n(ncp, front_loop, back_loop, norm_loop, inext, len_seg);
               }
            } else if (cont_normal == null) {
               this.draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
            } else if ((this.gleGetJoinStyle() & 256) == 256) {
               this.draw_segment_c_and_facet_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
            } else {
               this.draw_segment_c_and_edge_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
            }
         } else if (color_array == null) {
            if (cont_normal == null) {
               this.draw_segment_plain(ncp, front_loop, back_loop, inext, len_seg);
            } else if ((this.gleGetJoinStyle() & 256) == 256) {
               this.draw_binorm_segment_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len_seg);
            } else {
               this.draw_binorm_segment_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len_seg);
            }
         } else if (cont_normal == null) {
            this.draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
         } else if ((this.gleGetJoinStyle() & 256) == 256) {
            this.draw_binorm_segment_c_and_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, len_seg);
         } else {
            this.draw_binorm_segment_c_and_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, len_seg);
         }

         GL11.glPopMatrix();
         len_seg = len;
         i = inext;
         inext = inextnext;
         bi_0 = matrix.VEC_COPY(bi_1);
         double[][] tmp = front_norm;
         front_norm = back_norm;
         back_norm = tmp;
      }

   }

   private final void draw_angle_style_front_cap(int ncp, double[] bi, double[][] point_array) {
      GLUtessellator tobj = GLU.gluNewTess();
      tobj.gluTessProperty(100140, 100130.0D);
      tobj.gluTessCallback(100101, this.tessCallback);
      tobj.gluTessCallback(100100, this.tessCallback);
      tobj.gluTessCallback(100102, this.tessCallback);
      tobj.gluTessCallback(100103, this.tessCallback);
      if (bi[2] < 0.0D) {
         bi = matrix.VEC_SCALE(-1.0D, bi);
      }

      GL11.glNormal3d(bi[0], bi[1], bi[2]);
      tobj.gluTessBeginPolygon((Object)null);
      tobj.gluTessBeginContour();

      for(int j = 0; j < ncp; ++j) {
         tobj.gluTessVertex(point_array[j], 0, point_array[j]);
      }

      tobj.gluTessEndContour();
      tobj.gluTessEndPolygon();
      tobj.gluDeleteTess();
   }

   private final void draw_angle_style_back_cap(int ncp, double[] bi, double[][] point_array) {
      GLUtessellator tobj = GLU.gluNewTess();
      tobj.gluTessProperty(100140, 100130.0D);
      tobj.gluTessCallback(100101, this.tessCallback);
      tobj.gluTessCallback(100100, this.tessCallback);
      tobj.gluTessCallback(100102, this.tessCallback);
      tobj.gluTessCallback(100103, this.tessCallback);
      if (bi[2] > 0.0D) {
         bi = matrix.VEC_SCALE(-1.0D, bi);
      }

      GL11.glNormal3d(bi[0], bi[1], bi[2]);
      tobj.gluTessBeginPolygon((Object)null);
      tobj.gluTessBeginContour();

      for(int j = ncp - 1; j >= 0; --j) {
         tobj.gluTessVertex(point_array[j], 0, point_array[j]);
      }

      tobj.gluTessEndContour();
      tobj.gluTessEndPolygon();
      tobj.gluDeleteTess();
   }

   private final void draw_segment_plain(int ncp, double[][] front_contour, double[][] back_contour, int inext, double len) {
      System.out.println("draw_segment_plain");
      GL11.glBegin(5);

      for(int j = 0; j < ncp; ++j) {
         double tc = (double)j / (double)ncp;
         GL11.glTexCoord2d(tc, (double)this.SLICE_PROGRESS);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glTexCoord2d(tc, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
      this.SLICE_PROGRESS += this.SLICE;
   }

   private final void draw_segment_color(int ncp, double[][] front_contour, double[][] back_contour, float[] color_last, float[] color_next, int inext, double len) {
      GL11.glBegin(5);
      double tc = 0.0D;

      for(int j = 0; j < ncp; ++j) {
         tc = (double)j / (double)ncp;
         GL11.glTexCoord2d(tc, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glTexCoord2d(tc, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
      this.SLICE_PROGRESS += this.SLICE;
   }

   private final void draw_segment_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, int inext, double len) {
      System.out.println("draw_segment_edge_n");
      GL11.glBegin(5);

      for(int j = 0; j < ncp; ++j) {
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glNormal3d(norm_cont[0][0], norm_cont[0][1], norm_cont[0][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
   }

   private final void draw_segment_c_and_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, float[] color_last, float[] color_next, int inext, double len) {
      GL11.glBegin(5);
      double tc = 0.0D;

      for(int j = 0; j < ncp; ++j) {
         tc = (double)j / (double)ncp;
         GL11.glTexCoord2d(tc, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glTexCoord2d(tc, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(norm_cont[0][0], norm_cont[0][1], norm_cont[0][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(norm_cont[0][0], norm_cont[0][1], norm_cont[0][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
      this.SLICE_PROGRESS += this.SLICE;
   }

   private final void draw_segment_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, int inext, double len) {
      GL11.glBegin(5);

      for(int j = 0; j < ncp - 1; ++j) {
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
         GL11.glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
         GL11.glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
         GL11.glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
         GL11.glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
   }

   private final void draw_segment_c_and_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, float[] color_last, float[] color_next, int inext, double len) {
      System.out.println("draw_segment_c_and_facet_n");
      GL11.glBegin(5);

      for(int j = 0; j < ncp - 1; ++j) {
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
         GL11.glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
         GL11.glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
         GL11.glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
   }

   private final void draw_binorm_segment_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, int inext, double len) {
      GL11.glBegin(5);
      double tc = 0.0D;

      for(int j = 0; j < ncp; ++j) {
         tc = (double)j / (double)ncp;
         GL11.glTexCoord2d(tc, (double)this.SLICE_PROGRESS);
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glTexCoord2d(tc, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glNormal3d(front_norm[0][0], front_norm[0][1], front_norm[0][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glNormal3d(back_norm[0][0], back_norm[0][1], back_norm[0][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
      this.SLICE_PROGRESS += this.SLICE;
   }

   private final void draw_binorm_segment_c_and_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, float[] color_last, float[] color_next, int inext, double len) {
      GL11.glBegin(5);
      double tc = 0.0D;

      for(int j = 0; j < ncp; ++j) {
         tc = (double)j / (double)ncp;
         GL11.glTexCoord2d(tc, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glTexCoord2d(tc, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glTexCoord2d(1.0D, (double)this.SLICE_PROGRESS);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(front_norm[0][0], front_norm[0][1], front_norm[0][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glTexCoord2d(1.0D, (double)(this.SLICE_PROGRESS + this.SLICE));
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(back_norm[0][0], back_norm[0][1], back_norm[0][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
      this.SLICE_PROGRESS += this.SLICE;
   }

   private final void draw_binorm_segment_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, int inext, double len) {
      System.out.println("draw_binorm_segment_facet_n");
      GL11.glBegin(5);

      for(int j = 0; j < ncp - 1; ++j) {
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
         GL11.glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
         GL11.glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
         GL11.glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
         GL11.glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
         GL11.glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
         GL11.glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
   }

   private final void draw_binorm_segment_c_and_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, float[] color_last, float[] color_next, int inext, double len) {
      System.out.println("draw_binorm_segment_c_and_facet_n");
      GL11.glBegin(5);

      for(int j = 0; j < ncp - 1; ++j) {
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
         GL11.glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
         GL11.glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
         GL11.glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
      }

      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
         GL11.glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
         GL11.glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
         GL11.glColor3f(color_last[0], color_last[1], color_last[2]);
         GL11.glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
         GL11.glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
         GL11.glColor3f(color_next[0], color_next[1], color_next[2]);
         GL11.glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
         GL11.glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
      }

      GL11.glEnd();
   }

   private final void extrusion_round_or_cut_join(int ncp, double[][] contour, double[][] cont_normal, double[] up, int npoints, double[][] point_array, float[][] color_array, double[][][] xform_array) {
      int i = false;
      int j = false;
      int inext = false;
      int inextnext = false;
      double[][] m = new double[4][4];
      double tube_len = 0.0D;
      double seg_len = 0.0D;
      double[] diff = new double[3];
      double[] bi_0 = new double[3];
      double[] bi_1 = new double[3];
      double[] bisector_0 = new double[3];
      double[] bisector_1 = new double[3];
      double[] cut_0 = new double[3];
      double[] cut_1 = new double[3];
      double[] lcut_0 = new double[3];
      double[] lcut_1 = new double[3];
      boolean valid_cut_0 = false;
      boolean valid_cut_1 = false;
      double[] end_point_0 = new double[3];
      double[] end_point_1 = new double[3];
      double[] torsion_point_0 = new double[3];
      double[] torsion_point_1 = new double[3];
      double[] isect_point = new double[3];
      double[] origin = new double[3];
      double[] neg_z = new double[3];
      double[] yup = new double[3];
      double[][] front_cap = (double[][])null;
      double[][] back_cap = (double[][])null;
      double[][] front_loop = (double[][])null;
      double[][] back_loop = (double[][])null;
      double[][] front_norm = (double[][])null;
      double[][] back_norm = (double[][])null;
      double[][] norm_loop = (double[][])null;
      double[][] tmp = (double[][])null;
      boolean[] front_is_trimmed = null;
      boolean[] back_is_trimmed = null;
      float[] front_color = null;
      float[] back_color = null;
      boolean join_style_is_cut = false;
      double dot = 0.0D;
      boolean first_time = true;
      double[] cut_vec = null;
      String cap_callback = null;
      String tmp_cap_callback = null;
      if ((this.gleGetJoinStyle() & 3) == 3) {
         join_style_is_cut = true;
         cap_callback = new String("cut");
      } else {
         join_style_is_cut = false;
         cap_callback = new String("round");
      }

      if (up == null) {
         yup[0] = 0.0D;
         yup[1] = 1.0D;
         yup[2] = 0.0D;
      } else {
         yup = matrix.VEC_COPY(up);
      }

      yup = this.up_sanity_check(yup, npoints, point_array);
      origin[0] = 0.0D;
      origin[1] = 0.0D;
      origin[2] = 0.0D;
      neg_z[0] = 0.0D;
      neg_z[1] = 0.0D;
      neg_z[2] = 1.0D;
      front_norm = new double[ncp][3];
      back_norm = new double[ncp][3];
      front_loop = new double[ncp][3];
      back_loop = new double[ncp][3];
      front_cap = new double[ncp][3];
      back_cap = new double[ncp][3];
      boolean[] front_is_trimmed = new boolean[ncp];
      boolean[] back_is_trimmed = new boolean[ncp];
      int i = 1;
      int inext = intersect.FIND_NON_DEGENERATE_POINT(i, npoints, seg_len, diff, point_array);
      seg_len = matrix.VEC_LENGTH(diff);
      tube_len = seg_len;
      int j;
      if (cont_normal != null) {
         if (xform_array == null) {
            norm_loop = front_norm;
            back_norm = front_norm;

            for(j = 0; j < ncp; ++j) {
               norm_loop[j][0] = cont_normal[j][0];
               norm_loop[j][1] = cont_normal[j][1];
               norm_loop[j][2] = 0.0D;
            }
         } else {
            for(j = 0; j < ncp; ++j) {
               front_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext - 1], cont_normal[j]);
               front_norm[j][2] = 0.0D;
               back_norm[j][2] = 0.0D;
            }
         }
      } else {
         front_norm = back_norm = norm_loop = (double[][])null;
      }

      bi_0 = intersect.bisecting_plane(point_array[i - 1], point_array[i], point_array[inext]);
      valid_cut_0 = intersect.CUTTING_PLANE(cut_0, point_array[i - 1], point_array[i], point_array[inext]);

      for(yup = matrix.VEC_REFLECT(yup, bi_0); inext < npoints - 1; yup = matrix.VEC_REFLECT(yup, bi_0)) {
         int inextnext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, seg_len, diff, point_array);
         seg_len = matrix.VEC_LENGTH(diff);
         bi_1 = intersect.bisecting_plane(point_array[i], point_array[inext], point_array[inextnext]);
         valid_cut_1 = intersect.CUTTING_PLANE(cut_1, point_array[i], point_array[inext], point_array[inextnext]);
         m = matrix.uviewpoint_d(point_array[i], point_array[inext], yup);
         DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
         mbuffer.put(new double[]{m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3]});
         mbuffer.flip();
         GL11.glPushMatrix();
         GL11.glMultMatrix(mbuffer);
         lcut_0 = matrix.MAT_DOT_VEC_3X3(m, cut_0);
         lcut_1 = matrix.MAT_DOT_VEC_3X3(m, cut_1);
         bisector_0 = matrix.MAT_DOT_VEC_3X3(m, bi_0);
         bisector_1 = matrix.MAT_DOT_VEC_3X3(m, bi_1);
         neg_z[2] = -tube_len;

         for(j = 0; j < ncp; ++j) {
            if (xform_array == null) {
               end_point_0 = matrix.VEC_COPY_2(contour[j]);
               end_point_1 = matrix.VEC_COPY_2(contour[j]);
               torsion_point_0 = matrix.VEC_COPY_2(contour[j]);
               torsion_point_1 = matrix.VEC_COPY_2(contour[j]);
            } else {
               end_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
               torsion_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
               end_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
               torsion_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
               if (cont_normal != null) {
                  back_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext], cont_normal[j]);
               }
            }

            end_point_0[2] = 0.0D;
            torsion_point_0[2] = 0.0D;
            end_point_1[2] = -tube_len;
            torsion_point_1[2] = -tube_len;
            if (valid_cut_0 && join_style_is_cut) {
               isect_point = intersect.INNERSECT(origin, lcut_0, end_point_0, end_point_1);
               if (lcut_0[2] < 0.0D) {
                  lcut_0 = matrix.VEC_SCALE(-1.0D, lcut_0);
               }

               dot = lcut_0[0] * end_point_0[0];
               dot += lcut_0[1] * end_point_0[1];
               front_loop[j] = matrix.VEC_COPY(isect_point);
            } else {
               dot = 1.0D;
               front_loop[j] = matrix.VEC_COPY(end_point_0);
            }

            isect_point = intersect.INNERSECT(origin, bisector_0, end_point_0, torsion_point_1);
            if (!(dot <= 0.0D) && !(isect_point[2] < front_loop[j][2])) {
               front_is_trimmed[j] = false;
            } else {
               front_cap[j] = matrix.VEC_COPY(front_loop[j]);
               front_loop[j] = matrix.VEC_COPY(isect_point);
               front_is_trimmed[j] = true;
            }

            if (front_loop[j][2] < -tube_len) {
               front_loop[j] = matrix.VEC_COPY(end_point_1);
            }

            if (valid_cut_1 && join_style_is_cut) {
               isect_point = intersect.INNERSECT(neg_z, lcut_1, end_point_1, end_point_0);
               if (lcut_1[2] > 0.0D) {
                  lcut_1 = matrix.VEC_SCALE(-1.0D, lcut_1);
               }

               dot = lcut_1[0] * end_point_1[0];
               dot += lcut_1[1] * end_point_1[1];
               back_loop[j] = matrix.VEC_COPY(isect_point);
            } else {
               dot = 1.0D;
               back_loop[j] = matrix.VEC_COPY(end_point_1);
            }

            isect_point = intersect.INNERSECT(neg_z, bisector_1, torsion_point_0, end_point_1);
            if (!(dot <= 0.0D) && !(isect_point[2] > back_loop[j][2])) {
               back_is_trimmed[j] = false;
            } else {
               back_cap[j] = matrix.VEC_COPY(back_loop[j]);
               back_loop[j] = matrix.VEC_COPY(isect_point);
               back_is_trimmed[j] = true;
            }

            if (back_loop[j][2] > 0.0D) {
               back_loop[j] = matrix.VEC_COPY(end_point_0);
            }
         }

         if (xform_array == null) {
            if (color_array == null) {
               if (cont_normal == null) {
                  this.draw_segment_plain(ncp, front_loop, back_loop, inext, seg_len);
               } else if ((this.gleGetJoinStyle() & 256) == 256) {
                  this.draw_segment_facet_n(ncp, front_loop, back_loop, norm_loop, inext, seg_len);
               } else {
                  this.draw_segment_edge_n(ncp, front_loop, back_loop, norm_loop, inext, seg_len);
               }
            } else if (cont_normal == null) {
               this.draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
            } else if ((this.gleGetJoinStyle() & 256) == 256) {
               this.draw_segment_c_and_facet_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
            } else {
               this.draw_segment_c_and_edge_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
            }
         } else if (color_array == null) {
            if (cont_normal == null) {
               this.draw_segment_plain(ncp, front_loop, back_loop, inext, seg_len);
            } else if ((this.gleGetJoinStyle() & 256) == 256) {
               this.draw_binorm_segment_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, seg_len);
            } else {
               this.draw_binorm_segment_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, seg_len);
            }
         } else if (cont_normal == null) {
            this.draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
         } else if ((this.gleGetJoinStyle() & 256) == 256) {
            this.draw_binorm_segment_c_and_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, seg_len);
         } else {
            this.draw_binorm_segment_c_and_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, seg_len);
         }

         if (first_time) {
            first_time = false;
            tmp_cap_callback = cap_callback;
            cap_callback = new String("null");
            if ((this.gleGetJoinStyle() & 16) == 1) {
               if (color_array != null) {
                  GL11.glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
               }

               this.draw_angle_style_front_cap(ncp, bisector_0, front_loop);
            }
         }

         float[] front_color;
         float[] back_color;
         if (color_array != null) {
            front_color = color_array[inext - 1];
            back_color = color_array[inext];
         } else {
            front_color = null;
            back_color = null;
         }

         double[] cut_vec;
         if (cont_normal == null) {
            if (valid_cut_0) {
               cut_vec = lcut_0;
            } else {
               cut_vec = null;
            }

            this.draw_fillets_and_join_plain(ncp, front_loop, front_cap, front_is_trimmed, origin, bisector_0, front_color, back_color, cut_vec, true, cap_callback);
            if (inext == npoints - 2) {
               if ((this.gleGetJoinStyle() & 16) == 1) {
                  if (color_array != null) {
                     GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
                  }

                  this.draw_angle_style_back_cap(ncp, bisector_1, back_loop);
                  cap_callback = new String("null");
               }
            } else {
               cap_callback = tmp_cap_callback;
            }

            if (valid_cut_1) {
               cut_vec = lcut_1;
            } else {
               cut_vec = null;
            }

            this.draw_fillets_and_join_plain(ncp, back_loop, back_cap, back_is_trimmed, neg_z, bisector_1, back_color, front_color, cut_vec, false, cap_callback);
         } else {
            if (valid_cut_0) {
               cut_vec = lcut_0;
            } else {
               cut_vec = null;
            }

            this.draw_fillets_and_join_n_norms(ncp, front_loop, front_cap, front_is_trimmed, origin, bisector_0, front_norm, front_color, back_color, cut_vec, true, cap_callback);
            if (inext == npoints - 2) {
               if ((this.gleGetJoinStyle() & 16) == 1) {
                  if (color_array != null) {
                     GL11.glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
                  }

                  this.draw_angle_style_back_cap(ncp, bisector_1, back_loop);
                  cap_callback = new String("null");
               }
            } else {
               cap_callback = tmp_cap_callback;
            }

            if (valid_cut_1) {
               cut_vec = lcut_1;
            } else {
               cut_vec = null;
            }

            this.draw_fillets_and_join_n_norms(ncp, back_loop, back_cap, back_is_trimmed, neg_z, bisector_1, back_norm, back_color, front_color, cut_vec, false, cap_callback);
         }

         GL11.glPopMatrix();
         tmp = front_norm;
         front_norm = back_norm;
         back_norm = tmp;
         tube_len = seg_len;
         i = inext;
         inext = inextnext;
         bi_0 = matrix.VEC_COPY(bi_1);
         cut_0 = matrix.VEC_COPY(cut_1);
         valid_cut_0 = valid_cut_1;
      }

   }

   private final void draw_fillets_and_join_plain(int ncp, double[][] trimmed_loop, double[][] untrimmed_loop, boolean[] is_trimmed, double[] bis_origin, double[] bis_vector, float[] front_color, float[] back_color, double[] cut_vector, boolean face, String cap_callback) {
      int istop = false;
      int icnt = false;
      int icnt_prev = 0;
      int iloop = false;
      double[][] cap_loop = (double[][])null;
      double[] sect = new double[3];
      double[] tmp_vec = new double[3];
      int save_style = 0;
      boolean was_trimmed = false;
      cap_loop = new double[ncp + 3][3];
      int icnt = 0;
      int iloop = 0;
      if (!is_trimmed[0]) {
         if ((this.gleGetJoinStyle() & 3) == 3 && (save_style & 4096) != 4096) {
            tmp_vec = matrix.VEC_SUM(trimmed_loop[0], bis_vector);
            sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[0], tmp_vec);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            ++iloop;
         }

         cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[0]);
         ++iloop;
         icnt_prev = icnt++;
      } else {
         was_trimmed = true;

         while(is_trimmed[icnt]) {
            icnt_prev = icnt++;
            if (icnt >= ncp) {
               return;
            }
         }
      }

      int istop;
      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         istop = ncp;
      } else {
         istop = ncp - 1;
      }

      int save_style = this.gleGetJoinStyle();
      this.gleSetJoinStyle(save_style & -4097);

      while(icnt_prev < istop) {
         if (is_trimmed[icnt_prev] && is_trimmed[icnt]) {
         }

         if (is_trimmed[icnt_prev] && !is_trimmed[icnt]) {
            sect = intersect.INNERSECT(bis_origin, bis_vector, untrimmed_loop[icnt_prev], trimmed_loop[icnt]);
            draw_fillet_triangle_plain(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            ++iloop;
            cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
            ++iloop;
         }

         if (!is_trimmed[icnt_prev] && !is_trimmed[icnt]) {
            cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
            ++iloop;
         }

         if (!is_trimmed[icnt_prev] && is_trimmed[icnt]) {
            was_trimmed = true;
            sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt_prev], untrimmed_loop[icnt]);
            draw_fillet_triangle_plain(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            ++iloop;
            if (iloop >= 3) {
               if (cap_callback.equals("cut")) {
                  this.draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, (double[][])null, face);
               } else if (cap_callback.equals("round")) {
                  this.draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, (double[][])null, face);
               }
            }

            iloop = 0;
         }

         ++icnt_prev;
         ++icnt;
         icnt %= ncp;
      }

      --icnt;
      icnt += ncp;
      icnt %= ncp;
      if (!is_trimmed[icnt] && iloop >= 2) {
         tmp_vec = matrix.VEC_SUM(trimmed_loop[icnt], bis_vector);
         sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt], tmp_vec);
         cap_loop[iloop] = matrix.VEC_COPY(sect);
         ++iloop;
         if (!was_trimmed) {
            this.gleSetJoinStyle(save_style);
         }

         if (cap_callback.equals("cut")) {
            this.draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, (double[][])null, face);
         } else if (cap_callback.equals("round")) {
            this.draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, (double[][])null, face);
         }
      }

      this.gleSetJoinStyle(save_style);
   }

   private final void draw_fillets_and_join_n_norms(int ncp, double[][] trimmed_loop, double[][] untrimmed_loop, boolean[] is_trimmed, double[] bis_origin, double[] bis_vector, double[][] normals, float[] front_color, float[] back_color, double[] cut_vector, boolean face, String cap_callback) {
      int istop = false;
      int icnt = false;
      int icnt_prev = 0;
      int iloop = false;
      double[][] cap_loop = (double[][])null;
      double[][] norm_loop = (double[][])null;
      double[] sect = new double[3];
      double[] tmp_vec = new double[3];
      int save_style = 0;
      boolean was_trimmed = false;
      cap_loop = new double[ncp + 3][3];
      norm_loop = new double[ncp + 3][3];
      int icnt = 0;
      int iloop = 0;
      if (!is_trimmed[0]) {
         if ((this.gleGetJoinStyle() & 3) == 3 && (save_style & 4096) != 4096) {
            tmp_vec = matrix.VEC_SUM(trimmed_loop[0], bis_vector);
            sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[0], tmp_vec);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            norm_loop[iloop] = matrix.VEC_COPY(normals[0]);
            ++iloop;
         }

         cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[0]);
         norm_loop[iloop] = matrix.VEC_COPY(normals[0]);
         ++iloop;
         icnt_prev = icnt++;
      } else {
         was_trimmed = true;

         while(is_trimmed[icnt]) {
            icnt_prev = icnt++;
            if (icnt >= ncp) {
               return;
            }
         }
      }

      int istop;
      if ((this.gleGetJoinStyle() & 4096) == 4096) {
         istop = ncp;
      } else {
         istop = ncp - 1;
      }

      int save_style = this.gleGetJoinStyle();
      this.gleSetJoinStyle(save_style & -4097);

      while(icnt_prev < istop) {
         if (is_trimmed[icnt_prev] && is_trimmed[icnt]) {
         }

         if (is_trimmed[icnt_prev] && !is_trimmed[icnt]) {
            sect = intersect.INNERSECT(bis_origin, bis_vector, untrimmed_loop[icnt_prev], trimmed_loop[icnt]);
            this.draw_fillet_triangle_n_norms(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color, normals[icnt_prev], normals[icnt]);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            norm_loop[iloop] = matrix.VEC_COPY(normals[icnt_prev]);
            ++iloop;
            cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
            norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
            ++iloop;
         }

         if (!is_trimmed[icnt_prev] && !is_trimmed[icnt]) {
            cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
            norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
            ++iloop;
         }

         if (!is_trimmed[icnt_prev] && is_trimmed[icnt]) {
            was_trimmed = true;
            sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt_prev], untrimmed_loop[icnt]);
            this.draw_fillet_triangle_n_norms(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color, normals[icnt_prev], normals[icnt]);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            if ((this.gleGetJoinStyle() & 256) == 256) {
               norm_loop[iloop] = matrix.VEC_COPY(normals[icnt_prev]);
            } else {
               norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
            }

            ++iloop;
            if (iloop >= 3) {
               if (cap_callback.equals("cut")) {
                  this.draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
               } else if (cap_callback.equals("round")) {
                  this.draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
               }
            }

            iloop = 0;
         }

         ++icnt_prev;
         ++icnt;
         icnt %= ncp;
      }

      --icnt;
      icnt += ncp;
      icnt %= ncp;
      if (!is_trimmed[icnt] && iloop >= 2) {
         if ((this.gleGetJoinStyle() & 3) == 3 && (save_style & 4096) != 4096) {
            tmp_vec = matrix.VEC_SUM(trimmed_loop[icnt], bis_vector);
            sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt], tmp_vec);
            cap_loop[iloop] = matrix.VEC_COPY(sect);
            norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
            ++iloop;
         }

         if (!was_trimmed) {
            this.gleSetJoinStyle(save_style);
         }

         if (cap_callback.equals("cut")) {
            this.draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
         } else if (cap_callback.equals("round")) {
            this.draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
         }
      }

      this.gleSetJoinStyle(save_style);
   }

   private static void draw_fillet_triangle_plain(double[] va, double[] vb, double[] vc, boolean face, float[] front_color, float[] back_color) {
      if (front_color != null) {
         GL11.glColor3f(front_color[0], front_color[1], front_color[2]);
      }

      GL11.glBegin(5);
      if (face) {
         GL11.glVertex3d(va[0], va[1], va[2]);
         GL11.glVertex3d(vb[0], vb[1], vb[2]);
      } else {
         GL11.glVertex3d(vb[0], vb[1], vb[2]);
         GL11.glVertex3d(va[0], va[1], va[2]);
      }

      GL11.glVertex3d(vc[0], vc[1], vc[2]);
      GL11.glEnd();
   }

   private final void draw_fillet_triangle_n_norms(double[] va, double[] vb, double[] vc, boolean face, float[] front_color, float[] back_color, double[] na, double[] nb) {
      if (front_color != null) {
         GL11.glColor3f(front_color[0], front_color[1], front_color[2]);
      }

      GL11.glBegin(5);
      if ((this.gleGetJoinStyle() & 256) == 256) {
         GL11.glNormal3d(na[0], na[1], na[2]);
         if (face) {
            GL11.glVertex3d(va[0], va[1], va[2]);
            GL11.glVertex3d(vb[0], vb[1], vb[2]);
         } else {
            GL11.glVertex3d(vb[0], vb[1], vb[2]);
            GL11.glVertex3d(va[0], va[1], va[2]);
         }

         GL11.glNormal3d(vc[0], vc[1], vc[2]);
      } else {
         if (face) {
            GL11.glNormal3d(na[0], na[1], na[2]);
            GL11.glVertex3d(va[0], va[1], va[2]);
            GL11.glNormal3d(nb[0], nb[1], nb[2]);
            GL11.glVertex3d(vb[0], vb[1], vb[2]);
         } else {
            GL11.glNormal3d(nb[0], nb[1], nb[2]);
            GL11.glVertex3d(vb[0], vb[1], vb[2]);
            GL11.glNormal3d(na[0], na[1], na[2]);
            GL11.glVertex3d(va[0], va[1], va[2]);
            GL11.glNormal3d(nb[0], nb[1], nb[2]);
         }

         GL11.glVertex3d(vc[0], vc[1], vc[2]);
      }

      GL11.glEnd();
   }

   private final void draw_cut_style_cap_callback(int iloop, double[][] cap, float[] face_color, double[] cut_vector, double[] bisect_vector, double[][] norms, boolean frontwards) {
      double[] previous_vertex = null;
      double[] first_vertex = null;
      boolean is_colinear = false;
      GLUtessellator tobj = GLU.gluNewTess();
      tobj.gluTessProperty(100140, 100130.0D);
      tobj.gluTessCallback(100101, this.tessCallback);
      tobj.gluTessCallback(100100, this.tessCallback);
      tobj.gluTessCallback(100102, this.tessCallback);
      tobj.gluTessCallback(100103, this.tessCallback);
      if (face_color != null) {
         GL11.glColor3f(face_color[0], face_color[1], face_color[2]);
      }

      int i;
      double[] previous_vertex;
      double[] first_vertex;
      if (frontwards) {
         if (cut_vector != null) {
            if (cut_vector[2] < 0.0D) {
               cut_vector = matrix.VEC_SCALE(-1.0D, cut_vector);
            }

            GL11.glNormal3d(cut_vector[0], cut_vector[1], cut_vector[2]);
         }

         tobj.gluTessBeginPolygon((Object)null);
         tobj.gluTessBeginContour();
         first_vertex = null;
         previous_vertex = cap[iloop - 1];

         for(i = 0; i < iloop - 1; ++i) {
            is_colinear = intersect.COLINEAR(previous_vertex, cap[i], cap[i + 1]);
            if (!is_colinear) {
               tobj.gluTessVertex(cap[i], 0, cap[i]);
               previous_vertex = cap[i];
               if (first_vertex == null) {
                  first_vertex = previous_vertex;
               }
            }
         }

         if (first_vertex == null) {
            first_vertex = cap[0];
         }

         is_colinear = intersect.COLINEAR(previous_vertex, cap[iloop - 1], first_vertex);
         if (!is_colinear) {
            tobj.gluTessVertex(cap[iloop - 1], 0, cap[iloop - 1]);
         }

         tobj.gluTessEndContour();
         tobj.gluTessEndPolygon();
      } else {
         if (cut_vector != null) {
            if (cut_vector[2] > 0.0D) {
               cut_vector = matrix.VEC_SCALE(-1.0D, cut_vector);
            }

            GL11.glNormal3d(cut_vector[0], cut_vector[1], cut_vector[2]);
         }

         tobj.gluTessBeginPolygon((Object)null);
         tobj.gluTessBeginContour();
         first_vertex = null;
         previous_vertex = cap[0];

         for(i = iloop - 1; i > 0; --i) {
            is_colinear = intersect.COLINEAR(previous_vertex, cap[i], cap[i - 1]);
            if (!is_colinear) {
               tobj.gluTessVertex(cap[i], 0, cap[i]);
               previous_vertex = cap[i];
               if (first_vertex == null) {
                  first_vertex = previous_vertex;
               }
            }
         }

         if (first_vertex == null) {
            first_vertex = cap[iloop - 1];
         }

         is_colinear = intersect.COLINEAR(previous_vertex, cap[0], first_vertex);
         if (!is_colinear) {
            tobj.gluTessVertex(cap[0], 0, cap[0]);
         }

         tobj.gluTessEndContour();
         tobj.gluTessEndPolygon();
      }

      tobj.gluDeleteTess();
   }

   private final void draw_round_style_cap_callback(int ncp, double[][] cap, float[] face_color, double[] cut, double[] bi, double[][] norms, boolean frontwards) {
      double[] axis = new double[3];
      double[] xycut = new double[3];
      double theta = 0.0D;
      double[][] last_contour = (double[][])null;
      double[][] next_contour = (double[][])null;
      double[][] last_norm = (double[][])null;
      double[][] next_norm = (double[][])null;
      double[] cap_z = null;
      double[][] tmp = (double[][])null;
      int i = false;
      int j = false;
      int k = false;
      double[][] m = new double[4][4];
      if (face_color != null) {
         GL11.glColor3f(face_color[0], face_color[1], face_color[2]);
      }

      if (cut != null) {
         if (cut[2] > 0.0D) {
            cut = matrix.VEC_SCALE(-1.0D, cut);
         }

         if (bi[2] < 0.0D) {
            bi = matrix.VEC_SCALE(-1.0D, bi);
         }

         axis = matrix.VEC_CROSS_PRODUCT(cut, bi);
         if (!frontwards) {
            cut = matrix.VEC_SCALE(-1.0D, cut);
         }

         xycut[0] = 0.0D;
         xycut[1] = 0.0D;
         xycut[2] = 1.0D;
         xycut = matrix.VEC_PERP(cut, xycut);
         xycut = matrix.VEC_NORMALIZE(xycut);
         theta = matrix.VEC_DOT_PRODUCT(xycut, cut);
         theta = Math.acos(theta);
         theta /= (double)this.__ROUND_TESS_PIECES;
         m = matrix.urot_axis_d(theta, axis);
         last_contour = new double[ncp][3];
         next_contour = new double[ncp][3];
         double[] cap_z = new double[ncp];
         last_norm = new double[ncp][3];
         next_norm = new double[ncp][3];
         int j;
         if (frontwards) {
            for(j = 0; j < ncp; ++j) {
               last_contour[j][0] = cap[j][0];
               last_contour[j][1] = cap[j][1];
               last_contour[j][2] = cap_z[j] = cap[j][2];
            }

            if (norms != null) {
               for(j = 0; j < ncp; ++j) {
                  last_norm[j] = matrix.VEC_COPY(norms[j]);
               }
            }
         } else {
            int k;
            for(j = 0; j < ncp; ++j) {
               k = ncp - j - 1;
               last_contour[k][0] = cap[j][0];
               last_contour[k][1] = cap[j][1];
               last_contour[k][2] = cap_z[k] = cap[j][2];
            }

            if (norms != null) {
               if ((this.gleGetJoinStyle() & 256) == 256) {
                  for(j = 0; j < ncp - 1; ++j) {
                     k = ncp - j - 2;
                     last_norm[k] = matrix.VEC_COPY(norms[j]);
                  }
               } else {
                  for(j = 0; j < ncp; ++j) {
                     k = ncp - j - 1;
                     last_norm[k] = matrix.VEC_COPY(norms[j]);
                  }
               }
            }
         }

         for(int i = 0; i < this.__ROUND_TESS_PIECES; ++i) {
            for(j = 0; j < ncp; ++j) {
               next_contour[j][2] -= cap_z[j];
               last_contour[j][2] -= cap_z[j];
               next_contour[j] = matrix.MAT_DOT_VEC_3X3(m, last_contour[j]);
               next_contour[j][2] += cap_z[j];
               last_contour[j][2] += cap_z[j];
            }

            if (norms != null) {
               for(j = 0; j < ncp; ++j) {
                  next_norm[j] = matrix.MAT_DOT_VEC_3X3(m, last_norm[j]);
               }
            }

            if (norms == null) {
               this.draw_segment_plain(ncp, next_contour, last_contour, 0, 0.0D);
            } else if ((this.gleGetJoinStyle() & 256) == 256) {
               this.draw_binorm_segment_facet_n(ncp, next_contour, last_contour, next_norm, last_norm, 0, 0.0D);
            } else {
               this.draw_binorm_segment_edge_n(ncp, next_contour, last_contour, next_norm, last_norm, 0, 0.0D);
            }

            tmp = next_contour;
            next_contour = last_contour;
            last_contour = tmp;
            tmp = next_norm;
            next_norm = last_norm;
            last_norm = tmp;
         }

      }
   }

   class tessellCallBack implements GLUtessellatorCallback {
      public tessellCallBack(GLU glu) {
      }

      public void begin(int type) {
         GL11.glBegin(type);
      }

      public void end() {
         GL11.glEnd();
      }

      public void vertex(Object vertexData) {
         if (vertexData instanceof double[]) {
            double[] pointer = (double[])((double[])vertexData);
            if (pointer.length == 6) {
               GL11.glColor3d(pointer[3], pointer[4], pointer[5]);
            }

            GL11.glVertex3d(pointer[0], pointer[1], pointer[2]);
         }

      }

      public void vertexData(Object vertexData, Object polygonData) {
      }

      public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {
         double[] vertex = new double[]{coords[0], coords[1], coords[2], 0.0D, 0.0D, 0.0D};

         for(int i = 3; i < 6; ++i) {
            vertex[i] = (double)weight[0] * ((double[])((double[])data[0]))[i] + (double)weight[1] * ((double[])((double[])data[1]))[i] + (double)weight[2] * ((double[])((double[])data[2]))[i] + (double)weight[3] * ((double[])((double[])data[3]))[i];
         }

         outData[0] = vertex;
      }

      public void combineData(double[] coords, Object[] data, float[] weight, Object[] outData, Object polygonData) {
      }

      public void error(int errnum) {
         String estring = GLU.gluErrorString(errnum);
         System.err.println("Tessellation Error: " + estring);
      }

      public void beginData(int type, Object polygonData) {
      }

      public void endData(Object polygonData) {
      }

      public void edgeFlag(boolean boundaryEdge) {
      }

      public void edgeFlagData(boolean boundaryEdge, Object polygonData) {
      }

      public void errorData(int errnum, Object polygonData) {
      }
   }
}
