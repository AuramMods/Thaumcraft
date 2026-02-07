package thaumcraft.client.renderers.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.lib.ender.ShaderCallback;
import thaumcraft.client.lib.ender.ShaderHelper;

public class TileHoleRenderer extends TileEntitySpecialRenderer {
   private final ShaderCallback shaderCallback = new ShaderCallback() {
      public void call(int shader) {
         Minecraft mc = Minecraft.func_71410_x();
         int x = ARBShaderObjects.glGetUniformLocationARB(shader, "yaw");
         ARBShaderObjects.glUniform1fARB(x, (float)((double)(mc.field_71439_g.field_70177_z * 2.0F) * 3.141592653589793D / 360.0D));
         int z = ARBShaderObjects.glGetUniformLocationARB(shader, "pitch");
         ARBShaderObjects.glUniform1fARB(z, -((float)((double)(mc.field_71439_g.field_70125_A * 2.0F) * 3.141592653589793D / 360.0D)));
      }
   };
   private static final ResourceLocation starsTexture = new ResourceLocation("textures/entity/end_portal.png");

   public void func_180535_a(TileEntity te, double x, double y, double z, float f, int p_180535_9_) {
      GL11.glPushMatrix();
      this.func_147499_a(starsTexture);
      ShaderHelper.useShader(ShaderHelper.endShader, this.shaderCallback);
      GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
      EnumFacing[] var10 = EnumFacing.values();
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         EnumFacing face = var10[var12];
         IBlockState bs = te.func_145831_w().func_180495_p(te.func_174877_v().func_177972_a(face));
         if (bs.func_185914_p() && bs.func_177230_c() != BlocksTC.hole) {
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, (float)(-face.func_96559_d()), (float)face.func_82601_c(), (float)(-face.func_82599_e()));
            if (face.func_82599_e() < 0) {
               GL11.glTranslated(0.0D, 0.0D, -0.49900001287460327D);
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else {
               GL11.glTranslated(0.0D, 0.0D, 0.49900001287460327D);
            }

            GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
            UtilsFX.renderQuadCentered();
            GL11.glPopMatrix();
         }
      }

      ShaderHelper.releaseShader();
      GL11.glPopMatrix();
   }
}
