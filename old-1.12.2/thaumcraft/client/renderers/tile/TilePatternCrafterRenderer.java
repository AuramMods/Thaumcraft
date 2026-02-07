package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.block.ModelBoreBase;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.crafting.TilePatternCrafter;

public class TilePatternCrafterRenderer extends TileEntitySpecialRenderer {
   private ModelBoreBase model = new ModelBoreBase();
   private static final ResourceLocation TEX = new ResourceLocation("thaumcraft", "textures/blocks/pattern_crafter_modes.png");
   private static final ResourceLocation ICON = new ResourceLocation("thaumcraft", "items/gear_brass");

   public void renderEntityAt(TilePatternCrafter pc, double x, double y, double z, float fq) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      int f = 3;
      if (pc.func_145831_w() != null) {
         f = BlockStateUtils.getFacing(pc.func_145832_p()).ordinal();
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
      switch(f) {
      case 2:
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      case 3:
      default:
         break;
      case 4:
         GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
         break;
      case 5:
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glPushMatrix();
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, 0.0F, -0.5F);
      UtilsFX.renderQuadCentered(TEX, 10, 1, pc.type, 0.5F, 1.0F, 1.0F, 1.0F, pc.func_145838_q().func_185484_c(pc.func_145831_w().func_180495_p(pc.func_174877_v()), pc.func_145831_w(), pc.func_174877_v()), 771, 1.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(-0.2F, -0.40625F, 0.05F);
      GL11.glRotatef(-pc.rot % 360.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScaled(0.5D, 0.5D, 1.0D);
      GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
      UtilsFX.renderItemIn2D(ICON.toString(), 0.1F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.2F, -0.40625F, 0.05F);
      GL11.glRotatef(pc.rot % 360.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScaled(0.5D, 0.5D, 1.0D);
      GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
      UtilsFX.renderItemIn2D(ICON.toString(), 0.1F);
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity tileentity, double d, double d1, double d2, float f, int ff) {
      this.renderEntityAt((TilePatternCrafter)tileentity, d, d1, d2, f);
   }
}
