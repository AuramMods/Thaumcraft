package com.sasmaster.glelwjgl.java;

public interface GLE {
   String VERSION = new String("$Id: GLE.java,v 1.3 1998/05/02 12:06:39 descarte Exp descarte $");
   int SUMMARY = 0;
   int VERBOSE = 1;
   int TUBE_JN_RAW = 1;
   int TUBE_JN_ANGLE = 2;
   int TUBE_JN_CUT = 3;
   int TUBE_JN_ROUND = 4;
   int TUBE_JN_MASK = 15;
   int TUBE_JN_CAP = 16;
   int TUBE_NORM_FACET = 256;
   int TUBE_NORM_EDGE = 512;
   int TUBE_NORM_PATH_EDGE = 1024;
   int TUBE_NORM_MASK = 3840;
   int TUBE_CONTOUR_CLOSED = 4096;
   int GLE_TEXTURE_ENABLE = 65536;
   int GLE_TEXTURE_STYLE_MASK = 255;
   int GLE_TEXTURE_VERTEX_FLAT = 1;
   int GLE_TEXTURE_NORMAL_FLAT = 2;
   int GLE_TEXTURE_VERTEX_CYL = 3;
   int GLE_TEXTURE_NORMAL_CYL = 4;
   int GLE_TEXTURE_VERTEX_SPH = 5;
   int GLE_TEXTURE_NORMAL_SPH = 6;
   int GLE_TEXTURE_VERTEX_MODEL_FLAT = 7;
   int GLE_TEXTURE_NORMAL_MODEL_FLAT = 8;
   int GLE_TEXTURE_VERTEX_MODEL_CYL = 9;
   int GLE_TEXTURE_NORMAL_MODEL_CYL = 10;
   int GLE_TEXTURE_VERTEX_MODEL_SPH = 11;
   int GLE_TEXTURE_NORMAL_MODEL_SPH = 12;

   int gleGetJoinStyle();

   void gleSetJoinStyle(int var1);

   void gleTextureMode(int var1);

   void glePolyCylinder(int var1, double[][] var2, float[][] var3, double var4, float var6, float var7) throws GLEException;

   void glePolyCone(int var1, double[][] var2, float[][] var3, double[] var4, float var5, float var6) throws GLEException;

   void gleExtrusion(int var1, double[][] var2, double[][] var3, double[] var4, int var5, double[][] var6, float[][] var7) throws GLEException;

   void gleTwistExtrusion(int var1, double[][] var2, double[][] var3, double[] var4, int var5, double[][] var6, float[][] var7, double[] var8) throws GLEException;

   void gleSuperExtrusion(int var1, double[][] var2, double[][] var3, double[] var4, int var5, double[][] var6, float[][] var7, double[][][] var8) throws GLEException;

   void gleSpiral(int var1, double[][] var2, double[][] var3, double[] var4, double var5, double var7, double var9, double var11, double[][] var13, double[][] var14, double var15, double var17) throws GLEException;

   void gleLathe(int var1, double[][] var2, double[][] var3, double[] var4, double var5, double var7, double var9, double var11, double[][] var13, double[][] var14, double var15, double var17) throws GLEException;

   void gleHelicoid(double var1, double var3, double var5, double var7, double var9, double[][] var11, double[][] var12, double var13, double var15) throws GLEException;

   void gleToroid(double var1, double var3, double var5, double var7, double var9, double[][] var11, double[][] var12, double var13, double var15) throws GLEException;

   void gleScrew(int var1, double[][] var2, double[][] var3, double[] var4, double var5, double var7, double var9) throws GLEException;
}
