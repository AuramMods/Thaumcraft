package thaumcraft.common.entities.construct.golem.seals;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.ISealDisplayer;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.ItemTCBase;

public class ItemSealPlacer extends ItemTCBase implements ISealDisplayer {
   public ItemSealPlacer() {
      super("seal", "blank");
      this.func_77625_d(64);
      this.func_77627_a(true);
      this.func_77656_e(0);
   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      String[] vn = this.getVariantNames();

      for(int a = 0; a < vn.length; ++a) {
         par3List.add(new ItemStack(this, 1, a));
      }

   }

   public String[] getVariantNames() {
      if (SealHandler.types.size() + 1 != this.VARIANTS.length) {
         String[] rs = SealHandler.getRegisteredSeals();
         String[] out = new String[rs.length + 1];
         out[0] = "blank";
         int indx = 1;
         String[] var4 = rs;
         int var5 = rs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            String[] sp = s.split(":");
            out[indx] = sp.length > 1 ? sp[1] : sp[0];
            ++indx;
         }

         this.VARIANTS = out;
         this.VARIANTS_META = new int[this.VARIANTS.length];

         for(int m = 0; m < this.VARIANTS.length; this.VARIANTS_META[m] = m++) {
         }
      }

      return this.VARIANTS;
   }

   public static ItemStack getSealStack(String sealKey) {
      String[] rs = SealHandler.getRegisteredSeals();
      int indx = 1;
      String[] var3 = rs;
      int var4 = rs.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String s = var3[var5];
         if (s.equals(sealKey)) {
            return new ItemStack(ItemsTC.seals, 1, indx);
         }

         ++indx;
      }

      return null;
   }

   public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (!world.field_72995_K && stack.func_77952_i() != 0) {
         if (!player.func_175151_a(pos, side, stack)) {
            return EnumActionResult.FAIL;
         } else {
            String[] rs = SealHandler.getRegisteredSeals();
            ISeal seal = null;

            try {
               seal = (ISeal)SealHandler.getSeal(rs[stack.func_77952_i() - 1]).getClass().newInstance();
            } catch (Exception var13) {
               var13.printStackTrace();
            }

            if (seal != null && seal.canPlaceAt(world, pos, side)) {
               if (SealHandler.addSealEntity(world, pos, side, seal, player) && !player.field_71075_bZ.field_75098_d) {
                  --stack.field_77994_a;
               }

               return EnumActionResult.SUCCESS;
            } else {
               return EnumActionResult.FAIL;
            }
         }
      } else {
         return EnumActionResult.PASS;
      }
   }
}
