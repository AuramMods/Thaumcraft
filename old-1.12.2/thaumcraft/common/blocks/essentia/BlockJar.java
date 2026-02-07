package thaumcraft.common.blocks.essentia;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.ILabelable;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.BlockTCTile;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.devices.TileJarBrain;
import thaumcraft.common.tiles.essentia.TileJarFillable;

public class BlockJar extends BlockTCTile implements ILabelable {
   public BlockJar(Class t) {
      super(Material.field_151592_s, t);
      this.func_149711_c(0.3F);
      this.func_149672_a(SoundsTC.JAR);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.75D, 0.8125D);
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.func_176203_a(meta);
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      spillEssentia = false;
      super.func_180663_b(worldIn, pos, state);
      spillEssentia = true;
   }

   public void func_180653_a(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (te instanceof TileJarFillable) {
         this.spawnFilledJar(worldIn, pos, state, (TileJarFillable)te);
      } else if (te instanceof TileJarBrain) {
         this.spawnBrainJar(worldIn, pos, state, (TileJarBrain)te);
      } else {
         super.func_180653_a(worldIn, pos, state, chance, fortune);
      }

   }

   public void func_180657_a(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
      if (te instanceof TileJarFillable) {
         this.spawnFilledJar(worldIn, pos, state, (TileJarFillable)te);
      } else if (te instanceof TileJarBrain) {
         this.spawnBrainJar(worldIn, pos, state, (TileJarBrain)te);
      } else {
         super.func_180657_a(worldIn, player, pos, state, (TileEntity)null, stack);
      }

   }

   private void spawnFilledJar(World world, BlockPos pos, IBlockState state, TileJarFillable te) {
      ItemStack drop = new ItemStack(this, 1, this.func_176201_c(state));
      if (te.amount > 0) {
         ((BlockJarItem)drop.func_77973_b()).setAspects(drop, (new AspectList()).add(te.aspect, te.amount));
      }

      if (te.aspectFilter != null) {
         if (!drop.func_77942_o()) {
            drop.func_77982_d(new NBTTagCompound());
         }

         drop.func_77978_p().func_74778_a("AspectFilter", te.aspectFilter.getTag());
      }

      if (te.blocked) {
         func_180635_a(world, pos, new ItemStack(ItemsTC.jarBrace));
      }

      func_180635_a(world, pos, drop);
   }

   private void spawnBrainJar(World world, BlockPos pos, IBlockState state, TileJarBrain te) {
      ItemStack drop = new ItemStack(this, 1, this.func_176201_c(state));
      if (te.xp > 0) {
         drop.func_77983_a("xp", new NBTTagInt(te.xp));
      }

      func_180635_a(world, pos, drop);
   }

   public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack) {
      int l = MathHelper.func_76128_c((double)(ent.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
      TileEntity tile = world.func_175625_s(pos);
      if (tile instanceof TileJarFillable) {
         if (l == 0) {
            ((TileJarFillable)tile).facing = 2;
         }

         if (l == 1) {
            ((TileJarFillable)tile).facing = 5;
         }

         if (l == 2) {
            ((TileJarFillable)tile).facing = 3;
         }

         if (l == 3) {
            ((TileJarFillable)tile).facing = 4;
         }
      }

   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      TileEntity te = world.func_175625_s(pos);
      if (te != null && te instanceof TileJarBrain) {
         ((TileJarBrain)te).eatDelay = 40;
         if (!world.field_72995_K) {
            int var6 = world.field_73012_v.nextInt(Math.min(((TileJarBrain)te).xp + 1, 64));
            if (var6 > 0) {
               ((TileJarBrain)te).xp -= var6;
               int xp = var6;

               while(xp > 0) {
                  int var2 = EntityXPOrb.func_70527_a(xp);
                  xp -= var2;
                  world.func_72838_d(new EntityXPOrb(world, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, var2));
               }

               world.markAndNotifyBlock(pos, world.func_175726_f(pos), state, state, 3);
               te.func_70296_d();
            }
         } else {
            world.func_184134_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundsTC.jar, SoundCategory.BLOCKS, 0.2F, 1.0F, false);
         }
      }

      if (te != null && te instanceof TileJarFillable && !((TileJarFillable)te).blocked && heldItem != null && heldItem.func_77973_b() == ItemsTC.jarBrace) {
         ((TileJarFillable)te).blocked = true;
         --heldItem.field_77994_a;
         if (world.field_72995_K) {
            world.func_184134_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundsTC.key, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
         } else {
            te.func_70296_d();
         }
      } else if (te != null && te instanceof TileJarFillable && player.func_70093_af() && ((TileJarFillable)te).aspectFilter != null && side.ordinal() == ((TileJarFillable)te).facing) {
         ((TileJarFillable)te).aspectFilter = null;
         if (world.field_72995_K) {
            world.func_184134_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundsTC.page, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
         } else {
            world.func_72838_d(new EntityItem(world, (double)((float)pos.func_177958_n() + 0.5F + (float)side.func_82601_c() / 3.0F), (double)((float)pos.func_177956_o() + 0.5F), (double)((float)pos.func_177952_p() + 0.5F + (float)side.func_82599_e() / 3.0F), new ItemStack(ItemsTC.label)));
         }
      } else if (te != null && te instanceof TileJarFillable && player.func_70093_af() && heldItem == null) {
         if (((TileJarFillable)te).aspectFilter == null) {
            ((TileJarFillable)te).aspect = null;
         }

         if (world.field_72995_K) {
            world.func_184134_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundsTC.jar, SoundCategory.BLOCKS, 0.4F, 1.0F, false);
            world.func_184134_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundEvents.field_187615_H, SoundCategory.BLOCKS, 0.5F, 1.0F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.3F, false);
         } else {
            AuraHelper.polluteAura(world, pos, (float)((TileJarFillable)te).amount, true);
         }

         ((TileJarFillable)te).amount = 0;
         te.func_70296_d();
      }

      return true;
   }

   public boolean applyLabel(EntityPlayer player, BlockPos pos, EnumFacing side, ItemStack labelstack) {
      TileEntity te = player.field_70170_p.func_175625_s(pos);
      if (te != null && te instanceof TileJarFillable && ((TileJarFillable)te).aspectFilter == null) {
         if (((TileJarFillable)te).amount == 0 && ((IEssentiaContainerItem)((IEssentiaContainerItem)labelstack.func_77973_b())).getAspects(labelstack) == null) {
            return false;
         } else {
            if (((TileJarFillable)te).amount == 0 && ((IEssentiaContainerItem)((IEssentiaContainerItem)labelstack.func_77973_b())).getAspects(labelstack) != null) {
               ((TileJarFillable)te).aspect = ((IEssentiaContainerItem)((IEssentiaContainerItem)labelstack.func_77973_b())).getAspects(labelstack).getAspects()[0];
            }

            this.func_180633_a(player.field_70170_p, pos, player.field_70170_p.func_180495_p(pos), player, (ItemStack)null);
            ((TileJarFillable)te).aspectFilter = ((TileJarFillable)te).aspect;
            player.field_70170_p.markAndNotifyBlock(pos, player.field_70170_p.func_175726_f(pos), player.field_70170_p.func_180495_p(pos), player.field_70170_p.func_180495_p(pos), 3);
            te.func_70296_d();
            player.field_70170_p.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundsTC.jar, SoundCategory.BLOCKS, 0.4F, 1.0F);
            return true;
         }
      } else {
         return false;
      }
   }

   public float getEnchantPowerBonus(World world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      return te != null && te instanceof TileJarBrain ? 2.0F : super.getEnchantPowerBonus(world, pos);
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && tile instanceof TileJarBrain && ((TileJarBrain)tile).xp >= ((TileJarBrain)tile).xpMax) {
         FXDispatcher.INSTANCE.spark((double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 0.8F), (double)((float)pos.func_177952_p() + 0.5F), 3.0F, 0.2F + rand.nextFloat() * 0.2F, 1.0F, 0.3F + rand.nextFloat() * 0.2F, 0.5F);
      }

   }

   public boolean func_149740_M(IBlockState state) {
      return true;
   }

   public int func_180641_l(IBlockState state, World world, BlockPos pos) {
      TileEntity tile = world.func_175625_s(pos);
      float r;
      if (tile != null && tile instanceof TileJarBrain) {
         r = (float)((TileJarBrain)tile).xp / (float)((TileJarBrain)tile).xpMax;
         return MathHelper.func_76141_d(r * 14.0F) + (((TileJarBrain)tile).xp > 0 ? 1 : 0);
      } else if (tile != null && tile instanceof TileJarFillable) {
         r = (float)((TileJarFillable)tile).amount / (float)((TileJarFillable)tile).maxAmount;
         return MathHelper.func_76141_d(r * 14.0F) + (((TileJarFillable)tile).amount > 0 ? 1 : 0);
      } else {
         return super.func_180641_l(state, world, pos);
      }
   }
}
