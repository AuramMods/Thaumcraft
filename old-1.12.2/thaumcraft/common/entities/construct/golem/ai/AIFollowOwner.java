package thaumcraft.common.entities.construct.golem.ai;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;

public class AIFollowOwner extends EntityAIBase {
   private EntityOwnedConstruct thePet;
   private EntityLivingBase theOwner;
   World theWorld;
   private double field_75336_f;
   private PathNavigate petPathfinder;
   private int field_75343_h;
   float maxDist;
   float minDist;
   private float field_75344_i;

   public AIFollowOwner(EntityOwnedConstruct p_i1625_1_, double p_i1625_2_, float p_i1625_4_, float p_i1625_5_) {
      this.thePet = p_i1625_1_;
      this.theWorld = p_i1625_1_.field_70170_p;
      this.field_75336_f = p_i1625_2_;
      this.petPathfinder = p_i1625_1_.func_70661_as();
      this.minDist = p_i1625_4_;
      this.maxDist = p_i1625_5_;
      this.func_75248_a(3);
      if (!(p_i1625_1_.func_70661_as() instanceof PathNavigateGround) && !(p_i1625_1_.func_70661_as() instanceof PathNavigateGolemAir)) {
         throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
      }
   }

   public boolean func_75250_a() {
      EntityLivingBase entitylivingbase = this.thePet.getOwnerEntity();
      if (entitylivingbase == null) {
         return false;
      } else if (this.thePet.func_70068_e(entitylivingbase) < (double)(this.minDist * this.minDist)) {
         return false;
      } else {
         this.theOwner = entitylivingbase;
         return true;
      }
   }

   public boolean func_75253_b() {
      return !this.petPathfinder.func_75500_f() && this.thePet.func_70068_e(this.theOwner) > (double)(this.maxDist * this.maxDist);
   }

   public void func_75249_e() {
      this.field_75343_h = 0;
      this.field_75344_i = this.thePet.func_184643_a(PathNodeType.WATER);
      this.thePet.func_184644_a(PathNodeType.WATER, 0.0F);
   }

   public void func_75251_c() {
      this.theOwner = null;
      this.petPathfinder.func_75499_g();
      this.thePet.func_184644_a(PathNodeType.WATER, this.field_75344_i);
   }

   private boolean func_181065_a(BlockPos p_181065_1_) {
      IBlockState iblockstate = this.theWorld.func_180495_p(p_181065_1_);
      Block block = iblockstate.func_177230_c();
      return block == Blocks.field_150350_a ? true : !iblockstate.func_185917_h();
   }

   public void func_75246_d() {
      this.thePet.func_70671_ap().func_75651_a(this.theOwner, 10.0F, (float)this.thePet.func_70646_bf());
      if (--this.field_75343_h <= 0) {
         this.field_75343_h = 10;
         if (!this.petPathfinder.func_75497_a(this.theOwner, this.field_75336_f) && !this.thePet.func_110167_bD() && this.thePet.func_70068_e(this.theOwner) >= 144.0D) {
            int i = MathHelper.func_76128_c(this.theOwner.field_70165_t) - 2;
            int j = MathHelper.func_76128_c(this.theOwner.field_70161_v) - 2;
            int k = MathHelper.func_76128_c(this.theOwner.func_174813_aQ().field_72338_b);

            for(int l = 0; l <= 4; ++l) {
               for(int i1 = 0; i1 <= 4; ++i1) {
                  if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.func_180495_p(new BlockPos(i + l, k - 1, j + i1)).func_185896_q() && this.func_181065_a(new BlockPos(i + l, k, j + i1)) && this.func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
                     this.thePet.func_70012_b((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.thePet.field_70177_z, this.thePet.field_70125_A);
                     this.petPathfinder.func_75499_g();
                     return;
                  }
               }
            }
         }
      }

   }
}
