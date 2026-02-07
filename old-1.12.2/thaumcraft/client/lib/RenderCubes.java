package thaumcraft.client.lib;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCubes {
   public IBlockAccess blockAccess;
   public boolean flipTexture;
   public boolean field_152631_f;
   public boolean renderAllFaces;
   public boolean useInventoryTint = true;
   public boolean renderFromInside = false;
   public double renderMinX;
   public double renderMaxX;
   public double renderMinY;
   public double renderMaxY;
   public double renderMinZ;
   public double renderMaxZ;
   public boolean lockBlockBounds;
   public boolean partialRenderBounds;
   public final Minecraft minecraftRB;
   public int uvRotateEast;
   public int uvRotateWest;
   public int uvRotateSouth;
   public int uvRotateNorth;
   public int uvRotateTop;
   public int uvRotateBottom;
   public float aoLightValueScratchXYZNNN;
   public float aoLightValueScratchXYNN;
   public float aoLightValueScratchXYZNNP;
   public float aoLightValueScratchYZNN;
   public float aoLightValueScratchYZNP;
   public float aoLightValueScratchXYZPNN;
   public float aoLightValueScratchXYPN;
   public float aoLightValueScratchXYZPNP;
   public float aoLightValueScratchXYZNPN;
   public float aoLightValueScratchXYNP;
   public float aoLightValueScratchXYZNPP;
   public float aoLightValueScratchYZPN;
   public float aoLightValueScratchXYZPPN;
   public float aoLightValueScratchXYPP;
   public float aoLightValueScratchYZPP;
   public float aoLightValueScratchXYZPPP;
   public float aoLightValueScratchXZNN;
   public float aoLightValueScratchXZPN;
   public float aoLightValueScratchXZNP;
   public float aoLightValueScratchXZPP;
   public int aoBrightnessXYZNNN;
   public int aoBrightnessXYNN;
   public int aoBrightnessXYZNNP;
   public int aoBrightnessYZNN;
   public int aoBrightnessYZNP;
   public int aoBrightnessXYZPNN;
   public int aoBrightnessXYPN;
   public int aoBrightnessXYZPNP;
   public int aoBrightnessXYZNPN;
   public int aoBrightnessXYNP;
   public int aoBrightnessXYZNPP;
   public int aoBrightnessYZPN;
   public int aoBrightnessXYZPPN;
   public int aoBrightnessXYPP;
   public int aoBrightnessYZPP;
   public int aoBrightnessXYZPPP;
   public int aoBrightnessXZNN;
   public int aoBrightnessXZPN;
   public int aoBrightnessXZNP;
   public int aoBrightnessXZPP;
   public int brightnessTopLeft;
   public int brightnessBottomLeft;
   public int brightnessBottomRight;
   public int brightnessTopRight;
   public float colorRedTopLeft;
   public float colorRedBottomLeft;
   public float colorRedBottomRight;
   public float colorRedTopRight;
   public float colorGreenTopLeft;
   public float colorGreenBottomLeft;
   public float colorGreenBottomRight;
   public float colorGreenTopRight;
   public float colorBlueTopLeft;
   public float colorBlueBottomLeft;
   public float colorBlueBottomRight;
   public float colorBlueTopRight;
   private static final String __OBFID = "CL_00000940";
   private static RenderCubes instance;

   public RenderCubes(IBlockAccess p_i1251_1_) {
      this.blockAccess = p_i1251_1_;
      this.field_152631_f = false;
      this.flipTexture = false;
      this.minecraftRB = Minecraft.func_71410_x();
   }

   public RenderCubes() {
      this.minecraftRB = Minecraft.func_71410_x();
   }

   public void setRenderBounds(double p_147782_1_, double p_147782_3_, double p_147782_5_, double p_147782_7_, double p_147782_9_, double p_147782_11_) {
      if (!this.lockBlockBounds) {
         this.renderMinX = p_147782_1_;
         this.renderMaxX = p_147782_7_;
         this.renderMinY = p_147782_3_;
         this.renderMaxY = p_147782_9_;
         this.renderMinZ = p_147782_5_;
         this.renderMaxZ = p_147782_11_;
         this.partialRenderBounds = this.minecraftRB.field_71474_y.field_74348_k >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
      }

   }

   public void overrideBlockBounds(double p_147770_1_, double p_147770_3_, double p_147770_5_, double p_147770_7_, double p_147770_9_, double p_147770_11_) {
      this.renderMinX = p_147770_1_;
      this.renderMaxX = p_147770_7_;
      this.renderMinY = p_147770_3_;
      this.renderMaxY = p_147770_9_;
      this.renderMinZ = p_147770_5_;
      this.renderMaxZ = p_147770_11_;
      this.lockBlockBounds = true;
      this.partialRenderBounds = this.minecraftRB.field_71474_y.field_74348_k >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
   }

   public void renderFaceYNeg(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, TextureAtlasSprite p_147768_8_, float red, float green, float blue, int bright) {
      Tessellator tessellator = Tessellator.func_178181_a();
      double d3 = (double)p_147768_8_.func_94214_a(this.renderMinX * 16.0D);
      double d4 = (double)p_147768_8_.func_94214_a(this.renderMaxX * 16.0D);
      double d5 = (double)p_147768_8_.func_94207_b(this.renderMinZ * 16.0D);
      double d6 = (double)p_147768_8_.func_94207_b(this.renderMaxZ * 16.0D);
      if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
         d3 = (double)p_147768_8_.func_94209_e();
         d4 = (double)p_147768_8_.func_94212_f();
      }

      if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
         d5 = (double)p_147768_8_.func_94206_g();
         d6 = (double)p_147768_8_.func_94210_h();
      }

      double d7 = d4;
      double d8 = d3;
      double d9 = d5;
      double d10 = d6;
      if (this.uvRotateBottom == 2) {
         d3 = (double)p_147768_8_.func_94214_a(this.renderMinZ * 16.0D);
         d5 = (double)p_147768_8_.func_94207_b(16.0D - this.renderMaxX * 16.0D);
         d4 = (double)p_147768_8_.func_94214_a(this.renderMaxZ * 16.0D);
         d6 = (double)p_147768_8_.func_94207_b(16.0D - this.renderMinX * 16.0D);
         d9 = d5;
         d10 = d6;
         d7 = d3;
         d8 = d4;
         d5 = d6;
         d6 = d9;
      } else if (this.uvRotateBottom == 1) {
         d3 = (double)p_147768_8_.func_94214_a(16.0D - this.renderMaxZ * 16.0D);
         d5 = (double)p_147768_8_.func_94207_b(this.renderMinX * 16.0D);
         d4 = (double)p_147768_8_.func_94214_a(16.0D - this.renderMinZ * 16.0D);
         d6 = (double)p_147768_8_.func_94207_b(this.renderMaxX * 16.0D);
         d7 = d4;
         d8 = d3;
         d3 = d4;
         d4 = d8;
         d9 = d6;
         d10 = d5;
      } else if (this.uvRotateBottom == 3) {
         d3 = (double)p_147768_8_.func_94214_a(16.0D - this.renderMinX * 16.0D);
         d4 = (double)p_147768_8_.func_94214_a(16.0D - this.renderMaxX * 16.0D);
         d5 = (double)p_147768_8_.func_94207_b(16.0D - this.renderMinZ * 16.0D);
         d6 = (double)p_147768_8_.func_94207_b(16.0D - this.renderMaxZ * 16.0D);
         d7 = d4;
         d8 = d3;
         d9 = d5;
         d10 = d6;
      }

      double d11 = p_147768_2_ + this.renderMinX;
      double d12 = p_147768_2_ + this.renderMaxX;
      double d13 = p_147768_4_ + this.renderMinY;
      double d14 = p_147768_6_ + this.renderMinZ;
      double d15 = p_147768_6_ + this.renderMaxZ;
      if (this.renderFromInside) {
         d11 = p_147768_2_ + this.renderMaxX;
         d12 = p_147768_2_ + this.renderMinX;
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b(d11, d13, d15).func_187315_a(d8, d10).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d14).func_187315_a(d3, d5).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d13, d14).func_187315_a(d7, d9).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d13, d15).func_187315_a(d4, d6).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
   }

   public void renderFaceYPos(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, TextureAtlasSprite p_147806_8_, float red, float green, float blue, int bright) {
      Tessellator tessellator = Tessellator.func_178181_a();
      double d3 = (double)p_147806_8_.func_94214_a(this.renderMinX * 16.0D);
      double d4 = (double)p_147806_8_.func_94214_a(this.renderMaxX * 16.0D);
      double d5 = (double)p_147806_8_.func_94207_b(this.renderMinZ * 16.0D);
      double d6 = (double)p_147806_8_.func_94207_b(this.renderMaxZ * 16.0D);
      if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
         d3 = (double)p_147806_8_.func_94209_e();
         d4 = (double)p_147806_8_.func_94212_f();
      }

      if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
         d5 = (double)p_147806_8_.func_94206_g();
         d6 = (double)p_147806_8_.func_94210_h();
      }

      double d7 = d4;
      double d8 = d3;
      double d9 = d5;
      double d10 = d6;
      if (this.uvRotateTop == 1) {
         d3 = (double)p_147806_8_.func_94214_a(this.renderMinZ * 16.0D);
         d5 = (double)p_147806_8_.func_94207_b(16.0D - this.renderMaxX * 16.0D);
         d4 = (double)p_147806_8_.func_94214_a(this.renderMaxZ * 16.0D);
         d6 = (double)p_147806_8_.func_94207_b(16.0D - this.renderMinX * 16.0D);
         d9 = d5;
         d10 = d6;
         d7 = d3;
         d8 = d4;
         d5 = d6;
         d6 = d9;
      } else if (this.uvRotateTop == 2) {
         d3 = (double)p_147806_8_.func_94214_a(16.0D - this.renderMaxZ * 16.0D);
         d5 = (double)p_147806_8_.func_94207_b(this.renderMinX * 16.0D);
         d4 = (double)p_147806_8_.func_94214_a(16.0D - this.renderMinZ * 16.0D);
         d6 = (double)p_147806_8_.func_94207_b(this.renderMaxX * 16.0D);
         d7 = d4;
         d8 = d3;
         d3 = d4;
         d4 = d8;
         d9 = d6;
         d10 = d5;
      } else if (this.uvRotateTop == 3) {
         d3 = (double)p_147806_8_.func_94214_a(16.0D - this.renderMinX * 16.0D);
         d4 = (double)p_147806_8_.func_94214_a(16.0D - this.renderMaxX * 16.0D);
         d5 = (double)p_147806_8_.func_94207_b(16.0D - this.renderMinZ * 16.0D);
         d6 = (double)p_147806_8_.func_94207_b(16.0D - this.renderMaxZ * 16.0D);
         d7 = d4;
         d8 = d3;
         d9 = d5;
         d10 = d6;
      }

      double d11 = p_147806_2_ + this.renderMinX;
      double d12 = p_147806_2_ + this.renderMaxX;
      double d13 = p_147806_4_ + this.renderMaxY;
      double d14 = p_147806_6_ + this.renderMinZ;
      double d15 = p_147806_6_ + this.renderMaxZ;
      if (this.renderFromInside) {
         d11 = p_147806_2_ + this.renderMaxX;
         d12 = p_147806_2_ + this.renderMinX;
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b(d12, d13, d15).func_187315_a(d4, d6).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d13, d14).func_187315_a(d7, d9).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d14).func_187315_a(d3, d5).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d15).func_187315_a(d8, d10).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
   }

   public void renderFaceZNeg(Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_, TextureAtlasSprite p_147761_8_, float red, float green, float blue, int bright) {
      Tessellator tessellator = Tessellator.func_178181_a();
      double d3 = (double)p_147761_8_.func_94214_a(this.renderMinX * 16.0D);
      double d4 = (double)p_147761_8_.func_94214_a(this.renderMaxX * 16.0D);
      if (this.field_152631_f) {
         d4 = (double)p_147761_8_.func_94214_a((1.0D - this.renderMinX) * 16.0D);
         d3 = (double)p_147761_8_.func_94214_a((1.0D - this.renderMaxX) * 16.0D);
      }

      double d5 = (double)p_147761_8_.func_94207_b(16.0D - this.renderMaxY * 16.0D);
      double d6 = (double)p_147761_8_.func_94207_b(16.0D - this.renderMinY * 16.0D);
      double d7;
      if (this.flipTexture) {
         d7 = d3;
         d3 = d4;
         d4 = d7;
      }

      if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
         d3 = (double)p_147761_8_.func_94209_e();
         d4 = (double)p_147761_8_.func_94212_f();
      }

      if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
         d5 = (double)p_147761_8_.func_94206_g();
         d6 = (double)p_147761_8_.func_94210_h();
      }

      d7 = d4;
      double d8 = d3;
      double d9 = d5;
      double d10 = d6;
      if (this.uvRotateEast == 2) {
         d3 = (double)p_147761_8_.func_94214_a(this.renderMinY * 16.0D);
         d4 = (double)p_147761_8_.func_94214_a(this.renderMaxY * 16.0D);
         d5 = (double)p_147761_8_.func_94207_b(16.0D - this.renderMinX * 16.0D);
         d6 = (double)p_147761_8_.func_94207_b(16.0D - this.renderMaxX * 16.0D);
         d9 = d5;
         d10 = d6;
         d7 = d3;
         d8 = d4;
         d5 = d6;
         d6 = d9;
      } else if (this.uvRotateEast == 1) {
         d3 = (double)p_147761_8_.func_94214_a(16.0D - this.renderMaxY * 16.0D);
         d4 = (double)p_147761_8_.func_94214_a(16.0D - this.renderMinY * 16.0D);
         d5 = (double)p_147761_8_.func_94207_b(this.renderMaxX * 16.0D);
         d6 = (double)p_147761_8_.func_94207_b(this.renderMinX * 16.0D);
         d7 = d4;
         d8 = d3;
         d3 = d4;
         d4 = d8;
         d9 = d6;
         d10 = d5;
      } else if (this.uvRotateEast == 3) {
         d3 = (double)p_147761_8_.func_94214_a(16.0D - this.renderMinX * 16.0D);
         d4 = (double)p_147761_8_.func_94214_a(16.0D - this.renderMaxX * 16.0D);
         d5 = (double)p_147761_8_.func_94207_b(this.renderMaxY * 16.0D);
         d6 = (double)p_147761_8_.func_94207_b(this.renderMinY * 16.0D);
         d7 = d4;
         d8 = d3;
         d9 = d5;
         d10 = d6;
      }

      double d11 = p_147761_2_ + this.renderMinX;
      double d12 = p_147761_2_ + this.renderMaxX;
      double d13 = p_147761_4_ + this.renderMinY;
      double d14 = p_147761_4_ + this.renderMaxY;
      double d15 = p_147761_6_ + this.renderMinZ;
      if (this.renderFromInside) {
         d11 = p_147761_2_ + this.renderMaxX;
         d12 = p_147761_2_ + this.renderMinX;
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b(d11, d14, d15).func_187315_a(d7, d9).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d14, d15).func_187315_a(d3, d5).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d13, d15).func_187315_a(d8, d10).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d15).func_187315_a(d4, d6).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
   }

   public void renderFaceZPos(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, TextureAtlasSprite p_147734_8_, float red, float green, float blue, int bright) {
      Tessellator tessellator = Tessellator.func_178181_a();
      double d3 = (double)p_147734_8_.func_94214_a(this.renderMinX * 16.0D);
      double d4 = (double)p_147734_8_.func_94214_a(this.renderMaxX * 16.0D);
      double d5 = (double)p_147734_8_.func_94207_b(16.0D - this.renderMaxY * 16.0D);
      double d6 = (double)p_147734_8_.func_94207_b(16.0D - this.renderMinY * 16.0D);
      double d7;
      if (this.flipTexture) {
         d7 = d3;
         d3 = d4;
         d4 = d7;
      }

      if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
         d3 = (double)p_147734_8_.func_94209_e();
         d4 = (double)p_147734_8_.func_94212_f();
      }

      if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
         d5 = (double)p_147734_8_.func_94206_g();
         d6 = (double)p_147734_8_.func_94210_h();
      }

      d7 = d4;
      double d8 = d3;
      double d9 = d5;
      double d10 = d6;
      if (this.uvRotateWest == 1) {
         d3 = (double)p_147734_8_.func_94214_a(this.renderMinY * 16.0D);
         d6 = (double)p_147734_8_.func_94207_b(16.0D - this.renderMinX * 16.0D);
         d4 = (double)p_147734_8_.func_94214_a(this.renderMaxY * 16.0D);
         d5 = (double)p_147734_8_.func_94207_b(16.0D - this.renderMaxX * 16.0D);
         d9 = d5;
         d10 = d6;
         d7 = d3;
         d8 = d4;
         d5 = d6;
         d6 = d9;
      } else if (this.uvRotateWest == 2) {
         d3 = (double)p_147734_8_.func_94214_a(16.0D - this.renderMaxY * 16.0D);
         d5 = (double)p_147734_8_.func_94207_b(this.renderMinX * 16.0D);
         d4 = (double)p_147734_8_.func_94214_a(16.0D - this.renderMinY * 16.0D);
         d6 = (double)p_147734_8_.func_94207_b(this.renderMaxX * 16.0D);
         d7 = d4;
         d8 = d3;
         d3 = d4;
         d4 = d8;
         d9 = d6;
         d10 = d5;
      } else if (this.uvRotateWest == 3) {
         d3 = (double)p_147734_8_.func_94214_a(16.0D - this.renderMinX * 16.0D);
         d4 = (double)p_147734_8_.func_94214_a(16.0D - this.renderMaxX * 16.0D);
         d5 = (double)p_147734_8_.func_94207_b(this.renderMaxY * 16.0D);
         d6 = (double)p_147734_8_.func_94207_b(this.renderMinY * 16.0D);
         d7 = d4;
         d8 = d3;
         d9 = d5;
         d10 = d6;
      }

      double d11 = p_147734_2_ + this.renderMinX;
      double d12 = p_147734_2_ + this.renderMaxX;
      double d13 = p_147734_4_ + this.renderMinY;
      double d14 = p_147734_4_ + this.renderMaxY;
      double d15 = p_147734_6_ + this.renderMaxZ;
      if (this.renderFromInside) {
         d11 = p_147734_2_ + this.renderMaxX;
         d12 = p_147734_2_ + this.renderMinX;
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b(d11, d14, d15).func_187315_a(d3, d5).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d15).func_187315_a(d8, d10).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d13, d15).func_187315_a(d4, d6).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d12, d14, d15).func_187315_a(d7, d9).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
   }

   public void renderFaceXNeg(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, TextureAtlasSprite p_147798_8_, float red, float green, float blue, int bright) {
      Tessellator tessellator = Tessellator.func_178181_a();
      double d3 = (double)p_147798_8_.func_94214_a(this.renderMinZ * 16.0D);
      double d4 = (double)p_147798_8_.func_94214_a(this.renderMaxZ * 16.0D);
      double d5 = (double)p_147798_8_.func_94207_b(16.0D - this.renderMaxY * 16.0D);
      double d6 = (double)p_147798_8_.func_94207_b(16.0D - this.renderMinY * 16.0D);
      double d7;
      if (this.flipTexture) {
         d7 = d3;
         d3 = d4;
         d4 = d7;
      }

      if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
         d3 = (double)p_147798_8_.func_94209_e();
         d4 = (double)p_147798_8_.func_94212_f();
      }

      if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
         d5 = (double)p_147798_8_.func_94206_g();
         d6 = (double)p_147798_8_.func_94210_h();
      }

      d7 = d4;
      double d8 = d3;
      double d9 = d5;
      double d10 = d6;
      if (this.uvRotateNorth == 1) {
         d3 = (double)p_147798_8_.func_94214_a(this.renderMinY * 16.0D);
         d5 = (double)p_147798_8_.func_94207_b(16.0D - this.renderMaxZ * 16.0D);
         d4 = (double)p_147798_8_.func_94214_a(this.renderMaxY * 16.0D);
         d6 = (double)p_147798_8_.func_94207_b(16.0D - this.renderMinZ * 16.0D);
         d9 = d5;
         d10 = d6;
         d7 = d3;
         d8 = d4;
         d5 = d6;
         d6 = d9;
      } else if (this.uvRotateNorth == 2) {
         d3 = (double)p_147798_8_.func_94214_a(16.0D - this.renderMaxY * 16.0D);
         d5 = (double)p_147798_8_.func_94207_b(this.renderMinZ * 16.0D);
         d4 = (double)p_147798_8_.func_94214_a(16.0D - this.renderMinY * 16.0D);
         d6 = (double)p_147798_8_.func_94207_b(this.renderMaxZ * 16.0D);
         d7 = d4;
         d8 = d3;
         d3 = d4;
         d4 = d8;
         d9 = d6;
         d10 = d5;
      } else if (this.uvRotateNorth == 3) {
         d3 = (double)p_147798_8_.func_94214_a(16.0D - this.renderMinZ * 16.0D);
         d4 = (double)p_147798_8_.func_94214_a(16.0D - this.renderMaxZ * 16.0D);
         d5 = (double)p_147798_8_.func_94207_b(this.renderMaxY * 16.0D);
         d6 = (double)p_147798_8_.func_94207_b(this.renderMinY * 16.0D);
         d7 = d4;
         d8 = d3;
         d9 = d5;
         d10 = d6;
      }

      double d11 = p_147798_2_ + this.renderMinX;
      double d12 = p_147798_4_ + this.renderMinY;
      double d13 = p_147798_4_ + this.renderMaxY;
      double d14 = p_147798_6_ + this.renderMinZ;
      double d15 = p_147798_6_ + this.renderMaxZ;
      if (this.renderFromInside) {
         d14 = p_147798_6_ + this.renderMaxZ;
         d15 = p_147798_6_ + this.renderMinZ;
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b(d11, d13, d15).func_187315_a(d7, d9).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d14).func_187315_a(d3, d5).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d12, d14).func_187315_a(d8, d10).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d12, d15).func_187315_a(d4, d6).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
   }

   public void renderFaceXPos(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, TextureAtlasSprite p_147764_8_, float red, float green, float blue, int bright) {
      Tessellator tessellator = Tessellator.func_178181_a();
      double d3 = (double)p_147764_8_.func_94214_a(this.renderMinZ * 16.0D);
      double d4 = (double)p_147764_8_.func_94214_a(this.renderMaxZ * 16.0D);
      if (this.field_152631_f) {
         d4 = (double)p_147764_8_.func_94214_a((1.0D - this.renderMinZ) * 16.0D);
         d3 = (double)p_147764_8_.func_94214_a((1.0D - this.renderMaxZ) * 16.0D);
      }

      double d5 = (double)p_147764_8_.func_94207_b(16.0D - this.renderMaxY * 16.0D);
      double d6 = (double)p_147764_8_.func_94207_b(16.0D - this.renderMinY * 16.0D);
      double d7;
      if (this.flipTexture) {
         d7 = d3;
         d3 = d4;
         d4 = d7;
      }

      if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
         d3 = (double)p_147764_8_.func_94209_e();
         d4 = (double)p_147764_8_.func_94212_f();
      }

      if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
         d5 = (double)p_147764_8_.func_94206_g();
         d6 = (double)p_147764_8_.func_94210_h();
      }

      d7 = d4;
      double d8 = d3;
      double d9 = d5;
      double d10 = d6;
      if (this.uvRotateSouth == 2) {
         d3 = (double)p_147764_8_.func_94214_a(this.renderMinY * 16.0D);
         d5 = (double)p_147764_8_.func_94207_b(16.0D - this.renderMinZ * 16.0D);
         d4 = (double)p_147764_8_.func_94214_a(this.renderMaxY * 16.0D);
         d6 = (double)p_147764_8_.func_94207_b(16.0D - this.renderMaxZ * 16.0D);
         d9 = d5;
         d10 = d6;
         d7 = d3;
         d8 = d4;
         d5 = d6;
         d6 = d9;
      } else if (this.uvRotateSouth == 1) {
         d3 = (double)p_147764_8_.func_94214_a(16.0D - this.renderMaxY * 16.0D);
         d5 = (double)p_147764_8_.func_94207_b(this.renderMaxZ * 16.0D);
         d4 = (double)p_147764_8_.func_94214_a(16.0D - this.renderMinY * 16.0D);
         d6 = (double)p_147764_8_.func_94207_b(this.renderMinZ * 16.0D);
         d7 = d4;
         d8 = d3;
         d3 = d4;
         d4 = d8;
         d9 = d6;
         d10 = d5;
      } else if (this.uvRotateSouth == 3) {
         d3 = (double)p_147764_8_.func_94214_a(16.0D - this.renderMinZ * 16.0D);
         d4 = (double)p_147764_8_.func_94214_a(16.0D - this.renderMaxZ * 16.0D);
         d5 = (double)p_147764_8_.func_94207_b(this.renderMaxY * 16.0D);
         d6 = (double)p_147764_8_.func_94207_b(this.renderMinY * 16.0D);
         d7 = d4;
         d8 = d3;
         d9 = d5;
         d10 = d6;
      }

      double d11 = p_147764_2_ + this.renderMaxX;
      double d12 = p_147764_4_ + this.renderMinY;
      double d13 = p_147764_4_ + this.renderMaxY;
      double d14 = p_147764_6_ + this.renderMinZ;
      double d15 = p_147764_6_ + this.renderMaxZ;
      if (this.renderFromInside) {
         d14 = p_147764_6_ + this.renderMaxZ;
         d15 = p_147764_6_ + this.renderMinZ;
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b(d11, d12, d15).func_187315_a(d8, d10).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d12, d14).func_187315_a(d4, d6).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d14).func_187315_a(d7, d9).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(d11, d13, d15).func_187315_a(d3, d5).func_187314_a(j, k).func_181666_a(red, green, blue, 1.0F).func_181675_d();
   }

   public static RenderCubes getInstance() {
      if (instance == null) {
         instance = new RenderCubes();
      }

      return instance;
   }
}
