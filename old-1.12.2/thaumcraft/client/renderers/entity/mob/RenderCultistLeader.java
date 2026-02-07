package thaumcraft.client.renderers.entity.mob;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.boss.EntityCultistLeader;

@SideOnly(Side.CLIENT)
public class RenderCultistLeader extends RenderBiped<EntityCultistLeader> {
   private static final ResourceLocation skin = new ResourceLocation("thaumcraft", "textures/models/creature/cultist.png");
   private static final ResourceLocation fl = new ResourceLocation("thaumcraft", "textures/misc/wispy.png");

   public RenderCultistLeader(RenderManager p_i46127_1_) {
      super(p_i46127_1_, new ModelBiped(), 0.5F);
      this.func_177094_a(new LayerHeldItem(this));
      LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
         protected void func_177177_a() {
            this.field_177189_c = new ModelBiped();
            this.field_177186_d = new ModelBiped();
         }
      };
      this.func_177094_a(layerbipedarmor);
   }

   protected ResourceLocation getEntityTexture(EntityCultistLeader p_110775_1_) {
      return skin;
   }

   private void drawFloatyLine(double x, double y, double z, double x2, double y2, double z2, float partialTicks, int color, float speed, float distance, float width) {
      Entity player = Minecraft.func_71410_x().func_175606_aa();
      double iPX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)partialTicks;
      double iPY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)partialTicks;
      double iPZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)partialTicks;
      GL11.glTranslated(-iPX + x2, -iPY + y2, -iPZ + z2);
      float time = (float)(System.nanoTime() / 30000000L);
      Color co = new Color(color);
      float r = (float)co.getRed() / 255.0F;
      float g = (float)co.getGreen() / 255.0F;
      float b = (float)co.getBlue() / 255.0F;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      Tessellator tessellator = Tessellator.func_178181_a();
      double dc1x = (double)((float)(x - x2));
      double dc1y = (double)((float)(y - y2));
      double dc1z = (double)((float)(z - z2));
      this.func_110776_a(fl);
      tessellator.func_178180_c().func_181668_a(5, DefaultVertexFormats.field_181709_i);
      double dx2 = 0.0D;
      double dy2 = 0.0D;
      double dz2 = 0.0D;
      double d3 = x - x2;
      double d4 = y - y2;
      double d5 = z - z2;
      float dist = MathHelper.func_76133_a(d3 * d3 + d4 * d4 + d5 * d5);
      float blocks = (float)Math.round(dist);
      float length = blocks * 6.0F;
      float f9 = 0.0F;
      float f10 = 1.0F;

      double dz;
      float f13;
      int i;
      float f2;
      float f2a;
      float f3;
      double dx;
      double dy;
      for(i = 0; (float)i <= length * distance; ++i) {
         f2 = (float)i / length;
         f2a = (float)i * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         f3 = 1.0F - Math.abs((float)i - length / 2.0F) / (length / 2.0F);
         dx = dc1x + (double)(MathHelper.func_76126_a((float)((z % 16.0D + (double)(dist * (1.0F - f2) * 6.0F) - (double)(time % 32767.0F / 5.0F)) / 4.0D)) * 0.5F * f3);
         dy = dc1y + (double)(MathHelper.func_76126_a((float)((x % 16.0D + (double)(dist * (1.0F - f2) * 6.0F) - (double)(time % 32767.0F / 5.0F)) / 3.0D)) * 0.5F * f3);
         dz = dc1z + (double)(MathHelper.func_76126_a((float)((y % 16.0D + (double)(dist * (1.0F - f2) * 6.0F) - (double)(time % 32767.0F / 5.0F)) / 2.0D)) * 0.5F * f3);
         f13 = (1.0F - f2) * dist - time * speed;
         tessellator.func_178180_c().func_181662_b(dx * (double)f2, dy * (double)f2 - (double)width, dz * (double)f2).func_187315_a((double)f13, (double)f10).func_181666_a(r, g, b, 0.8F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(dx * (double)f2, dy * (double)f2 + (double)width, dz * (double)f2).func_187315_a((double)f13, (double)f9).func_181666_a(r, g, b, 0.8F).func_181675_d();
      }

      tessellator.func_78381_a();
      tessellator.func_178180_c().func_181668_a(5, DefaultVertexFormats.field_181709_i);

      for(i = 0; (float)i <= length * distance; ++i) {
         f2 = (float)i / length;
         f2a = (float)i * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         f3 = 1.0F - Math.abs((float)i - length / 2.0F) / (length / 2.0F);
         dx = dc1x + (double)(MathHelper.func_76126_a((float)((z % 16.0D + (double)(dist * (1.0F - f2) * 6.0F) - (double)(time % 32767.0F / 5.0F)) / 4.0D)) * 0.5F * f3);
         dy = dc1y + (double)(MathHelper.func_76126_a((float)((x % 16.0D + (double)(dist * (1.0F - f2) * 6.0F) - (double)(time % 32767.0F / 5.0F)) / 3.0D)) * 0.5F * f3);
         dz = dc1z + (double)(MathHelper.func_76126_a((float)((y % 16.0D + (double)(dist * (1.0F - f2) * 6.0F) - (double)(time % 32767.0F / 5.0F)) / 2.0D)) * 0.5F * f3);
         f13 = (1.0F - f2) * dist - time * speed;
         tessellator.func_178180_c().func_181662_b(dx * (double)f2 - (double)width, dy * (double)f2, dz * (double)f2).func_187315_a((double)f13, (double)f10).func_181666_a(r, g, b, 0.8F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(dx * (double)f2 + (double)width, dy * (double)f2, dz * (double)f2).func_187315_a((double)f13, (double)f9).func_181666_a(r, g, b, 0.8F).func_181675_d();
      }

      tessellator.func_78381_a();
      GL11.glDisable(3042);
   }
}
