package thaumcraft.common.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.OreDictionaryEntries;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.construct.ItemTurretPlacer;
import thaumcraft.common.entities.construct.golem.ItemGolemBell;
import thaumcraft.common.entities.construct.golem.ItemGolemPlacer;
import thaumcraft.common.entities.construct.golem.seals.ItemSealPlacer;
import thaumcraft.common.entities.construct.golem.seals.SealBreaker;
import thaumcraft.common.entities.construct.golem.seals.SealButcher;
import thaumcraft.common.entities.construct.golem.seals.SealEmpty;
import thaumcraft.common.entities.construct.golem.seals.SealEmptyAdvanced;
import thaumcraft.common.entities.construct.golem.seals.SealFill;
import thaumcraft.common.entities.construct.golem.seals.SealFillAdvanced;
import thaumcraft.common.entities.construct.golem.seals.SealGuard;
import thaumcraft.common.entities.construct.golem.seals.SealGuardAdvanced;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.entities.construct.golem.seals.SealHarvest;
import thaumcraft.common.entities.construct.golem.seals.SealLumber;
import thaumcraft.common.entities.construct.golem.seals.SealPickup;
import thaumcraft.common.entities.construct.golem.seals.SealPickupAdvanced;
import thaumcraft.common.entities.construct.golem.seals.SealProvide;
import thaumcraft.common.entities.construct.golem.seals.SealUse;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.items.armor.ItemBootsTraveller;
import thaumcraft.common.items.armor.ItemCultistBoots;
import thaumcraft.common.items.armor.ItemCultistLeaderArmor;
import thaumcraft.common.items.armor.ItemCultistPlateArmor;
import thaumcraft.common.items.armor.ItemCultistRobeArmor;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.items.armor.ItemGoggles;
import thaumcraft.common.items.armor.ItemRobeArmor;
import thaumcraft.common.items.armor.ItemThaumiumArmor;
import thaumcraft.common.items.armor.ItemVoidArmor;
import thaumcraft.common.items.armor.ItemVoidRobeArmor;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.baubles.ItemBaubles;
import thaumcraft.common.items.baubles.ItemVerdantCharm;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.casters.ItemFocusPouch;
import thaumcraft.common.items.casters.foci.FEffectBreak;
import thaumcraft.common.items.casters.foci.FEffectCurse;
import thaumcraft.common.items.casters.foci.FEffectExchange;
import thaumcraft.common.items.casters.foci.FEffectFire;
import thaumcraft.common.items.casters.foci.FEffectFrost;
import thaumcraft.common.items.casters.foci.FEffectMagic;
import thaumcraft.common.items.casters.foci.FEffectRift;
import thaumcraft.common.items.casters.foci.FMediumBolt;
import thaumcraft.common.items.casters.foci.FMediumProjectile;
import thaumcraft.common.items.casters.foci.FMediumTouch;
import thaumcraft.common.items.casters.foci.FModChain;
import thaumcraft.common.items.casters.foci.FModCharge;
import thaumcraft.common.items.casters.foci.FModFortune;
import thaumcraft.common.items.casters.foci.FModFrugal;
import thaumcraft.common.items.casters.foci.FModLingering;
import thaumcraft.common.items.casters.foci.FModPotency;
import thaumcraft.common.items.casters.foci.FModScatter;
import thaumcraft.common.items.casters.foci.FModSilkTouch;
import thaumcraft.common.items.consumables.ItemAlumentum;
import thaumcraft.common.items.consumables.ItemBathSalts;
import thaumcraft.common.items.consumables.ItemBottleTaint;
import thaumcraft.common.items.consumables.ItemBucketDeath;
import thaumcraft.common.items.consumables.ItemBucketPure;
import thaumcraft.common.items.consumables.ItemChunksEdible;
import thaumcraft.common.items.consumables.ItemLabel;
import thaumcraft.common.items.consumables.ItemPhial;
import thaumcraft.common.items.consumables.ItemSanitySoap;
import thaumcraft.common.items.consumables.ItemTripleMeatTreat;
import thaumcraft.common.items.consumables.ItemZombieBrain;
import thaumcraft.common.items.curios.ItemCurio;
import thaumcraft.common.items.curios.ItemLootBag;
import thaumcraft.common.items.curios.ItemPechWand;
import thaumcraft.common.items.curios.ItemPrimordialPearl;
import thaumcraft.common.items.curios.ItemThaumonomicon;
import thaumcraft.common.items.misc.ItemCreativeFluxSponge;
import thaumcraft.common.items.misc.ItemCreativePlacer;
import thaumcraft.common.items.resources.ItemCrystalEssence;
import thaumcraft.common.items.resources.ItemMagicDust;
import thaumcraft.common.items.tools.ItemCrimsonBlade;
import thaumcraft.common.items.tools.ItemElementalAxe;
import thaumcraft.common.items.tools.ItemElementalHoe;
import thaumcraft.common.items.tools.ItemElementalPickaxe;
import thaumcraft.common.items.tools.ItemElementalShovel;
import thaumcraft.common.items.tools.ItemElementalSword;
import thaumcraft.common.items.tools.ItemHandMirror;
import thaumcraft.common.items.tools.ItemPrimalCrusher;
import thaumcraft.common.items.tools.ItemResonator;
import thaumcraft.common.items.tools.ItemSanityChecker;
import thaumcraft.common.items.tools.ItemScribingTools;
import thaumcraft.common.items.tools.ItemThaumiumAxe;
import thaumcraft.common.items.tools.ItemThaumiumHoe;
import thaumcraft.common.items.tools.ItemThaumiumPickaxe;
import thaumcraft.common.items.tools.ItemThaumiumShovel;
import thaumcraft.common.items.tools.ItemThaumiumSword;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.items.tools.ItemVoidAxe;
import thaumcraft.common.items.tools.ItemVoidHoe;
import thaumcraft.common.items.tools.ItemVoidPickaxe;
import thaumcraft.common.items.tools.ItemVoidShovel;
import thaumcraft.common.items.tools.ItemVoidSword;
import thaumcraft.common.lib.CreativeTabThaumcraft;

public class ConfigItems {
   public static ItemStack startBook;
   public static CreativeTabs TABTC;
   public static final List<IThaumcraftItems> ITEM_VARIANT_HOLDERS;
   public static ItemStack AIR_CRYSTAL;
   public static ItemStack FIRE_CRYSTAL;
   public static ItemStack WATER_CRYSTAL;
   public static ItemStack EARTH_CRYSTAL;
   public static ItemStack ORDER_CRYSTAL;
   public static ItemStack ENTROPY_CRYSTAL;
   public static ItemStack FLUX_CRYSTAL;

   public static void preInit() {
      initializeItems();
      OreDictionaryEntries.initializeOreDictionary();
      AIR_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.AIR);
      FIRE_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.FIRE);
      WATER_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.WATER);
      EARTH_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.EARTH);
      ORDER_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.ORDER);
      ENTROPY_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY);
      FLUX_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.FLUX);
      OreDictionary.registerOre("shardAir", AIR_CRYSTAL);
      OreDictionary.registerOre("shardFire", AIR_CRYSTAL);
      OreDictionary.registerOre("shardWater", WATER_CRYSTAL);
      OreDictionary.registerOre("shardEarth", EARTH_CRYSTAL);
      OreDictionary.registerOre("shardOrder", ORDER_CRYSTAL);
      OreDictionary.registerOre("shardEntropy", ENTROPY_CRYSTAL);
      OreDictionary.registerOre("shardTainted", FLUX_CRYSTAL);
      NBTTagCompound contents = new NBTTagCompound();
      contents.func_74768_a("generation", 3);
      contents.func_74778_a("title", I18n.func_74838_a("book.start.title"));
      NBTTagList pages = new NBTTagList();
      pages.func_74742_a(new NBTTagString(I18n.func_74838_a("book.start.1")));
      pages.func_74742_a(new NBTTagString(I18n.func_74838_a("book.start.2")));
      pages.func_74742_a(new NBTTagString(I18n.func_74838_a("book.start.3")));
      contents.func_74782_a("pages", pages);
      startBook.func_77982_d(contents);
   }

   private static void initializeItems() {
      GameRegistry.register(ItemsTC.thaumonomicon = new ItemThaumonomicon());
      GameRegistry.register(ItemsTC.curio = new ItemCurio());
      GameRegistry.register(ItemsTC.lootBag = new ItemLootBag());
      GameRegistry.register(ItemsTC.primordialPearl = new ItemPrimordialPearl());
      GameRegistry.register(ItemsTC.pechWand = new ItemPechWand());
      GameRegistry.register(ItemsTC.amber = new ItemTCBase("amber", new String[0]));
      GameRegistry.register(ItemsTC.quicksilver = new ItemTCBase("quicksilver", new String[0]));
      GameRegistry.register(ItemsTC.ingots = new ItemTCBase("ingot", new String[]{"thaumium", "void", "brass"}));
      GameRegistry.register(ItemsTC.nuggets = new ItemTCBase("nugget", new String[]{"iron", "copper", "tin", "silver", "lead", "quicksilver", "thaumium", "void", "brass", "quartz"}));
      GameRegistry.register(ItemsTC.clusters = new ItemTCBase("cluster", new String[]{"iron", "gold", "copper", "tin", "silver", "lead", "cinnabar", "quartz"}));
      GameRegistry.register(ItemsTC.fabric = new ItemTCBase("fabric", new String[0]));
      GameRegistry.register(ItemsTC.visResonator = new ItemTCBase("vis_resonator", new String[0]));
      GameRegistry.register(ItemsTC.tallow = new ItemTCBase("tallow", new String[0]));
      GameRegistry.register(ItemsTC.gear = new ItemTCBase("gear", new String[]{"brass", "thaumium", "void"}));
      GameRegistry.register(ItemsTC.plate = new ItemTCBase("plate", new String[]{"brass", "iron", "thaumium", "void"}));
      GameRegistry.register(ItemsTC.filter = new ItemTCBase("filter", new String[0]));
      GameRegistry.register(ItemsTC.morphicResonator = new ItemTCBase("morphic_resonator", new String[0]));
      GameRegistry.register(ItemsTC.salisMundus = new ItemMagicDust());
      GameRegistry.register(ItemsTC.mirroredGlass = new ItemTCBase("mirrored_glass", new String[0]));
      GameRegistry.register(ItemsTC.voidSeed = new ItemTCBase("void_seed", new String[0]));
      GameRegistry.register(ItemsTC.mind = new ItemTCBase("mind", new String[]{"clockwork", "biothaumic"}));
      GameRegistry.register(ItemsTC.modules = new ItemTCBase("module", new String[]{"vision", "aggression"}));
      GameRegistry.register(ItemsTC.crystalEssence = new ItemCrystalEssence());
      GameRegistry.register(ItemsTC.chunks = new ItemChunksEdible());
      GameRegistry.register(ItemsTC.tripleMeatTreat = new ItemTripleMeatTreat());
      GameRegistry.register(ItemsTC.brain = new ItemZombieBrain());
      GameRegistry.register(ItemsTC.label = new ItemLabel());
      GameRegistry.register(ItemsTC.phial = new ItemPhial());
      GameRegistry.register(ItemsTC.alumentum = new ItemAlumentum());
      GameRegistry.register(ItemsTC.jarBrace = new ItemTCBase("jar_brace", new String[0]));
      GameRegistry.register(ItemsTC.bucketDeath = new ItemBucketDeath());
      GameRegistry.register(ItemsTC.bucketPure = new ItemBucketPure());
      GameRegistry.register(ItemsTC.bottleTaint = new ItemBottleTaint());
      GameRegistry.register(ItemsTC.sanitySoap = new ItemSanitySoap());
      GameRegistry.register(ItemsTC.bathSalts = new ItemBathSalts());
      GameRegistry.register(ItemsTC.turretPlacer = new ItemTurretPlacer());
      GameRegistry.register(ItemsTC.scribingTools = new ItemScribingTools());
      GameRegistry.register(ItemsTC.thaumometer = new ItemThaumometer());
      GameRegistry.register(ItemsTC.resonator = new ItemResonator());
      GameRegistry.register(ItemsTC.sanityChecker = new ItemSanityChecker());
      GameRegistry.register(ItemsTC.handMirror = new ItemHandMirror());
      GameRegistry.register(ItemsTC.thaumiumAxe = new ItemThaumiumAxe(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
      GameRegistry.register(ItemsTC.thaumiumSword = new ItemThaumiumSword(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
      GameRegistry.register(ItemsTC.thaumiumShovel = new ItemThaumiumShovel(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
      GameRegistry.register(ItemsTC.thaumiumPick = new ItemThaumiumPickaxe(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
      GameRegistry.register(ItemsTC.thaumiumHoe = new ItemThaumiumHoe(ThaumcraftMaterials.TOOLMAT_THAUMIUM));
      GameRegistry.register(ItemsTC.voidAxe = new ItemVoidAxe(ThaumcraftMaterials.TOOLMAT_VOID));
      GameRegistry.register(ItemsTC.voidSword = new ItemVoidSword(ThaumcraftMaterials.TOOLMAT_VOID));
      GameRegistry.register(ItemsTC.voidShovel = new ItemVoidShovel(ThaumcraftMaterials.TOOLMAT_VOID));
      GameRegistry.register(ItemsTC.voidPick = new ItemVoidPickaxe(ThaumcraftMaterials.TOOLMAT_VOID));
      GameRegistry.register(ItemsTC.voidHoe = new ItemVoidHoe(ThaumcraftMaterials.TOOLMAT_VOID));
      GameRegistry.register(ItemsTC.elementalAxe = new ItemElementalAxe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
      GameRegistry.register(ItemsTC.elementalSword = new ItemElementalSword(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
      GameRegistry.register(ItemsTC.elementalShovel = new ItemElementalShovel(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
      GameRegistry.register(ItemsTC.elementalPick = new ItemElementalPickaxe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
      GameRegistry.register(ItemsTC.elementalHoe = new ItemElementalHoe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL));
      GameRegistry.register(ItemsTC.primalCrusher = new ItemPrimalCrusher());
      GameRegistry.register(ItemsTC.crimsonBlade = new ItemCrimsonBlade());
      GameRegistry.register(ItemsTC.goggles = new ItemGoggles());
      GameRegistry.register(ItemsTC.thaumiumHelm = new ItemThaumiumArmor("thaumium_helm", ThaumcraftMaterials.ARMORMAT_THAUMIUM, 2, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.thaumiumChest = new ItemThaumiumArmor("thaumium_chest", ThaumcraftMaterials.ARMORMAT_THAUMIUM, 2, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.thaumiumLegs = new ItemThaumiumArmor("thaumium_legs", ThaumcraftMaterials.ARMORMAT_THAUMIUM, 2, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.thaumiumBoots = new ItemThaumiumArmor("thaumium_boots", ThaumcraftMaterials.ARMORMAT_THAUMIUM, 2, EntityEquipmentSlot.FEET));
      GameRegistry.register(ItemsTC.clothChest = new ItemRobeArmor("cloth_chest", ThaumcraftMaterials.ARMORMAT_SPECIAL, 1, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.clothLegs = new ItemRobeArmor("cloth_legs", ThaumcraftMaterials.ARMORMAT_SPECIAL, 2, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.clothBoots = new ItemRobeArmor("cloth_boots", ThaumcraftMaterials.ARMORMAT_SPECIAL, 1, EntityEquipmentSlot.FEET));
      GameRegistry.register(ItemsTC.travellerBoots = new ItemBootsTraveller());
      GameRegistry.register(ItemsTC.fortressHelm = new ItemFortressArmor("fortress_helm", ThaumcraftMaterials.ARMORMAT_FORTRESS, 4, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.fortressChest = new ItemFortressArmor("fortress_chest", ThaumcraftMaterials.ARMORMAT_FORTRESS, 4, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.fortressLegs = new ItemFortressArmor("fortress_legs", ThaumcraftMaterials.ARMORMAT_FORTRESS, 4, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.voidHelm = new ItemVoidArmor("void_helm", ThaumcraftMaterials.ARMORMAT_VOID, 2, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.voidChest = new ItemVoidArmor("void_chest", ThaumcraftMaterials.ARMORMAT_VOID, 2, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.voidLegs = new ItemVoidArmor("void_legs", ThaumcraftMaterials.ARMORMAT_VOID, 2, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.voidBoots = new ItemVoidArmor("void_boots", ThaumcraftMaterials.ARMORMAT_VOID, 2, EntityEquipmentSlot.FEET));
      GameRegistry.register(ItemsTC.voidRobeHelm = new ItemVoidRobeArmor("void_robe_helm", ThaumcraftMaterials.ARMORMAT_VOID, 4, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.voidRobeChest = new ItemVoidRobeArmor("void_robe_chest", ThaumcraftMaterials.ARMORMAT_VOID, 4, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.voidRobeLegs = new ItemVoidRobeArmor("void_robe_legs", ThaumcraftMaterials.ARMORMAT_VOID, 4, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.crimsonPlateHelm = new ItemCultistPlateArmor("crimson_plate_helm", ArmorMaterial.IRON, 4, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.crimsonPlateChest = new ItemCultistPlateArmor("crimson_plate_chest", ArmorMaterial.IRON, 4, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.crimsonPlateLegs = new ItemCultistPlateArmor("crimson_plate_legs", ArmorMaterial.IRON, 4, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.crimsonBoots = new ItemCultistBoots());
      GameRegistry.register(ItemsTC.crimsonRobeHelm = new ItemCultistRobeArmor("crimson_robe_helm", ArmorMaterial.IRON, 4, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.crimsonRobeChest = new ItemCultistRobeArmor("crimson_robe_chest", ArmorMaterial.IRON, 4, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.crimsonRobeLegs = new ItemCultistRobeArmor("crimson_robe_legs", ArmorMaterial.IRON, 4, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.crimsonPraetorHelm = new ItemCultistLeaderArmor("crimson_praetor_helm", 4, EntityEquipmentSlot.HEAD));
      GameRegistry.register(ItemsTC.crimsonPraetorChest = new ItemCultistLeaderArmor("crimson_praetor_chest", 4, EntityEquipmentSlot.CHEST));
      GameRegistry.register(ItemsTC.crimsonPraetorLegs = new ItemCultistLeaderArmor("crimson_praetor_legs", 4, EntityEquipmentSlot.LEGS));
      GameRegistry.register(ItemsTC.baubles = new ItemBaubles());
      GameRegistry.register(ItemsTC.amuletVis = new ItemAmuletVis());
      GameRegistry.register(ItemsTC.charmVerdant = new ItemVerdantCharm());
      GameRegistry.register(ItemsTC.creativePlacer = new ItemCreativePlacer());
      GameRegistry.register(ItemsTC.creativeFluxSponge = new ItemCreativeFluxSponge());
      GameRegistry.register(ItemsTC.casterBasic = new ItemCaster("caster_basic", 0));
      GameRegistry.register(ItemsTC.focusBlank = (new ItemTCBase("focus_blank", new String[0])).func_77625_d(1).func_77637_a(TABTC));
      GameRegistry.register(ItemsTC.focus = new ItemFocus());
      GameRegistry.register(ItemsTC.focusPouch = new ItemFocusPouch());
      GameRegistry.register(ItemsTC.golemBell = new ItemGolemBell());
      GameRegistry.register(ItemsTC.golemPlacer = new ItemGolemPlacer());
      GameRegistry.register(ItemsTC.seals = new ItemSealPlacer());
   }

   public static void init() {
      FocusHelper.registerFocusPart(FocusHelper.POTENCY = new FModPotency());
      FocusHelper.registerFocusPart(FocusHelper.LINGERING = new FModLingering());
      FocusHelper.registerFocusPart(FocusHelper.FRUGAL = new FModFrugal());
      FocusHelper.registerFocusPart(FocusHelper.SCATTER = new FModScatter());
      FocusHelper.registerFocusPart(FocusHelper.CHAIN = new FModChain());
      FocusHelper.registerFocusPart(FocusHelper.SILKTOUCH = new FModSilkTouch());
      FocusHelper.registerFocusPart(FocusHelper.FORTUNE = new FModFortune());
      FocusHelper.registerFocusPart(FocusHelper.CHARGE = new FModCharge());
      FocusHelper.registerFocusPart(FocusHelper.TOUCH = new FMediumTouch());
      FocusHelper.registerFocusPart(FocusHelper.BOLT = new FMediumBolt(), FocusHelper.CHAIN, FocusHelper.CHARGE);
      FocusHelper.registerFocusPart(FocusHelper.PROJECTILE = new FMediumProjectile(), FocusHelper.SCATTER, FocusHelper.CHARGE);
      FocusHelper.registerFocusPart(FocusHelper.FIRE = new FEffectFire(), FocusHelper.POTENCY, FocusHelper.FRUGAL, FocusHelper.LINGERING);
      FocusHelper.registerFocusPart(FocusHelper.FROST = new FEffectFrost(), FocusHelper.POTENCY, FocusHelper.FRUGAL, FocusHelper.LINGERING);
      FocusHelper.registerFocusPart(FocusHelper.MAGIC = new FEffectMagic(), FocusHelper.POTENCY, FocusHelper.FRUGAL);
      FocusHelper.registerFocusPart(FocusHelper.CURSE = new FEffectCurse(), FocusHelper.POTENCY, FocusHelper.FRUGAL, FocusHelper.LINGERING);
      FocusHelper.registerFocusPart(FocusHelper.BREAK = new FEffectBreak(), FocusHelper.FORTUNE, FocusHelper.FRUGAL, FocusHelper.SILKTOUCH);
      FocusHelper.registerFocusPart(FocusHelper.EXCHANGE = new FEffectExchange(), FocusHelper.FORTUNE, FocusHelper.FRUGAL, FocusHelper.SILKTOUCH);
      FocusHelper.registerFocusPart(FocusHelper.RIFT = new FEffectRift(), FocusHelper.POTENCY, FocusHelper.FRUGAL, FocusHelper.LINGERING);
   }

   public static void preInitSeals() {
      SealHandler.registerSeal(new SealPickup());
      SealHandler.registerSeal(new SealPickupAdvanced());
      SealHandler.registerSeal(new SealFill());
      SealHandler.registerSeal(new SealFillAdvanced());
      SealHandler.registerSeal(new SealEmpty());
      SealHandler.registerSeal(new SealEmptyAdvanced());
      SealHandler.registerSeal(new SealHarvest());
      SealHandler.registerSeal(new SealButcher());
      SealHandler.registerSeal(new SealGuard());
      SealHandler.registerSeal(new SealGuardAdvanced());
      SealHandler.registerSeal(new SealLumber());
      SealHandler.registerSeal(new SealBreaker());
      SealHandler.registerSeal(new SealUse());
      SealHandler.registerSeal(new SealProvide());
   }

   @SideOnly(Side.CLIENT)
   public static void initModelsAndVariants() {
      Iterator var0 = ITEM_VARIANT_HOLDERS.iterator();

      while(var0.hasNext()) {
         IThaumcraftItems itemVariantHolder = (IThaumcraftItems)var0.next();
         initModelAndVariants(itemVariantHolder);
      }

   }

   @SideOnly(Side.CLIENT)
   private static void initModelAndVariants(IThaumcraftItems item) {
      int i;
      if (item.getCustomMesh() != null) {
         ModelLoader.setCustomMeshDefinition(item.getItem(), item.getCustomMesh());

         for(i = 0; i < item.getVariantNames().length; ++i) {
            ModelBakery.registerItemVariants(item.getItem(), new ResourceLocation[]{item.getCustomModelResourceLocation(item.getVariantNames()[i])});
         }
      } else if (item.getItem() == ItemsTC.seals) {
         for(i = 0; i < item.getVariantNames().length; ++i) {
            ModelLoader.setCustomModelResourceLocation(item.getItem(), item.getVariantMeta()[i], new ModelResourceLocation(item.getItem().getRegistryName() + "_" + item.getVariantNames()[i], (String)null));
         }
      } else if (!item.getItem().func_77614_k()) {
         ModelLoader.setCustomModelResourceLocation(item.getItem(), 0, new ModelResourceLocation(item.getItem().getRegistryName(), (String)null));
      } else {
         for(i = 0; i < item.getVariantNames().length; ++i) {
            ModelLoader.setCustomModelResourceLocation(item.getItem(), item.getVariantMeta()[i], item.getCustomModelResourceLocation(item.getVariantNames()[i]));
         }
      }

   }

   static {
      startBook = new ItemStack(Items.field_151164_bB);
      TABTC = new CreativeTabThaumcraft(CreativeTabs.getNextID(), "thaumcraft");
      ITEM_VARIANT_HOLDERS = new ArrayList();
   }
}
