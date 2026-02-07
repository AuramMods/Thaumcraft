package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderThaumicSlime extends RenderSlime {
   private static final ResourceLocation slimeTexture = new ResourceLocation("thaumcraft", "textures/models/creature/tslime.png");

   public RenderThaumicSlime(RenderManager p_i46141_1_, ModelBase p_i46141_2_, float p_i46141_3_) {
      super(p_i46141_1_, p_i46141_2_, p_i46141_3_);
   }

   protected ResourceLocation func_110775_a(EntitySlime entity) {
      return slimeTexture;
   }
}
