package thaumcraft.api.aspects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.internal.CommonInternals;

public class AspectHelper {
   public static AspectList cullTags(AspectList temp) {
      AspectList temp2 = new AspectList();
      Aspect[] var2 = temp.getAspects();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Aspect tag = var2[var4];
         if (tag != null) {
            temp2.add(tag, temp.getAmount(tag));
         }
      }

      while(temp2 != null && temp2.size() > 7) {
         Aspect lowest = null;
         float low = 32767.0F;
         Aspect[] var11 = temp2.getAspects();
         int var12 = var11.length;

         for(int var6 = 0; var6 < var12; ++var6) {
            Aspect tag = var11[var6];
            if (tag != null) {
               float ta = (float)temp2.getAmount(tag);
               if (tag.isPrimal()) {
                  ta *= 0.9F;
               } else {
                  if (!tag.getComponents()[0].isPrimal()) {
                     ta *= 1.1F;
                     if (!tag.getComponents()[0].getComponents()[0].isPrimal()) {
                        ta *= 1.05F;
                     }

                     if (!tag.getComponents()[0].getComponents()[1].isPrimal()) {
                        ta *= 1.05F;
                     }
                  }

                  if (!tag.getComponents()[1].isPrimal()) {
                     ta *= 1.1F;
                     if (!tag.getComponents()[1].getComponents()[0].isPrimal()) {
                        ta *= 1.05F;
                     }

                     if (!tag.getComponents()[1].getComponents()[1].isPrimal()) {
                        ta *= 1.05F;
                     }
                  }
               }

               if (ta < low) {
                  low = ta;
                  lowest = tag;
               }
            }
         }

         temp2.aspects.remove(lowest);
      }

      return temp2;
   }

   public static AspectList getObjectAspects(ItemStack is) {
      return ThaumcraftApi.internalMethods.getObjectAspects(is);
   }

   public static AspectList generateTags(ItemStack is) {
      return ThaumcraftApi.internalMethods.generateTags(is);
   }

   public static AspectList getEntityAspects(Entity entity) {
      AspectList tags = null;
      if (entity instanceof EntityPlayer) {
         tags = new AspectList();
         tags.add(Aspect.MAN, 4);
         Random rand = new Random((long)((EntityPlayer)entity).func_70005_c_().hashCode());
         Aspect[] posa = (Aspect[])Aspect.aspects.values().toArray(new Aspect[0]);
         tags.add(posa[rand.nextInt(posa.length)], 15);
         tags.add(posa[rand.nextInt(posa.length)], 15);
         tags.add(posa[rand.nextInt(posa.length)], 15);
         return tags;
      } else {
         Iterator var9 = CommonInternals.scanEntities.iterator();

         while(true) {
            while(true) {
               label33:
               while(true) {
                  ThaumcraftApi.EntityTags et;
                  do {
                     if (!var9.hasNext()) {
                        return tags;
                     }

                     et = (ThaumcraftApi.EntityTags)var9.next();
                  } while(!et.entityName.equals(EntityList.func_75621_b(entity)));

                  if (et.nbts != null && et.nbts.length != 0) {
                     NBTTagCompound tc = new NBTTagCompound();
                     entity.func_189511_e(tc);
                     ThaumcraftApi.EntityTagsNBT[] var5 = et.nbts;
                     int var6 = var5.length;

                     for(int var7 = 0; var7 < var6; ++var7) {
                        ThaumcraftApi.EntityTagsNBT nbt = var5[var7];
                        if (!tc.func_74764_b(nbt.name) || !ThaumcraftApiHelper.getNBTDataFromId(tc, tc.func_150299_b(nbt.name), nbt.name).equals(nbt.value)) {
                           continue label33;
                        }
                     }

                     tags = et.aspects;
                  } else {
                     tags = et.aspects;
                  }
               }
            }
         }
      }
   }

   public static Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
      Collection<Aspect> aspects = Aspect.aspects.values();
      Iterator var3 = aspects.iterator();

      Aspect aspect;
      do {
         do {
            if (!var3.hasNext()) {
               return null;
            }

            aspect = (Aspect)var3.next();
         } while(aspect.getComponents() == null);
      } while((aspect.getComponents()[0] != aspect1 || aspect.getComponents()[1] != aspect2) && (aspect.getComponents()[0] != aspect2 || aspect.getComponents()[1] != aspect1));

      return aspect;
   }

   public static Aspect getRandomPrimal(Random rand, Aspect aspect) {
      ArrayList<Aspect> list = new ArrayList();
      if (aspect != null) {
         AspectList temp = new AspectList();
         temp.add(aspect, 1);
         AspectList temp2 = reduceToPrimals(temp);
         Aspect[] var5 = temp2.getAspects();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Aspect a = var5[var7];

            for(int b = 0; b < temp2.getAmount(a); ++b) {
               list.add(a);
            }
         }
      }

      return list.size() > 0 ? (Aspect)list.get(rand.nextInt(list.size())) : null;
   }

   public static AspectList reduceToPrimals(AspectList in) {
      AspectList out = new AspectList();
      Aspect[] var2 = in.getAspects();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Aspect aspect = var2[var4];
         if (aspect != null) {
            if (aspect.isPrimal()) {
               out.add(aspect, in.getAmount(aspect));
            } else {
               AspectList temp = new AspectList();
               temp.add(aspect.getComponents()[0], in.getAmount(aspect));
               temp.add(aspect.getComponents()[1], in.getAmount(aspect));
               AspectList temp2 = reduceToPrimals(temp);
               Aspect[] var8 = temp2.getAspects();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  Aspect a = var8[var10];
                  out.add(a, temp2.getAmount(a));
               }
            }
         }
      }

      return out;
   }

   public static AspectList getPrimalAspects(AspectList in) {
      AspectList t = new AspectList();
      t.add(Aspect.AIR, in.getAmount(Aspect.AIR));
      t.add(Aspect.FIRE, in.getAmount(Aspect.FIRE));
      t.add(Aspect.WATER, in.getAmount(Aspect.WATER));
      t.add(Aspect.EARTH, in.getAmount(Aspect.EARTH));
      t.add(Aspect.ORDER, in.getAmount(Aspect.ORDER));
      t.add(Aspect.ENTROPY, in.getAmount(Aspect.ENTROPY));
      return t;
   }

   public static AspectList getAuraAspects(AspectList in) {
      AspectList t = new AspectList();
      t.add(Aspect.AIR, in.getAmount(Aspect.AIR));
      t.add(Aspect.FIRE, in.getAmount(Aspect.FIRE));
      t.add(Aspect.WATER, in.getAmount(Aspect.WATER));
      t.add(Aspect.EARTH, in.getAmount(Aspect.EARTH));
      t.add(Aspect.ORDER, in.getAmount(Aspect.ORDER));
      t.add(Aspect.ENTROPY, in.getAmount(Aspect.ENTROPY));
      t.add(Aspect.FLUX, in.getAmount(Aspect.FLUX));
      return t;
   }
}
