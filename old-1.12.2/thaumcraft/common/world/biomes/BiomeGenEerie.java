package thaumcraft.common.world.biomes;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.entities.monster.EntityWisp;

public class BiomeGenEerie extends Biome {
   public BiomeGenEerie(BiomeProperties par1) {
      super(par1);
      this.field_76762_K.clear();
      this.field_76762_K.add(new SpawnListEntry(EntityBat.class, 3, 1, 1));
      this.field_76761_J.add(new SpawnListEntry(EntityWitch.class, 8, 1, 1));
      this.field_76761_J.add(new SpawnListEntry(EntityEnderman.class, 4, 1, 1));
      if (Config.spawnAngryZombie) {
         this.field_76761_J.add(new SpawnListEntry(EntityBrainyZombie.class, 32, 1, 1));
         this.field_76761_J.add(new SpawnListEntry(EntityGiantBrainyZombie.class, 8, 1, 1));
      }

      if (Config.spawnWisp) {
         this.field_76761_J.add(new SpawnListEntry(EntityWisp.class, 3, 1, 1));
      }

      if (Config.spawnElder) {
         this.field_76761_J.add(new SpawnListEntry(EntityEldritchGuardian.class, 1, 1, 1));
      }

      this.field_76760_I.field_76832_z = 2;
      this.field_76760_I.field_76802_A = 1;
      this.field_76760_I.field_76803_B = 2;
   }

   @SideOnly(Side.CLIENT)
   public int func_180627_b(BlockPos p_180627_1_) {
      return 4212800;
   }

   @SideOnly(Side.CLIENT)
   public int func_180625_c(BlockPos p_180625_1_) {
      return 4212800;
   }

   public int func_76731_a(float par1) {
      return 2237081;
   }

   public int getWaterColorMultiplier() {
      return 3035999;
   }
}
