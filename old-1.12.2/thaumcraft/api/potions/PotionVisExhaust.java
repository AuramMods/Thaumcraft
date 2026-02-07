package thaumcraft.api.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionVisExhaust extends Potion {
   public static PotionVisExhaust instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionVisExhaust(boolean par2, int par3) {
      super(par2, par3);
      this.func_76399_b(5, 1);
   }

   public String func_76393_a() {
      return "vis_exhaust";
   }

   public static void init() {
      instance.func_76390_b("potion.visexhaust");
      instance.func_76399_b(5, 1);
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

   public void func_76394_a(EntityLivingBase target, int par2) {
   }
}
