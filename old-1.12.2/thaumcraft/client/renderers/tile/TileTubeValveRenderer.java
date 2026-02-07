package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.block.ModelTubeValve;
import thaumcraft.common.tiles.essentia.TileTubeValve;

public class TileTubeValveRenderer extends TileEntitySpecialRenderer {
   private ModelTubeValve model = new ModelTubeValve();
   private static final ResourceLocation TEX_VALVE = new ResourceLocation("thaumcraft", "textures/models/valve.png");

   public void renderEntityAt(TileTubeValve valve, double x, double y, double z, float fq) {
      this.func_147499_a(TEX_VALVE);
      GL11.glPushMatrix();
      GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
      if (valve.facing.func_96559_d() == 0) {
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      } else {
         GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
         GL11.glRotatef(90.0F, (float)valve.facing.func_96559_d(), 0.0F, 0.0F);
      }

      GL11.glRotatef(90.0F, (float)valve.facing.func_82601_c(), (float)valve.facing.func_96559_d(), (float)valve.facing.func_82599_e());
      GL11.glRotated((double)(-valve.rotation) * 1.5D, 0.0D, 1.0D, 0.0D);
      GL11.glTranslated(0.0D, (double)(-0.03F - valve.rotation / 360.0F * 0.09F), 0.0D);
      GL11.glPushMatrix();
      this.model.renderRing();
      GL11.glScaled(0.75D, 1.0D, 0.75D);
      this.model.renderRod();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity tileentity, double d, double d1, double d2, float f, int q) {
      this.renderEntityAt((TileTubeValve)tileentity, d, d1, d2, f);
   }
}
