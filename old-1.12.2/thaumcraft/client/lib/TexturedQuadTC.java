package thaumcraft.client.lib;

import java.awt.Color;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class TexturedQuadTC {
   public PositionTextureVertex[] vertexPositions;
   public int nVertices;
   private boolean invertNormal;
   private boolean flipped;

   public TexturedQuadTC(PositionTextureVertex[] vertices) {
      this.flipped = false;
      this.vertexPositions = vertices;
      this.nVertices = vertices.length;
   }

   public TexturedQuadTC(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight) {
      this(vertices);
      float f2 = 0.0F / textureWidth;
      float f3 = 0.0F / textureHeight;
      vertices[0] = vertices[0].func_78240_a((float)texcoordU2 / textureWidth - f2, (float)texcoordV1 / textureHeight + f3);
      vertices[1] = vertices[1].func_78240_a((float)texcoordU1 / textureWidth + f2, (float)texcoordV1 / textureHeight + f3);
      vertices[2] = vertices[2].func_78240_a((float)texcoordU1 / textureWidth + f2, (float)texcoordV2 / textureHeight - f3);
      vertices[3] = vertices[3].func_78240_a((float)texcoordU2 / textureWidth - f2, (float)texcoordV2 / textureHeight - f3);
   }

   public void flipFace() {
      this.flipped = true;
      PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];

      for(int i = 0; i < this.vertexPositions.length; ++i) {
         apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
      }

      this.vertexPositions = apositiontexturevertex;
   }

   public void draw(VertexBuffer renderer, float scale, int bright, int color, float alpha) {
      if (bright != -99) {
         renderer.func_181668_a(7, UtilsFX.VERTEXFORMAT_POS_TEX_CO_LM_NO);
      } else {
         renderer.func_181668_a(7, DefaultVertexFormats.field_181712_l);
      }

      Color c = new Color(color);
      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';

      for(int i = 0; i < 4; ++i) {
         PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
         if (bright != -99) {
            renderer.func_181662_b(positiontexturevertex.field_78243_a.field_72450_a * (double)scale, positiontexturevertex.field_78243_a.field_72448_b * (double)scale, positiontexturevertex.field_78243_a.field_72449_c * (double)scale).func_187315_a((double)positiontexturevertex.field_78241_b, (double)positiontexturevertex.field_78242_c).func_181669_b(c.getRed(), c.getGreen(), c.getBlue(), (int)(alpha * 255.0F)).func_187314_a(j, k).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
         } else {
            renderer.func_181662_b(positiontexturevertex.field_78243_a.field_72450_a * (double)scale, positiontexturevertex.field_78243_a.field_72448_b * (double)scale, positiontexturevertex.field_78243_a.field_72449_c * (double)scale).func_187315_a((double)positiontexturevertex.field_78241_b, (double)positiontexturevertex.field_78242_c).func_181669_b(c.getRed(), c.getGreen(), c.getBlue(), (int)(alpha * 255.0F)).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
         }
      }

      Tessellator.func_178181_a().func_78381_a();
   }

   public void draw(VertexBuffer renderer, float scale, int bright, int[] color, float[] alpha) {
      if (bright != -99) {
         renderer.func_181668_a(7, UtilsFX.VERTEXFORMAT_POS_TEX_CO_LM_NO);
      } else {
         renderer.func_181668_a(7, DefaultVertexFormats.field_181712_l);
      }

      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';

      for(int i = 0; i < 4; ++i) {
         int idx = this.flipped ? 3 - i : i;
         Color c = new Color(color[idx]);
         PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
         if (bright != -99) {
            renderer.func_181662_b(positiontexturevertex.field_78243_a.field_72450_a * (double)scale, positiontexturevertex.field_78243_a.field_72448_b * (double)scale, positiontexturevertex.field_78243_a.field_72449_c * (double)scale).func_187315_a((double)positiontexturevertex.field_78241_b, (double)positiontexturevertex.field_78242_c).func_181669_b(c.getRed(), c.getGreen(), c.getBlue(), (int)(alpha[idx] * 255.0F)).func_187314_a(j, k).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
         } else {
            renderer.func_181662_b(positiontexturevertex.field_78243_a.field_72450_a * (double)scale, positiontexturevertex.field_78243_a.field_72448_b * (double)scale, positiontexturevertex.field_78243_a.field_72449_c * (double)scale).func_187315_a((double)positiontexturevertex.field_78241_b, (double)positiontexturevertex.field_78242_c).func_181669_b(c.getRed(), c.getGreen(), c.getBlue(), (int)(alpha[idx] * 255.0F)).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
         }
      }

      Tessellator.func_178181_a().func_78381_a();
   }
}
