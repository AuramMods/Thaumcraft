package thaumcraft.common.tiles.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.items.IScribeTools;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftManager;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileResearchTable extends TileThaumcraft implements IInventory {
   public ItemStack[] contents = new ItemStack[2];
   public ResearchTableData data = null;

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      NBTTagList var2 = nbttagcompound.func_150295_c("Inventory", 10);
      this.contents = new ItemStack[this.func_70302_i_()];

      for(int var3 = 0; var3 < Math.min(2, var2.func_74745_c()); ++var3) {
         NBTTagCompound var4 = var2.func_150305_b(var3);
         int var5 = var4.func_74771_c("Slot") & 255;
         if (var5 >= 0 && var5 < this.contents.length) {
            this.contents[var5] = ItemStack.func_77949_a(var4);
         }
      }

      if (nbttagcompound.func_74764_b("note")) {
         this.data = new ResearchTableData(this);
         this.data.deserialize(nbttagcompound.func_74775_l("note"));
      } else {
         this.data = null;
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.contents.length; ++var3) {
         if (this.contents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.func_74774_a("Slot", (byte)var3);
            this.contents[var3].func_77955_b(var4);
            var2.func_74742_a(var4);
         }
      }

      nbttagcompound.func_74782_a("Inventory", var2);
      if (this.data != null) {
         nbttagcompound.func_74782_a("note", this.data.serialize());
      } else {
         nbttagcompound.func_82580_o("note");
      }

      return nbttagcompound;
   }

   protected void func_190201_b(World worldIn) {
      super.func_190201_b(worldIn);
      if (!this.func_145830_o()) {
         this.func_145834_a(worldIn);
      }

   }

   public void startNewTheory(EntityPlayer player, Set<String> mutators) {
      this.data = new ResearchTableData(player, this);
      this.data.initialize(player, mutators);
      this.syncTile(false);
      this.func_70296_d();
   }

   public void finishTheory(EntityPlayer player) {
      String hc = "";
      int ht = 0;
      Iterator var4 = this.data.categoryTotals.keySet().iterator();

      while(var4.hasNext()) {
         String cat = (String)var4.next();
         int tot = Math.round((float)(Integer)this.data.categoryTotals.get(cat) / 100.0F * (float)IPlayerKnowledge.EnumKnowledgeType.THEORY.getProgression());
         if (tot > ht) {
            ht = tot;
            hc = cat;
         }

         ResearchCategory rc = ResearchCategories.getResearchCategory(cat);
         ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, rc, tot);
      }

      if (ht > 0) {
         ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory(hc), ht / 4);
      }

      this.data = null;
   }

   public Set<String> checkSurroundingAids() {
      HashMap<String, ITheorycraftAid> mutators = new HashMap();

      Iterator var5;
      String muk;
      ITheorycraftAid mu;
      for(int y = -1; y <= 1; ++y) {
         for(int x = -4; x <= 4; ++x) {
            for(int z = -4; z <= 4; ++z) {
               var5 = TheorycraftManager.aids.keySet().iterator();

               while(var5.hasNext()) {
                  muk = (String)var5.next();
                  mu = (ITheorycraftAid)TheorycraftManager.aids.get(muk);
                  IBlockState state = this.field_145850_b.func_180495_p(this.func_174877_v().func_177982_a(x, y, z));
                  if (mu.getAidObject() instanceof Block) {
                     if (state.func_177230_c() == (Block)mu.getAidObject()) {
                        mutators.put(muk, mu);
                     }
                  } else if (mu.getAidObject() instanceof ItemStack) {
                     ItemStack is = state.func_177230_c().func_185473_a(this.func_145831_w(), this.func_174877_v().func_177982_a(x, y, z), state);
                     if (is != null && is.func_185136_b((ItemStack)mu.getAidObject())) {
                        mutators.put(muk, mu);
                     }
                  }
               }
            }
         }
      }

      List<Entity> l = EntityUtils.getEntitiesInRange(this.func_145831_w(), this.func_174877_v(), (Entity)null, Entity.class, 5.0D);
      if (l != null && !l.isEmpty()) {
         Iterator var11 = l.iterator();

         while(var11.hasNext()) {
            Entity e = (Entity)var11.next();
            var5 = TheorycraftManager.aids.keySet().iterator();

            while(var5.hasNext()) {
               muk = (String)var5.next();
               mu = (ITheorycraftAid)TheorycraftManager.aids.get(muk);
               if (mu.getAidObject() instanceof Class && e.getClass().isAssignableFrom((Class)mu.getAidObject())) {
                  mutators.put(muk, mu);
               }
            }
         }
      }

      return mutators.keySet();
   }

   public boolean consumeInkFromTable() {
      if (this.contents[0] != null && this.contents[0].func_77973_b() instanceof IScribeTools && this.contents[0].func_77952_i() < this.contents[0].func_77958_k()) {
         this.contents[0].func_77964_b(this.contents[0].func_77952_i() + 1);
         this.syncTile(false);
         this.func_70296_d();
         return true;
      } else {
         return false;
      }
   }

   public boolean consumepaperFromTable() {
      if (this.contents[1] != null && this.contents[1].func_77973_b() == Items.field_151121_aF && this.contents[1].field_77994_a > 0) {
         this.func_70298_a(1, 1);
         this.syncTile(false);
         this.func_70296_d();
         return true;
      } else {
         return false;
      }
   }

   public int func_70302_i_() {
      return 2;
   }

   public ItemStack func_70301_a(int var1) {
      return this.contents[var1];
   }

   public ItemStack func_70298_a(int var1, int var2) {
      if (this.contents[var1] != null) {
         ItemStack var3;
         if (this.contents[var1].field_77994_a <= var2) {
            var3 = this.contents[var1];
            this.contents[var1] = null;
            this.func_70296_d();
            return var3;
         } else {
            var3 = this.contents[var1].func_77979_a(var2);
            if (this.contents[var1].field_77994_a == 0) {
               this.contents[var1] = null;
            }

            this.func_70296_d();
            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int var1) {
      if (this.contents[var1] != null) {
         ItemStack var2 = this.contents[var1];
         this.contents[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void func_70299_a(int var1, ItemStack var2) {
      this.contents[var1] = var2;
      if (var2 != null && var2.field_77994_a > this.func_70297_j_()) {
         var2.field_77994_a = this.func_70297_j_();
      }

      this.func_70296_d();
   }

   public String func_70005_c_() {
      return "Research Table";
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer var1) {
      return this.field_145850_b.func_175625_s(this.func_174877_v()) != this ? false : var1.func_174831_c(this.func_174877_v()) <= 64.0D;
   }

   public boolean func_145818_k_() {
      return false;
   }

   public boolean func_94041_b(int i, ItemStack itemstack) {
      if (itemstack == null) {
         return false;
      } else {
         switch(i) {
         case 0:
            if (itemstack.func_77973_b() instanceof IScribeTools) {
               return true;
            }
            break;
         case 1:
            if (itemstack.func_77973_b() == Items.field_151121_aF && itemstack.func_77952_i() == 0) {
               return true;
            }
         }

         return false;
      }
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.field_145850_b != null && this.field_145850_b.field_72995_K) {
         this.syncTile(false);
      }

   }

   public boolean func_145842_c(int i, int j) {
      if (i == 1) {
         if (this.field_145850_b.field_72995_K) {
            this.field_145850_b.func_184134_a((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), SoundsTC.learn, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
         }

         return true;
      } else {
         return super.func_145842_c(i, j);
      }
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {
   }

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
   }
}
