package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.block.ModelBellows;
import thaumcraft.client.renderers.models.block.ModelBoreBase;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileBellows;
import thaumcraft.common.tiles.essentia.TileTubeBuffer;

public class TileBellowsRenderer extends TileEntitySpecialRenderer<TileBellows> {
   private ModelBellows model = new ModelBellows();
   private ModelBoreBase model2 = new ModelBoreBase();
   private static final ResourceLocation TEX = new ResourceLocation("thaumcraft", "textures/blocks/bellows.png");
   private static final ResourceLocation TEX_BORE = new ResourceLocation("thaumcraft", "textures/models/bore.png");

   public void renderTileEntityAt(TileBellows bellows, double x, double y, double z, float fq, int q) {
      float scale = 0.0F;
      EnumFacing dir = EnumFacing.WEST;
      boolean extension = false;
      if (bellows == null) {
         EntityPlayer p = Minecraft.func_71410_x().field_71439_g;
         scale = MathHelper.func_76126_a((float)p.field_70173_aa / 8.0F) * 0.3F + 0.7F;
      } else {
         scale = bellows.inflation;
         dir = BlockStateUtils.getFacing(bellows.func_145832_p());
         TileEntity te = bellows.func_145831_w().func_175625_s(bellows.func_174877_v().func_177972_a(BlockStateUtils.getFacing(bellows.func_145832_p())));
         if (te != null && te instanceof TileTubeBuffer) {
            extension = true;
         }
      }

      float tscale = 0.125F + scale * 0.875F;
      Minecraft mc = FMLClientHandler.instance().getClient();
      if (extension) {
         this.func_147499_a(TEX_BORE);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)x + 0.5F + (float)dir.func_82601_c(), (float)y + (float)dir.func_96559_d(), (float)z + 0.5F + (float)dir.func_82599_e());
         switch(dir.func_176734_d().ordinal()) {
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

         this.model2.renderNozzle();
         GL11.glPopMatrix();
      }

      this.func_147499_a(TEX);
      GL11.glPushMatrix();
      GL11.glEnable(2977);
      GL11.glEnable(32826);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.translateFromOrientation((double)((float)x), (double)((float)y), (double)((float)z), dir.ordinal());
      GL11.glTranslatef(0.0F, 1.0F, 0.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, (scale + 0.1F) / 2.0F, 0.5F);
      this.model.Bag.func_78793_a(0.0F, 0.5F, 0.0F);
      this.model.Bag.func_78785_a(0.0625F);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, -tscale / 2.0F + 0.5F, 0.0F);
      this.model.TopPlank.func_78785_a(0.0625F);
      GL11.glTranslatef(0.0F, tscale / 2.0F - 0.5F, 0.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, tscale / 2.0F - 0.5F, 0.0F);
      this.model.BottomPlank.func_78785_a(0.0625F);
      GL11.glTranslatef(0.0F, -tscale / 2.0F + 0.5F, 0.0F);
      GL11.glPopMatrix();
      this.model.render();
      GL11.glDisable(32826);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   private void translateFromOrientation(double x, double y, double z, int orientation) {
      GL11.glTranslatef((float)x + 0.5F, (float)y - 0.5F, (float)z + 0.5F);
      if (orientation == 0) {
         GL11.glTranslatef(0.0F, 1.0F, -1.0F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 1) {
         GL11.glTranslatef(0.0F, 1.0F, 1.0F);
         GL11.glRotatef(270.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 2) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 4) {
         GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 5) {
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      }

   }
}
