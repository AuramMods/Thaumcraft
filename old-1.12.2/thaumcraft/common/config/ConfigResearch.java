package thaumcraft.common.config;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ScanBlock;
import thaumcraft.api.research.ScanBlockState;
import thaumcraft.api.research.ScanEntity;
import thaumcraft.api.research.ScanItem;
import thaumcraft.api.research.ScanMaterial;
import thaumcraft.api.research.ScanOreDictionary;
import thaumcraft.api.research.ScanningManager;
import thaumcraft.api.research.theorycraft.AidBookshelf;
import thaumcraft.api.research.theorycraft.CardAnalyze;
import thaumcraft.api.research.theorycraft.CardBalance;
import thaumcraft.api.research.theorycraft.CardExperimentation;
import thaumcraft.api.research.theorycraft.CardInspired;
import thaumcraft.api.research.theorycraft.CardNotation;
import thaumcraft.api.research.theorycraft.CardPonder;
import thaumcraft.api.research.theorycraft.CardReject;
import thaumcraft.api.research.theorycraft.CardRethink;
import thaumcraft.api.research.theorycraft.CardStudy;
import thaumcraft.api.research.theorycraft.TheorycraftManager;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityEldritchCrab;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityFireBat;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thaumcraft.common.entities.monster.tainted.EntityTaintSwarm;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanEnchantment;
import thaumcraft.common.lib.research.ScanGeneric;
import thaumcraft.common.lib.research.ScanPotion;
import thaumcraft.common.lib.research.theorycraft.AidBasicAlchemy;
import thaumcraft.common.lib.research.theorycraft.AidBasicArtifice;
import thaumcraft.common.lib.research.theorycraft.AidBasicEldritch;
import thaumcraft.common.lib.research.theorycraft.AidBasicGolemancy;
import thaumcraft.common.lib.research.theorycraft.AidBasicThaumaturgy;
import thaumcraft.common.lib.research.theorycraft.AidBrainInAJar;
import thaumcraft.common.lib.research.theorycraft.AidGlyphedStone;
import thaumcraft.common.lib.research.theorycraft.AidPortal;
import thaumcraft.common.lib.research.theorycraft.CardAwareness;
import thaumcraft.common.lib.research.theorycraft.CardCalibrate;
import thaumcraft.common.lib.research.theorycraft.CardConcentrate;
import thaumcraft.common.lib.research.theorycraft.CardCurio;
import thaumcraft.common.lib.research.theorycraft.CardDarkWhispers;
import thaumcraft.common.lib.research.theorycraft.CardFocus;
import thaumcraft.common.lib.research.theorycraft.CardGlyphs;
import thaumcraft.common.lib.research.theorycraft.CardMindOverMatter;
import thaumcraft.common.lib.research.theorycraft.CardPortal;
import thaumcraft.common.lib.research.theorycraft.CardReactions;
import thaumcraft.common.lib.research.theorycraft.CardRealization;
import thaumcraft.common.lib.research.theorycraft.CardRevelation;
import thaumcraft.common.lib.research.theorycraft.CardScripting;
import thaumcraft.common.lib.research.theorycraft.CardSculpting;
import thaumcraft.common.lib.research.theorycraft.CardSpellbinding;
import thaumcraft.common.lib.research.theorycraft.CardSynergy;
import thaumcraft.common.lib.research.theorycraft.CardSynthesis;
import thaumcraft.common.lib.research.theorycraft.CardTinker;

public class ConfigResearch {
   public static String[] TCCategories = new String[]{"BASICS", "THAUMATURGY", "ALCHEMY", "ARTIFICE", "GOLEMANCY", "ELDRITCH"};
   private static final ResourceLocation BACK_OVER = new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_over.png");
   public static ResearchCategory BASICS;
   public static ResearchCategory THAUMATURGY;
   public static ResearchCategory ALCHEMY;
   public static ResearchCategory ARTIFICE;
   public static ResearchCategory GOLEMANCY;
   public static ResearchCategory ELDRITCH;

   public static void init() {
      initCategories();
      initScannables();
      initTheorycraft();
      String[] var0 = TCCategories;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         String cat = var0[var2];
         ThaumcraftApi.registerResearchLocation(new ResourceLocation("thaumcraft", "research/" + cat.toLowerCase()));
      }

      ThaumcraftApi.registerResearchLocation(new ResourceLocation("thaumcraft", "research/scans"));
   }

   public static void postInit() {
      ResearchManager.parseAllResearch();
   }

   private static void initCategories() {
      BASICS = ResearchCategories.registerCategory("BASICS", (String)null, (new AspectList()).add(Aspect.PLANT, 5).add(Aspect.ORDER, 5).add(Aspect.ENTROPY, 5).add(Aspect.AIR, 5).add(Aspect.FIRE, 5).add(Aspect.EARTH, 3).add(Aspect.WATER, 5), new ResourceLocation("thaumcraft", "textures/items/thaumonomicon_cheat.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_1.jpg"), BACK_OVER);
      THAUMATURGY = ResearchCategories.registerCategory("THAUMATURGY", "UNLOCKTHAUMATURGY", (new AspectList()).add(Aspect.AURA, 20).add(Aspect.MAGIC, 20).add(Aspect.FLUX, 15).add(Aspect.CRYSTAL, 5).add(Aspect.COLD, 5), new ResourceLocation("thaumcraft", "textures/research/r_thaumaturgy.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_2.jpg"), BACK_OVER);
      ALCHEMY = ResearchCategories.registerCategory("ALCHEMY", "UNLOCKALCHEMY", (new AspectList()).add(Aspect.ALCHEMY, 30).add(Aspect.FLUX, 10).add(Aspect.MAGIC, 10).add(Aspect.LIFE, 5).add(Aspect.AVERSION, 5).add(Aspect.DESIRE, 5), new ResourceLocation("thaumcraft", "textures/research/r_crucible.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_3.jpg"), BACK_OVER);
      ARTIFICE = ResearchCategories.registerCategory("ARTIFICE", "UNLOCKARTIFICE", (new AspectList()).add(Aspect.MECHANISM, 10).add(Aspect.CRAFT, 10).add(Aspect.METAL, 10).add(Aspect.TOOL, 10).add(Aspect.ENERGY, 10).add(Aspect.LIGHT, 5).add(Aspect.FLIGHT, 5).add(Aspect.TRAP, 5), new ResourceLocation("thaumcraft", "textures/research/r_artifice.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_4.jpg"), BACK_OVER);
      GOLEMANCY = ResearchCategories.registerCategory("GOLEMANCY", "UNLOCKGOLEMANCY", (new AspectList()).add(Aspect.MAN, 20).add(Aspect.MOTION, 10).add(Aspect.MIND, 10).add(Aspect.MECHANISM, 10).add(Aspect.EXCHANGE, 5).add(Aspect.SENSES, 5).add(Aspect.BEAST, 5), new ResourceLocation("thaumcraft", "textures/research/r_golemancy.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_5.jpg"), BACK_OVER);
      ELDRITCH = ResearchCategories.registerCategory("ELDRITCH", "UNLOCKELDRITCH", (new AspectList()).add(Aspect.ELDRITCH, 20).add(Aspect.DARKNESS, 10).add(Aspect.MAGIC, 5).add(Aspect.MIND, 5).add(Aspect.VOID, 5).add(Aspect.DEATH, 5).add(Aspect.UNDEAD, 5), new ResourceLocation("thaumcraft", "textures/research/r_eldritch.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_6.jpg"), BACK_OVER);
   }

   private static void initScannables() {
      ScanningManager.addScannableThing(new ScanGeneric());
      Iterator var0 = Enchantment.field_185264_b.func_148742_b().iterator();

      ResourceLocation loc;
      while(var0.hasNext()) {
         loc = (ResourceLocation)var0.next();
         Enchantment ench = (Enchantment)Enchantment.field_185264_b.func_82594_a(loc);
         ScanningManager.addScannableThing(new ScanEnchantment(ench));
      }

      var0 = Potion.field_188414_b.func_148742_b().iterator();

      while(var0.hasNext()) {
         loc = (ResourceLocation)var0.next();
         Potion pot = (Potion)Potion.field_188414_b.func_82594_a(loc);
         ScanningManager.addScannableThing(new ScanPotion(pot));
      }

      ScanningManager.addScannableThing(new ScanEntity("!Wisp", EntityWisp.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!ThaumSlime", EntityThaumicSlime.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!Firebat", EntityFireBat.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!Pech", EntityPech.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!BrainyZombie", EntityBrainyZombie.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!EldritchCrab", EntityEldritchCrab.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!EldritchCrab", EntityInhabitedZombie.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!CrimsonCultist", EntityCultist.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!EldritchGuardian", EntityEldritchGuardian.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!TaintCrawler", EntityTaintCrawler.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!Taintacle", EntityTaintacle.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!TaintSeed", EntityTaintSeed.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!TaintSwarm", EntityTaintSwarm.class, true));
      ScanningManager.addScannableThing(new ScanEntity("f_golem", EntityGolem.class, true));
      ScanningManager.addScannableThing(new ScanEntity("f_golem", EntityOwnedConstruct.class, true));
      ScanningManager.addScannableThing(new ScanEntity("f_SPIDER", EntitySpider.class, true));
      ScanningManager.addScannableThing(new ScanEntity("f_BAT", EntityBat.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!ORMOB", IEldritchMob.class, true));
      ScanningManager.addScannableThing(new ScanEntity("!ORBOSS", EntityThaumcraftBoss.class, true));
      ScanningManager.addScannableThing(new ScanBlockState("!ORBLOCK1", BlocksTC.stone.func_176203_a(2), true));
      ScanningManager.addScannableThing(new ScanBlockState("!ORBLOCK2", BlocksTC.stone.func_176203_a(4), true));
      ScanningManager.addScannableThing(new ScanBlockState("!ORBLOCK3", BlocksTC.stone.func_176203_a(11), true));
      ScanningManager.addScannableThing(new ScanBlock("ORE", new Block[]{BlocksTC.oreAmber, BlocksTC.oreCinnabar, BlocksTC.crystalAir, BlocksTC.crystalFire, BlocksTC.crystalWater, BlocksTC.crystalEarth, BlocksTC.crystalOrder, BlocksTC.crystalEntropy, BlocksTC.crystalTaint}));
      ScanningManager.addScannableThing(new ScanBlock("!OREAMBER", new Block[]{BlocksTC.oreAmber}));
      ScanningManager.addScannableThing(new ScanBlock("!ORECINNABAR", new Block[]{BlocksTC.oreCinnabar}));
      ScanningManager.addScannableThing(new ScanBlock("!ORECRYSTAL", new Block[]{BlocksTC.crystalAir, BlocksTC.crystalFire, BlocksTC.crystalWater, BlocksTC.crystalEarth, BlocksTC.crystalOrder, BlocksTC.crystalEntropy, BlocksTC.crystalTaint}));
      ScanningManager.addScannableThing(new ScanBlock("f_TELEPORT", new Block[]{Blocks.field_150427_aO, Blocks.field_150384_bq, Blocks.field_150378_br}));
      ScanningManager.addScannableThing(new ScanItem("f_TELEPORT", new ItemStack(Items.field_151079_bi)));
      ScanningManager.addScannableThing(new ScanEntity("f_TELEPORT", EntityEnderman.class, true));
      ScanningManager.addScannableThing(new ScanEntity("f_BRAIN", EntityBrainyZombie.class, true));
      ScanningManager.addScannableThing(new ScanItem("f_BRAIN", new ItemStack(ItemsTC.brain)));
      ScanningManager.addScannableThing(new ScanBlock("f_DISPENSER", new Block[]{Blocks.field_150367_z}));
      ScanningManager.addScannableThing(new ScanItem("f_DISPENSER", new ItemStack(Blocks.field_150367_z)));
      ScanningManager.addScannableThing(new ScanItem("f_MATCLAY", new ItemStack(Items.field_151119_aD)));
      ScanningManager.addScannableThing(new ScanBlock("f_MATCLAY", new Block[]{Blocks.field_150405_ch, Blocks.field_150406_ce}));
      ScanningManager.addScannableThing(new ScanMaterial("f_MATCLAY", new Material[]{Material.field_151571_B}));
      ScanningManager.addScannableThing(new ScanOreDictionary("f_MATIRON", new String[]{"oreIron", "ingotIron", "blockIron", "plateIron"}));
      ScanningManager.addScannableThing(new ScanOreDictionary("f_MATBRASS", new String[]{"ingotBrass", "blockBrass", "plateBrass"}));
      ScanningManager.addScannableThing(new ScanOreDictionary("f_MATTHAUMIUM", new String[]{"ingotThaumium", "blockThaumium", "plateThaumium"}));
      ScanningManager.addScannableThing(new ScanOreDictionary("f_MATVOID", new String[]{"ingotVoid", "blockVoid", "plateVoid"}));
   }

   private static void initTheorycraft() {
      TheorycraftManager.registerAid(new AidBookshelf());
      TheorycraftManager.registerAid(new AidBrainInAJar());
      TheorycraftManager.registerAid(new AidGlyphedStone());
      TheorycraftManager.registerAid(new AidPortal.AidPortalEnd());
      TheorycraftManager.registerAid(new AidPortal.AidPortalNether());
      TheorycraftManager.registerAid(new AidPortal.AidPortalCrimson());
      TheorycraftManager.registerAid(new AidBasicAlchemy());
      TheorycraftManager.registerAid(new AidBasicArtifice());
      TheorycraftManager.registerAid(new AidBasicThaumaturgy());
      TheorycraftManager.registerAid(new AidBasicGolemancy());
      TheorycraftManager.registerAid(new AidBasicEldritch());
      TheorycraftManager.registerCard(CardStudy.class);
      TheorycraftManager.registerCard(CardAnalyze.class);
      TheorycraftManager.registerCard(CardBalance.class);
      TheorycraftManager.registerCard(CardNotation.class);
      TheorycraftManager.registerCard(CardPonder.class);
      TheorycraftManager.registerCard(CardRethink.class);
      TheorycraftManager.registerCard(CardReject.class);
      TheorycraftManager.registerCard(CardExperimentation.class);
      TheorycraftManager.registerCard(CardCurio.class);
      TheorycraftManager.registerCard(CardInspired.class);
      TheorycraftManager.registerCard(CardConcentrate.class);
      TheorycraftManager.registerCard(CardReactions.class);
      TheorycraftManager.registerCard(CardSynthesis.class);
      TheorycraftManager.registerCard(CardCalibrate.class);
      TheorycraftManager.registerCard(CardMindOverMatter.class);
      TheorycraftManager.registerCard(CardTinker.class);
      TheorycraftManager.registerCard(CardFocus.class);
      TheorycraftManager.registerCard(CardAwareness.class);
      TheorycraftManager.registerCard(CardSpellbinding.class);
      TheorycraftManager.registerCard(CardSculpting.class);
      TheorycraftManager.registerCard(CardScripting.class);
      TheorycraftManager.registerCard(CardSynergy.class);
      TheorycraftManager.registerCard(CardDarkWhispers.class);
      TheorycraftManager.registerCard(CardGlyphs.class);
      TheorycraftManager.registerCard(CardPortal.class);
      TheorycraftManager.registerCard(CardRevelation.class);
      TheorycraftManager.registerCard(CardRealization.class);
   }

   private static void initBasicResearch() {
   }

   private static void initThaumaturgyResearch() {
   }

   private static void initArtificeResearch() {
      ThaumcraftApi.addWarpToItem(new ItemStack(BlocksTC.jarBrain), 1);
   }

   private static void initAlchemyResearch() {
   }

   private static void initGolemancyResearch() {
   }

   private static void initEldritchResearch() {
   }

   public static void checkPeriodicStuff(EntityPlayer player) {
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
      Biome biome = player.field_70170_p.func_180494_b(player.func_180425_c());
      if (!knowledge.isResearchKnown("m_hellandback") && BiomeDictionary.isBiomeOfType(biome, Type.NETHER)) {
         knowledge.addResearch("m_hellandback");
         knowledge.sync((EntityPlayerMP)player);
         player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.hellandback")));
      }

      if (!knowledge.isResearchKnown("m_endoftheworld") && BiomeDictionary.isBiomeOfType(biome, Type.END)) {
         knowledge.addResearch("m_endoftheworld");
         knowledge.sync((EntityPlayerMP)player);
         player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.endoftheworld")));
      }

      if (knowledge.isResearchKnown("UNLOCKTHAUMATURGY@1") && !knowledge.isResearchKnown("UNLOCKTHAUMATURGY@2")) {
         if (player.field_70163_u < 10.0D && !knowledge.isResearchKnown("m_deepdown")) {
            knowledge.addResearch("m_deepdown");
            knowledge.sync((EntityPlayerMP)player);
            player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.deepdown")));
         }

         if (player.field_70163_u > (double)player.func_130014_f_().func_72940_L() * 0.4D && !knowledge.isResearchKnown("m_uphigh")) {
            knowledge.addResearch("m_uphigh");
            knowledge.sync((EntityPlayerMP)player);
            player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.uphigh")));
         }

         if (!knowledge.isResearchKnown("m_finddesert") && (BiomeDictionary.isBiomeOfType(biome, Type.HOT) || BiomeDictionary.isBiomeOfType(biome, Type.DRY))) {
            knowledge.addResearch("m_finddesert");
            knowledge.sync((EntityPlayerMP)player);
            player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.finddesert")));
         }

         if (!knowledge.isResearchKnown("m_findocean") && BiomeDictionary.isBiomeOfType(biome, Type.OCEAN)) {
            knowledge.addResearch("m_findocean");
            knowledge.sync((EntityPlayerMP)player);
            player.func_145747_a(new TextComponentString(TextFormatting.DARK_PURPLE + I18n.func_74838_a("got.findocean")));
         }
      }

      StatisticsManagerServer sms = player.func_184102_h().func_184103_al().func_152602_a(player);
      if (sms != null) {
         if (!knowledge.isResearchKnown("m_walker") && sms.func_77444_a(StatList.field_188100_j) > 160000) {
            knowledge.addResearch("m_walker");
            knowledge.sync((EntityPlayerMP)player);
         }

         if (!knowledge.isResearchKnown("m_runner") && sms.func_77444_a(StatList.field_188102_l) > 80000) {
            knowledge.addResearch("m_runner");
            knowledge.sync((EntityPlayerMP)player);
         }

         if (!knowledge.isResearchKnown("m_jumper") && sms.func_77444_a(StatList.field_75953_u) > 500) {
            knowledge.addResearch("m_jumper");
            knowledge.sync((EntityPlayerMP)player);
         }

         if (!knowledge.isResearchKnown("m_swimmer") && sms.func_77444_a(StatList.field_75946_m) > 8000) {
            knowledge.addResearch("m_swimmer");
            knowledge.sync((EntityPlayerMP)player);
         }
      }

   }
}
