package thaumcraft.api.golems.parts;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.IGolemAPI;

public class PartModel {
   private ResourceLocation objModel;
   private ResourceLocation texture;
   private PartModel.EnumAttachPoint attachPoint;

   public PartModel(ResourceLocation objModel, ResourceLocation objTexture, PartModel.EnumAttachPoint attachPoint) {
      this.objModel = objModel;
      this.texture = objTexture;
      this.attachPoint = attachPoint;
   }

   public ResourceLocation getObjModel() {
      return this.objModel;
   }

   public ResourceLocation getTexture() {
      return this.texture;
   }

   public PartModel.EnumAttachPoint getAttachPoint() {
      return this.attachPoint;
   }

   public boolean useMaterialTextureForObjectPart(String partName) {
      return partName.startsWith("bm");
   }

   public void preRenderObjectPart(String partName, IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side) {
   }

   public void postRenderObjectPart(String partName, IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side) {
   }

   public float preRenderArmRotationX(IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side, float inputRot) {
      return inputRot;
   }

   public float preRenderArmRotationY(IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side, float inputRot) {
      return inputRot;
   }

   public float preRenderArmRotationZ(IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side, float inputRot) {
      return inputRot;
   }

   public static enum EnumLimbSide {
      LEFT,
      RIGHT,
      MIDDLE;
   }

   public static enum EnumAttachPoint {
      ARMS,
      LEGS,
      BODY,
      HEAD;
   }
}
