package thaumcraft.common.items.casters.foci;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartEffect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.misc.TileHole;

public class FEffectRift implements IFocusPartEffect {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "textures/foci/rift.png");

   public IFocusPart.EnumFocusPartType getType() {
      return IFocusPart.EnumFocusPartType.EFFECT;
   }

   public String getKey() {
      return "thaumcraft.RIFT";
   }

   public String getResearch() {
      return "FOCUSBREAK@2";
   }

   public Aspect getAspect() {
      return Aspect.VOID;
   }

   public int getGemColor() {
      return 3084645;
   }

   public int getIconColor() {
      return 6863354;
   }

   public ResourceLocation getIcon() {
      return this.icon;
   }

   public float getBaseCost() {
      return 10.0F;
   }

   public boolean onEffectTrigger(World world, RayTraceResult ray, Entity caster, ItemStack casterStack, Entity mediumEntity, FocusCore.FocusEffect effect, float charge) {
      if (ray.field_72313_a == Type.BLOCK) {
         if (world.field_73011_w.getDimension() == Config.dimensionOuterId) {
            if (!world.field_72995_K) {
               world.func_184148_a((EntityPlayer)null, (double)ray.func_178782_a().func_177958_n() + 0.5D, (double)ray.func_178782_a().func_177956_o() + 0.5D, (double)ray.func_178782_a().func_177952_p() + 0.5D, SoundsTC.wandfail, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }

            return false;
         } else {
            float maxdis = 8.0F * effect.effectMultipler * charge;
            int dur = 120;
            if (effect.modifiers != null) {
               IFocusPart[] var10 = effect.modifiers;
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  IFocusPart mod = var10[var12];
                  if (mod instanceof FModLingering) {
                     dur *= 2;
                  }
               }
            }

            int distance = false;
            BlockPos pos = new BlockPos(ray.func_178782_a());

            int distance;
            for(distance = 0; (float)distance < maxdis; ++distance) {
               IBlockState bi = world.func_180495_p(pos);
               if (BlockUtils.isPortableHoleBlackListed(bi) || bi.func_177230_c() == Blocks.field_150357_h || bi.func_177230_c() == BlocksTC.hole || bi.func_177230_c().isAir(bi, world, pos) || bi.func_185887_b(world, pos) == -1.0F) {
                  break;
               }

               pos = pos.func_177972_a(ray.field_178784_b.func_176734_d());
            }

            if (!world.field_72995_K) {
               world.func_184133_a((EntityPlayer)null, ray.func_178782_a(), SoundEvents.field_187534_aX, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return createHole(world, ray.func_178782_a(), ray.field_178784_b, (byte)Math.round((float)(distance + 1)), dur);
         }
      } else {
         return false;
      }
   }

   public static boolean createHole(World world, BlockPos pos, EnumFacing side, byte count, int max) {
      IBlockState bs = world.func_180495_p(pos);
      if (!world.field_72995_K && world.func_175625_s(pos) == null && !BlockUtils.isPortableHoleBlackListed(bs) && bs.func_177230_c() != Blocks.field_150357_h && bs.func_177230_c() != BlocksTC.hole && (bs.func_177230_c().isAir(bs, world, pos) || !bs.func_177230_c().func_176196_c(world, pos)) && bs.func_185887_b(world, pos) != -1.0F) {
         if (world.func_175656_a(pos, BlocksTC.hole.func_176223_P())) {
            TileHole ts = (TileHole)world.func_175625_s(pos);
            ts.oldblock = bs;
            ts.countdownmax = (short)max;
            ts.count = count;
            ts.direction = side;
            ts.func_70296_d();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean hasCustomParticle() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void drawCustomParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
      FXGeneric fb = new FXGeneric(world, posX, posY, posZ, motionX, motionY, motionZ);
      fb.func_187114_a(16 + world.field_73012_v.nextInt(16));
      fb.setParticles(384 + world.field_73012_v.nextInt(16), 1, 1);
      fb.setSlowDown(0.75D);
      fb.setAlphaF(1.0F, 0.0F);
      fb.setScale((float)(0.699999988079071D + world.field_73012_v.nextGaussian() * 0.30000001192092896D));
      fb.func_70538_b(0.25F, 0.25F, 1.0F);
      fb.setRandomMovementScale(0.01F, 0.01F, 0.01F);
      ParticleEngine.INSTANCE.addEffectWithDelay(world, fb, 0);
   }
}
