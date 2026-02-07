package thaumcraft.common.tiles.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.api.casters.IFocusPartMedium;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.HexUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileThaumcraftInventory;
import thaumcraft.common.world.aura.AuraHandler;

public class TileFocalManipulator extends TileThaumcraftInventory implements ITickable {
   public float vis = 0.0F;
   public FocalData data = null;
   int ticks = 0;
   public float visCost = 0.0F;
   public int xpCost = 0;
   public float castCost = 0.0F;
   private AspectList crystals = new AspectList();
   public AspectList crystalsSync = new AspectList();
   public static final int XP_MULT = 6;
   public static final int VIS_MULT = 100;
   public boolean doGuiReset = false;

   public TileFocalManipulator() {
      this.syncedSlots = new int[]{0};
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      super.readSyncNBT(nbttagcompound);
      this.vis = nbttagcompound.func_74760_g("vis");
      this.crystalsSync = new AspectList();
      this.crystalsSync.readFromNBT(nbttagcompound, "crystals");
      this.deserializeData(nbttagcompound);
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      super.writeSyncNBT(nbttagcompound);
      nbttagcompound.func_74776_a("vis", this.vis);
      if (this.data != null) {
         this.serializeData(nbttagcompound, this.data);
      }

      this.crystalsSync.writeToNBT(nbttagcompound, "crystals");
      return nbttagcompound;
   }

   public void serializeData(NBTTagCompound nbt, FocalData data) {
      NBTTagList gridtag = new NBTTagList();
      Iterator var4 = data.hexes.keySet().iterator();

      while(var4.hasNext()) {
         String hex = (String)var4.next();
         String key = (String)data.hexes.get(hex);
         if (hex != null && !hex.isEmpty() && key != null && !key.isEmpty()) {
            NBTTagCompound gt = new NBTTagCompound();
            gt.func_74778_a("hex", hex);
            gt.func_74778_a("key", key);
            gridtag.func_74742_a(gt);
         }
      }

      nbt.func_74782_a("hexgrid", gridtag);
   }

   public void deserializeData(NBTTagCompound nbt) {
      this.generateHexes(false);
      if (nbt != null) {
         NBTTagList grid = nbt.func_150295_c("hexgrid", 10);

         for(int x = 0; x < grid.func_74745_c(); ++x) {
            NBTTagCompound gt = grid.func_150305_b(x);
            this.data.hexes.put(gt.func_74779_i("hex"), gt.func_74779_i("key"));
         }

      }
   }

   public void placePart(int q, int r, String part, EntityPlayer player) {
      if (this.data != null) {
         HexUtils.Hex h = new HexUtils.Hex(q, r);
         this.data.hexes.put(h.toString(), part);
         this.func_70296_d();
      }
   }

   private void generateHexes(boolean sync) {
      this.data = new FocalData();
      HashMap<String, HexUtils.Hex> hexLocs = HexUtils.generateHexes(1);
      HashMap<String, String> hexes = new HashMap();
      Iterator var4 = hexLocs.values().iterator();

      while(var4.hasNext()) {
         HexUtils.Hex hex = (HexUtils.Hex)var4.next();
         hexes.put(hex.toString(), "");
      }

      this.data.hexes = hexes;
      if (sync) {
         this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 1, 0);
      }

   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + 1), (double)(this.field_174879_c.func_177952_p() + 1));
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      super.func_70299_a(par1, par2ItemStack);
      if (this.field_145850_b.field_72995_K) {
         this.doGuiReset = true;
      } else {
         this.vis = 0.0F;
      }

   }

   public void func_73660_a() {
      boolean complete = false;
      if (!this.field_145850_b.field_72995_K) {
         if (this.data == null) {
            this.generateHexes(false);
            this.syncTile(false);
         }

         ++this.ticks;
         if (this.ticks % 20 == 0) {
            if (this.vis > 0.0F && (this.func_70301_a(0) == null || this.func_70301_a(0).func_77973_b() != ItemsTC.focusBlank)) {
               complete = true;
               this.vis = 0.0F;
               this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.wandfail, SoundCategory.BLOCKS, 0.33F, 1.0F);
            }

            if (!complete && this.vis > 0.0F) {
               float amt = AuraHandler.drainVis(this.func_145831_w(), this.func_174877_v(), Math.min(5.0F, this.vis), false);
               if (amt > 0.0F) {
                  this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 5, 1);
                  this.vis -= amt;
                  this.syncTile(false);
                  this.func_70296_d();
               }

               if (this.vis <= 0.0F && this.func_70301_a(0) != null && this.func_70301_a(0).func_77973_b() == ItemsTC.focusBlank) {
                  complete = true;
                  this.endCraft();
               }
            }
         }
      } else if (this.vis > 0.0F) {
         FXDispatcher.INSTANCE.drawGenericParticles((double)this.field_174879_c.func_177958_n() + 0.5D + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.3F), (double)this.field_174879_c.func_177956_o() + 1.4D + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.3F), (double)this.field_174879_c.func_177952_p() + 0.5D + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.3F), 0.0D, 0.0D, 0.0D, 0.5F + this.field_145850_b.field_73012_v.nextFloat() * 0.4F, 1.0F - this.field_145850_b.field_73012_v.nextFloat() * 0.4F, 1.0F - this.field_145850_b.field_73012_v.nextFloat() * 0.4F, 0.8F, false, 448, 9, 1, 6 + this.field_145850_b.field_73012_v.nextInt(5), 0, 0.3F + this.field_145850_b.field_73012_v.nextFloat() * 0.3F, 0.0F, 0);
      }

      if (complete) {
         this.vis = 0.0F;
         this.syncTile(false);
         this.func_70296_d();
      }

   }

   private FocusCore genCore() {
      if (this.data == null) {
         return null;
      } else {
         FocusCore core = new FocusCore();
         ArrayList<FocusCore.FocusEffect> effects = new ArrayList();
         Iterator var3 = this.data.hexes.keySet().iterator();

         while(true) {
            HexUtils.Hex h;
            String mainKey;
            IFocusPart mainPart;
            do {
               if (!var3.hasNext()) {
                  core.effects = (FocusCore.FocusEffect[])effects.toArray(new FocusCore.FocusEffect[effects.size()]);
                  core.generate();
                  return core;
               }

               String hs = (String)var3.next();
               h = HexUtils.Hex.fromString(hs);
               mainKey = (String)this.data.hexes.get(hs);
               mainPart = FocusHelper.getFocusPart(mainKey);
            } while(mainPart == null);

            ArrayList<IFocusPart> mods = new ArrayList();

            for(int a = 0; a < 6; ++a) {
               HexUtils.Hex target = h.getNeighbour(a);
               String key = (String)this.data.hexes.get(target.toString());
               if (key != null && !key.isEmpty()) {
                  IFocusPart part = FocusHelper.getFocusPart(key);
                  if (FocusHelper.canPartsConnect(part, mainPart)) {
                     mods.add(part);
                  }
               }
            }

            if (mainPart.getType() == IFocusPart.EnumFocusPartType.MEDIUM) {
               core.medium = (IFocusPartMedium)mainPart;
               core.mediumModifiers = (IFocusPart[])mods.toArray(new IFocusPart[mods.size()]);
            }

            if (mainPart.getType() == IFocusPart.EnumFocusPartType.EFFECT) {
               FocusCore.FocusEffect fe = new FocusCore.FocusEffect();
               fe.effect = (IFocusPartEffect)mainPart;
               fe.modifiers = (IFocusPart[])mods.toArray(new IFocusPart[mods.size()]);
               ArrayList<String> ex = new ArrayList();
               ex.add(mainKey);
               float[] m = this.getModsRecursive(h, mainPart, new float[]{fe.effect.getCostMultiplier(), fe.effect.getEffectMultiplier()}, ex);
               fe.costMultipler = m[0];
               fe.effectMultipler = m[1];
               effects.add(fe);
            }
         }
      }
   }

   private float[] getModsRecursive(HexUtils.Hex h, IFocusPart mainPart, float[] mods, ArrayList<String> exlude) {
      for(int a = 0; a < 6; ++a) {
         HexUtils.Hex target = h.getNeighbour(a);
         String key = (String)this.data.hexes.get(target.toString());
         if (key != null && !key.isEmpty() && !exlude.contains(key)) {
            IFocusPart part = FocusHelper.getFocusPart(key);
            if (part.getType() != IFocusPart.EnumFocusPartType.EFFECT && FocusHelper.canPartsConnect(part, mainPart)) {
               exlude.add(key);
               mods = this.getModsRecursive(target, part, new float[]{mods[0] * part.getCostMultiplier(), mods[1] * part.getEffectMultiplier()}, exlude);
            }
         }
      }

      return mods;
   }

   public void endCraft() {
      this.vis = 0.0F;
      if (this.func_70301_a(0) != null && this.func_70301_a(0).func_77973_b() == ItemsTC.focusBlank) {
         FocusCore core = this.genCore();
         if (core != null) {
            this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.wand, SoundCategory.BLOCKS, 1.0F, 1.0F);
            ItemStack focus = new ItemStack(ItemsTC.focus);
            ItemFocus.setCore(focus, core);
            this.func_70299_a(0, focus);
            this.generateHexes(true);
            this.crystalsSync = new AspectList();
            this.syncTile(false);
            this.func_70296_d();
         }
      }

   }

   public boolean startCraft(int id, EntityPlayer p) {
      if (this.data != null && !(this.vis > 0.0F) && this.func_70301_a(0) != null && this.func_70301_a(0).func_77973_b() == ItemsTC.focusBlank) {
         Iterator var3 = this.data.hexes.keySet().iterator();

         while(var3.hasNext()) {
            String hs = (String)var3.next();
            String mainKey = (String)this.data.hexes.get(hs);
            IFocusPart part = FocusHelper.getFocusPart(mainKey);
            if (part != null && !ThaumcraftCapabilities.knowsResearchStrict(p, part.getResearch())) {
               return false;
            }
         }

         FocusCore core = this.calcCosts();
         if (this.crystals.getAspects().length <= 0) {
            return false;
         } else {
            ItemStack[] components = new ItemStack[this.crystals.getAspects().length];
            int r = 0;
            Aspect[] var14 = this.crystals.getAspects();
            int var7 = var14.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Aspect as = var14[var8];
               components[r] = ThaumcraftApiHelper.makeCrystal(as, this.crystals.getAmount(as));
               ++r;
            }

            if (components.length >= 0) {
               int a;
               for(a = 0; a < components.length; ++a) {
                  if (!InventoryUtils.isPlayerCarryingAmount(p, components[a], false)) {
                     return false;
                  }
               }

               for(a = 0; a < components.length; ++a) {
                  InventoryUtils.consumeInventoryItem(p, components[a], true, false);
               }

               this.crystalsSync = this.crystals.copy();
            }

            if (!p.field_71075_bZ.field_75098_d && p.field_71068_ca < this.xpCost) {
               return false;
            } else {
               if (!p.field_71075_bZ.field_75098_d) {
                  p.func_71013_b(this.xpCost);
               }

               this.vis = this.visCost;
               this.func_70296_d();
               this.syncTile(false);
               this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.craftstart, SoundCategory.BLOCKS, 1.0F, 1.0F);
               ItemStack focus = new ItemStack(ItemsTC.focus);
               ItemFocus.setCore(focus, core);
               FMLCommonHandler.instance().firePlayerCraftingEvent(p, focus, this);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public FocusCore calcCosts() {
      this.crystals = new AspectList();
      this.visCost = 0.0F;
      this.xpCost = 0;
      this.castCost = 0.0F;
      FocusCore core = this.genCore();
      if (core == null) {
         return null;
      } else {
         this.xpCost = core.partsRaw.size() * 6;
         if (core.partsRaw.size() > 0) {
            this.visCost = 100.0F;
         }

         for(int a = 1; a < core.partsRaw.size(); ++a) {
            this.visCost = (float)((double)this.visCost * 1.5D);
         }

         this.castCost = core.cost;
         Iterator var4 = core.partsRaw.values().iterator();

         while(var4.hasNext()) {
            IFocusPart p = (IFocusPart)var4.next();
            this.crystals.add(p.getAspect(), 1);
         }

         return core;
      }
   }

   public boolean func_94041_b(int par1, ItemStack stack) {
      return stack != null && stack.func_77973_b() == ItemsTC.focusBlank;
   }

   public boolean func_145842_c(int i, int j) {
      if (i == 1) {
         this.generateHexes(false);
         this.doGuiReset = true;
      }

      if (i == 5) {
         if (this.field_145850_b.field_72995_K) {
            FXDispatcher.INSTANCE.visSparkle(this.field_174879_c.func_177958_n() + this.func_145831_w().field_73012_v.nextInt(3) - this.func_145831_w().field_73012_v.nextInt(3), this.field_174879_c.func_177956_o() + this.func_145831_w().field_73012_v.nextInt(3), this.field_174879_c.func_177952_p() + this.func_145831_w().field_73012_v.nextInt(3) - this.func_145831_w().field_73012_v.nextInt(3), this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o() + 1, this.field_174879_c.func_177952_p(), j);
         }

         return true;
      } else {
         return super.func_145842_c(i, j);
      }
   }
}
