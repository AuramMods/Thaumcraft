package thaumcraft.client.lib.ender;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.ARBShaderObjects;
import thaumcraft.common.config.Config;

public final class ShaderHelper {
   private static final int VERT = 35633;
   private static final int FRAG = 35632;
   private static final String PREFIX = "/assets/thaumcraft/shader/";
   public static int endShader = 0;

   public static void initShaders() {
      if (useShaders()) {
         endShader = createProgram("ender.vert", "ender.frag");
      }
   }

   public static void useShader(int shader, ShaderCallback callback) {
      if (useShaders()) {
         ARBShaderObjects.glUseProgramObjectARB(shader);
         if (shader != 0) {
            int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
            ARBShaderObjects.glUniform1iARB(time, Minecraft.func_71410_x().func_175606_aa().field_70173_aa);
            if (callback != null) {
               callback.call(shader);
            }
         }

      }
   }

   public static void useShader(int shader) {
      useShader(shader, (ShaderCallback)null);
   }

   public static void releaseShader() {
      useShader(0);
   }

   public static boolean useShaders() {
      return Config.shaders && OpenGlHelper.field_148824_g;
   }

   private static int createProgram(String vert, String frag) {
      int vertId = 0;
      int fragId = 0;
      int program = false;
      if (vert != null) {
         vertId = createShader("/assets/thaumcraft/shader/" + vert, 35633);
      }

      if (frag != null) {
         fragId = createShader("/assets/thaumcraft/shader/" + frag, 35632);
      }

      int program = ARBShaderObjects.glCreateProgramObjectARB();
      if (program == 0) {
         return 0;
      } else {
         if (vert != null) {
            ARBShaderObjects.glAttachObjectARB(program, vertId);
         }

         if (frag != null) {
            ARBShaderObjects.glAttachObjectARB(program, fragId);
         }

         ARBShaderObjects.glLinkProgramARB(program);
         if (ARBShaderObjects.glGetObjectParameteriARB(program, 35714) == 0) {
            return 0;
         } else {
            ARBShaderObjects.glValidateProgramARB(program);
            return ARBShaderObjects.glGetObjectParameteriARB(program, 35715) == 0 ? 0 : program;
         }
      }
   }

   private static int createShader(String filename, int shaderType) {
      byte shader = 0;

      try {
         int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
         if (shader == 0) {
            return 0;
         } else {
            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);
            if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
               throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            } else {
               return shader;
            }
         }
      } catch (Exception var4) {
         ARBShaderObjects.glDeleteObjectARB(shader);
         var4.printStackTrace();
         return -1;
      }
   }

   private static String getLogInfo(int obj) {
      return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
   }

   private static String readFileAsString(String filename) throws Exception {
      StringBuilder source = new StringBuilder();
      InputStream in = ShaderHelper.class.getResourceAsStream(filename);
      Exception exception = null;
      if (in == null) {
         return "";
      } else {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Exception innerExc = null;

            try {
               String line;
               try {
                  while((line = reader.readLine()) != null) {
                     source.append(line).append('\n');
                  }
               } catch (Exception var31) {
                  exception = var31;
               }
            } finally {
               try {
                  reader.close();
               } catch (Exception var30) {
                  if (innerExc == null) {
                     innerExc = var30;
                  } else {
                     var30.printStackTrace();
                  }
               }

            }

            if (innerExc != null) {
               throw innerExc;
            }
         } catch (Exception var33) {
            exception = var33;
         } finally {
            try {
               in.close();
            } catch (Exception var29) {
               if (exception == null) {
                  exception = var29;
               } else {
                  var29.printStackTrace();
               }
            }

            if (exception != null) {
               throw exception;
            }

         }

         return source.toString();
      }
   }
}
