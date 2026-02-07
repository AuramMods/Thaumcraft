package thaumcraft.common.lib.events;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.ChunkDataEvent.Load;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.construct.golem.seals.SealEntity;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketSealToClient;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

public class ChunkEvents {
   @SubscribeEvent
   public void chunkSave(Save event) {
      int dim = event.getWorld().field_73011_w.getDimension();
      ChunkPos loc = event.getChunk().func_76632_l();
      NBTTagCompound nbt = new NBTTagCompound();
      event.getData().func_74782_a("Thaumcraft", nbt);
      nbt.func_74757_a(Config.regenKey, true);
      AuraChunk ac = AuraHandler.getAuraChunk(dim, loc.field_77276_a, loc.field_77275_b);
      if (ac != null) {
         nbt.func_74777_a("base", ac.getBase());
         nbt.func_74776_a("flux", ac.getFlux());
         nbt.func_74776_a("vis", ac.getVis());
         if (!event.getChunk().func_177410_o()) {
            AuraHandler.removeAuraChunk(dim, loc.field_77276_a, loc.field_77275_b);
         }
      }

      NBTTagList tagList = new NBTTagList();
      Iterator var7 = SealHandler.getSealsInChunk(event.getWorld(), loc).iterator();

      while(var7.hasNext()) {
         ISealEntity seal = (ISealEntity)var7.next();
         NBTTagCompound sealnbt = seal.writeNBT();
         tagList.func_74742_a(sealnbt);
         if (!event.getChunk().func_177410_o()) {
            SealHandler.removeSealEntity(event.getWorld(), seal.getSealPos(), true);
         }
      }

      nbt.func_74782_a("seals", tagList);
   }

   @SubscribeEvent
   public void chunkLoad(Load event) {
      int dim = event.getWorld().field_73011_w.getDimension();
      ChunkPos loc = event.getChunk().func_76632_l();
      NBTTagCompound nbt;
      if (event.getData().func_74775_l("Thaumcraft").func_74764_b("base")) {
         nbt = event.getData().func_74775_l("Thaumcraft");
         short base = nbt.func_74765_d("base");
         float flux = nbt.func_74760_g("flux");
         float vis = nbt.func_74760_g("vis");
         AuraHandler.addAuraChunk(dim, event.getChunk(), base, vis, flux);
      } else {
         AuraHandler.generateAura(event.getChunk(), event.getWorld().field_73012_v);
      }

      if (event.getData().func_74775_l("Thaumcraft").func_74764_b("seals")) {
         nbt = event.getData().func_74775_l("Thaumcraft");
         NBTTagList tagList = nbt.func_150295_c("seals", 10);

         for(int a = 0; a < tagList.func_74745_c(); ++a) {
            NBTTagCompound tasknbt = tagList.func_150305_b(a);
            SealEntity seal = new SealEntity();
            seal.readNBT(tasknbt);
            SealHandler.addSealEntity(event.getWorld(), seal);
         }
      }

      if (!event.getData().func_74775_l("Thaumcraft").func_74764_b(Config.regenKey) && (Config.regenAmber || Config.regenAura || Config.regenCinnibar || Config.regenCrystals || Config.regenStructure || Config.regenTrees)) {
         Thaumcraft.log.warn("World gen was never run for chunk at " + event.getChunk().func_76632_l() + ". Adding to queue for regeneration.");
         ArrayList<ChunkPos> chunks = (ArrayList)ServerEvents.chunksToGenerate.get(dim);
         if (chunks == null) {
            ServerEvents.chunksToGenerate.put(dim, new ArrayList());
            chunks = (ArrayList)ServerEvents.chunksToGenerate.get(dim);
         }

         if (chunks != null) {
            chunks.add(new ChunkPos(loc.field_77276_a, loc.field_77275_b));
            ServerEvents.chunksToGenerate.put(dim, chunks);
         }
      }

   }

   @SubscribeEvent
   public void chunkWatch(Watch event) {
      Iterator var2 = SealHandler.getSealsInChunk(event.getPlayer().field_70170_p, event.getChunk()).iterator();

      while(var2.hasNext()) {
         ISealEntity seal = (ISealEntity)var2.next();
         PacketHandler.INSTANCE.sendTo(new PacketSealToClient(seal), event.getPlayer());
      }

   }
}
