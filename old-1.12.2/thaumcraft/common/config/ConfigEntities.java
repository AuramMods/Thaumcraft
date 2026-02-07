package thaumcraft.common.config;

import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import thaumcraft.Thaumcraft;
import thaumcraft.common.entities.EntityFallingTaint;
import thaumcraft.common.entities.EntityFollowingItem;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.construct.EntityArcaneBore;
import thaumcraft.common.entities.construct.EntityTurretCrossbow;
import thaumcraft.common.entities.construct.EntityTurretCrossbowAdvanced;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityEldritchCrab;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityFireBat;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.entities.monster.boss.EntityCultistLeader;
import thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.entities.monster.boss.EntityTaintacleGiant;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;
import thaumcraft.common.entities.monster.cult.EntityCultistKnight;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeedPrime;
import thaumcraft.common.entities.monster.tainted.EntityTaintSwarm;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;
import thaumcraft.common.entities.monster.tainted.EntityTaintacleSmall;
import thaumcraft.common.entities.projectile.EntityAlumentum;
import thaumcraft.common.entities.projectile.EntityBottleTaint;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.entities.projectile.EntityGenericProjectile;
import thaumcraft.common.entities.projectile.EntityGolemDart;
import thaumcraft.common.entities.projectile.EntityGolemOrb;
import thaumcraft.common.entities.projectile.EntityGrapple;
import thaumcraft.common.world.aura.EntityAuraNode;

public class ConfigEntities {
   public static HashMap<Class, Integer> championModWhitelist = new HashMap();

   public static void preInit() {
      int id = 0;
      int var1 = id + 1;
      EntityRegistry.registerModEntity(EntityAuraNode.class, "AuraNode", id, Thaumcraft.instance, 160, 20, true);
      EntityRegistry.registerModEntity(EntityCultistPortalGreater.class, "CultistPortalGreater", var1++, Thaumcraft.instance, 64, 20, false, 6842578, 32896);
      EntityRegistry.registerModEntity(EntityCultistPortalLesser.class, "CultistPortalLesser", var1++, Thaumcraft.instance, 64, 20, false, 9438728, 6316242);
      EntityRegistry.registerModEntity(EntitySpecialItem.class, "SpecialItem", var1++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityFollowingItem.class, "FollowItem", var1++, Thaumcraft.instance, 64, 20, false);
      EntityRegistry.registerModEntity(EntityFallingTaint.class, "FallingTaint", var1++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityAlumentum.class, "Alumentum", var1++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityGolemDart.class, "GolemDart", var1++, Thaumcraft.instance, 64, 20, false);
      EntityRegistry.registerModEntity(EntityEldritchOrb.class, "EldritchOrb", var1++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityBottleTaint.class, "BottleTaint", var1++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityGolemOrb.class, "GolemOrb", var1++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityGrapple.class, "Grapple", var1++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityGenericProjectile.class, "GenericProjectile", var1++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityTurretCrossbow.class, "TurretBasic", var1++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityTurretCrossbowAdvanced.class, "TurretAdvanced", var1++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityArcaneBore.class, "ArcaneBore", var1++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityThaumcraftGolem.class, "Golem", var1++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityEldritchWarden.class, "EldritchWarden", var1++, Thaumcraft.instance, 64, 3, true, 6842578, 8421504);
      EntityRegistry.registerModEntity(EntityEldritchGolem.class, "EldritchGolem", var1++, Thaumcraft.instance, 64, 3, true, 6842578, 8947848);
      EntityRegistry.registerModEntity(EntityCultistLeader.class, "CultistLeader", var1++, Thaumcraft.instance, 64, 3, true, 6842578, 9438728);
      EntityRegistry.registerModEntity(EntityTaintacleGiant.class, "TaintacleGiant", var1++, Thaumcraft.instance, 96, 3, false, 6842578, 10618530);
      EntityRegistry.registerModEntity(EntityBrainyZombie.class, "BrainyZombie", var1++, Thaumcraft.instance, 64, 3, true, -16129, -16744448);
      EntityRegistry.registerModEntity(EntityGiantBrainyZombie.class, "GiantBrainyZombie", var1++, Thaumcraft.instance, 64, 3, true, -16129, -16760832);
      EntityRegistry.registerModEntity(EntityWisp.class, "Wisp", var1++, Thaumcraft.instance, 64, 3, false, -16129, -1);
      EntityRegistry.registerModEntity(EntityFireBat.class, "Firebat", var1++, Thaumcraft.instance, 64, 3, false, -16129, -806354944);
      EntityRegistry.registerModEntity(EntityPech.class, "Pech", var1++, Thaumcraft.instance, 64, 3, true, -16129, -12582848);
      EntityRegistry.registerModEntity(EntityMindSpider.class, "MindSpider", var1++, Thaumcraft.instance, 64, 3, true, 4996656, 4473924);
      EntityRegistry.registerModEntity(EntityEldritchGuardian.class, "EldritchGuardian", var1++, Thaumcraft.instance, 64, 3, true, 8421504, 0);
      EntityRegistry.registerModEntity(EntityCultistKnight.class, "CultistKnight", var1++, Thaumcraft.instance, 64, 3, true, 9438728, 128);
      EntityRegistry.registerModEntity(EntityCultistCleric.class, "CultistCleric", var1++, Thaumcraft.instance, 64, 3, true, 9438728, 8388608);
      EntityRegistry.registerModEntity(EntityEldritchCrab.class, "EldritchCrab", var1++, Thaumcraft.instance, 64, 3, true, 8421504, 5570560);
      EntityRegistry.registerModEntity(EntityInhabitedZombie.class, "InhabitedZombie", var1++, Thaumcraft.instance, 64, 3, true, 8421504, 5570560);
      EntityRegistry.registerModEntity(EntityThaumicSlime.class, "ThaumSlime", var1++, Thaumcraft.instance, 64, 3, true, 10618530, -32513);
      EntityRegistry.registerModEntity(EntityTaintCrawler.class, "TaintCrawler", var1++, Thaumcraft.instance, 64, 3, true, 10618530, 3158064);
      EntityRegistry.registerModEntity(EntityTaintacle.class, "Taintacle", var1++, Thaumcraft.instance, 64, 3, false, 10618530, 4469572);
      EntityRegistry.registerModEntity(EntityTaintacleSmall.class, "TaintacleTiny", var1++, Thaumcraft.instance, 64, 3, false);
      EntityRegistry.registerModEntity(EntityTaintSwarm.class, "TaintSwarm", var1++, Thaumcraft.instance, 64, 3, false, 10618530, 16744576);
      EntityRegistry.registerModEntity(EntityTaintSeed.class, "TaintSeed", var1++, Thaumcraft.instance, 64, 20, false, 10618530, 4465237);
      EntityRegistry.registerModEntity(EntityTaintSeedPrime.class, "TaintSeedPrime", var1++, Thaumcraft.instance, 64, 20, false, 10618530, 5583718);
   }

   public static void postInitEntitySpawns() {
      ArrayList<Biome> biomes = new ArrayList();
      UnmodifiableIterator var1 = BiomeManager.getBiomes(BiomeType.WARM).iterator();

      BiomeEntry be;
      while(var1.hasNext()) {
         be = (BiomeEntry)var1.next();
         biomes.add(be.biome);
      }

      var1 = BiomeManager.getBiomes(BiomeType.COOL).iterator();

      while(var1.hasNext()) {
         be = (BiomeEntry)var1.next();
         biomes.add(be.biome);
      }

      var1 = BiomeManager.getBiomes(BiomeType.ICY).iterator();

      while(var1.hasNext()) {
         be = (BiomeEntry)var1.next();
         biomes.add(be.biome);
      }

      var1 = BiomeManager.getBiomes(BiomeType.DESERT).iterator();

      while(var1.hasNext()) {
         be = (BiomeEntry)var1.next();
         biomes.add(be.biome);
      }

      Biome[] allBiomes = (Biome[])biomes.toArray(new Biome[]{null});
      if (Config.spawnAngryZombie) {
         Iterator var7 = biomes.iterator();

         while(var7.hasNext()) {
            Biome bgb = (Biome)var7.next();
            if (bgb.func_76747_a(EnumCreatureType.MONSTER) != null & bgb.func_76747_a(EnumCreatureType.MONSTER).size() > 0) {
               EntityRegistry.addSpawn(EntityBrainyZombie.class, 10, 1, 1, EnumCreatureType.MONSTER, new Biome[]{bgb});
            }
         }
      }

      if (Config.spawnPech) {
         Biome[] var9 = BiomeDictionary.getBiomesForType(Type.MAGICAL);
         int var8 = var9.length;

         for(int var4 = 0; var4 < var8; ++var4) {
            Biome bgb = var9[var4];
            if (bgb.func_76747_a(EnumCreatureType.MONSTER) != null & bgb.func_76747_a(EnumCreatureType.MONSTER).size() > 0) {
               EntityRegistry.addSpawn(EntityPech.class, 10, 1, 1, EnumCreatureType.MONSTER, new Biome[]{bgb});
            }
         }
      }

      if (Config.spawnFireBat) {
         EntityRegistry.addSpawn(EntityFireBat.class, 10, 1, 2, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(Type.NETHER));
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(System.currentTimeMillis());
         if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            EntityRegistry.addSpawn(EntityFireBat.class, 5, 1, 2, EnumCreatureType.MONSTER, (Biome[])biomes.toArray(allBiomes));
         }
      }

      if (Config.spawnWisp) {
         EntityRegistry.addSpawn(EntityWisp.class, 5, 1, 1, EnumCreatureType.MONSTER, BiomeDictionary.getBiomesForType(Type.NETHER));
      }

      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Zombie:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Spider:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Blaze:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Enderman:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Skeleton:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Witch:1");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.EldritchCrab:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.Taintacle:2");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.InhabitedZombie:3");
   }
}
