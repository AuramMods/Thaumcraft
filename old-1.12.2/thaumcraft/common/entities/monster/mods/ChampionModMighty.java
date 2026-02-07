package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class ChampionModMighty implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float ammount) {
      return 0.0F;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!(boss.field_70170_p.field_73012_v.nextFloat() > 0.3F)) {
         float w = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float d = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float h = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70131_O;
         int p = 176 + boss.field_70170_p.field_73012_v.nextInt(4) * 3;
         FXDispatcher.INSTANCE.drawGenericParticles(boss.func_174813_aQ().field_72340_a + (double)w, boss.func_174813_aQ().field_72338_b + (double)h, boss.func_174813_aQ().field_72339_c + (double)d, 0.0D, 0.0D, 0.0D, 0.8F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.8F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.8F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.7F, false, p, 3, 1, 4 + boss.field_70170_p.field_73012_v.nextInt(3), 0, 1.0F + boss.field_70170_p.field_73012_v.nextFloat() * 0.3F, 0.0F, 0);
      }
   }
}
