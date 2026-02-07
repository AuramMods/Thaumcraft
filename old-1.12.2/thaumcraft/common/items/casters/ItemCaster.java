package thaumcraft.common.items.casters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.casters.CasterTriggerRegistry;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.IFocusBlockPicker;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.casters.IFocusPartMedium;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.api.items.IArchitect;
import thaumcraft.api.items.IArchitectExtended;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketAuraToClient;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

public class ItemCaster extends ItemTCBase implements IArchitectExtended, ICaster {
   int area = 0;
   DecimalFormat myFormatter = new DecimalFormat("#######.#");

   public ItemCaster(String name, int area) {
      super(name);
      this.area = area;
      this.field_77777_bU = 1;
      this.func_77656_e(0);
      this.func_185043_a(new ResourceLocation("focus"), new IItemPropertyGetter() {
         @SideOnly(Side.CLIENT)
         public float func_185085_a(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            ItemFocus f = ((ItemCaster)stack.func_77973_b()).getFocus(stack);
            return stack.func_77973_b() instanceof ItemCaster && f != null ? 1.0F : 0.0F;
         }
      });
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      if (oldStack.func_77973_b() != null && oldStack.func_77973_b() == this && newStack.func_77973_b() != null && newStack.func_77973_b() == this) {
         ItemFocus oldf = ((ItemCaster)oldStack.func_77973_b()).getFocus(oldStack);
         ItemFocus newf = ((ItemCaster)newStack.func_77973_b()).getFocus(newStack);
         int s1 = 0;
         int s2 = 0;
         if (oldf != null) {
            s1 = ItemFocus.getCore(((ItemCaster)oldStack.func_77973_b()).getFocusStack(oldStack)).getSortingHelper().hashCode();
         }

         if (newf != null) {
            s2 = ItemFocus.getCore(((ItemCaster)newStack.func_77973_b()).getFocusStack(newStack)).getSortingHelper().hashCode();
         }

         return s1 != s2;
      } else {
         return newStack.func_77973_b() != oldStack.func_77973_b();
      }
   }

   public boolean func_77645_m() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77662_d() {
      return true;
   }

   private float getAuraPool(EntityPlayer player) {
      float tot = 0.0F;
      int zz;
      switch(this.area) {
      case 1:
         tot = AuraHandler.getVis(player.field_70170_p, player.func_180425_c());
         EnumFacing[] var7 = EnumFacing.field_176754_o;
         zz = var7.length;

         for(int var5 = 0; var5 < zz; ++var5) {
            EnumFacing face = var7[var5];
            tot += AuraHandler.getVis(player.field_70170_p, player.func_180425_c().func_177967_a(face, 16));
         }

         return tot;
      case 2:
         tot = 0.0F;

         for(int xx = -1; xx <= 1; ++xx) {
            for(zz = -1; zz <= 1; ++zz) {
               tot += AuraHandler.getVis(player.field_70170_p, player.func_180425_c().func_177982_a(xx * 16, 0, zz * 16));
            }
         }

         return tot;
      default:
         tot = AuraHandler.getVis(player.field_70170_p, player.func_180425_c());
         return tot;
      }
   }

   public boolean consumeVis(ItemStack is, EntityPlayer player, float amount, boolean crafting) {
      amount *= this.getConsumptionModifier(is, player, crafting);
      float tot = this.getAuraPool(player);
      if (tot < amount) {
         return false;
      } else {
         float i;
         int zz;
         switch(this.area) {
         case 1:
            i = amount / 5.0F;

            while(amount > 0.0F) {
               if (i > amount) {
                  i = amount;
               }

               amount -= AuraHandler.drainVis(player.field_70170_p, player.func_180425_c(), i, false);
               if (amount <= 0.0F) {
                  return true;
               }

               if (i > amount) {
                  i = amount;
               }

               EnumFacing[] var11 = EnumFacing.field_176754_o;
               zz = var11.length;

               for(int var9 = 0; var9 < zz; ++var9) {
                  EnumFacing face = var11[var9];
                  amount -= AuraHandler.drainVis(player.field_70170_p, player.func_180425_c().func_177967_a(face, 16), i, false);
                  if (amount <= 0.0F) {
                     return true;
                  }
               }
            }

            return true;
         case 2:
            i = amount / 9.0F;

            while(amount > 0.0F) {
               if (i > amount) {
                  i = amount;
               }

               for(int xx = -1; xx <= 1; ++xx) {
                  for(zz = -1; zz <= 1; ++zz) {
                     amount -= AuraHandler.drainVis(player.field_70170_p, player.func_180425_c().func_177982_a(xx * 16, 0, zz * 16), i, false);
                     if (amount <= 0.0F) {
                        return true;
                     }
                  }
               }
            }

            return true;
         default:
            AuraHandler.drainVis(player.field_70170_p, player.func_180425_c(), amount, false);
            return true;
         }
      }
   }

   public float getConsumptionModifier(ItemStack is, EntityPlayer player, boolean crafting) {
      float consumptionModifier = 1.0F;
      if (player != null) {
         consumptionModifier -= CasterManager.getTotalVisDiscount(player);
      }

      return Math.max(consumptionModifier, 0.1F);
   }

   public ItemFocus getFocus(ItemStack stack) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("focus")) {
         NBTTagCompound nbt = stack.func_77978_p().func_74775_l("focus");
         ItemStack fs = ItemStack.func_77949_a(nbt);
         if (fs != null) {
            return (ItemFocus)fs.func_77973_b();
         }
      }

      return null;
   }

   public ItemStack getFocusStack(ItemStack stack) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("focus")) {
         NBTTagCompound nbt = stack.func_77978_p().func_74775_l("focus");
         return ItemStack.func_77949_a(nbt);
      } else {
         return null;
      }
   }

   public void setFocus(ItemStack stack, ItemStack focus) {
      if (focus == null) {
         stack.func_77978_p().func_82580_o("focus");
      } else {
         stack.func_77983_a("focus", focus.func_77955_b(new NBTTagCompound()));
      }

   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }

   public void func_77624_a(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.func_77942_o()) {
         String tt = "";
         int amount = RechargeHelper.getCharge(stack);
         float mod = this.getConsumptionModifier(stack, player, false);
         String consumption = this.myFormatter.format((double)(mod * 100.0F));
         String text = "";
         ItemStack focus = this.getFocusStack(stack);
         if (focus != null) {
            float amt = ((ItemFocus)focus.func_77973_b()).getVisCost(focus) * mod;
            if (amt > 0.0F) {
               text = "§r, " + this.myFormatter.format((double)amt) + " " + I18n.func_74838_a("item.Focus.cost1");
            }
         }

         if (Thaumcraft.proxy.isShiftKeyDown()) {
            list.add(" §" + I18n.func_74838_a("tc.vis.cost") + "§r " + this.myFormatter.format((long)amount) + ", §o(" + consumption + "%" + text);
         } else {
            if (tt.length() > 0) {
               tt = tt + " | ";
            }

            tt = tt + "§" + this.myFormatter.format((long)amount) + "§r";
         }
      }

      if (this.getFocus(stack) != null) {
         list.add(TextFormatting.BOLD + "" + TextFormatting.ITALIC + "" + TextFormatting.GREEN + this.getFocus(stack).func_77653_i(this.getFocusStack(stack)));
         if (Thaumcraft.proxy.isShiftKeyDown()) {
            this.getFocus(stack).addFocusInformation(this.getFocusStack(stack), player, list, par4);
         }
      }

   }

   public void func_77663_a(ItemStack is, World w, Entity e, int slot, boolean currentItem) {
      if (currentItem && !w.field_72995_K && e.field_70173_aa % 10 == 0 && e instanceof EntityPlayerMP) {
         this.updateAura(is, w, (EntityPlayerMP)e);
      }

   }

   private void updateAura(ItemStack stack, World world, EntityPlayerMP player) {
      float cv;
      float cf;
      short bv;
      cv = 0.0F;
      cf = 0.0F;
      bv = 0;
      AuraChunk ac;
      int zz;
      label33:
      switch(this.area) {
      case 1:
         ac = AuraHandler.getAuraChunk(world.field_73011_w.getDimension(), (int)player.field_70165_t >> 4, (int)player.field_70161_v >> 4);
         cv = ac.getVis();
         cf = ac.getFlux();
         bv = ac.getBase();
         EnumFacing[] var12 = EnumFacing.field_176754_o;
         zz = var12.length;
         int var10 = 0;

         while(true) {
            if (var10 >= zz) {
               break label33;
            }

            EnumFacing face = var12[var10];
            ac = AuraHandler.getAuraChunk(world.field_73011_w.getDimension(), ((int)player.field_70165_t >> 4) + face.func_82601_c(), ((int)player.field_70161_v >> 4) + face.func_82599_e());
            cv += ac.getVis();
            cf += ac.getFlux();
            bv += ac.getBase();
            ++var10;
         }
      case 2:
         int xx = -1;

         while(true) {
            if (xx > 1) {
               break label33;
            }

            for(zz = -1; zz <= 1; ++zz) {
               ac = AuraHandler.getAuraChunk(world.field_73011_w.getDimension(), ((int)player.field_70165_t >> 4) + xx, ((int)player.field_70161_v >> 4) + zz);
               cv += ac.getVis();
               cf += ac.getFlux();
               bv += ac.getBase();
            }

            ++xx;
         }
      default:
         ac = AuraHandler.getAuraChunk(world.field_73011_w.getDimension(), (int)player.field_70165_t >> 4, (int)player.field_70161_v >> 4);
         cv = ac.getVis();
         cf = ac.getFlux();
         bv = ac.getBase();
      }

      PacketHandler.INSTANCE.sendTo(new PacketAuraToClient(new AuraChunk((Chunk)null, bv, cv, cf)), player);
   }

   public EnumActionResult onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      IBlockState bs = world.func_180495_p(pos);
      if (bs.func_177230_c() instanceof IInteractWithCaster && ((IInteractWithCaster)bs.func_177230_c()).onCasterRightClick(world, itemstack, player, pos, side, hand)) {
         return EnumActionResult.SUCCESS;
      } else {
         TileEntity tile = world.func_175625_s(pos);
         if (tile != null && tile instanceof IInteractWithCaster && ((IInteractWithCaster)tile).onCasterRightClick(world, itemstack, player, pos, side, hand)) {
            return EnumActionResult.SUCCESS;
         } else if (CasterTriggerRegistry.hasTrigger(bs)) {
            return CasterTriggerRegistry.performTrigger(world, itemstack, player, pos, side, bs) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
         } else {
            ItemStack fb = this.getFocusStack(itemstack);
            if (fb != null) {
               FocusCore core = ItemFocus.getCore(fb);
               Iterator var14 = core.partsRaw.values().iterator();

               while(var14.hasNext()) {
                  IFocusPart fe = (IFocusPart)var14.next();
                  if (fe instanceof IFocusBlockPicker && player.func_70093_af() && world.func_175625_s(pos) == null) {
                     if (!world.field_72995_K) {
                        ItemStack isout = new ItemStack(bs.func_177230_c(), 1, bs.func_177230_c().func_176201_c(bs));

                        try {
                           if (bs != Blocks.field_150350_a) {
                              ItemStack is = BlockUtils.getSilkTouchDrop(bs);
                              if (is != null) {
                                 isout = is.func_77946_l();
                              }
                           }
                        } catch (Exception var18) {
                        }

                        this.storePickedBlock(itemstack, isout);
                        return EnumActionResult.SUCCESS;
                     }

                     player.func_184609_a(hand);
                     return EnumActionResult.PASS;
                  }
               }
            }

            return EnumActionResult.PASS;
         }
      }
   }

   public ActionResult<ItemStack> func_77659_a(ItemStack itemstack, World world, EntityPlayer player, EnumHand hand) {
      ItemStack focusStack = this.getFocusStack(itemstack);
      ItemFocus focus = this.getFocus(itemstack);
      if (focus != null) {
         FocusCore core = ItemFocus.getCore(focusStack);
         if (core.getFinalCastMethod() != IFocusPartMedium.EnumFocusCastMethod.INSTANT) {
            if (core.getFinalCastMethod() == IFocusPartMedium.EnumFocusCastMethod.DEFAULT && !CasterManager.isOnCooldown(player) && this.consumeVis(itemstack, player, focus.getVisCost(focusStack), false)) {
               core.medium.onMediumTrigger(world, player, itemstack, core, 1.0F);
               CasterManager.setCooldown(player, focus.getActivationTime(focusStack));
            }

            player.func_184598_c(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
         }

         if (this.consumeVis(itemstack, player, focus.getVisCost(focusStack), false)) {
            player.func_184609_a(hand);
            if (core.medium.onMediumTrigger(world, player, itemstack, core, 1.0F)) {
               CasterManager.setCooldown(player, focus.getActivationTime(focusStack));
               return new ActionResult(EnumActionResult.PASS, itemstack);
            }

            return new ActionResult(EnumActionResult.FAIL, itemstack);
         }
      }

      return super.func_77659_a(itemstack, world, player, hand);
   }

   public void onUsingTick(ItemStack itemstack, EntityLivingBase player, int count) {
      ItemStack focusStack = this.getFocusStack(itemstack);
      ItemFocus focus = this.getFocus(itemstack);
      if (focus != null) {
         FocusCore core = ItemFocus.getCore(focusStack);
         if (core.getFinalCastMethod() == IFocusPartMedium.EnumFocusCastMethod.DEFAULT && count % focus.getActivationTime(focusStack) == 0 && !CasterManager.isOnCooldown(player) && this.consumeVis(itemstack, (EntityPlayer)player, focus.getVisCost(focusStack), false)) {
            CasterManager.setCooldown(player, focus.getActivationTime(focusStack));
            core.medium.onMediumTrigger(player.func_130014_f_(), player, itemstack, core, 1.0F);
         } else if (core.getFinalCastMethod() == IFocusPartMedium.EnumFocusCastMethod.CHARGE && count == 1) {
            player.func_184597_cx();
         }
      }

   }

   public void func_77615_a(ItemStack itemstack, World world, EntityLivingBase player, int timeLeft) {
      ItemStack focusStack = this.getFocusStack(itemstack);
      ItemFocus focus = this.getFocus(itemstack);
      if (focus != null) {
         FocusCore core = ItemFocus.getCore(focusStack);
         if (core.getFinalCastMethod() == IFocusPartMedium.EnumFocusCastMethod.CHARGE) {
            float max = (float)focus.getActivationTime(focusStack);
            float count = (float)(this.func_77626_a(itemstack) - timeLeft);
            if (count > max) {
               count = max;
            }

            float progress = count / max;
            double rad = (double)(100.0F + progress * 80.0F) * 0.017453292519943D;
            float charge = (float)MathHelper.clip(2.0D + 1.5D * Math.tan(rad) / 5.671D, 0.5D, 2.0D);
            if (this.consumeVis(itemstack, (EntityPlayer)player, focus.getVisCost(focusStack) * charge, false)) {
               core.medium.onMediumTrigger(player.func_130014_f_(), player, itemstack, core, charge);
            }
         }
      }

   }

   public int func_77626_a(ItemStack itemstack) {
      return 72000;
   }

   public EnumAction func_77661_b(ItemStack par1ItemStack) {
      return EnumAction.BOW;
   }

   public ArrayList<BlockPos> getArchitectBlocks(ItemStack stack, World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
      ItemFocus focus = this.getFocus(stack);
      return focus != null && focus instanceof IArchitect ? ((IArchitect)focus).getArchitectBlocks(stack, world, pos, side, player) : null;
   }

   public RayTraceResult getArchitectMOP(ItemStack stack, World world, EntityLivingBase player) {
      ItemFocus focus = this.getFocus(stack);
      return focus != null && focus instanceof IArchitectExtended ? ((IArchitectExtended)focus).getArchitectMOP(stack, world, player) : null;
   }

   public boolean useBlockHighlight(ItemStack stack) {
      ItemFocus focus = this.getFocus(stack);
      return focus != null && focus instanceof IArchitectExtended ? ((IArchitectExtended)focus).useBlockHighlight(stack) : true;
   }

   public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing side, IArchitect.EnumAxis axis) {
      return false;
   }

   public void storePickedBlock(ItemStack stack, ItemStack stackout) {
      NBTTagCompound item = new NBTTagCompound();
      stack.func_77983_a("picked", stackout.func_77955_b(item));
   }

   public ItemStack getPickedBlock(ItemStack stack) {
      ItemStack out = null;
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("picked")) {
         out = new ItemStack(Blocks.field_150350_a);
         out.func_77963_c(stack.func_77978_p().func_74775_l("picked"));
      }

      return out;
   }
}
