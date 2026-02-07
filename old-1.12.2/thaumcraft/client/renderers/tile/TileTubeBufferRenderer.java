package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.client.renderers.models.block.ModelTubeValve;
import thaumcraft.common.tiles.essentia.TileTubeBuffer;

public class TileTubeBufferRenderer extends TileEntitySpecialRenderer {
   private ModelTubeValve model = new ModelTubeValve();
   private static final ResourceLocation TEX_VALVE = new ResourceLocation("thaumcraft", "textures/models/valve.png");

   public void renderEntityAt(TileTubeBuffer buffer, double x, double y, double z, float fq) {
      this.func_147499_a(TEX_VALVE);
      if (buffer.func_145831_w() != null) {
         EnumFacing[] var9 = EnumFacing.field_82609_l;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            EnumFacing dir = var9[var11];
            if (buffer.chokedSides[dir.ordinal()] != 0 && buffer.openSides[dir.ordinal()] && ThaumcraftApiHelper.getConnectableTile(buffer.func_145831_w(), buffer.func_174877_v(), dir) != null) {
               GL11.glPushMatrix();
               GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
               if (dir.func_176734_d().func_96559_d() == 0) {
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               } else {
                  GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
                  GL11.glRotatef(90.0F, (float)dir.func_176734_d().func_96559_d(), 0.0F, 0.0F);
               }

               GL11.glRotatef(90.0F, (float)dir.func_176734_d().func_82601_c(), (float)dir.func_176734_d().func_96559_d(), (float)dir.func_176734_d().func_82599_e());
               GL11.glPushMatrix();
               if (buffer.chokedSides[dir.ordinal()] == 2) {
                  GL11.glColor3f(1.0F, 0.3F, 0.3F);
               } else {
                  GL11.glColor3f(0.3F, 0.3F, 1.0F);
               }

               GL11.glScaled(2.0D, 1.0D, 2.0D);
               GL11.glTranslated(0.0D, -0.5D, 0.0D);
               this.model.renderRod();
               GL11.glPopMatrix();
               GL11.glColor3f(1.0F, 1.0F, 1.0F);
               GL11.glPopMatrix();
            }
         }
      }

   }

   public void func_180535_a(TileEntity tileentity, double d, double d1, double d2, float f, int q) {
      this.renderEntityAt((TileTubeBuffer)tileentity, d, d1, d2, f);
   }
}
