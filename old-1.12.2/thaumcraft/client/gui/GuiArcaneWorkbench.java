package thaumcraft.client.gui;

import java.awt.Color;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.blocks.world.ore.ShardType;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.items.resources.ItemCrystalEssence;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

@SideOnly(Side.CLIENT)
public class GuiArcaneWorkbench extends GuiContainer {
   private TileArcaneWorkbench tileEntity;
   private InventoryPlayer ip;
   private int[][] aspectLocs = new int[][]{{72, 21}, {24, 43}, {24, 102}, {72, 124}, {120, 102}, {120, 43}};
   ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/arcaneworkbench.png");

   public GuiArcaneWorkbench(InventoryPlayer par1InventoryPlayer, TileArcaneWorkbench e) {
      super(new ContainerArcaneWorkbench(par1InventoryPlayer, e));
      this.tileEntity = e;
      this.ip = par1InventoryPlayer;
      this.field_147000_g = 234;
      this.field_146999_f = 190;
   }

   protected void func_146976_a(float par1, int par2, int par3) {
      this.field_146297_k.field_71446_o.func_110577_a(this.tex);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      int var5 = (this.field_146294_l - this.field_146999_f) / 2;
      int var6 = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(var5, var6, 0, 0, this.field_146999_f, this.field_147000_g);
      int cost = 0;
      IArcaneRecipe result = ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tileEntity.inventory, this.ip.field_70458_d);
      ItemStack[] crystals = null;
      if (result != null) {
         cost = result.getVis(this.tileEntity.inventory);
         crystals = result.getCrystals();
      }

      if (crystals != null) {
         GlStateManager.func_179112_b(770, 1);
         ItemStack[] var10 = crystals;
         int var11 = crystals.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            ItemStack cs = var10[var12];
            if (cs != null) {
               Aspect a = ((ItemCrystalEssence)cs.func_77973_b()).getAspects(cs).getAspects()[0];
               if (a != null) {
                  int id = ShardType.getMetaByAspect(a);
                  Color col = new Color(a.getColor());
                  GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, 0.33F);
                  GL11.glPushMatrix();
                  GL11.glTranslatef((float)(var5 + ContainerArcaneWorkbench.xx[id]) + 7.5F, (float)(var6 + ContainerArcaneWorkbench.yy[id]) + 8.0F, 0.0F);
                  GL11.glRotatef((float)(id * 60 + this.field_146297_k.func_175606_aa().field_70173_aa % 360) + par1, 0.0F, 0.0F, 1.0F);
                  GL11.glScalef(0.5F, 0.5F, 0.0F);
                  this.func_73729_b(-32, -32, 192, 0, 64, 64);
                  GL11.glScalef(1.0F, 1.0F, 1.0F);
                  GL11.glPopMatrix();
               }
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179112_b(770, 771);
      }

      GL11.glDisable(3042);
      if (cost > 0) {
         if (this.tileEntity.auraVisClient < cost) {
            GL11.glPushMatrix();
            float var40 = 0.33F;
            GL11.glColor4f(var40, var40, var40, 0.66F);
            GL11.glEnable(2896);
            GL11.glEnable(2884);
            GL11.glEnable(3042);
            this.field_146296_j.func_180450_b(result.func_77572_b(this.tileEntity.inventory), var5 + 160, var6 + 64);
            this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, result.func_77572_b(this.tileEntity.inventory), var5 + 160, var6 + 64, "");
            GlStateManager.func_179145_e();
            GlStateManager.func_179126_j();
            RenderHelper.func_74519_b();
            GL11.glPopMatrix();
            RenderHelper.func_74518_a();
         }

         GL11.glPushMatrix();
         GL11.glTranslatef((float)(var5 + 168), (float)(var6 + 46), 0.0F);
         GL11.glScalef(0.5F, 0.5F, 0.0F);
         String text = this.tileEntity.auraVisClient + " available";
         int ll = this.field_146289_q.func_78256_a(text) / 2;
         this.field_146289_q.func_78276_b(text, -ll, 0, this.tileEntity.auraVisClient < cost ? 15625838 : 7237358);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(var5 + 168), (float)(var6 + 38), 0.0F);
         GL11.glScalef(0.5F, 0.5F, 0.0F);
         text = cost + " vis";
         ll = this.field_146289_q.func_78256_a(text) / 2;
         this.field_146289_q.func_78276_b(text, -ll, 0, 12648447);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

   }
}
