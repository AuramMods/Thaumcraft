package thaumcraft.api.capabilities;

import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import thaumcraft.api.research.ResearchCategory;

public interface IPlayerKnowledge extends INBTSerializable<NBTTagCompound> {
   void clear();

   IPlayerKnowledge.EnumResearchStatus getResearchStatus(@Nonnull String var1);

   boolean isResearchComplete(String var1);

   boolean isResearchKnown(String var1);

   int getResearchStage(@Nonnull String var1);

   boolean addResearch(@Nonnull String var1);

   boolean setResearchStage(@Nonnull String var1, int var2);

   boolean removeResearch(@Nonnull String var1);

   @Nonnull
   Set<String> getResearchList();

   boolean setResearchFlag(@Nonnull String var1, @Nonnull IPlayerKnowledge.EnumResearchFlag var2);

   boolean clearResearchFlag(@Nonnull String var1, @Nonnull IPlayerKnowledge.EnumResearchFlag var2);

   boolean hasResearchFlag(@Nonnull String var1, @Nonnull IPlayerKnowledge.EnumResearchFlag var2);

   boolean addKnowledge(@Nonnull IPlayerKnowledge.EnumKnowledgeType var1, ResearchCategory var2, int var3);

   int getKnowledge(@Nonnull IPlayerKnowledge.EnumKnowledgeType var1, ResearchCategory var2);

   int getKnowledgeRaw(@Nonnull IPlayerKnowledge.EnumKnowledgeType var1, ResearchCategory var2);

   void sync(EntityPlayerMP var1);

   public static enum EnumResearchFlag {
      PAGE,
      RESEARCH,
      POPUP;
   }

   public static enum EnumKnowledgeType {
      THEORY(32, true, "T"),
      OBSERVATION(16, true, "O"),
      EPIPHANY(256, false, "E");

      private short progression;
      private boolean hasFields;
      private String abbr;

      private EnumKnowledgeType(int progression, boolean hasFields, String abbr) {
         this.progression = (short)progression;
         this.hasFields = hasFields;
         this.abbr = abbr;
      }

      public int getProgression() {
         return this.progression;
      }

      public boolean hasFields() {
         return this.hasFields;
      }

      public String getAbbreviation() {
         return this.abbr;
      }
   }

   public static enum EnumResearchStatus {
      UNKNOWN,
      COMPLETE,
      IN_PROGRESS;
   }
}
