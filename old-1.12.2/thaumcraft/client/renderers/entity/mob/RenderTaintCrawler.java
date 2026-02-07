package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;

@SideOnly(Side.CLIENT)
public class RenderTaintCrawler extends RenderLiving {
   private static final ResourceLocation textures = new ResourceLocation("thaumcraft", "textures/models/creature/crawler.png");

   public RenderTaintCrawler(RenderManager p_i46144_1_) {
      super(p_i46144_1_, new ModelSilverfish(), 0.2F);
   }

   protected float func_180584_a(EntityTaintCrawler p_180584_1_) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(EntityTaintCrawler entity) {
      return textures;
   }

   protected float func_77037_a(EntityLivingBase p_77037_1_) {
      return this.func_180584_a((EntityTaintCrawler)p_77037_1_);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return this.getEntityTexture((EntityTaintCrawler)entity);
   }

   protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
      GL11.glScalef(0.7F, 0.7F, 0.7F);
   }
}
