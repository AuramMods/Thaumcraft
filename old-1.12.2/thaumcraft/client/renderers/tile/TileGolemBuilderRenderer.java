package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.crafting.TileGolemBuilder;

@SideOnly(Side.CLIENT)
public class TileGolemBuilderRenderer extends TileEntitySpecialRenderer {
   private IModelCustom model;
   private static final ResourceLocation TM = new ResourceLocation("thaumcraft", "models/block/golembuilder.obj");
   private static final ResourceLocation TEX = new ResourceLocation("thaumcraft", "textures/blocks/golembuilder.png");
   EntityItem entityitem = null;

   public TileGolemBuilderRenderer() {
      this.model = AdvancedModelLoader.loadModel(TM);
   }

   public void renderTileEntityAt(TileGolemBuilder tile, double par2, double par4, double par6, float pt) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_147499_a(TEX);
      GL11.glMatrixMode(5890);
      GL11.glLoadIdentity();
      GL11.glScalef(1.0F, -1.0F, 1.0F);
      GL11.glMatrixMode(5888);
      EnumFacing facing = BlockStateUtils.getFacing(tile.func_145832_p());
      if (tile.func_145831_w() != null) {
         switch(facing.ordinal()) {
         case 3:
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 4:
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 5:
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      this.model.renderAllExcept("press");
      GL11.glPushMatrix();
      float h = (float)tile.press;
      double s = Math.sin(Math.toRadians((double)h)) * 0.625D;
      GL11.glTranslated(0.0D, -s, 0.0D);
      this.model.renderPart("press");
      GL11.glPopMatrix();
      GL11.glMatrixMode(5890);
      GL11.glLoadIdentity();
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glMatrixMode(5888);
      GL11.glTranslatef(-0.3125F, 0.625F, 1.3125F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      TextureAtlasSprite icon = Minecraft.func_71410_x().func_175602_ab().func_175023_a().func_178122_a(Blocks.field_150353_l.func_176223_P());
      UtilsFX.renderQuadFromIcon(icon, 0.625F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int q) {
      this.renderTileEntityAt((TileGolemBuilder)par1TileEntity, par2, par4, par6, par8);
   }
}
