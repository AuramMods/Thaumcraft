package thaumcraft.client.lib.events;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import thaumcraft.common.lib.potions.PotionBlurredVision;
import thaumcraft.common.lib.potions.PotionDeathGaze;
import thaumcraft.common.lib.potions.PotionSunScorned;
import thaumcraft.common.lib.potions.PotionUnnaturalHunger;

public class ShaderHandler {
   public static int warpVignette = 0;
   public static final int SHADER_DESAT = 0;
   public static final int SHADER_BLUR = 1;
   public static final int SHADER_HUNGER = 2;
   public static final int SHADER_SUNSCORNED = 3;
   public static ResourceLocation[] shader_resources = new ResourceLocation[]{new ResourceLocation("shaders/post/desaturatetc.json"), new ResourceLocation("shaders/post/blurtc.json"), new ResourceLocation("shaders/post/hunger.json"), new ResourceLocation("shaders/post/sunscorned.json")};

   protected void checkShaders(PlayerTickEvent event, Minecraft mc) {
      if (event.player.func_70644_a(PotionDeathGaze.instance)) {
         warpVignette = 10;
         if (!RenderEventHandler.shaderGroups.containsKey(0)) {
            try {
               this.setShader(new ShaderGroup(mc.func_110434_K(), mc.func_110442_L(), mc.func_147110_a(), shader_resources[0]), 0);
            } catch (JsonSyntaxException var10) {
            } catch (IOException var11) {
            }
         }
      } else if (RenderEventHandler.shaderGroups.containsKey(0)) {
         this.deactivateShader(0);
      }

      if (event.player.func_70644_a(PotionBlurredVision.instance)) {
         if (!RenderEventHandler.shaderGroups.containsKey(1)) {
            try {
               this.setShader(new ShaderGroup(mc.func_110434_K(), mc.func_110442_L(), mc.func_147110_a(), shader_resources[1]), 1);
            } catch (JsonSyntaxException var8) {
            } catch (IOException var9) {
            }
         }
      } else if (RenderEventHandler.shaderGroups.containsKey(1)) {
         this.deactivateShader(1);
      }

      if (event.player.func_70644_a(PotionUnnaturalHunger.instance)) {
         if (!RenderEventHandler.shaderGroups.containsKey(2)) {
            try {
               this.setShader(new ShaderGroup(mc.func_110434_K(), mc.func_110442_L(), mc.func_147110_a(), shader_resources[2]), 2);
            } catch (JsonSyntaxException var6) {
            } catch (IOException var7) {
            }
         }
      } else if (RenderEventHandler.shaderGroups.containsKey(2)) {
         this.deactivateShader(2);
      }

      if (event.player.func_70644_a(PotionSunScorned.instance)) {
         if (!RenderEventHandler.shaderGroups.containsKey(3)) {
            try {
               this.setShader(new ShaderGroup(mc.func_110434_K(), mc.func_110442_L(), mc.func_147110_a(), shader_resources[3]), 3);
            } catch (JsonSyntaxException var4) {
            } catch (IOException var5) {
            }
         }
      } else if (RenderEventHandler.shaderGroups.containsKey(3)) {
         this.deactivateShader(3);
      }

   }

   void setShader(ShaderGroup target, int shaderId) {
      if (OpenGlHelper.field_148824_g) {
         if (RenderEventHandler.shaderGroups.containsKey(shaderId)) {
            ((ShaderGroup)RenderEventHandler.shaderGroups.get(shaderId)).func_148021_a();
            RenderEventHandler.shaderGroups.remove(shaderId);
         }

         try {
            if (target == null) {
               this.deactivateShader(shaderId);
            } else {
               RenderEventHandler.resetShaders = true;
               RenderEventHandler.shaderGroups.put(shaderId, target);
            }
         } catch (Exception var4) {
            RenderEventHandler.shaderGroups.remove(shaderId);
         }
      }

   }

   public void deactivateShader(int shaderId) {
      if (RenderEventHandler.shaderGroups.containsKey(shaderId)) {
         ((ShaderGroup)RenderEventHandler.shaderGroups.get(shaderId)).func_148021_a();
      }

      RenderEventHandler.shaderGroups.remove(shaderId);
   }
}
