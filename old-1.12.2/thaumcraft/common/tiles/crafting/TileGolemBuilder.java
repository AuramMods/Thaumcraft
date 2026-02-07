package thaumcraft.common.tiles.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.construct.golem.GolemProperties;
import thaumcraft.common.entities.construct.golem.ItemGolemPlacer;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileThaumcraftInventory;

public class TileGolemBuilder extends TileThaumcraftInventory implements ITickable, IEssentiaTransport {
   public long golem = -1L;
   public int cost = 0;
   public int maxCost = 0;
   boolean bufferedEssentia = false;
   int ticks = 0;
   public int press = 0;
   IGolemProperties props = null;
   ItemStack[] components = null;

   public TileGolemBuilder() {
      this.itemStacks = new ItemStack[1];
      this.syncedSlots = new int[]{0};
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.itemStacks = new ItemStack[1];
      this.syncedSlots = new int[]{0};
      super.readSyncNBT(nbttagcompound);
      this.golem = nbttagcompound.func_74763_f("golem");
      this.cost = nbttagcompound.func_74762_e("cost");
      this.maxCost = nbttagcompound.func_74762_e("mcost");
      if (this.golem >= 0L) {
         try {
            this.props = GolemProperties.fromLong(this.golem);
            this.components = this.props.generateComponents();
         } catch (Exception var3) {
            this.props = null;
            this.components = null;
            this.cost = 0;
            this.golem = -1L;
         }
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74772_a("golem", this.golem);
      nbttagcompound.func_74768_a("cost", this.cost);
      nbttagcompound.func_74768_a("mcost", this.maxCost);
      return super.writeSyncNBT(nbttagcompound);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)(this.field_174879_c.func_177958_n() - 1), (double)this.field_174879_c.func_177956_o(), (double)(this.field_174879_c.func_177952_p() - 1), (double)(this.field_174879_c.func_177958_n() + 2), (double)(this.field_174879_c.func_177956_o() + 2), (double)(this.field_174879_c.func_177952_p() + 2));
   }

   public void func_73660_a() {
      boolean complete = false;
      if (!this.field_145850_b.field_72995_K) {
         ++this.ticks;
         if (this.ticks % 5 == 0 && !complete && this.cost > 0 && this.golem >= 0L) {
            if (this.bufferedEssentia || this.drawEssentia()) {
               this.bufferedEssentia = false;
               --this.cost;
               this.func_70296_d();
            }

            if (this.cost <= 0) {
               ItemStack placer = new ItemStack(ItemsTC.golemPlacer);
               placer.func_77983_a("props", new NBTTagLong(this.golem));
               if (this.func_70301_a(0) == null || this.func_70301_a(0).field_77994_a < this.func_70301_a(0).func_77976_d() && this.func_70301_a(0).func_77969_a(placer) && ItemStack.func_77970_a(this.func_70301_a(0), placer)) {
                  if (this.func_70301_a(0) == null) {
                     this.func_70299_a(0, placer.func_77946_l());
                  } else {
                     ++this.func_70301_a(0).field_77994_a;
                  }

                  complete = true;
                  this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.wand, SoundCategory.BLOCKS, 1.0F, 1.0F);
               }
            }
         }
      } else {
         int a;
         if (this.press < 90 && this.cost > 0 && this.golem > 0L) {
            this.press += 6;
            if (this.press >= 60) {
               this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.66F, 1.0F + this.field_145850_b.field_73012_v.nextFloat() * 0.1F, false);

               for(a = 0; a < FXDispatcher.INSTANCE.particleCount(8); ++a) {
                  FXDispatcher.INSTANCE.drawVentParticles((double)this.field_174879_c.func_177958_n() + 0.5D, (double)(this.field_174879_c.func_177956_o() + 1), (double)this.field_174879_c.func_177952_p() + 0.5D, this.field_145850_b.field_73012_v.nextGaussian() * 0.1D, 0.0D, this.field_145850_b.field_73012_v.nextGaussian() * 0.1D, 11184810);
               }
            }
         }

         if (this.press >= 90 && this.field_145850_b.field_73012_v.nextInt(8) == 0) {
            FXDispatcher.INSTANCE.drawVentParticles((double)this.field_174879_c.func_177958_n() + 0.5D, (double)(this.field_174879_c.func_177956_o() + 1), (double)this.field_174879_c.func_177952_p() + 0.5D, this.field_145850_b.field_73012_v.nextGaussian() * 0.1D, 0.0D, this.field_145850_b.field_73012_v.nextGaussian() * 0.1D, 11184810);
            this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.1F, 1.0F + this.field_145850_b.field_73012_v.nextFloat() * 0.1F, false);
         }

         if (this.press > 0 && (this.cost <= 0 || this.golem == -1L)) {
            if (this.press >= 90) {
               for(a = 0; a < FXDispatcher.INSTANCE.particleCount(5); ++a) {
                  FXDispatcher.INSTANCE.drawVentParticles((double)this.field_174879_c.func_177958_n() + 0.5D, (double)(this.field_174879_c.func_177956_o() + 1), (double)this.field_174879_c.func_177952_p() + 0.5D, this.field_145850_b.field_73012_v.nextGaussian() * 0.1D, 0.0D, this.field_145850_b.field_73012_v.nextGaussian() * 0.1D, 11184810);
               }
            }

            this.press -= 3;
         }
      }

      if (complete) {
         this.cost = 0;
         this.golem = -1L;
         this.syncTile(false);
         this.func_70296_d();
      }

   }

   public boolean startCraft(long id, EntityPlayer p) {
      ItemStack placer = new ItemStack(ItemsTC.golemPlacer);
      placer.func_77983_a("props", new NBTTagLong(id));
      if (this.func_70301_a(0) == null || this.func_70301_a(0).field_77994_a < this.func_70301_a(0).func_77976_d() && this.func_70301_a(0).func_77969_a(placer) && ItemStack.func_77970_a(this.func_70301_a(0), placer)) {
         this.golem = id;
         this.props = GolemProperties.fromLong(this.golem);
         this.components = this.props.generateComponents();
         ItemStack[] var5 = this.components;
         int var6 = var5.length;

         int var7;
         ItemStack stack;
         for(var7 = 0; var7 < var6; ++var7) {
            stack = var5[var7];
            if (!InventoryUtils.isPlayerCarryingAmount(p, stack, true)) {
               this.cost = 0;
               this.props = null;
               this.components = null;
               this.golem = -1L;
               return false;
            }
         }

         this.cost = this.props.getTraits().size() * 2;
         var5 = this.components;
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            stack = var5[var7];
            this.cost += stack.field_77994_a;
            InventoryUtils.consumeInventoryItem(p, stack, true, true);
         }

         this.maxCost = this.cost;
         this.func_70296_d();
         this.syncTile(false);
         this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.wand, SoundCategory.BLOCKS, 0.25F, 1.0F);
         return true;
      } else {
         this.cost = 0;
         this.props = null;
         this.components = null;
         this.golem = -1L;
         return false;
      }
   }

   public boolean func_94041_b(int par1, ItemStack par2ItemStack) {
      return par2ItemStack != null && par2ItemStack.func_77973_b() instanceof ItemGolemPlacer;
   }

   boolean drawEssentia() {
      EnumFacing[] var1 = EnumFacing.field_82609_l;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing face = var1[var3];
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.func_174877_v(), face);
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(face.func_176734_d())) {
               return false;
            }

            if (ic.getSuctionAmount(face.func_176734_d()) < this.getSuctionAmount(face) && ic.takeEssentia(Aspect.MECHANISM, 1, face.func_176734_d()) == 1) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isConnectable(EnumFacing face) {
      return face.func_176736_b() >= 0 || face == EnumFacing.DOWN;
   }

   public boolean canInputFrom(EnumFacing face) {
      return this.isConnectable(face);
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
      return Aspect.MECHANISM;
   }

   public int getSuctionAmount(EnumFacing face) {
      return this.cost > 0 && this.golem >= 0L ? 128 : 0;
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
      if (!this.bufferedEssentia && this.cost > 0 && this.golem >= 0L && aspect == Aspect.MECHANISM) {
         this.bufferedEssentia = true;
         return 1;
      } else {
         return 0;
      }
   }
}
