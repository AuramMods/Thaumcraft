package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.lib.SoundsTC;

public class ChampionModInfested implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (boss.field_70170_p.field_73012_v.nextFloat() < 0.4F && !boss.field_70170_p.field_72995_K) {
         EntityTaintCrawler spiderling = new EntityTaintCrawler(boss.field_70170_p);
         spiderling.func_70012_b(boss.field_70165_t, boss.field_70163_u + (double)(boss.field_70131_O / 2.0F), boss.field_70161_v, boss.field_70170_p.field_73012_v.nextFloat() * 360.0F, 0.0F);
         boss.field_70170_p.func_72838_d(spiderling);
         boss.func_184185_a(SoundsTC.gore, 0.5F, 1.0F);
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (boss.field_70170_p.field_73012_v.nextBoolean()) {
         FXDispatcher.INSTANCE.slimeJumpFX(boss, 0);
      }

   }
}
