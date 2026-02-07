package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.client.renderers.models.block.ModelTubeValve;
import thaumcraft.common.tiles.essentia.TileTubeOneway;

public class TileTubeOnewayRenderer extends TileEntitySpecialRenderer {
   private ModelTubeValve model = new ModelTubeValve();
   private static final ResourceLocation TEX_VALVE = new ResourceLocation("thaumcraft", "textures/models/valve.png");
   EnumFacing fd = null;

   public void renderEntityAt(TileTubeOneway valve, double x, double y, double z, float fq) {
      this.func_147499_a(TEX_VALVE);
      if (valve.func_145831_w() == null || ThaumcraftApiHelper.getConnectableTile(valve.func_145831_w(), valve.func_174877_v(), valve.facing.func_176734_d()) != null) {
         GL11.glPushMatrix();
         this.fd = valve.facing;
         GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
         if (this.fd.func_96559_d() == 0) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
            GL11.glRotatef(90.0F, (float)this.fd.func_96559_d(), 0.0F, 0.0F);
         }

         GL11.glRotatef(90.0F, (float)this.fd.func_82601_c(), (float)this.fd.func_96559_d(), (float)this.fd.func_82599_e());
         GL11.glPushMatrix();
         GL11.glColor3f(0.45F, 0.5F, 1.0F);
         GL11.glScaled(2.0D, 2.0D, 2.0D);
         GL11.glTranslated(0.0D, -0.3199999928474426D, 0.0D);
         this.model.renderRod();
         GL11.glPopMatrix();
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   public void func_180535_a(TileEntity tileentity, double d, double d1, double d2, float f, int q) {
      this.renderEntityAt((TileTubeOneway)tileentity, d, d1, d2, f);
   }
}
