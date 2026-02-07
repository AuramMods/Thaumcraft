package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.crafting.TileThaumatorium;

@SideOnly(Side.CLIENT)
public class TileThaumatoriumRenderer extends TileEntitySpecialRenderer {
   EntityItem entityitem = null;

   public void renderTileEntityAt(TileThaumatorium tile, double par2, double par4, double par6, float par8) {
      EnumFacing facing = BlockStateUtils.getFacing(tile.func_145832_p());
      if (tile != null && tile.func_145831_w() != null && tile.recipeHash != null && tile.recipeHash.size() > 0) {
         int stack = Minecraft.func_71410_x().func_175606_aa().field_70173_aa / 40 % tile.recipeHash.size();
         CrucibleRecipe recipe = ThaumcraftApi.getCrucibleRecipeFromHash((Integer)tile.recipeHash.get(stack));
         if (recipe != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par2 + 0.5F + (float)facing.func_82601_c() / 1.99F, (float)par4 + 1.125F, (float)par6 + 0.5F + (float)facing.func_82599_e() / 1.99F);
            switch(facing.ordinal()) {
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

            GL11.glScaled(0.75D, 0.75D, 0.75D);
            ItemStack is = recipe.getRecipeOutput().func_77946_l();
            is.field_77994_a = 1;
            this.entityitem = new EntityItem(tile.func_145831_w(), 0.0D, 0.0D, 0.0D, is);
            this.entityitem.field_70290_d = 0.0F;
            RenderManager rendermanager = Minecraft.func_71410_x().func_175598_ae();
            rendermanager.func_188391_a(this.entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
            GL11.glPopMatrix();
         }
      }

   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int q) {
      this.renderTileEntityAt((TileThaumatorium)par1TileEntity, par2, par4, par6, par8);
   }
}
