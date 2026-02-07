package thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.block.ModelBoreBase;
import thaumcraft.common.tiles.essentia.TileAlembic;

@SideOnly(Side.CLIENT)
public class TileAlembicRenderer extends TileEntitySpecialRenderer {
   private ModelBoreBase modelBore = new ModelBoreBase();
   private static final ResourceLocation TEX_LABEL = new ResourceLocation("thaumcraft", "textures/models/label.png");
   private static final ResourceLocation TEX_BORE = new ResourceLocation("thaumcraft", "textures/models/bore.png");

   public void renderTileEntityAt(TileAlembic tile, double x, double y, double z, float f) {
      if (tile.aspectFilter != null) {
         GL11.glPushMatrix();
         GL11.glBlendFunc(770, 771);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         switch(tile.facing) {
         case 2:
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 3:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         case 4:
         default:
            break;
         case 5:
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.5F, -0.376F);
         UtilsFX.renderQuadCentered(TEX_LABEL, 0.44F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.5F, -0.377F);
         GL11.glScaled(0.02D, 0.02D, 0.02D);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         UtilsFX.drawTag(-8, -8, tile.aspectFilter);
         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }

      if (tile.func_145831_w() != null) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_147499_a(TEX_BORE);
         EnumFacing[] var9 = EnumFacing.field_176754_o;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            EnumFacing dir = var9[var11];
            if (tile.canOutputTo(dir)) {
               TileEntity te = ThaumcraftApiHelper.getConnectableTile(tile.func_145831_w(), tile.func_174877_v(), dir);
               if (te != null && ((IEssentiaTransport)te).canInputFrom(dir.func_176734_d())) {
                  GL11.glPushMatrix();
                  GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
                  switch(dir.ordinal()) {
                  case 0:
                     GL11.glTranslatef(-0.5F, 0.5F, 0.0F);
                     GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
                     break;
                  case 1:
                     GL11.glTranslatef(0.5F, 0.5F, 0.0F);
                     GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                     break;
                  case 2:
                     GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                     break;
                  case 3:
                     GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                     break;
                  case 4:
                     GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                     break;
                  case 5:
                     GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                  }

                  this.modelBore.renderNozzle();
                  GL11.glPopMatrix();
               }
            }
         }
      }

   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int q) {
      this.renderTileEntityAt((TileAlembic)par1TileEntity, par2, par4, par6, par8);
   }
}
