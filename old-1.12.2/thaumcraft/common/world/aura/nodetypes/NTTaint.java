package thaumcraft.common.world.aura.nodetypes;

import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.EntityAuraNode;

public class NTTaint extends NTNormal {
   public NTTaint(int id) {
      super(id);
   }

   public boolean performDischargeEvent(EntityAuraNode node) {
      boolean x = super.performDischargeEvent(node);
      if (x) {
         float f = AuraHandler.getFluxSaturation(node.field_70170_p, node.func_180425_c());
         if (node.field_70170_p.field_73012_v.nextFloat() > f * 0.8F) {
            AuraHelper.polluteAura(node.field_70170_p, node.func_180425_c(), 1.0F, false);
         }
      }

      return x;
   }
}
