package thaumcraft.codechicken.lib.render.uv;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class UVScale extends UVTransformation {
   double su;
   double sv;

   public UVScale(double scaleu, double scalev) {
      this.su = scaleu;
      this.sv = scalev;
   }

   public UVScale(double d) {
      this(d, d);
   }

   public void apply(UV uv) {
      uv.u *= this.su;
      uv.v *= this.sv;
   }

   public UVTransformation inverse() {
      return new UVScale(1.0D / this.su, 1.0D / this.sv);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "UVScale(" + new BigDecimal(this.su, cont) + ", " + new BigDecimal(this.sv, cont) + ")";
   }
}
