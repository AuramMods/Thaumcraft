package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.tiles.devices.TileRechargePedestal;

@SideOnly(Side.CLIENT)
public class TileRechargePedestalRenderer extends TileEntitySpecialRenderer {
   public void renderTileEntityAt(TileRechargePedestal ped, double par2, double par4, double par6, float partialTicks) {
      if (ped != null && ped.func_145831_w() != null && ped.func_70301_a(0) != null) {
         EntityItem entityitem = null;
         float ticks = (float)Minecraft.func_71410_x().func_175606_aa().field_70173_aa + partialTicks;
         GL11.glPushMatrix();
         float h = MathHelper.func_76126_a(ticks % 32767.0F / 16.0F) * 0.05F;
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.15F + h, (float)par6 + 0.5F);
         GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
         ItemStack is = ped.func_70301_a(0).func_77946_l();
         is.field_77994_a = 1;
         entityitem = new EntityItem(ped.func_145831_w(), 0.0D, 0.0D, 0.0D, is);
         entityitem.field_70290_d = 0.0F;
         RenderManager rendermanager = Minecraft.func_71410_x().func_175598_ae();
         rendermanager.func_188391_a(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
         GL11.glPopMatrix();
      }

   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int p_180535_9_) {
      this.renderTileEntityAt((TileRechargePedestal)par1TileEntity, par2, par4, par6, par8);
   }
}
