package thaumcraft.common.config;

import java.util.Iterator;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.RecipeSorter.Category;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.Part;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;
import thaumcraft.common.lib.crafting.DustTriggerSimple;
import thaumcraft.common.lib.crafting.InfusionEnchantmentRecipe;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;
import thaumcraft.common.lib.crafting.RecipeMagicDust;
import thaumcraft.common.lib.crafting.RecipeTripleMeatTreat;
import thaumcraft.common.lib.crafting.RecipesRobeArmorDyes;
import thaumcraft.common.lib.crafting.RecipesVoidRobeArmorDyes;
import thaumcraft.common.lib.crafting.ShapelessNBTOreRecipe;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class ConfigRecipes {
   public static void postInit() {
      initializeSmelting();
      initializeNormalRecipes();
      initializeArcaneRecipes();
      initializeInfusionRecipes();
      initializeAlchemyRecipes();
      initializeCompoundRecipes();
      RecipeSorter.register("forge:shapelessorenbt", ShapelessNBTOreRecipe.class, Category.SHAPELESS, "after:forge:shapelessore");
      RecipeSorter.register("forge:robearmordye", RecipesRobeArmorDyes.class, Category.SHAPELESS, "after:forge:shapelessorenbt");
      RecipeSorter.register("forge:shapelessarcanerecipe", ShapelessArcaneRecipe.class, Category.SHAPELESS, "after:forge:shapelessorenbt");
      RecipeSorter.register("forge:voidrobearmordye", RecipesVoidRobeArmorDyes.class, Category.SHAPELESS, "after:forge:robearmordye");
      RecipeSorter.register("forge:triplemeattreat", RecipeTripleMeatTreat.class, Category.SHAPELESS, "after:forge:voidrobearmordye");
      RecipeSorter.register("forge:salismundus", RecipeMagicDust.class, Category.SHAPELESS, "after:forge:triplemeattreat");
      RecipeSorter.register("forge:arcanerecipe", ShapedArcaneRecipe.class, Category.SHAPED, "after:minecraft:shaped");
   }

   private static void initializeCompoundRecipes() {
      IDustTrigger.registerDustTrigger(new DustTriggerSimple("!gotdream", Blocks.field_150342_X, new ItemStack(ItemsTC.thaumonomicon)));
      IDustTrigger.registerDustTrigger(new DustTriggerSimple("FIRSTSTEPS@1", Blocks.field_150462_ai, new ItemStack(BlocksTC.arcaneWorkbench)));
      IDustTrigger.registerDustTrigger(new DustTriggerSimple("UNLOCKALCHEMY@1", Blocks.field_150383_bp, new ItemStack(BlocksTC.crucible)));
      Part NB = new Part(Blocks.field_150385_bj, new ItemStack(BlocksTC.placeholder, 1, 0));
      Part OB = new Part(Blocks.field_150343_Z, new ItemStack(BlocksTC.placeholder, 1, 1));
      Part IB = new Part(Blocks.field_150411_aY, "AIR");
      Part LA = new Part(Material.field_151587_i, BlocksTC.infernalFurnace, true);
      Part[][][] infernalFurnaceBlueprint = new Part[][][]{{{NB, OB, NB}, {OB, null, OB}, {NB, OB, NB}}, {{NB, OB, NB}, {OB, LA, OB}, {NB, IB, NB}}, {{NB, OB, NB}, {OB, OB, OB}, {NB, OB, NB}}};
      IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFERNALFURNACE", infernalFurnaceBlueprint));
      ThaumcraftApi.addMultiblockRecipeToCatalog("thaumcraft:infernalfurnace", new ThaumcraftApi.BluePrint("INFERNALFURNACE", infernalFurnaceBlueprint, new ItemStack[]{new ItemStack(Blocks.field_150385_bj, 12), new ItemStack(Blocks.field_150343_Z, 12), new ItemStack(Blocks.field_150411_aY), new ItemStack(Items.field_151129_at)}));
      Part IM = new Part(BlocksTC.infusionMatrix, (Object)null);
      Part SNT = new Part(BlocksTC.stone.func_176203_a(0), "AIR");
      Part SNB1 = new Part(BlocksTC.stone.func_176203_a(0), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.NORMAL, EnumFacing.EAST)));
      Part SNB2 = new Part(BlocksTC.stone.func_176203_a(0), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.NORMAL, EnumFacing.NORTH)));
      Part SNB3 = new Part(BlocksTC.stone.func_176203_a(0), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.NORMAL, EnumFacing.SOUTH)));
      Part SNB4 = new Part(BlocksTC.stone.func_176203_a(0), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.NORMAL, EnumFacing.WEST)));
      Part PN = new Part(BlocksTC.pedestal.func_176203_a(0), (Object)null);
      Part[][][] infusionAltarNormalBlueprint = new Part[][][]{{{null, null, null}, {null, IM, null}, {null, null, null}}, {{SNT, null, SNT}, {null, null, null}, {SNT, null, SNT}}, {{SNB1, null, SNB2}, {null, PN, null}, {SNB3, null, SNB4}}};
      IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFUSION", infusionAltarNormalBlueprint));
      ThaumcraftApi.addMultiblockRecipeToCatalog("thaumcraft:infusionaltar", new ThaumcraftApi.BluePrint("INFUSION", infusionAltarNormalBlueprint, new ItemStack[]{new ItemStack(BlocksTC.stone, 8), new ItemStack(BlocksTC.pedestal), new ItemStack(BlocksTC.infusionMatrix)}));
      Part SAT = new Part(BlocksTC.stone.func_176203_a(2), "AIR");
      Part SAB1 = new Part(BlocksTC.stone.func_176203_a(2), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ANCIENT, EnumFacing.EAST)));
      Part SAB2 = new Part(BlocksTC.stone.func_176203_a(2), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ANCIENT, EnumFacing.NORTH)));
      Part SAB3 = new Part(BlocksTC.stone.func_176203_a(2), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ANCIENT, EnumFacing.SOUTH)));
      Part SAB4 = new Part(BlocksTC.stone.func_176203_a(2), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ANCIENT, EnumFacing.WEST)));
      Part PA = new Part(BlocksTC.pedestal.func_176203_a(2), (Object)null);
      Part[][][] infusionAltarAncientBlueprint = new Part[][][]{{{null, null, null}, {null, IM, null}, {null, null, null}}, {{SAT, null, SAT}, {null, null, null}, {SAT, null, SAT}}, {{SAB1, null, SAB2}, {null, PA, null}, {SAB3, null, SAB4}}};
      IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFUSIONANCIENT", infusionAltarAncientBlueprint));
      ThaumcraftApi.addMultiblockRecipeToCatalog("thaumcraft:infusionaltarancient", new ThaumcraftApi.BluePrint("INFUSIONANCIENT", infusionAltarAncientBlueprint, new ItemStack[]{new ItemStack(BlocksTC.stone, 8, 2), new ItemStack(BlocksTC.pedestal, 1, 2), new ItemStack(BlocksTC.infusionMatrix)}));
      Part SET = new Part(BlocksTC.stone.func_176203_a(4), "AIR");
      Part SEB1 = new Part(BlocksTC.stone.func_176203_a(4), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ELDRITCH, EnumFacing.EAST)));
      Part SEB2 = new Part(BlocksTC.stone.func_176203_a(4), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ELDRITCH, EnumFacing.NORTH)));
      Part SEB3 = new Part(BlocksTC.stone.func_176203_a(4), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ELDRITCH, EnumFacing.SOUTH)));
      Part SEB4 = new Part(BlocksTC.stone.func_176203_a(4), new ItemStack(BlocksTC.pillar, 1, BlockPillar.calcMeta(BlockPillar.PillarType.ELDRITCH, EnumFacing.WEST)));
      Part PE = new Part(BlocksTC.pedestal.func_176203_a(1), (Object)null);
      Part[][][] infusionAltarEldritchBlueprint = new Part[][][]{{{null, null, null}, {null, IM, null}, {null, null, null}}, {{SET, null, SET}, {null, null, null}, {SET, null, SET}}, {{SEB1, null, SEB2}, {null, PE, null}, {SEB3, null, SEB4}}};
      IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFUSIONELDRITCH", infusionAltarEldritchBlueprint));
      ThaumcraftApi.addMultiblockRecipeToCatalog("thaumcraft:infusionaltareldritch", new ThaumcraftApi.BluePrint("INFUSIONELDRITCH", infusionAltarEldritchBlueprint, new ItemStack[]{new ItemStack(BlocksTC.stone, 8, 4), new ItemStack(BlocksTC.pedestal, 1, 1), new ItemStack(BlocksTC.infusionMatrix)}));
      Part TH1 = (new Part(BlocksTC.metal.func_176203_a(2), BlocksTC.thaumatoriumTop)).setApplyPlayerFacing(true);
      Part TH2 = (new Part(BlocksTC.metal.func_176203_a(2), BlocksTC.thaumatorium)).setApplyPlayerFacing(true);
      Part TH3 = new Part(BlocksTC.crucible, (Object)null);
      Part[][][] thaumotoriumBlueprint = new Part[][][]{{{TH1}}, {{TH2}}, {{TH3}}};
      IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("THAUMATORIUM", thaumotoriumBlueprint));
      ThaumcraftApi.addMultiblockRecipeToCatalog("thaumcraft:Thaumatorium", new ThaumcraftApi.BluePrint("THAUMATORIUM", thaumotoriumBlueprint, new ItemStack[]{new ItemStack(BlocksTC.metal, 2, 2), new ItemStack(BlocksTC.crucible)}));
      Part GP1 = new Part(Blocks.field_150411_aY, new ItemStack(BlocksTC.placeholder, 1, 2));
      Part GP2 = new Part(Blocks.field_150383_bp, new ItemStack(BlocksTC.placeholder, 1, 4));
      Part GP3 = new Part(Blocks.field_150331_J.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.UP), BlocksTC.golemBuilder);
      Part GP4 = new Part(Blocks.field_150467_bQ, new ItemStack(BlocksTC.placeholder, 1, 3));
      Part GP5 = new Part(BlocksTC.tableStone, new ItemStack(BlocksTC.placeholder, 1, 5));
      Part[][][] golempressBlueprint = new Part[][][]{{{null, null}, {GP1, null}}, {{GP2, GP4}, {GP3, GP5}}};
      IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("MINDCLOCKWORK", golempressBlueprint));
      ThaumcraftApi.addMultiblockRecipeToCatalog("thaumcraft:GolemPress", new ThaumcraftApi.BluePrint("MINDCLOCKWORK", new ItemStack(BlocksTC.golemBuilder), golempressBlueprint, new ItemStack[]{new ItemStack(Blocks.field_150411_aY), new ItemStack(Items.field_151066_bu), new ItemStack(Blocks.field_150331_J), new ItemStack(Blocks.field_150467_bQ), new ItemStack(BlocksTC.tableStone)}));
   }

   private static void initializeAlchemyRecipes() {
      CrucibleRecipe[] cre = new CrucibleRecipe[Aspect.aspects.size()];
      int idx = 0;

      for(Iterator var2 = Aspect.aspects.values().iterator(); var2.hasNext(); ++idx) {
         Aspect aspect = (Aspect)var2.next();
         cre[idx] = new CrucibleRecipe("BASEALCHEMY", ThaumcraftApiHelper.makeCrystal(aspect), "nuggetQuartz", (new AspectList()).add(aspect, 2));
      }

      ThaumcraftApi.addCrucibleRecipe("thaumcraft:vis_crystals", false, cre);
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:nitor", false, new CrucibleRecipe("UNLOCKALCHEMY@3", new ItemStack(BlocksTC.nitor, 1, 4), "dustGlowstone", (new AspectList()).merge(Aspect.ENERGY, 10).merge(Aspect.FIRE, 10).merge(Aspect.LIGHT, 10)));
      String[] dyes = new String[]{"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};
      IRecipe[] re = new ShapelessOreRecipe[16];

      for(int a = 0; a < 16; ++a) {
         re[a] = shapelessOreDictRecipe(new ItemStack(BlocksTC.nitor, 1, a), new Object[]{"dye" + dyes[15 - a], "nitor"});
      }

      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:nitorcolor", false, re);
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:alumentum", false, new CrucibleRecipe("ALUMENTUM@2", new ItemStack(ItemsTC.alumentum), new ItemStack(Items.field_151044_h, 1, 32767), (new AspectList()).merge(Aspect.ENERGY, 10).merge(Aspect.FIRE, 10).merge(Aspect.ENTROPY, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:brassingot", false, new CrucibleRecipe("METALLURGY@1", new ItemStack(ItemsTC.ingots, 1, 2), "ingotIron", (new AspectList()).merge(Aspect.TOOL, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:thaumiumingot", false, new CrucibleRecipe("METALLURGY@2", new ItemStack(ItemsTC.ingots, 1, 0), "ingotIron", (new AspectList()).merge(Aspect.MAGIC, 5).merge(Aspect.EARTH, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:voidingot", false, new CrucibleRecipe("METALLURGY@4", new ItemStack(ItemsTC.ingots, 1, 1), new ItemStack(ItemsTC.voidSeed), (new AspectList()).merge(Aspect.METAL, 10).merge(Aspect.FLUX, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:voidseed", false, new CrucibleRecipe("METALLURGY@4", new ItemStack(ItemsTC.voidSeed), new ItemStack(Items.field_151014_N), (new AspectList()).merge(Aspect.DARKNESS, 5).merge(Aspect.VOID, 5).merge(Aspect.ELDRITCH, 5).merge(Aspect.MAGIC, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:hedge_1", false, new CrucibleRecipe("HEDGEALCHEMY@1", new ItemStack(ItemsTC.tallow), new ItemStack(Items.field_151078_bh), (new AspectList()).merge(Aspect.FIRE, 1)), new CrucibleRecipe("HEDGEALCHEMY@1", new ItemStack(Items.field_151116_aA), new ItemStack(Items.field_151078_bh), (new AspectList()).merge(Aspect.AIR, 3).merge(Aspect.BEAST, 3)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:focus_blank", false, new CrucibleRecipe("UNLOCKTHAUMATURGY", new ItemStack(ItemsTC.focusBlank), ConfigItems.ORDER_CRYSTAL, (new AspectList()).merge(Aspect.CRYSTAL, 20).merge(Aspect.MAGIC, 10).merge(Aspect.AURA, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:metal_purification", false, new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 0), "oreIron", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)), new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 1), "oreGold", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)), new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 6), "oreCinnabar", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)), new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 2), "oreCopper", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)), new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 3), "oreTin", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)), new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 4), "oreSilver", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)), new CrucibleRecipe("METALPURIFICATION", new ItemStack(ItemsTC.clusters, 1, 5), "oreLead", (new AspectList()).merge(Aspect.METAL, 5).merge(Aspect.ORDER, 5)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:LiquidDeath", false, new CrucibleRecipe("LIQUIDDEATH", new ItemStack(ItemsTC.bucketDeath), new ItemStack(Items.field_151133_ar), (new AspectList()).add(Aspect.DEATH, 100).add(Aspect.ALCHEMY, 20).add(Aspect.ENTROPY, 50)));
      ItemStack bt = new ItemStack(ItemsTC.phial, 1, 1);
      ((IEssentiaContainerItem)((IEssentiaContainerItem)bt.func_77973_b())).setAspects(bt, (new AspectList()).add(Aspect.FLUX, 10));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:BottleTaint", false, new CrucibleRecipe("BOTTLETAINT", new ItemStack(ItemsTC.bottleTaint), bt, (new AspectList()).add(Aspect.FLUX, 30).add(Aspect.WATER, 30)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:BathSalts", false, new CrucibleRecipe("BATHSALTS", new ItemStack(ItemsTC.bathSalts), new ItemStack(ItemsTC.salisMundus), (new AspectList()).add(Aspect.MIND, 40).add(Aspect.AIR, 40).add(Aspect.ORDER, 40).add(Aspect.LIFE, 40)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SaneSoap", false, new CrucibleRecipe("SANESOAP", new ItemStack(ItemsTC.sanitySoap), new ItemStack(BlocksTC.fleshBlock), (new AspectList()).add(Aspect.MIND, 75).add(Aspect.ELDRITCH, 50).add(Aspect.ORDER, 75).add(Aspect.LIFE, 50)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealCollect", false, new CrucibleRecipe("SEALCOLLECT", GolemHelper.getSealStack("thaumcraft:pickup"), new ItemStack(ItemsTC.seals), (new AspectList()).add(Aspect.DESIRE, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealCollectAdv", false, new CrucibleRecipe(new String[]{"SEALCOLLECT", "MINDBIOTHAUMIC"}, GolemHelper.getSealStack("thaumcraft:pickup_advanced"), GolemHelper.getSealStack("thaumcraft:pickup"), (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.MIND, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealStore", false, new CrucibleRecipe("SEALSTORE", GolemHelper.getSealStack("thaumcraft:fill"), new ItemStack(ItemsTC.seals), (new AspectList()).add(Aspect.AVERSION, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealStoreAdv", false, new CrucibleRecipe(new String[]{"SEALSTORE", "MINDBIOTHAUMIC"}, GolemHelper.getSealStack("thaumcraft:fill_advanced"), GolemHelper.getSealStack("thaumcraft:fill"), (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.MIND, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealEmpty", false, new CrucibleRecipe("SEALEMPTY", GolemHelper.getSealStack("thaumcraft:empty"), new ItemStack(ItemsTC.seals), (new AspectList()).add(Aspect.VOID, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealEmptyAdv", false, new CrucibleRecipe(new String[]{"SEALEMPTY", "MINDBIOTHAUMIC"}, GolemHelper.getSealStack("thaumcraft:empty_advanced"), GolemHelper.getSealStack("thaumcraft:empty"), (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.MIND, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealProvide", false, new CrucibleRecipe("SEALPROVIDE", GolemHelper.getSealStack("thaumcraft:provider"), GolemHelper.getSealStack("thaumcraft:empty_advanced"), (new AspectList()).add(Aspect.EXCHANGE, 10).add(Aspect.DESIRE, 10)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealGuard", false, new CrucibleRecipe("SEALGUARD", GolemHelper.getSealStack("thaumcraft:guard"), new ItemStack(ItemsTC.seals), (new AspectList()).add(Aspect.AVERSION, 20).add(Aspect.PROTECT, 20)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealGuardAdv", false, new CrucibleRecipe(new String[]{"SEALGUARD", "MINDBIOTHAUMIC"}, GolemHelper.getSealStack("thaumcraft:guard_advanced"), GolemHelper.getSealStack("thaumcraft:guard"), (new AspectList()).add(Aspect.SENSES, 20).add(Aspect.MIND, 20)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealLumber", false, new CrucibleRecipe("SEALLUMBER", GolemHelper.getSealStack("thaumcraft:lumber"), GolemHelper.getSealStack("thaumcraft:breaker"), (new AspectList()).add(Aspect.PLANT, 40).add(Aspect.SENSES, 20)));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:SealUse", false, new CrucibleRecipe("SEALUSE", GolemHelper.getSealStack("thaumcraft:use"), new ItemStack(ItemsTC.seals), (new AspectList()).add(Aspect.CRAFT, 20).add(Aspect.SENSES, 10).add(Aspect.MIND, 20)));
   }

   private static void initializeArcaneRecipes() {
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:vis_resonator", false, new ShapelessArcaneRecipe("UNLOCKTHAUMATURGY@2", new ItemStack(ItemsTC.visResonator), 50, new ItemStack[]{ConfigItems.WATER_CRYSTAL, ConfigItems.AIR_CRYSTAL}, new Object[]{"plateIron", "gemQuartz"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:activatorrail", false, new ShapelessArcaneRecipe("FIRSTSTEPS", new ItemStack(BlocksTC.activatorRail), 10, new ItemStack[0], new Object[]{new ItemStack(Blocks.field_150408_cc)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:thaumometer", false, new ShapedArcaneRecipe("FIRSTSTEPS@2", new ItemStack(ItemsTC.thaumometer), 20, new ItemStack[]{ConfigItems.AIR_CRYSTAL, ConfigItems.EARTH_CRYSTAL, ConfigItems.FIRE_CRYSTAL, ConfigItems.WATER_CRYSTAL, ConfigItems.ORDER_CRYSTAL, ConfigItems.ENTROPY_CRYSTAL}, new Object[]{" I ", "IGI", " I ", 'I', "ingotGold", 'G', new ItemStack(Blocks.field_150410_aZ)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:sanitychecker", false, new ShapedArcaneRecipe("WARP", new ItemStack(ItemsTC.sanityChecker), 20, new ItemStack[]{ConfigItems.ORDER_CRYSTAL, ConfigItems.ENTROPY_CRYSTAL}, new Object[]{"BN ", "M N", "BN ", 'N', "nuggetBrass", 'B', new ItemStack(ItemsTC.brain), 'M', new ItemStack(ItemsTC.mirroredGlass)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:rechargepedestal", false, new ShapedArcaneRecipe("RECHARGEPEDESTAL", new ItemStack(BlocksTC.rechargePedestal), 100, new ItemStack[]{ThaumcraftApiHelper.makeCrystal(Aspect.AIR, 2), ThaumcraftApiHelper.makeCrystal(Aspect.ORDER, 2)}, new Object[]{" R ", "DRD", "SSS", 'I', "ingotGold", 'D', "gemDiamond", 'R', new ItemStack(ItemsTC.visResonator), 'S', "stone"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:workbenchcharger", false, new ShapedArcaneRecipe("WORKBENCHCHARGER", new ItemStack(BlocksTC.arcaneWorkbenchCharger), 200, new ItemStack[]{ThaumcraftApiHelper.makeCrystal(Aspect.AIR, 2), ThaumcraftApiHelper.makeCrystal(Aspect.ORDER, 4)}, new Object[]{" R ", "W W", "I I", 'I', "ingotIron", 'R', new ItemStack(ItemsTC.visResonator), 'W', new ItemStack(BlocksTC.plank)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:wand_workbench", false, new ShapedArcaneRecipe("BASETHAUMATURGY@2", new ItemStack(BlocksTC.wandWorkbench), 100, new ItemStack[]{ConfigItems.WATER_CRYSTAL, ConfigItems.EARTH_CRYSTAL}, new Object[]{"ISI", "BRB", "GTG", 'S', new ItemStack(BlocksTC.slabStone), 'T', new ItemStack(BlocksTC.tableStone), 'R', new ItemStack(ItemsTC.visResonator), 'B', new ItemStack(BlocksTC.stone), 'G', "ingotGold", 'I', "plateIron"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:caster_basic", false, new ShapedArcaneRecipe("UNLOCKTHAUMATURGY@2", new ItemStack(ItemsTC.casterBasic), 100, new ItemStack[]{ConfigItems.AIR_CRYSTAL, ConfigItems.EARTH_CRYSTAL, ConfigItems.FIRE_CRYSTAL, ConfigItems.WATER_CRYSTAL, ConfigItems.ORDER_CRYSTAL, ConfigItems.ENTROPY_CRYSTAL}, new Object[]{"III", "LRL", "LTL", 'T', new ItemStack(ItemsTC.thaumometer), 'R', new ItemStack(ItemsTC.visResonator), 'L', new ItemStack(Items.field_179555_bs), 'I', "ingotIron"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EnchantedFabric", false, new ShapedArcaneRecipe("ENCHFABRIC@1", new ItemStack(ItemsTC.fabric), 5, new ItemStack[0], new Object[]{" S ", "SCS", " S ", 'S', new ItemStack(Items.field_151007_F), 'C', new ItemStack(Blocks.field_150325_L, 1, 32767)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:RobeChest", false, new ShapedArcaneRecipe("ENCHFABRIC", new ItemStack(ItemsTC.clothChest, 1), 100, new ItemStack[0], new Object[]{"I I", "III", "III", 'I', new ItemStack(ItemsTC.fabric)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:RobeLegs", false, new ShapedArcaneRecipe("ENCHFABRIC", new ItemStack(ItemsTC.clothLegs, 1), 100, new ItemStack[0], new Object[]{"III", "I I", "I I", 'I', new ItemStack(ItemsTC.fabric)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:RobeBoots", false, new ShapedArcaneRecipe("ENCHFABRIC", new ItemStack(ItemsTC.clothBoots, 1), 100, new ItemStack[0], new Object[]{"I I", "I I", 'I', new ItemStack(ItemsTC.fabric)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Goggles", false, new ShapedArcaneRecipe("UNLOCKARTIFICE@2", new ItemStack(ItemsTC.goggles), 50, new ItemStack[0], new Object[]{"LGL", "L L", "TGT", 'T', new ItemStack(ItemsTC.thaumometer, 1, 32767), 'G', "ingotBrass", 'L', Items.field_151116_aA}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:SealBlank", false, new ShapelessArcaneRecipe("CONTROLSEALS", new ItemStack(ItemsTC.seals, 3), 20, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{new ItemStack(Items.field_151119_aD), new ItemStack(ItemsTC.tallow), "dyeRed", "nitor"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:modvision", false, new ShapedArcaneRecipe("GOLEMVISION", new ItemStack(ItemsTC.modules, 1, 0), 50, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{"B B", "E E", "PGP", 'B', new ItemStack(Items.field_151069_bo), 'E', new ItemStack(Items.field_151071_bq), 'P', "plateBrass", 'G', "gearBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:modaggression", false, new ShapedArcaneRecipe("SEALGUARD", new ItemStack(ItemsTC.modules, 1, 1), 50, new ItemStack[]{ConfigItems.FIRE_CRYSTAL}, new Object[]{" R ", "RTR", "PGP", 'R', "paneGlass", 'T', new ItemStack(Items.field_151065_br), 'P', "plateBrass", 'G', "gearBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:mirrorglass", false, new ShapelessArcaneRecipe("BASEARTIFICE", new ItemStack(ItemsTC.mirroredGlass), 50, new ItemStack[]{ConfigItems.WATER_CRYSTAL, ConfigItems.ORDER_CRYSTAL}, new Object[]{new ItemStack(ItemsTC.quicksilver), "paneGlass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:ArcaneSpa", false, new ShapedArcaneRecipe("ARCANESPA", new ItemStack(BlocksTC.spa), 50, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{"QIQ", "SJS", "SPS", 'P', "gearBrass", 'J', new ItemStack(BlocksTC.jarNormal), 'S', new ItemStack(BlocksTC.stone, 1, 0), 'Q', new ItemStack(Blocks.field_150371_ca), 'I', new ItemStack(Blocks.field_150411_aY)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Tube", false, new ShapedArcaneRecipe("TUBES", new ItemStack(BlocksTC.tube, 8, 0), 10, new ItemStack[0], new Object[]{" Q ", "IGI", " B ", 'I', "plateIron", 'B', new ItemStack(ItemsTC.nuggets, 1, 8), 'G', "blockGlass", 'Q', new ItemStack(ItemsTC.nuggets, 1, 5)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Resonator", false, new ShapedArcaneRecipe("TUBES", new ItemStack(ItemsTC.resonator), 50, new ItemStack[0], new Object[]{"I I", "INI", " S ", 'I', "plateIron", 'N', Items.field_151128_bU, 'S', "stickWood"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:TubeValve", false, new ShapelessArcaneRecipe("TUBES", new ItemStack(BlocksTC.tubeValve), 10, new ItemStack[0], new Object[]{new ItemStack(BlocksTC.tube), new ItemStack(Blocks.field_150442_at)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:TubeFilter", false, new ShapelessArcaneRecipe("TUBES", new ItemStack(BlocksTC.tubeFilter), 10, new ItemStack[0], new Object[]{new ItemStack(BlocksTC.tube, 1, 0), new ItemStack(ItemsTC.filter)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:TubeRestrict", false, new ShapelessArcaneRecipe("TUBES", new ItemStack(BlocksTC.tubeRestrict), 10, new ItemStack[]{ConfigItems.EARTH_CRYSTAL}, new Object[]{new ItemStack(BlocksTC.tube)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:TubeOneway", false, new ShapelessArcaneRecipe("TUBES", new ItemStack(BlocksTC.tubeOneway), 10, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{new ItemStack(BlocksTC.tube)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:TubeBuffer", false, new ShapedArcaneRecipe("TUBES", new ItemStack(BlocksTC.tubeBuffer), 25, new ItemStack[0], new Object[]{"PVP", "TWT", "PRP", 'T', new ItemStack(BlocksTC.tube), 'V', new ItemStack(BlocksTC.tubeValve), 'W', "plateIron", 'R', new ItemStack(BlocksTC.tubeRestrict), 'P', new ItemStack(ItemsTC.phial)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:WardedJar", false, new ShapedArcaneRecipe("WARDEDJARS", new ItemStack(BlocksTC.jarNormal), 5, new ItemStack[0], new Object[]{"GWG", "G G", "GGG", 'W', "slabWood", 'G', "paneGlass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:JarVoid", false, new ShapedArcaneRecipe("WARDEDJARS", new ItemStack(BlocksTC.jarVoid), 50, new ItemStack[]{ConfigItems.ENTROPY_CRYSTAL}, new Object[]{"J", 'J', new ItemStack(BlocksTC.jarNormal)}));
      IArcaneRecipe[] re = new ShapedArcaneRecipe[16];

      for(int a = 0; a < 16; ++a) {
         ItemStack banner = new ItemStack(BlocksTC.banner, 1, 0);
         banner.func_77982_d(new NBTTagCompound());
         banner.func_77978_p().func_74774_a("color", (byte)a);
         re[a] = new ShapedArcaneRecipe("BASEARTIFICE", banner, 10, new ItemStack[0], new Object[]{"WS", "WS", "WB", 'W', new ItemStack(Blocks.field_150325_L, 1, 15 - a), 'S', "stickWood", 'B', "slabWood"});
      }

      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Banners", false, re);
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:PaveBarrier", false, new ShapedArcaneRecipe("PAVINGSTONES", new ItemStack(BlocksTC.pavingStone, 4, 0), 50, new ItemStack[]{ConfigItems.FIRE_CRYSTAL, ConfigItems.ORDER_CRYSTAL}, new Object[]{"SS", "SS", 'S', new ItemStack(BlocksTC.stone, 1, 1)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:PaveTravel", false, new ShapedArcaneRecipe("PAVINGSTONES", new ItemStack(BlocksTC.pavingStone, 4, 1), 50, new ItemStack[]{ConfigItems.AIR_CRYSTAL, ConfigItems.EARTH_CRYSTAL}, new Object[]{"SS", "SS", 'S', new ItemStack(BlocksTC.stone, 1, 1)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:ArcaneLamp", false, new ShapedArcaneRecipe("ARCANELAMP", new ItemStack(BlocksTC.lampArcane), 50, new ItemStack[]{ConfigItems.AIR_CRYSTAL, ConfigItems.FIRE_CRYSTAL}, new Object[]{" I ", "IAI", " I ", 'A', new ItemStack(BlocksTC.translucent), 'I', "plateIron"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Levitator", false, new ShapedArcaneRecipe("LEVITATOR", new ItemStack(BlocksTC.levitator), 35, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{"WIW", "BNB", "WGW", 'I', "plateThaumium", 'N', "nitor", 'W', "plankWood", 'B', "plateIron", 'G', "gearBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:RedstoneRelay", false, new ShapedArcaneRecipe("REDSTONERELAY", new ItemStack(BlocksTC.redstoneRelay), 10, new ItemStack[]{ConfigItems.ORDER_CRYSTAL}, new Object[]{"   ", "GTG", "SSS", 'T', new ItemStack(Blocks.field_150429_aA), 'G', "gearBrass", 'S', new ItemStack(Blocks.field_150333_U)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:ArcaneEar", false, new ShapedArcaneRecipe("ARCANEEAR", new ItemStack(BlocksTC.arcaneEar), 15, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{"   ", "PGP", "WRW", 'W', "slabWood", 'R', Items.field_151137_ax, 'G', "gearBrass", 'P', "plateBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:InfusionMatrix", false, new ShapedArcaneRecipe("INFUSION@2", new ItemStack(BlocksTC.infusionMatrix), 150, new ItemStack[]{ConfigItems.AIR_CRYSTAL, ConfigItems.EARTH_CRYSTAL, ConfigItems.FIRE_CRYSTAL, ConfigItems.WATER_CRYSTAL, ConfigItems.ORDER_CRYSTAL, ConfigItems.ENTROPY_CRYSTAL}, new Object[]{"S S", " N ", "S S", 'S', new ItemStack(BlocksTC.stone, 1, 1), 'N', "nitor"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:MatrixMotion", false, new ShapedArcaneRecipe("INFUSIONBOOST", new ItemStack(BlocksTC.stone, 1, 8), 500, new ItemStack[]{ConfigItems.ORDER_CRYSTAL, ConfigItems.AIR_CRYSTAL}, new Object[]{"SNS", "NGN", "SNS", 'S', new ItemStack(BlocksTC.stone), 'N', "nitor", 'G', new ItemStack(Blocks.field_150484_ah)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:MatrixCost", false, new ShapedArcaneRecipe("INFUSIONBOOST", new ItemStack(BlocksTC.stone, 1, 9), 500, new ItemStack[]{ConfigItems.ORDER_CRYSTAL, ConfigItems.WATER_CRYSTAL}, new Object[]{"SAS", "AGA", "SAS", 'S', new ItemStack(BlocksTC.stone), 'A', new ItemStack(ItemsTC.alumentum), 'G', new ItemStack(Blocks.field_150484_ah)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:ArcanePedestal", false, new ShapedArcaneRecipe("INFUSION", new ItemStack(BlocksTC.pedestal), 10, new ItemStack[0], new Object[]{"SSS", " B ", "SSS", 'S', new ItemStack(BlocksTC.slabStone), 'B', new ItemStack(BlocksTC.stone)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:AncientPedestal", false, new ShapedArcaneRecipe("INFUSIONANCIENT", new ItemStack(BlocksTC.pedestal, 1, 2), 150, new ItemStack[0], new Object[]{"SSS", " B ", "SSS", 'S', new ItemStack(BlocksTC.slabStone, 1, 2), 'B', new ItemStack(BlocksTC.stone, 1, 2)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EldritchPedestal", false, new ShapedArcaneRecipe("INFUSIONELDRITCH", new ItemStack(BlocksTC.pedestal, 1, 1), 150, new ItemStack[0], new Object[]{"SSS", " B ", "SSS", 'S', new ItemStack(BlocksTC.slabStone, 1, 3), 'B', new ItemStack(BlocksTC.stone, 1, 4)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:FocusPouch", false, new ShapedArcaneRecipe("FOCUSPOUCH", new ItemStack(ItemsTC.focusPouch), 25, new ItemStack[0], new Object[]{"LGL", "LBL", "LLL", 'B', new ItemStack(ItemsTC.baubles, 1, 2), 'L', Items.field_151116_aA, 'G', Items.field_151043_k}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:dioptra", false, new ShapedArcaneRecipe("DIOPTRA", new ItemStack(BlocksTC.dioptra), 50, new ItemStack[]{ConfigItems.AIR_CRYSTAL, ConfigItems.WATER_CRYSTAL}, new Object[]{"APA", "IGI", "AAA", 'A', new ItemStack(BlocksTC.stone), 'G', new ItemStack(ItemsTC.thaumometer), 'P', new ItemStack(ItemsTC.visResonator), 'I', "plateIron"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:HungryChest", false, new ShapedArcaneRecipe("HUNGRYCHEST", new ItemStack(BlocksTC.hungryChest), 15, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{"WTW", "W W", "WWW", 'W', new ItemStack(BlocksTC.plank), 'T', new ItemStack(Blocks.field_150415_aT)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Filter", false, new ShapedArcaneRecipe("BASEALCHEMY", new ItemStack(ItemsTC.filter, 2, 0), 15, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{"GWG", 'G', Items.field_151043_k, 'W', new ItemStack(BlocksTC.plank, 1, 1)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:MorphicResonator", false, new ShapedArcaneRecipe("BASEALCHEMY", new ItemStack(ItemsTC.morphicResonator), 50, new ItemStack[]{ConfigItems.ORDER_CRYSTAL}, new Object[]{" G ", "B B", " G ", 'G', "paneGlass", 'B', "plateBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Alembic", false, new ShapedArcaneRecipe("ESSENTIASMELTER@3", new ItemStack(BlocksTC.alembic), 50, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{"WFW", "SBS", "WFW", 'W', new ItemStack(BlocksTC.plank), 'B', Items.field_151133_ar, 'F', new ItemStack(ItemsTC.filter), 'S', "plateBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EssentiaSmelter", false, new ShapedArcaneRecipe("ESSENTIASMELTER", new ItemStack(BlocksTC.smelterBasic), 50, new ItemStack[]{ConfigItems.FIRE_CRYSTAL}, new Object[]{"BCB", "SFS", "SSS", 'C', new ItemStack(BlocksTC.crucible), 'F', new ItemStack(Blocks.field_150460_al), 'S', "cobblestone", 'B', "plateBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EssentiaSmelterThaumium", false, new ShapedArcaneRecipe("ESSENTIASMELTERTHAUMIUM", new ItemStack(BlocksTC.smelterThaumium), 250, new ItemStack[]{ConfigItems.FIRE_CRYSTAL}, new Object[]{"BFB", "IGI", "III", 'C', new ItemStack(BlocksTC.crucible), 'F', new ItemStack(BlocksTC.smelterBasic), 'G', new ItemStack(BlocksTC.metal, 1, 2), 'I', "plateThaumium", 'B', "plateBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EssentiaSmelterVoid", false, new ShapedArcaneRecipe("ESSENTIASMELTERVOID", new ItemStack(BlocksTC.smelterVoid), 750, new ItemStack[]{ConfigItems.FIRE_CRYSTAL}, new Object[]{"BFB", "IGI", "III", 'C', new ItemStack(BlocksTC.crucible), 'F', new ItemStack(BlocksTC.smelterBasic), 'G', new ItemStack(BlocksTC.metal, 1, 3), 'I', "plateVoid", 'B', "plateBrass"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:AlchemicalConstruct", false, new ShapedArcaneRecipe("TUBES", new ItemStack(BlocksTC.metal, 1, 2), 75, new ItemStack[]{ConfigItems.WATER_CRYSTAL, ConfigItems.ORDER_CRYSTAL}, new Object[]{"IVI", "TWT", "IVI", 'W', new ItemStack(BlocksTC.plank), 'V', new ItemStack(BlocksTC.tubeValve), 'T', new ItemStack(BlocksTC.tube), 'I', "plateIron"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:AdvAlchemyConstruct", false, new ShapedArcaneRecipe("ESSENTIASMELTERVOID@1", new ItemStack(BlocksTC.metal, 1, 3), 200, new ItemStack[]{ConfigItems.FIRE_CRYSTAL, ConfigItems.EARTH_CRYSTAL}, new Object[]{"VAV", "APA", "VAV", 'A', new ItemStack(BlocksTC.metal, 1, 2), 'V', "plateVoid", 'P', new ItemStack(ItemsTC.primordialPearl)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:SmelterAux", false, new ShapedArcaneRecipe("IMPROVEDSMELTING", new ItemStack(BlocksTC.smelterAux), 100, new ItemStack[]{ConfigItems.WATER_CRYSTAL}, new Object[]{"WTW", "RGR", "IBI", 'W', new ItemStack(BlocksTC.plank), 'B', new ItemStack(BlocksTC.bellows), 'R', "plateBrass", 'T', new ItemStack(BlocksTC.tubeFilter), 'I', "plateIron", 'G', new ItemStack(BlocksTC.metal, 1, 2)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:SmelterVent", false, new ShapedArcaneRecipe("IMPROVEDSMELTING2", new ItemStack(BlocksTC.smelterVent), 150, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{"IBI", "MGF", "IBI", 'I', "plateIron", 'B', "plateBrass", 'F', new ItemStack(ItemsTC.filter), 'M', new ItemStack(ItemsTC.filter), 'G', new ItemStack(BlocksTC.metal, 1, 2)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EssentiaTransportIn", false, new ShapedArcaneRecipe("ESSENTIATRANSPORT", new ItemStack(BlocksTC.essentiaTransportInput), 100, new ItemStack[]{ConfigItems.WATER_CRYSTAL, ConfigItems.AIR_CRYSTAL}, new Object[]{"   ", "BQB", "IGI", 'I', "plateIron", 'B', "plateBrass", 'Q', new ItemStack(Blocks.field_150367_z, 1, 0), 'G', new ItemStack(BlocksTC.metal, 1, 2)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:EssentiaTransportOut", false, new ShapedArcaneRecipe("ESSENTIATRANSPORT", new ItemStack(BlocksTC.essentiaTransportOutput), 100, new ItemStack[]{ConfigItems.WATER_CRYSTAL, ConfigItems.AIR_CRYSTAL}, new Object[]{"   ", "BQB", "IGI", 'I', "plateIron", 'B', "plateBrass", 'Q', new ItemStack(Blocks.field_150438_bZ, 1, 0), 'G', new ItemStack(BlocksTC.metal, 1, 2)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Bellows", false, new ShapedArcaneRecipe("BELLOWS", new ItemStack(BlocksTC.bellows, 1, 0), 25, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{"WW ", "LLI", "WW ", 'W', "plankWood", 'I', "ingotIron", 'L', Items.field_151116_aA}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:Centrifuge", false, new ShapedArcaneRecipe("CENTRIFUGE", new ItemStack(BlocksTC.centrifuge), 100, new ItemStack[]{ConfigItems.ENTROPY_CRYSTAL, ConfigItems.ORDER_CRYSTAL}, new Object[]{" T ", "RCP", " T ", 'T', new ItemStack(BlocksTC.tube), 'P', "gearBrass", 'R', new ItemStack(ItemsTC.morphicResonator), 'C', new ItemStack(BlocksTC.metal, 1, 2)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:MnemonicMatrix", false, new ShapedArcaneRecipe("THAUMATORIUM", new ItemStack(BlocksTC.brainBox), 50, new ItemStack[]{ConfigItems.ORDER_CRYSTAL}, new Object[]{"IAI", "ABA", "IAI", 'B', new ItemStack(ItemsTC.mind, 1, 0), 'A', "gemAmber", 'I', "plateIron"}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:MindClockwork", false, new ShapedArcaneRecipe("MINDCLOCKWORK@2", new ItemStack(ItemsTC.mind, 1, 0), 25, new ItemStack[]{ConfigItems.ORDER_CRYSTAL, ConfigItems.FIRE_CRYSTAL}, new Object[]{" P ", "PGP", "BCB", 'G', "gearBrass", 'B', "plateBrass", 'P', "paneGlass", 'C', new ItemStack(Items.field_151132_bS)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:AutomatedCrossbow", false, new ShapedArcaneRecipe("BASICTURRET", new ItemStack(ItemsTC.turretPlacer, 1, 0), 100, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{"BGI", "WMW", "S S", 'G', "gearBrass", 'I', "plateIron", 'S', "stickWood", 'M', new ItemStack(ItemsTC.mind), 'B', new ItemStack(Items.field_151031_f), 'W', new ItemStack(BlocksTC.plank)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:AdvancedCrossbow", false, new ShapedArcaneRecipe("ADVANCEDTURRET", new ItemStack(ItemsTC.turretPlacer, 1, 1), 150, new ItemStack[]{ConfigItems.AIR_CRYSTAL}, new Object[]{"PMP", "PTP", "BGB", 'G', "gearBrass", 'T', new ItemStack(ItemsTC.turretPlacer, 1, 0), 'P', "plateIron", 'B', "plateBrass", 'M', new ItemStack(ItemsTC.mind, 1, 1)}));
      ThaumcraftApi.addArcaneCraftingRecipe("thaumcraft:patterncrafter", false, new ShapedArcaneRecipe("ARCANEPATTERNCRAFTER", new ItemStack(BlocksTC.patternCrafter), 50, new ItemStack[]{ConfigItems.ORDER_CRYSTAL, ConfigItems.EARTH_CRYSTAL, ConfigItems.WATER_CRYSTAL}, new Object[]{"VH ", "GCG", " W ", 'H', new ItemStack(Blocks.field_150438_bZ), 'W', new ItemStack(BlocksTC.plank), 'G', "gearBrass", 'V', new ItemStack(ItemsTC.visResonator), 'C', new ItemStack(Blocks.field_150462_ai)}));
   }

   private static void initializeInfusionRecipes() {
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:SealHarvest", false, new InfusionRecipe("SEALHARVEST", GolemHelper.getSealStack("thaumcraft:harvest"), 0, (new AspectList()).add(Aspect.PLANT, 10).add(Aspect.SENSES, 10).add(Aspect.MAN, 10), new ItemStack(ItemsTC.seals), new Object[]{new ItemStack(Items.field_151014_N), new ItemStack(Items.field_151080_bb), new ItemStack(Items.field_151081_bc), new ItemStack(Items.field_151100_aR, 1, 3), new ItemStack(Items.field_151120_aE), new ItemStack(Blocks.field_150434_aF)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:SealButcher", false, new InfusionRecipe("SEALBUTCHER", GolemHelper.getSealStack("thaumcraft:butcher"), 0, (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.SENSES, 10).add(Aspect.MAN, 10), GolemHelper.getSealStack("thaumcraft:guard"), new Object[]{new ItemStack(Items.field_151116_aA), new ItemStack(Blocks.field_150325_L, 1, 32767), new ItemStack(Items.field_179555_bs), new ItemStack(Items.field_151147_al), new ItemStack(Items.field_179561_bm), new ItemStack(Items.field_151082_bd)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:SealBreak", false, new InfusionRecipe("SEALBREAK", GolemHelper.getSealStack("thaumcraft:breaker"), 1, (new AspectList()).add(Aspect.TOOL, 10).add(Aspect.ENTROPY, 10).add(Aspect.MAN, 10), new ItemStack(ItemsTC.seals), new Object[]{new ItemStack(Items.field_151006_E), new ItemStack(Items.field_151005_D), new ItemStack(Items.field_151011_C)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:EverfullUrn", false, new InfusionRecipe("EVERFULLURN", new ItemStack(BlocksTC.everfullUrn), 1, (new AspectList()).add(Aspect.WATER, 40).add(Aspect.CRAFT, 20).add(Aspect.EARTH, 20), new ItemStack(Blocks.field_150405_ch), new Object[]{new ItemStack(Items.field_151131_as), new ItemStack(Items.field_151131_as), "shardWater", new ItemStack(ItemsTC.salisMundus)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:JarBrain", false, new InfusionRecipe("JARBRAIN", new ItemStack(BlocksTC.jarBrain), 4, (new AspectList()).add(Aspect.MIND, 25).add(Aspect.SENSES, 25).add(Aspect.UNDEAD, 25), new ItemStack(BlocksTC.jarNormal), new ItemStack[]{new ItemStack(ItemsTC.brain), new ItemStack(Items.field_151070_bp), new ItemStack(Items.field_151131_as), new ItemStack(Items.field_151070_bp)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:VisAmulet", false, new InfusionRecipe("VISAMULET", new ItemStack(ItemsTC.amuletVis, 1, 1), 6, (new AspectList()).add(Aspect.AURA, 50).add(Aspect.ENERGY, 100).add(Aspect.VOID, 50), new ItemStack(ItemsTC.baubles, 1, 0), new Object[]{new ItemStack(ItemsTC.visResonator), ThaumcraftApiHelper.makeCrystal(Aspect.AIR), ThaumcraftApiHelper.makeCrystal(Aspect.FIRE), ThaumcraftApiHelper.makeCrystal(Aspect.WATER), ThaumcraftApiHelper.makeCrystal(Aspect.EARTH), ThaumcraftApiHelper.makeCrystal(Aspect.ORDER)}));
      InfusionRunicAugmentRecipe ra = new InfusionRunicAugmentRecipe();
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:RunicArmor", false, ra);
      InfusionRunicAugmentRecipe[] rar = new InfusionRunicAugmentRecipe[5];

      ItemStack in;
      for(int a = 0; a <= 4; ++a) {
         in = new ItemStack(ItemsTC.baubles, 1, 1);
         if (a > 0) {
            in.func_77983_a("TC.RUNIC", new NBTTagByte((byte)a));
         }

         rar[a] = new InfusionRunicAugmentRecipe(in);
      }

      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:RunicArmorFake", true, rar);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:Mirror", false, new InfusionRecipe("MIRROR", new ItemStack(BlocksTC.mirror), 1, (new AspectList()).add(Aspect.MOTION, 25).add(Aspect.DARKNESS, 25).add(Aspect.EXCHANGE, 25), new ItemStack(ItemsTC.mirroredGlass), new Object[]{"ingotGold", "ingotGold", "ingotGold", new ItemStack(Items.field_151079_bi)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:MirrorHand", false, new InfusionRecipe("MIRRORHAND", new ItemStack(ItemsTC.handMirror), 5, (new AspectList()).add(Aspect.TOOL, 50).add(Aspect.MOTION, 50), new ItemStack(BlocksTC.mirror), new Object[]{"stickWood", new ItemStack(Items.field_151111_aL), new ItemStack(Items.field_151148_bJ)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:MirrorEssentia", false, new InfusionRecipe("MIRRORESSENTIA", new ItemStack(BlocksTC.mirrorEssentia), 2, (new AspectList()).add(Aspect.MOTION, 25).add(Aspect.WATER, 25).add(Aspect.EXCHANGE, 25), new ItemStack(ItemsTC.mirroredGlass), new Object[]{"ingotIron", "ingotIron", "ingotIron", new ItemStack(Items.field_151079_bi)}));
      ItemStack isEA = new ItemStack(ItemsTC.elementalAxe);
      EnumInfusionEnchantment.addInfusionEnchantment(isEA, EnumInfusionEnchantment.COLLECTOR, 1);
      EnumInfusionEnchantment.addInfusionEnchantment(isEA, EnumInfusionEnchantment.BURROWING, 1);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:ElementalAxe", false, new InfusionRecipe("ELEMENTALTOOLS", isEA, 1, (new AspectList()).add(Aspect.WATER, 60).add(Aspect.PLANT, 30), new ItemStack(ItemsTC.thaumiumAxe), new Object[]{"shardWater", "shardWater", new ItemStack(Items.field_151045_i), new ItemStack(BlocksTC.plank)}));
      in = new ItemStack(ItemsTC.elementalPick);
      EnumInfusionEnchantment.addInfusionEnchantment(in, EnumInfusionEnchantment.REFINING, 1);
      EnumInfusionEnchantment.addInfusionEnchantment(in, EnumInfusionEnchantment.SOUNDING, 2);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:ElementalPick", false, new InfusionRecipe("ELEMENTALTOOLS", in, 1, (new AspectList()).add(Aspect.FIRE, 30).add(Aspect.METAL, 30).add(Aspect.SENSES, 30), new ItemStack(ItemsTC.thaumiumPick), new Object[]{"shardFire", "shardFire", new ItemStack(Items.field_151045_i), new ItemStack(BlocksTC.plank)}));
      ItemStack isESW = new ItemStack(ItemsTC.elementalSword);
      EnumInfusionEnchantment.addInfusionEnchantment(isESW, EnumInfusionEnchantment.ARCING, 2);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:ElementalSword", false, new InfusionRecipe("ELEMENTALTOOLS", isESW, 1, (new AspectList()).add(Aspect.AIR, 30).add(Aspect.MOTION, 30).add(Aspect.AVERSION, 30), new ItemStack(ItemsTC.thaumiumSword), new Object[]{"shardAir", "shardAir", new ItemStack(Items.field_151045_i), new ItemStack(BlocksTC.plank)}));
      ItemStack isES = new ItemStack(ItemsTC.elementalShovel);
      EnumInfusionEnchantment.addInfusionEnchantment(isES, EnumInfusionEnchantment.DESTRUCTIVE, 1);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:ElementalShovel", false, new InfusionRecipe("ELEMENTALTOOLS", isES, 1, (new AspectList()).add(Aspect.EARTH, 60).add(Aspect.CRAFT, 30), new ItemStack(ItemsTC.thaumiumShovel), new Object[]{"shardEarth", "shardEarth", new ItemStack(Items.field_151045_i), new ItemStack(BlocksTC.plank)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:ElementalHoe", false, new InfusionRecipe("ELEMENTALTOOLS", new ItemStack(ItemsTC.elementalHoe), 1, (new AspectList()).add(Aspect.ORDER, 30).add(Aspect.PLANT, 30).add(Aspect.ENTROPY, 30), new ItemStack(ItemsTC.thaumiumHoe), new Object[]{"shardOrder", "shardEntropy", new ItemStack(Items.field_151045_i), new ItemStack(BlocksTC.plank)}));
      InfusionEnchantmentRecipe IEBURROWING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.BURROWING, (new AspectList()).add(Aspect.SENSES, 80).add(Aspect.EARTH, 150), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(Items.field_179556_br)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEBURROWING", false, IEBURROWING);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEBURROWINGFAKE", true, new InfusionEnchantmentRecipe(IEBURROWING, new ItemStack(Items.field_151039_o)));
      InfusionEnchantmentRecipe IECOLLECTOR = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.COLLECTOR, (new AspectList()).add(Aspect.DESIRE, 80).add(Aspect.WATER, 100), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(Items.field_151058_ca)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IECOLLECTOR", false, IECOLLECTOR);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IECOLLECTORFAKE", true, new InfusionEnchantmentRecipe(IECOLLECTOR, new ItemStack(Items.field_151049_t)));
      InfusionEnchantmentRecipe IEDESTRUCTIVE = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.DESTRUCTIVE, (new AspectList()).add(Aspect.AVERSION, 200).add(Aspect.ENTROPY, 250), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(Blocks.field_150335_W)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEDESTRUCTIVE", false, IEDESTRUCTIVE);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEDESTRUCTIVEFAKE", true, new InfusionEnchantmentRecipe(IEDESTRUCTIVE, new ItemStack(Items.field_151050_s)));
      InfusionEnchantmentRecipe IEREFINING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.REFINING, (new AspectList()).add(Aspect.ORDER, 80).add(Aspect.EXCHANGE, 60), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(ItemsTC.salisMundus)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEREFINING", false, IEREFINING);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEREFININGFAKE", true, new InfusionEnchantmentRecipe(IEREFINING, new ItemStack(Items.field_151035_b)));
      InfusionEnchantmentRecipe IESOUNDING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.SOUNDING, (new AspectList()).add(Aspect.SENSES, 40).add(Aspect.FIRE, 60), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(Items.field_151148_bJ)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IESOUNDING", false, IESOUNDING);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IESOUNDINGFAKE", true, new InfusionEnchantmentRecipe(IESOUNDING, new ItemStack(Items.field_151005_D)));
      InfusionEnchantmentRecipe IEARCING = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.ARCING, (new AspectList()).add(Aspect.ENERGY, 40).add(Aspect.AIR, 60), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(Blocks.field_150451_bX)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEARCING", false, IEARCING);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEARCINGFAKE", true, new InfusionEnchantmentRecipe(IEARCING, new ItemStack(Items.field_151041_m)));
      InfusionEnchantmentRecipe IEESSENCE = new InfusionEnchantmentRecipe(EnumInfusionEnchantment.ESSENCE, (new AspectList()).add(Aspect.BEAST, 40).add(Aspect.FLUX, 60), new Object[]{new ItemStack(Items.field_151134_bR), new ItemStack(ItemsTC.crystalEssence)});
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEESSENCE", false, IEESSENCE);
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:IEESSENCEFAKE", true, new InfusionEnchantmentRecipe(IEBURROWING, new ItemStack(Items.field_151052_q)));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:BootsTraveller", false, new InfusionRecipe("BOOTSTRAVELLER", new ItemStack(ItemsTC.travellerBoots), 1, (new AspectList()).add(Aspect.FLIGHT, 100).add(Aspect.MOTION, 100), new ItemStack(Items.field_151021_T), new Object[]{"shardAir", "shardAir", new ItemStack(ItemsTC.fabric), new ItemStack(ItemsTC.fabric), new ItemStack(Items.field_151008_G), new ItemStack(Items.field_151115_aP, 1, 32767)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:MindBiothaumic", false, new InfusionRecipe("MINDBIOTHAUMIC", new ItemStack(ItemsTC.mind, 1, 1), 4, (new AspectList()).add(Aspect.MIND, 25).add(Aspect.MECHANISM, 25), new ItemStack(ItemsTC.mind, 1, 0), new Object[]{new ItemStack(ItemsTC.brain), new ItemStack(Items.field_151113_aN)}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:ArcaneBore", false, new InfusionRecipe("ARCANEBORE", new ItemStack(ItemsTC.turretPlacer, 1, 2), 4, (new AspectList()).add(Aspect.ENERGY, 25).add(Aspect.EARTH, 25).add(Aspect.MECHANISM, 100).add(Aspect.VOID, 25).add(Aspect.MOTION, 25), new ItemStack(ItemsTC.turretPlacer), new Object[]{new ItemStack(BlocksTC.plank), new ItemStack(BlocksTC.plank), "gearBrass", "plateBrass", new ItemStack(Items.field_151046_w), new ItemStack(Items.field_151047_v), new ItemStack(ItemsTC.morphicResonator), "shardAir", "shardEarth"}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:LampGrowth", false, new InfusionRecipe("LAMPGROWTH", new ItemStack(BlocksTC.lampGrowth), 4, (new AspectList()).add(Aspect.PLANT, 16).add(Aspect.LIGHT, 8).add(Aspect.LIFE, 8).add(Aspect.TOOL, 8), new ItemStack(BlocksTC.lampArcane), new Object[]{new ItemStack(Items.field_151043_k), new ItemStack(Items.field_151100_aR, 1, 15), "shardEarth", new ItemStack(Items.field_151043_k), new ItemStack(Items.field_151100_aR, 1, 15), "shardEarth"}));
      ThaumcraftApi.addInfusionCraftingRecipe("thaumcraft:LampFertility", false, new InfusionRecipe("LAMPFERTILITY", new ItemStack(BlocksTC.lampFertility), 4, (new AspectList()).add(Aspect.BEAST, 16).add(Aspect.LIGHT, 8).add(Aspect.LIFE, 8).add(Aspect.DESIRE, 8), new ItemStack(BlocksTC.lampArcane), new Object[]{new ItemStack(Items.field_151043_k), new ItemStack(Items.field_151015_O), "shardFire", new ItemStack(Items.field_151043_k), new ItemStack(Items.field_151172_bF), "shardFire"}));
   }

   private static void initializeNormalRecipes() {
      GameRegistry.addRecipe(new RecipesRobeArmorDyes());
      GameRegistry.addRecipe(new RecipesVoidRobeArmorDyes());
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 0), new Object[]{"#", '#', Items.field_151042_j});
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 6), new Object[]{"#", '#', new ItemStack(ItemsTC.ingots, 1, 0)});
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 7), new Object[]{"#", '#', new ItemStack(ItemsTC.ingots, 1, 1)});
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 8), new Object[]{"#", '#', new ItemStack(ItemsTC.ingots, 1, 2)});
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 9), new Object[]{"#", '#', Items.field_151128_bU});
      oreDictRecipe(new ItemStack(Items.field_151042_j), new Object[]{"###", "###", "###", '#', "nuggetIron"});
      oreDictRecipe(new ItemStack(ItemsTC.ingots, 1, 0), new Object[]{"###", "###", "###", '#', "nuggetThaumium"});
      oreDictRecipe(new ItemStack(ItemsTC.ingots, 1, 1), new Object[]{"###", "###", "###", '#', "nuggetVoid"});
      oreDictRecipe(new ItemStack(ItemsTC.ingots, 1, 2), new Object[]{"###", "###", "###", '#', "nuggetBrass"});
      oreDictRecipe(new ItemStack(ItemsTC.quicksilver), new Object[]{"###", "###", "###", '#', "nuggetQuicksilver"});
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 5), new Object[]{"#", '#', new ItemStack(ItemsTC.quicksilver)});
      oreDictRecipe(new ItemStack(BlocksTC.metal, 1, 0), new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.ingots, 1, 0)});
      oreDictRecipe(new ItemStack(ItemsTC.ingots, 9, 0), new Object[]{"#", '#', new ItemStack(BlocksTC.metal, 1, 0)});
      oreDictRecipe(new ItemStack(BlocksTC.metal, 1, 1), new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.ingots, 1, 1)});
      oreDictRecipe(new ItemStack(ItemsTC.ingots, 9, 1), new Object[]{"#", '#', new ItemStack(BlocksTC.metal, 1, 1)});
      oreDictRecipe(new ItemStack(BlocksTC.metal, 1, 4), new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.ingots, 1, 2)});
      oreDictRecipe(new ItemStack(ItemsTC.ingots, 9, 2), new Object[]{"#", '#', new ItemStack(BlocksTC.metal, 1, 4)});
      oreDictRecipe(new ItemStack(BlocksTC.fleshBlock), new Object[]{"###", "###", "###", '#', Items.field_151078_bh});
      oreDictRecipe(new ItemStack(Items.field_151078_bh, 9, 0), new Object[]{"#", '#', BlocksTC.fleshBlock});
      oreDictRecipe(new ItemStack(BlocksTC.translucent, 1, 0), new Object[]{"###", "###", "###", '#', "gemAmber"});
      oreDictRecipe(new ItemStack(BlocksTC.translucent, 4, 1), new Object[]{"##", "##", '#', new ItemStack(BlocksTC.translucent, 1, 0)});
      oreDictRecipe(new ItemStack(BlocksTC.translucent, 4, 0), new Object[]{"##", "##", '#', new ItemStack(BlocksTC.translucent, 1, 1)});
      oreDictRecipe(new ItemStack(ItemsTC.amber, 9, 0), new Object[]{"#", '#', new ItemStack(BlocksTC.translucent, 1, 0)});
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:ironplate", false, oreDictRecipe(new ItemStack(ItemsTC.plate, 3, 1), new Object[]{"BBB", 'B', "ingotIron"}));
      ThaumcraftApi.addRecipeUnlinked("thaumcraft:ironplate");
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:brassstuff", false, oreDictRecipe(new ItemStack(ItemsTC.plate, 3, 0), new Object[]{"BBB", 'B', "ingotBrass"}), oreDictRecipe(new ItemStack(ItemsTC.gear, 1, 0), new Object[]{" B ", "BIB", " B ", 'I', "ingotIron", 'B', "ingotBrass"}));
      ThaumcraftApi.addRecipeUnlinked("thaumcraft:brassstuff");
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:thaumiumstuff", false, oreDictRecipe(new ItemStack(ItemsTC.plate, 3, 2), new Object[]{"BBB", 'B', "ingotThaumium"}), oreDictRecipe(new ItemStack(ItemsTC.gear, 1, 1), new Object[]{" B ", "BIB", " B ", 'I', "ingotIron", 'B', "ingotThaumium"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumHelm, 1), new Object[]{"III", "I I", 'I', "ingotThaumium"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumChest, 1), new Object[]{"I I", "III", "III", 'I', "ingotThaumium"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumLegs, 1), new Object[]{"III", "I I", "I I", 'I', "ingotThaumium"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumBoots, 1), new Object[]{"I I", "I I", 'I', "ingotThaumium"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumShovel, 1), new Object[]{"I", "S", "S", 'I', "ingotThaumium", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumPick, 1), new Object[]{"III", " S ", " S ", 'I', "ingotThaumium", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumAxe, 1), new Object[]{"II", "SI", "S ", 'I', "ingotThaumium", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumHoe, 1), new Object[]{"II", "S ", "S ", 'I', "ingotThaumium", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.thaumiumSword, 1), new Object[]{"I", "I", "S", 'I', "ingotThaumium", 'S', "stickWood"}));
      ThaumcraftApi.addRecipeUnlinked("thaumcraft:thaumiumstuff");
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:voidstuff", false, oreDictRecipe(new ItemStack(ItemsTC.plate, 3, 3), new Object[]{"BBB", 'B', "ingotVoid"}), oreDictRecipe(new ItemStack(ItemsTC.gear, 1, 2), new Object[]{" B ", "BIB", " B ", 'I', "ingotIron", 'B', "ingotVoid"}), oreDictRecipe(new ItemStack(ItemsTC.voidHelm, 1), new Object[]{"III", "I I", 'I', "ingotVoid"}), oreDictRecipe(new ItemStack(ItemsTC.voidChest, 1), new Object[]{"I I", "III", "III", 'I', "ingotVoid"}), oreDictRecipe(new ItemStack(ItemsTC.voidLegs, 1), new Object[]{"III", "I I", "I I", 'I', "ingotVoid"}), oreDictRecipe(new ItemStack(ItemsTC.voidBoots, 1), new Object[]{"I I", "I I", 'I', "ingotVoid"}), oreDictRecipe(new ItemStack(ItemsTC.voidShovel, 1), new Object[]{"I", "S", "S", 'I', "ingotVoid", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.voidPick, 1), new Object[]{"III", " S ", " S ", 'I', "ingotVoid", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.voidAxe, 1), new Object[]{"II", "SI", "S ", 'I', "ingotVoid", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.voidHoe, 1), new Object[]{"II", "S ", "S ", 'I', "ingotVoid", 'S', "stickWood"}), oreDictRecipe(new ItemStack(ItemsTC.voidSword, 1), new Object[]{"I", "I", "S", 'I', "ingotVoid", 'S', "stickWood"}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:mundanebaubles", false, oreDictRecipe(new ItemStack(ItemsTC.baubles, 1, 0), new Object[]{" S ", "S S", " I ", 'S', new ItemStack(Items.field_151007_F), 'I', "ingotBrass"}), oreDictRecipe(new ItemStack(ItemsTC.baubles, 1, 1), new Object[]{"NNN", "N N", "NNN", 'N', "nuggetBrass"}), oreDictRecipe(new ItemStack(ItemsTC.baubles, 1, 2), new Object[]{" L ", "L L", " I ", 'L', new ItemStack(Items.field_151116_aA), 'I', "ingotBrass"}), oreDictRecipe(new ItemStack(ItemsTC.baubles, 1, 9), new Object[]{" S ", "SGS", " I ", 'S', new ItemStack(Items.field_151007_F), 'G', new ItemStack(Items.field_151045_i), 'I', "ingotGold"}), oreDictRecipe(new ItemStack(ItemsTC.baubles, 1, 10), new Object[]{"NGN", "N N", "NNN", 'G', new ItemStack(Items.field_151045_i), 'N', "nuggetGold"}), oreDictRecipe(new ItemStack(ItemsTC.baubles, 1, 11), new Object[]{" L ", "LGL", " I ", 'L', new ItemStack(Items.field_151116_aA), 'G', new ItemStack(Items.field_151045_i), 'I', "ingotGold"}));
      ThaumcraftApi.addRecipeUnlinked("thaumcraft:mundanebaubles");
      CraftingManager.func_77594_a().func_180302_a(new RecipeTripleMeatTreat());
      CraftingManager.func_77594_a().func_180302_a(new RecipeMagicDust());
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:salismundus", true, new ShapelessOreRecipe(new ItemStack(ItemsTC.salisMundus), new Object[]{Items.field_151145_ak, Items.field_151054_z, Items.field_151137_ax, new ItemStack(ItemsTC.crystalEssence, 1, 32767), new ItemStack(ItemsTC.crystalEssence, 1, 32767), new ItemStack(ItemsTC.crystalEssence, 1, 32767)}));
      ThaumcraftApi.addRecipeUnlinked("thaumcraft:salismundus");
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.quicksilver), new Object[]{"#", '#', new ItemStack(BlocksTC.shimmerleaf)});
      CraftingManager.func_77594_a().func_92103_a(new ItemStack(Items.field_151065_br), new Object[]{"#", '#', new ItemStack(BlocksTC.cinderpearl)});
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:JarLabel", false, shapelessOreDictRecipe(new ItemStack(ItemsTC.label, 4, 0), new Object[]{"dyeBlack", "slimeball", Items.field_151121_aF, Items.field_151121_aF, Items.field_151121_aF, Items.field_151121_aF}));
      int count = 0;
      IRecipe[] jre = new IRecipe[Aspect.aspects.size()];

      for(Iterator var2 = Aspect.aspects.values().iterator(); var2.hasNext(); ++count) {
         Aspect aspect = (Aspect)var2.next();
         ItemStack essence = new ItemStack(ItemsTC.phial, 1, 1);
         ((IEssentiaContainerItem)((IEssentiaContainerItem)essence.func_77973_b())).setAspects(essence, (new AspectList()).add(aspect, 8));
         ItemStack output = new ItemStack(ItemsTC.label, 1, 1);
         ((IEssentiaContainerItem)((IEssentiaContainerItem)output.func_77973_b())).setAspects(output, (new AspectList()).add(aspect, 1));
         jre[count] = shapelessNBTOreRecipe(output, new Object[]{new ItemStack(ItemsTC.label), essence});
      }

      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:JarLabelEssence", false, jre);
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:JarLabelNull", false, shapelessOreDictRecipe(new ItemStack(ItemsTC.label), new Object[]{new ItemStack(ItemsTC.label, 1, 1)}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:PlankGreatwood", false, GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.plank, 4, 0), new Object[]{"W", 'W', new ItemStack(BlocksTC.log, 1, 0)}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:PlankSilverwood", false, GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.plank, 4, 1), new Object[]{"W", 'W', new ItemStack(BlocksTC.log, 1, 3)}));
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.stairsGreatwood, 4, 0), new Object[]{"K  ", "KK ", "KKK", 'K', new ItemStack(BlocksTC.plank, 1, 0)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.stairsSilverwood, 4, 0), new Object[]{"K  ", "KK ", "KKK", 'K', new ItemStack(BlocksTC.plank, 1, 1)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.stairsArcane, 4, 0), new Object[]{"K  ", "KK ", "KKK", 'K', new ItemStack(BlocksTC.stone, 1, 0)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.stairsArcaneBrick, 4, 0), new Object[]{"K  ", "KK ", "KKK", 'K', new ItemStack(BlocksTC.stone, 1, 1)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.stairsAncient, 4, 0), new Object[]{"K  ", "KK ", "KKK", 'K', new ItemStack(BlocksTC.stone, 1, 2)});
      oreDictRecipe(new ItemStack(BlocksTC.stone, 9, 0), new Object[]{"KKK", "KCK", "KKK", 'K', "stone", 'C', new ItemStack(ItemsTC.crystalEssence)});
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:arcane_stone", true, new ShapedOreRecipe(new ItemStack(BlocksTC.stone, 9, 0), new Object[]{"KKK", "KCK", "KKK", 'K', "stone", 'C', new ItemStack(ItemsTC.crystalEssence, 1, 32767)}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:arcane_brick", false, GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.stone, 4, 1), new Object[]{"KK", "KK", 'K', new ItemStack(BlocksTC.stone, 1, 0)}));
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.slabWood, 6, 0), new Object[]{"KKK", 'K', new ItemStack(BlocksTC.plank, 1, 0)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.slabWood, 6, 1), new Object[]{"KKK", 'K', new ItemStack(BlocksTC.plank, 1, 1)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.slabStone, 6, 0), new Object[]{"KKK", 'K', new ItemStack(BlocksTC.stone, 1, 0)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.slabStone, 6, 1), new Object[]{"KKK", 'K', new ItemStack(BlocksTC.stone, 1, 1)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.slabStone, 6, 2), new Object[]{"KKK", 'K', new ItemStack(BlocksTC.stone, 1, 2)});
      GameRegistry.addShapedRecipe(new ItemStack(BlocksTC.slabStone, 6, 3), new Object[]{"KKK", 'K', new ItemStack(BlocksTC.stone, 1, 4)});
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:phial", false, oreDictRecipe(new ItemStack(ItemsTC.phial, 8, 0), new Object[]{" C ", "G G", " G ", 'G', "blockGlass", 'C', Items.field_151119_aD}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:tablewood", false, oreDictRecipe(new ItemStack(BlocksTC.tableWood), new Object[]{"SSS", "W W", 'S', "slabWood", 'W', "plankWood"}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:tablestone", false, oreDictRecipe(new ItemStack(BlocksTC.tableStone), new Object[]{"SSS", "W W", 'S', new ItemStack(Blocks.field_150333_U), 'W', "stone"}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:scribetools", false, shapelessOreDictRecipe(new ItemStack(ItemsTC.scribingTools), new Object[]{new ItemStack(ItemsTC.phial, 1, 0), Items.field_151008_G, "dyeBlack"}), shapelessOreDictRecipe(new ItemStack(ItemsTC.scribingTools), new Object[]{Items.field_151069_bo, Items.field_151008_G, "dyeBlack"}), shapelessOreDictRecipe(new ItemStack(ItemsTC.scribingTools), new Object[]{new ItemStack(ItemsTC.scribingTools, 1, 32767), "dyeBlack"}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:GolemBell", false, oreDictRecipe(new ItemStack(ItemsTC.golemBell), new Object[]{" QQ", " QQ", "S  ", 'S', "stickWood", 'Q', "gemQuartz"}));
      ThaumcraftApi.addIRecipeToCatalog("thaumcraft:BrassBrace", false, oreDictRecipe(new ItemStack(ItemsTC.jarBrace, 2), new Object[]{"NSN", "S S", "NSN", 'N', "nuggetBrass", 'S', "stickWood"}));
   }

   private static void initializeSmelting() {
      GameRegistry.addSmelting(BlocksTC.oreCinnabar, new ItemStack(ItemsTC.quicksilver), 1.0F);
      GameRegistry.addSmelting(BlocksTC.oreAmber, new ItemStack(ItemsTC.amber), 1.0F);
      GameRegistry.addSmelting(BlocksTC.oreQuartz, new ItemStack(Items.field_151128_bU), 1.0F);
      GameRegistry.addSmelting(BlocksTC.log, new ItemStack(Items.field_151044_h, 1, 1), 0.5F);
      GameRegistry.addSmelting(new ItemStack(ItemsTC.clusters, 1, 0), new ItemStack(Items.field_151042_j, 2, 0), 1.0F);
      GameRegistry.addSmelting(new ItemStack(ItemsTC.clusters, 1, 1), new ItemStack(Items.field_151043_k, 2, 0), 1.0F);
      GameRegistry.addSmelting(new ItemStack(ItemsTC.clusters, 1, 6), new ItemStack(ItemsTC.quicksilver, 2, 0), 1.0F);
      GameRegistry.addSmelting(new ItemStack(ItemsTC.clusters, 1, 7), new ItemStack(Items.field_151128_bU, 2, 0), 1.0F);
      ThaumcraftApi.addSmeltingBonus("oreGold", new ItemStack(Items.field_151074_bl, 0, 0));
      ThaumcraftApi.addSmeltingBonus("oreIron", new ItemStack(ItemsTC.nuggets, 0, 0));
      ThaumcraftApi.addSmeltingBonus("oreCinnabar", new ItemStack(ItemsTC.nuggets, 0, 5));
      ThaumcraftApi.addSmeltingBonus("oreCopper", new ItemStack(ItemsTC.nuggets, 0, 1));
      ThaumcraftApi.addSmeltingBonus("oreTin", new ItemStack(ItemsTC.nuggets, 0, 2));
      ThaumcraftApi.addSmeltingBonus("oreSilver", new ItemStack(ItemsTC.nuggets, 0, 3));
      ThaumcraftApi.addSmeltingBonus("oreLead", new ItemStack(ItemsTC.nuggets, 0, 4));
      ThaumcraftApi.addSmeltingBonus("oreQuartz", new ItemStack(ItemsTC.nuggets, 0, 9));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 0), new ItemStack(ItemsTC.nuggets, 0, 0));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 1), new ItemStack(Items.field_151074_bl, 0, 0));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 6), new ItemStack(ItemsTC.nuggets, 0, 5));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 2), new ItemStack(ItemsTC.nuggets, 0, 1));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 3), new ItemStack(ItemsTC.nuggets, 0, 2));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 4), new ItemStack(ItemsTC.nuggets, 0, 3));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 5), new ItemStack(ItemsTC.nuggets, 0, 4));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(ItemsTC.clusters, 1, 7), new ItemStack(ItemsTC.nuggets, 0, 9));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.field_151082_bd), new ItemStack(ItemsTC.chunks, 0, 0));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.field_151076_bf), new ItemStack(ItemsTC.chunks, 0, 1));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.field_151147_al), new ItemStack(ItemsTC.chunks, 0, 2));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.field_151115_aP, 1, 32767), new ItemStack(ItemsTC.chunks, 0, 3));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.field_179558_bo), new ItemStack(ItemsTC.chunks, 0, 4));
      ThaumcraftApi.addSmeltingBonus(new ItemStack(Items.field_179561_bm), new ItemStack(ItemsTC.chunks, 0, 5));
   }

   static IRecipe oreDictRecipe(ItemStack res, Object[] params) {
      IRecipe rec = new ShapedOreRecipe(res, params);
      CraftingManager.func_77594_a().func_77592_b().add(rec);
      return rec;
   }

   static IRecipe shapelessOreDictRecipe(ItemStack res, Object[] params) {
      IRecipe rec = new ShapelessOreRecipe(res, params);
      CraftingManager.func_77594_a().func_77592_b().add(rec);
      return rec;
   }

   static IRecipe shapelessNBTOreRecipe(ItemStack res, Object[] params) {
      IRecipe rec = new ShapelessNBTOreRecipe(res, params);
      CraftingManager.func_77594_a().func_77592_b().add(rec);
      return rec;
   }

   public static void postAspects() {
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:hedge_2", false, new CrucibleRecipe("HEDGEALCHEMY@2", new ItemStack(Items.field_151016_H, 2, 0), new ItemStack(Items.field_151016_H), new AspectList(new ItemStack(Items.field_151016_H))), new CrucibleRecipe("HEDGEALCHEMY@2", new ItemStack(Items.field_151123_aH, 2, 0), new ItemStack(Items.field_151123_aH), new AspectList(new ItemStack(Items.field_151123_aH))), new CrucibleRecipe("HEDGEALCHEMY@2", new ItemStack(Items.field_151114_aO, 2, 0), "dustGlowstone", new AspectList(new ItemStack(Items.field_151114_aO))), new CrucibleRecipe("HEDGEALCHEMY@2", new ItemStack(Items.field_151100_aR, 2, 0), new ItemStack(Items.field_151100_aR, 1, 0), new AspectList(new ItemStack(Items.field_151100_aR))));
      ThaumcraftApi.addCrucibleRecipe("thaumcraft:hedge_3", false, new CrucibleRecipe("HEDGEALCHEMY@3", new ItemStack(Items.field_151119_aD, 1, 0), new ItemStack(Blocks.field_150346_d), (new AspectList(new ItemStack(Items.field_151119_aD, 1, 0))).remove(new AspectList(new ItemStack(Blocks.field_150346_d)))), new CrucibleRecipe("HEDGEALCHEMY@3", new ItemStack(Items.field_151007_F), new ItemStack(Items.field_151015_O), (new AspectList(new ItemStack(Items.field_151007_F))).remove(new AspectList(new ItemStack(Items.field_151015_O)))), new CrucibleRecipe("HEDGEALCHEMY@3", new ItemStack(Blocks.field_150321_G), new ItemStack(Items.field_151007_F), (new AspectList(new ItemStack(Blocks.field_150321_G))).remove(new AspectList(new ItemStack(Items.field_151007_F)))), new CrucibleRecipe("HEDGEALCHEMY@3", new ItemStack(Blocks.field_150341_Y), new ItemStack(Blocks.field_150347_e), (new AspectList(new ItemStack(Blocks.field_150341_Y))).remove(new AspectList(new ItemStack(Blocks.field_150347_e)))), new CrucibleRecipe("HEDGEALCHEMY@3", new ItemStack(Items.field_151129_at), new ItemStack(Items.field_151133_ar), new AspectList(new ItemStack(Items.field_151129_at))));
   }
}
