package thaumcraft.common.lib.events;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.entities.construct.golem.ItemGolemBell;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;
import thaumcraft.common.lib.network.misc.PacketItemKeyToServer;

public class KeyHandler {
   public KeyBinding keyF = new KeyBinding("Change Wand Focus", 33, "key.categories.misc");
   public KeyBinding keyH = new KeyBinding("Activate Hover Harness", 35, "key.categories.misc");
   public KeyBinding keyG = new KeyBinding("Misc Wand Toggle", 34, "key.categories.misc");
   private boolean keyPressedF = false;
   private boolean keyPressedH = false;
   private boolean keyPressedG = false;
   public static boolean radialActive = false;
   public static boolean radialLock = false;
   public static long lastPressF = 0L;
   public static long lastPressH = 0L;
   public static long lastPressG = 0L;

   public KeyHandler() {
      ClientRegistry.registerKeyBinding(this.keyF);
      ClientRegistry.registerKeyBinding(this.keyH);
      ClientRegistry.registerKeyBinding(this.keyG);
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void playerTick(PlayerTickEvent event) {
      if (event.side != Side.SERVER) {
         if (event.phase == Phase.START) {
            EntityPlayer player;
            if (this.keyF.func_151470_d()) {
               if (FMLClientHandler.instance().getClient().field_71415_G) {
                  player = event.player;
                  if (player != null) {
                     if (!this.keyPressedF) {
                        lastPressF = System.currentTimeMillis();
                        radialLock = false;
                     }

                     if (!radialLock && (player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() instanceof ICaster || player.func_184592_cb() != null && player.func_184592_cb().func_77973_b() instanceof ICaster)) {
                        if (player.func_70093_af()) {
                           PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer("REMOVE"));
                        } else {
                           radialActive = true;
                        }
                     } else if (player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() instanceof ItemGolemBell && !this.keyPressedF) {
                        PacketHandler.INSTANCE.sendToServer(new PacketItemKeyToServer(0));
                     }
                  }

                  this.keyPressedF = true;
               }
            } else {
               radialActive = false;
               if (this.keyPressedF) {
                  lastPressF = System.currentTimeMillis();
               }

               this.keyPressedF = false;
            }

            if (this.keyG.func_151470_d()) {
               if (FMLClientHandler.instance().getClient().field_71415_G) {
                  player = event.player;
                  if (player != null && !this.keyPressedG) {
                     lastPressG = System.currentTimeMillis();
                     PacketHandler.INSTANCE.sendToServer(new PacketItemKeyToServer(1));
                  }

                  this.keyPressedG = true;
               }
            } else {
               if (this.keyPressedG) {
                  lastPressG = System.currentTimeMillis();
               }

               this.keyPressedG = false;
            }
         }

      }
   }
}
