package thaumcraft.common.world.aura.nodetypes;

import net.minecraft.util.math.MathHelper;
import thaumcraft.common.world.aura.EntityAuraNode;

public class NTUnstable extends NTNormal {
   public NTUnstable(int id) {
      super(id);
   }

   public boolean performDischargeEvent(EntityAuraNode node) {
      boolean b = super.performDischargeEvent(node);
      if (b) {
         node.setNodeSize(node.getNodeSize() + MathHelper.func_76136_a(node.func_130014_f_().field_73012_v, -1, 1));
      }

      return b;
   }
}
