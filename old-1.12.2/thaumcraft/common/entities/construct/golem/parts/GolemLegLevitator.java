package thaumcraft.common.entities.construct.golem.parts;

import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.parts.GolemLeg;
import thaumcraft.client.fx.FXDispatcher;

public class GolemLegLevitator implements GolemLeg.ILegFunction {
   public void onUpdateTick(IGolemAPI golem) {
      if (golem.getGolemWorld().field_72995_K && (!golem.getGolemEntity().field_70122_E || golem.getGolemEntity().field_70173_aa % 5 == 0)) {
         FXDispatcher.INSTANCE.drawGolemFlyParticles(golem.getGolemEntity().field_70165_t, golem.getGolemEntity().field_70163_u + 0.1D, golem.getGolemEntity().field_70161_v, golem.getGolemWorld().field_73012_v.nextGaussian() / 100.0D, -0.1D, golem.getGolemWorld().field_73012_v.nextGaussian() / 100.0D);
      }

   }
}
