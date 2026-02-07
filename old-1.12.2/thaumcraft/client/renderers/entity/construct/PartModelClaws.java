package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.parts.PartModel;

public class PartModelClaws extends PartModel {
   float f = 0.0F;

   public PartModelClaws(ResourceLocation objModel, ResourceLocation objTexture, PartModel.EnumAttachPoint attachPoint) {
      super(objModel, objTexture, attachPoint);
   }

   public void preRenderObjectPart(String partName, IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side) {
      if (partName.startsWith("claw")) {
         this.f = 0.0F;
         this.f = golem.getGolemEntity().func_70678_g(partialTicks) * 4.1F;
         this.f *= this.f;
         GlStateManager.func_179137_b(0.0D, -0.2D, 0.0D);
         GlStateManager.func_179114_b(this.f, partName.endsWith("1") ? 1.0F : -1.0F, 0.0F, 0.0F);
      }

   }
}
