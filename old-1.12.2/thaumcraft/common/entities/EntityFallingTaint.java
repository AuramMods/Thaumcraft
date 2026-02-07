package thaumcraft.common.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.world.taint.BlockTaint;
import thaumcraft.common.lib.SoundsTC;

public class EntityFallingTaint extends Entity implements IEntityAdditionalSpawnData {
   public IBlockState fallTile;
   BlockPos oldPos;
   public int fallTime = 0;
   private int fallHurtMax = 40;
   private float fallHurtAmount = 2.0F;

   public IBlockState getBlock() {
      return this.fallTile;
   }

   public EntityFallingTaint(World par1World) {
      super(par1World);
   }

   public EntityFallingTaint(World par1World, double par2, double par4, double par6, IBlockState par8, BlockPos o) {
      super(par1World);
      this.fallTile = par8;
      this.field_70156_m = true;
      this.func_70105_a(0.98F, 0.98F);
      this.func_70107_b(par2, par4, par6);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.field_70169_q = par2;
      this.field_70167_r = par4;
      this.field_70166_s = par6;
      this.oldPos = o;
   }

   protected boolean func_70041_e_() {
      return false;
   }

   protected void func_70088_a() {
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeInt(Block.func_149682_b(this.fallTile.func_177230_c()));
      data.writeByte(this.fallTile.func_177230_c().func_176201_c(this.fallTile));
   }

   public void readSpawnData(ByteBuf data) {
      try {
         this.fallTile = Block.func_149729_e(data.readInt()).func_176203_a(data.readByte());
      } catch (Exception var3) {
      }

   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   public void func_70071_h_() {
      if (this.fallTile != null && this.fallTile != Blocks.field_150350_a) {
         this.field_70169_q = this.field_70165_t;
         this.field_70167_r = this.field_70163_u;
         this.field_70166_s = this.field_70161_v;
         ++this.fallTime;
         this.field_70181_x -= 0.03999999910593033D;
         this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
         this.field_70159_w *= 0.9800000190734863D;
         this.field_70181_x *= 0.9800000190734863D;
         this.field_70179_y *= 0.9800000190734863D;
         BlockPos bp = new BlockPos(this);
         if (!this.field_70170_p.field_72995_K) {
            if (this.fallTime == 1) {
               if (this.field_70170_p.func_180495_p(this.oldPos) != this.fallTile) {
                  this.func_70106_y();
                  return;
               }

               this.field_70170_p.func_175698_g(this.oldPos);
            }

            if (!this.field_70122_E && this.field_70170_p.func_180495_p(bp.func_177977_b()) != BlocksTC.fluxGoo) {
               if (this.fallTime > 100 && !this.field_70170_p.field_72995_K && (bp.func_177956_o() < 1 || bp.func_177956_o() > 256) || this.fallTime > 600) {
                  this.func_70106_y();
               }
            } else {
               this.field_70159_w *= 0.699999988079071D;
               this.field_70179_y *= 0.699999988079071D;
               this.field_70181_x *= -0.5D;
               if (this.field_70170_p.func_180495_p(bp).func_177230_c() != Blocks.field_150331_J && this.field_70170_p.func_180495_p(bp).func_177230_c() != Blocks.field_180384_M && this.field_70170_p.func_180495_p(bp).func_177230_c() != Blocks.field_150332_K) {
                  this.func_184185_a(SoundsTC.gore, 0.5F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F) * 0.8F);
                  this.func_70106_y();
                  if (this.canPlace(bp) && !BlockTaint.canFallBelow(this.field_70170_p, bp.func_177977_b()) && this.field_70170_p.func_175656_a(bp, this.fallTile)) {
                  }
               }
            }
         } else if (this.field_70122_E || this.fallTime == 1) {
            for(int j = 0; j < 10; ++j) {
               FXDispatcher.INSTANCE.taintLandFX(this);
            }
         }
      } else {
         this.func_70106_y();
      }

   }

   private boolean canPlace(BlockPos pos) {
      return this.field_70170_p.func_180495_p(pos).func_177230_c() == BlocksTC.taintFibre || this.field_70170_p.func_180495_p(pos).func_177230_c() == BlocksTC.fluxGoo || this.field_70170_p.func_175716_a(this.fallTile.func_177230_c(), pos, true, EnumFacing.UP, (Entity)null, (ItemStack)null);
   }

   public void func_180430_e(float distance, float damageMultiplier) {
   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      Block block = this.fallTile != null ? this.fallTile.func_177230_c() : Blocks.field_150350_a;
      ResourceLocation resourcelocation = (ResourceLocation)Block.field_149771_c.func_177774_c(block);
      par1NBTTagCompound.func_74778_a("Block", resourcelocation == null ? "" : resourcelocation.toString());
      par1NBTTagCompound.func_74774_a("Data", (byte)block.func_176201_c(this.fallTile));
      par1NBTTagCompound.func_74774_a("Time", (byte)this.fallTime);
      par1NBTTagCompound.func_74776_a("FallHurtAmount", this.fallHurtAmount);
      par1NBTTagCompound.func_74768_a("FallHurtMax", this.fallHurtMax);
      par1NBTTagCompound.func_74772_a("Old", this.oldPos.func_177986_g());
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      int i = par1NBTTagCompound.func_74771_c("Data") & 255;
      if (par1NBTTagCompound.func_150297_b("Block", 8)) {
         this.fallTile = Block.func_149684_b(par1NBTTagCompound.func_74779_i("Block")).func_176203_a(i);
      } else if (par1NBTTagCompound.func_150297_b("TileID", 99)) {
         this.fallTile = Block.func_149729_e(par1NBTTagCompound.func_74762_e("TileID")).func_176203_a(i);
      } else {
         this.fallTile = Block.func_149729_e(par1NBTTagCompound.func_74771_c("Tile") & 255).func_176203_a(i);
      }

      this.fallTime = par1NBTTagCompound.func_74771_c("Time") & 255;
      this.oldPos = BlockPos.func_177969_a(par1NBTTagCompound.func_74763_f("Old"));
      if (par1NBTTagCompound.func_74764_b("HurtEntities")) {
         this.fallHurtAmount = par1NBTTagCompound.func_74760_g("FallHurtAmount");
         this.fallHurtMax = par1NBTTagCompound.func_74762_e("FallHurtMax");
      }

      if (this.fallTile == null) {
         this.fallTile = Blocks.field_150354_m.func_176223_P();
      }

   }

   public void func_85029_a(CrashReportCategory par1CrashReportCategory) {
      super.func_85029_a(par1CrashReportCategory);
      par1CrashReportCategory.func_71507_a("Immitating block ID", Block.func_149682_b(this.fallTile.func_177230_c()));
      par1CrashReportCategory.func_71507_a("Immitating block data", this.fallTile.func_177230_c().func_176201_c(this.fallTile));
   }

   public SoundCategory func_184176_by() {
      return SoundCategory.BLOCKS;
   }

   @SideOnly(Side.CLIENT)
   public World getWorld() {
      return this.field_70170_p;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_90999_ad() {
      return false;
   }
}
