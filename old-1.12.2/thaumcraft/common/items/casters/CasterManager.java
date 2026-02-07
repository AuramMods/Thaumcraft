package thaumcraft.common.items.casters;

import baubles.api.BaublesApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;

public class CasterManager {
   static HashMap<Integer, Long> cooldownServer = new HashMap();
   static HashMap<Integer, Long> cooldownClient = new HashMap();

   public static int getBaseChargeRate(EntityPlayer player, boolean currentItem, int slot) {
      return ThaumcraftCapabilities.getKnowledge(player).isResearchKnown("NODETAPPER2") && slot < 9 ? 3 : (ThaumcraftCapabilities.getKnowledge(player).isResearchKnown("NODETAPPER1") && slot < 9 ? 2 : (currentItem ? 1 : 0));
   }

   public static float getTotalVisDiscount(EntityPlayer player) {
      int total = 0;
      if (player == null) {
         return 0.0F;
      } else {
         IInventory baubles = BaublesApi.getBaubles(player);

         int level1;
         for(level1 = 0; level1 < 4; ++level1) {
            if (baubles.func_70301_a(level1) != null && baubles.func_70301_a(level1).func_77973_b() instanceof IVisDiscountGear) {
               total += ((IVisDiscountGear)baubles.func_70301_a(level1).func_77973_b()).getVisDiscount(baubles.func_70301_a(level1), player);
            }
         }

         for(level1 = 0; level1 < 4; ++level1) {
            if (player.field_71071_by.field_70460_b[level1] != null && player.field_71071_by.field_70460_b[level1].func_77973_b() instanceof IVisDiscountGear) {
               total += ((IVisDiscountGear)player.field_71071_by.field_70460_b[level1].func_77973_b()).getVisDiscount(player.field_71071_by.field_70460_b[level1], player);
            }
         }

         if (player.func_70644_a(PotionVisExhaust.instance) || player.func_70644_a(PotionInfectiousVisExhaust.instance)) {
            level1 = 0;
            int level2 = 0;
            if (player.func_70644_a(PotionVisExhaust.instance)) {
               level1 = player.func_70660_b(PotionVisExhaust.instance).func_76458_c();
            }

            if (player.func_70644_a(PotionInfectiousVisExhaust.instance)) {
               level2 = player.func_70660_b(PotionInfectiousVisExhaust.instance).func_76458_c();
            }

            total -= (Math.max(level1, level2) + 1) * 10;
         }

         return (float)total / 100.0F;
      }
   }

   public static boolean consumeVisFromInventory(EntityPlayer player, float cost) {
      for(int a = player.field_71071_by.field_70462_a.length - 1; a >= 0; --a) {
         ItemStack item = player.field_71071_by.field_70462_a[a];
         if (item != null && item.func_77973_b() instanceof ICaster) {
            boolean done = ((ICaster)item.func_77973_b()).consumeVis(item, player, cost, true);
            if (done) {
               return true;
            }
         }
      }

      return false;
   }

   public static void changeFocus(ItemStack is, World w, EntityPlayer player, String focus) {
      ICaster wand = (ICaster)is.func_77973_b();
      TreeMap<String, Integer> foci = new TreeMap();
      HashMap<Integer, Integer> pouches = new HashMap();
      int pouchcount = 0;
      ItemStack item = null;
      IInventory baubles = BaublesApi.getBaubles(player);

      int a;
      ItemStack[] inv;
      int pouchslot;
      for(a = 0; a < baubles.func_70302_i_(); ++a) {
         if (baubles.func_70301_a(a) != null && baubles.func_70301_a(a).func_77973_b() instanceof ItemFocusPouch) {
            ++pouchcount;
            item = baubles.func_70301_a(a);
            pouches.put(pouchcount, a - 4);
            inv = ((ItemFocusPouch)item.func_77973_b()).getInventory(item);

            for(pouchslot = 0; pouchslot < inv.length; ++pouchslot) {
               item = inv[pouchslot];
               if (item != null && item.func_77973_b() instanceof ItemFocus) {
                  foci.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), pouchslot + pouchcount * 1000);
               }
            }
         }
      }

      for(a = 0; a < 36; ++a) {
         item = player.field_71071_by.field_70462_a[a];
         if (item != null && item.func_77973_b() instanceof ItemFocus) {
            foci.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), a);
         }

         if (item != null && item.func_77973_b() instanceof ItemFocusPouch) {
            ++pouchcount;
            pouches.put(pouchcount, a);
            inv = ((ItemFocusPouch)item.func_77973_b()).getInventory(item);

            for(pouchslot = 0; pouchslot < inv.length; ++pouchslot) {
               item = inv[pouchslot];
               if (item != null && item.func_77973_b() instanceof ItemFocus) {
                  foci.put(((ItemFocus)item.func_77973_b()).getSortingHelper(item), pouchslot + pouchcount * 1000);
               }
            }
         }
      }

      if (!focus.equals("REMOVE") && foci.size() != 0) {
         if (foci != null && foci.size() > 0 && focus != null) {
            String newkey = focus;
            if (foci.get(focus) == null) {
               newkey = (String)foci.higherKey(focus);
            }

            if (newkey == null || foci.get(newkey) == null) {
               newkey = (String)foci.firstKey();
            }

            if ((Integer)foci.get(newkey) < 1000 && (Integer)foci.get(newkey) >= 0) {
               item = player.field_71071_by.field_70462_a[(Integer)foci.get(newkey)].func_77946_l();
            } else {
               int pid = (Integer)foci.get(newkey) / 1000;
               if (pouches.containsKey(pid)) {
                  pouchslot = (Integer)pouches.get(pid);
                  int focusslot = (Integer)foci.get(newkey) - pid * 1000;
                  ItemStack tmp = null;
                  if (pouchslot >= 0) {
                     tmp = player.field_71071_by.field_70462_a[pouchslot].func_77946_l();
                  } else {
                     tmp = baubles.func_70301_a(pouchslot + 4).func_77946_l();
                  }

                  item = fetchFocusFromPouch(player, focusslot, tmp, pouchslot);
               }
            }

            if (item == null) {
               return;
            }

            if ((Integer)foci.get(newkey) < 1000 && (Integer)foci.get(newkey) >= 0) {
               player.field_71071_by.func_70299_a((Integer)foci.get(newkey), (ItemStack)null);
            }

            player.func_184185_a(SoundsTC.ticks, 0.3F, 1.0F);
            if (wand.getFocus(is) != null && (addFocusToPouch(player, wand.getFocusStack(is).func_77946_l(), pouches) || player.field_71071_by.func_70441_a(wand.getFocusStack(is).func_77946_l()))) {
               wand.setFocus(is, (ItemStack)null);
            }

            if (wand.getFocus(is) == null) {
               wand.setFocus(is, item);
            } else if (!addFocusToPouch(player, item, pouches)) {
               player.field_71071_by.func_70441_a(item);
            }
         }

      } else {
         if (wand.getFocus(is) != null && (addFocusToPouch(player, wand.getFocusStack(is).func_77946_l(), pouches) || player.field_71071_by.func_70441_a(wand.getFocusStack(is).func_77946_l()))) {
            wand.setFocus(is, (ItemStack)null);
            player.func_184185_a(SoundsTC.ticks, 0.3F, 0.9F);
         }

      }
   }

   private static ItemStack fetchFocusFromPouch(EntityPlayer player, int focusid, ItemStack pouch, int pouchslot) {
      ItemStack focus = null;
      ItemStack[] inv = ((ItemFocusPouch)pouch.func_77973_b()).getInventory(pouch);
      ItemStack contents = inv[focusid];
      if (contents != null && contents.func_77973_b() instanceof ItemFocus) {
         focus = contents.func_77946_l();
         inv[focusid] = null;
         ((ItemFocusPouch)pouch.func_77973_b()).setInventory(pouch, inv);
         if (pouchslot >= 0) {
            player.field_71071_by.func_70299_a(pouchslot, pouch);
            player.field_71071_by.func_70296_d();
         } else {
            IInventory baubles = BaublesApi.getBaubles(player);
            baubles.func_70299_a(pouchslot + 4, pouch);
            baubles.func_70296_d();
         }
      }

      return focus;
   }

   private static boolean addFocusToPouch(EntityPlayer player, ItemStack focus, HashMap<Integer, Integer> pouches) {
      IInventory baubles = BaublesApi.getBaubles(player);
      Iterator var5 = pouches.values().iterator();

      while(var5.hasNext()) {
         Integer pouchslot = (Integer)var5.next();
         ItemStack pouch;
         if (pouchslot >= 0) {
            pouch = player.field_71071_by.field_70462_a[pouchslot];
         } else {
            pouch = baubles.func_70301_a(pouchslot + 4);
         }

         ItemStack[] inv = ((ItemFocusPouch)pouch.func_77973_b()).getInventory(pouch);

         for(int q = 0; q < inv.length; ++q) {
            ItemStack contents = inv[q];
            if (contents == null) {
               inv[q] = focus.func_77946_l();
               ((ItemFocusPouch)pouch.func_77973_b()).setInventory(pouch, inv);
               if (pouchslot >= 0) {
                  player.field_71071_by.func_70299_a(pouchslot, pouch);
                  player.field_71071_by.func_70296_d();
               } else {
                  baubles.func_70299_a(pouchslot + 4, pouch);
                  baubles.func_70296_d();
               }

               return true;
            }
         }
      }

      return false;
   }

   public static void toggleMisc(ItemStack itemstack, World world, EntityPlayer player) {
   }

   private static int getAreaSize(ItemStack stack) {
      return 3;
   }

   public static int getAreaDim(ItemStack stack) {
      return stack.func_77942_o() && stack.func_77978_p().func_74764_b("aread") ? stack.func_77978_p().func_74762_e("aread") : 0;
   }

   public static int getAreaX(ItemStack stack) {
      ICaster wand = (ICaster)stack.func_77973_b();
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("areax")) {
         int a = stack.func_77978_p().func_74762_e("areax");
         if (a > getAreaSize(wand.getFocusStack(stack))) {
            a = getAreaSize(wand.getFocusStack(stack));
         }

         return a;
      } else {
         return getAreaSize(wand.getFocusStack(stack));
      }
   }

   public static int getAreaY(ItemStack stack) {
      ICaster wand = (ICaster)stack.func_77973_b();
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("areay")) {
         int a = stack.func_77978_p().func_74762_e("areay");
         if (a > getAreaSize(wand.getFocusStack(stack))) {
            a = getAreaSize(wand.getFocusStack(stack));
         }

         return a;
      } else {
         return getAreaSize(wand.getFocusStack(stack));
      }
   }

   public static int getAreaZ(ItemStack stack) {
      ICaster wand = (ICaster)stack.func_77973_b();
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("areaz")) {
         int a = stack.func_77978_p().func_74762_e("areaz");
         if (a > getAreaSize(wand.getFocusStack(stack))) {
            a = getAreaSize(wand.getFocusStack(stack));
         }

         return a;
      } else {
         return getAreaSize(wand.getFocusStack(stack));
      }
   }

   public static void setAreaX(ItemStack stack, int area) {
      if (stack.func_77942_o()) {
         stack.func_77978_p().func_74768_a("areax", area);
      }

   }

   public static void setAreaY(ItemStack stack, int area) {
      if (stack.func_77942_o()) {
         stack.func_77978_p().func_74768_a("areay", area);
      }

   }

   public static void setAreaZ(ItemStack stack, int area) {
      if (stack.func_77942_o()) {
         stack.func_77978_p().func_74768_a("areaz", area);
      }

   }

   public static void setAreaDim(ItemStack stack, int dim) {
      if (stack.func_77942_o()) {
         stack.func_77978_p().func_74768_a("aread", dim);
      }

   }

   public static void sendImpact(World world, double x, double y, double z, FocusCore core) {
      ArrayList<String> pParts = new ArrayList();
      Iterator var9 = core.partsRaw.keySet().iterator();

      while(var9.hasNext()) {
         String k = (String)var9.next();
         IFocusPart p = (IFocusPart)core.partsRaw.get(k);
         if (p != null && p.hasCustomParticle()) {
            pParts.add(k);
         }
      }

      if (!pParts.isEmpty()) {
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(x, y, z, (String[])pParts.toArray(new String[0])), new TargetPoint(world.field_73011_w.getDimension(), x, y, z, 32.0D));
      }

   }

   static boolean isOnCooldown(EntityLivingBase entityLiving) {
      if (entityLiving.field_70170_p.field_72995_K && cooldownClient.containsKey(entityLiving.func_145782_y())) {
         return (Long)cooldownClient.get(entityLiving.func_145782_y()) > System.currentTimeMillis();
      } else if (!entityLiving.field_70170_p.field_72995_K && cooldownServer.containsKey(entityLiving.func_145782_y())) {
         return (Long)cooldownServer.get(entityLiving.func_145782_y()) > System.currentTimeMillis();
      } else {
         return false;
      }
   }

   public static float getCooldown(EntityLivingBase entityLiving) {
      return entityLiving.field_70170_p.field_72995_K && cooldownClient.containsKey(entityLiving.func_145782_y()) ? (float)((Long)cooldownClient.get(entityLiving.func_145782_y()) - System.currentTimeMillis()) / 1000.0F : 0.0F;
   }

   public static void setCooldown(EntityLivingBase entityLiving, int cd) {
      if (cd == 0) {
         cooldownClient.remove(entityLiving.func_145782_y());
         cooldownServer.remove(entityLiving.func_145782_y());
      } else if (entityLiving.field_70170_p.field_72995_K) {
         cooldownClient.put(entityLiving.func_145782_y(), System.currentTimeMillis() + (long)(cd * 50));
      } else {
         cooldownServer.put(entityLiving.func_145782_y(), System.currentTimeMillis() + (long)(cd * 50));
      }

   }
}
