package thaumcraft.api.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;

public class PotionFluxTaint extends Potion {
   public static PotionFluxTaint instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionFluxTaint(boolean par2, int par3) {
      super(par2, par3);
      this.func_76399_b(3, 1);
   }

   public String func_76393_a() {
      return "flux_taint";
   }

   public static void init() {
      instance.func_76390_b("potion.fluxtaint");
      instance.func_76399_b(3, 1);
      instance.func_76404_a(0.25D);
   }

   public boolean func_76398_f() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public int func_76392_e() {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(rl);
      return super.func_76392_e();
   }

   public void func_76394_a(EntityLivingBase target, int strength) {
      if (target instanceof ITaintedMob) {
         target.func_70691_i(1.0F);
      } else if (!target.func_70662_br() && !(target instanceof EntityPlayer)) {
         target.func_70097_a(DamageSourceThaumcraft.taint, 1.0F);
      } else if (!target.func_70662_br() && (target.func_110138_aP() > 1.0F || target instanceof EntityPlayer)) {
         target.func_70097_a(DamageSourceThaumcraft.taint, 1.0F);
      }

   }

   public boolean func_76397_a(int par1, int par2) {
      int k = 40 >> par2;
      return k > 0 ? par1 % k == 0 : true;
   }
}
