package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entity.ModelFireBat;
import thaumcraft.common.entities.monster.EntityFireBat;

@SideOnly(Side.CLIENT)
public class RenderFireBat extends RenderLiving {
   private int renderedBatSize;
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/creature/firebat.png");
   private static final ResourceLocation rl2 = new ResourceLocation("thaumcraft", "textures/models/creature/vampirebat.png");

   public RenderFireBat(RenderManager rm) {
      super(rm, new ModelFireBat(), 0.25F);
      this.renderedBatSize = ((ModelFireBat)this.field_77045_g).getBatSize();
   }

   public void func_82443_a(EntityFireBat par1EntityBat, double par2, double par4, double par6, float par8, float par9) {
      int var10 = ((ModelFireBat)this.field_77045_g).getBatSize();
      if (var10 != this.renderedBatSize) {
         this.renderedBatSize = var10;
         this.field_77045_g = new ModelBat();
      }

      super.func_76986_a(par1EntityBat, par2, par4, par6, par8, par9);
   }

   protected void func_82442_a(EntityFireBat par1EntityBat, float par2) {
      if (!par1EntityBat.getIsDevil() && !par1EntityBat.getIsVampire()) {
         GL11.glScalef(0.35F, 0.35F, 0.35F);
      } else {
         GL11.glScalef(0.6F, 0.6F, 0.6F);
      }

   }

   protected void func_82445_a(EntityFireBat par1EntityBat, double par2, double par4, double par6) {
      super.func_77039_a(par1EntityBat, par2, par4, par6);
   }

   protected void func_82444_a(EntityFireBat par1EntityBat, float par2, float par3, float par4) {
      if (!par1EntityBat.getIsBatHanging()) {
         GL11.glTranslatef(0.0F, MathHelper.func_76134_b(par2 * 0.3F) * 0.1F, 0.0F);
      } else {
         GL11.glTranslatef(0.0F, -0.1F, 0.0F);
      }

      super.func_77043_a(par1EntityBat, par2, par3, par4);
   }

   protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
      this.func_82442_a((EntityFireBat)par1EntityLiving, par2);
   }

   protected void func_77043_a(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      this.func_82444_a((EntityFireBat)par1EntityLiving, par2, par3, par4);
   }

   protected void func_77039_a(EntityLivingBase par1EntityLiving, double par2, double par4, double par6) {
      this.func_82445_a((EntityFireBat)par1EntityLiving, par2, par4, par6);
   }

   public void func_76986_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.func_82443_a((EntityFireBat)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return entity instanceof EntityFireBat && ((EntityFireBat)entity).getIsVampire() ? rl2 : rl;
   }
}
