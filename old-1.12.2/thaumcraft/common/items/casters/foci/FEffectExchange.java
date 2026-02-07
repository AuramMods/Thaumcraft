package thaumcraft.common.items.casters.foci;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusBlockPicker;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.lib.events.ServerEvents;

public class FEffectExchange implements IFocusPartEffect, IFocusBlockPicker {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/exchange.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.EXCHANGE";
   }

   public String getResearch() {
      return "FOCUSEXCHANGE";
   }

   public Aspect getAspect() {
      return Aspect.EXCHANGE;
   }

   public int getGemColor() {
      return 13927953;
   }

   public int getIconColor() {
      return 16775142;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 0.25F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      if (ray.field_72313_a == Type.BLOCK && !world.field_72995_K && caster instanceof EntityPlayer && casterStack != null) {
         boolean silk = false;
         int fortune = 0;
         if (effect.modifiers != null) {
            IFocusPart[] var10 = effect.modifiers;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               IFocusPart mod = var10[var12];
               if (mod instanceof FModSilkTouch) {
                  silk = true;
               }

               if (mod instanceof FModFortune) {
                  fortune = 2;
               }
            }
         }

         ItemCaster c = (ItemCaster)casterStack.func_77973_b();
         if (c.getPickedBlock(casterStack) == null) {
            return false;
         } else {
            ServerEvents.addSwapper(world, ray.func_178782_a(), world.func_180495_p(ray.func_178782_a()), c.getPickedBlock(casterStack), true, 0, (EntityPlayer)caster, true, false, this.getGemColor(), true, silk, fortune, ServerEvents.DEFAULT_PREDICATE, effect.effect.getBaseCost() * effect.costMultipler);
            return true;
         }
      } else {
         return false;
      }
   }
}
