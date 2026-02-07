package thaumcraft.api.internal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.api.research.ResearchCategory;

public class DummyInternalMethodHandler implements IInternalMethodHandler {
   public boolean completeResearch(EntityPlayer player, String researchkey) {
      return false;
   }

   public void addWarpToPlayer(EntityPlayer player, int amount, IPlayerWarp.EnumWarpType type) {
   }

   public AspectList getObjectAspects(ItemStack is) {
      return null;
   }

   public AspectList generateTags(ItemStack is) {
      return null;
   }

   public float drainVis(World world, BlockPos pos, float amount, boolean simulate) {
      return 0.0F;
   }

   public float drainFlux(World world, BlockPos pos, float amount, boolean simulate) {
      return 0.0F;
   }

   public void addVis(World world, BlockPos pos, float amount) {
   }

   public void addFlux(World world, BlockPos pos, float amount, boolean showEffect) {
   }

   public float getTotalAura(World world, BlockPos pos) {
      return 0.0F;
   }

   public float getVis(World world, BlockPos pos) {
      return 0.0F;
   }

   public float getFlux(World world, BlockPos pos) {
      return 0.0F;
   }

   public int getAuraBase(World world, BlockPos pos) {
      return 0;
   }

   public void registerSeal(ISeal seal) {
   }

   public ISeal getSeal(String key) {
      return null;
   }

   public ISealEntity getSealEntity(int dim, SealPos pos) {
      return null;
   }

   public void addGolemTask(int dim, Task task) {
   }

   public boolean shouldPreserveAura(World world, EntityPlayer player, BlockPos pos) {
      return false;
   }

   public ItemStack getSealStack(String key) {
      return null;
   }

   public boolean doesPlayerHaveRequisites(EntityPlayer player, String researchkey) {
      return false;
   }

   public boolean addKnowledge(EntityPlayer player, IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory field, int amount) {
      return false;
   }

   public boolean progressResearch(EntityPlayer player, String researchkey) {
      return false;
   }
}
