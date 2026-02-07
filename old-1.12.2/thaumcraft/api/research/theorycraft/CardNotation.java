package thaumcraft.api.research.theorycraft;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CardNotation extends TheorycraftCard {
   private String cat1;
   private String cat2;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74778_a("cat1", this.cat1);
      nbt.func_74778_a("cat2", this.cat2);
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.cat1 = nbt.func_74779_i("cat1");
      this.cat2 = nbt.func_74779_i("cat2");
   }

   public boolean isAidOnly() {
      return true;
   }

   public int getInspirationCost() {
      return 1;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.notation.name", new Object[0])).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.notation.text", new Object[]{TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category." + this.cat1, new Object[0])).func_150254_d() + TextFormatting.RESET, TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category." + this.cat2, new Object[0])).func_150254_d() + TextFormatting.RESET})).func_150260_c();
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      if (data.categoryTotals.size() < 2) {
         return false;
      } else {
         int lVal = Integer.MAX_VALUE;
         String lKey = "";
         int hVal = 0;
         String hKey = "";
         Iterator var7 = data.categoryTotals.keySet().iterator();

         while(var7.hasNext()) {
            String category = (String)var7.next();
            int q = data.getTotal(category);
            if (q < lVal) {
               lVal = q;
               lKey = category;
            }

            if (q > hVal) {
               hVal = q;
               hKey = category;
            }
         }

         if (!hKey.equals(lKey) && lVal > 0) {
            this.cat1 = lKey;
            this.cat2 = hKey;
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (this.cat1 != null && this.cat2 != null) {
         int lVal = data.getTotal(this.cat1);
         data.addTotal(this.cat1, -lVal);
         data.addTotal(this.cat2, lVal / 2 + MathHelper.func_76136_a(player.func_70681_au(), 0, lVal / 2));
         return true;
      } else {
         return false;
      }
   }
}
