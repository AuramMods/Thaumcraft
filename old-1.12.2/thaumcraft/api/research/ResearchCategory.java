package thaumcraft.api.research;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ResearchCategory {
   public int minDisplayColumn;
   public int minDisplayRow;
   public int maxDisplayColumn;
   public int maxDisplayRow;
   public ResourceLocation icon;
   public ResourceLocation background;
   public ResourceLocation background2;
   public String researchKey;
   public String key;
   public AspectList formula;
   public Map<String, ResearchEntry> research = new HashMap();

   public ResearchCategory(String key, String researchkey, AspectList formula, ResourceLocation icon, ResourceLocation background) {
      this.key = key;
      this.researchKey = researchkey;
      this.icon = icon;
      this.background = background;
      this.background2 = null;
      this.formula = formula;
   }

   public ResearchCategory(String key, String researchKey, AspectList formula, ResourceLocation icon, ResourceLocation background, ResourceLocation background2) {
      this.key = key;
      this.researchKey = researchKey;
      this.icon = icon;
      this.background = background;
      this.background2 = background2;
      this.formula = formula;
   }

   public int applyFormula(AspectList as) {
      return this.applyFormula(as, 1.0D);
   }

   public int applyFormula(AspectList as, double mod) {
      if (this.formula == null) {
         return 0;
      } else {
         double total = 0.0D;
         Aspect[] var6 = this.formula.getAspects();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Aspect aspect = var6[var8];
            total += mod * mod * (double)as.getAmount(aspect) * ((double)this.formula.getAmount(aspect) / 10.0D);
         }

         if (total > 0.0D) {
            total = Math.sqrt(total);
         }

         return MathHelper.func_76143_f(total);
      }
   }
}
