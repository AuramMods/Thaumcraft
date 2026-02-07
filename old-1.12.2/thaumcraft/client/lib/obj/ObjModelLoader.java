package thaumcraft.client.lib.obj;

import net.minecraft.util.ResourceLocation;

public class ObjModelLoader implements IModelCustomLoader {
   private static final String[] types = new String[]{"obj"};

   public String getType() {
      return "OBJ model";
   }

   public String[] getSuffixes() {
      return types;
   }

   public IModelCustom loadInstance(ResourceLocation resource) throws WavefrontObject.ModelFormatException {
      return new WavefrontObject(resource);
   }
}
