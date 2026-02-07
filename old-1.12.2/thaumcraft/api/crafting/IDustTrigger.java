package thaumcraft.api.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDustTrigger {
   ArrayList<IDustTrigger> triggers = new ArrayList();

   IDustTrigger.Placement getValidFace(World var1, EntityPlayer var2, BlockPos var3, EnumFacing var4);

   void execute(World var1, EntityPlayer var2, BlockPos var3, IDustTrigger.Placement var4, EnumFacing var5);

   default List<BlockPos> sparkle(World world, EntityPlayer player, BlockPos pos, IDustTrigger.Placement placement) {
      return Arrays.asList(pos);
   }

   static void registerDustTrigger(IDustTrigger trigger) {
      triggers.add(trigger);
   }

   public static class Placement {
      public int xOffset;
      public int yOffset;
      public int zOffset;
      public EnumFacing facing;

      public Placement(int xOffset, int yOffset, int zOffset, EnumFacing facing) {
         this.xOffset = xOffset;
         this.yOffset = yOffset;
         this.zOffset = zOffset;
         this.facing = facing;
      }
   }
}
