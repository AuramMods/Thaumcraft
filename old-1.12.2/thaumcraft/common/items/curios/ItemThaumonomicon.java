package thaumcraft.common.items.curios;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.lib.CommandThaumcraft;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.research.ResearchManager;

public class ItemThaumonomicon extends ItemTCBase {
   public ItemThaumonomicon() {
      super("thaumonomicon", "normal", "cheat");
      this.func_77627_a(true);
      this.func_77625_d(1);
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      if (Config.allowCheatSheet) {
         par3List.add(new ItemStack(this, 1, 1));
      }

   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.func_77624_a(stack, player, list, par4);
      if (stack.func_77952_i() == 1) {
         list.add(TextFormatting.DARK_PURPLE + "Creative only");
      }

   }

   public ActionResult<ItemStack> func_77659_a(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
      if (!world.field_72995_K) {
         Collection rc;
         Iterator var6;
         ResearchCategory cat;
         Collection rl;
         Iterator var9;
         ResearchEntry ri;
         if (Config.allowCheatSheet && stack.func_77952_i() == 1) {
            rc = ResearchCategories.researchCategories.values();
            var6 = rc.iterator();

            while(var6.hasNext()) {
               cat = (ResearchCategory)var6.next();
               rl = cat.research.values();
               var9 = rl.iterator();

               while(var9.hasNext()) {
                  ri = (ResearchEntry)var9.next();
                  CommandThaumcraft.giveRecursiveResearch(player, ri.getKey());
               }
            }
         } else {
            rc = ResearchCategories.researchCategories.values();
            var6 = rc.iterator();

            label65:
            while(var6.hasNext()) {
               cat = (ResearchCategory)var6.next();
               rl = cat.research.values();
               var9 = rl.iterator();

               while(true) {
                  do {
                     do {
                        if (!var9.hasNext()) {
                           continue label65;
                        }

                        ri = (ResearchEntry)var9.next();
                     } while(!ThaumcraftCapabilities.knowsResearch(player, ri.getKey()));
                  } while(ri.getSiblings() == null);

                  String[] var11 = ri.getSiblings();
                  int var12 = var11.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     String sib = var11[var13];
                     if (!ThaumcraftCapabilities.knowsResearch(player, sib)) {
                        ResearchManager.completeResearch(player, sib);
                     }
                  }
               }
            }
         }

         ThaumcraftCapabilities.getKnowledge(player).sync((EntityPlayerMP)player);
      } else {
         world.func_184134_a(player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundsTC.page, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
      }

      player.openGui(Thaumcraft.instance, 12, world, 0, 0, 0);
      return new ActionResult(EnumActionResult.SUCCESS, stack);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return itemstack.func_77952_i() != 1 ? EnumRarity.UNCOMMON : EnumRarity.EPIC;
   }
}
