package thaumcraft.common.tiles.devices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileLampFertility extends TileThaumcraft implements IEssentiaTransport, ITickable {
   public int charges = 0;
   int count = 0;
   int drawDelay = 0;

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.field_145850_b != null && this.field_145850_b.field_72995_K) {
         this.field_145850_b.func_180500_c(EnumSkyBlock.BLOCK, this.func_174877_v());
      }

   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.charges < 10) {
            if (this.drawEssentia()) {
               ++this.charges;
               this.func_70296_d();
               this.syncTile(true);
            }

            if (this.charges <= 1) {
               if (BlockStateUtils.isEnabled(this.func_145832_p())) {
                  this.field_145850_b.func_180501_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.func_174877_v()).func_177226_a(IBlockEnabled.ENABLED, false), 3);
               }
            } else if (!this.gettingPower() && !BlockStateUtils.isEnabled(this.func_145832_p())) {
               this.field_145850_b.func_180501_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.func_174877_v()).func_177226_a(IBlockEnabled.ENABLED, true), 3);
            }
         }

         if (!this.gettingPower() && this.charges > 1 && this.count++ % 300 == 0) {
            this.updateAnimals();
         }
      }

   }

   private void updateAnimals() {
      int distance = 7;
      List<EntityAnimal> var5 = this.field_145850_b.func_72872_a(EntityAnimal.class, (new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + 1), (double)(this.field_174879_c.func_177952_p() + 1))).func_72314_b((double)distance, (double)distance, (double)distance));
      Iterator var2 = var5.iterator();

      while(var2.hasNext()) {
         EntityAnimal var3 = (EntityAnimal)var2.next();
         EntityLivingBase var4 = var3;
         if (var3.func_70874_b() == 0 && !var3.func_70880_s()) {
            ArrayList<EntityAnimal> sa = new ArrayList();
            Iterator varq = var5.iterator();

            while(varq.hasNext()) {
               EntityLivingBase var7 = (EntityLivingBase)varq.next();
               if (var7.getClass().equals(var4.getClass())) {
                  sa.add((EntityAnimal)var7);
               }
            }

            if (sa == null || sa.size() <= 9) {
               Iterator var22 = sa.iterator();
               EntityAnimal partner = null;

               while(var22.hasNext()) {
                  EntityAnimal var33 = (EntityAnimal)var22.next();
                  if (var33.func_70874_b() == 0 && !var33.func_70880_s()) {
                     if (partner != null) {
                        this.charges -= 5;
                        var33.func_146082_f((EntityPlayer)null);
                        partner.func_146082_f((EntityPlayer)null);
                        return;
                     }

                     partner = var33;
                  }
               }
            }
         }
      }

   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.charges = nbttagcompound.func_74762_e("charges");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
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

            if (ic.getSuctionAmount(BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d()) < this.getSuctionAmount(BlockStateUtils.getFacing(this.func_145832_p())) && ic.takeEssentia(Aspect.DESIRE, 1, BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d()) == 1) {
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
      return Aspect.DESIRE;
   }

   public int getSuctionAmount(EnumFacing face) {
      return face == BlockStateUtils.getFacing(this.func_145832_p()) ? 128 - this.charges * 10 : 0;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return null;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
      return 0;
   }
}
