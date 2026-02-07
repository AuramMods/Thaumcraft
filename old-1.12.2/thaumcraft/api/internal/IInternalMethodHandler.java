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

public interface IInternalMethodHandler {
   boolean addKnowledge(EntityPlayer var1, IPlayerKnowledge.EnumKnowledgeType var2, ResearchCategory var3, int var4);

   boolean progressResearch(EntityPlayer var1, String var2);

   boolean completeResearch(EntityPlayer var1, String var2);

   boolean doesPlayerHaveRequisites(EntityPlayer var1, String var2);

   void addWarpToPlayer(EntityPlayer var1, int var2, IPlayerWarp.EnumWarpType var3);

   AspectList getObjectAspects(ItemStack var1);

   AspectList generateTags(ItemStack var1);

   float drainVis(World var1, BlockPos var2, float var3, boolean var4);

   float drainFlux(World var1, BlockPos var2, float var3, boolean var4);

   void addVis(World var1, BlockPos var2, float var3);

   void addFlux(World var1, BlockPos var2, float var3, boolean var4);

   float getTotalAura(World var1, BlockPos var2);

   float getVis(World var1, BlockPos var2);

   float getFlux(World var1, BlockPos var2);

   int getAuraBase(World var1, BlockPos var2);

   void registerSeal(ISeal var1);

   ISeal getSeal(String var1);

   ISealEntity getSealEntity(int var1, SealPos var2);

   void addGolemTask(int var1, Task var2);

   boolean shouldPreserveAura(World var1, EntityPlayer var2, BlockPos var3);

   ItemStack getSealStack(String var1);
}
