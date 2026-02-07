package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartMedium;
import thaumcraft.common.entities.projectile.EntityGenericProjectile;
import thaumcraft.common.items.casters.ICaster;

public class FMediumProjectile implements IFocusPartMedium {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/projectile.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MEDIUM;
   }

   public String getKey() {
      return "thaumcraft.PROJECTILE";
   }

   public String getResearch() {
      return "FOCUSPROJECTILE@2";
   }

   public Aspect getAspect() {
      return Aspect.FLIGHT;
   }

   public int getGemColor() {
      return 5800698;
   }

   public int getIconColor() {
      return 16765440;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public boolean onMediumTrigger(World world, Entity caster, ItemStack casterStack, FocusCore focus, float charge) {
      boolean mainhand = true;
      if (caster instanceof EntityPlayer) {
         if (((EntityPlayer)caster).func_184614_ca() != null && ((EntityPlayer)caster).func_184614_ca().func_77973_b() instanceof ICaster) {
            mainhand = true;
         } else if (((EntityPlayer)caster).func_184592_cb() != null && ((EntityPlayer)caster).func_184592_cb().func_77973_b() instanceof ICaster) {
            mainhand = false;
         }
      }

      float speed = 1.5F;
      float spread = 2.0F;
      int projectiles = 1;
      if (focus.mediumModifiers != null) {
         IFocusPart[] var10 = focus.mediumModifiers;
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            IFocusPart mod = var10[var12];
            if (mod instanceof FModScatter) {
               spread *= 6.0F;
               projectiles = 5;
            }
         }
      }

      if (!world.field_72995_K && caster instanceof EntityLivingBase) {
         for(int a = 0; a < projectiles; ++a) {
            EntityGenericProjectile projectile = new EntityGenericProjectile(world, (EntityLivingBase)caster, casterStack, speed, spread, focus, charge, mainhand);
            projectile.func_184538_a(caster, caster.field_70125_A, caster.field_70177_z + (mainhand ? -0.5F : 0.5F), -2.0F, speed, spread);
            world.func_72838_d(projectile);
         }
      }

      return true;
   }
}
