package thaumcraft.client.renderers.tile;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.lib.RenderCubes;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.block.ModelBrain;
import thaumcraft.client.renderers.models.block.ModelJar;
import thaumcraft.common.config.Config;
import thaumcraft.common.tiles.devices.TileJarBrain;
import thaumcraft.common.tiles.essentia.TileJar;
import thaumcraft.common.tiles.essentia.TileJarFillable;

@SideOnly(Side.CLIENT)
public class TileJarRenderer extends TileEntitySpecialRenderer {
   private ModelJar model = new ModelJar();
   private ModelBrain brain = new ModelBrain();
   private static final ResourceLocation TEX_LABEL = new ResourceLocation("thaumcraft", "textures/models/label.png");
   private static final ResourceLocation TEX_BRAIN = new ResourceLocation("thaumcraft", "textures/models/brain2.png");
   private static final ResourceLocation TEX_BRINE = new ResourceLocation("thaumcraft", "textures/models/jarbrine.png");

   public void renderTileEntityAt(TileJar tile, double x, double y, double z, float f) {
      GL11.glPushMatrix();
      GL11.glDisable(2884);
      GL11.glTranslatef((float)x + 0.5F, (float)y + 0.01F, (float)z + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (tile instanceof TileJarBrain) {
         this.renderBrain((TileJarBrain)tile, x, y, z, f);
      } else if (tile instanceof TileJarFillable) {
         GL11.glDisable(2896);
         if (((TileJarFillable)tile).blocked) {
            GL11.glPushMatrix();
            this.func_147499_a(TEX_BRINE);
            GL11.glScaled(1.001D, 1.001D, 1.001D);
            this.model.renderLidBrace();
            GL11.glPopMatrix();
         }

         if (ThaumcraftApiHelper.getConnectableTile(tile.func_145831_w(), tile.func_174877_v(), EnumFacing.UP) != null) {
            GL11.glPushMatrix();
            this.func_147499_a(TEX_BRINE);
            GL11.glScaled(0.9D, 1.0D, 0.9D);
            this.model.renderLidExtension();
            GL11.glPopMatrix();
         }

         if (((TileJarFillable)tile).aspectFilter != null) {
            GL11.glPushMatrix();
            GL11.glBlendFunc(770, 771);
            switch(((TileJarFillable)tile).facing) {
            case 3:
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 4:
               GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 5:
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            float rot = (float)((((TileJarFillable)tile).aspectFilter.getTag().hashCode() + tile.func_174877_v().func_177958_n() + ((TileJarFillable)tile).facing) % 4 - 2);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -0.4F, 0.315F);
            if (Config.crooked) {
               GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
            }

            UtilsFX.renderQuadCentered(TEX_LABEL, 0.5F, 1.0F, 1.0F, 1.0F, -99, 771, 1.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -0.4F, 0.316F);
            if (Config.crooked) {
               GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
            }

            GL11.glScaled(0.021D, 0.021D, 0.021D);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            UtilsFX.drawTag(-8, -8, ((TileJarFillable)tile).aspectFilter);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
         }

         if (((TileJarFillable)tile).amount > 0) {
            this.renderLiquid((TileJarFillable)tile, x, y, z, f);
         }

         GL11.glEnable(2896);
      }

      GL11.glEnable(2884);
      GL11.glPopMatrix();
   }

   public void renderLiquid(TileJarFillable te, double x, double y, double z, float f) {
      GL11.glPushMatrix();
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      World world = te.func_145831_w();
      RenderCubes renderBlocks = new RenderCubes();
      GL11.glDisable(2896);
      float level = (float)te.amount / (float)te.maxAmount * 0.625F;
      Tessellator t = Tessellator.func_178181_a();
      renderBlocks.setRenderBounds(0.25D, 0.0625D, 0.25D, 0.75D, 0.0625D + (double)level, 0.75D);
      t.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181711_k);
      Color co = new Color(0);
      if (te.aspect != null) {
         co = new Color(te.aspect.getColor());
      }

      TextureAtlasSprite icon = Minecraft.func_71410_x().func_147117_R().func_110572_b("thaumcraft:blocks/animatedglow");
      this.func_147499_a(TextureMap.field_110575_b);
      renderBlocks.renderFaceYNeg(BlocksTC.jarNormal, -0.5D, 0.0D, -0.5D, icon, (float)co.getRed() / 255.0F, (float)co.getGreen() / 255.0F, (float)co.getBlue() / 255.0F, 200);
      renderBlocks.renderFaceYPos(BlocksTC.jarNormal, -0.5D, 0.0D, -0.5D, icon, (float)co.getRed() / 255.0F, (float)co.getGreen() / 255.0F, (float)co.getBlue() / 255.0F, 200);
      renderBlocks.renderFaceZNeg(BlocksTC.jarNormal, -0.5D, 0.0D, -0.5D, icon, (float)co.getRed() / 255.0F, (float)co.getGreen() / 255.0F, (float)co.getBlue() / 255.0F, 200);
      renderBlocks.renderFaceZPos(BlocksTC.jarNormal, -0.5D, 0.0D, -0.5D, icon, (float)co.getRed() / 255.0F, (float)co.getGreen() / 255.0F, (float)co.getBlue() / 255.0F, 200);
      renderBlocks.renderFaceXNeg(BlocksTC.jarNormal, -0.5D, 0.0D, -0.5D, icon, (float)co.getRed() / 255.0F, (float)co.getGreen() / 255.0F, (float)co.getBlue() / 255.0F, 200);
      renderBlocks.renderFaceXPos(BlocksTC.jarNormal, -0.5D, 0.0D, -0.5D, icon, (float)co.getRed() / 255.0F, (float)co.getGreen() / 255.0F, (float)co.getBlue() / 255.0F, 200);
      t.func_78381_a();
      GL11.glEnable(2896);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void renderBrain(TileJarBrain te, double x, double y, double z, float f) {
      float bob = MathHelper.func_76126_a((float)Minecraft.func_71410_x().field_71439_g.field_70173_aa / 14.0F) * 0.03F + 0.03F;
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, -0.8F + bob, 0.0F);

      float f2;
      for(f2 = te.rota - te.rotb; f2 >= 3.141593F; f2 -= 6.283185F) {
      }

      while(f2 < -3.141593F) {
         f2 += 6.283185F;
      }

      float f3 = te.rotb + f2 * f;
      GL11.glRotatef(f3 * 180.0F / 3.141593F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      this.func_147499_a(TEX_BRAIN);
      GL11.glScalef(0.4F, 0.4F, 0.4F);
      this.brain.render();
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      this.func_147499_a(TEX_BRINE);
      this.model.renderBrine();
      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int q) {
      this.renderTileEntityAt((TileJar)par1TileEntity, par2, par4, par6, par8);
   }
}
