package thaumcraft.common.world.aura.nodetypes;

import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.world.aura.EntityAuraNode;

public class NTHungry extends NTNormal {
   public NTHungry(int id) {
      super(id);
   }

   public boolean performDischargeEvent(EntityAuraNode node) {
      float f = AuraHelper.getVis(node.field_70170_p, node.func_180425_c()) / (float)AuraHelper.getAuraBase(node.field_70170_p, node.func_180425_c());
      if (node.field_70170_p.field_73012_v.nextFloat() < f) {
         float q = AuraHelper.drainVis(node.field_70170_p, node.func_180425_c(), (float)Math.sqrt((double)node.getNodeSize()), false);
         int m = node.getNodeSize() / 3;
         if (q > 0.0F && node.field_70170_p.field_73012_v.nextInt(5 + m * m) == 0) {
            node.setNodeSize(node.getNodeSize() + 1);
         }
      }

      return false;
   }
}
