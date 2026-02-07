package thaumcraft.common.world.biomes;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

public class BiomeGenEldritch extends Biome {
   public BiomeGenEldritch(BiomeProperties p_i1990_1_) {
      super(p_i1990_1_);
      this.field_76761_J.clear();
      this.field_76762_K.clear();
      this.field_76755_L.clear();
      this.field_82914_M.clear();
      this.field_76761_J.add(new SpawnListEntry(EntityInhabitedZombie.class, 1, 1, 1));
      this.field_76761_J.add(new SpawnListEntry(EntityEldritchGuardian.class, 1, 1, 1));
      this.field_76752_A = Blocks.field_150346_d.func_176223_P();
      this.field_76753_B = Blocks.field_150346_d.func_176223_P();
   }

   @SideOnly(Side.CLIENT)
   public int func_76731_a(float p_76731_1_) {
      return 0;
   }

   public void func_180624_a(World world, Random random, BlockPos pos) {
   }
}
