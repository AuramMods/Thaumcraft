package thaumcraft.common.lib.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBlurredVision extends Potion {
   public static PotionBlurredVision instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionBlurredVision(boolean par2, int par3) {
      super(par2, par3);
      this.func_76399_b(0, 0);
      this.func_76390_b("potion.blurred");
      this.func_76399_b(5, 2);
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
   }
}
