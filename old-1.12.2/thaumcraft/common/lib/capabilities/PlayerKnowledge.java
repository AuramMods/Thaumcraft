package thaumcraft.common.lib.capabilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncKnowledge;

public class PlayerKnowledge {
   public static void preInit() {
      CapabilityManager.INSTANCE.register(IPlayerKnowledge.class, new IStorage<IPlayerKnowledge>() {
         public NBTTagCompound writeNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, EnumFacing side) {
            return (NBTTagCompound)instance.serializeNBT();
         }

         public void readNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
               instance.deserializeNBT((NBTTagCompound)nbt);
            }

         }
      }, () -> {
         return new PlayerKnowledge.DefaultImpl();
      });
   }

   public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
      public static final ResourceLocation NAME = new ResourceLocation("thaumcraft", "knowledge");
      private final PlayerKnowledge.DefaultImpl knowledge = new PlayerKnowledge.DefaultImpl();

      public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
         return capability == ThaumcraftCapabilities.KNOWLEDGE;
      }

      public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
         return capability == ThaumcraftCapabilities.KNOWLEDGE ? ThaumcraftCapabilities.KNOWLEDGE.cast(this.knowledge) : null;
      }

      public NBTTagCompound serializeNBT() {
         return this.knowledge.serializeNBT();
      }

      public void deserializeNBT(NBTTagCompound nbt) {
         this.knowledge.deserializeNBT(nbt);
      }
   }

   private static class DefaultImpl implements IPlayerKnowledge {
      private final HashSet<String> research;
      private final Map<String, Integer> stages;
      private final Map<String, HashSet<IPlayerKnowledge.EnumResearchFlag>> flags;
      private final Map<String, Integer> knowledge;

      private DefaultImpl() {
         this.research = new HashSet();
         this.stages = new HashMap();
         this.flags = new HashMap();
         this.knowledge = new HashMap();
      }

      public void clear() {
         this.research.clear();
         this.flags.clear();
         this.stages.clear();
         this.knowledge.clear();
      }

      public IPlayerKnowledge.EnumResearchStatus getResearchStatus(@Nonnull String res) {
         if (!this.isResearchKnown(res)) {
            return IPlayerKnowledge.EnumResearchStatus.UNKNOWN;
         } else {
            ResearchEntry entry = ResearchCategories.getResearch(res);
            return entry != null && entry.getStages() != null && this.getResearchStage(res) <= entry.getStages().length ? IPlayerKnowledge.EnumResearchStatus.IN_PROGRESS : IPlayerKnowledge.EnumResearchStatus.COMPLETE;
         }
      }

      public boolean isResearchKnown(String res) {
         if (res == null) {
            return false;
         } else if (res.equals("")) {
            return true;
         } else {
            String[] ss = res.split("@");
            return ss.length > 1 && this.getResearchStage(ss[0]) < MathHelper.func_82715_a(ss[1], 0) ? false : this.research.contains(ss[0]);
         }
      }

      public boolean isResearchComplete(String res) {
         return this.getResearchStatus(res) == IPlayerKnowledge.EnumResearchStatus.COMPLETE;
      }

      public int getResearchStage(String res) {
         if (res != null && this.research.contains(res)) {
            Integer stage = (Integer)this.stages.get(res);
            return stage == null ? 0 : stage;
         } else {
            return -1;
         }
      }

      public boolean setResearchStage(String res, int stage) {
         if (res != null && this.research.contains(res) && stage > 0) {
            this.stages.put(res, stage);
            return true;
         } else {
            return false;
         }
      }

      public boolean addResearch(@Nonnull String res) {
         if (!this.isResearchKnown(res)) {
            this.research.add(res);
            return true;
         } else {
            return false;
         }
      }

      public boolean removeResearch(@Nonnull String res) {
         if (this.isResearchKnown(res)) {
            this.research.remove(res);
            return true;
         } else {
            return false;
         }
      }

      @Nonnull
      public Set<String> getResearchList() {
         return Collections.unmodifiableSet(this.research);
      }

      public boolean setResearchFlag(@Nonnull String res, @Nonnull IPlayerKnowledge.EnumResearchFlag flag) {
         HashSet<IPlayerKnowledge.EnumResearchFlag> list = (HashSet)this.flags.get(res);
         if (list == null) {
            list = new HashSet();
            this.flags.put(res, list);
         }

         if (list.contains(flag)) {
            return false;
         } else {
            list.add(flag);
            return true;
         }
      }

      public boolean clearResearchFlag(@Nonnull String res, @Nonnull IPlayerKnowledge.EnumResearchFlag flag) {
         HashSet<IPlayerKnowledge.EnumResearchFlag> list = (HashSet)this.flags.get(res);
         if (list != null) {
            boolean b = list.remove(flag);
            if (list.isEmpty()) {
               this.flags.remove(this.research);
            }

            return b;
         } else {
            return false;
         }
      }

      public boolean hasResearchFlag(@Nonnull String res, @Nonnull IPlayerKnowledge.EnumResearchFlag flag) {
         return this.flags.get(res) != null ? ((HashSet)this.flags.get(res)).contains(flag) : false;
      }

      private String getKey(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category) {
         return type.getAbbreviation() + "_" + (category == null ? "" : category.key);
      }

      public boolean addKnowledge(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category, int amount) {
         String key = this.getKey(type, category);
         int c = this.getKnowledgeRaw(type, category);
         if (c + amount < 0) {
            return false;
         } else {
            c += amount;
            this.knowledge.put(key, c);
            return true;
         }
      }

      public int getKnowledge(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category) {
         String key = this.getKey(type, category);
         int c = !this.knowledge.containsKey(key) ? 0 : (Integer)this.knowledge.get(key);
         return (int)Math.floor((double)c / (double)type.getProgression());
      }

      public int getKnowledgeRaw(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category) {
         String key = this.getKey(type, category);
         return !this.knowledge.containsKey(key) ? 0 : (Integer)this.knowledge.get(key);
      }

      public void sync(@Nonnull EntityPlayerMP player) {
         PacketHandler.INSTANCE.sendTo(new PacketSyncKnowledge(player), player);
      }

      public NBTTagCompound serializeNBT() {
         NBTTagCompound rootTag = new NBTTagCompound();
         NBTTagList researchList = new NBTTagList();

         NBTTagCompound tag;
         for(Iterator var3 = this.research.iterator(); var3.hasNext(); researchList.func_74742_a(tag)) {
            String resKey = (String)var3.next();
            tag = new NBTTagCompound();
            tag.func_74778_a("key", resKey);
            if (this.stages.containsKey(resKey)) {
               tag.func_74768_a("stage", (Integer)this.stages.get(resKey));
            }

            if (this.flags.containsKey(resKey)) {
               HashSet<IPlayerKnowledge.EnumResearchFlag> list = (HashSet)this.flags.get(resKey);
               if (list != null) {
                  String fs = "";

                  IPlayerKnowledge.EnumResearchFlag flag;
                  for(Iterator var8 = list.iterator(); var8.hasNext(); fs = fs + flag.name()) {
                     flag = (IPlayerKnowledge.EnumResearchFlag)var8.next();
                     if (fs.length() > 0) {
                        fs = fs + ",";
                     }
                  }

                  tag.func_74778_a("flags", fs);
               }
            }
         }

         rootTag.func_74782_a("research", researchList);
         NBTTagList knowledgeList = new NBTTagList();
         Iterator var11 = this.knowledge.keySet().iterator();

         while(var11.hasNext()) {
            String key = (String)var11.next();
            Integer c = (Integer)this.knowledge.get(key);
            if (c != null && c > 0 && key != null && !key.isEmpty()) {
               NBTTagCompound tag = new NBTTagCompound();
               tag.func_74778_a("key", key);
               tag.func_74768_a("amount", c);
               knowledgeList.func_74742_a(tag);
            }
         }

         rootTag.func_74782_a("knowledge", knowledgeList);
         return rootTag;
      }

      public void deserializeNBT(NBTTagCompound rootTag) {
         if (rootTag != null) {
            this.clear();
            NBTTagList researchList = rootTag.func_150295_c("research", 10);

            for(int i = 0; i < researchList.func_74745_c(); ++i) {
               NBTTagCompound tag = researchList.func_150305_b(i);
               String know = tag.func_74779_i("key");
               if (know != null && !this.isResearchKnown(know)) {
                  this.research.add(know);
                  int stage = tag.func_74762_e("stage");
                  if (stage > 0) {
                     this.stages.put(know, stage);
                  }

                  String fs = tag.func_74779_i("flags");
                  if (fs.length() > 0) {
                     String[] ss = fs.split(",");
                     String[] var9 = ss;
                     int var10 = ss.length;

                     for(int var11 = 0; var11 < var10; ++var11) {
                        String s = var9[var11];
                        IPlayerKnowledge.EnumResearchFlag flag = null;

                        try {
                           flag = IPlayerKnowledge.EnumResearchFlag.valueOf(s);
                        } catch (Exception var15) {
                        }

                        if (flag != null) {
                           this.setResearchFlag(know, flag);
                        }
                     }
                  }
               }
            }

            NBTTagList knowledgeList = rootTag.func_150295_c("knowledge", 10);

            for(int i = 0; i < knowledgeList.func_74745_c(); ++i) {
               NBTTagCompound tag = knowledgeList.func_150305_b(i);
               String key = tag.func_74779_i("key");
               int amount = tag.func_74762_e("amount");
               this.knowledge.put(key, amount);
            }

            this.addAutoUnlockResearch();
         }
      }

      private void addAutoUnlockResearch() {
         Iterator var1 = ResearchCategories.researchCategories.values().iterator();

         while(var1.hasNext()) {
            ResearchCategory cat = (ResearchCategory)var1.next();
            Iterator var3 = cat.research.values().iterator();

            while(var3.hasNext()) {
               ResearchEntry ri = (ResearchEntry)var3.next();
               if (ri.hasMeta(ResearchEntry.EnumResearchMeta.AUTOUNLOCK)) {
                  this.addResearch(ri.getKey());
               }
            }
         }

      }

      // $FF: synthetic method
      DefaultImpl(Object x0) {
         this();
      }
   }
}
