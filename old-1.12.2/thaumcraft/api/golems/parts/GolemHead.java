package thaumcraft.api.golems.parts;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.golems.EnumGolemTrait;

public class GolemHead {
   protected static GolemHead[] heads = new GolemHead[1];
   public byte id;
   public String key;
   public String[] research;
   public ResourceLocation icon;
   public Object[] components;
   public EnumGolemTrait[] traits;
   public GolemHead.IHeadFunction function;
   public PartModel model;
   private static byte lastID = 0;

   public GolemHead(String key, String[] research, ResourceLocation icon, PartModel model, Object[] comp, EnumGolemTrait[] tags) {
      this.key = key;
      this.research = research;
      this.icon = icon;
      this.components = comp;
      this.traits = tags;
      this.model = model;
      this.function = null;
   }

   public GolemHead(String key, String[] research, ResourceLocation icon, PartModel model, Object[] comp, GolemHead.IHeadFunction function, EnumGolemTrait[] tags) {
      this(key, research, icon, model, comp, tags);
      this.function = function;
   }

   public static void register(GolemHead thing) {
      thing.id = lastID++;
      if (thing.id >= heads.length) {
         GolemHead[] temp = new GolemHead[thing.id + 1];
         System.arraycopy(heads, 0, temp, 0, heads.length);
         heads = temp;
      }

      heads[thing.id] = thing;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("golem.head." + this.key.toLowerCase());
   }

   public String getLocalizedDescription() {
      return I18n.func_74838_a("golem.head.text." + this.key.toLowerCase());
   }

   public static GolemHead[] getHeads() {
      return heads;
   }

   public interface IHeadFunction extends IGenericFunction {
   }
}
