package thaumcraft.common.lib.crafting;

import baubles.api.IBauble;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.events.PlayerEvents;

public class InfusionRunicAugmentRecipe extends InfusionRecipe {
   private ItemStack[] components;

   public InfusionRunicAugmentRecipe() {
      super("RUNICSHIELDING", (Object)null, 0, (AspectList)null, (Object)null, new ItemStack[]{new ItemStack(ItemsTC.amber), new ItemStack(ItemsTC.salisMundus)});
   }

   public InfusionRunicAugmentRecipe(ItemStack in) {
      super("RUNICSHIELDING", (Object)null, 0, (AspectList)null, in, new ItemStack[]{new ItemStack(ItemsTC.salisMundus), new ItemStack(ItemsTC.amber)});
      this.components = new ItemStack[]{new ItemStack(ItemsTC.salisMundus), new ItemStack(ItemsTC.amber)};
      int fc = PlayerEvents.getRunicCharge(in);
      if (fc > 0) {
         ArrayList<ItemStack> com = new ArrayList();
         com.add(new ItemStack(ItemsTC.salisMundus));
         com.add(new ItemStack(ItemsTC.amber));
         int c = 0;

         while(c < fc) {
            ++c;
            com.add(new ItemStack(ItemsTC.amber));
         }

         this.components = (ItemStack[])com.toArray(this.components);
      }

   }

   public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
      if (!ThaumcraftCapabilities.knowsResearch(player, this.research)) {
         return false;
      } else if (!(central.func_77973_b() instanceof ItemArmor) && !(central.func_77973_b() instanceof IBauble)) {
         return false;
      } else {
         ItemStack i2 = null;
         ArrayList<ItemStack> ii = new ArrayList();
         Iterator var7 = input.iterator();

         while(var7.hasNext()) {
            ItemStack is = (ItemStack)var7.next();
            ii.add(is.func_77946_l());
         }

         ItemStack[] var13 = this.getComponents(central);
         int var14 = var13.length;

         for(int var9 = 0; var9 < var14; ++var9) {
            ItemStack comp = var13[var9];
            boolean b = false;

            for(int a = 0; a < ii.size(); ++a) {
               i2 = ((ItemStack)ii.get(a)).func_77946_l();
               if (ThaumcraftApiHelper.areItemStacksEqualForCrafting(i2, comp)) {
                  ii.remove(a);
                  b = true;
                  break;
               }
            }

            if (!b) {
               return false;
            }
         }

         return ii.size() == 0;
      }
   }

   public Object getRecipeOutput(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      if (input == null) {
         return null;
      } else {
         ItemStack out = input.func_77946_l();
         int base = PlayerEvents.getRunicCharge(input) + 1;
         out.func_77983_a("TC.RUNIC", new NBTTagByte((byte)base));
         return out;
      }
   }

   public AspectList getAspects(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      AspectList out = new AspectList();
      int vis = 20 + (int)(20.0D * Math.pow(2.0D, (double)PlayerEvents.getRunicCharge(input)));
      if (vis > 0) {
         out.add(Aspect.PROTECT, vis);
         out.add(Aspect.CRYSTAL, vis / 2);
         out.add(Aspect.ENERGY, vis / 2);
      }

      return out;
   }

   public int getInstability(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      int i = 5 + PlayerEvents.getRunicCharge(input) / 2;
      return i;
   }

   public ItemStack[] getComponents(ItemStack input) {
      ArrayList<ItemStack> com = new ArrayList();
      com.add(new ItemStack(ItemsTC.salisMundus));
      com.add(new ItemStack(ItemsTC.amber));
      int fc = PlayerEvents.getRunicCharge(input);
      if (fc > 0) {
         for(int c = 0; c < fc; ++c) {
            com.add(new ItemStack(ItemsTC.amber));
         }
      }

      return (ItemStack[])com.toArray(new ItemStack[0]);
   }

   public ItemStack[] getComponents() {
      return this.components;
   }
}
