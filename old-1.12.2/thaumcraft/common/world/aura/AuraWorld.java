package thaumcraft.common.world.aura;

import java.util.concurrent.ConcurrentHashMap;
import thaumcraft.common.lib.utils.PosXY;

public class AuraWorld {
   int dim;
   ConcurrentHashMap<PosXY, AuraChunk> auraChunks = new ConcurrentHashMap();

   public AuraWorld(int dim) {
      this.dim = dim;
   }

   public ConcurrentHashMap<PosXY, AuraChunk> getAuraChunks() {
      return this.auraChunks;
   }

   public void setAuraChunks(ConcurrentHashMap<PosXY, AuraChunk> auraChunks) {
      this.auraChunks = auraChunks;
   }

   public AuraChunk getAuraChunkAt(int x, int y) {
      return this.getAuraChunkAt(new PosXY(x, y));
   }

   public AuraChunk getAuraChunkAt(PosXY loc) {
      return (AuraChunk)this.auraChunks.get(loc);
   }
}
