package thaumcraft.api.golems.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.golems.EnumGolemTrait;

public class GolemMaterial {
   protected static GolemMaterial[] materials = new GolemMaterial[1];
   public byte id;
   public String key;
   public String[] research;
   public ResourceLocation texture;
   public int itemColor;
   public int healthMod;
   public int armor;
   public int damage;
   public ItemStack componentBase;
   public ItemStack componentMechanism;
   public EnumGolemTrait[] traits;
   private static byte lastID = 0;

   public GolemMaterial(String key, String[] research, ResourceLocation texture, int itemColor, int hp, int armor, int damage, ItemStack compb, ItemStack compm, EnumGolemTrait[] tags) {
      this.key = key;
      this.research = research;
      this.texture = texture;
      this.itemColor = itemColor;
      this.componentBase = compb;
      this.componentMechanism = compm;
      this.healthMod = hp;
      this.armor = armor;
      this.traits = tags;
      this.damage = damage;
   }

   public static void register(GolemMaterial thing) {
      thing.id = lastID++;
      if (thing.id >= materials.length) {
         GolemMaterial[] temp = new GolemMaterial[thing.id + 1];
         System.arraycopy(materials, 0, temp, 0, materials.length);
         materials = temp;
      }

      materials[thing.id] = thing;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("golem.material." + this.key.toLowerCase());
   }

   public String getLocalizedDescription() {
      return I18n.func_74838_a("golem.material.text." + this.key.toLowerCase());
   }

   public static GolemMaterial[] getMaterials() {
      return materials;
   }
}
