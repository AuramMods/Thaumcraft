package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IChampionModifierEffect {
   float performEffect(EntityLivingBase var1, EntityLivingBase var2, DamageSource var3, float var4);

   @SideOnly(Side.CLIENT)
   void showFX(EntityLivingBase var1);
}
