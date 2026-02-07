package thaumcraft.common.world.aura.nodetypes;

import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.EntityAuraNode;

public abstract class NodeType {
   int id;
   public static NodeType[] nodeTypes = new NodeType[6];

   public NodeType(int id) {
      this.id = id;
   }

   public boolean performDischargeEvent(EntityAuraNode node) {
      if (node.field_70170_p.field_72995_K) {
         return false;
      } else {
         float vis = AuraHandler.getVis(node.field_70170_p, node.func_180425_c());
         int base = AuraHandler.getAuraBase(node.field_70170_p, node.func_180425_c());
         if (vis < (float)base) {
            AuraHandler.addVis(node.field_70170_p, node.func_180425_c(), 1.0F);
            return true;
         } else {
            float diff = vis / (float)base - 1.0F;
            if (vis < 500.0F && node.field_70170_p.field_73012_v.nextFloat() > diff) {
               AuraHandler.addVis(node.field_70170_p, node.func_180425_c(), 1.0F);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   static {
      nodeTypes[0] = new NTNormal(0);
      nodeTypes[1] = new NTDark(1);
      nodeTypes[2] = new NTHungry(2);
      nodeTypes[3] = new NTUnstable(3);
      nodeTypes[4] = new NTTaint(4);
      nodeTypes[5] = new NTPure(5);
   }
}
