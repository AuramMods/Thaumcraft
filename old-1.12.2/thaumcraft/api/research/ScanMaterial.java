package thaumcraft.api.research;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class ScanMaterial implements IScanThing {
   String research;
   Material[] mats;

   public ScanMaterial(Material mat) {
      this.research = "!" + mat.getClass().getTypeName();
      this.mats = new Material[]{mat};
   }

   public ScanMaterial(String research, Material... mats) {
      this.research = research;
      this.mats = mats;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      if (obj != null && obj instanceof BlockPos) {
         Material[] var3 = this.mats;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Material mat = var3[var5];
            if (player.field_70170_p.func_180495_p((BlockPos)obj).func_185904_a() == mat) {
               return true;
            }
         }
      }

      return false;
   }

   public String getResearchKey(EntityPlayer player, Object object) {
      return this.research;
   }
}
