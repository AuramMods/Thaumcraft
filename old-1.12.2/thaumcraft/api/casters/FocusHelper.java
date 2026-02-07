package thaumcraft.api.casters;

import java.util.HashMap;

public class FocusHelper {
   public static IFocusPartMedium TOUCH;
   public static IFocusPartMedium BOLT;
   public static IFocusPartMedium PROJECTILE;
   public static IFocusPartEffect FIRE;
   public static IFocusPartEffect FROST;
   public static IFocusPartEffect MAGIC;
   public static IFocusPartEffect CURSE;
   public static IFocusPartEffect BREAK;
   public static IFocusPartEffect RIFT;
   public static IFocusPartEffect EXCHANGE;
   public static IFocusPart FRUGAL;
   public static IFocusPart POTENCY;
   public static IFocusPart LINGERING;
   public static IFocusPart SCATTER;
   public static IFocusPart CHAIN;
   public static IFocusPart SILKTOUCH;
   public static IFocusPart FORTUNE;
   public static IFocusPart CHARGE;
   public static HashMap<String, IFocusPart> focusParts = new HashMap();
   public static HashMap<String, IFocusPart[]> focusPartsConnections = new HashMap();

   public static boolean registerFocusPart(IFocusPart part, IFocusPart... connections) {
      if (focusParts.containsKey(part.getKey())) {
         return false;
      } else {
         focusParts.put(part.getKey(), part);
         if (connections != null) {
            focusPartsConnections.put(part.getKey(), connections);
         }

         return true;
      }
   }

   public static IFocusPart getFocusPart(String key) {
      return (IFocusPart)focusParts.get(key);
   }

   public static boolean canPartsConnect(IFocusPart part1, IFocusPart part2) {
      if (part1 != null && part2 != null) {
         if (part1.getType() == part2.getType()) {
            return false;
         } else if (part1.canConnectTo(part2) && part2.canConnectTo(part1)) {
            if (part1.getType() == IFocusPart.EnumFocusPartType.MEDIUM && part2.getType() == IFocusPart.EnumFocusPartType.EFFECT || part2.getType() == IFocusPart.EnumFocusPartType.MEDIUM && part1.getType() == IFocusPart.EnumFocusPartType.EFFECT) {
               return true;
            } else {
               IFocusPart[] conns = (IFocusPart[])focusPartsConnections.get(part1.getKey());
               IFocusPart[] var3;
               int var4;
               int var5;
               IFocusPart pc;
               if (conns != null) {
                  var3 = conns;
                  var4 = conns.length;

                  for(var5 = 0; var5 < var4; ++var5) {
                     pc = var3[var5];
                     if (pc == part2) {
                        return true;
                     }
                  }
               }

               conns = (IFocusPart[])focusPartsConnections.get(part2.getKey());
               if (conns != null) {
                  var3 = conns;
                  var4 = conns.length;

                  for(var5 = 0; var5 < var4; ++var5) {
                     pc = var3[var5];
                     if (pc == part1) {
                        return true;
                     }
                  }
               }

               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
