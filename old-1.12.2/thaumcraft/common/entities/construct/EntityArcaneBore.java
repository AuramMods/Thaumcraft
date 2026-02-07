package thaumcraft.common.entities.construct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBoreDig;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.aura.AuraHandler;

public class EntityArcaneBore extends EntityOwnedConstruct {
   EnumFacing lastInventoryFace;
   BlockPos digTarget;
   BlockPos digTargetPrev;
   float digCost;
   int paused;
   int maxPause;
   long soundDelay;
   Object beam1;
   double beamLength;
   int breakCounter;
   int digDelay;
   int digDelayMax;
   float radInc;
   public int spiral;
   public float currentRadius;
   private float charge;
   private static final DataParameter<EnumFacing> FACING;
   private static final DataParameter<Boolean> ACTIVE;
   public boolean clientDigging;

   public EntityArcaneBore(World worldIn) {
      super(worldIn);
      this.lastInventoryFace = null;
      this.digTarget = null;
      this.digTargetPrev = null;
      this.digCost = 0.25F;
      this.paused = 100;
      this.maxPause = 100;
      this.soundDelay = 0L;
      this.beam1 = null;
      this.beamLength = 0.0D;
      this.breakCounter = 0;
      this.digDelay = 0;
      this.digDelayMax = 0;
      this.radInc = 0.0F;
      this.spiral = 0;
      this.currentRadius = 0.0F;
      this.charge = 0.0F;
      this.clientDigging = false;
      this.func_70105_a(0.9F, 0.9F);
   }

   public EntityArcaneBore(World worldIn, BlockPos pos, EnumFacing facing) {
      this(worldIn);
      this.setFacing(facing);
      this.func_70080_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5D, 0.0F, 0.0F);
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(50.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(32.0D);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K) {
         this.field_70177_z = this.field_70759_as;
         if (this.field_70173_aa % 50 == 0) {
            this.func_70691_i(1.0F);
         }

         if (this.field_70173_aa % 10 == 0 && this.getCharge() < 10.0F) {
            this.rechargeVis();
         }

         int k = MathHelper.func_76128_c(this.field_70165_t);
         int l = MathHelper.func_76128_c(this.field_70163_u);
         int i1 = MathHelper.func_76128_c(this.field_70161_v);
         if (BlockRailBase.func_176562_d(this.field_70170_p, new BlockPos(k, l - 1, i1))) {
            --l;
         }

         BlockPos blockpos = new BlockPos(k, l, i1);
         IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
         if (BlockRailBase.func_176563_d(iblockstate)) {
            if (iblockstate.func_177230_c() == BlocksTC.activatorRail) {
               boolean ac = (Boolean)iblockstate.func_177229_b(BlockRailPowered.field_176569_M);
               this.setActive(!ac);
            }
         } else {
            this.setActive(this.field_70170_p.func_175676_y((new BlockPos(this)).func_177977_b()) > 0);
         }

         if (this.validInventory()) {
            try {
               this.func_184614_ca().func_77945_a(this.field_70170_p, this, 0, true);
            } catch (Exception var7) {
            }
         }
      }

      if (!this.isActive()) {
         this.digTarget = null;
         this.func_70671_ap().func_75650_a(this.field_70165_t + (double)this.getFacing().func_82601_c(), this.field_70163_u, this.field_70161_v + (double)this.getFacing().func_82599_e(), 10.0F, 33.0F);
      }

      if (this.digTarget != null && this.getCharge() >= this.digCost && !this.field_70170_p.field_72995_K) {
         this.func_70671_ap().func_75650_a((double)this.digTarget.func_177958_n() + 0.5D, (double)this.digTarget.func_177956_o(), (double)this.digTarget.func_177952_p() + 0.5D, 10.0F, 90.0F);
         if (this.digDelay-- <= 0 && this.dig()) {
            this.setCharge((float)((byte)((int)(this.getCharge() - this.digCost))));
            if (this.soundDelay < System.currentTimeMillis()) {
               this.soundDelay = System.currentTimeMillis() + 1200L + (long)this.field_70170_p.field_73012_v.nextInt(100);
               this.func_184185_a(SoundsTC.rumble, 0.25F, 0.9F + this.field_70170_p.field_73012_v.nextFloat() * 0.2F);
            }
         }
      }

      if (!this.field_70170_p.field_72995_K && this.digTarget == null && this.isActive() && this.validInventory()) {
         this.findNextBlockToDig();
         if (this.digTarget != null) {
            this.field_70170_p.func_72960_a(this, (byte)16);
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBoreDig(this.digTarget, this, this.digDelayMax), new TargetPoint(this.field_70170_p.field_73011_w.getDimension(), (double)this.digTarget.func_177958_n(), (double)this.digTarget.func_177956_o(), (double)this.digTarget.func_177952_p(), 32.0D));
         } else {
            this.field_70170_p.func_72960_a(this, (byte)17);
            this.func_70671_ap().func_75650_a(this.field_70165_t + (double)(this.getFacing().func_82601_c() * 2), this.field_70163_u + (double)(this.getFacing().func_96559_d() * 2) + (double)this.func_70047_e(), this.field_70161_v + (double)(this.getFacing().func_82599_e() * 2), 10.0F, 33.0F);
         }
      }

   }

   public boolean validInventory() {
      boolean b = this.func_184614_ca() != null && this.func_184614_ca().func_77973_b() instanceof ItemPickaxe;
      if (b && this.func_184614_ca().func_77952_i() + 1 >= this.func_184614_ca().func_77958_k()) {
         b = false;
      }

      return b;
   }

   public int getDigRadius() {
      int r = 0;
      if (this.func_184614_ca() != null && this.func_184614_ca().func_77973_b() instanceof ItemPickaxe) {
         ItemPickaxe pa = (ItemPickaxe)this.func_184614_ca().func_77973_b();
         r = pa.func_150913_i().func_77995_e() / 3;
         r += EnumInfusionEnchantment.getInfusionEnchantmentLevel(this.func_184614_ca(), EnumInfusionEnchantment.DESTRUCTIVE) * 2;
      }

      return r <= 1 ? 2 : r;
   }

   public int getDigDepth() {
      int r = this.getDigRadius() * 8;
      r += EnumInfusionEnchantment.getInfusionEnchantmentLevel(this.func_184614_ca(), EnumInfusionEnchantment.BURROWING) * 16;
      return r;
   }

   public int getFortune() {
      int r = 0;
      if (this.validInventory()) {
         r = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, this.func_184614_ca());
         r += EnumInfusionEnchantment.getInfusionEnchantmentLevel(this.func_184614_ca(), EnumInfusionEnchantment.SOUNDING);
      }

      return r;
   }

   public int getDigSpeed() {
      int speed = 0;
      if (this.validInventory()) {
         ItemPickaxe pa = (ItemPickaxe)this.func_184614_ca().func_77973_b();
         speed = (int)((float)speed + pa.func_150913_i().func_77998_b() / 2.0F);
         speed += EnchantmentHelper.func_77506_a(Enchantments.field_185305_q, this.func_184614_ca());
      }

      return speed;
   }

   public int getRefining() {
      int refining = 0;
      if (this.func_184614_ca() != null) {
         refining = EnumInfusionEnchantment.getInfusionEnchantmentLevel(this.func_184614_ca(), EnumInfusionEnchantment.REFINING);
      }

      return refining;
   }

   public boolean hasSilkTouch() {
      return this.func_184614_ca() != null && EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, this.func_184614_ca()) > 0;
   }

   private boolean canSilkTouch(BlockPos pos, IBlockState state) {
      return this.hasSilkTouch() && state.func_177230_c().canSilkHarvest(this.field_70170_p, pos, state, (EntityPlayer)null);
   }

   private boolean dig() {
      boolean b = false;
      if (this.digTarget != null && !this.field_70170_p.func_175623_d(this.digTarget)) {
         IBlockState digBs = this.field_70170_p.func_180495_p(this.digTarget);
         if (!digBs.func_177230_c().isAir(digBs, this.field_70170_p, this.digTarget)) {
            int tfortune = this.getFortune();
            boolean silktouch = false;
            if (this.canSilkTouch(this.digTarget, digBs)) {
               silktouch = true;
               tfortune = 0;
            }

            List<ItemStack> items = new ArrayList();
            if (silktouch) {
               ItemStack dropped = BlockUtils.getSilkTouchDrop(digBs);
               if (dropped != null) {
                  ((List)items).add(dropped);
               }
            } else {
               items = digBs.func_177230_c().getDrops(this.field_70170_p, this.digTarget, digBs, tfortune);
            }

            List<EntityItem> targets = this.field_70170_p.func_72872_a(EntityItem.class, (new AxisAlignedBB((double)this.digTarget.func_177958_n(), (double)this.digTarget.func_177956_o(), (double)this.digTarget.func_177952_p(), (double)(this.digTarget.func_177958_n() + 1), (double)(this.digTarget.func_177956_o() + 1), (double)(this.digTarget.func_177952_p() + 1))).func_72314_b(1.0D, 1.0D, 1.0D));
            if (targets.size() > 0) {
               Iterator var7 = targets.iterator();

               while(var7.hasNext()) {
                  EntityItem e = (EntityItem)var7.next();
                  ((List)items).add(e.func_92059_d().func_77946_l());
                  e.func_70106_y();
               }
            }

            int refining = this.getRefining();
            if (((List)items).size() > 0) {
               Iterator var19 = ((List)items).iterator();

               label86:
               while(true) {
                  ItemStack dropped;
                  do {
                     if (!var19.hasNext()) {
                        break label86;
                     }

                     ItemStack is = (ItemStack)var19.next();
                     dropped = is.func_77946_l();
                     if (!silktouch && refining > 0) {
                        dropped = Utils.findSpecialMiningResult(is, (float)(refining + 1) * 0.125F, this.field_70170_p.field_73012_v);
                     }
                  } while(dropped == null);

                  if (this.lastInventoryFace == null) {
                     EnumFacing[] var20 = EnumFacing.field_82609_l;
                     int var22 = var20.length;

                     for(int var13 = 0; var13 < var22; ++var13) {
                        EnumFacing f = var20[var13];
                        BlockPos p = this.func_180425_c().func_177972_a(f);
                        TileEntity inventory = this.field_70170_p.func_175625_s(p);
                        if (inventory != null && inventory instanceof IInventory) {
                           dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, f.func_176734_d(), true);
                           if (dropped == null) {
                              this.lastInventoryFace = f;
                              break;
                           }
                        }
                     }
                  } else {
                     BlockPos p = this.func_180425_c().func_177972_a(this.lastInventoryFace);
                     TileEntity inventory = this.field_70170_p.func_175625_s(p);
                     if (inventory != null && inventory instanceof IInventory) {
                        dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, this.lastInventoryFace.func_176734_d(), true);
                        if (dropped != null) {
                           this.lastInventoryFace = null;
                        }
                     } else {
                        this.lastInventoryFace = null;
                     }
                  }

                  if (dropped != null) {
                     EntityItem ei = new EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v, dropped.func_77946_l());
                     ei.field_70159_w = 0.0D;
                     ei.field_70181_x = 0.0D;
                     ei.field_70179_y = 0.0D;
                     this.field_70170_p.func_72838_d(ei);
                  }
               }
            }
         }

         if (this.func_184614_ca() != null) {
            ++this.breakCounter;
            if (this.breakCounter >= 50) {
               this.breakCounter = 0;
               this.func_184614_ca().func_77972_a(1, this);
            }

            if (this.func_184614_ca().field_77994_a <= 0) {
               this.func_184611_a(this.func_184600_cs(), (ItemStack)null);
            }
         }

         b = this.field_70170_p.func_175698_g(this.digTarget);
      }

      this.digTarget = null;
      return b;
   }

   private void findNextBlockToDig() {
      if (this.digTargetPrev == null || this.func_174831_c(this.digTargetPrev) > (double)((this.getDigRadius() + 1) * (this.getDigRadius() + 1))) {
         this.digTargetPrev = new BlockPos(this);
      }

      if (this.radInc == 0.0F) {
         this.radInc = 1.0F;
      }

      int digRadius = this.getDigRadius();
      int digDepth = this.getDigDepth();
      int x = this.digTargetPrev.func_177958_n();
      int z = this.digTargetPrev.func_177952_p();
      int y = this.digTargetPrev.func_177956_o();
      int x2 = x + this.getFacing().func_82601_c() * digDepth;
      int y2 = y + this.getFacing().func_96559_d() * digDepth;
      int z2 = z + this.getFacing().func_82599_e() * digDepth;
      BlockPos end = new BlockPos(x2, y2, z2);
      RayTraceResult mop = this.field_70170_p.func_147447_a((new Vec3d(this.digTargetPrev)).func_72441_c(0.5D, 0.5D, 0.5D), (new Vec3d(end)).func_72441_c(0.5D, 0.5D, 0.5D), false, true, false);
      Vec3d vsource;
      if (mop != null) {
         vsource = new Vec3d(this.field_70165_t + (double)this.getFacing().func_82601_c(), this.field_70163_u + (double)this.func_70047_e() + (double)this.getFacing().func_96559_d(), this.field_70161_v + (double)this.getFacing().func_82599_e());
         mop = this.field_70170_p.func_147447_a(vsource, (new Vec3d(mop.func_178782_a())).func_72441_c(0.5D, 0.5D, 0.5D), false, true, false);
         if (mop != null) {
            IBlockState blockState = this.field_70170_p.func_180495_p(mop.func_178782_a());
            if (blockState.func_185887_b(this.field_70170_p, mop.func_178782_a()) > -1.0F && blockState.func_185890_d(this.field_70170_p, mop.func_178782_a()) != null) {
               this.digDelay = Math.max(10 - this.getDigSpeed(), (int)(blockState.func_185887_b(this.field_70170_p, mop.func_178782_a()) * 2.0F) - this.getDigSpeed() * 2);
               if (this.digDelay < 1) {
                  this.digDelay = 1;
               }

               this.digDelayMax = this.digDelay;
               if (!mop.func_178782_a().equals(this.func_180425_c()) && !mop.func_178782_a().equals(this.func_180425_c().func_177977_b())) {
                  this.digTarget = mop.func_178782_a();
                  return;
               }
            }
         }
      }

      while(x == this.digTargetPrev.func_177958_n() && z == this.digTargetPrev.func_177952_p() && y == this.digTargetPrev.func_177956_o()) {
         if (Math.abs(this.currentRadius) > (float)digRadius) {
            this.currentRadius = (float)digRadius;
         }

         this.spiral = (int)((float)this.spiral + 5.0F + (10.0F - Math.abs(this.currentRadius)) * 2.0F);
         if (this.spiral >= 360) {
            this.spiral -= 360;
            this.currentRadius += this.radInc;
            if (this.currentRadius > (float)digRadius || this.currentRadius < (float)(-digRadius)) {
               this.currentRadius = 0.0F;
            }
         }

         vsource = new Vec3d(this.field_70165_t + (double)this.getFacing().func_82601_c(), this.field_70163_u + (double)this.getFacing().func_96559_d() + (double)this.func_70047_e(), this.field_70161_v + (double)this.getFacing().func_82599_e());
         Vec3d vtar = new Vec3d(0.0D, (double)this.currentRadius, 0.0D);
         vtar = Utils.rotateAroundZ(vtar, (float)this.spiral / 180.0F * 3.1415927F);
         vtar = Utils.rotateAroundY(vtar, 1.5707964F * (float)this.getFacing().func_82601_c());
         vtar = Utils.rotateAroundX(vtar, 1.5707964F * (float)this.getFacing().func_96559_d());
         Vec3d vres = vsource.func_72441_c(vtar.field_72450_a, vtar.field_72448_b, vtar.field_72449_c);
         x = MathHelper.func_76128_c(vres.field_72450_a);
         y = MathHelper.func_76128_c(vres.field_72448_b);
         z = MathHelper.func_76128_c(vres.field_72449_c);
      }

      this.digTargetPrev = new BlockPos(x, y, z);
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      try {
         if (source.func_76346_g() != null && this.isOwner((EntityLivingBase)source.func_76346_g())) {
            EnumFacing f = BlockPistonBase.func_185647_a(this.func_180425_c(), (EntityLivingBase)source.func_76346_g());
            if (f != EnumFacing.DOWN) {
               this.setFacing(f);
            }

            return false;
         }
      } catch (Exception var4) {
      }

      this.field_70177_z = (float)((double)this.field_70177_z + this.func_70681_au().nextGaussian() * 45.0D);
      this.field_70125_A = (float)((double)this.field_70125_A + this.func_70681_au().nextGaussian() * 20.0D);
      return super.func_70097_a(source, amount);
   }

   protected void rechargeVis() {
      this.setCharge(this.getCharge() + AuraHandler.drainVis(this.field_70170_p, this.func_180425_c(), 10.0F, false));
   }

   public boolean func_70104_M() {
      return true;
   }

   public boolean func_70067_L() {
      return true;
   }

   public void func_70645_a(DamageSource cause) {
      super.func_70645_a(cause);
      if (!this.field_70170_p.field_72995_K) {
         this.dropStuff();
      }

   }

   protected void dropStuff() {
      if (this.func_184614_ca() != null) {
         this.func_70099_a(this.func_184614_ca(), 0.5F);
      }

   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof ItemNameTag) {
         return false;
      } else if (!this.field_70170_p.field_72995_K && this.isOwner(player) && !this.field_70128_L) {
         if (player.func_70093_af()) {
            this.func_184185_a(SoundsTC.zap, 1.0F, 1.0F);
            this.dropStuff();
            this.func_70099_a(new ItemStack(ItemsTC.turretPlacer, 1, 2), 0.5F);
            this.func_70106_y();
            player.func_184609_a(hand);
         } else {
            player.openGui(Thaumcraft.instance, 14, this.field_70170_p, this.func_145782_y(), 0, 0);
         }

         return true;
      } else {
         return super.func_184645_a(player, hand, stack);
      }
   }

   public void func_70653_a(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
      super.func_70653_a(p_70653_1_, p_70653_2_, p_70653_3_ / 10.0D, p_70653_5_ / 10.0D);
      if (this.field_70181_x > 0.1D) {
         this.field_70181_x = 0.1D;
      }

   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(FACING, EnumFacing.DOWN);
      this.field_70180_af.func_187214_a(ACTIVE, false);
   }

   @SideOnly(Side.CLIENT)
   public boolean isActive() {
      return (Boolean)this.field_70180_af.func_187225_a(ACTIVE);
   }

   public void setActive(boolean attacking) {
      this.field_70180_af.func_187227_b(ACTIVE, attacking);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setCharge(nbt.func_74760_g("charge"));
      this.setFacing(EnumFacing.field_82609_l[nbt.func_74771_c("faceing")]);
      this.setActive(nbt.func_74767_n("active"));
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74776_a("charge", this.getCharge());
      nbt.func_74774_a("faceing", (byte)this.getFacing().func_176745_a());
      nbt.func_74757_a("active", this.isActive());
   }

   public EnumFacing getFacing() {
      return (EnumFacing)this.func_184212_Q().func_187225_a(FACING);
   }

   public void setFacing(EnumFacing face) {
      this.func_184212_Q().func_187227_b(FACING, face);
   }

   public float getCharge() {
      return this.charge;
   }

   public void setCharge(float c) {
      this.charge = c;
   }

   public void func_70091_d(double x, double y, double z) {
      super.func_70091_d(x / 5.0D, y, z / 5.0D);
   }

   protected void func_70076_C() {
      this.func_70097_a(DamageSource.field_76380_i, 400.0F);
   }

   protected void func_70628_a(boolean p_70628_1_, int treasure) {
      float b = (float)treasure * 0.15F;
      if (this.field_70146_Z.nextFloat() < 0.2F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.mind), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.2F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.morphicResonator), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.2F + b) {
         this.func_70099_a(new ItemStack(BlocksTC.crystalAir), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.2F + b) {
         this.func_70099_a(new ItemStack(BlocksTC.crystalEarth), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.5F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.gear), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.5F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.plate), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.5F + b) {
         this.func_70099_a(new ItemStack(BlocksTC.plank), 0.5F);
      }

   }

   public int func_70646_bf() {
      return 10;
   }

   public Team func_96124_cp() {
      if (this.isOwned()) {
         EntityLivingBase entitylivingbase = this.getOwnerEntity();
         if (entitylivingbase != null) {
            return entitylivingbase.func_96124_cp();
         }
      }

      return super.func_96124_cp();
   }

   public float func_70047_e() {
      return 0.8125F;
   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte par1) {
      if (par1 == 16) {
         this.clientDigging = true;
      } else if (par1 == 17) {
         this.clientDigging = false;
      } else {
         super.func_70103_a(par1);
      }

   }

   static {
      FACING = EntityDataManager.func_187226_a(EntityArcaneBore.class, DataSerializers.field_187202_l);
      ACTIVE = EntityDataManager.func_187226_a(EntityArcaneBore.class, DataSerializers.field_187198_h);
   }
}
