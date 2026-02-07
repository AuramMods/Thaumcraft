package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entity.ModelEldritchGuardian;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;

@SideOnly(Side.CLIENT)
public class RenderEldritchGuardian extends RenderLiving {
   protected ModelEldritchGuardian modelMain;
   private static final ResourceLocation[] skin = new ResourceLocation[]{new ResourceLocation("thaumcraft", "textures/models/creature/eldritch_guardian.png"), new ResourceLocation("thaumcraft", "textures/models/creature/eldritch_warden.png")};

   public RenderEldritchGuardian(RenderManager rm, ModelEldritchGuardian par1ModelBiped, float par2) {
      super(rm, par1ModelBiped, par2);
      this.modelMain = par1ModelBiped;
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return entity instanceof EntityEldritchWarden ? skin[1] : skin[0];
   }

   public void doRenderLiving(EntityLiving guardian, double par2, double par4, double par6, float par8, float par9) {
      GL11.glEnable(3042);
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glBlendFunc(770, 771);
      float base = 1.0F;
      double d3 = par4;
      if (guardian instanceof EntityEldritchWarden) {
         d3 = par4 - (double)(guardian.field_70131_O * ((float)((EntityEldritchWarden)guardian).getSpawnTimer() / 150.0F));
      } else {
         Entity e = Minecraft.func_71410_x().func_175606_aa();
         float d6 = e.field_70170_p.func_175659_aa() == EnumDifficulty.HARD ? 576.0F : 1024.0F;
         float d7 = 256.0F;
         if (guardian.field_70170_p != null && guardian.field_70170_p.field_73011_w.getDimension() == Config.dimensionOuterId) {
            base = 1.0F;
         } else {
            double d8 = guardian.func_70092_e(e.field_70165_t, e.field_70163_u, e.field_70161_v);
            if (d8 < 256.0D) {
               base = 0.6F;
            } else {
               base = (float)(1.0D - Math.min((double)(d6 - d7), d8 - (double)d7) / (double)(d6 - d7)) * 0.6F;
            }
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, base);
      super.func_76986_a(guardian, par2, d3, par6, par8, par9);
      GL11.glDisable(3042);
      GL11.glAlphaFunc(516, 0.1F);
   }

   public void func_76986_a(EntityLiving par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving(par1Entity, par2, par4, par6, par8, par9);
   }
}
