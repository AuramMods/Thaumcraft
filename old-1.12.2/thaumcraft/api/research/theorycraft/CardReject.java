package thaumcraft.api.research.theorycraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CardReject extends TheorycraftCard {
   private String cat1;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74778_a("cat", this.cat1);
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.cat1 = nbt.func_74779_i("cat");
   }

   public int getInspirationCost() {
      return 0;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.reject.name", new Object[]{TextFormatting.DARK_BLUE + "" + TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category." + this.cat1, new Object[0])).func_150260_c() + TextFormatting.RESET + "" + TextFormatting.BOLD})).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.reject.text", new Object[]{TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category." + this.cat1, new Object[0])).func_150254_d() + TextFormatting.RESET})).func_150260_c();
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      ArrayList<String> s = new ArrayList();
      Iterator var4 = data.categoryTotals.keySet().iterator();

      while(var4.hasNext()) {
         String c = (String)var4.next();
         if (!data.categoriesBlocked.contains(c)) {
            s.add(c);
         }
      }

      if (s.size() < 1) {
         return false;
      } else {
         Random r = new Random(this.getSeed());
         this.cat1 = (String)s.get(r.nextInt(s.size()));
         return this.cat1 != null;
      }
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (this.cat1 == null) {
         return false;
      } else {
         data.categoriesBlocked.add(this.cat1);
         return true;
      }
   }
}
