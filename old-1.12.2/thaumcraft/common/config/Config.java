package thaumcraft.common.config;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.common.entities.EntityFallingTaint;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;
import thaumcraft.common.entities.monster.EntityEldritchCrab;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.entities.monster.boss.EntityCultistLeader;
import thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;
import thaumcraft.common.entities.monster.cult.EntityCultistKnight;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thaumcraft.common.lib.potions.PotionBlurredVision;
import thaumcraft.common.lib.potions.PotionDeathGaze;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;
import thaumcraft.common.lib.potions.PotionSunScorned;
import thaumcraft.common.lib.potions.PotionThaumarhia;
import thaumcraft.common.lib.potions.PotionUnnaturalHunger;
import thaumcraft.common.lib.potions.PotionWarpWard;
import thaumcraft.common.lib.utils.CropUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.biomes.BiomeGenEerie;
import thaumcraft.common.world.biomes.BiomeGenEldritch;
import thaumcraft.common.world.biomes.BiomeGenMagicalForest;
import thaumcraft.common.world.biomes.BiomeHandler;

public class Config {
   public static Configuration config;
   public static final String CATEGORY_GRAPHICS = "Graphics";
   public static final String CATEGORY_ENCH = "Enchantments";
   public static final String CATEGORY_ENTITIES = "Entities";
   public static final String CATEGORY_BIOMES = "Biomes";
   public static final String CATEGORY_RESEARCH = "Research";
   public static final String CATEGORY_WORLD = "World_Generation";
   public static final String CATEGORY_REGEN = "World_Regeneration";
   public static final String CATEGORY_SPAWN = "Monster_Spawning";
   public static final String CATEGORY_RUNIC = "Runic_Shielding";
   public static int overworldDim = 0;
   public static int biomeMagicalForestID = 193;
   public static int biomeEerieID = 194;
   public static int biomeEldritchID = 195;
   public static int biomeMagicalForestWeight = 5;
   public static float taintSpreadRate = 1.0F;
   public static int taintSpreadArea = 32;
   public static boolean wuss = false;
   public static int dimensionOuterId = -42;
   public static boolean championMobs = true;
   public static int oreDensity = 100;
   public static int shieldRecharge = 2000;
   public static int shieldWait = 4000;
   public static int shieldCost = 1;
   public static boolean largeTagText = false;
   public static boolean colorBlind = false;
   public static boolean shaders = true;
   public static boolean nostress = false;
   public static boolean crooked = true;
   public static boolean showTags = false;
   public static boolean blueBiome = false;
   public static boolean dialBottom = false;
   public static boolean showGolemEmotes = true;
   public static int nodeRefresh = 10;
   public static final float auraSize = 4.0F;
   public static boolean genAura = true;
   public static boolean genStructure = true;
   public static boolean genCinnibar = true;
   public static boolean genQuartz = true;
   public static boolean genAmber = true;
   public static boolean genCrystals = true;
   public static boolean genTrees = true;
   public static boolean genMagicForest = true;
   public static boolean regenAura = false;
   public static boolean regenStructure = false;
   public static boolean regenQuartz = false;
   public static boolean regenCinnibar = false;
   public static boolean regenAmber = false;
   public static boolean regenCrystals = false;
   public static boolean regenTrees = false;
   public static String regenKey = "DEFAULT";
   public static boolean allowCheatSheet = true;
   public static boolean golemChestInteract = true;
   public static int nodeRarity = 33;
   public static int specialNodeRarity = 10;
   public static int chargeBarPos = 0;
   public static int researchDifficulty = 0;
   public static boolean CallowCheatSheet = true;
   public static boolean ChardNode = true;
   public static boolean Cwuss = false;
   public static int CresearchDifficulty = 0;
   public static boolean spawnAngryZombie = true;
   public static boolean spawnFireBat = true;
   public static boolean spawnTaintacle = true;
   public static boolean spawnWisp = true;
   public static boolean spawnPech = true;
   public static boolean spawnElder = true;
   public static ArrayList<Aspect> aspectOrder = new ArrayList();
   public static boolean foundCopperIngot = false;
   public static boolean foundTinIngot = false;
   public static boolean foundSilverIngot = false;
   public static boolean foundLeadIngot = false;
   public static boolean foundCopperOre = false;
   public static boolean foundTinOre = false;
   public static boolean foundSilverOre = false;
   public static boolean foundLeadOre = false;
   public static boolean isHalloween;

   public static void initialize(File file) {
      config = new Configuration(file);
      config.addCustomCategoryComment("Graphics", "Graphics");
      config.addCustomCategoryComment("Enchantments", "Custom enchantments");
      config.addCustomCategoryComment("Monster_Spawning", "Will these mobs spawn");
      config.addCustomCategoryComment("Research", "Various research related things.");
      config.addCustomCategoryComment("World_Generation", "Settings to turn certain world-gen on or off.");
      config.addCustomCategoryComment("World_Regeneration", "If a chunk is encountered that skipped TC worldgen, then the game will attempt to regenerate certain world features if they are set to true. CAUTION: Best used for worlds created before you added this mod, and only if you know what you are doing. Backups are advised.");
      config.addCustomCategoryComment("Biomes", "Biomes and effects");
      config.addCustomCategoryComment("Runic_Shielding", "Runic Shielding");
      config.load();
      syncConfigurable();
      Property mfcp = config.get("Biomes", "magical_forest_biome_weight", 3);
      mfcp.setComment("higher values increases number of magical forest biomes. If you are using biome addon mods you probably want to increase this weight quite a bit");
      biomeMagicalForestWeight = mfcp.getInt();
      Property biomeMFProp = config.get("Biomes", "biome_magical_forest", biomeMagicalForestID);
      biomeMFProp.setComment("Magical Forest biome id");
      biomeMagicalForestID = biomeMFProp.getInt();
      if (Biome.func_150568_d(biomeMagicalForestID) != null) {
         biomeMagicalForestID = BiomeHandler.getFirstFreeBiomeSlot(biomeMagicalForestID);
         biomeMFProp.set(biomeMagicalForestID);
      }

      try {
         Biome.func_185354_a(biomeMagicalForestID, "magical_forest", new BiomeGenMagicalForest((new BiomeProperties("Magical Forest")).func_185398_c(0.2F).func_185400_d(0.3F).func_185410_a(0.8F).func_185395_b(0.4F)));
         BiomeHandler.MAGICAL_FOREST = Biome.func_150568_d(biomeMagicalForestID);
      } catch (Exception var9) {
         Thaumcraft.log.fatal("Could not register Magical Forest Biome");
      }

      Property biomeEerieProp = config.get("Biomes", "biome_eerie", biomeEerieID);
      biomeEerieProp.setComment("Eerie biome id");
      biomeEerieID = biomeEerieProp.getInt();
      if (Biome.func_150568_d(biomeEerieID) != null) {
         biomeEerieID = BiomeHandler.getFirstFreeBiomeSlot(biomeEerieID);
         biomeEerieProp.set(biomeEerieID);
      }

      try {
         Biome.func_185354_a(biomeEerieID, "eerie", new BiomeGenEerie((new BiomeProperties("Eerie")).func_185398_c(0.125F).func_185400_d(0.4F).func_185410_a(0.8F).func_185396_a()));
         BiomeHandler.EERIE = Biome.func_150568_d(biomeEerieID);
      } catch (Exception var8) {
         Thaumcraft.log.fatal("Could not register Eerie Biome");
      }

      Property biomeEldritchProp = config.get("Biomes", "biome_eldritch", biomeEldritchID);
      biomeEldritchProp.setComment("Eldritch Lands biome id");
      biomeEldritchID = biomeEldritchProp.getInt();
      if (Biome.func_150568_d(biomeEldritchID) != null) {
         biomeEldritchID = BiomeHandler.getFirstFreeBiomeSlot(biomeEldritchID);
         biomeEldritchProp.set(biomeEldritchID);
      }

      try {
         Biome.func_185354_a(biomeEldritchID, "eldritch", new BiomeGenEldritch((new BiomeProperties("Outer Lands")).func_185398_c(0.125F).func_185400_d(0.15F).func_185410_a(0.8F).func_185395_b(0.2F)));
         BiomeHandler.ELDRITCH = Biome.func_150568_d(biomeEldritchID);
      } catch (Exception var7) {
         Thaumcraft.log.fatal("Could not register Eldritch Lands Biome");
      }

      Property dimEldritch = config.get("Biomes", "outer_lands_dim", dimensionOuterId);
      dimensionOuterId = dimEldritch.getInt();
      Property mdim = config.get("Biomes", "main_dim", overworldDim);
      mdim.setComment("The dimension considered to be your 'overworld'. Certain TC structures will only spawn in this dim.");
      overworldDim = mdim.getInt();
      config.save();
      MinecraftForge.EVENT_BUS.register(Config.ConfigChangeListener.class);
   }

   public static void save() {
      config.save();
   }

   public static void postInitPotions() {
      PotionFluxTaint.instance = new PotionFluxTaint(true, 6697847);
      PotionVisExhaust.instance = new PotionVisExhaust(true, 6702199);
      PotionInfectiousVisExhaust.instance = new PotionInfectiousVisExhaust(true, 6706551);
      PotionUnnaturalHunger.instance = new PotionUnnaturalHunger(true, 4482611);
      PotionWarpWard.instance = new PotionWarpWard(false, 14742263);
      PotionDeathGaze.instance = new PotionDeathGaze(true, 6702131);
      PotionBlurredVision.instance = new PotionBlurredVision(true, 8421504);
      PotionSunScorned.instance = new PotionSunScorned(true, 16308330);
      PotionThaumarhia.instance = new PotionThaumarhia(true, 6702199);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("fluxTaint"), PotionFluxTaint.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("visExhaust"), PotionVisExhaust.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("infectiousVisExhaust"), PotionInfectiousVisExhaust.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("unnaturalHunger"), PotionUnnaturalHunger.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("warpWard"), PotionWarpWard.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("deathGaze"), PotionDeathGaze.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("blurredVision"), PotionBlurredVision.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("sunScorned"), PotionSunScorned.instance);
      Potion.field_188414_b.func_177775_a(-1, new ResourceLocation("thaumarhia"), PotionThaumarhia.instance);
   }

   public static void syncConfigurable() {
      Property cbp = config.get("Graphics", "charge_bar_pos", chargeBarPos);
      cbp.setComment("The location of the HUD for item vis charge levels. 0 = left (default), 1 = right, 2 = top");
      chargeBarPos = MathHelper.func_76125_a(cbp.getInt(), 0, 2);
      Property cb = config.get("Graphics", "color_blind", colorBlind);
      cb.setComment("Setting this to true will make certain colors higher contrast or darker to prevent them from being 'invisible' to color blind people.");
      colorBlind = cb.getBoolean(false);
      Property ltt = config.get("Graphics", "large_tag_text", largeTagText);
      ltt.setComment("Setting this to true will make the amount text in aspect tags twice as large. Useful for certain resolutions and custom fonts.");
      largeTagText = ltt.getBoolean(false);
      Property shad = config.get("Graphics", "shaders", shaders);
      shad.setComment("This setting will disable certain thaumcraft shaders for those who experience FPS drops.");
      shaders = shad.getBoolean(false);
      Property nost = config.get("Graphics", "no_stress", nostress);
      nost.setComment("Set to true to disable anxiety triggers like the heartbeat sound.");
      nostress = nost.getBoolean(false);
      Property ocd = config.get("Graphics", "crooked", crooked);
      ocd.setComment("Hate crooked labels, kittens, puppies and all things awesome? If yes, set this to false.");
      crooked = ocd.getBoolean(true);
      Property dbp = config.get("Graphics", "wand_dial_bottom", dialBottom);
      dbp.setComment("Set to true to have the wand dial display in the bottom left instead of the top left.");
      dialBottom = dbp.getBoolean(false);
      Property showtags = config.get("Graphics", "display_aspects", false);
      showtags.setComment("Item aspects are hidden by default and pressing shift reveals them. Changing this setting to 'true' will reverse this behaviour and always display aspects unless shift is pressed.");
      showTags = showtags.getBoolean(false);
      Property blueb = config.get("Graphics", "blue_magical_forest", blueBiome);
      blueb.setComment("Set this to true to get the old blue magical forest back.");
      blueBiome = blueb.getBoolean(false);
      Property sge = config.get("Graphics", "show_golem_emotes", showGolemEmotes);
      sge.setComment("Will golems display emote particles if they recieve orders or encounter problems");
      showGolemEmotes = sge.getBoolean(true);
      genAura = config.get("World_Generation", "generate_aura_nodes", true).getBoolean(true);
      genStructure = config.get("World_Generation", "generate_structures", true).getBoolean(true);
      genCinnibar = config.get("World_Generation", "generate_cinnibar_ore", true).getBoolean(true);
      genAmber = config.get("World_Generation", "generate_amber_ore", true).getBoolean(true);
      genQuartz = config.get("World_Generation", "generate_quartz_ore", true).getBoolean(true);
      genCrystals = config.get("World_Generation", "generate_vis_crystals", true).getBoolean(true);
      genTrees = config.get("World_Generation", "generate_trees", true).getBoolean(true);
      Property gt = config.get("World_Generation", "generate_magic_forest", genMagicForest);
      gt.setComment("Can magic forest biomes generate at worldgen");
      genMagicForest = gt.getBoolean(true);
      Property nodRare = config.get("World_Generation", "node_rarity", nodeRarity);
      nodRare.setComment("How rare nodes are in the world. The number means there will be (on average) one node per N chunks.");
      nodeRarity = nodRare.getInt();
      Property nodSpec = config.get("World_Generation", "special_node_rarity", specialNodeRarity);
      nodSpec.setComment("The chance of a node being special (pure, dark, unstable, etc.). The number means roughly 1 in N nodes will be special, so setting the number to 5 will mean 1 in 5 nodes may be special.");
      specialNodeRarity = nodSpec.getInt();
      if (specialNodeRarity < 3) {
         specialNodeRarity = 3;
      }

      Property regKey = config.get("World_Regeneration", "regen_key", "DEFAULT");
      regKey.setComment("This key is used to keep track of which chunk have been generated/regenerated. Changing it will cause the regeneration code to run again, so only change it if you want it to happen. Useful to regen only one world feature at a time.");
      regenKey = regKey.getString();
      regenAura = config.get("World_Regeneration", "aura_nodes", false).getBoolean(false);
      regenStructure = config.get("World_Regeneration", "structures", false).getBoolean(false);
      regenCinnibar = config.get("World_Regeneration", "cinnibar_ore", false).getBoolean(false);
      regenAmber = config.get("World_Regeneration", "amber_ore", false).getBoolean(false);
      regenQuartz = config.get("World_Regeneration", "Quartz_ore", false).getBoolean(false);
      regenCrystals = config.get("World_Regeneration", "vis_crystals", false).getBoolean(false);
      regenTrees = config.get("World_Regeneration", "trees", false).getBoolean(false);
      Property resDif = config.get("Research", "research_difficulty", 0);
      resDif.setComment("0 = normal, -1 = easy (all research items are directly purchased with levels), 1 = Hard (all research items need to be solved via the research table)");
      CresearchDifficulty = researchDifficulty = resDif.getInt(0);
      Property resAmt = config.get("Research", "research_amount", 1);
      resAmt.setComment("This setting is useful for cooperative or team play. When a research is completed this is the amount of discoveries that will be created (default 1, max 64) Setting it less than 1 will create a discovery that will not be used up when learned.");
      if (resAmt.getInt(0) > 64) {
         resAmt.set(64);
      }

      if (resAmt.getInt(0) < 0) {
         resAmt.set(0);
      }

      spawnAngryZombie = config.get("Monster_Spawning", "spawn_angry_zombies", true).getBoolean(true);
      spawnFireBat = config.get("Monster_Spawning", "spawn_fire_bats", true).getBoolean(true);
      spawnWisp = config.get("Monster_Spawning", "spawn_wisps", true).getBoolean(true);
      spawnTaintacle = config.get("Monster_Spawning", "spawn_taintacles", true).getBoolean(true);
      spawnPech = config.get("Monster_Spawning", "spawn_pechs", true).getBoolean(true);
      spawnElder = config.get("Monster_Spawning", "spawn_eldercreatures", true).getBoolean(true);
      Property cm = config.get("Monster_Spawning", "champion_mobs", championMobs);
      cm.setComment("Setting this to false will disable spawning champion mobs. Even when false they will still have a greatly reduced chance of spawning in certain dangerous places.");
      championMobs = cm.getBoolean(true);
      Property wm = config.get("general", "wuss_mode", wuss);
      wm.setComment("Setting this to true disables Warp, Taint spread and similar mechanics. You wuss.");
      Cwuss = wuss = wm.getBoolean(false);
      Property cheatsheet = config.get("general", "allow_cheat_sheet", false);
      cheatsheet.setComment("Enables a version of the Thauminomicon in creative mode that grants you all the research when you first use it.");
      CallowCheatSheet = allowCheatSheet = cheatsheet.getBoolean(false);
      Property ts = config.get("general", "taint_spread", (int)(taintSpreadRate * 100.0F));
      ts.setComment("The % chance of taint fibres spreading on a block tick. Setting this to 0 will effectively stop taint fibre spread. Default 100%");
      taintSpreadRate = (float)ts.getInt() / 100.0F;
      Property tsa = config.get("general", "taint_spread_area", taintSpreadArea);
      tsa.setComment("The range at which taint can spread from a taint seed. This value is only a base and will be modified by flux levels. (valid values are 8-256, default 32)");
      taintSpreadArea = MathHelper.func_76125_a(tsa.getInt(), 8, 256);
      Property od = config.get("World_Generation", "ore_density", oreDensity);
      od.setComment("The % of normal ore amounts that will be spawned. For example 50 will spawn half the ores while 200 will spawn double. Default 100");
      oreDensity = Math.max(1, od.getInt());
      Property rss = config.get("Runic_Shielding", "runic_shield_recharge_speed", shieldRecharge);
      rss.setComment("How many milliseconds passes between runic shielding recharge ticks. Lower values equals faster recharge. Minimum of 500.");
      shieldRecharge = Math.max(500, rss.getInt());
      Property rsd = config.get("Runic_Shielding", "runic_shield_recharge_delay", shieldWait);
      rsd.setComment("How many milliseconds passes after a shield has been reduced to zero before it can start recharging again. Minimum of 0.");
      shieldWait = Math.max(0, rsd.getInt());
      Property rsc = config.get("Runic_Shielding", "runic_shield_cost", shieldCost);
      rsc.setComment("How much aer and terra vis it costs to reacharge a single unit of shielding. Minimum of 0.");
      shieldCost = Math.max(0, rsc.getInt());
   }

   public static void postInitLoot() {
      int COMMON = false;
      int UNCOMMON = true;
      int RARE = true;
      new Random(System.currentTimeMillis());
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151074_bl, 1), 2500, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151074_bl, 2), 2250, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151074_bl, 3), 2000, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.salisMundus), 3, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.salisMundus), 6, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.salisMundus), 9, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_185161_cS), 5, 0, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151111_aL), 5, 0, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151106_aX), 5, 0, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_185160_cR), 5, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_185160_cR), 10, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 0), 1, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 0), 3, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1), 1, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 0), 9, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 1), 3, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.primordialPearl, 2), 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151156_bN), 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151045_i), 10, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151045_i), 50, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151166_bC), 15, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151166_bC), 75, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151043_k), 100, 0, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151079_bi), 100, 0, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.amuletVis, 1, 0), 6, 1, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 0), 10, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 1), 10, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 2), 10, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 3), 5, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 4), 5, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 5), 5, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(ItemsTC.baubles, 1, 6), 5, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151062_by), 5, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151062_by), 10, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151062_by), 20, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151153_ao, 1, 1), 1, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151153_ao, 1, 1), 2, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151153_ao, 1, 1), 3, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151153_ao, 1, 0), 3, 0);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151153_ao, 1, 0), 6, 1);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151153_ao, 1, 0), 9, 2);
      ThaumcraftApi.addLootBagItem(new ItemStack(Items.field_151122_aG), 10, 0, 1, 2);
      Iterator var4 = PotionType.field_185176_a.iterator();

      while(var4.hasNext()) {
         PotionType pt = (PotionType)var4.next();
         ThaumcraftApi.addLootBagItem(PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), pt), 2, 0, 1, 2);
         ThaumcraftApi.addLootBagItem(PotionUtils.func_185188_a(new ItemStack(Items.field_185155_bH), pt), 2, 0, 1, 2);
         ThaumcraftApi.addLootBagItem(PotionUtils.func_185188_a(new ItemStack(Items.field_185156_bI), pt), 2, 1, 2);
      }

      ItemStack[] var10000 = new ItemStack[]{new ItemStack(ItemsTC.lootBag, 1, 0), new ItemStack(ItemsTC.ingots), new ItemStack(ItemsTC.amber)};
      var10000 = new ItemStack[]{new ItemStack(ItemsTC.lootBag, 1, 1), new ItemStack(ItemsTC.baubles, 1, 0), new ItemStack(ItemsTC.baubles, 1, 1), new ItemStack(ItemsTC.baubles, 1, 2)};
      var10000 = new ItemStack[]{new ItemStack(ItemsTC.lootBag, 1, 2), new ItemStack(ItemsTC.thaumonomicon), new ItemStack(ItemsTC.thaumiumSword), new ItemStack(ItemsTC.thaumiumAxe), new ItemStack(ItemsTC.thaumiumHoe), new ItemStack(ItemsTC.thaumiumPick), new ItemStack(ItemsTC.baubles, 1, 3), new ItemStack(ItemsTC.baubles, 1, 4), new ItemStack(ItemsTC.baubles, 1, 5), new ItemStack(ItemsTC.baubles, 1, 6), new ItemStack(ItemsTC.amuletVis, 1, 0)};
   }

   public static void postInitModCompatibility() {
      String[] ores = OreDictionary.getOreNames();
      String[] var1 = ores;
      int var2 = ores.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String ore = var1[var3];
         if (ore != null) {
            Iterator var5;
            ItemStack is;
            if (ore.equals("oreCopper") && OreDictionary.getOres(ore, false).size() > 0) {
               foundCopperOre = true;
               var5 = OreDictionary.getOres(ore, false).iterator();

               while(var5.hasNext()) {
                  is = (ItemStack)var5.next();
                  Utils.addSpecialMiningResult(is, new ItemStack(ItemsTC.clusters, 1, 2), 1.0F);
               }
            }

            if (ore.equals("oreTin") && OreDictionary.getOres(ore, false).size() > 0) {
               foundTinOre = true;
               var5 = OreDictionary.getOres(ore, false).iterator();

               while(var5.hasNext()) {
                  is = (ItemStack)var5.next();
                  Utils.addSpecialMiningResult(is, new ItemStack(ItemsTC.clusters, 1, 3), 1.0F);
               }
            }

            if (ore.equals("oreSilver") && OreDictionary.getOres(ore, false).size() > 0) {
               foundSilverOre = true;
               var5 = OreDictionary.getOres(ore, false).iterator();

               while(var5.hasNext()) {
                  is = (ItemStack)var5.next();
                  Utils.addSpecialMiningResult(is, new ItemStack(ItemsTC.clusters, 1, 4), 1.0F);
               }
            }

            if (ore.equals("oreLead") && OreDictionary.getOres(ore, false).size() > 0) {
               foundLeadOre = true;
               var5 = OreDictionary.getOres(ore, false).iterator();

               while(var5.hasNext()) {
                  is = (ItemStack)var5.next();
                  Utils.addSpecialMiningResult(is, new ItemStack(ItemsTC.clusters, 1, 5), 1.0F);
               }
            }

            ItemStack is;
            boolean first;
            Iterator var9;
            if (ore.equals("ingotCopper")) {
               first = true;
               var9 = OreDictionary.getOres(ore, false).iterator();

               while(var9.hasNext()) {
                  is = (ItemStack)var9.next();
                  if (is.field_77994_a > 1) {
                     is.field_77994_a = 1;
                  }

                  foundCopperIngot = true;
                  CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 1), new Object[]{"#", '#', is});
                  if (first) {
                     first = false;
                     FurnaceRecipes.func_77602_a().func_151394_a(new ItemStack(ItemsTC.clusters, 1, 2), new ItemStack(is.func_77973_b(), 2, is.func_77952_i()), 1.0F);
                     ConfigRecipes.oreDictRecipe(is, new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.nuggets, 1, 1)});
                  }
               }
            } else if (ore.equals("ingotTin")) {
               first = true;
               var9 = OreDictionary.getOres(ore, false).iterator();

               while(var9.hasNext()) {
                  is = (ItemStack)var9.next();
                  if (is.field_77994_a > 1) {
                     is.field_77994_a = 1;
                  }

                  foundTinIngot = true;
                  CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 2), new Object[]{"#", '#', is});
                  if (first) {
                     first = false;
                     FurnaceRecipes.func_77602_a().func_151394_a(new ItemStack(ItemsTC.clusters, 1, 3), new ItemStack(is.func_77973_b(), 2, is.func_77952_i()), 1.0F);
                     ConfigRecipes.oreDictRecipe(is, new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.nuggets, 1, 2)});
                  }
               }
            } else if (ore.equals("ingotSilver")) {
               first = true;
               var9 = OreDictionary.getOres(ore, false).iterator();

               while(var9.hasNext()) {
                  is = (ItemStack)var9.next();
                  if (is.field_77994_a > 1) {
                     is.field_77994_a = 1;
                  }

                  foundSilverIngot = true;
                  CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 3), new Object[]{"#", '#', is});
                  if (first) {
                     first = false;
                     FurnaceRecipes.func_77602_a().func_151394_a(new ItemStack(ItemsTC.clusters, 1, 4), new ItemStack(is.func_77973_b(), 2, is.func_77952_i()), 1.0F);
                     ConfigRecipes.oreDictRecipe(is, new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.nuggets, 1, 3)});
                  }
               }
            } else if (ore.equals("ingotLead")) {
               first = true;
               var9 = OreDictionary.getOres(ore, false).iterator();

               while(var9.hasNext()) {
                  is = (ItemStack)var9.next();
                  if (is.field_77994_a > 1) {
                     is.field_77994_a = 1;
                  }

                  foundLeadIngot = true;
                  CraftingManager.func_77594_a().func_92103_a(new ItemStack(ItemsTC.nuggets, 9, 4), new Object[]{"#", '#', is});
                  if (first) {
                     first = false;
                     FurnaceRecipes.func_77602_a().func_151394_a(new ItemStack(ItemsTC.clusters, 1, 5), new ItemStack(is.func_77973_b(), 2, is.func_77952_i()), 1.0F);
                     ConfigRecipes.oreDictRecipe(is, new Object[]{"###", "###", "###", '#', new ItemStack(ItemsTC.nuggets, 1, 4)});
                  }
               }
            }
         }
      }

      Thaumcraft.log.info("Adding entities to MFR safari net blacklist.");
      registerSafariNetBlacklist(EntityOwnedConstruct.class);
      registerSafariNetBlacklist(EntityFallingTaint.class);
      registerSafariNetBlacklist(EntityWisp.class);
      registerSafariNetBlacklist(EntityPech.class);
      registerSafariNetBlacklist(EntityEldritchGuardian.class);
      registerSafariNetBlacklist(EntityEldritchWarden.class);
      registerSafariNetBlacklist(EntityEldritchGolem.class);
      registerSafariNetBlacklist(EntityCultistCleric.class);
      registerSafariNetBlacklist(EntityCultistKnight.class);
      registerSafariNetBlacklist(EntityCultistLeader.class);
      registerSafariNetBlacklist(EntityCultistPortalGreater.class);
      registerSafariNetBlacklist(EntityCultistPortalLesser.class);
      registerSafariNetBlacklist(EntityEldritchCrab.class);
      registerSafariNetBlacklist(EntityInhabitedZombie.class);
   }

   public static void registerSafariNetBlacklist(Class<?> blacklistedEntity) {
      try {
         Class<?> registry = Class.forName("powercrystals.minefactoryreloaded.MFRRegistry");
         if (registry != null) {
            Method reg = registry.getMethod("registerSafariNetBlacklist", Class.class);
            reg.invoke(registry, blacklistedEntity);
         }
      } catch (Exception var3) {
      }

   }

   public static void postInitMisc() {
      Iterator var0 = GameData.getItemRegistry().typeSafeIterable().iterator();

      while(var0.hasNext()) {
         Item item = (Item)var0.next();
         if (item != null && item instanceof IPlantable) {
            IBlockState bs = ((IPlantable)item).getPlant((IBlockAccess)null, (BlockPos)null);
            if (bs != null) {
               ThaumcraftApi.registerSeed(bs.func_177230_c(), new ItemStack(item));
            }
         }
      }

      CropUtils.addStandardCrop((Block)Blocks.field_150440_ba, 32767);
      CropUtils.addStandardCrop((Block)Blocks.field_150423_aK, 32767);
      CropUtils.addStackedCrop((Block)Blocks.field_150436_aH, 32767);
      CropUtils.addStackedCrop((Block)Blocks.field_150434_aF, 32767);
      CropUtils.addStandardCrop((Block)Blocks.field_150388_bm, 3);
      ThaumcraftApi.registerSeed(Blocks.field_150375_by, new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BROWN.func_176767_b()));
      Utils.addSpecialMiningResult(new ItemStack(Blocks.field_150366_p), new ItemStack(ItemsTC.clusters, 1, 0), 1.0F);
      Utils.addSpecialMiningResult(new ItemStack(Blocks.field_150352_o), new ItemStack(ItemsTC.clusters, 1, 1), 1.0F);
      Utils.addSpecialMiningResult(new ItemStack(BlocksTC.oreCinnabar), new ItemStack(ItemsTC.clusters, 1, 6), 1.0F);
      Utils.addSpecialMiningResult(new ItemStack(Items.field_151128_bU), new ItemStack(ItemsTC.clusters, 1, 7), 1.0F);
      Collection<Aspect> pa = Aspect.aspects.values();
      Iterator var4 = pa.iterator();

      while(var4.hasNext()) {
         Aspect aspect = (Aspect)var4.next();
         aspectOrder.add(aspect);
      }

   }

   public static class ConfigChangeListener {
      @SubscribeEvent
      public void onConfigChanged(OnConfigChangedEvent eventArgs) {
         if (eventArgs.getModID().equals("thaumcraft")) {
            Config.syncConfigurable();
            if (Config.config != null && Config.config.hasChanged()) {
               Config.save();
            }
         }

      }
   }
}
