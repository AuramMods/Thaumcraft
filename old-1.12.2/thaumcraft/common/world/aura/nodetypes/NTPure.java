package thaumcraft.common.world.aura.nodetypes;

import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.world.aura.EntityAuraNode;

public class NTPure extends NTNormal {
   public NTPure(int id) {
      super(id);
   }

   public boolean performDischargeEvent(EntityAuraNode node) {
      boolean x = super.performDischargeEvent(node);
      if (x) {
         AuraHelper.drainFlux(node.field_70170_p, node.func_180425_c(), 1.0F, false);
      }

      return x;
   }
}
