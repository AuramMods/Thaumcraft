package thaumcraft.common.tiles.devices;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileMirrorEssentia extends TileThaumcraft implements IAspectSource, ITickable {
   public boolean linked = false;
   public int linkX;
   public int linkY;
   public int linkZ;
   public int linkDim;
   public EnumFacing linkedFacing;
   public int instability;
   int count;
   int inc;

   public TileMirrorEssentia() {
      this.linkedFacing = EnumFacing.DOWN;
      this.count = 0;
      this.inc = 40;
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.linked = nbttagcompound.func_74767_n("linked");
      this.linkX = nbttagcompound.func_74762_e("linkX");
      this.linkY = nbttagcompound.func_74762_e("linkY");
      this.linkZ = nbttagcompound.func_74762_e("linkZ");
      this.linkDim = nbttagcompound.func_74762_e("linkDim");
      this.instability = nbttagcompound.func_74762_e("instability");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74757_a("linked", this.linked);
      nbttagcompound.func_74768_a("linkX", this.linkX);
      nbttagcompound.func_74768_a("linkY", this.linkY);
      nbttagcompound.func_74768_a("linkZ", this.linkZ);
      nbttagcompound.func_74768_a("linkDim", this.linkDim);
      nbttagcompound.func_74768_a("instability", this.instability);
      return nbttagcompound;
   }

   protected void addInstability(World targetWorld, int amt) {
      this.instability += amt;
      this.func_70296_d();
      if (targetWorld != null) {
         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirrorEssentia) {
            ((TileMirrorEssentia)te).instability += amt;
            if (((TileMirrorEssentia)te).instability < 0) {
               ((TileMirrorEssentia)te).instability = 0;
            }

            te.func_70296_d();
         }
      }

   }

   public void restoreLink() {
      if (this.isDestinationValid()) {
         World targetWorld = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(this.linkDim);
         if (targetWorld == null) {
            return;
         }

         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirrorEssentia) {
            TileMirrorEssentia tm = (TileMirrorEssentia)te;
            tm.linked = true;
            tm.linkX = this.func_174877_v().func_177958_n();
            tm.linkY = this.func_174877_v().func_177956_o();
            tm.linkZ = this.func_174877_v().func_177952_p();
            tm.linkDim = this.field_145850_b.field_73011_w.getDimension();
            tm.syncTile(false);
            this.linkedFacing = BlockStateUtils.getFacing(targetWorld.func_180495_p(new BlockPos(this.linkX, this.linkY, this.linkZ)));
            this.linked = true;
            this.func_70296_d();
            tm.func_70296_d();
            this.syncTile(false);
         }
      }

   }

   public void invalidateLink() {
      World targetWorld = DimensionManager.getWorld(this.linkDim);
      if (targetWorld != null) {
         if (Utils.isChunkLoaded(targetWorld, this.linkX, this.linkZ)) {
            TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
            if (te != null && te instanceof TileMirrorEssentia) {
               TileMirrorEssentia tm = (TileMirrorEssentia)te;
               tm.linked = false;
               tm.linkedFacing = EnumFacing.DOWN;
               this.func_70296_d();
               tm.func_70296_d();
               tm.syncTile(false);
            }

         }
      }
   }

   public boolean isLinkValid() {
      if (!this.linked) {
         return false;
      } else {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (targetWorld == null) {
            return false;
         } else {
            TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
            if (te != null && te instanceof TileMirrorEssentia) {
               TileMirrorEssentia tm = (TileMirrorEssentia)te;
               if (!tm.linked) {
                  this.linked = false;
                  this.func_70296_d();
                  this.syncTile(false);
                  return false;
               } else if (tm.linkX == this.func_174877_v().func_177958_n() && tm.linkY == this.func_174877_v().func_177956_o() && tm.linkZ == this.func_174877_v().func_177952_p() && tm.linkDim == this.field_145850_b.field_73011_w.getDimension()) {
                  return true;
               } else {
                  this.linked = false;
                  this.func_70296_d();
                  this.syncTile(false);
                  return false;
               }
            } else {
               this.linked = false;
               this.func_70296_d();
               this.syncTile(false);
               return false;
            }
         }
      }
   }

   public boolean isLinkValidSimple() {
      if (!this.linked) {
         return false;
      } else {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (targetWorld == null) {
            return false;
         } else {
            TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
            if (te != null && te instanceof TileMirrorEssentia) {
               TileMirrorEssentia tm = (TileMirrorEssentia)te;
               if (!tm.linked) {
                  return false;
               } else {
                  return tm.linkX == this.func_174877_v().func_177958_n() && tm.linkY == this.func_174877_v().func_177956_o() && tm.linkZ == this.func_174877_v().func_177952_p() && tm.linkDim == this.field_145850_b.field_73011_w.getDimension();
               }
            } else {
               return false;
            }
         }
      }
   }

   public boolean isDestinationValid() {
      World targetWorld = DimensionManager.getWorld(this.linkDim);
      if (targetWorld == null) {
         return false;
      } else {
         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirrorEssentia) {
            TileMirrorEssentia tm = (TileMirrorEssentia)te;
            return !tm.isLinkValid();
         } else {
            this.linked = false;
            this.func_70296_d();
            this.syncTile(false);
            return false;
         }
      }
   }

   public AspectList getAspects() {
      return null;
   }

   public void setAspects(AspectList aspects) {
   }

   public boolean doesContainerAccept(Aspect tag) {
      World targetWorld = DimensionManager.getWorld(this.linkDim);
      if (this.linkedFacing == EnumFacing.DOWN && targetWorld != null) {
         this.linkedFacing = BlockStateUtils.getFacing(targetWorld.func_180495_p(new BlockPos(this.linkX, this.linkY, this.linkZ)));
      }

      TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
      return te != null && te instanceof TileMirrorEssentia ? EssentiaHandler.canAcceptEssentia(te, tag, this.linkedFacing, 8, true) : true;
   }

   public int addToContainer(Aspect tag, int amount) {
      if (this.isLinkValid() && amount <= 1) {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (this.linkedFacing == EnumFacing.DOWN && targetWorld != null) {
            this.linkedFacing = BlockStateUtils.getFacing(targetWorld.func_180495_p(new BlockPos(this.linkX, this.linkY, this.linkZ)));
         }

         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirrorEssentia) {
            boolean b = EssentiaHandler.addEssentia(te, tag, this.linkedFacing, 8, true, 5);
            if (b) {
               this.addInstability((World)null, amount);
            }

            return b ? 0 : 1;
         } else {
            return amount;
         }
      } else {
         return amount;
      }
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      if (this.isLinkValid() && amount <= 1) {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (this.linkedFacing == EnumFacing.DOWN && targetWorld != null) {
            this.linkedFacing = BlockStateUtils.getFacing(targetWorld.func_180495_p(new BlockPos(this.linkX, this.linkY, this.linkZ)));
         }

         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirrorEssentia) {
            boolean b = EssentiaHandler.drainEssentia(te, tag, this.linkedFacing, 8, true, 5);
            if (b) {
               this.addInstability((World)null, amount);
            }

            return b;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      if (this.isLinkValid() && amount <= 1) {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (this.linkedFacing == EnumFacing.DOWN && targetWorld != null) {
            this.linkedFacing = BlockStateUtils.getFacing(targetWorld.func_180495_p(new BlockPos(this.linkX, this.linkY, this.linkZ)));
         }

         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         return te != null && te instanceof TileMirrorEssentia ? EssentiaHandler.findEssentia(te, tag, this.linkedFacing, 8, true) : false;
      } else {
         return false;
      }
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         this.checkInstability();
         if (this.count++ % this.inc == 0) {
            if (!this.isLinkValidSimple()) {
               if (this.inc < 600) {
                  this.inc += 20;
               }

               this.restoreLink();
            } else {
               this.inc = 40;
            }
         }
      }

   }

   public void checkInstability() {
      if (this.instability > 64) {
         AuraHelper.polluteAura(this.field_145850_b, this.field_174879_c, 1.0F, true);
         this.instability -= 64;
         this.func_70296_d();
      }

      if (this.instability > 0 && this.count % 100 == 0) {
         --this.instability;
      }

   }

   public boolean isBlocked() {
      return false;
   }
}
