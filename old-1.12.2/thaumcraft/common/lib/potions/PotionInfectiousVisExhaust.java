package thaumcraft.common.lib.potions;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.potions.PotionVisExhaust;

public class PotionInfectiousVisExhaust extends Potion {
   public static PotionInfectiousVisExhaust instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionInfectiousVisExhaust(boolean par2, int par3) {
      super(par2, par3);
      this.func_76399_b(0, 0);
      this.func_76390_b("potion.infvisexhaust");
      this.func_76399_b(6, 1);
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
      List<EntityLivingBase> targets = target.field_70170_p.func_72872_a(EntityLivingBase.class, target.func_174813_aQ().func_72314_b(4.0D, 4.0D, 4.0D));
      if (targets.size() > 0) {
         Iterator var4 = targets.iterator();

         while(var4.hasNext()) {
            EntityLivingBase e = (EntityLivingBase)var4.next();
            if (!e.func_70644_a(instance)) {
               if (par2 > 0) {
                  e.func_70690_d(new PotionEffect(instance, 6000, par2 - 1, false, true));
               } else {
                  e.func_70690_d(new PotionEffect(PotionVisExhaust.instance, 6000, 0, false, true));
               }
            }
         }
      }

   }

   public boolean func_76397_a(int par1, int par2) {
      return par1 % 40 == 0;
   }
}
