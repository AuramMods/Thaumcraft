package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHeldItemPech extends LayerHeldItem {
   public LayerHeldItemPech(RenderLivingBase<?> livingEntityRendererIn) {
      super(livingEntityRendererIn);
   }

   public void func_177141_a(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      boolean flag = entitylivingbaseIn.func_184591_cq() == EnumHandSide.RIGHT;
      ItemStack itemstack = flag ? entitylivingbaseIn.func_184592_cb() : entitylivingbaseIn.func_184614_ca();
      ItemStack itemstack1 = flag ? entitylivingbaseIn.func_184614_ca() : entitylivingbaseIn.func_184592_cb();
      if (itemstack != null || itemstack1 != null) {
         GlStateManager.func_179094_E();
         if (this.field_177206_a.func_177087_b().field_78091_s) {
            float f = 0.5F;
            GlStateManager.func_179109_b(0.0F, 0.625F, 0.0F);
            GlStateManager.func_179114_b(-20.0F, -1.0F, 0.0F, 0.0F);
            GlStateManager.func_179152_a(f, f, f);
         }

         this.renderHeldItem(entitylivingbaseIn, itemstack1, TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
         this.renderHeldItem(entitylivingbaseIn, itemstack, TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
         GlStateManager.func_179121_F();
      }

   }

   private void renderHeldItem(EntityLivingBase p_188358_1_, ItemStack p_188358_2_, TransformType p_188358_3_, EnumHandSide p_188358_4_) {
      if (p_188358_2_ != null) {
         GlStateManager.func_179094_E();
         ((ModelBiped)this.field_177206_a.func_177087_b()).func_187073_a(0.0625F, p_188358_4_);
         GlStateManager.func_179109_b(0.0F, -0.1F, 0.0625F);
         if (p_188358_2_.func_77973_b() instanceof ItemBow) {
            GlStateManager.func_179137_b(-0.07500000298023224D, -0.1D, 0.0D);
         }

         if (p_188358_1_.func_70093_af()) {
            GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
         }

         GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         boolean flag = p_188358_4_ == EnumHandSide.LEFT;
         GlStateManager.func_179109_b(flag ? -0.0625F : 0.0625F, 0.125F, -0.625F);
         Minecraft.func_71410_x().func_175597_ag().func_187462_a(p_188358_1_, p_188358_2_, p_188358_3_, flag);
         GlStateManager.func_179121_F();
      }

   }

   public boolean func_177142_b() {
      return false;
   }
}
