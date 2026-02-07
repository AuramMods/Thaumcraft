package thaumcraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.container.ContainerHandMirror;

@SideOnly(Side.CLIENT)
public class GuiHandMirror extends GuiContainer {
   int ci = 0;
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_handmirror.png");

   public GuiHandMirror(InventoryPlayer par1InventoryPlayer, World world, int x, int y, int z) {
      super(new ContainerHandMirror(par1InventoryPlayer, world, x, y, z));
      this.ci = par1InventoryPlayer.field_70461_c;
   }

   protected void drawGuiContainerForegroundLayer() {
   }

   protected boolean func_146983_a(int par1) {
      return false;
   }

   protected void func_146976_a(float par1, int par2, int par3) {
      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = (this.field_146294_l - this.field_146999_f) / 2;
      int var6 = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(var5, var6, 0, 0, this.field_146999_f, this.field_147000_g);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.func_73729_b(var5 + 8 + this.ci * 18, var6 + 142, 240, 0, 16, 16);
      GL11.glDisable(3042);
   }
}
