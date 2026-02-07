package thaumcraft.common.lib.research.theorycraft;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.InventoryUtils;

public class CardMindOverMatter extends TheorycraftCard {
   ItemStack stack;
   static ItemStack[] options;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74782_a("stack", this.stack.serializeNBT());
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.stack = ItemStack.func_77949_a(nbt.func_74775_l("stack"));
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      Random r = new Random(this.getSeed());
      this.stack = options[r.nextInt(options.length)].func_77946_l();
      return this.stack != null;
   }

   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ARTIFICE";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.mindmatter.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.mindmatter.text", new Object[]{this.getVal()})).func_150254_d();
   }

   private int getVal() {
      int q = 5;

      try {
         q = (int)((double)q + Math.sqrt((double)ThaumcraftCraftingManager.getObjectTags(this.stack).visSize()));
      } catch (Exception var3) {
      }

      return q;
   }

   public ItemStack[] getRequiredItems() {
      return new ItemStack[]{this.stack};
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (InventoryUtils.consumeInventoryItem(player, this.stack, false, false)) {
         data.addTotal(this.getResearchCategory(), this.getVal());
         return true;
      } else {
         return false;
      }
   }

   static {
      options = new ItemStack[]{new ItemStack(ItemsTC.ingots, 1, 2), new ItemStack(Items.field_151043_k), new ItemStack(Items.field_151042_j), new ItemStack(ItemsTC.gear), new ItemStack(ItemsTC.plate), new ItemStack(ItemsTC.plate, 1, 1), new ItemStack(ItemsTC.mirroredGlass), new ItemStack(ItemsTC.baubles), new ItemStack(ItemsTC.baubles, 1, 1), new ItemStack(ItemsTC.baubles, 1, 2), new ItemStack(Items.field_151072_bj), new ItemStack(Items.field_185161_cS), new ItemStack(Items.field_151059_bz), new ItemStack(Items.field_151114_aO), new ItemStack(Items.field_151064_bs), new ItemStack(Items.field_179563_cD), new ItemStack(Items.field_179562_cC), new ItemStack(Items.field_151128_bU), new ItemStack(Items.field_179556_br), new ItemStack(Items.field_151070_bp)};
   }
}
