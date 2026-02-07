package thaumcraft.common.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.api.internal.IInternalMethodHandler;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.common.entities.construct.golem.seals.ItemSealPlacer;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXPollute;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.world.aura.AuraHandler;

public class InternalMethodHandler implements IInternalMethodHandler {
   public boolean addKnowledge(EntityPlayer player, IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory field, int amount) {
      return amount != 0 && !player.field_70170_p.field_72995_K ? ResearchManager.addKnowledge(player, type, field, amount) : false;
   }

   public void addWarpToPlayer(EntityPlayer player, int amount, IPlayerWarp.EnumWarpType type) {
      if (amount != 0 && !player.field_70170_p.field_72995_K) {
         IPlayerWarp pw = ThaumcraftCapabilities.getWarp(player);
         int cur = pw.get(type);
         if (amount < 0 && cur + amount < 0) {
            amount = cur;
         }

         pw.add(type, amount);
         pw.sync((EntityPlayerMP)player);
         if (type == IPlayerWarp.EnumWarpType.PERMANENT) {
            PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)0, amount), (EntityPlayerMP)player);
         }

         if (type == IPlayerWarp.EnumWarpType.NORMAL) {
            PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)1, amount), (EntityPlayerMP)player);
         }

         if (type == IPlayerWarp.EnumWarpType.TEMPORARY) {
            PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)2, amount), (EntityPlayerMP)player);
         }

         pw.setCounter(pw.get(IPlayerWarp.EnumWarpType.TEMPORARY) + pw.get(IPlayerWarp.EnumWarpType.PERMANENT) + pw.get(IPlayerWarp.EnumWarpType.NORMAL));
         if (type != IPlayerWarp.EnumWarpType.TEMPORARY && ThaumcraftCapabilities.knowsResearchStrict(player, "FIRSTSTEPS") && !ThaumcraftCapabilities.knowsResearchStrict(player, "WARP")) {
            this.completeResearch(player, "WARP");
            player.func_145747_a(new TextComponentTranslation("research.WARP.warn", new Object[0]));
         }

      }
   }

   public boolean progressResearch(EntityPlayer player, String researchkey) {
      return researchkey != null && !player.field_70170_p.field_72995_K ? ResearchManager.progressResearch(player, researchkey) : false;
   }

   public boolean completeResearch(EntityPlayer player, String researchkey) {
      return researchkey != null && !player.field_70170_p.field_72995_K ? ResearchManager.completeResearch(player, researchkey) : false;
   }

   public boolean doesPlayerHaveRequisites(EntityPlayer player, String researchkey) {
      return ResearchManager.doesPlayerHaveRequisites(player, researchkey);
   }

   public AspectList getObjectAspects(ItemStack is) {
      return ThaumcraftCraftingManager.getObjectTags(is);
   }

   public AspectList generateTags(ItemStack is) {
      return ThaumcraftCraftingManager.generateTags(is);
   }

   public float drainFlux(World world, BlockPos pos, float amount, boolean simulate) {
      return AuraHandler.drainFlux(world, pos, amount, simulate);
   }

   public float getFlux(World world, BlockPos pos) {
      return AuraHandler.getFlux(world, pos);
   }

   public float drainVis(World world, BlockPos pos, float amount, boolean simulate) {
      return AuraHandler.drainVis(world, pos, amount, simulate);
   }

   public void addVis(World world, BlockPos pos, float amount) {
      AuraHandler.addVis(world, pos, amount);
   }

   public float getTotalAura(World world, BlockPos pos) {
      return AuraHandler.getTotalAura(world, pos);
   }

   public float getVis(World world, BlockPos pos) {
      return AuraHandler.getVis(world, pos);
   }

   public int getAuraBase(World world, BlockPos pos) {
      return AuraHandler.getAuraBase(world, pos);
   }

   public void addFlux(World world, BlockPos pos, float amount, boolean showEffect) {
      if (!world.field_72995_K) {
         AuraHandler.addFlux(world, pos, amount);
         if (showEffect && amount > 0.0F) {
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXPollute(pos, amount), new TargetPoint(world.field_73011_w.getDimension(), (double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), 32.0D));
         }

      }
   }

   public void registerSeal(ISeal seal) {
      SealHandler.registerSeal(seal);
   }

   public ISeal getSeal(String key) {
      return SealHandler.getSeal(key);
   }

   public ISealEntity getSealEntity(int dim, SealPos pos) {
      return SealHandler.getSealEntity(dim, pos);
   }

   public void addGolemTask(int dim, Task task) {
      TaskHandler.addTask(dim, task);
   }

   public boolean shouldPreserveAura(World world, EntityPlayer player, BlockPos pos) {
      return AuraHandler.shouldPreserveAura(world, player, pos);
   }

   public ItemStack getSealStack(String key) {
      return ItemSealPlacer.getSealStack(key);
   }
}
