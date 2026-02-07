package thaumcraft.api.research.theorycraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;

public class CardAnalyze extends TheorycraftCard {
   String cat = null;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74778_a("cat", this.cat);
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.cat = nbt.func_74779_i("cat");
   }

   public String getResearchCategory() {
      return this.cat;
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      Random r = new Random(this.getSeed());
      ArrayList<String> cats = new ArrayList();
      Iterator var5 = ResearchCategories.researchCategories.values().iterator();

      while(var5.hasNext()) {
         ResearchCategory rc = (ResearchCategory)var5.next();
         if (rc.key != "BASICS" && ThaumcraftCapabilities.getKnowledge(player).getKnowledge(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, (ResearchCategory)ResearchCategories.researchCategories.get(this.cat)) > 0) {
            cats.add(rc.key);
         }
      }

      if (cats.size() > 0) {
         this.cat = (String)cats.get(r.nextInt(cats.size()));
      }

      return this.cat != null;
   }

   public int getInspirationCost() {
      return 2;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.analyze.name", new Object[]{TextFormatting.DARK_BLUE + "" + TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category." + this.cat, new Object[0])).func_150254_d() + TextFormatting.RESET})).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.analyze.text", new Object[]{TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category." + this.cat, new Object[0])).func_150254_d() + TextFormatting.RESET, TextFormatting.BOLD + (new TextComponentTranslation("tc.research_category.BASICS", new Object[0])).func_150254_d() + TextFormatting.RESET})).func_150260_c();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      ResearchCategory rc = ResearchCategories.getResearchCategory(this.cat);
      int k = ThaumcraftCapabilities.getKnowledge(player).getKnowledge(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, rc);
      if (k >= 1) {
         data.addTotal("BASICS", 5);
         ThaumcraftCapabilities.getKnowledge(player).addKnowledge(IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, rc, -IPlayerKnowledge.EnumKnowledgeType.OBSERVATION.getProgression());
         data.addTotal(this.cat, MathHelper.func_76136_a(player.func_70681_au(), 25, 50));
         return true;
      } else {
         return false;
      }
   }
}
