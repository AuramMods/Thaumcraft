package thaumcraft.common.lib.research;

import java.util.HashMap;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.utils.HexUtils;

public class ResearchNoteData {
   public int color;
   public HashMap<String, ResearchNoteData.HexEntry> hexEntries = new HashMap();
   public HashMap<String, HexUtils.Hex> hexes = new HashMap();
   public boolean complete;
   public AspectList aspects = new AspectList();

   public boolean isComplete() {
      return this.complete;
   }

   public static class HexEntry {
      public Aspect aspect;
      public int type;
      public boolean bonus;

      public HexEntry(Aspect aspect, int type) {
         this.aspect = aspect;
         this.type = type;
      }

      public HexEntry(Aspect aspect, int type, boolean bonus) {
         this.aspect = aspect;
         this.type = type;
         this.bonus = bonus;
      }
   }
}
