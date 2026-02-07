package thaumcraft.client.renderers.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.entities.EntityFallingTaint;

@SideOnly(Side.CLIENT)
public class RenderFallingTaint extends Render {
   public RenderFallingTaint(RenderManager p_i46177_1_) {
      super(p_i46177_1_);
      this.field_76989_e = 0.5F;
   }

   public void doRender(EntityFallingTaint p_180557_1_, double p_180557_2_, double p_180557_4_, double p_180557_6_, float p_180557_8_, float p_180557_9_) {
      if (p_180557_1_.getBlock() != null) {
         this.func_110776_a(TextureMap.field_110575_b);
         IBlockState iblockstate = p_180557_1_.getBlock();
         Block block = iblockstate.func_177230_c();
         BlockPos blockpos = new BlockPos(p_180557_1_);
         World world = p_180557_1_.getWorld();
         if (iblockstate != world.func_180495_p(blockpos) && block.func_149645_b(iblockstate) != EnumBlockRenderType.INVISIBLE && block.func_149645_b(iblockstate) == EnumBlockRenderType.MODEL) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)p_180557_2_, (float)p_180557_4_, (float)p_180557_6_);
            GlStateManager.func_179140_f();
            Tessellator tessellator = Tessellator.func_178181_a();
            VertexBuffer VertexBuffer = tessellator.func_178180_c();
            VertexBuffer.func_181668_a(7, DefaultVertexFormats.field_176600_a);
            int i = blockpos.func_177958_n();
            int j = blockpos.func_177956_o();
            int k = blockpos.func_177952_p();
            VertexBuffer.func_178969_c((double)((float)(-i) - 0.5F), (double)(-j), (double)((float)(-k) - 0.5F));
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.func_71410_x().func_175602_ab();
            IBakedModel ibakedmodel = blockrendererdispatcher.func_184389_a(iblockstate);
            blockrendererdispatcher.func_175019_b().func_178267_a(world, ibakedmodel, iblockstate, blockpos, VertexBuffer, false);
            VertexBuffer.func_178969_c(0.0D, 0.0D, 0.0D);
            tessellator.func_78381_a();
            GlStateManager.func_179145_e();
            GlStateManager.func_179121_F();
            super.func_76986_a(p_180557_1_, p_180557_2_, p_180557_4_, p_180557_6_, p_180557_8_, p_180557_9_);
         }
      }

   }

   protected ResourceLocation getEntityTexture(EntityFallingBlock entity) {
      return TextureMap.field_110575_b;
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return this.getEntityTexture((EntityFallingBlock)entity);
   }

   public void func_76986_a(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
      this.doRender((EntityFallingTaint)entity, x, y, z, p_76986_8_, partialTicks);
   }
}
