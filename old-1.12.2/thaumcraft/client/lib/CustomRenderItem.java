package thaumcraft.client.lib;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CustomRenderItem extends RenderItem {
   public CustomRenderItem() {
      super((TextureManager)null, Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178083_a(), (ItemColors)null);
   }

   public void func_175041_b() {
   }

   public void func_180453_a(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text) {
      if (stack != null && stack.field_77994_a <= 0) {
         text = TextFormatting.GOLD + "*";
      }

      Minecraft.func_71410_x().func_175599_af().func_180453_a(fr, stack, xPosition, yPosition, text);
   }

   protected void func_175048_a(Item itm, int subType, String identifier) {
   }

   protected void func_175029_a(Block blk, int subType, String identifier) {
   }

   public void func_175039_a(boolean p_175039_1_) {
      Minecraft.func_71410_x().func_175599_af().func_175039_a(p_175039_1_);
   }

   public ItemModelMesher func_175037_a() {
      return Minecraft.func_71410_x().func_175599_af().func_175037_a();
   }

   public void func_180454_a(ItemStack stack, IBakedModel model) {
      Minecraft.func_71410_x().func_175599_af().func_180454_a(stack, model);
   }

   public boolean func_175050_a(ItemStack stack) {
      return Minecraft.func_71410_x().func_175599_af().func_175050_a(stack);
   }

   public void func_181564_a(ItemStack p_181564_1_, TransformType p_181564_2_) {
      Minecraft.func_71410_x().func_175599_af().func_181564_a(p_181564_1_, p_181564_2_);
   }

   public IBakedModel func_184393_a(ItemStack p_184393_1_, World p_184393_2_, EntityLivingBase p_184393_3_) {
      return Minecraft.func_71410_x().func_175599_af().func_184393_a(p_184393_1_, p_184393_2_, p_184393_3_);
   }

   public void func_184392_a(ItemStack p_184392_1_, EntityLivingBase p_184392_2_, TransformType p_184392_3_, boolean p_184392_4_) {
      Minecraft.func_71410_x().func_175599_af().func_184392_a(p_184392_1_, p_184392_2_, p_184392_3_, p_184392_4_);
   }

   public void func_184391_a(EntityLivingBase p_184391_1_, ItemStack p_184391_2_, int p_184391_3_, int p_184391_4_) {
      Minecraft.func_71410_x().func_175599_af().func_184391_a(p_184391_1_, p_184391_2_, p_184391_3_, p_184391_4_);
   }

   public void func_175042_a(ItemStack stack, int x, int y) {
      Minecraft.func_71410_x().func_175599_af().func_175042_a(stack, x, y);
   }

   public void func_180450_b(ItemStack stack, int xPosition, int yPosition) {
      Minecraft.func_71410_x().func_175599_af().func_180450_b(stack, xPosition, yPosition);
   }

   public void func_175030_a(FontRenderer fr, ItemStack stack, int xPosition, int yPosition) {
      Minecraft.func_71410_x().func_175599_af().func_175030_a(fr, stack, xPosition, yPosition);
   }

   public void func_110549_a(IResourceManager resourceManager) {
      Minecraft.func_71410_x().func_175599_af().func_110549_a(resourceManager);
   }
}
