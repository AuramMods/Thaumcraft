package thaumcraft.codechicken.lib.render;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import thaumcraft.codechicken.lib.colour.ColourRGBA;
import thaumcraft.codechicken.lib.lighting.LC;
import thaumcraft.codechicken.lib.lighting.LightMatrix;
import thaumcraft.codechicken.lib.util.Copyable;
import thaumcraft.codechicken.lib.vec.Rotation;
import thaumcraft.codechicken.lib.vec.Transformation;
import thaumcraft.codechicken.lib.vec.Vector3;

public class CCRenderState {
   private static int nextOperationIndex;
   private static ArrayList<CCRenderState.VertexAttribute<?>> vertexAttributes = new ArrayList();
   public static CCRenderState.VertexAttribute<Vector3[]> normalAttrib = new CCRenderState.VertexAttribute<Vector3[]>() {
      private Vector3[] normalRef;

      public Vector3[] newArray(int length) {
         return new Vector3[length];
      }

      public boolean load() {
         this.normalRef = (Vector3[])CCRenderState.model.getAttributes(this);
         if (CCRenderState.model.hasAttribute(this)) {
            return this.normalRef != null;
         } else if (CCRenderState.model.hasAttribute(CCRenderState.sideAttrib)) {
            CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
            return true;
         } else {
            throw new IllegalStateException("Normals requested but neither normal or side attrutes are provided by the model");
         }
      }

      public void operate() {
         if (this.normalRef != null) {
            CCRenderState.setNormal(this.normalRef[CCRenderState.vertexIndex]);
         } else {
            CCRenderState.setNormal(Rotation.axes[CCRenderState.side]);
         }

      }
   };
   public static CCRenderState.VertexAttribute<int[]> colourAttrib = new CCRenderState.VertexAttribute<int[]>() {
      private int[] colourRef;

      public int[] newArray(int length) {
         return new int[length];
      }

      public boolean load() {
         this.colourRef = (int[])CCRenderState.model.getAttributes(this);
         return this.colourRef != null || !CCRenderState.model.hasAttribute(this);
      }

      public void operate() {
         if (this.colourRef != null) {
            CCRenderState.setColour(ColourRGBA.multiply(CCRenderState.baseColour, this.colourRef[CCRenderState.vertexIndex]));
         } else {
            CCRenderState.setColour(CCRenderState.baseColour);
         }

      }
   };
   public static CCRenderState.VertexAttribute<int[]> lightingAttrib = new CCRenderState.VertexAttribute<int[]>() {
      private int[] colourRef;

      public int[] newArray(int length) {
         return new int[length];
      }

      public boolean load() {
         if (CCRenderState.computeLighting && CCRenderState.useColour && CCRenderState.model.hasAttribute(this)) {
            this.colourRef = (int[])CCRenderState.model.getAttributes(this);
            if (this.colourRef != null) {
               CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public void operate() {
         CCRenderState.setColour(ColourRGBA.multiply(CCRenderState.colour, this.colourRef[CCRenderState.vertexIndex]));
      }
   };
   public static CCRenderState.VertexAttribute<int[]> sideAttrib = new CCRenderState.VertexAttribute<int[]>() {
      private int[] sideRef;

      public int[] newArray(int length) {
         return new int[length];
      }

      public boolean load() {
         this.sideRef = (int[])CCRenderState.model.getAttributes(this);
         if (CCRenderState.model.hasAttribute(this)) {
            return this.sideRef != null;
         } else {
            CCRenderState.pipeline.addDependency(CCRenderState.normalAttrib);
            return true;
         }
      }

      public void operate() {
         if (this.sideRef != null) {
            CCRenderState.side = this.sideRef[CCRenderState.vertexIndex];
         } else {
            CCRenderState.side = CCModel.findSide(CCRenderState.normal);
         }

      }
   };
   public static CCRenderState.VertexAttribute<LC[]> lightCoordAttrib = new CCRenderState.VertexAttribute<LC[]>() {
      private LC[] lcRef;
      private Vector3 vec = new Vector3();
      private Vector3 pos = new Vector3();

      public LC[] newArray(int length) {
         return new LC[length];
      }

      public boolean load() {
         this.lcRef = (LC[])CCRenderState.model.getAttributes(this);
         if (CCRenderState.model.hasAttribute(this)) {
            return this.lcRef != null;
         } else {
            this.pos.set((double)CCRenderState.lightMatrix.pos.x, (double)CCRenderState.lightMatrix.pos.y, (double)CCRenderState.lightMatrix.pos.z);
            CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
            CCRenderState.pipeline.addRequirement(Transformation.operationIndex);
            return true;
         }
      }

      public void operate() {
         if (this.lcRef != null) {
            CCRenderState.lc.set(this.lcRef[CCRenderState.vertexIndex]);
         } else {
            CCRenderState.lc.compute(this.vec.set(CCRenderState.vert.vec).sub(this.pos), CCRenderState.side);
         }

      }
   };
   public static CCRenderState.IVertexSource model;
   public static int firstVertexIndex;
   public static int lastVertexIndex;
   public static int vertexIndex;
   public static CCRenderPipeline pipeline = new CCRenderPipeline();
   public static int baseColour;
   public static int alphaOverride;
   public static boolean useNormals;
   public static boolean computeLighting;
   public static boolean useColour;
   public static LightMatrix lightMatrix = new LightMatrix();
   public static Vertex5 vert = new Vertex5();
   public static boolean hasNormal;
   public static Vector3 normal = new Vector3();
   public static boolean hasColour;
   public static int colour;
   public static boolean hasBrightness;
   public static int brightness;
   public static int side;
   public static LC lc = new LC();

   public static int registerOperation() {
      return nextOperationIndex++;
   }

   public static int operationCount() {
      return nextOperationIndex;
   }

   private static int registerVertexAttribute(CCRenderState.VertexAttribute<?> attr) {
      vertexAttributes.add(attr);
      return vertexAttributes.size() - 1;
   }

   public static CCRenderState.VertexAttribute<?> getAttribute(int index) {
      return (CCRenderState.VertexAttribute)vertexAttributes.get(index);
   }

   public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length) {
      System.arraycopy(src, srcPos, dst, destPos, length);
      if (dst instanceof Copyable[]) {
         Object[] oa = (Object[])((Object[])dst);
         Copyable<Object>[] c = (Copyable[])((Copyable[])dst);

         for(int i = destPos; i < destPos + length; ++i) {
            if (c[i] != null) {
               oa[i] = c[i].copy();
            }
         }
      }

   }

   public static <T> T copyOf(CCRenderState.VertexAttribute<T> attr, T src, int length) {
      T dst = attr.newArray(length);
      arrayCopy(src, 0, dst, 0, ((Object[])((Object[])src)).length);
      return dst;
   }

   public static void reset() {
      model = null;
      pipeline.reset();
      hasColour = false;
      hasBrightness = false;
      hasNormal = false;
      useNormals = false;
      computeLighting = true;
      useColour = true;
      alphaOverride = -1;
      baseColour = -1;
   }

   public static void setPipeline(CCRenderState.IVertexOperation... ops) {
      pipeline.setPipeline(ops);
   }

   public static void setPipeline(CCRenderState.IVertexSource model, int start, int end, CCRenderState.IVertexOperation... ops) {
      pipeline.reset();
      setModel(model, start, end);
      pipeline.setPipeline(ops);
   }

   public static void bindModel(CCRenderState.IVertexSource model) {
      if (CCRenderState.model != model) {
         CCRenderState.model = model;
         pipeline.rebuild();
      }

   }

   public static void setModel(CCRenderState.IVertexSource source) {
      setModel(source, 0, source.getVertices().length);
   }

   public static void setModel(CCRenderState.IVertexSource source, int start, int end) {
      bindModel(source);
      setVertexRange(start, end);
   }

   public static void setVertexRange(int start, int end) {
      firstVertexIndex = start;
      lastVertexIndex = end;
   }

   public static void setNormal(double x, double y, double z) {
      hasNormal = true;
      normal.set(x, y, z);
   }

   public static void setNormal(Vector3 n) {
      hasNormal = true;
      normal.set(n);
   }

   public static void setColour(int c) {
      hasColour = true;
      colour = c;
   }

   public static void setBrightness(int b) {
      hasBrightness = true;
      brightness = b;
   }

   public static void pullLightmap() {
      setBrightness((int)OpenGlHelper.lastBrightnessY << 16 | (int)OpenGlHelper.lastBrightnessX);
   }

   public static void pushLightmap() {
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)(brightness & '\uffff'), (float)(brightness >>> 16));
   }

   public static void setDynamic() {
      useNormals = true;
      computeLighting = false;
   }

   public static void changeTexture(String texture) {
      changeTexture(new ResourceLocation(texture));
   }

   public static void changeTexture(ResourceLocation texture) {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(texture);
   }

   public static void draw() {
      Tessellator.func_178181_a().func_78381_a();
   }

   public interface IVertexSource {
      Vertex5[] getVertices();

      <T> T getAttributes(CCRenderState.VertexAttribute<T> var1);

      boolean hasAttribute(CCRenderState.VertexAttribute<?> var1);

      void prepareVertex();
   }

   public abstract static class VertexAttribute<T> implements CCRenderState.IVertexOperation {
      public final int attributeIndex = CCRenderState.registerVertexAttribute(this);
      private final int operationIndex = CCRenderState.registerOperation();
      public boolean active = false;

      public abstract T newArray(int var1);

      public int operationID() {
         return this.operationIndex;
      }
   }

   public interface IVertexOperation {
      boolean load();

      void operate();

      int operationID();
   }
}
