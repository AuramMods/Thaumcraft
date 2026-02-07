package thaumcraft.common.items.resources;

import java.util.Iterator;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.ItemTCEssentiaContainer;

public class ItemCrystalEssence extends ItemTCEssentiaContainer {
   public ItemCrystalEssence() {
      super("crystal_essence", 1);
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      Iterator var4 = Aspect.aspects.values().iterator();

      while(var4.hasNext()) {
         Aspect tag = (Aspect)var4.next();
         ItemStack i = new ItemStack(this);
         this.setAspects(i, (new AspectList()).add(tag, this.base));
         par3List.add(i);
      }

   }
}
