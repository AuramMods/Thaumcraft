package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.parts.PartModel;

public class PartModelHauler extends PartModel {
   public PartModelHauler(ResourceLocation objModel, ResourceLocation objTexture, PartModel.EnumAttachPoint attachPoint) {
      super(objModel, objTexture, attachPoint);
   }

   public void postRenderObjectPart(String partName, IGolemAPI golem, float partialTicks, PartModel.EnumLimbSide side) {
      if (golem.getCarrying().length > 1 && golem.getCarrying()[1] != null) {
         ItemStack itemstack = golem.getCarrying()[1];
         if (itemstack != null) {
            GlStateManager.func_179094_E();
            Item item = itemstack.func_77973_b();
            Minecraft minecraft = Minecraft.func_71410_x();
            GlStateManager.func_179139_a(0.375D, 0.375D, 0.375D);
            GlStateManager.func_179109_b(0.0F, 0.33F, 0.825F);
            if (!(item instanceof ItemBlock)) {
               GlStateManager.func_179109_b(0.0F, 0.0F, -0.25F);
            }

            minecraft.func_175597_ag().func_178099_a(golem.getGolemEntity(), itemstack, TransformType.HEAD);
            GlStateManager.func_179121_F();
         }
      }

   }
}
