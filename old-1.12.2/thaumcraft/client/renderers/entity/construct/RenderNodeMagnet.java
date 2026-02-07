package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import thaumcraft.client.renderers.models.entity.ModelNodeMagnet;

public class RenderNodeMagnet extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/nodemagnet.png");

   public RenderNodeMagnet(RenderManager rm) {
      super(rm, new ModelNodeMagnet(), 0.5F);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return rl;
   }
}
