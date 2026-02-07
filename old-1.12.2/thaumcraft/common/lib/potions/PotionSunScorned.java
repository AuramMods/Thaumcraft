package thaumcraft.common.lib.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionSunScorned extends Potion {
   public static PotionSunScorned instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionSunScorned(boolean par2, int par3) {
      super(par2, par3);
      this.func_76399_b(0, 0);
      this.func_76390_b("potion.sunscorned");
      this.func_76399_b(6, 2);
      this.func_76404_a(0.25D);
   }

   public boolean func_76398_f() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public int func_76392_e() {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(rl);
      return super.func_76392_e();
   }

   public void func_76394_a(EntityLivingBase target, int par2) {
      if (!target.field_70170_p.field_72995_K) {
         float f = target.func_70013_c(1.0F);
         if (f > 0.5F && target.field_70170_p.field_73012_v.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && target.field_70170_p.func_175710_j(new BlockPos(MathHelper.func_76128_c(target.field_70165_t), MathHelper.func_76128_c(target.field_70163_u), MathHelper.func_76128_c(target.field_70161_v)))) {
            target.func_70015_d(4);
         } else if (f < 0.25F && target.field_70170_p.field_73012_v.nextFloat() > f * 2.0F) {
            target.func_70691_i(1.0F);
         }
      }

   }

   public boolean func_76397_a(int par1, int par2) {
      return par1 % 40 == 0;
   }
}
