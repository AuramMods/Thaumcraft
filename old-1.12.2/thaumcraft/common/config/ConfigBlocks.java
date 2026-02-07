package thaumcraft.common.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.IBlockTypes;
import thaumcraft.common.blocks.ItemBlockTC;
import thaumcraft.common.blocks.basic.BlockBannerTC;
import thaumcraft.common.blocks.basic.BlockBannerTCItem;
import thaumcraft.common.blocks.basic.BlockCandle;
import thaumcraft.common.blocks.basic.BlockMetalTC;
import thaumcraft.common.blocks.basic.BlockPavingStone;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.blocks.basic.BlockPlanksTC;
import thaumcraft.common.blocks.basic.BlockStairsTC;
import thaumcraft.common.blocks.basic.BlockStoneSlabTC;
import thaumcraft.common.blocks.basic.BlockStoneSlabTCItem;
import thaumcraft.common.blocks.basic.BlockStoneTC;
import thaumcraft.common.blocks.basic.BlockTable;
import thaumcraft.common.blocks.basic.BlockTranslucent;
import thaumcraft.common.blocks.basic.BlockWoodSlabTC;
import thaumcraft.common.blocks.basic.BlockWoodSlabTCItem;
import thaumcraft.common.blocks.crafting.BlockArcaneWorkbench;
import thaumcraft.common.blocks.crafting.BlockArcaneWorkbenchCharger;
import thaumcraft.common.blocks.crafting.BlockCrucible;
import thaumcraft.common.blocks.crafting.BlockFocalManipulator;
import thaumcraft.common.blocks.crafting.BlockGolemBuilder;
import thaumcraft.common.blocks.crafting.BlockInfusionMatrix;
import thaumcraft.common.blocks.crafting.BlockPatternCrafter;
import thaumcraft.common.blocks.crafting.BlockResearchTable;
import thaumcraft.common.blocks.crafting.BlockThaumatorium;
import thaumcraft.common.blocks.devices.BlockArcaneEar;
import thaumcraft.common.blocks.devices.BlockBellows;
import thaumcraft.common.blocks.devices.BlockBrainBox;
import thaumcraft.common.blocks.devices.BlockDioptra;
import thaumcraft.common.blocks.devices.BlockHungryChest;
import thaumcraft.common.blocks.devices.BlockInfernalFurnace;
import thaumcraft.common.blocks.devices.BlockLamp;
import thaumcraft.common.blocks.devices.BlockLevitator;
import thaumcraft.common.blocks.devices.BlockMirror;
import thaumcraft.common.blocks.devices.BlockMirrorItem;
import thaumcraft.common.blocks.devices.BlockPedestal;
import thaumcraft.common.blocks.devices.BlockRechargePedestal;
import thaumcraft.common.blocks.devices.BlockRedstoneRelay;
import thaumcraft.common.blocks.devices.BlockSpa;
import thaumcraft.common.blocks.devices.BlockWaterJug;
import thaumcraft.common.blocks.essentia.BlockAlembic;
import thaumcraft.common.blocks.essentia.BlockCentrifuge;
import thaumcraft.common.blocks.essentia.BlockEssentiaTransport;
import thaumcraft.common.blocks.essentia.BlockJar;
import thaumcraft.common.blocks.essentia.BlockJarItem;
import thaumcraft.common.blocks.essentia.BlockSmelter;
import thaumcraft.common.blocks.essentia.BlockSmelterAux;
import thaumcraft.common.blocks.essentia.BlockSmelterVent;
import thaumcraft.common.blocks.essentia.BlockTube;
import thaumcraft.common.blocks.misc.BlockBarrier;
import thaumcraft.common.blocks.misc.BlockEffect;
import thaumcraft.common.blocks.misc.BlockFluidDeath;
import thaumcraft.common.blocks.misc.BlockFluidPure;
import thaumcraft.common.blocks.misc.BlockHole;
import thaumcraft.common.blocks.misc.BlockNitor;
import thaumcraft.common.blocks.misc.BlockPlaceholder;
import thaumcraft.common.blocks.world.BlockGrassAmbient;
import thaumcraft.common.blocks.world.BlockLoot;
import thaumcraft.common.blocks.world.ore.BlockCrystal;
import thaumcraft.common.blocks.world.ore.BlockCrystalItem;
import thaumcraft.common.blocks.world.ore.BlockOreTC;
import thaumcraft.common.blocks.world.ore.ShardType;
import thaumcraft.common.blocks.world.plants.BlockLeavesTC;
import thaumcraft.common.blocks.world.plants.BlockLeavesTCItem;
import thaumcraft.common.blocks.world.plants.BlockLogsTC;
import thaumcraft.common.blocks.world.plants.BlockPlantCinderpearl;
import thaumcraft.common.blocks.world.plants.BlockPlantShimmerleaf;
import thaumcraft.common.blocks.world.plants.BlockPlantVishroom;
import thaumcraft.common.blocks.world.plants.BlockSaplingTC;
import thaumcraft.common.blocks.world.plants.BlockSaplingTCItem;
import thaumcraft.common.blocks.world.taint.BlockFluxGoo;
import thaumcraft.common.blocks.world.taint.BlockTaint;
import thaumcraft.common.blocks.world.taint.BlockTaintFeature;
import thaumcraft.common.blocks.world.taint.BlockTaintFibre;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;
import thaumcraft.common.tiles.crafting.TileCrucible;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;
import thaumcraft.common.tiles.crafting.TileGolemBuilder;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;
import thaumcraft.common.tiles.crafting.TilePatternCrafter;
import thaumcraft.common.tiles.crafting.TilePedestal;
import thaumcraft.common.tiles.crafting.TileResearchTable;
import thaumcraft.common.tiles.crafting.TileThaumatorium;
import thaumcraft.common.tiles.crafting.TileThaumatoriumTop;
import thaumcraft.common.tiles.devices.TileArcaneEar;
import thaumcraft.common.tiles.devices.TileBellows;
import thaumcraft.common.tiles.devices.TileDioptra;
import thaumcraft.common.tiles.devices.TileHungryChest;
import thaumcraft.common.tiles.devices.TileInfernalFurnace;
import thaumcraft.common.tiles.devices.TileJarBrain;
import thaumcraft.common.tiles.devices.TileLampArcane;
import thaumcraft.common.tiles.devices.TileLampFertility;
import thaumcraft.common.tiles.devices.TileLampGrowth;
import thaumcraft.common.tiles.devices.TileLevitator;
import thaumcraft.common.tiles.devices.TileMirror;
import thaumcraft.common.tiles.devices.TileMirrorEssentia;
import thaumcraft.common.tiles.devices.TileRechargePedestal;
import thaumcraft.common.tiles.devices.TileRedstoneRelay;
import thaumcraft.common.tiles.devices.TileSpa;
import thaumcraft.common.tiles.devices.TileWaterJug;
import thaumcraft.common.tiles.essentia.TileAlembic;
import thaumcraft.common.tiles.essentia.TileCentrifuge;
import thaumcraft.common.tiles.essentia.TileEssentiaInput;
import thaumcraft.common.tiles.essentia.TileEssentiaOutput;
import thaumcraft.common.tiles.essentia.TileJarFillable;
import thaumcraft.common.tiles.essentia.TileJarFillableVoid;
import thaumcraft.common.tiles.essentia.TileSmelter;
import thaumcraft.common.tiles.essentia.TileTube;
import thaumcraft.common.tiles.essentia.TileTubeBuffer;
import thaumcraft.common.tiles.essentia.TileTubeFilter;
import thaumcraft.common.tiles.essentia.TileTubeOneway;
import thaumcraft.common.tiles.essentia.TileTubeRestrict;
import thaumcraft.common.tiles.essentia.TileTubeValve;
import thaumcraft.common.tiles.misc.TileBanner;
import thaumcraft.common.tiles.misc.TileBarrierStone;
import thaumcraft.common.tiles.misc.TileHole;
import thaumcraft.common.tiles.misc.TileNitor;

public class ConfigBlocks {
   static ArrayList<Block> blocks = new ArrayList();

   public static void preInit() {
      initializeBlocks();
      registerTileEntities();
      BlocksTC.oreAmber.setHarvestLevel("pickaxe", 1);
      BlocksTC.oreCinnabar.setHarvestLevel("pickaxe", 2);
      BlockUtils.portableHoleBlackList.add("minecraft:bed");
      BlockUtils.portableHoleBlackList.add("minecraft:piston");
      BlockUtils.portableHoleBlackList.add("minecraft:piston_head");
      BlockUtils.portableHoleBlackList.add("minecraft:sticky_piston");
      BlockUtils.portableHoleBlackList.add("minecraft:piston_extension");
      BlockUtils.portableHoleBlackList.add("minecraft:wooden_door");
      BlockUtils.portableHoleBlackList.add("minecraft:spruce_door");
      BlockUtils.portableHoleBlackList.add("minecraft:birch_door");
      BlockUtils.portableHoleBlackList.add("minecraft:jungle_door");
      BlockUtils.portableHoleBlackList.add("minecraft:acacia_door");
      BlockUtils.portableHoleBlackList.add("minecraft:dark_oak_door");
      BlockUtils.portableHoleBlackList.add("minecraft:iron_door");
      BlockUtils.portableHoleBlackList.add("thaumcraft:infernal_furnace");
   }

   private static void initializeBlocks() {
      BlocksTC.oreAmber = initBlock(new BlockOreTC(), "ore_amber").func_149711_c(1.5F);
      BlocksTC.oreCinnabar = initBlock(new BlockOreTC(), "ore_cinnabar").func_149711_c(2.0F);
      BlocksTC.oreQuartz = initBlock(new BlockOreTC(), "ore_quartz").func_149711_c(3.0F);
      BlocksTC.crystalAir = initBlock(new BlockCrystal(Aspect.AIR), "crystal_aer", BlockCrystalItem.class);
      BlocksTC.crystalFire = initBlock(new BlockCrystal(Aspect.FIRE), "crystal_ignis", BlockCrystalItem.class);
      BlocksTC.crystalWater = initBlock(new BlockCrystal(Aspect.WATER), "crystal_aqua", BlockCrystalItem.class);
      BlocksTC.crystalEarth = initBlock(new BlockCrystal(Aspect.EARTH), "crystal_terra", BlockCrystalItem.class);
      BlocksTC.crystalOrder = initBlock(new BlockCrystal(Aspect.ORDER), "crystal_ordo", BlockCrystalItem.class);
      BlocksTC.crystalEntropy = initBlock(new BlockCrystal(Aspect.ENTROPY), "crystal_perditio", BlockCrystalItem.class);
      BlocksTC.crystalTaint = initBlock(new BlockCrystal(Aspect.FLUX), "crystal_vitium", BlockCrystalItem.class);
      ShardType.AIR.setOre(BlocksTC.crystalAir);
      ShardType.FIRE.setOre(BlocksTC.crystalFire);
      ShardType.WATER.setOre(BlocksTC.crystalWater);
      ShardType.EARTH.setOre(BlocksTC.crystalEarth);
      ShardType.ORDER.setOre(BlocksTC.crystalOrder);
      ShardType.ENTROPY.setOre(BlocksTC.crystalEntropy);
      ShardType.FLUX.setOre(BlocksTC.crystalTaint);
      BlocksTC.stone = initBlock(new BlockStoneTC(), "stone");
      BlocksTC.stairsArcane = initBlock(new BlockStairsTC(BlocksTC.stone.func_176203_a(0)), "arcane_stairs", ItemBlock.class);
      BlocksTC.stairsArcaneBrick = initBlock(new BlockStairsTC(BlocksTC.stone.func_176203_a(1)), "arcane_brick_stairs", ItemBlock.class);
      BlocksTC.stairsAncient = initBlock(new BlockStairsTC(BlocksTC.stone.func_176203_a(2)), "ancient_stairs", ItemBlock.class);
      BlocksTC.slabStone = (new BlockStoneSlabTC()).func_149711_c(2.0F).func_149752_b(10.0F);
      BlocksTC.doubleSlabStone = (new BlockStoneSlabTC()).func_149711_c(2.0F).func_149752_b(10.0F);
      BlocksTC.slabStone = initBlock(BlocksTC.slabStone, "slab_stone", BlockStoneSlabTCItem.class);
      BlocksTC.doubleSlabStone = initBlock(BlocksTC.doubleSlabStone, "slab_stone_double", (Class)null);
      BlocksTC.sapling = initBlock(new BlockSaplingTC(), "sapling", BlockSaplingTCItem.class);
      BlocksTC.log = initBlock(new BlockLogsTC(), "log");
      BlocksTC.leaf = initBlock(new BlockLeavesTC(), "leaf", BlockLeavesTCItem.class);
      BlocksTC.shimmerleaf = initBlock(new BlockPlantShimmerleaf(), "shimmerleaf", ItemBlock.class);
      BlocksTC.cinderpearl = initBlock(new BlockPlantCinderpearl(), "cinderpearl", ItemBlock.class);
      BlocksTC.vishroom = initBlock(new BlockPlantVishroom(), "vishroom", ItemBlock.class);
      BlocksTC.plank = initBlock(new BlockPlanksTC(), "plank");
      BlocksTC.stairsGreatwood = initBlock(new BlockStairsTC(BlocksTC.plank.func_176203_a(0)), "greatwood_stairs", ItemBlock.class);
      BlocksTC.stairsSilverwood = initBlock(new BlockStairsTC(BlocksTC.plank.func_176203_a(1)), "silverwood_stairs", ItemBlock.class);
      BlocksTC.slabWood = (new BlockWoodSlabTC()).func_149711_c(2.0F).func_149752_b(5.0F);
      BlocksTC.doubleSlabWood = (new BlockWoodSlabTC()).func_149711_c(2.0F).func_149752_b(5.0F);
      BlocksTC.slabWood = initBlock(BlocksTC.slabWood, "slab_wood", BlockWoodSlabTCItem.class);
      BlocksTC.doubleSlabWood = initBlock(BlocksTC.doubleSlabWood, "slab_wood_double", (Class)null);
      BlocksTC.translucent = initBlock(new BlockTranslucent(), "translucent");
      BlocksTC.fleshBlock = initBlock(new BlockTC(Material.field_151583_m, SoundsTC.GORE), "flesh_block").func_149711_c(0.25F);
      BlocksTC.lootCrate = initBlock(new BlockLoot(Material.field_151575_d, SoundType.field_185848_a), "loot_crate");
      BlocksTC.lootUrn = initBlock(new BlockLoot(Material.field_151576_e, SoundsTC.URN), "loot_urn");
      BlocksTC.taintFibre = initBlock(new BlockTaintFibre(), "taint_fibre", BlockCrystalItem.class);
      BlocksTC.taintBlock = initBlock(new BlockTaint(), "taint");
      BlocksTC.taintFeature = initBlock(new BlockTaintFeature(), "taint_feature");
      BlocksTC.taintLog = initBlock(new BlockTaintLog(), "taint_log");
      BlocksTC.grassAmbient = initBlock(new BlockGrassAmbient(), "grass_ambient", ItemBlock.class);
      BlocksTC.tableWood = initBlock(new BlockTable(Material.field_151575_d, SoundType.field_185848_a), "table_wood").func_149711_c(2.0F);
      BlocksTC.tableStone = initBlock(new BlockTable(Material.field_151576_e, SoundType.field_185851_d), "table_stone").func_149711_c(2.5F);
      BlocksTC.candle = initBlock(new BlockCandle(), "candle");
      BlocksTC.nitor = initBlock(new BlockNitor(), "nitor");
      BlocksTC.pedestal = initBlock(new BlockPedestal(), "pedestal");
      BlocksTC.metal = initBlock(new BlockMetalTC(), "metal");
      BlocksTC.pavingStone = initBlock(new BlockPavingStone(), "paving_stone");
      BlocksTC.barrier = initBlock(new BlockBarrier(), "barrier", ItemBlock.class);
      BlocksTC.pillar = initBlock(new BlockPillar(), "pillar");
      BlocksTC.banner = initBlock(new BlockBannerTC(), "banner", BlockBannerTCItem.class);
      BlocksTC.arcaneWorkbench = initBlock(new BlockArcaneWorkbench(), "arcane_workbench");
      BlocksTC.arcaneWorkbenchCharger = initBlock(new BlockArcaneWorkbenchCharger(), "arcane_workbench_charger");
      BlocksTC.dioptra = initBlock(new BlockDioptra(), "dioptra");
      BlocksTC.researchTable = initBlock(new BlockResearchTable(), "research_table");
      BlocksTC.crucible = initBlock(new BlockCrucible(), "crucible");
      BlocksTC.arcaneEar = initBlock(new BlockArcaneEar(), "arcane_ear");
      BlocksTC.lampArcane = initBlock(new BlockLamp(TileLampArcane.class), "lamp_arcane");
      BlocksTC.lampFertility = initBlock(new BlockLamp(TileLampFertility.class), "lamp_fertility");
      BlocksTC.lampGrowth = initBlock(new BlockLamp(TileLampGrowth.class), "lamp_growth");
      BlocksTC.levitator = initBlock(new BlockLevitator(), "levitator");
      BlocksTC.centrifuge = initBlock(new BlockCentrifuge(), "centrifuge");
      BlocksTC.bellows = initBlock(new BlockBellows(), "bellows");
      BlocksTC.smelterBasic = initBlock(new BlockSmelter(), "smelter_basic");
      BlocksTC.smelterThaumium = initBlock(new BlockSmelter(), "smelter_thaumium");
      BlocksTC.smelterVoid = initBlock(new BlockSmelter(), "smelter_void");
      BlocksTC.smelterAux = initBlock(new BlockSmelterAux(), "smelter_aux");
      BlocksTC.smelterVent = initBlock(new BlockSmelterVent(), "smelter_vent");
      BlocksTC.alembic = initBlock(new BlockAlembic(), "alembic");
      BlocksTC.rechargePedestal = initBlock(new BlockRechargePedestal(), "recharge_pedestal");
      BlocksTC.wandWorkbench = initBlock(new BlockFocalManipulator(), "wand_workbench");
      BlocksTC.hungryChest = new BlockHungryChest();
      BlocksTC.hungryChest = initBlock(BlocksTC.hungryChest, "hungry_chest", ItemBlock.class);
      BlocksTC.tube = initBlock(new BlockTube(TileTube.class), "tube");
      BlocksTC.tubeValve = initBlock(new BlockTube(TileTubeValve.class), "tube_valve");
      BlocksTC.tubeRestrict = initBlock(new BlockTube(TileTubeRestrict.class), "tube_restrict");
      BlocksTC.tubeOneway = initBlock(new BlockTube(TileTubeOneway.class), "tube_oneway");
      BlocksTC.tubeFilter = initBlock(new BlockTube(TileTubeFilter.class), "tube_filter");
      BlocksTC.tubeBuffer = initBlock(new BlockTube(TileTubeBuffer.class), "tube_buffer");
      BlocksTC.jarNormal = initBlock(new BlockJar(TileJarFillable.class), "jar_normal", BlockJarItem.class);
      BlocksTC.jarVoid = initBlock(new BlockJar(TileJarFillableVoid.class), "jar_void", BlockJarItem.class);
      BlocksTC.jarBrain = initBlock(new BlockJar(TileJarBrain.class), "jar_brain", BlockJarItem.class);
      BlocksTC.infusionMatrix = initBlock(new BlockInfusionMatrix(), "infusion_matrix");
      BlocksTC.infernalFurnace = initBlock(new BlockInfernalFurnace(), "infernal_furnace");
      BlocksTC.everfullUrn = initBlock(new BlockWaterJug(), "everfull_urn");
      BlocksTC.thaumatorium = initBlock(new BlockThaumatorium(false), "thaumatorium");
      BlocksTC.thaumatoriumTop = initBlock(new BlockThaumatorium(true), "thaumatorium_top");
      BlocksTC.brainBox = initBlock(new BlockBrainBox(), "brain_box");
      BlocksTC.spa = initBlock(new BlockSpa(), "spa");
      BlocksTC.golemBuilder = initBlock(new BlockGolemBuilder(), "golem_builder");
      BlocksTC.mirror = initBlock(new BlockMirror(TileMirror.class), "mirror", BlockMirrorItem.class);
      BlocksTC.mirrorEssentia = initBlock(new BlockMirror(TileMirrorEssentia.class), "mirror_essentia", BlockMirrorItem.class);
      BlocksTC.essentiaTransportInput = initBlock(new BlockEssentiaTransport(TileEssentiaInput.class), "essentia_input");
      BlocksTC.essentiaTransportOutput = initBlock(new BlockEssentiaTransport(TileEssentiaOutput.class), "essentia_output");
      BlocksTC.redstoneRelay = initBlock(new BlockRedstoneRelay(), "redstone_relay");
      BlocksTC.patternCrafter = initBlock(new BlockPatternCrafter(), "pattern_crafter");
      BlocksTC.activatorRail = initBlock((new BlockRailPowered()).func_149711_c(0.7F).func_149647_a(ConfigItems.TABTC).func_149663_c("activator_rail"), "activator_rail", ItemBlock.class);
      FluidRegistry.registerFluid(ConfigBlocks.FluidFluxGoo.instance);
      BlocksTC.fluxGoo = new BlockFluxGoo();
      GameRegistry.registerBlock(BlocksTC.fluxGoo, "flux_goo");
      FluidRegistry.registerFluid(ConfigBlocks.FluidDeath.instance);
      BlocksTC.liquidDeath = new BlockFluidDeath();
      GameRegistry.registerBlock(BlocksTC.liquidDeath, "liquid_death");
      FluidRegistry.registerFluid(ConfigBlocks.FluidPure.instance);
      BlocksTC.purifyingFluid = new BlockFluidPure();
      GameRegistry.registerBlock(BlocksTC.purifyingFluid, "purifying_fluid");
      BlocksTC.hole = initBlock(new BlockHole(), "hole", ItemBlock.class);
      BlocksTC.effect = initBlock(new BlockEffect(), "effect");
      BlocksTC.placeholder = initBlock(new BlockPlaceholder(), "placeholder");
   }

   private static void registerTileEntities() {
      GameRegistry.registerTileEntity(TileArcaneWorkbench.class, "thaumcraft:TileArcaneWorkbench");
      GameRegistry.registerTileEntity(TileDioptra.class, "thaumcraft:TileDioptra");
      GameRegistry.registerTileEntity(TileArcaneEar.class, "thaumcraft:TileArcaneEar");
      GameRegistry.registerTileEntity(TileLevitator.class, "thaumcraft:TileLevitator");
      GameRegistry.registerTileEntity(TileCrucible.class, "thaumcraft:TileCrucible");
      GameRegistry.registerTileEntity(TileNitor.class, "thaumcraft:TileNitor");
      GameRegistry.registerTileEntity(TileFocalManipulator.class, "thaumcraft:TileFocalManipulator");
      GameRegistry.registerTileEntity(TilePedestal.class, "thaumcraft:TilePedestal");
      GameRegistry.registerTileEntity(TileRechargePedestal.class, "thaumcraft:TileRechargePedestal");
      GameRegistry.registerTileEntity(TileResearchTable.class, "thaumcraft:TileResearchTable");
      GameRegistry.registerTileEntity(TileTube.class, "thaumcraft:TileTube");
      GameRegistry.registerTileEntity(TileTubeValve.class, "thaumcraft:TileTubeValve");
      GameRegistry.registerTileEntity(TileTubeFilter.class, "thaumcraft:TileTubeFilter");
      GameRegistry.registerTileEntity(TileTubeRestrict.class, "thaumcraft:TileTubeRestrict");
      GameRegistry.registerTileEntity(TileTubeOneway.class, "thaumcraft:TileTubeOneway");
      GameRegistry.registerTileEntity(TileTubeBuffer.class, "thaumcraft:TileTubeBuffer");
      GameRegistry.registerTileEntity(TileHungryChest.class, "thaumcraft:TileChestHungry");
      GameRegistry.registerTileEntity(TileCentrifuge.class, "thaumcraft:TileCentrifuge");
      GameRegistry.registerTileEntity(TileJarFillable.class, "thaumcraft:TileJar");
      GameRegistry.registerTileEntity(TileJarFillableVoid.class, "thaumcraft:TileJarVoid");
      GameRegistry.registerTileEntity(TileJarBrain.class, "thaumcraft:TileJarBrain");
      GameRegistry.registerTileEntity(TileBellows.class, "thaumcraft:TileBellows");
      GameRegistry.registerTileEntity(TileSmelter.class, "thaumcraft:TileSmelter");
      GameRegistry.registerTileEntity(TileAlembic.class, "thaumcraft:TileAlembic");
      GameRegistry.registerTileEntity(TileInfusionMatrix.class, "thaumcraft:TileInfusionMatrix");
      GameRegistry.registerTileEntity(TileWaterJug.class, "thaumcraft:TileWaterJug");
      GameRegistry.registerTileEntity(TileInfernalFurnace.class, "thaumcraft:TileInfernalFurnace");
      GameRegistry.registerTileEntity(TileThaumatorium.class, "thaumcraft:TileThaumatorium");
      GameRegistry.registerTileEntity(TileThaumatoriumTop.class, "thaumcraft:TileThaumatoriumTop");
      GameRegistry.registerTileEntity(TileSpa.class, "thaumcraft:TileSpa");
      GameRegistry.registerTileEntity(TileLampGrowth.class, "thaumcraft:TileLampGrowth");
      GameRegistry.registerTileEntity(TileLampArcane.class, "thaumcraft:TileLampArcane");
      GameRegistry.registerTileEntity(TileLampFertility.class, "thaumcraft:TileLampFertility");
      GameRegistry.registerTileEntity(TileMirror.class, "thaumcraft:TileMirror");
      GameRegistry.registerTileEntity(TileMirrorEssentia.class, "thaumcraft:TileMirrorEssentia");
      GameRegistry.registerTileEntity(TileRedstoneRelay.class, "thaumcraft:TileRedstoneRelay");
      GameRegistry.registerTileEntity(TileGolemBuilder.class, "thaumcraft:TileGolemBuilder");
      GameRegistry.registerTileEntity(TileEssentiaInput.class, "thaumcraft:TileEssentiaInput");
      GameRegistry.registerTileEntity(TileEssentiaOutput.class, "thaumcraft:TileEssentiaOutput");
      GameRegistry.registerTileEntity(TilePatternCrafter.class, "thaumcraft:TilePatternCrafter");
      GameRegistry.registerTileEntity(TileBanner.class, "thaumcraft:TileBanner");
      GameRegistry.registerTileEntity(TileHole.class, "thaumcraft:TileHole");
      GameRegistry.registerTileEntity(TileBarrierStone.class, "thaumcraft:TileBarrierStone");
   }

   public static void init() {
   }

   public static void initModelsAndVariants() {
      Iterator var0 = blocks.iterator();

      while(true) {
         while(var0.hasNext()) {
            Block block = (Block)var0.next();
            Item blockItem = Item.func_150898_a(block);
            ResourceLocation loc = GameData.getBlockRegistry().getNameForObject(block);
            String location = loc.toString();
            if (block instanceof IBlockTypes && ((IBlockTypes)block).hasTypes()) {
               BlockStateContainer bsc = block.func_176194_O();
               IBlockState state = bsc.func_177621_b();
               IProperty[] var7 = ((IBlockTypes)block).getTypes();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  IProperty prop = var7[var9];

                  for(Iterator var11 = prop.func_177700_c().iterator(); var11.hasNext(); state = state.func_177231_a(prop)) {
                     Object value = var11.next();
                     int meta = block.func_180651_a(state);
                     String stateName = ((IBlockTypes)block).getTypeName(state);
                     String type = "type=" + stateName.toLowerCase(Locale.US);
                     ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, type));
                  }
               }
            } else {
               ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
            }
         }

         return;
      }
   }

   private static Block initBlock(BlockTC block, String name) {
      return initBlock(block, name, (Class)null, false);
   }

   private static Block initBlock(Block block, String name, Class ib) {
      return initBlock(block, name, ib, false);
   }

   private static Block initBlock(Block block, String name, Class ib, boolean noReg) {
      if (!noReg) {
         blocks.add(block);
      }

      block.func_149663_c(name);
      if (ib != null) {
         registerBlock(block, ib, name);
      } else if (block instanceof BlockTC && ((BlockTC)block).hasTypes()) {
         registerBlock(block, ItemBlockTC.class, name);
      } else {
         registerBlock(block, ItemBlock.class, name);
      }

      return block;
   }

   public static Block registerBlock(Block block, ItemBlock itemBlock, String name) {
      block = (Block)register(block, name);
      register(itemBlock, name);
      return block;
   }

   public static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, String name) {
      try {
         return registerBlock(block, (ItemBlock)itemBlock.getConstructor(Block.class).newInstance(block), name);
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static <T extends IForgeRegistryEntry<?>> T register(T object, String name) {
      object.setRegistryName(new ResourceLocation("thaumcraft", name));
      return GameRegistry.register(object);
   }

   public static final class FluidTaintDust extends Fluid {
      public static final String name = "taint_dust";
      public static final ConfigBlocks.FluidTaintDust instance = new ConfigBlocks.FluidTaintDust();

      private FluidTaintDust() {
         super("taint_dust", new ResourceLocation("thaumcraft:blocks/taint_dust"), new ResourceLocation("thaumcraft:blocks/taint_dust"));
         this.setViscosity(8000);
         this.setDensity(2000);
      }
   }

   public static final class FluidFluxGoo extends Fluid {
      public static final String name = "flux_goo";
      public static final ConfigBlocks.FluidFluxGoo instance = new ConfigBlocks.FluidFluxGoo();

      private FluidFluxGoo() {
         super("flux_goo", new ResourceLocation("thaumcraft:blocks/flux_goo"), new ResourceLocation("thaumcraft:blocks/flux_goo"));
         this.setViscosity(6000);
         this.setDensity(8);
      }
   }

   public static final class FluidDeath extends Fluid {
      public static final String name = "liquid_death";
      public static final ConfigBlocks.FluidDeath instance = new ConfigBlocks.FluidDeath();

      private FluidDeath() {
         super("liquid_death", new ResourceLocation("thaumcraft:blocks/animatedglow"), new ResourceLocation("thaumcraft:blocks/animatedglow"));
         this.setViscosity(1500);
         this.setRarity(EnumRarity.RARE);
      }

      public int getColor() {
         return -263978855;
      }
   }

   public static final class FluidPure extends Fluid {
      public static final String name = "purifying_fluid";
      public static final ConfigBlocks.FluidPure instance = new ConfigBlocks.FluidPure();

      private FluidPure() {
         super("purifying_fluid", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"));
         this.setLuminosity(5);
         this.setRarity(EnumRarity.RARE);
      }

      public int getColor() {
         return 2013252778;
      }
   }
}
