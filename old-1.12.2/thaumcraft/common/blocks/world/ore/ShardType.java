package thaumcraft.common.blocks.world.ore;

import net.minecraft.block.Block;
import thaumcraft.api.aspects.Aspect;

public enum ShardType {
   AIR(0, "air", Aspect.AIR),
   FIRE(1, "fire", Aspect.FIRE),
   WATER(2, "water", Aspect.WATER),
   EARTH(3, "earth", Aspect.EARTH),
   ORDER(4, "order", Aspect.ORDER),
   ENTROPY(5, "entropy", Aspect.ENTROPY),
   FLUX(6, "flux", Aspect.FLUX);

   private static final ShardType[] METADATA_LOOKUP = new ShardType[values().length];
   private final int metadata;
   private final String name;
   private final Aspect aspect;
   private Block ore;

   private ShardType(int metadata, String unlocalizedName, Aspect aspect) {
      this.metadata = metadata;
      this.name = unlocalizedName;
      this.aspect = aspect;
   }

   public int getMetadata() {
      return this.metadata;
   }

   public Aspect getAspect() {
      return this.aspect;
   }

   public Block getOre() {
      return this.ore;
   }

   public void setOre(Block b) {
      this.ore = b;
   }

   public String getUnlocalizedName() {
      return this.name;
   }

   public String toString() {
      return this.getUnlocalizedName();
   }

   public static int getMetaByAspect(Aspect a) {
      ShardType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         if (var0[var2].getAspect() == a) {
            return var2;
         }
      }

      return -1;
   }

   public static ShardType byMetadata(int metadata) {
      if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
         metadata = 0;
      }

      return METADATA_LOOKUP[metadata];
   }

   public String getName() {
      return this.name;
   }

   static {
      ShardType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ShardType var3 = var0[var2];
         METADATA_LOOKUP[var3.getMetadata()] = var3;
      }

   }
}
