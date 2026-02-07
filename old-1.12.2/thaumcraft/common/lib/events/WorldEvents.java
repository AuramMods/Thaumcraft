package thaumcraft.common.lib.events;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.MultiPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.event.world.NoteBlockEvent.Play;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.tiles.devices.TileArcaneEar;
import thaumcraft.common.world.aura.AuraHandler;

public class WorldEvents {
   public static WorldEvents INSTANCE = new WorldEvents();

   @SubscribeEvent
   public void worldLoad(Load event) {
      if (!event.getWorld().field_72995_K) {
         AuraHandler.addAuraWorld(event.getWorld().field_73011_w.getDimension());
      }

   }

   @SubscribeEvent
   public void worldSave(Save event) {
      if (!event.getWorld().field_72995_K) {
      }

   }

   @SubscribeEvent
   public void worldUnload(Unload event) {
      if (!event.getWorld().field_72995_K) {
         SealHandler.sealEntities.remove(event.getWorld().field_73011_w.getDimension());
         AuraHandler.removeAuraWorld(event.getWorld().field_73011_w.getDimension());
      }
   }

   @SubscribeEvent
   public void placeBlockEvent(PlaceEvent event) {
      if (this.isNearActiveBoss(event.getWorld(), event.getPlayer(), event.getPos().func_177958_n(), event.getPos().func_177956_o(), event.getPos().func_177952_p())) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void placeBlockEvent(MultiPlaceEvent event) {
      if (this.isNearActiveBoss(event.getWorld(), event.getPlayer(), event.getPos().func_177958_n(), event.getPos().func_177956_o(), event.getPos().func_177952_p())) {
         event.setCanceled(true);
      }

   }

   private boolean isNearActiveBoss(World world, EntityPlayer player, int x, int y, int z) {
      return false;
   }

   @SubscribeEvent
   public void noteEvent(Play event) {
      if (!event.getWorld().field_72995_K) {
         if (!TileArcaneEar.noteBlockEvents.containsKey(event.getWorld().field_73011_w.getDimension())) {
            TileArcaneEar.noteBlockEvents.put(event.getWorld().field_73011_w.getDimension(), new ArrayList());
         }

         ArrayList<Integer[]> list = (ArrayList)TileArcaneEar.noteBlockEvents.get(event.getWorld().field_73011_w.getDimension());
         list.add(new Integer[]{event.getPos().func_177958_n(), event.getPos().func_177956_o(), event.getPos().func_177952_p(), event.getInstrument().ordinal(), event.getVanillaNoteId()});
         TileArcaneEar.noteBlockEvents.put(event.getWorld().field_73011_w.getDimension(), list);
      }
   }

   @SubscribeEvent
   public void explosionEventDetonate(Detonate event) {
      if (event.getAffectedEntities() != null && !event.getWorld().field_72995_K) {
         Iterator var2 = event.getAffectedEntities().iterator();

         while(true) {
            Entity e;
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var2.hasNext()) {
                              return;
                           }

                           e = (Entity)var2.next();
                        } while(e == null);
                     } while(!(e instanceof EntityItem));
                  } while(((EntityItem)e).func_92059_d() == null);
               } while(((EntityItem)e).func_92059_d().func_77973_b() != ItemsTC.primordialPearl);
            } while(((EntityItem)e).func_92059_d().func_77952_i() != 2);

            int qq = 0;

            for(int q = 0; q < ((EntityItem)e).func_92059_d().field_77994_a; ++q) {
               int num = 2 + event.getWorld().field_73012_v.nextInt(2);

               for(int a = 0; a < num; ++a) {
                  EntityItem entityitem = new EntityItem(event.getWorld(), e.field_70165_t, e.field_70163_u, e.field_70161_v, new ItemStack(ItemsTC.primordialPearl));
                  entityitem.func_174869_p();
                  entityitem.field_70159_w *= 3.0D;
                  entityitem.field_70181_x *= 3.0D;
                  entityitem.field_70179_y *= 3.0D;
                  if (e.captureDrops) {
                     e.capturedDrops.add(entityitem);
                  } else {
                     e.field_70170_p.func_72838_d(entityitem);
                  }

                  BlockPos p = new BlockPos(event.getExplosion().getPosition());
                  int xx = MathHelper.func_76136_a(event.getWorld().field_73012_v, -4, 4);
                  int yy = MathHelper.func_76136_a(event.getWorld().field_73012_v, 0, 4);
                  int zz = MathHelper.func_76136_a(event.getWorld().field_73012_v, -4, 4);
                  p = p.func_177982_a(xx, yy, zz);
                  AuraHelper.polluteAura(event.getWorld(), p, (float)(5 + event.getWorld().field_73012_v.nextInt(10)), event.getWorld().func_175623_d(p) && qq++ < 8);
               }
            }
         }
      }
   }
}
