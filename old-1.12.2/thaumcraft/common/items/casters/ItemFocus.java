package thaumcraft.common.items.casters;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.common.items.ItemTCBase;

public class ItemFocus extends ItemTCBase {
   public ItemFocus() {
      super("focus");
      this.field_77777_bU = 1;
      this.func_77656_e(0);
      this.func_77637_a((CreativeTabs)null);
   }

   public int[] getFocusColors(ItemStack focusstack) {
      int[] colors = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
      FocusCore core = getCore(focusstack);
      int r = 0;
      int g = 0;
      int b = 0;
      FocusCore.FocusEffect[] var7 = core.effects;
      int i = var7.length;

      for(int var9 = 0; var9 < i; ++var9) {
         FocusCore.FocusEffect ef = var7[var9];
         Color c = new Color(ef.effect.getGemColor());
         r += c.getRed();
         g += c.getGreen();
         b += c.getBlue();
      }

      r /= core.effects.length;
      g /= core.effects.length;
      b /= core.effects.length;
      Color c = new Color(r, g, b);
      colors[0] = c.getRGB();
      i = 1;

      for(Iterator var13 = core.partsRaw.values().iterator(); var13.hasNext(); ++i) {
         IFocusPart p = (IFocusPart)var13.next();
         colors[i] = p.getIconColor();
      }

      return colors;
   }

   public String getSortingHelper(ItemStack focusstack) {
      return getCore(focusstack).getSortingHelper();
   }

   public static void setCore(ItemStack focusstack, FocusCore core) {
      NBTTagCompound tag = core.serialize();
      focusstack.func_77983_a("core", tag);
   }

   public static FocusCore getCore(ItemStack focusstack) {
      NBTTagCompound tag = focusstack.func_179543_a("core", true);
      FocusCore core = new FocusCore();
      core.deserialize(tag);
      return core;
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      float al = this.getVisCost(stack);
      DecimalFormat myFormatter = new DecimalFormat("#####.##");
      String amount = myFormatter.format((double)al);
      list.add(amount + " " + I18n.func_74838_a("item.Focus.cost1"));
      this.addFocusInformation(stack, player, list, par4);
   }

   public void addFocusInformation(ItemStack focusstack, EntityPlayer player, List list, boolean par4) {
      FocusCore core = getCore(focusstack);
      String t = TextFormatting.GOLD + core.medium.getName();
      int var8;
      int var9;
      if (Thaumcraft.proxy.isShiftKeyDown() && core.mediumModifiers != null && core.mediumModifiers.length > 0) {
         t = t + TextFormatting.DARK_GRAY + " [";
         IFocusPart[] var7 = core.mediumModifiers;
         var8 = var7.length;

         for(var9 = 0; var9 < var8; ++var9) {
            IFocusPart p = var7[var9];
            if (p.getType() != IFocusPart.EnumFocusPartType.EFFECT) {
               t = t + p.getName() + " ";
            }
         }

         t = t + "]";
      }

      list.add(t);
      FocusCore.FocusEffect[] var16 = core.effects;
      var8 = var16.length;

      for(var9 = 0; var9 < var8; ++var9) {
         FocusCore.FocusEffect fe = var16[var9];
         String t0 = TextFormatting.DARK_PURPLE + fe.effect.getName();
         if (Thaumcraft.proxy.isShiftKeyDown() && fe.modifiers != null && fe.modifiers.length > 0) {
            t0 = t0 + TextFormatting.DARK_GRAY + " [";
            IFocusPart[] var12 = fe.modifiers;
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               IFocusPart p = var12[var14];
               if (p.getType() != IFocusPart.EnumFocusPartType.MEDIUM) {
                  t0 = t0 + p.getName() + " ";
               }
            }

            t0 = t0 + "]";
         }

         list.add(t0);
      }

   }

   public EnumRarity func_77613_e(ItemStack focusstack) {
      return EnumRarity.RARE;
   }

   public float getVisCost(ItemStack focusstack) {
      return getCore(focusstack).cost;
   }

   public int getActivationTime(ItemStack focusstack) {
      return getCore(focusstack).getFinalChargeTime();
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      return super.func_77659_a(itemStackIn, worldIn, playerIn, hand);
   }
}
