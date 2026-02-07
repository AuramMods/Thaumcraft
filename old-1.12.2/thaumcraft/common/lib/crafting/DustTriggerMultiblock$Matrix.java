package thaumcraft.common.lib.crafting;

import thaumcraft.api.crafting.Part;

class DustTriggerMultiblock$Matrix {
   private int rows;
   private int cols;
   private Part[][] matrix;
   // $FF: synthetic field
   final DustTriggerMultiblock this$0;

   public DustTriggerMultiblock$Matrix(DustTriggerMultiblock this$0, Part[][] matrix) {
      this.this$0 = this$0;
      this.rows = matrix.length;
      this.cols = matrix[0].length;
      this.matrix = new Part[this.rows][this.cols];

      for(int i = 0; i < this.rows; ++i) {
         for(int j = 0; j < this.cols; ++j) {
            this.matrix[i][j] = matrix[i][j];
         }
      }

   }

   public void Rotate90DegRight(int times) {
      for(int a = 0; a < times; ++a) {
         Part[][] newMatrix = new Part[this.cols][this.rows];

         int i;
         for(i = 0; i < this.rows; ++i) {
            for(int j = 0; j < this.cols; ++j) {
               newMatrix[j][this.rows - i - 1] = this.matrix[i][j];
            }
         }

         this.matrix = newMatrix;
         i = this.rows;
         this.rows = this.cols;
         this.cols = i;
      }

   }

   // $FF: synthetic method
   static int access$000(DustTriggerMultiblock$Matrix x0) {
      return x0.rows;
   }

   // $FF: synthetic method
   static int access$100(DustTriggerMultiblock$Matrix x0) {
      return x0.cols;
   }

   // $FF: synthetic method
   static Part[][] access$200(DustTriggerMultiblock$Matrix x0) {
      return x0.matrix;
   }
}
