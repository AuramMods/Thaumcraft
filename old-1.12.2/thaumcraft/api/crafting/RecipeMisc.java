package thaumcraft.api.crafting;

import net.minecraft.item.ItemStack;

public class RecipeMisc {
   RecipeMisc.MiscRecipeType type;
   ItemStack input;
   ItemStack output;

   public RecipeMisc(ItemStack input, ItemStack output, RecipeMisc.MiscRecipeType type) {
      this.input = input;
      this.output = output;
      this.type = type;
   }

   public RecipeMisc.MiscRecipeType getType() {
      return this.type;
   }

   public void setType(RecipeMisc.MiscRecipeType type) {
      this.type = type;
   }

   public ItemStack getInput() {
      return this.input;
   }

   public void setInput(ItemStack input) {
      this.input = input;
   }

   public ItemStack getOutput() {
      return this.output;
   }

   public void setOutput(ItemStack output) {
      this.output = output;
   }

   public static enum MiscRecipeType {
      SMELTING;
   }
}
