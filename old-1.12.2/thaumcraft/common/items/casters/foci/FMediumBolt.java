package thaumcraft.common.items.casters.foci;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartMedium;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;

public class FMediumBolt implements IFocusPartMedium {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/bolt.png");
   private ArrayList<Long> exList = new ArrayList();

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.MEDIUM;
   }

   public String getKey() {
      return "thaumcraft.BOLT";
   }

   public String getResearch() {
      return "FOCUSBOLT@2";
   }

   public Aspect getAspect() {
      return Aspect.FIRE;
   }

   public int getGemColor() {
      return 5821431;
   }

   public int getIconColor() {
      return 12648447;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getCostMultiplier() {
      return 1.25F;
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

      float range = 16.0F;
      boolean chaining = false;
      int links = 2;
      if (focus.mediumModifiers != null) {
         IFocusPart[] var10 = focus.mediumModifiers;
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            IFocusPart mod = var10[var12];
            if (mod == FocusHelper.CHAIN) {
               chaining = true;
            }
         }
      }

      Entity pointedEntity = EntityUtils.getPointedEntity(world, caster, 0.0D, (double)range, 0.1F);
      RayTraceResult mop;
      if (pointedEntity != null) {
         mop = new RayTraceResult(pointedEntity);
      } else {
         mop = Utils.rayTrace(world, caster, true, (double)range);
      }

      if (mop == null) {
         return false;
      } else {
         Vec3d v = caster.func_70676_i(1.0F);
         double px = caster.field_70165_t + v.field_72450_a * (double)range;
         double py = caster.func_174813_aQ().field_72338_b + 0.25D + (double)(caster.field_70131_O / 2.0F) + v.field_72448_b * (double)range;
         double pz = caster.field_70161_v + v.field_72449_c * (double)range;
         boolean b = false;
         boolean unhurt = mop.field_72308_g == null || mop.field_72308_g.field_70172_ad <= 0;
         int resetHurt = 0;
         ArrayList<Vec3d> impacts = new ArrayList();
         FocusCore.FocusEffect[] var23 = focus.effects;
         int var24 = var23.length;

         for(int var25 = 0; var25 < var24; ++var25) {
            FocusCore.FocusEffect effect = var23[var25];
            if (mop.field_72308_g != null && unhurt && mop.field_72308_g.field_70172_ad > 0) {
               resetHurt = mop.field_72308_g.field_70172_ad;
               mop.field_72308_g.field_70172_ad = 0;
            }

            if (world.field_72995_K) {
               this.shootLightning(caster, px, py, pz, effect.effect.getGemColor(), effect.effectMultipler * charge, mainhand);
            }

            IBlockState oldState = null;
            if (mop.func_178782_a() != null) {
               oldState = world.func_180495_p(mop.func_178782_a());
            }

            if (effect.effect.onEffectTrigger(world, mop, caster, casterStack, caster, effect, charge)) {
               double hmod = 0.0D;
               if (mop.field_72308_g != null) {
                  hmod = (double)(mop.field_72308_g.field_70131_O / 2.0F);
               }

               impacts.add(new Vec3d(mop.field_72307_f.field_72450_a, mop.field_72307_f.field_72448_b + hmod, mop.field_72307_f.field_72449_c));
               b = true;
               if (chaining) {
                  Entity sourceE = mop.field_72308_g;
                  BlockPos sourceB = mop.func_178782_a();
                  this.exList.clear();
                  long l = mop.field_72308_g != null ? (long)sourceE.func_145782_y() : (mop.func_178782_a() != null ? sourceB.func_177986_g() : 0L);
                  this.exList.add(l);
                  Random rand = new Random(l);

                  for(int a = 0; a < links; ++a) {
                     RayTraceResult rt;
                     Color c;
                     double px2;
                     double py2;
                     double pz2;
                     double px3;
                     double py3;
                     double pz3;
                     if (sourceE != null) {
                        Entity nt = this.getClosestEntityOfType(sourceE, (double)range);
                        if (nt != null) {
                           rt = new RayTraceResult(nt);
                           if (!effect.effect.onEffectTrigger(world, rt, caster, casterStack, caster, effect, charge)) {
                              break;
                           }

                           impacts.add(new Vec3d(rt.field_72307_f.field_72450_a, rt.field_72307_f.field_72448_b + hmod, rt.field_72307_f.field_72449_c));
                           if (world.field_72995_K) {
                              c = new Color(effect.effect.getGemColor());
                              px2 = sourceE.field_70165_t;
                              py2 = sourceE.func_174813_aQ().field_72338_b + (double)(sourceE.field_70131_O / 2.0F);
                              pz2 = sourceE.field_70161_v;
                              px3 = nt.field_70165_t;
                              py3 = nt.func_174813_aQ().field_72338_b + (double)(sourceE.field_70131_O / 2.0F);
                              pz3 = nt.field_70161_v;
                              FXDispatcher.INSTANCE.arcBolt(px2, py2, pz2, px3, py3, pz3, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, charge * effect.effectMultipler);
                           }

                           sourceE = nt;
                        }
                     } else if (sourceB != null) {
                        BlockPos pos = this.getClosestBlockOfType(world, mop.func_178782_a(), (double)range, oldState, rand);
                        if (pos != null) {
                           rt = new RayTraceResult((new Vec3d(pos)).func_72441_c(0.5D, 0.5D, 0.5D), EnumFacing.UP, pos);
                           if (!effect.effect.onEffectTrigger(world, rt, caster, casterStack, caster, effect, charge)) {
                              break;
                           }

                           impacts.add(new Vec3d(rt.field_72307_f.field_72450_a, rt.field_72307_f.field_72448_b + hmod, rt.field_72307_f.field_72449_c));
                           if (world.field_72995_K) {
                              c = new Color(effect.effect.getGemColor());
                              px2 = (double)sourceB.func_177958_n() + 0.25D + (double)(world.field_73012_v.nextFloat() * 0.5F);
                              py2 = (double)sourceB.func_177956_o() + 0.25D + (double)(world.field_73012_v.nextFloat() * 0.5F);
                              pz2 = (double)sourceB.func_177952_p() + 0.25D + (double)(world.field_73012_v.nextFloat() * 0.5F);
                              px3 = (double)pos.func_177958_n() + 0.25D + (double)(world.field_73012_v.nextFloat() * 0.5F);
                              py3 = (double)pos.func_177956_o() + 0.25D + (double)(world.field_73012_v.nextFloat() * 0.5F);
                              pz3 = (double)pos.func_177952_p() + 0.25D + (double)(world.field_73012_v.nextFloat() * 0.5F);
                              FXDispatcher.INSTANCE.arcBolt(px2, py2, pz2, px3, py3, pz3, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, charge * effect.effectMultipler);
                           }

                           sourceB = pos;
                        }
                     }
                  }
               }
            }
         }

         if (!world.field_72995_K && !impacts.isEmpty()) {
            Iterator var55 = impacts.iterator();

            while(var55.hasNext()) {
               Vec3d ve = (Vec3d)var55.next();
               CasterManager.sendImpact(world, ve.field_72450_a, ve.field_72448_b, ve.field_72449_c, focus);
            }
         }

         if (resetHurt > 0) {
            mop.field_72308_g.field_70172_ad = resetHurt;
         }

         return b;
      }
   }

   private Entity getClosestEntityOfType(Entity source, double range) {
      List<Entity> l = EntityUtils.getEntitiesInRange(source.field_70170_p, source.field_70165_t, source.field_70163_u, source.field_70161_v, source, source.getClass(), range);
      Entity out = null;
      double d = Double.MAX_VALUE;
      Iterator var8 = l.iterator();

      while(var8.hasNext()) {
         Entity e = (Entity)var8.next();
         if (!this.exList.contains((long)e.func_145782_y())) {
            double r = source.func_70068_e(e);
            if (r < d) {
               d = r;
               out = e;
            }
         }
      }

      if (out != null) {
         this.exList.add((long)out.func_145782_y());
      }

      return out;
   }

   private BlockPos getClosestBlockOfType(World world, BlockPos source, double range, IBlockState state, Random rand) {
      range = Math.max(2.0D, Math.min(8.0D, range / 2.0D));
      double md = Double.MAX_VALUE;
      BlockPos mp = null;
      Iterator var10 = BlockPos.func_177975_b(source.func_177963_a(-range, -range, -range), source.func_177963_a(range, range, range)).iterator();

      while(var10.hasNext()) {
         MutableBlockPos blockpos$mutableblockpos1 = (MutableBlockPos)var10.next();
         if (!this.exList.contains(blockpos$mutableblockpos1.func_177986_g())) {
            Double d = blockpos$mutableblockpos1.func_177951_i(source) + (double)rand.nextInt((int)range);
            if (d <= range * range && d <= md) {
               IBlockState iblockstate1 = world.func_180495_p(blockpos$mutableblockpos1);
               if (state.func_177230_c() == iblockstate1.func_177230_c()) {
                  md = d;
                  mp = blockpos$mutableblockpos1.func_185334_h();
               }
            }
         }
      }

      if (mp != null) {
         this.exList.add(mp.func_177986_g());
         return mp;
      } else {
         return null;
      }
   }

   private void shootLightning(Entity caster, double xx, double yy, double zz, int color, float width, boolean mainhand) {
      Color c = new Color(color);
      double px = caster.field_70165_t;
      double py = caster.func_174813_aQ().field_72338_b + (double)(caster.field_70131_O / 2.0F) + 0.25D;
      double pz = caster.field_70161_v;
      px += (double)(-MathHelper.func_76134_b(caster.field_70177_z / 180.0F * 3.141593F) * (Minecraft.func_71410_x().field_71474_y.field_74320_O > 0 ? 0.3F : 1.0F) * (float)(mainhand ? 1 : -1));
      py += -0.25D;
      pz += (double)(-MathHelper.func_76126_a(caster.field_70177_z / 180.0F * 3.141593F) * 0.3F * (float)(mainhand ? 1 : -1));
      Vec3d vec3d = caster.func_70676_i(1.0F);
      px += vec3d.field_72450_a * 0.5D;
      py += vec3d.field_72448_b * 0.5D;
      pz += vec3d.field_72449_c * 0.5D;
      FXDispatcher.INSTANCE.arcBolt(px, py, pz, xx, yy, zz, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, width);
   }
}
