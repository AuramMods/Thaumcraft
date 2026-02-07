package thaumcraft.common.tiles.essentia;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileTube extends TileThaumcraft implements IEssentiaTransport, IInteractWithCaster, ITickable {
   public static final int freq = 5;
   public EnumFacing facing;
   public boolean[] openSides;
   Aspect essentiaType;
   int essentiaAmount;
   Aspect suctionType;
   int suction;
   int venting;
   int count;
   int ventColor;

   public TileTube() {
      this.facing = EnumFacing.NORTH;
      this.openSides = new boolean[]{true, true, true, true, true, true};
      this.essentiaType = null;
      this.essentiaAmount = 0;
      this.suctionType = null;
      this.suction = 0;
      this.venting = 0;
      this.count = 0;
      this.ventColor = 0;
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.essentiaType = Aspect.getAspect(nbttagcompound.func_74779_i("type"));
      this.essentiaAmount = nbttagcompound.func_74762_e("amount");
      this.facing = EnumFacing.field_82609_l[nbttagcompound.func_74762_e("side")];
      byte[] sides = nbttagcompound.func_74770_j("open");
      if (sides != null && sides.length == 6) {
         for(int a = 0; a < 6; ++a) {
            this.openSides[a] = sides[a] == 1;
         }
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      if (this.essentiaType != null) {
         nbttagcompound.func_74778_a("type", this.essentiaType.getTag());
      }

      nbttagcompound.func_74768_a("amount", this.essentiaAmount);
      byte[] sides = new byte[6];

      for(int a = 0; a < 6; ++a) {
         sides[a] = (byte)(this.openSides[a] ? 1 : 0);
      }

      nbttagcompound.func_74768_a("side", this.facing.ordinal());
      nbttagcompound.func_74773_a("open", sides);
      return nbttagcompound;
   }

   public void func_145839_a(NBTTagCompound nbttagcompound) {
      super.func_145839_a(nbttagcompound);
      this.suctionType = Aspect.getAspect(nbttagcompound.func_74779_i("stype"));
      this.suction = nbttagcompound.func_74762_e("samount");
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
      super.func_189515_b(nbttagcompound);
      if (this.suctionType != null) {
         nbttagcompound.func_74778_a("stype", this.suctionType.getTag());
      }

      nbttagcompound.func_74768_a("samount", this.suction);
      return nbttagcompound;
   }

   public void func_73660_a() {
      if (this.venting > 0) {
         --this.venting;
      }

      if (this.count == 0) {
         this.count = this.field_145850_b.field_73012_v.nextInt(10);
      }

      if (!this.field_145850_b.field_72995_K) {
         if (this.venting <= 0) {
            if (++this.count % 2 == 0) {
               this.calculateSuction((Aspect)null, false, false);
               this.checkVenting();
               if (this.essentiaType != null && this.essentiaAmount == 0) {
                  this.essentiaType = null;
               }
            }

            if (this.count % 5 == 0 && this.suction > 0) {
               this.equalizeWithNeighbours(false);
            }
         }
      } else if (this.venting > 0) {
         Random r = new Random((long)(this.hashCode() * 4));
         float rp = r.nextFloat() * 360.0F;
         float ry = r.nextFloat() * 360.0F;
         double fx = (double)(-MathHelper.func_76126_a(ry / 180.0F * 3.1415927F) * MathHelper.func_76134_b(rp / 180.0F * 3.1415927F));
         double fz = (double)(MathHelper.func_76134_b(ry / 180.0F * 3.1415927F) * MathHelper.func_76134_b(rp / 180.0F * 3.1415927F));
         double fy = (double)(-MathHelper.func_76126_a(rp / 180.0F * 3.1415927F));
         FXDispatcher.INSTANCE.drawVentParticles((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, fx / 5.0D, fy / 5.0D, fz / 5.0D, this.ventColor);
      }

   }

   void calculateSuction(Aspect filter, boolean restrict, boolean directional) {
      this.suction = 0;
      this.suctionType = null;
      EnumFacing loc = null;

      for(int dir = 0; dir < 6; ++dir) {
         try {
            loc = EnumFacing.field_82609_l[dir];
            if ((!directional || this.facing == loc.func_176734_d()) && this.isConnectable(loc)) {
               TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.field_174879_c, loc);
               if (te != null) {
                  IEssentiaTransport ic = (IEssentiaTransport)te;
                  if ((filter == null || ic.getSuctionType(loc.func_176734_d()) == null || ic.getSuctionType(loc.func_176734_d()) == filter) && (filter != null || this.getEssentiaAmount(loc) <= 0 || ic.getSuctionType(loc.func_176734_d()) == null || this.getEssentiaType(loc) == ic.getSuctionType(loc.func_176734_d())) && (filter == null || this.getEssentiaAmount(loc) <= 0 || this.getEssentiaType(loc) == null || ic.getSuctionType(loc.func_176734_d()) == null || this.getEssentiaType(loc) == ic.getSuctionType(loc.func_176734_d()))) {
                     int suck = ic.getSuctionAmount(loc.func_176734_d());
                     if (suck > 0 && suck > this.suction + 1) {
                        Aspect st = ic.getSuctionType(loc.func_176734_d());
                        if (st == null) {
                           st = filter;
                        }

                        this.setSuction(st, restrict ? suck / 2 : suck - 1);
                     }
                  }
               }
            }
         } catch (Exception var10) {
         }
      }

   }

   void checkVenting() {
      EnumFacing loc = null;

      for(int dir = 0; dir < 6; ++dir) {
         try {
            loc = EnumFacing.field_82609_l[dir];
            if (this.isConnectable(loc)) {
               TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.field_174879_c, loc);
               if (te != null) {
                  IEssentiaTransport ic = (IEssentiaTransport)te;
                  int suck = ic.getSuctionAmount(loc.func_176734_d());
                  if (this.suction > 0 && (suck == this.suction || suck == this.suction - 1) && this.suctionType != ic.getSuctionType(loc.func_176734_d()) && !(te instanceof TileTubeFilter)) {
                     int c = -1;
                     if (this.suctionType != null) {
                        c = Config.aspectOrder.indexOf(this.suctionType);
                     }

                     this.field_145850_b.func_175641_c(this.field_174879_c, BlocksTC.tube, 1, c);
                     this.venting = 40;
                  }
               }
            }
         } catch (Exception var7) {
         }
      }

   }

   void equalizeWithNeighbours(boolean directional) {
      EnumFacing loc = null;
      if (this.essentiaAmount <= 0) {
         for(int dir = 0; dir < 6; ++dir) {
            try {
               loc = EnumFacing.field_82609_l[dir];
               if ((!directional || this.facing != loc.func_176734_d()) && this.isConnectable(loc)) {
                  TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.field_174879_c, loc);
                  if (te != null) {
                     IEssentiaTransport ic = (IEssentiaTransport)te;
                     if (ic.canOutputTo(loc.func_176734_d()) && (this.getSuctionType((EnumFacing)null) == null || this.getSuctionType((EnumFacing)null) == ic.getEssentiaType(loc.func_176734_d()) || ic.getEssentiaType(loc.func_176734_d()) == null) && this.getSuctionAmount((EnumFacing)null) > ic.getSuctionAmount(loc.func_176734_d()) && this.getSuctionAmount((EnumFacing)null) >= ic.getMinimumSuction()) {
                        Aspect a = this.getSuctionType((EnumFacing)null);
                        if (a == null) {
                           a = ic.getEssentiaType(loc.func_176734_d());
                           if (a == null) {
                              a = ic.getEssentiaType((EnumFacing)null);
                           }
                        }

                        int am = this.addEssentia(a, ic.takeEssentia(a, 1, loc.func_176734_d()), loc);
                        if (am > 0) {
                           if (this.field_145850_b.field_73012_v.nextInt(100) == 0) {
                              this.field_145850_b.func_175641_c(this.field_174879_c, BlocksTC.tube, 0, 0);
                           }

                           return;
                        }
                     }
                  }
               }
            } catch (Exception var8) {
            }
         }

      }
   }

   public boolean isConnectable(EnumFacing face) {
      return face == null ? false : this.openSides[face.ordinal()];
   }

   public boolean canInputFrom(EnumFacing face) {
      return face == null ? false : this.openSides[face.ordinal()];
   }

   public boolean canOutputTo(EnumFacing face) {
      return face == null ? false : this.openSides[face.ordinal()];
   }

   public void setSuction(Aspect aspect, int amount) {
      this.suctionType = aspect;
      this.suction = amount;
   }

   public Aspect getSuctionType(EnumFacing loc) {
      return this.suctionType;
   }

   public int getSuctionAmount(EnumFacing loc) {
      return this.suction;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return this.essentiaType;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return this.essentiaAmount;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
      if (this.canOutputTo(face) && this.essentiaType == aspect && this.essentiaAmount > 0 && amount > 0) {
         --this.essentiaAmount;
         if (this.essentiaAmount <= 0) {
            this.essentiaType = null;
         }

         this.func_70296_d();
         return 1;
      } else {
         return 0;
      }
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
      if (this.canInputFrom(face) && this.essentiaAmount == 0 && amount > 0) {
         this.essentiaType = aspect;
         ++this.essentiaAmount;
         this.func_70296_d();
         return 1;
      } else {
         return 0;
      }
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean func_145842_c(int i, int j) {
      if (i == 0) {
         if (this.field_145850_b.field_72995_K) {
            this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, SoundsTC.creak, SoundCategory.AMBIENT, 1.0F, 1.3F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F, false);
         }

         return true;
      } else if (i != 1) {
         return super.func_145842_c(i, j);
      } else {
         if (this.field_145850_b.field_72995_K) {
            if (this.venting <= 0) {
               this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.1F, 1.0F + this.field_145850_b.field_73012_v.nextFloat() * 0.1F, false);
            }

            this.venting = 50;
            if (j != -1 && j < Config.aspectOrder.size()) {
               this.ventColor = ((Aspect)Config.aspectOrder.get(j)).getColor();
            } else {
               this.ventColor = 11184810;
            }
         }

         return true;
      }
   }

   public boolean onCasterRightClick(World world, ItemStack wandstack, EntityPlayer player, BlockPos bp, EnumFacing side, EnumHand hand) {
      RayTraceResult hit = RayTracer.retraceBlock(world, player, this.field_174879_c);
      if (hit == null) {
         return false;
      } else {
         if (hit.subHit >= 0 && hit.subHit < 6) {
            player.field_70170_p.func_184134_a((double)bp.func_177958_n() + 0.5D, (double)bp.func_177956_o() + 0.5D, (double)bp.func_177952_p() + 0.5D, SoundsTC.tool, SoundCategory.BLOCKS, 0.5F, 0.9F + player.field_70170_p.field_73012_v.nextFloat() * 0.2F, false);
            player.func_184609_a(hand);
            this.func_70296_d();
            this.syncTile(true);
            this.openSides[hit.subHit] = !this.openSides[hit.subHit];
            EnumFacing dir = EnumFacing.field_82609_l[hit.subHit];
            TileEntity tile = world.func_175625_s(this.field_174879_c.func_177972_a(dir));
            if (tile != null && tile instanceof TileTube) {
               ((TileTube)tile).openSides[dir.func_176734_d().ordinal()] = this.openSides[hit.subHit];
               this.syncTile(true);
               tile.func_70296_d();
            }
         }

         if (hit.subHit == 6) {
            player.field_70170_p.func_184134_a((double)bp.func_177958_n() + 0.5D, (double)bp.func_177956_o() + 0.5D, (double)bp.func_177952_p() + 0.5D, SoundsTC.tool, SoundCategory.BLOCKS, 0.5F, 0.9F + player.field_70170_p.field_73012_v.nextFloat() * 0.2F, false);
            player.func_184609_a(hand);
            int a = this.facing.ordinal();
            this.func_70296_d();

            while(true) {
               ++a;
               if (a >= 20) {
                  break;
               }

               if (this.canConnectSide(EnumFacing.field_82609_l[a % 6].func_176734_d()) && this.isConnectable(EnumFacing.field_82609_l[a % 6].func_176734_d())) {
                  a %= 6;
                  this.facing = EnumFacing.field_82609_l[a];
                  this.syncTile(true);
                  this.func_70296_d();
                  break;
               }
            }
         }

         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 1), (double)(this.func_174877_v().func_177952_p() + 1));
   }

   public RayTraceResult rayTrace(World world, Vec3d vec3d, Vec3d vec3d1, RayTraceResult fullblock) {
      return fullblock;
   }

   public boolean canConnectSide(EnumFacing side) {
      TileEntity tile = this.field_145850_b.func_175625_s(this.field_174879_c.func_177972_a(side));
      return tile != null && tile instanceof IEssentiaTransport;
   }

   public void addTraceableCuboids(List<IndexedCuboid6> cuboids) {
      float min = 0.375F;
      float max = 0.625F;
      if (this.canConnectSide(EnumFacing.DOWN)) {
         cuboids.add(new IndexedCuboid6(0, new Cuboid6((double)((float)this.field_174879_c.func_177958_n() + min), (double)this.field_174879_c.func_177956_o(), (double)((float)this.field_174879_c.func_177952_p() + min), (double)((float)this.field_174879_c.func_177958_n() + max), (double)this.field_174879_c.func_177956_o() + 0.375D, (double)((float)this.field_174879_c.func_177952_p() + max))));
      }

      if (this.canConnectSide(EnumFacing.UP)) {
         cuboids.add(new IndexedCuboid6(1, new Cuboid6((double)((float)this.field_174879_c.func_177958_n() + min), (double)this.field_174879_c.func_177956_o() + 0.625D, (double)((float)this.field_174879_c.func_177952_p() + min), (double)((float)this.field_174879_c.func_177958_n() + max), (double)(this.field_174879_c.func_177956_o() + 1), (double)((float)this.field_174879_c.func_177952_p() + max))));
      }

      if (this.canConnectSide(EnumFacing.NORTH)) {
         cuboids.add(new IndexedCuboid6(2, new Cuboid6((double)((float)this.field_174879_c.func_177958_n() + min), (double)((float)this.field_174879_c.func_177956_o() + min), (double)this.field_174879_c.func_177952_p(), (double)((float)this.field_174879_c.func_177958_n() + max), (double)((float)this.field_174879_c.func_177956_o() + max), (double)this.field_174879_c.func_177952_p() + 0.375D)));
      }

      if (this.canConnectSide(EnumFacing.SOUTH)) {
         cuboids.add(new IndexedCuboid6(3, new Cuboid6((double)((float)this.field_174879_c.func_177958_n() + min), (double)((float)this.field_174879_c.func_177956_o() + min), (double)this.field_174879_c.func_177952_p() + 0.625D, (double)((float)this.field_174879_c.func_177958_n() + max), (double)((float)this.field_174879_c.func_177956_o() + max), (double)(this.field_174879_c.func_177952_p() + 1))));
      }

      if (this.canConnectSide(EnumFacing.WEST)) {
         cuboids.add(new IndexedCuboid6(4, new Cuboid6((double)this.field_174879_c.func_177958_n(), (double)((float)this.field_174879_c.func_177956_o() + min), (double)((float)this.field_174879_c.func_177952_p() + min), (double)this.field_174879_c.func_177958_n() + 0.375D, (double)((float)this.field_174879_c.func_177956_o() + max), (double)((float)this.field_174879_c.func_177952_p() + max))));
      }

      if (this.canConnectSide(EnumFacing.EAST)) {
         cuboids.add(new IndexedCuboid6(5, new Cuboid6((double)this.field_174879_c.func_177958_n() + 0.625D, (double)((float)this.field_174879_c.func_177956_o() + min), (double)((float)this.field_174879_c.func_177952_p() + min), (double)(this.field_174879_c.func_177958_n() + 1), (double)((float)this.field_174879_c.func_177956_o() + max), (double)((float)this.field_174879_c.func_177952_p() + max))));
      }

      cuboids.add(new IndexedCuboid6(6, new Cuboid6((double)this.field_174879_c.func_177958_n() + 0.375D, (double)this.field_174879_c.func_177956_o() + 0.375D, (double)this.field_174879_c.func_177952_p() + 0.375D, (double)this.field_174879_c.func_177958_n() + 0.625D, (double)this.field_174879_c.func_177956_o() + 0.625D, (double)this.field_174879_c.func_177952_p() + 0.625D)));
   }
}
