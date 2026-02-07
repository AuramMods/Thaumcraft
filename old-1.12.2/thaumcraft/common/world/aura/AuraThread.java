package thaumcraft.common.world.aura;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.Thaumcraft;
import thaumcraft.common.lib.events.ServerEvents;

public class AuraThread implements Runnable {
   public int dim;
   private final long INTERVAL = 1000L;
   private boolean stop = false;
   Random rand = new Random(System.currentTimeMillis());
   private float phaseVis = 0.0F;
   private float phaseFlux = 0.0F;
   private float[] phaseTable = new float[]{0.25F, 0.15F, 0.1F, 0.05F, 0.0F, 0.05F, 0.1F, 0.15F};

   public AuraThread(int dim2) {
      this.dim = dim2;
   }

   public void run() {
      Thaumcraft.log.info("Starting aura thread for dim " + this.dim);

      while(!this.stop) {
         if (AuraHandler.auras.isEmpty()) {
            Thaumcraft.log.warn("No auras found!");
            break;
         }

         long startTime = System.currentTimeMillis();
         AuraWorld auraWorld = AuraHandler.getAuraWorld(this.dim);
         if (auraWorld != null) {
            World world = DimensionManager.getWorld(this.dim);
            if (world != null) {
               this.phaseVis = this.phaseTable[world.field_73011_w.func_76559_b(world.func_72912_H().func_76073_f())];
               this.phaseFlux = 0.25F - this.phaseVis;
            }

            Iterator var5 = auraWorld.auraChunks.values().iterator();

            while(var5.hasNext()) {
               AuraChunk auraChunk = (AuraChunk)var5.next();
               this.processAuraChunk(auraWorld, auraChunk);
            }
         } else {
            this.stop();
         }

         long executionTime = System.currentTimeMillis() - startTime;

         try {
            if (executionTime > 1000L) {
               Thaumcraft.log.warn("AURAS TAKING " + (executionTime - 1000L) + " ms LONGER THAN NORMAL IN DIM " + this.dim);
            }

            Thread.sleep(Math.max(1L, 1000L - executionTime));
         } catch (InterruptedException var7) {
         }
      }

      Thaumcraft.log.info("Stopping aura thread for dim " + this.dim);
      ServerEvents.auraThreads.remove(this.dim);
   }

   private void processAuraChunk(AuraWorld auraWorld, AuraChunk auraChunk) {
      List<Integer> directions = Arrays.asList(0, 1, 2, 3);
      Collections.shuffle(directions, this.rand);
      int x = auraChunk.loc.field_77276_a;
      int y = auraChunk.loc.field_77275_b;
      short base = auraChunk.getBase();
      boolean dirty = false;
      float currentVis = auraChunk.getVis();
      float currentFlux = auraChunk.getFlux();
      AuraChunk neighbourVisChunk = null;
      AuraChunk neighbourFluxChunk = null;
      float lowestVisFlux = Float.MAX_VALUE;
      float lowestVis = Float.MAX_VALUE;
      float lowestFlux = Float.MAX_VALUE;
      Iterator var15 = directions.iterator();

      while(true) {
         AuraChunk n;
         do {
            do {
               if (!var15.hasNext()) {
                  boolean shared = false;
                  float inc;
                  if (neighbourVisChunk != null && lowestVis < currentVis && (double)(lowestVis / currentVis) < 0.75D) {
                     inc = Math.min(currentVis - lowestVis, 1.0F);
                     currentVis -= inc;
                     neighbourVisChunk.setVis(lowestVis + inc);
                     dirty = true;
                     this.markChunkAsDirty(neighbourVisChunk, auraWorld.dim);
                     shared = true;
                  }

                  if (neighbourFluxChunk != null && lowestFlux < currentFlux && (double)(lowestFlux / currentFlux) < 0.5D) {
                     inc = Math.min(currentFlux - lowestFlux, 1.0F);
                     currentFlux -= inc;
                     neighbourFluxChunk.setFlux(lowestFlux + inc);
                     dirty = true;
                     this.markChunkAsDirty(neighbourFluxChunk, auraWorld.dim);
                  }

                  if (currentVis + currentFlux > (float)base && !shared && neighbourVisChunk != null && lowestVis < currentVis + currentFlux && (double)((lowestVis + lowestVisFlux) / (currentVis + currentFlux)) < 0.75D) {
                     inc = Math.min(currentVis + currentFlux - lowestVis, Math.min((float)base - (currentVis + currentFlux), 1.0F));
                     neighbourVisChunk.setVis(lowestVis + inc);
                     currentVis -= inc;
                     dirty = true;
                     this.markChunkAsDirty(neighbourVisChunk, auraWorld.dim);
                  }

                  if ((double)currentVis > (double)base * 1.1D || (double)currentVis <= (double)base * 0.1D && currentVis >= currentFlux && (double)this.rand.nextFloat() < 0.1D) {
                     currentFlux += this.phaseFlux;
                     dirty = true;
                  }

                  if (currentVis + currentFlux < (float)base) {
                     inc = Math.min((float)base - (currentVis + currentFlux), this.phaseVis);
                     currentVis += inc;
                     dirty = true;
                  }

                  if (dirty) {
                     auraChunk.setVis(currentVis);
                     auraChunk.setFlux(currentFlux);
                     this.markChunkAsDirty(auraChunk, auraWorld.dim);
                  }

                  return;
               }

               Integer a = (Integer)var15.next();
               EnumFacing dir = EnumFacing.func_176731_b(a);
               n = auraWorld.getAuraChunkAt(x + dir.func_82601_c(), y + dir.func_82599_e());
            } while(n == null);

            if ((neighbourVisChunk == null || lowestVis > n.getVis()) && n.getVis() + n.getFlux() < (float)n.getBase()) {
               neighbourVisChunk = n;
               lowestVis = n.getVis();
               lowestVisFlux = n.getFlux();
            }
         } while(neighbourFluxChunk != null && !(lowestFlux > n.getFlux()));

         neighbourFluxChunk = n;
         lowestFlux = n.getFlux();
      }
   }

   private void markChunkAsDirty(AuraChunk chunk, int dim) {
      if (!chunk.isModified()) {
         ChunkPos pos = new ChunkPos(chunk.loc.field_77276_a, chunk.loc.field_77275_b);
         if (!AuraHandler.dirtyChunks.containsKey(dim)) {
            AuraHandler.dirtyChunks.put(dim, new CopyOnWriteArrayList());
         }

         CopyOnWriteArrayList<ChunkPos> dc = (CopyOnWriteArrayList)AuraHandler.dirtyChunks.get(dim);
         if (!dc.contains(pos)) {
            dc.add(pos);
         }

      }
   }

   public void stop() {
      this.stop = true;
   }
}
