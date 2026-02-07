package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class FocusCore {
   public IFocusPartMedium medium;
   public IFocusPart[] mediumModifiers;
   public FocusCore.FocusEffect[] effects;
   public float cost;
   public LinkedHashMap<String, IFocusPart> partsRaw;

   public FocusCore(IFocusPartMedium medium, IFocusPart[] mediumModifiers, FocusCore.FocusEffect[] effects) {
      this.medium = medium;
      this.mediumModifiers = mediumModifiers;
      this.effects = effects;
   }

   public FocusCore() {
      this.medium = FocusHelper.TOUCH;
      FocusCore.FocusEffect fe = new FocusCore.FocusEffect();
      fe.effect = FocusHelper.FIRE;
      fe.costMultipler = FocusHelper.TOUCH.getCostMultiplier();
      fe.effectMultipler = FocusHelper.TOUCH.getEffectMultiplier();
      this.effects = new FocusCore.FocusEffect[]{fe};
      this.generate();
   }

   public IFocusPartMedium.EnumFocusCastMethod getFinalCastMethod() {
      if (this.mediumModifiers != null) {
         IFocusPart[] var1 = this.mediumModifiers;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            IFocusPart part = var1[var3];
            if (part == FocusHelper.CHARGE) {
               return IFocusPartMedium.EnumFocusCastMethod.CHARGE;
            }
         }
      }

      return this.medium.getCastMethod();
   }

   public int getFinalChargeTime() {
      if (this.mediumModifiers != null) {
         IFocusPart[] var1 = this.mediumModifiers;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            IFocusPart part = var1[var3];
            if (part == FocusHelper.CHARGE) {
               return this.medium.getChargeTime() * 10;
            }
         }
      }

      return this.medium.getChargeTime();
   }

   public void generate() {
      this.partsRaw = new LinkedHashMap();
      if (this.medium != null) {
         this.partsRaw.put(this.medium.getKey(), this.medium);
         this.cost = 0.0F;
         int var2;
         int var3;
         if (this.mediumModifiers != null) {
            IFocusPart[] var1 = this.mediumModifiers;
            var2 = var1.length;

            for(var3 = 0; var3 < var2; ++var3) {
               IFocusPart p = var1[var3];
               this.partsRaw.put(p.getKey(), p);
            }
         }

         FocusCore.FocusEffect[] var10 = this.effects;
         var2 = var10.length;

         for(var3 = 0; var3 < var2; ++var3) {
            FocusCore.FocusEffect fe = var10[var3];
            this.partsRaw.put(fe.effect.getKey(), fe.effect);
            float cost2 = fe.effect.getBaseCost();
            cost2 *= fe.costMultipler;
            if (fe.modifiers != null) {
               IFocusPart[] var6 = fe.modifiers;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  IFocusPart p = var6[var8];
                  this.partsRaw.put(p.getKey(), p);
               }
            }

            this.cost += cost2;
         }

      }
   }

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74778_a("medium", this.medium.getKey());
      int var4;
      int var5;
      if (this.mediumModifiers != null && this.mediumModifiers.length > 0) {
         String s = "";
         IFocusPart[] var3 = this.mediumModifiers;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            IFocusPart p = var3[var5];
            s = s + "~" + p.getKey();
         }

         s = s.replaceFirst("~", "");
         nbt.func_74778_a("mediumMods", s);
      }

      NBTTagList efflist = new NBTTagList();
      FocusCore.FocusEffect[] var14 = this.effects;
      var4 = var14.length;

      for(var5 = 0; var5 < var4; ++var5) {
         FocusCore.FocusEffect fe = var14[var5];
         NBTTagCompound gt = new NBTTagCompound();
         gt.func_74778_a("effect", fe.effect.getKey());
         gt.func_74776_a("costMod", fe.costMultipler);
         gt.func_74776_a("effMod", fe.effectMultipler);
         if (fe.modifiers != null && fe.modifiers.length > 0) {
            String s = "";
            IFocusPart[] var9 = fe.modifiers;
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               IFocusPart p = var9[var11];
               s = s + "~" + p.getKey();
            }

            s = s.replaceFirst("~", "");
            gt.func_74778_a("mods", s);
         }

         efflist.func_74742_a(gt);
      }

      nbt.func_74782_a("effects", efflist);
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      IFocusPart mp = FocusHelper.getFocusPart(nbt.func_74779_i("medium"));
      if (mp != null) {
         this.medium = (IFocusPartMedium)mp;
         String s = nbt.func_74779_i("mediumMods");
         String[] ss = s.split("~");
         if (ss.length > 0) {
            ArrayList<IFocusPart> li = new ArrayList();

            for(int a = 0; a < ss.length; ++a) {
               IFocusPart p = FocusHelper.getFocusPart(ss[a]);
               if (p != null) {
                  li.add(p);
               }
            }

            this.mediumModifiers = (IFocusPart[])li.toArray(new IFocusPart[li.size()]);
         }

         NBTTagList efflist = nbt.func_150295_c("effects", 10);
         ArrayList<FocusCore.FocusEffect> fes = new ArrayList();

         for(int x = 0; x < efflist.func_74745_c(); ++x) {
            NBTTagCompound nbtdata = efflist.func_150305_b(x);
            FocusCore.FocusEffect fe = new FocusCore.FocusEffect();
            fe.effect = (IFocusPartEffect)FocusHelper.getFocusPart(nbtdata.func_74779_i("effect"));
            fe.costMultipler = nbtdata.func_74760_g("costMod");
            fe.effectMultipler = nbtdata.func_74760_g("effMod");
            String mods = nbtdata.func_74779_i("mods");
            if (!mods.isEmpty()) {
               String[] modlist = mods.split("~");
               ArrayList<IFocusPart> li = new ArrayList();

               for(int a = 0; a < modlist.length; ++a) {
                  IFocusPart p = FocusHelper.getFocusPart(modlist[a]);
                  if (p != null) {
                     li.add(p);
                  }
               }

               fe.modifiers = (IFocusPart[])li.toArray(new IFocusPart[li.size()]);
            }

            fes.add(fe);
         }

         this.effects = (FocusCore.FocusEffect[])fes.toArray(new FocusCore.FocusEffect[fes.size()]);
         this.generate();
      }
   }

   public String getSortingHelper() {
      String s = this.medium.getKey();
      FocusCore.FocusEffect[] var2 = this.effects;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         FocusCore.FocusEffect ef = var2[var4];
         s = s + ef.effect.getKey();
      }

      return s;
   }

   public static class FocusEffect {
      public IFocusPartEffect effect;
      public IFocusPart[] modifiers;
      public float effectMultipler;
      public float costMultipler;
   }
}
