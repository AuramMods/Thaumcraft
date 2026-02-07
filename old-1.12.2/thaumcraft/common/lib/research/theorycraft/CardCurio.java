package thaumcraft.common.lib.research.theorycraft;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
import thaumcraft.common.items.curios.ItemCurio;
import thaumcraft.common.lib.utils.InventoryUtils;

public class CardCurio extends TheorycraftCard {
   ItemStack curio = null;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74782_a("stack", this.curio.serializeNBT());
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.curio = ItemStack.func_77949_a(nbt.func_74775_l("stack"));
   }

   public int getInspirationCost() {
      return 1;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.curio.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.curio.text", new Object[0])).func_150254_d();
   }

   public ItemStack[] getRequiredItems() {
      return new ItemStack[]{this.curio};
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      Random r = new Random(this.getSeed());
      ArrayList<ItemStack> curios = new ArrayList();
      ItemStack[] var5 = player.field_71071_by.field_70462_a;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ItemStack stack = var5[var7];
         if (stack != null && stack.func_77973_b() instanceof ItemCurio) {
            curios.add(stack.func_77946_l());
         }
      }

      if (!curios.isEmpty()) {
         this.curio = (ItemStack)curios.get(r.nextInt(curios.size()));
      }

      return this.curio != null;
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (InventoryUtils.consumeInventoryItem(player, this.getRequiredItems()[0], false, false)) {
         data.addTotal("BASICS", 5);
         String[] s = (String[])ResearchCategories.researchCategories.keySet().toArray(new String[0]);
         data.addTotal(s[player.func_70681_au().nextInt(s.length)], 5);
         String type = ((ItemCurio)this.getRequiredItems()[0].func_77973_b()).getVariantNames()[this.getRequiredItems()[0].func_77952_i()];
         byte var6 = -1;
         switch(type.hashCode()) {
         case -1409612218:
            if (type.equals("arcane")) {
               var6 = 0;
            }
            break;
         case -921822312:
            if (type.equals("preserved")) {
               var6 = 1;
            }
            break;
         case -916376058:
            if (type.equals("twisted")) {
               var6 = 5;
            }
            break;
         case -862592328:
            if (type.equals("ancient")) {
               var6 = 2;
            }
            break;
         case -32226363:
            if (type.equals("eldritch")) {
               var6 = 3;
            }
            break;
         case 108524171:
            if (type.equals("rites")) {
               var6 = 6;
            }
            break;
         case 1549887614:
            if (type.equals("knowledge")) {
               var6 = 4;
            }
         }

         label57:
         switch(var6) {
         case 0:
            data.addTotal("THAUMATURGY", 20);
            break;
         case 1:
            data.addTotal("ALCHEMY", 20);
            break;
         case 2:
            data.addTotal("GOLEMANCY", 20);
            break;
         case 3:
            data.addTotal("ELDTICH", 20);
            break;
         case 4:
            int a = 0;

            while(true) {
               if (a >= 5) {
                  break label57;
               }

               data.addTotal(s[player.func_70681_au().nextInt(s.length)], 5);
               ++a;
            }
         case 5:
            data.addTotal("ARTIFICE", 20);
            break;
         case 6:
            data.addTotal("ELDTICH", MathHelper.func_76136_a(player.func_70681_au(), 10, 15));
            data.addTotal("THAUMATURGY", MathHelper.func_76136_a(player.func_70681_au(), 5, 15));
            break;
         default:
            data.addTotal("BASICS", 25);
         }

         if (player.func_70681_au().nextBoolean()) {
            ++data.bonusDraws;
         }

         if (player.func_70681_au().nextBoolean()) {
            ++data.bonusDraws;
         }

         return true;
      } else {
         return false;
      }
   }
}
