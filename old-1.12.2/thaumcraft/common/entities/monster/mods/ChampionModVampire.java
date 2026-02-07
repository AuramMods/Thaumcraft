package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class ChampionModVampire implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      boss.func_70691_i(Math.max(2.0F, amount / 2.0F));
      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!(boss.field_70170_p.field_73012_v.nextFloat() > 0.2F)) {
         float w = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float d = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70130_N;
         float h = boss.field_70170_p.field_73012_v.nextFloat() * boss.field_70131_O;
         FXDispatcher.INSTANCE.drawGenericParticles(boss.func_174813_aQ().field_72340_a + (double)w, boss.func_174813_aQ().field_72338_b + (double)h, boss.func_174813_aQ().field_72339_c + (double)d, 0.0D, 0.0D, 0.0D, 0.9F + boss.field_70170_p.field_73012_v.nextFloat() * 0.1F, 0.0F, 0.0F, 0.9F, false, 147, 4, 1, 8 + boss.field_70170_p.field_73012_v.nextInt(4), 0, 0.5F + boss.field_70170_p.field_73012_v.nextFloat() * 0.2F, 0.0F, 0);
      }
   }
}
