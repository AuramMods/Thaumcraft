package thaumcraft.api.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TheorycraftCard {
   private long seed = -1L;

   public long getSeed() {
      if (this.seed < 0L) {
         this.setSeed(System.nanoTime());
      }

      return this.seed;
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      return true;
   }

   public boolean isAidOnly() {
      return false;
   }

   public abstract int getInspirationCost();

   public String getResearchCategory() {
      return null;
   }

   public abstract String getLocalizedName();

   public abstract String getLocalizedText();

   public ItemStack[] getRequiredItems() {
      return null;
   }

   public abstract boolean activate(EntityPlayer var1, ResearchTableData var2);

   public void setSeed(long seed) {
      this.seed = Math.abs(seed);
   }

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74772_a("seed", this.seed);
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      if (nbt != null) {
         this.seed = nbt.func_74763_f("seed");
      }
   }
}
