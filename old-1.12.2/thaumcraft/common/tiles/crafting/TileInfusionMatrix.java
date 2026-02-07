package thaumcraft.common.tiles.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.blocks.basic.BlockStoneTC;
import thaumcraft.common.blocks.devices.BlockPedestal;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSource;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileInfusionMatrix extends TileThaumcraft implements IInteractWithCaster, IAspectContainer, ITickable {
   private ArrayList<BlockPos> pedestals = new ArrayList();
   private int dangerCount = 0;
   public boolean active = false;
   public boolean crafting = false;
   public boolean checkSurroundings = true;
   public int symmetryInstability = 0;
   public float costMult = 0.0F;
   public int instability = 0;
   private int cycleTime = 20;
   private AspectList recipeEssentia = new AspectList();
   private ArrayList<ItemStack> recipeIngredients = null;
   private Object recipeOutput = null;
   private String recipePlayer = null;
   private String recipeOutputLabel = null;
   private ItemStack recipeInput = null;
   private int recipeInstability = 0;
   private int recipeXP = 0;
   private int recipeType = 0;
   public HashMap<String, TileInfusionMatrix.SourceFX> sourceFX = new HashMap();
   public int count = 0;
   public int craftCount = 0;
   public float startUp;
   private int countDelay;
   ArrayList<ItemStack> ingredients;
   int itemCount;

   public TileInfusionMatrix() {
      this.countDelay = this.cycleTime / 2;
      this.ingredients = new ArrayList();
      this.itemCount = 0;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n() - 0.1D, (double)this.func_174877_v().func_177956_o() - 0.1D, (double)this.func_174877_v().func_177952_p() - 0.1D, (double)this.func_174877_v().func_177958_n() + 1.1D, (double)this.func_174877_v().func_177956_o() + 1.1D, (double)this.func_174877_v().func_177952_p() + 1.1D);
   }

   public void readSyncNBT(NBTTagCompound nbtCompound) {
      this.active = nbtCompound.func_74767_n("active");
      this.crafting = nbtCompound.func_74767_n("crafting");
      this.instability = nbtCompound.func_74765_d("instability");
      this.recipeEssentia.readFromNBT(nbtCompound);
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbtCompound) {
      nbtCompound.func_74757_a("active", this.active);
      nbtCompound.func_74757_a("crafting", this.crafting);
      nbtCompound.func_74777_a("instability", (short)this.instability);
      this.recipeEssentia.writeToNBT(nbtCompound);
      return nbtCompound;
   }

   public void func_145839_a(NBTTagCompound nbtCompound) {
      super.func_145839_a(nbtCompound);
      NBTTagList nbttaglist = nbtCompound.func_150295_c("recipein", 10);
      this.recipeIngredients = new ArrayList();

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         this.recipeIngredients.add(ItemStack.func_77949_a(nbttagcompound1));
      }

      String rot = nbtCompound.func_74779_i("rotype");
      if (rot != null && rot.equals("@")) {
         this.recipeOutput = ItemStack.func_77949_a(nbtCompound.func_74775_l("recipeout"));
      } else if (rot != null) {
         this.recipeOutputLabel = rot;
         this.recipeOutput = nbtCompound.func_74781_a("recipeout");
      }

      this.recipeInput = ItemStack.func_77949_a(nbtCompound.func_74775_l("recipeinput"));
      this.recipeInstability = nbtCompound.func_74762_e("recipeinst");
      this.recipeType = nbtCompound.func_74762_e("recipetype");
      this.recipeXP = nbtCompound.func_74762_e("recipexp");
      this.recipePlayer = nbtCompound.func_74779_i("recipeplayer");
      if (this.recipePlayer.isEmpty()) {
         this.recipePlayer = null;
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtCompound) {
      super.func_189515_b(nbtCompound);
      if (this.recipeIngredients != null && this.recipeIngredients.size() > 0) {
         NBTTagList nbttaglist = new NBTTagList();
         Iterator var3 = this.recipeIngredients.iterator();

         while(var3.hasNext()) {
            ItemStack stack = (ItemStack)var3.next();
            if (stack != null) {
               NBTTagCompound nbttagcompound1 = new NBTTagCompound();
               nbttagcompound1.func_74774_a("item", (byte)this.count);
               stack.func_77955_b(nbttagcompound1);
               nbttaglist.func_74742_a(nbttagcompound1);
               ++this.count;
            }
         }

         nbtCompound.func_74782_a("recipein", nbttaglist);
      }

      if (this.recipeOutput != null && this.recipeOutput instanceof ItemStack) {
         nbtCompound.func_74778_a("rotype", "@");
      }

      if (this.recipeOutput != null && this.recipeOutput instanceof NBTBase) {
         nbtCompound.func_74778_a("rotype", this.recipeOutputLabel);
      }

      if (this.recipeOutput != null && this.recipeOutput instanceof ItemStack) {
         nbtCompound.func_74782_a("recipeout", ((ItemStack)this.recipeOutput).func_77955_b(new NBTTagCompound()));
      }

      if (this.recipeOutput != null && this.recipeOutput instanceof NBTBase) {
         nbtCompound.func_74782_a("recipeout", (NBTBase)this.recipeOutput);
      }

      if (this.recipeInput != null) {
         nbtCompound.func_74782_a("recipeinput", this.recipeInput.func_77955_b(new NBTTagCompound()));
      }

      nbtCompound.func_74768_a("recipeinst", this.recipeInstability);
      nbtCompound.func_74768_a("recipetype", this.recipeType);
      nbtCompound.func_74768_a("recipexp", this.recipeXP);
      if (this.recipePlayer == null) {
         nbtCompound.func_74778_a("recipeplayer", "");
      } else {
         nbtCompound.func_74778_a("recipeplayer", this.recipePlayer);
      }

      return nbtCompound;
   }

   public void func_73660_a() {
      ++this.count;
      if (this.checkSurroundings) {
         this.checkSurroundings = false;
         this.getSurroundings();
      }

      if (this.field_145850_b.field_72995_K) {
         this.doEffects();
      } else {
         if (this.count % (this.crafting ? 20 : 100) == 0 && !this.validLocation()) {
            this.active = false;
            this.func_70296_d();
            this.syncTile(false);
            return;
         }

         if (this.active && this.crafting && this.count % this.countDelay == 0) {
            this.craftCycle();
            this.func_70296_d();
         }
      }

   }

   public boolean validLocation() {
      if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(0, -2, 0)).func_177230_c() != BlocksTC.pedestal) {
         return false;
      } else if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, 1)).func_177230_c() != BlocksTC.pillar) {
         return false;
      } else if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, 1)).func_177230_c() != BlocksTC.pillar) {
         return false;
      } else if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, -1)).func_177230_c() != BlocksTC.pillar) {
         return false;
      } else {
         return this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, -1)).func_177230_c() == BlocksTC.pillar;
      }
   }

   public void craftingStart(EntityPlayer player) {
      if (!this.validLocation()) {
         this.active = false;
         this.func_70296_d();
         this.syncTile(false);
      } else {
         this.getSurroundings();
         TileEntity te = null;
         this.recipeInput = null;
         te = this.field_145850_b.func_175625_s(this.field_174879_c.func_177979_c(2));
         if (te != null && te instanceof TilePedestal) {
            TilePedestal ped = (TilePedestal)te;
            if (ped.func_70301_a(0) != null) {
               this.recipeInput = ped.func_70301_a(0).func_77946_l();
            }
         }

         if (this.recipeInput != null) {
            ArrayList<ItemStack> components = new ArrayList();
            Iterator var4 = this.pedestals.iterator();

            while(var4.hasNext()) {
               BlockPos cc = (BlockPos)var4.next();
               te = this.field_145850_b.func_175625_s(cc);
               if (te != null && te instanceof TilePedestal) {
                  TilePedestal ped = (TilePedestal)te;
                  if (ped.func_70301_a(0) != null) {
                     components.add(ped.func_70301_a(0).func_77946_l());
                  }
               }
            }

            if (components.size() != 0) {
               InfusionRecipe recipe = ThaumcraftCraftingManager.findMatchingInfusionRecipe(components, this.recipeInput, player);
               if ((double)this.costMult < 0.5D) {
                  this.costMult = 0.5F;
               }

               if (recipe != null) {
                  this.recipeType = 0;
                  this.recipeIngredients = components;
                  if (recipe.getRecipeOutput(player, this.recipeInput, components) instanceof Object[]) {
                     Object[] obj = (Object[])((Object[])recipe.getRecipeOutput(player, this.recipeInput, components));
                     this.recipeOutputLabel = (String)obj[0];
                     this.recipeOutput = (NBTBase)obj[1];
                  } else {
                     this.recipeOutput = recipe.getRecipeOutput(player, this.recipeInput, components);
                  }

                  this.recipeInstability = recipe.getInstability(player, this.recipeInput, components);
                  AspectList al = recipe.getAspects(player, this.recipeInput, components);
                  AspectList al2 = new AspectList();
                  Aspect[] var7 = al.getAspects();
                  int var8 = var7.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     Aspect as = var7[var9];
                     if ((int)((float)al.getAmount(as) * this.costMult) > 0) {
                        al2.add(as, (int)((float)al.getAmount(as) * this.costMult));
                     }
                  }

                  this.recipeEssentia = al2;
                  this.recipePlayer = player.func_70005_c_();
                  this.instability = this.symmetryInstability + this.recipeInstability;
                  this.crafting = true;
                  this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.craftstart, SoundCategory.BLOCKS, 0.5F, 1.0F);
                  this.syncTile(false);
                  this.func_70296_d();
               }
            }
         }
      }
   }

   public void craftCycle() {
      boolean valid = false;
      TileEntity te = this.field_145850_b.func_175625_s(this.field_174879_c.func_177979_c(2));
      if (te != null && te instanceof TilePedestal) {
         TilePedestal ped = (TilePedestal)te;
         if (ped.func_70301_a(0) != null) {
            ItemStack i2 = ped.func_70301_a(0).func_77946_l();
            if (this.recipeInput.func_77952_i() == 32767) {
               i2.func_77964_b(32767);
            }

            if (ThaumcraftApiHelper.areItemStacksEqualForCrafting(i2, this.recipeInput)) {
               valid = true;
            }
         }
      }

      if (!valid || this.instability > 0 && this.field_145850_b.field_73012_v.nextInt(500) <= this.instability) {
         switch(this.field_145850_b.field_73012_v.nextInt(21)) {
         case 0:
         case 2:
         case 10:
         case 13:
            this.inEvEjectItem(0);
            break;
         case 1:
         case 11:
            this.inEvEjectItem(2);
            break;
         case 3:
         case 8:
         case 14:
            this.inEvZap(false);
            break;
         case 4:
         case 15:
            this.inEvEjectItem(5);
            break;
         case 5:
         case 16:
            this.inEvHarm(false);
            break;
         case 6:
         case 17:
            this.inEvEjectItem(1);
            break;
         case 7:
            this.inEvEjectItem(4);
            break;
         case 9:
            this.field_145850_b.func_72876_a((Entity)null, (double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, 1.5F + this.field_145850_b.field_73012_v.nextFloat(), false);
            break;
         case 12:
            this.inEvZap(true);
            break;
         case 18:
            this.inEvHarm(true);
            break;
         case 19:
            this.inEvEjectItem(3);
            break;
         case 20:
            this.inEvWarp();
         }

         if (valid) {
            return;
         }
      }

      if (!valid) {
         this.instability = 0;
         this.crafting = false;
         this.recipeEssentia = new AspectList();
         this.syncTile(false);
         this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.craftfail, SoundCategory.BLOCKS, 1.0F, 0.6F);
         this.func_70296_d();
      } else {
         Iterator var11;
         Aspect[] ingEss;
         Aspect as;
         if (this.recipeType == 1 && this.recipeXP > 0) {
            List<EntityPlayer> targets = this.field_145850_b.func_72872_a(EntityPlayer.class, (new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 1), (double)(this.func_174877_v().func_177952_p() + 1))).func_72314_b(10.0D, 10.0D, 10.0D));
            if (targets != null && targets.size() > 0) {
               var11 = targets.iterator();

               EntityPlayer target;
               do {
                  if (!var11.hasNext()) {
                     ingEss = this.recipeEssentia.getAspects();
                     if (ingEss != null && ingEss.length > 0 && this.field_145850_b.field_73012_v.nextInt(3) == 0) {
                        as = ingEss[this.field_145850_b.field_73012_v.nextInt(ingEss.length)];
                        this.recipeEssentia.add(as, 1);
                        if (this.field_145850_b.field_73012_v.nextInt(50 - this.recipeInstability * 2) == 0) {
                           ++this.instability;
                        }

                        if (this.instability > 25) {
                           this.instability = 25;
                        }

                        this.syncTile(false);
                        this.func_70296_d();
                     }

                     return;
                  }

                  target = (EntityPlayer)var11.next();
               } while(!target.field_71075_bZ.field_75098_d && target.field_71068_ca <= 0);

               if (!target.field_71075_bZ.field_75098_d) {
                  target.func_71013_b(1);
               }

               --this.recipeXP;
               target.func_70097_a(DamageSource.field_76376_m, (float)this.field_145850_b.field_73012_v.nextInt(2));
               PacketHandler.INSTANCE.sendToAllAround(new PacketFXInfusionSource(this.field_174879_c, (byte)0, (byte)0, (byte)0, target.func_145782_y()), new TargetPoint(this.func_145831_w().field_73011_w.getDimension(), (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), 32.0D));
               target.func_184185_a(SoundEvents.field_187659_cY, 1.0F, 2.0F + this.field_145850_b.field_73012_v.nextFloat() * 0.4F);
               this.countDelay = this.cycleTime;
            }
         } else {
            if (this.recipeType == 1 && this.recipeXP == 0) {
               this.countDelay = this.cycleTime / 2;
            }

            if (this.countDelay < 1) {
               this.countDelay = 1;
            }

            if (this.recipeEssentia.visSize() > 0) {
               Aspect[] var9 = this.recipeEssentia.getAspects();
               int var13 = var9.length;

               for(int var15 = 0; var15 < var13; ++var15) {
                  Aspect aspect = var9[var15];
                  int na = this.recipeEssentia.getAmount(aspect);
                  if (na > 0) {
                     if (EssentiaHandler.drainEssentia(this, aspect, (EnumFacing)null, 12, na > 1 ? this.countDelay : 0)) {
                        this.recipeEssentia.reduce(aspect, 1);
                        this.syncTile(false);
                        this.func_70296_d();
                        return;
                     }

                     if (this.field_145850_b.field_73012_v.nextInt(100 - this.recipeInstability * 3) == 0) {
                        ++this.instability;
                     }

                     if (this.instability > 25) {
                        this.instability = 25;
                     }

                     this.syncTile(false);
                     this.func_70296_d();
                  }
               }

               this.checkSurroundings = true;
            } else if (this.recipeIngredients.size() <= 0) {
               this.instability = 0;
               this.crafting = false;
               this.craftingFinish(this.recipeOutput, this.recipeOutputLabel);
               this.recipeOutput = null;
               this.syncTile(false);
               this.func_70296_d();
            } else {
               for(int a = 0; a < this.recipeIngredients.size(); ++a) {
                  var11 = this.pedestals.iterator();

                  while(var11.hasNext()) {
                     BlockPos cc = (BlockPos)var11.next();
                     te = this.field_145850_b.func_175625_s(cc);
                     if (te != null && te instanceof TilePedestal && ((TilePedestal)te).func_70301_a(0) != null && ThaumcraftApiHelper.areItemStacksEqualForCrafting(((TilePedestal)te).func_70301_a(0), this.recipeIngredients.get(a))) {
                        if (this.itemCount == 0) {
                           this.itemCount = 5;
                           PacketHandler.INSTANCE.sendToAllAround(new PacketFXInfusionSource(this.field_174879_c, (byte)(this.field_174879_c.func_177958_n() - cc.func_177958_n()), (byte)(this.field_174879_c.func_177956_o() - cc.func_177956_o()), (byte)(this.field_174879_c.func_177952_p() - cc.func_177952_p()), 0), new TargetPoint(this.func_145831_w().field_73011_w.getDimension(), (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), 32.0D));
                        } else if (this.itemCount-- <= 1) {
                           ItemStack is = ((TilePedestal)te).func_70301_a(0).func_77973_b().getContainerItem(((TilePedestal)te).func_70301_a(0));
                           ((TilePedestal)te).func_70299_a(0, is == null ? null : is.func_77946_l());
                           ((TilePedestal)te).func_70296_d();
                           this.recipeIngredients.remove(a);
                           this.func_70296_d();
                        }

                        return;
                     }
                  }

                  ingEss = this.recipeEssentia.getAspects();
                  if (ingEss != null && ingEss.length > 0 && this.field_145850_b.field_73012_v.nextInt(1 + a) == 0) {
                     as = ingEss[this.field_145850_b.field_73012_v.nextInt(ingEss.length)];
                     this.recipeEssentia.add(as, 1);
                     if (this.field_145850_b.field_73012_v.nextInt(50 - this.recipeInstability * 2) == 0) {
                        ++this.instability;
                     }

                     if (this.instability > 25) {
                        this.instability = 25;
                     }

                     this.syncTile(false);
                     this.func_70296_d();
                  }
               }

            }
         }
      }
   }

   private void inEvZap(boolean all) {
      List<EntityLivingBase> targets = this.field_145850_b.func_72872_a(EntityLivingBase.class, (new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 1), (double)(this.func_174877_v().func_177952_p() + 1))).func_72314_b(10.0D, 10.0D, 10.0D));
      if (targets != null && targets.size() > 0) {
         Iterator var3 = targets.iterator();

         while(var3.hasNext()) {
            EntityLivingBase target = (EntityLivingBase)var3.next();
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(this.field_174879_c, target, 0.3F - this.field_145850_b.field_73012_v.nextFloat() * 0.1F, 0.0F, 0.3F - this.field_145850_b.field_73012_v.nextFloat() * 0.1F), new TargetPoint(this.field_145850_b.field_73011_w.getDimension(), (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), 32.0D));
            target.func_70097_a(DamageSource.field_76376_m, (float)(4 + this.field_145850_b.field_73012_v.nextInt(4)));
            if (!all) {
               break;
            }
         }
      }

   }

   private void inEvHarm(boolean all) {
      List<EntityLivingBase> targets = this.field_145850_b.func_72872_a(EntityLivingBase.class, (new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 1), (double)(this.func_174877_v().func_177952_p() + 1))).func_72314_b(10.0D, 10.0D, 10.0D));
      if (targets != null && targets.size() > 0) {
         Iterator var3 = targets.iterator();

         while(var3.hasNext()) {
            EntityLivingBase target = (EntityLivingBase)var3.next();
            if (this.field_145850_b.field_73012_v.nextBoolean()) {
               target.func_70690_d(new PotionEffect(PotionFluxTaint.instance, 120, 0, false, true));
            } else {
               PotionEffect pe = new PotionEffect(PotionVisExhaust.instance, 2400, 0, true, true);
               pe.getCurativeItems().clear();
               target.func_70690_d(pe);
            }

            if (!all) {
               break;
            }
         }
      }

   }

   private void inEvWarp() {
      List<EntityPlayer> targets = this.field_145850_b.func_72872_a(EntityPlayer.class, (new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 1), (double)(this.func_174877_v().func_177952_p() + 1))).func_72314_b(10.0D, 10.0D, 10.0D));
      if (targets != null && targets.size() > 0) {
         EntityPlayer target = (EntityPlayer)targets.get(this.field_145850_b.field_73012_v.nextInt(targets.size()));
         if (this.field_145850_b.field_73012_v.nextFloat() < 0.25F) {
            ThaumcraftApi.internalMethods.addWarpToPlayer(target, 1, IPlayerWarp.EnumWarpType.NORMAL);
         } else {
            ThaumcraftApi.internalMethods.addWarpToPlayer(target, 2 + this.field_145850_b.field_73012_v.nextInt(4), IPlayerWarp.EnumWarpType.TEMPORARY);
         }
      }

   }

   private void inEvEjectItem(int type) {
      for(int q = 0; q < 50 && this.pedestals.size() > 0; ++q) {
         BlockPos cc = (BlockPos)this.pedestals.get(this.field_145850_b.field_73012_v.nextInt(this.pedestals.size()));
         TileEntity te = this.field_145850_b.func_175625_s(cc);
         if (te != null && te instanceof TilePedestal && ((TilePedestal)te).func_70301_a(0) != null) {
            if (type >= 3 && type != 5) {
               ((TilePedestal)te).func_70299_a(0, (ItemStack)null);
            } else {
               InventoryUtils.dropItems(this.field_145850_b, cc);
            }

            if (type != 1 && type != 3) {
               if (type != 2 && type != 4) {
                  if (type == 5) {
                     this.field_145850_b.func_72876_a((Entity)null, (double)((float)cc.func_177958_n() + 0.5F), (double)((float)cc.func_177956_o() + 0.5F), (double)((float)cc.func_177952_p() + 0.5F), 1.0F, false);
                  }
               } else {
                  int a = 5 + this.field_145850_b.field_73012_v.nextInt(5);
                  AuraHelper.polluteAura(this.field_145850_b, cc, (float)a, true);
               }
            } else {
               this.field_145850_b.func_175656_a(cc.func_177984_a(), BlocksTC.fluxGoo.func_176223_P());
               this.field_145850_b.func_184133_a((EntityPlayer)null, cc, SoundEvents.field_187615_H, SoundCategory.BLOCKS, 0.3F, 1.0F);
            }

            this.field_145850_b.func_175641_c(cc, BlocksTC.pedestal, 11, 0);
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(this.field_174879_c, cc.func_177984_a(), 0.3F - this.field_145850_b.field_73012_v.nextFloat() * 0.1F, 0.0F, 0.3F - this.field_145850_b.field_73012_v.nextFloat() * 0.1F), new TargetPoint(this.field_145850_b.field_73011_w.getDimension(), (double)cc.func_177958_n(), (double)cc.func_177956_o(), (double)cc.func_177952_p(), 32.0D));
            return;
         }
      }

   }

   public void craftingFinish(Object out, String label) {
      TileEntity te = this.field_145850_b.func_175625_s(this.field_174879_c.func_177979_c(2));
      if (te != null && te instanceof TilePedestal) {
         if (out instanceof ItemStack) {
            ((TilePedestal)te).setInventorySlotContentsFromInfusion(0, ((ItemStack)out).func_77946_l());
         } else {
            ItemStack temp;
            if (out instanceof NBTBase) {
               temp = ((TilePedestal)te).func_70301_a(0);
               NBTBase tag = (NBTBase)out;
               temp.func_77983_a(label, tag);
               this.syncTile(false);
               te.func_70296_d();
            } else if (out instanceof Enchantment) {
               temp = ((TilePedestal)te).func_70301_a(0);
               Map enchantments = EnchantmentHelper.func_82781_a(temp);
               enchantments.put((Enchantment)out, EnchantmentHelper.func_77506_a((Enchantment)out, temp) + 1);
               EnchantmentHelper.func_82782_a(enchantments, temp);
               this.syncTile(false);
               te.func_70296_d();
            }
         }

         if (this.recipePlayer != null) {
            EntityPlayer p = this.field_145850_b.func_72924_a(this.recipePlayer);
            if (p != null) {
               FMLCommonHandler.instance().firePlayerCraftingEvent(p, ((TilePedestal)te).func_70301_a(0), new InventoryFake(this.recipeIngredients));
            }
         }

         this.recipeEssentia = new AspectList();
         this.syncTile(false);
         this.func_70296_d();
         this.field_145850_b.func_175641_c(this.field_174879_c.func_177979_c(2), BlocksTC.pedestal, 12, 0);
         this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.wand, SoundCategory.BLOCKS, 0.5F, 1.0F);
      }

   }

   private void getSurroundings() {
      ArrayList<BlockPos> stuff = new ArrayList();
      this.pedestals.clear();

      try {
         int x;
         TileEntity te;
         for(int xx = -12; xx <= 12; ++xx) {
            for(int zz = -12; zz <= 12; ++zz) {
               boolean skip = false;

               for(int yy = -5; yy <= 10; ++yy) {
                  if (xx != 0 || zz != 0) {
                     int x = this.field_174879_c.func_177958_n() + xx;
                     int y = this.field_174879_c.func_177956_o() - yy;
                     x = this.field_174879_c.func_177952_p() + zz;
                     BlockPos bp = new BlockPos(x, y, x);
                     te = this.field_145850_b.func_175625_s(bp);
                     if (!skip && yy > 0 && Math.abs(xx) <= 8 && Math.abs(zz) <= 8 && te != null && te instanceof TilePedestal) {
                        this.pedestals.add(bp);
                        skip = true;
                     } else {
                        Block bi = this.field_145850_b.func_180495_p(bp).func_177230_c();

                        try {
                           if (bi != null && (bi == Blocks.field_150465_bP || bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(this.func_145831_w(), bp))) {
                              stuff.add(bp);
                           }
                        } catch (Exception var16) {
                        }
                     }
                  }
               }
            }
         }

         this.cycleTime = 10;
         this.symmetryInstability = 0;
         this.costMult = 1.0F;
         if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, -1)).func_177230_c() == BlocksTC.pillar && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, -1)).func_177230_c() == BlocksTC.pillar && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, 1)).func_177230_c() == BlocksTC.pillar && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, 1)).func_177230_c() == BlocksTC.pillar) {
            if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, -1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ANCIENT && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, -1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ANCIENT && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, 1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ANCIENT && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, 1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ANCIENT) {
               --this.cycleTime;
               this.costMult -= 0.1F;
               this.symmetryInstability += 2;
            }

            if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, -1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ELDRITCH && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, -1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ELDRITCH && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(1, -2, 1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ELDRITCH && this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(-1, -2, 1)).func_177229_b(BlockPillar.VARIANT) == BlockPillar.PillarType.ELDRITCH) {
               this.cycleTime -= 3;
               this.costMult += 0.05F;
               this.symmetryInstability -= 4;
            }
         }

         int[] xm = new int[]{-1, 1, 1, -1};
         int[] zm = new int[]{-1, -1, 1, 1};

         int apc;
         for(apc = 0; apc < 4; ++apc) {
            if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(xm[apc], -3, zm[apc])).func_177230_c() == BlocksTC.stone) {
               IBlockState state = this.field_145850_b.func_180495_p(this.field_174879_c.func_177982_a(xm[apc], -3, zm[apc]));
               Comparable c = (BlockStoneTC.StoneType)state.func_177229_b(BlockStoneTC.instance.TYPE);
               if (c == BlockStoneTC.StoneType.MATRIX_SPEED) {
                  --this.cycleTime;
                  this.costMult += 0.01F;
               }

               if (c == BlockStoneTC.StoneType.MATRIX_COST) {
                  ++this.cycleTime;
                  this.costMult -= 0.02F;
               }
            }
         }

         this.countDelay = this.cycleTime / 2;
         apc = 0;
         Iterator var22 = this.pedestals.iterator();

         int zz;
         int z;
         int xx;
         while(var22.hasNext()) {
            BlockPos cc = (BlockPos)var22.next();
            boolean items = false;
            x = this.field_174879_c.func_177958_n() - cc.func_177958_n();
            z = this.field_174879_c.func_177952_p() - cc.func_177952_p();
            te = this.field_145850_b.func_175625_s(cc);
            if (te != null && te instanceof TilePedestal) {
               this.symmetryInstability += 2;
               if (((TilePedestal)te).func_70301_a(0) != null) {
                  ++this.symmetryInstability;
                  if (BlockPedestal.instance.isType(this.field_145850_b.func_180495_p(cc), BlockPedestal.PedestalType.ELDRITCH)) {
                     ++this.symmetryInstability;
                  }

                  items = true;
               }

               if (BlockPedestal.instance.isType(this.field_145850_b.func_180495_p(cc), BlockPedestal.PedestalType.ELDRITCH)) {
                  this.costMult += 0.0025F;
               }

               if (BlockPedestal.instance.isType(this.field_145850_b.func_180495_p(cc), BlockPedestal.PedestalType.ANCIENT)) {
                  this.costMult -= 0.01F;
                  ++apc;
               }
            }

            xx = this.field_174879_c.func_177958_n() + x;
            zz = this.field_174879_c.func_177952_p() + z;
            BlockPos cc2 = new BlockPos(xx, cc.func_177956_o(), zz);
            te = this.field_145850_b.func_175625_s(cc2);
            if (te != null && te instanceof TilePedestal && this.field_145850_b.func_180495_p(cc2) == this.field_145850_b.func_180495_p(cc)) {
               this.symmetryInstability -= 2;
               if (((TilePedestal)te).func_70301_a(0) != null && items) {
                  --this.symmetryInstability;
                  if (BlockPedestal.instance.isType(this.field_145850_b.func_180495_p(cc2), BlockPedestal.PedestalType.ELDRITCH)) {
                     this.symmetryInstability -= 2;
                  }
               }
            }
         }

         this.symmetryInstability += apc / 4;
         float sym = 0.0F;
         Iterator var26 = stuff.iterator();

         while(var26.hasNext()) {
            BlockPos cc = (BlockPos)var26.next();
            x = this.field_174879_c.func_177958_n() - cc.func_177958_n();
            z = this.field_174879_c.func_177952_p() - cc.func_177952_p();
            Block bi = this.field_145850_b.func_180495_p(cc).func_177230_c();

            try {
               if (bi == Blocks.field_150465_bP || bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(this.func_145831_w(), cc)) {
                  sym += 0.1F;
               }
            } catch (Exception var14) {
            }

            xx = this.field_174879_c.func_177958_n() + x;
            zz = this.field_174879_c.func_177952_p() + z;
            bi = this.field_145850_b.func_180495_p(new BlockPos(xx, cc.func_177956_o(), zz)).func_177230_c();

            try {
               if (bi == Blocks.field_150465_bP || bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(this.func_145831_w(), cc)) {
                  sym -= 0.2F;
               }
            } catch (Exception var15) {
            }
         }

         this.symmetryInstability = (int)((float)this.symmetryInstability + sym);
      } catch (Exception var17) {
      }

   }

   public boolean onCasterRightClick(World world, ItemStack wandstack, EntityPlayer player, BlockPos pos, EnumFacing side, EnumHand hand) {
      if (!world.field_72995_K && this.active && !this.crafting) {
         this.craftingStart(player);
         return false;
      } else if (!world.field_72995_K && !this.active && this.validLocation()) {
         world.func_184133_a((EntityPlayer)null, pos, SoundsTC.craftstart, SoundCategory.BLOCKS, 0.5F, 1.0F);
         this.active = true;
         this.syncTile(false);
         this.func_70296_d();
         return false;
      } else {
         return false;
      }
   }

   private void doEffects() {
      if (this.crafting) {
         if (this.craftCount == 0) {
            this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundsTC.infuserstart, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
         } else if (this.craftCount == 0 || this.craftCount % 65 == 0) {
            this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundsTC.infuser, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
         }

         ++this.craftCount;
         FXDispatcher.INSTANCE.blockRunes((double)this.field_174879_c.func_177958_n(), (double)(this.field_174879_c.func_177956_o() - 2), (double)this.field_174879_c.func_177952_p(), 0.5F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F, 0.1F, 0.7F + this.field_145850_b.field_73012_v.nextFloat() * 0.3F, 25, -0.03F);
      } else if (this.craftCount > 0) {
         this.craftCount -= 2;
         if (this.craftCount < 0) {
            this.craftCount = 0;
         }

         if (this.craftCount > 50) {
            this.craftCount = 50;
         }
      }

      if (this.active && this.startUp != 1.0F) {
         if (this.startUp < 1.0F) {
            this.startUp += Math.max(this.startUp / 10.0F, 0.001F);
         }

         if ((double)this.startUp > 0.999D) {
            this.startUp = 1.0F;
         }
      }

      if (!this.active && this.startUp > 0.0F) {
         if (this.startUp > 0.0F) {
            this.startUp -= this.startUp / 10.0F;
         }

         if ((double)this.startUp < 0.001D) {
            this.startUp = 0.0F;
         }
      }

      String[] var1 = (String[])this.sourceFX.keySet().toArray(new String[0]);
      int var2 = var1.length;

      int yy;
      for(yy = 0; yy < var2; ++yy) {
         String fxk = var1[yy];
         TileInfusionMatrix.SourceFX fx = (TileInfusionMatrix.SourceFX)this.sourceFX.get(fxk);
         if (fx.ticks <= 0) {
            this.sourceFX.remove(fxk);
         } else {
            if (fx.loc.equals(this.field_174879_c)) {
               Entity player = this.field_145850_b.func_73045_a(fx.color);
               if (player != null) {
                  for(int a = 0; a < FXDispatcher.INSTANCE.particleCount(2); ++a) {
                     FXDispatcher.INSTANCE.drawInfusionParticles4(player.field_70165_t + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * player.field_70130_N), player.func_174813_aQ().field_72338_b + (double)(this.field_145850_b.field_73012_v.nextFloat() * player.field_70131_O), player.field_70161_v + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * player.field_70130_N), this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p());
                  }
               }
            } else {
               TileEntity tile = this.field_145850_b.func_175625_s(fx.loc);
               if (tile instanceof TilePedestal) {
                  ItemStack is = ((TilePedestal)tile).func_70301_a(0);
                  if (is != null) {
                     if (this.field_145850_b.field_73012_v.nextInt(3) == 0) {
                        FXDispatcher.INSTANCE.drawInfusionParticles3((double)((float)fx.loc.func_177958_n() + this.field_145850_b.field_73012_v.nextFloat()), (double)((float)fx.loc.func_177956_o() + this.field_145850_b.field_73012_v.nextFloat() + 1.0F), (double)((float)fx.loc.func_177952_p() + this.field_145850_b.field_73012_v.nextFloat()), this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p());
                     } else {
                        Item bi = is.func_77973_b();
                        int md = is.func_77952_i();
                        int a;
                        if (bi instanceof ItemBlock) {
                           for(a = 0; a < FXDispatcher.INSTANCE.particleCount(2); ++a) {
                              FXDispatcher.INSTANCE.drawInfusionParticles2((double)((float)fx.loc.func_177958_n() + this.field_145850_b.field_73012_v.nextFloat()), (double)((float)fx.loc.func_177956_o() + this.field_145850_b.field_73012_v.nextFloat() + 1.0F), (double)((float)fx.loc.func_177952_p() + this.field_145850_b.field_73012_v.nextFloat()), this.field_174879_c, Block.func_149634_a(bi).func_176223_P(), md);
                           }
                        } else {
                           for(a = 0; a < FXDispatcher.INSTANCE.particleCount(2); ++a) {
                              FXDispatcher.INSTANCE.drawInfusionParticles1((double)((float)fx.loc.func_177958_n() + 0.4F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F), (double)((float)fx.loc.func_177956_o() + 1.23F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F), (double)((float)fx.loc.func_177952_p() + 0.4F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F), this.field_174879_c, bi, md);
                           }
                        }
                     }
                  }
               } else {
                  fx.ticks = 0;
               }
            }

            --fx.ticks;
            this.sourceFX.put(fxk, fx);
         }
      }

      if (this.crafting && this.instability > 0 && this.field_145850_b.field_73012_v.nextInt(200) <= this.instability) {
         float xx = (float)this.field_174879_c.func_177958_n() + 0.5F + (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 4.0F;
         float zz = (float)this.field_174879_c.func_177952_p() + 0.5F + (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 4.0F;

         for(yy = this.field_174879_c.func_177956_o() - 2; !this.field_145850_b.func_175623_d(new BlockPos((double)xx, (double)yy, (double)zz)); ++yy) {
         }

         FXDispatcher.INSTANCE.arcLightning((double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), (double)xx, (double)yy, (double)zz, 0.8F, 0.1F, 1.0F, 0.0F);
      }

   }

   public AspectList getAspects() {
      return this.recipeEssentia;
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public class SourceFX {
      public BlockPos loc;
      public int ticks;
      public int color;
      public int entity;

      public SourceFX(BlockPos loc, int ticks, int color) {
         this.loc = loc;
         this.ticks = ticks;
         this.color = color;
      }
   }
}
