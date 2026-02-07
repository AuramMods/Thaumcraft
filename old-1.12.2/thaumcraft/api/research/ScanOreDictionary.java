package thaumcraft.api.research;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public class ScanOreDictionary implements IScanThing {
   String research;
   String[] entries;

   public ScanOreDictionary(String research, String... entries) {
      this.research = research;
      this.entries = entries;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      ItemStack stack = null;
      if (obj != null && obj instanceof BlockPos) {
         IBlockState state = player.field_70170_p.func_180495_p((BlockPos)obj);
         stack = state.func_177230_c().func_185473_a(player.field_70170_p, (BlockPos)obj, state);
      }

      if (obj != null && obj instanceof ItemStack) {
         stack = (ItemStack)obj;
      }

      if (obj != null && obj instanceof EntityItem && ((EntityItem)obj).func_92059_d() != null) {
         stack = ((EntityItem)obj).func_92059_d();
      }

      if (stack != null) {
         int[] ids = OreDictionary.getOreIDs(stack);
         String[] var5 = this.entries;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String entry = var5[var7];
            int[] var9 = ids;
            int var10 = ids.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               int id = var9[var11];
               if (OreDictionary.getOreName(id).equals(entry)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public String getResearchKey(EntityPlayer player, Object object) {
      return this.research;
   }
}
