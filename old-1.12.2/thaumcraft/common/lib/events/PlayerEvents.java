package thaumcraft.common.lib.events;

import baubles.api.BaublesApi;
import java.util.HashMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.AttachCapabilitiesEvent.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigResearch;
import thaumcraft.common.items.curios.ItemThaumonomicon;
import thaumcraft.common.items.resources.ItemCrystalEssence;
import thaumcraft.common.lib.capabilities.PlayerKnowledge;
import thaumcraft.common.lib.capabilities.PlayerWarp;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.potions.PotionDeathGaze;
import thaumcraft.common.lib.potions.PotionWarpWard;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.world.aura.AuraHandler;

public class PlayerEvents {
   public static PlayerEvents INSTANCE = new PlayerEvents();
   static HashMap<Integer, Long> nextCycle = new HashMap();
   static HashMap<Integer, Integer> lastCharge = new HashMap();
   static HashMap<Integer, Integer> lastMaxCharge = new HashMap();
   static HashMap<Integer, Integer> runicInfo = new HashMap();
   static HashMap<String, Long> upgradeCooldown = new HashMap();
   public HashMap<Integer, Float> prevStep = new HashMap();

   @SubscribeEvent
   public void livingTick(LivingUpdateEvent event) {
      if (event.getEntity() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.getEntity();
         this.handleMisc(player);
         this.handleSpeedMods(player);
         if (!player.field_70170_p.field_72995_K) {
            this.handleRunicArmor(player);
            this.handleWarp(player);
            if (player.field_70173_aa % 20 == 0 && ResearchManager.syncList.remove(player.func_70005_c_()) != null) {
               IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
               knowledge.sync((EntityPlayerMP)player);
            }

            if (player.field_70173_aa % 200 == 0) {
               ConfigResearch.checkPeriodicStuff(player);
            }
         }
      }

   }

   @SubscribeEvent
   public void pickupItem(EntityItemPickupEvent event) {
      if (event.getEntityPlayer() != null && !event.getEntityPlayer().field_70170_p.field_72995_K && event.getItem() != null && event.getItem().func_92059_d() != null) {
         IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());
         if (event.getItem().func_92059_d().func_77973_b() instanceof ItemCrystalEssence && !knowledge.isResearchKnown("!gotcrystals")) {
            knowledge.addResearch("!gotcrystals");
            knowledge.sync((EntityPlayerMP)event.getEntityPlayer());
            event.getEntityPlayer().func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.crystals")));
         }

         if (event.getItem().func_92059_d().func_77973_b() instanceof ItemThaumonomicon && !knowledge.isResearchKnown("!gotthaumonomicon")) {
            knowledge.addResearch("!gotthaumonomicon");
            knowledge.sync((EntityPlayerMP)event.getEntityPlayer());
         }
      }

   }

   @SubscribeEvent
   public void wakeUp(PlayerWakeUpEvent event) {
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer());
      if (event.getEntityPlayer() != null && !event.getEntityPlayer().field_70170_p.field_72995_K && knowledge.isResearchKnown("!gotcrystals") && !knowledge.isResearchKnown("!gotdream")) {
         knowledge.addResearch("!gotdream");
         knowledge.sync((EntityPlayerMP)event.getEntityPlayer());
         ItemStack book = ConfigItems.startBook.func_77946_l();
         book.func_77978_p().func_74778_a("author", event.getEntityPlayer().func_70005_c_());
         if (!event.getEntityPlayer().field_71071_by.func_70441_a(book)) {
            event.getEntityPlayer().func_70099_a(book, 2.0F);
         }

         event.getEntityPlayer().func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.dream")));
      }

   }

   private void handleMisc(EntityPlayer player) {
   }

   @SubscribeEvent
   public void tooltipEvent(ItemTooltipEvent event) {
      int charge = getRunicCharge(event.getItemStack());
      if (charge > 0) {
         event.getToolTip().add(TextFormatting.GOLD + I18n.func_74838_a("item.runic.charge") + " +" + charge);
      }

      int warp = getFinalWarp(event.getItemStack(), event.getEntityPlayer());
      if (warp > 0) {
         event.getToolTip().add(TextFormatting.DARK_PURPLE + I18n.func_74838_a("item.warping") + " " + warp);
      }

      int al = getFinalDiscount(event.getItemStack(), event.getEntityPlayer());
      if (al > 0) {
         event.getToolTip().add(TextFormatting.DARK_PURPLE + I18n.func_74838_a("tc.visdiscount") + ": " + al + "%");
      }

      if (event.getItemStack() != null) {
         if (event.getItemStack().func_77973_b() instanceof IRechargable) {
            int c = Math.round((float)RechargeHelper.getCharge(event.getItemStack()));
            if (c >= 0) {
               event.getToolTip().add(TextFormatting.YELLOW + I18n.func_74838_a("tc.charge") + " " + c);
            }
         }

         if (event.getItemStack().func_77973_b() instanceof IEssentiaContainerItem) {
            AspectList aspects = ((IEssentiaContainerItem)event.getItemStack().func_77973_b()).getAspects(event.getItemStack());
            if (aspects != null && aspects.size() > 0) {
               Aspect[] var6 = aspects.getAspectsSortedByName();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  Aspect tag = var6[var8];
                  event.getToolTip().add(tag.getName() + " x" + aspects.getAmount(tag));
               }
            }
         }

         NBTTagList nbttaglist = EnumInfusionEnchantment.getInfusionEnchantmentTagList(event.getItemStack());
         if (nbttaglist != null) {
            for(int j = 0; j < nbttaglist.func_74745_c(); ++j) {
               int k = nbttaglist.func_150305_b(j).func_74765_d("id");
               int l = nbttaglist.func_150305_b(j).func_74765_d("lvl");
               if (k >= 0 && k < EnumInfusionEnchantment.values().length) {
                  String s = TextFormatting.GOLD + I18n.func_74838_a("enchantment.infusion." + EnumInfusionEnchantment.values()[k].toString());
                  if (EnumInfusionEnchantment.values()[k].maxLevel > 1) {
                     s = s + " " + I18n.func_74838_a("enchantment.level." + l);
                  }

                  event.getToolTip().add(1, s);
               }
            }
         }
      }

   }

   private void handleRunicArmor(EntityPlayer player) {
      int charge;
      if (player.field_70173_aa % 20 == 0) {
         int max = 0;

         for(int a = 0; a < 4; ++a) {
            if (player.field_71071_by.field_70460_b[a] != null) {
               max += getRunicCharge(player.field_71071_by.field_70460_b[a]);
            }
         }

         IInventory baubles = BaublesApi.getBaubles(player);

         for(charge = 0; charge < baubles.func_70302_i_(); ++charge) {
            if (baubles.func_70301_a(charge) != null) {
               max += getRunicCharge(baubles.func_70301_a(charge));
            }
         }

         if (lastMaxCharge.containsKey(player.func_145782_y())) {
            charge = (Integer)lastMaxCharge.get(player.func_145782_y());
            if (charge > max) {
               player.func_110149_m(player.func_110139_bj() - (float)(charge - max));
            }

            if (max <= 0) {
               lastMaxCharge.remove(player.func_145782_y());
            }
         }

         if (max > 0) {
            runicInfo.put(player.func_145782_y(), max);
            lastMaxCharge.put(player.func_145782_y(), max);
         } else {
            runicInfo.remove(player.func_145782_y());
         }
      }

      if (runicInfo.containsKey(player.func_145782_y())) {
         if (!nextCycle.containsKey(player.func_145782_y())) {
            nextCycle.put(player.func_145782_y(), 0L);
         }

         long time = System.currentTimeMillis();
         charge = (int)player.func_110139_bj();
         if (charge == 0 && lastCharge.containsKey(player.func_145782_y()) && (Integer)lastCharge.get(player.func_145782_y()) > 0) {
            nextCycle.put(player.func_145782_y(), time + (long)Config.shieldWait);
            lastCharge.put(player.func_145782_y(), 0);
         }

         if (charge < (Integer)runicInfo.get(player.func_145782_y()) && (Long)nextCycle.get(player.func_145782_y()) < time && !AuraHandler.shouldPreserveAura(player.field_70170_p, player, player.func_180425_c()) && AuraHelper.getVis(player.field_70170_p, new BlockPos(player)) >= (float)Config.shieldCost) {
            AuraHandler.drainVis(player.field_70170_p, new BlockPos(player), (float)Config.shieldCost, false);
            nextCycle.put(player.func_145782_y(), time + (long)Config.shieldRecharge);
            player.func_110149_m((float)(charge + 1));
            lastCharge.put(player.func_145782_y(), charge + 1);
         }
      }

   }

   public static int getRunicCharge(ItemStack stack) {
      int base = 0;
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("TC.RUNIC")) {
         base += stack.func_77978_p().func_74771_c("TC.RUNIC");
      }

      return base;
   }

   public static int getFinalWarp(ItemStack stack, EntityPlayer player) {
      if (stack == null) {
         return 0;
      } else {
         int warp = 0;
         if (stack.func_77973_b() instanceof IWarpingGear) {
            IWarpingGear armor = (IWarpingGear)stack.func_77973_b();
            warp += armor.getWarp(stack, player);
         }

         if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("TC.WARP")) {
            warp += stack.func_77978_p().func_74771_c("TC.WARP");
         }

         return warp;
      }
   }

   public static int getFinalDiscount(ItemStack stack, EntityPlayer player) {
      if (stack != null && stack.func_77973_b() instanceof IVisDiscountGear) {
         IVisDiscountGear gear = (IVisDiscountGear)stack.func_77973_b();
         return gear.getVisDiscount(stack, player);
      } else {
         return 0;
      }
   }

   private void handleSpeedMods(EntityPlayer player) {
      if (player.field_70170_p.field_72995_K && (player.func_70093_af() || player.field_71071_by.field_70460_b[0] == null || player.field_71071_by.field_70460_b[0].func_77973_b() != ItemsTC.travellerBoots) && this.prevStep.containsKey(player.func_145782_y())) {
         player.field_70138_W = (Float)this.prevStep.get(player.func_145782_y());
         this.prevStep.remove(player.func_145782_y());
      }

   }

   @SubscribeEvent
   public void playerJumps(LivingJumpEvent event) {
      if (event.getEntity() instanceof EntityPlayer && ((EntityPlayer)event.getEntity()).field_71071_by.field_70460_b[0] != null && ((EntityPlayer)event.getEntity()).field_71071_by.field_70460_b[0].func_77973_b() == ItemsTC.travellerBoots) {
         ItemStack is = ((EntityPlayer)event.getEntity()).field_71071_by.field_70460_b[0];
         if (RechargeHelper.getCharge(is) > 0) {
            EntityLivingBase var10000 = event.getEntityLiving();
            var10000.field_70181_x += 0.2750000059604645D;
         }
      }

   }

   private void handleWarp(EntityPlayer player) {
      if (!Config.wuss && player.field_70173_aa > 0 && player.field_70173_aa % 2000 == 0 && !player.func_70644_a(PotionWarpWard.instance)) {
         WarpEvents.checkWarpEvent(player);
      }

      if (player.field_70173_aa % 10 == 0 && player.func_70644_a(PotionDeathGaze.instance)) {
         WarpEvents.checkDeathGaze(player);
      }

   }

   @SubscribeEvent
   public void droppedItem(ItemTossEvent event) {
      NBTTagCompound itemData = event.getEntityItem().getEntityData();
      itemData.func_74778_a("thrower", event.getPlayer().func_70005_c_());
   }

   @SubscribeEvent
   public void attachCapabilitiesPlayer(Entity event) {
      if (event.getEntity() instanceof EntityPlayer) {
         event.addCapability(PlayerKnowledge.Provider.NAME, new PlayerKnowledge.Provider());
         event.addCapability(PlayerWarp.Provider.NAME, new PlayerWarp.Provider());
      }

   }

   @SubscribeEvent
   public void playerJoin(EntityJoinWorldEvent event) {
      if (!event.getWorld().field_72995_K && event.getEntity() instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
         ThaumcraftCapabilities.getKnowledge(player).sync(player);
         ThaumcraftCapabilities.getWarp(player).sync(player);
      }

   }

   @SubscribeEvent
   public void cloneCapabilitiesEvent(Clone event) {
      try {
         NBTTagCompound nbtKnowledge = (NBTTagCompound)ThaumcraftCapabilities.getKnowledge(event.getOriginal()).serializeNBT();
         ThaumcraftCapabilities.getKnowledge(event.getEntityPlayer()).deserializeNBT(nbtKnowledge);
         NBTTagCompound nbtWarp = (NBTTagCompound)ThaumcraftCapabilities.getWarp(event.getOriginal()).serializeNBT();
         ThaumcraftCapabilities.getWarp(event.getEntityPlayer()).deserializeNBT(nbtWarp);
      } catch (Exception var4) {
         Thaumcraft.log.error("Could not clone player [" + event.getOriginal().func_70005_c_() + "] knowledge when changing dimensions");
      }

   }
}
