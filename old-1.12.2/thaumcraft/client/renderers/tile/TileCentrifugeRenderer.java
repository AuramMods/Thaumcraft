package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.block.ModelCentrifuge;
import thaumcraft.common.tiles.essentia.TileCentrifuge;

public class TileCentrifugeRenderer extends TileEntitySpecialRenderer {
   private ModelCentrifuge model = new ModelCentrifuge();
   private static final ResourceLocation TEX = new ResourceLocation("thaumcraft", "textures/models/centrifuge.png");

   public void renderEntityAt(TileCentrifuge cf, double x, double y, double z, float fq) {
      this.func_147499_a(TEX);
      GL11.glPushMatrix();
      GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
      this.model.renderBoxes();
      GL11.glRotated((double)cf.rotation, 0.0D, 1.0D, 0.0D);
      this.model.renderSpinnyBit();
      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity tileentity, double d, double d1, double d2, float f, int q) {
      this.renderEntityAt((TileCentrifuge)tileentity, d, d1, d2, f);
   }
}
