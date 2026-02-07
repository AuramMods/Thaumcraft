package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CasterTriggerRegistry {
   private static HashMap<String, LinkedHashMap<IBlockState, List<CasterTriggerRegistry.Trigger>>> triggers = new HashMap();
   private static final String DEFAULT = "default";

   public static void registerWandBlockTrigger(ICasterTriggerManager manager, int event, IBlockState state, String modid) {
      if (!triggers.containsKey(modid)) {
         triggers.put(modid, new LinkedHashMap());
      }

      LinkedHashMap<IBlockState, List<CasterTriggerRegistry.Trigger>> temp = (LinkedHashMap)triggers.get(modid);
      List<CasterTriggerRegistry.Trigger> ts = (List)temp.get(state);
      if (ts == null) {
         ts = new ArrayList();
      }

      ((List)ts).add(new CasterTriggerRegistry.Trigger(manager, event));
      temp.put(state, ts);
      triggers.put(modid, temp);
   }

   public static void registerCasterBlockTrigger(ICasterTriggerManager manager, int event, IBlockState state) {
      registerWandBlockTrigger(manager, event, state, "default");
   }

   public static boolean hasTrigger(IBlockState state) {
      Iterator var1 = triggers.keySet().iterator();

      LinkedHashMap temp;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         String modid = (String)var1.next();
         temp = (LinkedHashMap)triggers.get(modid);
      } while(!temp.containsKey(state));

      return true;
   }

   public static boolean hasTrigger(IBlockState state, String modid) {
      if (!triggers.containsKey(modid)) {
         return false;
      } else {
         LinkedHashMap<IBlockState, List<CasterTriggerRegistry.Trigger>> temp = (LinkedHashMap)triggers.get(modid);
         return temp.containsKey(state);
      }
   }

   public static boolean performTrigger(World world, ItemStack casterStack, EntityPlayer player, BlockPos pos, EnumFacing side, IBlockState state) {
      Iterator var6 = triggers.keySet().iterator();

      while(true) {
         List l;
         do {
            do {
               if (!var6.hasNext()) {
                  return false;
               }

               String modid = (String)var6.next();
               LinkedHashMap<IBlockState, List<CasterTriggerRegistry.Trigger>> temp = (LinkedHashMap)triggers.get(modid);
               l = (List)temp.get(state);
            } while(l == null);
         } while(l.size() == 0);

         Iterator var10 = l.iterator();

         while(var10.hasNext()) {
            CasterTriggerRegistry.Trigger trig = (CasterTriggerRegistry.Trigger)var10.next();
            boolean result = trig.manager.performTrigger(world, casterStack, player, pos, side, trig.event);
            if (result) {
               return true;
            }
         }
      }
   }

   public static boolean performTrigger(World world, ItemStack casterStack, EntityPlayer player, BlockPos pos, EnumFacing side, IBlockState state, String modid) {
      if (!triggers.containsKey(modid)) {
         return false;
      } else {
         LinkedHashMap<IBlockState, List<CasterTriggerRegistry.Trigger>> temp = (LinkedHashMap)triggers.get(modid);
         List<CasterTriggerRegistry.Trigger> l = (List)temp.get(state);
         if (l != null && l.size() != 0) {
            Iterator var9 = l.iterator();

            boolean result;
            do {
               if (!var9.hasNext()) {
                  return false;
               }

               CasterTriggerRegistry.Trigger trig = (CasterTriggerRegistry.Trigger)var9.next();
               result = trig.manager.performTrigger(world, casterStack, player, pos, side, trig.event);
            } while(!result);

            return true;
         } else {
            return false;
         }
      }
   }

   private static class Trigger {
      ICasterTriggerManager manager;
      int event;

      public Trigger(ICasterTriggerManager manager, int event) {
         this.manager = manager;
         this.event = event;
      }
   }
}
