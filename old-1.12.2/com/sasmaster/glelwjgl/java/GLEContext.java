package com.sasmaster.glelwjgl.java;

public class GLEContext {
   public static final String VERSION = new String("$Id: GLEContext.java,v 1.1 1998/05/03 16:18:47 descarte Exp descarte $");
   private int joinStyle = 1;
   protected int ncp = 0;
   protected double[][] contour = (double[][])null;
   protected double[][] contourNormal = (double[][])null;
   protected double[] up = null;
   protected int npoints = 0;
   protected double[][] pointArray = (double[][])null;
   protected float[][] colourArray = (float[][])null;
   protected double[][][] xformArray = (double[][][])null;

   protected final int getJoinStyle() {
      return this.joinStyle;
   }

   protected final void setJoinStyle(int style) {
      this.joinStyle = style;
   }
}
