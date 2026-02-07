package thaumcraft.client.renderers.entity.construct;

import java.util.HashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.parts.PartModel;

public class PartModelBreakers extends PartModel {
   private HashMap<Integer, Float[]> ani = new HashMap();

   public PartModelBreakers(ResourceLocation objModel, ResourceLocation objTexture, PartModel.EnumAttachPoint attachPoint) {
      super(objModel, objTexture, attachPoint);
   }

   public void preRenderObjectPart(String partName, IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side) {
      if (partName.equals("grinder")) {
         float lastSpeed = 0.0F;
         float lastRot = 0.0F;
         if (this.ani.containsKey(golem.getGolemEntity().func_145782_y())) {
            lastSpeed = ((Float[])this.ani.get(golem.getGolemEntity().func_145782_y()))[0];
            lastRot = ((Float[])this.ani.get(golem.getGolemEntity().func_145782_y()))[1];
         }

         float f = Math.max(lastSpeed, golem.getGolemEntity().func_70678_g(partialTicks) * 20.0F);
         float rot = lastRot + f;
         lastSpeed = f * 0.99F;
         this.ani.put(golem.getGolemEntity().func_145782_y(), new Float[]{lastSpeed, rot});
         GlStateManager.func_179137_b(0.0D, -0.34D, 0.0D);
         GlStateManager.func_179114_b(((float)golem.getGolemEntity().field_70173_aa + partialTicks) / 2.0F + rot + (float)(side == PartModel.EnumLimbSide.LEFT ? 22 : 0), side == PartModel.EnumLimbSide.LEFT ? -1.0F : 1.0F, 0.0F, 0.0F);
      }

   }
}
