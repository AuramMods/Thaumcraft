package thaumcraft.api.golems.parts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;

public class GolemArm {
   protected static GolemArm[] arms = new GolemArm[1];
   public byte id;
   public String key;
   public String[] research;
   public ResourceLocation icon;
   public Object[] components;
   public EnumGolemTrait[] traits;
   public GolemArm.IArmFunction function;
   public PartModel model;
   private static byte lastID = 0;

   public GolemArm(String key, String[] research, ResourceLocation icon, PartModel model, Object[] comp, EnumGolemTrait[] tags) {
      this.key = key;
      this.research = research;
      this.icon = icon;
      this.components = comp;
      this.traits = tags;
      this.model = model;
      this.function = null;
   }

   public GolemArm(String key, String[] research, ResourceLocation icon, PartModel model, Object[] comp, GolemArm.IArmFunction function, EnumGolemTrait[] tags) {
      this(key, research, icon, model, comp, tags);
      this.function = function;
   }

   public static void register(GolemArm thing) {
      thing.id = lastID++;
      if (thing.id >= arms.length) {
         GolemArm[] temp = new GolemArm[thing.id + 1];
         System.arraycopy(arms, 0, temp, 0, arms.length);
         arms = temp;
      }

      arms[thing.id] = thing;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("golem.arm." + this.key.toLowerCase());
   }

   public String getLocalizedDescription() {
      return I18n.func_74838_a("golem.arm.text." + this.key.toLowerCase());
   }

   public static GolemArm[] getArms() {
      return arms;
   }

   public interface IArmFunction extends IGenericFunction {
      void onMeleeAttack(IGolemAPI var1, Entity var2);

      void onRangedAttack(IGolemAPI var1, EntityLivingBase var2, float var3);

      EntityAIAttackRanged getRangedAttackAI(IRangedAttackMob var1);
   }
}
