package thaumcraft.api.damagesource;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceThaumcraft extends DamageSource {
   public static DamageSource taint = (new DamageSourceThaumcraft("taint")).func_76348_h().func_82726_p();
   public static DamageSource tentacle = new DamageSourceThaumcraft("tentacle");
   public static DamageSource swarm = new DamageSourceThaumcraft("swarm");
   public static DamageSource dissolve = (new DamageSourceThaumcraft("dissolve")).func_76348_h();
   private boolean isUnblockable = false;
   private boolean isDamageAllowedInCreativeMode = false;
   private float hungerDamage = 0.3F;
   private boolean fireDamage;
   private boolean projectile;
   private boolean difficultyScaled;
   private boolean magicDamage = false;
   private boolean explosion = false;

   protected DamageSourceThaumcraft(String par1Str) {
      super(par1Str);
   }

   public static DamageSource causeSwarmDamage(EntityLivingBase par0EntityLiving) {
      return new EntityDamageSource("swarm", par0EntityLiving);
   }

   public static DamageSource causeTentacleDamage(EntityLivingBase par0EntityLiving) {
      return new EntityDamageSource("tentacle", par0EntityLiving);
   }
}
