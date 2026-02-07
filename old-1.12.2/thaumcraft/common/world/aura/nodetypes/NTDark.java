package thaumcraft.common.world.aura.nodetypes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.aura.EntityAuraNode;
import thaumcraft.common.world.biomes.BiomeHandler;

public class NTDark extends NTNormal {
   public NTDark(int id) {
      super(id);
   }

   public boolean performDischargeEvent(EntityAuraNode node) {
      boolean x = super.performDischargeEvent(node);
      int rad = node.field_70170_p.field_73012_v.nextInt(180) * 2;
      Vec3d vsource = new Vec3d(node.field_70165_t, node.field_70163_u, node.field_70161_v);
      int r = (int)(4.0D + Math.sqrt((double)(node.getNodeSize() / 3)));

      for(int q = 0; q < r; ++q) {
         Vec3d vtar = new Vec3d((double)q, 0.0D, 0.0D);
         vtar = Utils.rotateAroundY(vtar, (float)rad / 180.0F * 3.1415927F);
         Vec3d vres = vsource.func_72441_c(vtar.field_72450_a, vtar.field_72448_b, vtar.field_72449_c);
         BlockPos t = new BlockPos(MathHelper.func_76128_c(vres.field_72450_a), MathHelper.func_76128_c(vres.field_72448_b), MathHelper.func_76128_c(vres.field_72449_c));
         Biome biome = node.field_70170_p.func_180494_b(t);
         if (biome != BiomeHandler.EERIE) {
            Utils.setBiomeAt(node.field_70170_p, t, BiomeHandler.EERIE);
            break;
         }
      }

      return x;
   }
}
