package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class ChampionModWarded implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase mob, EntityLivingBase target, DamageSource source, float amount) {
      if (mob.field_70172_ad <= 0 && mob.field_70173_aa % 25 == 0) {
         int bh = (int)mob.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() / 2;
         if (mob.func_110139_bj() < (float)bh) {
            mob.func_110149_m(mob.func_110139_bj() + 1.0F);
         }
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.field_70170_p.field_73012_v.nextBoolean()) {
         float w = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float d = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float h = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70131_O;
         FXDispatcher.INSTANCE.drawGenericParticles(boss.func_174813_aQ().field_72340_a + (double)w, boss.func_174813_aQ().field_72338_b + (double)h, boss.func_174813_aQ().field_72339_c + (double)d, 0.0D, 0.0D, 0.0D, 0.5F + boss.field_70170_p.field_73012_v.nextFloat() * 0.1F, 0.5F + boss.field_70170_p.field_73012_v.nextFloat() * 0.1F, 0.5F + boss.field_70170_p.field_73012_v.nextFloat() * 0.1F, 0.6F, true, 21, 4, 1, 4 + boss.field_70170_p.field_73012_v.nextInt(4), 0, 0.8F + boss.field_70170_p.field_73012_v.nextFloat() * 0.3F, 0.0F, 0);
      }
   }
}
