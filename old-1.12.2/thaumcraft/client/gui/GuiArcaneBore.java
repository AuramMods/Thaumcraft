package thaumcraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.container.ContainerArcaneBore;
import thaumcraft.common.entities.construct.EntityArcaneBore;

@SideOnly(Side.CLIENT)
public class GuiArcaneBore extends GuiContainer {
   EntityArcaneBore turret;
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_arcanebore.png");

   public GuiArcaneBore(InventoryPlayer par1InventoryPlayer, World world, EntityArcaneBore t) {
      super(new ContainerArcaneBore(par1InventoryPlayer, world, t));
      this.field_146999_f = 175;
      this.field_147000_g = 232;
      this.turret = t;
   }

   protected void func_146979_b(int par1, int par2) {
   }

   protected void func_146976_a(float par1, int par2, int par3) {
      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      GL11.glEnable(3042);
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      int h = (int)(39.0F * (this.turret.func_110143_aJ() / this.turret.func_110138_aP()));
      this.func_73729_b(k + 68, l + 59, 192, 48, h, 6);
      if (this.turret.func_184614_ca() != null && this.turret.func_184614_ca().func_77952_i() + 1 >= this.turret.func_184614_ca().func_77958_k()) {
         this.func_73729_b(k + 80, l + 29, 240, 0, 16, 16);
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)(k + 124), (float)(l + 18), 505.0F);
      GL11.glScalef(0.5F, 0.5F, 0.0F);
      String text = "Width: " + (1 + this.turret.getDigRadius() * 2);
      this.field_146289_q.func_175063_a(text, 0.0F, 0.0F, 16777215);
      text = "Depth: " + this.turret.getDigDepth();
      this.field_146289_q.func_175063_a(text, 64.0F, 0.0F, 16777215);
      text = "Speed: +" + this.turret.getDigSpeed();
      this.field_146289_q.func_175063_a(text, 0.0F, 10.0F, 16777215);
      int base = 0;
      int refining = this.turret.getRefining();
      int fortune = this.turret.getFortune();
      if (this.turret.hasSilkTouch() || refining > 0 || fortune > 0) {
         text = "Other properties:";
         this.field_146289_q.func_175063_a(text, 0.0F, 24.0F, 16777215);
      }

      if (refining > 0) {
         text = "Refining " + refining;
         this.field_146289_q.func_175063_a(text, 4.0F, (float)(34 + base), 12632256);
         base += 9;
      }

      if (fortune > 0) {
         text = "Fortune " + fortune;
         this.field_146289_q.func_175063_a(text, 4.0F, (float)(34 + base), 15648330);
         base += 9;
      }

      if (this.turret.hasSilkTouch()) {
         text = "Silk Touch";
         this.field_146289_q.func_175063_a(text, 4.0F, (float)(34 + base), 8421631);
         base += 9;
      }

      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glDisable(3042);
   }
}
