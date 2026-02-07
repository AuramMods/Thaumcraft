package thaumcraft.client.renderers.entity;

import java.util.Random;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RenderSpecialItem extends RenderEntityItem {
   public RenderSpecialItem(RenderManager p_i46167_1_, RenderItem p_i46167_2_) {
      super(p_i46167_1_, p_i46167_2_);
   }

   public void func_76986_a(EntityItem e, double x, double y, double z, float p_177075_8_, float pt) {
      Random random = new Random(187L);
      float var11 = MathHelper.func_76126_a(((float)e.func_174872_o() + pt) / 10.0F + e.field_70290_d) * 0.1F + 0.1F;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x, (float)y + var11 + 0.25F, (float)z);
      int q = !FMLClientHandler.instance().getClient().field_71474_y.field_74347_j ? 5 : 10;
      Tessellator tessellator = Tessellator.func_178181_a();
      VertexBuffer wr = tessellator.func_178180_c();
      RenderHelper.func_74518_a();
      float f1 = (float)e.func_174872_o() / 500.0F;
      float f3 = 0.9F;
      float f2 = 0.0F;
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();

      for(int i = 0; i < q; ++i) {
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
         wr.func_181668_a(6, DefaultVertexFormats.field_181706_f);
         float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
         float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
         fa /= 30.0F / ((float)Math.min(e.func_174872_o(), 10) / 10.0F);
         f4 /= 30.0F / ((float)Math.min(e.func_174872_o(), 10) / 10.0F);
         wr.func_181662_b(0.0D, 0.0D, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F - f2).func_181675_d();
         wr.func_181662_b(-0.866D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181666_a(1.0F, 0.0F, 1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(0.866D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181666_a(1.0F, 0.0F, 1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(0.0D, (double)fa, (double)(1.0F * f4)).func_181666_a(1.0F, 0.0F, 1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(-0.866D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181666_a(1.0F, 0.0F, 1.0F, 0.0F).func_181675_d();
         tessellator.func_78381_a();
      }

      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.func_74519_b();
      GL11.glPopMatrix();
      super.func_76986_a(e, x, y, z, p_177075_8_, pt);
   }
}
