package thaumcraft.api.research;

import net.minecraft.util.text.translation.I18n;

public class ResearchAddendum {
   String text;
   String[] recipes;
   String[] research;

   public String getText() {
      return this.text;
   }

   public String getTextLocalized() {
      return I18n.func_74838_a(this.getText());
   }

   public void setText(String text) {
      this.text = text;
   }

   public String[] getRecipes() {
      return this.recipes;
   }

   public void setRecipes(String[] recipes) {
      this.recipes = recipes;
   }

   public String[] getResearch() {
      return this.research;
   }

   public void setResearch(String[] research) {
      this.research = research;
   }
}
