package thaumcraft.common.world.aura;

import java.lang.ref.WeakReference;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class AuraChunk {
   ChunkPos loc;
   short base;
   float vis;
   float flux;
   WeakReference<Chunk> chunkRef;

   public AuraChunk(ChunkPos loc) {
      this.loc = loc;
   }

   public AuraChunk(Chunk chunk, short base, float vis, float flux) {
      if (chunk != null) {
         this.loc = chunk.func_76632_l();
         this.chunkRef = new WeakReference(chunk);
      }

      this.base = base;
      this.vis = vis;
      this.flux = flux;
   }

   public boolean isModified() {
      return this.chunkRef != null && this.chunkRef.get() != null ? ((Chunk)this.chunkRef.get()).func_76601_a(false) : false;
   }

   public short getBase() {
      return this.base;
   }

   public void setBase(short base) {
      this.base = base;
   }

   public float getVis() {
      return this.vis;
   }

   public void setVis(float vis) {
      this.vis = Math.min(32766.0F, Math.max(0.0F, vis));
   }

   public float getFlux() {
      return this.flux;
   }

   public void setFlux(float flux) {
      this.flux = Math.min(32766.0F, Math.max(0.0F, flux));
   }

   public ChunkPos getLoc() {
      return this.loc;
   }

   public void setLoc(ChunkPos loc) {
      this.loc = loc;
   }
}
