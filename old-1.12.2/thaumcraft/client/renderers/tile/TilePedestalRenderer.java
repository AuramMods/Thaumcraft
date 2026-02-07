package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.tiles.crafting.TilePedestal;

@SideOnly(Side.CLIENT)
public class TilePedestalRenderer extends TileEntitySpecialRenderer<TilePedestal> {
   public void renderTileEntityAt(TilePedestal ped, double par2, double par4, double par6, float par8, int p_180535_9_) {
      if (ped != null && ped.func_145831_w() != null && ped.func_70301_a(0) != null) {
         EntityItem entityitem = null;
         float ticks = (float)Minecraft.func_71410_x().func_175606_aa().field_70173_aa + par8;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
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
}
