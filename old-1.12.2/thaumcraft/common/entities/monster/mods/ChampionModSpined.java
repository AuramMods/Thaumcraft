package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class ChampionModSpined implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (target != null && !source.field_76373_n.equalsIgnoreCase("thorns")) {
         target.func_70097_a(DamageSource.func_92087_a(boss), (float)(1 + boss.field_70170_p.field_73012_v.nextInt(3)));
         target.func_184185_a(SoundEvents.field_187903_gc, 0.5F, 1.0F);
         return amount;
      } else {
         return amount;
      }
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.field_70170_p.field_73012_v.nextBoolean()) {
         float w = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float d = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float h = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70131_O;
         int p = 176 + boss.field_70170_p.field_73012_v.nextInt(4) * 3;
         FXDispatcher.INSTANCE.drawGenericParticles(boss.func_174813_aQ().field_72340_a + (double)w, boss.func_174813_aQ().field_72338_b + (double)h, boss.func_174813_aQ().field_72339_c + (double)d, 0.0D, 0.0D, 0.0D, 0.5F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.1F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.1F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.7F, false, p, 3, 1, 3, 0, 1.2F + boss.field_70170_p.field_73012_v.nextFloat() * 0.3F, 0.0F, 0);
      }
   }
}
