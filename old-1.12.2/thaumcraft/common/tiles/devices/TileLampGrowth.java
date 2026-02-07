package thaumcraft.common.tiles.devices;

import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockMist;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.lib.utils.CropUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileLampGrowth extends TileThaumcraft implements IEssentiaTransport, ITickable {
   private boolean reserve = false;
   public int charges = -1;
   public int maxCharges = 20;
   int lx = 0;
   int ly = 0;
   int lz = 0;
   Block lid;
   int lmd;
   ArrayList<BlockPos> checklist;
   int drawDelay;

   public TileLampGrowth() {
      this.lid = Blocks.field_150350_a;
      this.lmd = 0;
      this.checklist = new ArrayList();
      this.drawDelay = 0;
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.field_145850_b != null && this.field_145850_b.field_72995_K) {
         this.field_145850_b.func_180500_c(EnumSkyBlock.BLOCK, this.func_174877_v());
      }

   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.charges <= 0) {
            if (this.reserve) {
               this.charges = this.maxCharges;
               this.reserve = false;
               this.func_70296_d();
               this.syncTile(true);
            } else if (this.drawEssentia()) {
               this.charges = this.maxCharges;
               this.func_70296_d();
               this.syncTile(true);
            }

            if (this.charges <= 0) {
               if (BlockStateUtils.isEnabled(this.func_145832_p())) {
                  this.field_145850_b.func_180501_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.func_174877_v()).func_177226_a(IBlockEnabled.ENABLED, false), 3);
               }
            } else if (!this.gettingPower() && !BlockStateUtils.isEnabled(this.func_145832_p())) {
               this.field_145850_b.func_180501_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.func_174877_v()).func_177226_a(IBlockEnabled.ENABLED, true), 3);
            }
         }

         if (!this.reserve && this.drawEssentia()) {
            this.reserve = true;
         }

         if (this.charges == 0) {
            this.charges = -1;
            this.syncTile(true);
         }

         if (!this.gettingPower() && this.charges > 0) {
            this.updatePlant();
         }
      }

   }

   boolean isPlant(BlockPos bp) {
      IBlockState b = this.field_145850_b.func_180495_p(bp);
      boolean flag = b.func_177230_c() instanceof IGrowable;
      Material mat = b.func_185904_a();
      return (flag || mat == Material.field_151570_A || mat == Material.field_151585_k) && mat != Material.field_151577_b;
   }

   private void updatePlant() {
      IBlockState bs = this.field_145850_b.func_180495_p(new BlockPos(this.lx, this.ly, this.lz));
      if (this.lid != bs.func_177230_c() || this.lmd != bs.func_177230_c().func_176201_c(bs)) {
         EntityPlayer p = this.field_145850_b.func_184137_a((double)this.lx, (double)this.ly, (double)this.lz, 32.0D, false);
         if (p != null) {
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockMist(new BlockPos(this.lx, this.ly, this.lz), 4259648), new TargetPoint(this.field_145850_b.field_73011_w.getDimension(), (double)this.lx, (double)this.ly, (double)this.lz, 32.0D));
         }

         this.lid = bs.func_177230_c();
         this.lmd = bs.func_177230_c().func_176201_c(bs);
      }

      int distance = 6;
      int x;
      int y;
      if (this.checklist.size() == 0) {
         for(x = -distance; x <= distance; ++x) {
            for(y = -distance; y <= distance; ++y) {
               this.checklist.add(this.func_174877_v().func_177982_a(x, distance, y));
            }
         }

         Collections.shuffle(this.checklist, this.field_145850_b.field_73012_v);
      }

      x = ((BlockPos)this.checklist.get(0)).func_177958_n();
      y = ((BlockPos)this.checklist.get(0)).func_177956_o();
      int z = ((BlockPos)this.checklist.get(0)).func_177952_p();
      this.checklist.remove(0);

      while(y >= this.field_174879_c.func_177956_o() - distance) {
         BlockPos bp = new BlockPos(x, y, z);
         if (!this.field_145850_b.func_175623_d(bp) && this.isPlant(bp) && this.func_145835_a((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) < (double)(distance * distance) && !CropUtils.isGrownCrop(this.field_145850_b, bp) && CropUtils.doesLampGrow(this.field_145850_b, bp)) {
            --this.charges;
            this.lx = x;
            this.ly = y;
            this.lz = z;
            IBlockState bs2 = this.field_145850_b.func_180495_p(bp);
            this.lid = bs2.func_177230_c();
            this.lmd = bs2.func_177230_c().func_176201_c(bs2);
            this.field_145850_b.func_175684_a(bp, this.lid, 1);
            return;
         }

         --y;
      }

   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.reserve = nbttagcompound.func_74767_n("reserve");
      this.charges = nbttagcompound.func_74762_e("charges");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74757_a("reserve", this.reserve);
      nbttagcompound.func_74768_a("charges", this.charges);
      return nbttagcompound;
   }

   boolean drawEssentia() {
      if (++this.drawDelay % 5 != 0) {
         return false;
      } else {
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.func_174877_v(), BlockStateUtils.getFacing(this.func_145832_p()));
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d())) {
               return false;
            }

            if (ic.getSuctionAmount(BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d()) < this.getSuctionAmount(BlockStateUtils.getFacing(this.func_145832_p())) && ic.takeEssentia(Aspect.PLANT, 1, BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d()) == 1) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isConnectable(EnumFacing face) {
      return face == BlockStateUtils.getFacing(this.func_145832_p());
   }

   public boolean canInputFrom(EnumFacing face) {
      return face == BlockStateUtils.getFacing(this.func_145832_p());
   }

   public boolean canOutputTo(EnumFacing face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(EnumFacing face) {
      return Aspect.PLANT;
   }

   public int getSuctionAmount(EnumFacing face) {
      return face != BlockStateUtils.getFacing(this.func_145832_p()) || this.reserve && this.charges > 0 ? 0 : 128;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return null;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing loc) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing loc) {
      return 0;
   }
}
