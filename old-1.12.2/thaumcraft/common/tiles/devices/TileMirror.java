package thaumcraft.common.tiles.devices;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSpell.AmbientMobFactory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileMirror extends TileThaumcraft implements IInventory, ITickable {
   public boolean linked = false;
   public int linkX;
   public int linkY;
   public int linkZ;
   public int linkDim;
   public int instability;
   int count = 0;
   int inc = 40;
   private ArrayList<ItemStack> outputStacks = new ArrayList();

   public void restoreLink() {
      if (this.isDestinationValid()) {
         World targetWorld = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(this.linkDim);
         if (targetWorld == null) {
            return;
         }

         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirror) {
            TileMirror tm = (TileMirror)te;
            tm.linked = true;
            tm.linkX = this.func_174877_v().func_177958_n();
            tm.linkY = this.func_174877_v().func_177956_o();
            tm.linkZ = this.func_174877_v().func_177952_p();
            tm.linkDim = this.field_145850_b.field_73011_w.getDimension();
            tm.syncTile(false);
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
            if (te != null && te instanceof TileMirror) {
               TileMirror tm = (TileMirror)te;
               tm.linked = false;
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
            if (te != null && te instanceof TileMirror) {
               TileMirror tm = (TileMirror)te;
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
            if (te != null && te instanceof TileMirror) {
               TileMirror tm = (TileMirror)te;
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
         if (te != null && te instanceof TileMirror) {
            TileMirror tm = (TileMirror)te;
            return !tm.isLinkValid();
         } else {
            this.linked = false;
            this.func_70296_d();
            this.syncTile(false);
            return false;
         }
      }
   }

   public boolean transport(EntityItem ie) {
      ItemStack items = ie.func_92059_d();
      if (this.linked && this.isLinkValid()) {
         World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(this.linkDim);
         TileEntity target = world.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (target != null && target instanceof TileMirror) {
            ((TileMirror)target).addStack(items);
            this.addInstability((World)null, items.field_77994_a);
            ie.func_70106_y();
            this.func_70296_d();
            target.func_70296_d();
            world.func_175641_c(this.func_174877_v(), this.field_145854_h, 1, 0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean transportDirect(ItemStack items) {
      if (items != null && items.field_77994_a > 0) {
         this.addStack(items.func_77946_l());
         this.func_70296_d();
         return true;
      } else {
         return false;
      }
   }

   public void eject() {
      if (this.outputStacks.size() > 0 && this.count > 20) {
         int i = this.field_145850_b.field_73012_v.nextInt(this.outputStacks.size());
         if (this.outputStacks.get(i) != null) {
            ItemStack outItem = ((ItemStack)this.outputStacks.get(i)).func_77946_l();
            outItem.field_77994_a = 1;
            if (this.spawnItem(outItem)) {
               --((ItemStack)this.outputStacks.get(i)).field_77994_a;
               this.addInstability((World)null, 1);
               this.field_145850_b.func_175641_c(this.func_174877_v(), this.field_145854_h, 1, 0);
               if (((ItemStack)this.outputStacks.get(i)).field_77994_a <= 0) {
                  this.outputStacks.remove(i);
               }

               this.func_70296_d();
            }
         }
      }

   }

   public boolean spawnItem(ItemStack stack) {
      try {
         EnumFacing face = BlockStateUtils.getFacing(this.func_145832_p());
         EntityItem ie2 = new EntityItem(this.field_145850_b, (double)this.func_174877_v().func_177958_n() + 0.5D, (double)this.func_174877_v().func_177956_o() + 0.25D, (double)this.func_174877_v().func_177952_p() + 0.5D, stack);
         ie2.field_70159_w = (double)((float)face.func_82601_c() * 0.15F);
         ie2.field_70181_x = (double)((float)face.func_96559_d() * 0.15F);
         ie2.field_70179_y = (double)((float)face.func_82599_e() * 0.15F);
         ie2.field_71088_bW = 20;
         this.field_145850_b.func_72838_d(ie2);
         return true;
      } catch (Exception var4) {
         return false;
      }
   }

   protected void addInstability(World targetWorld, int amt) {
      this.instability += amt;
      this.func_70296_d();
      if (targetWorld != null) {
         TileEntity te = targetWorld.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
         if (te != null && te instanceof TileMirror) {
            ((TileMirror)te).instability += amt;
            if (((TileMirror)te).instability < 0) {
               ((TileMirror)te).instability = 0;
            }

            te.func_70296_d();
         }
      }

   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      super.readSyncNBT(nbttagcompound);
      this.linked = nbttagcompound.func_74767_n("linked");
      this.linkX = nbttagcompound.func_74762_e("linkX");
      this.linkY = nbttagcompound.func_74762_e("linkY");
      this.linkZ = nbttagcompound.func_74762_e("linkZ");
      this.linkDim = nbttagcompound.func_74762_e("linkDim");
      this.instability = nbttagcompound.func_74762_e("instability");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      super.writeSyncNBT(nbttagcompound);
      nbttagcompound.func_74757_a("linked", this.linked);
      nbttagcompound.func_74768_a("linkX", this.linkX);
      nbttagcompound.func_74768_a("linkY", this.linkY);
      nbttagcompound.func_74768_a("linkZ", this.linkZ);
      nbttagcompound.func_74768_a("linkDim", this.linkDim);
      nbttagcompound.func_74768_a("instability", this.instability);
      return nbttagcompound;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_145842_c(int i, int j) {
      if (i != 1) {
         return super.func_145842_c(i, j);
      } else {
         if (this.field_145850_b.field_72995_K) {
            EnumFacing face = BlockStateUtils.getFacing(this.func_145832_p());

            for(int q = 0; q < FXDispatcher.INSTANCE.particleCount(1); ++q) {
               double xx = (double)this.func_174877_v().func_177958_n() + 0.33D + (double)(this.field_145850_b.field_73012_v.nextFloat() * 0.33F) - (double)face.func_82601_c() / 2.0D;
               double yy = (double)this.func_174877_v().func_177956_o() + 0.33D + (double)(this.field_145850_b.field_73012_v.nextFloat() * 0.33F) - (double)face.func_96559_d() / 2.0D;
               double zz = (double)this.func_174877_v().func_177952_p() + 0.33D + (double)(this.field_145850_b.field_73012_v.nextFloat() * 0.33F) - (double)face.func_82599_e() / 2.0D;
               AmbientMobFactory amf = new AmbientMobFactory();
               Particle var21 = amf.func_178902_a(0, this.field_145850_b, xx, yy, zz, (double)face.func_82601_c() * 0.05D, (double)face.func_96559_d() * 0.05D, (double)face.func_82599_e() * 0.05D, new int[0]);
               Minecraft.func_71410_x().field_71452_i.func_78873_a(var21);
            }
         }

         return true;
      }
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         this.eject();
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
      if (this.instability > 128) {
         AuraHelper.polluteAura(this.field_145850_b, this.field_174879_c, 1.0F, true);
         this.instability -= 128;
         this.func_70296_d();
      }

      if (this.instability > 0 && this.count % 100 == 0) {
         --this.instability;
      }

   }

   public void func_145839_a(NBTTagCompound nbtCompound) {
      super.func_145839_a(nbtCompound);
      NBTTagList nbttaglist = nbtCompound.func_150295_c("Items", 10);
      this.outputStacks = new ArrayList();

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         byte b0 = nbttagcompound1.func_74771_c("Slot");
         this.outputStacks.add(ItemStack.func_77949_a(nbttagcompound1));
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtCompound) {
      super.func_189515_b(nbtCompound);
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.outputStacks.size(); ++i) {
         if (this.outputStacks.get(i) != null && ((ItemStack)this.outputStacks.get(i)).field_77994_a > 0) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            ((ItemStack)this.outputStacks.get(i)).func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbtCompound.func_74782_a("Items", nbttaglist);
      return nbtCompound;
   }

   public int func_70302_i_() {
      return 1;
   }

   public ItemStack func_70301_a(int par1) {
      return null;
   }

   public ItemStack func_70298_a(int par1, int par2) {
      return null;
   }

   public ItemStack func_70304_b(int par1) {
      return null;
   }

   public void addStack(ItemStack stack) {
      this.outputStacks.add(stack);
      this.func_70296_d();
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(this.linkDim);
      TileEntity target = world.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
      if (target != null && target instanceof TileMirror) {
         ((TileMirror)target).addStack(par2ItemStack.func_77946_l());
         this.addInstability((World)null, par2ItemStack.field_77994_a);
         world.func_175641_c(this.func_174877_v(), this.field_145854_h, 1, 0);
      } else {
         this.spawnItem(par2ItemStack.func_77946_l());
      }

   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer var1) {
      return false;
   }

   public boolean func_94041_b(int var1, ItemStack var2) {
      World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(this.linkDim);
      TileEntity target = world.func_175625_s(new BlockPos(this.linkX, this.linkY, this.linkZ));
      return target != null && target instanceof TileMirror;
   }

   public String func_70005_c_() {
      return null;
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {
   }

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
   }
}
