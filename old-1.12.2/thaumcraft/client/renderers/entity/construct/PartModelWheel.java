package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.parts.PartModel;
import thaumcraft.common.entities.construct.golem.parts.GolemLegWheels;

public class PartModelWheel extends PartModel {
   public PartModelWheel(ResourceLocation objModel, ResourceLocation objTexture, PartModel.EnumAttachPoint attachPoint) {
      super(objModel, objTexture, attachPoint);
   }

   public void preRenderObjectPart(String partName, IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side) {
      if (partName.equals("wheel")) {
         float lastRot = 0.0F;
         if (GolemLegWheels.ani.containsKey(golem.getGolemEntity().func_145782_y())) {
            lastRot = (Float)GolemLegWheels.ani.get(golem.getGolemEntity().func_145782_y());
         }

         GlStateManager.func_179137_b(0.0D, -0.375D, 0.0D);
         GlStateManager.func_179114_b(lastRot, -1.0F, 0.0F, 0.0F);
      }

   }
}
