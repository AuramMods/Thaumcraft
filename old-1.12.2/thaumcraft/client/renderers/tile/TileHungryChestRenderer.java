package thaumcraft.client.renderers.tile;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.tiles.devices.TileHungryChest;

@SideOnly(Side.CLIENT)
public class TileHungryChestRenderer extends TileEntitySpecialRenderer {
   private ModelChest chestModel = new ModelChest();
   private static final ResourceLocation textureNormal = new ResourceLocation("thaumcraft", "textures/models/chesthungry.png");

   public void renderTileEntityChestAt(TileHungryChest chest, double par2, double par4, double par6, float par8, int bp) {
      int var9 = false;
      int var9;
      if (!chest.func_145830_o()) {
         var9 = 0;
      } else {
         var9 = chest.func_145832_p();
      }

      ModelChest var14 = this.chestModel;
      if (bp >= 0) {
         this.func_147499_a(field_178460_a[bp]);
         GlStateManager.func_179128_n(5890);
         GlStateManager.func_179094_E();
         GlStateManager.func_179152_a(4.0F, 4.0F, 1.0F);
         GlStateManager.func_179109_b(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.func_179128_n(5888);
      } else {
         this.func_147499_a(textureNormal);
      }

      GL11.glPushMatrix();
      GL11.glEnable(32826);
      if (bp < 0) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
      GL11.glScalef(1.0F, -1.0F, -1.0F);
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      short var11 = 0;
      if (var9 == 2) {
         var11 = 180;
      }

      if (var9 == 3) {
         var11 = 0;
      }

      if (var9 == 4) {
         var11 = 90;
      }

      if (var9 == 5) {
         var11 = -90;
      }

      GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      float var12 = chest.field_145986_n + (chest.field_145989_m - chest.field_145986_n) * par8;
      var12 = 1.0F - var12;
      var12 = 1.0F - var12 * var12 * var12;
      var14.field_78234_a.field_78795_f = -(var12 * 3.1415927F / 2.0F);
      var14.func_78231_a();
      GL11.glDisable(32826);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (bp >= 0) {
         GlStateManager.func_179128_n(5890);
         GlStateManager.func_179121_F();
         GlStateManager.func_179128_n(5888);
      }

   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int q) {
      this.renderTileEntityChestAt((TileHungryChest)par1TileEntity, par2, par4, par6, par8, q);
   }
}
