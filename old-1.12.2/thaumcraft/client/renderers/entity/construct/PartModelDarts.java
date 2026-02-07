package thaumcraft.client.renderers.entity.construct;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.parts.PartModel;

public class PartModelDarts extends PartModel {
   public PartModelDarts(ResourceLocation objModel, ResourceLocation objTexture, PartModel.EnumAttachPoint attachPoint) {
      super(objModel, objTexture, attachPoint);
   }

   public float preRenderArmRotationX(IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side, float inputRot) {
      if (golem.isInCombat()) {
         inputRot = 90.0F - golem.getGolemEntity().field_70127_C + inputRot / 10.0F;
      }

      return inputRot;
   }

   public float preRenderArmRotationY(IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side, float inputRot) {
      if (golem.isInCombat()) {
         inputRot /= 10.0F;
      }

      return inputRot;
   }

   public float preRenderArmRotationZ(IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side, float inputRot) {
      if (golem.isInCombat()) {
         inputRot /= 10.0F;
      }

      return inputRot;
   }
}
