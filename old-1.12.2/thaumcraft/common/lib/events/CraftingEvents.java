package thaumcraft.common.lib.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.consumables.ItemPhial;
import thaumcraft.common.lib.research.ResearchManager;

public class CraftingEvents implements IFuelHandler {
   public int getBurnTime(ItemStack fuel) {
      if (fuel.func_77969_a(new ItemStack(ItemsTC.alumentum))) {
         return 4800;
      } else {
         return fuel.func_77969_a(new ItemStack(BlocksTC.log)) ? 400 : 0;
      }
   }

   @SubscribeEvent
   public void onCrafting(ItemCraftedEvent event) {
      int warp = ThaumcraftApi.getWarp(event.crafting);
      if (!Config.wuss && warp > 0 && !event.player.field_70170_p.field_72995_K) {
         ThaumcraftApi.internalMethods.addWarpToPlayer(event.player, warp, IPlayerWarp.EnumWarpType.NORMAL);
      }

      int stackHash;
      if (event.crafting.func_77973_b() == ItemsTC.label && event.crafting.func_77942_o()) {
         for(stackHash = 0; stackHash < 9; ++stackHash) {
            ItemStack var3 = event.craftMatrix.func_70301_a(stackHash);
            if (var3 != null && var3.func_77973_b() instanceof ItemPhial) {
               ++var3.field_77994_a;
               event.craftMatrix.func_70299_a(stackHash, var3);
            }
         }
      }

      if (event.player != null && !event.player.field_70170_p.field_72995_K) {
         stackHash = ResearchManager.createItemStackHash(event.crafting.func_77946_l());
         if (ResearchManager.craftingReferences.contains(stackHash)) {
            ResearchManager.completeResearch(event.player, "[#]" + stackHash);
         }
      }

   }
}
