package thaumcraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.container.ContainerFocusPouch;

@SideOnly(Side.CLIENT)
public class GuiFocusPouch extends GuiContainer {
   private int blockSlot;
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_focuspouch.png");

   public GuiFocusPouch(InventoryPlayer par1InventoryPlayer, World world, int x, int y, int z) {
      super(new ContainerFocusPouch(par1InventoryPlayer, world, x, y, z));
      this.blockSlot = par1InventoryPlayer.field_70461_c;
      this.field_146999_f = 175;
      this.field_147000_g = 232;
   }

   protected void func_146979_b(int par1, int par2) {
      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      float t = this.field_73735_i;
      this.field_73735_i = 200.0F;
      GL11.glEnable(3042);
      this.func_73729_b(8 + this.blockSlot * 18, 209, 240, 0, 16, 16);
      GL11.glDisable(3042);
      this.field_73735_i = t;
   }

   protected boolean func_146983_a(int par1) {
      return false;
   }

   protected void func_146976_a(float par1, int par2, int par3) {
      if (this.field_146297_k.field_71439_g.field_71071_by.field_70462_a[this.blockSlot] == null) {
         this.field_146297_k.field_71439_g.func_71053_j();
      }

      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = (this.field_146294_l - this.field_146999_f) / 2;
      int var6 = (this.field_146295_m - this.field_147000_g) / 2;
      GL11.glEnable(3042);
      this.func_73729_b(var5, var6, 0, 0, this.field_146999_f, this.field_147000_g);
      GL11.glDisable(3042);
   }
}
