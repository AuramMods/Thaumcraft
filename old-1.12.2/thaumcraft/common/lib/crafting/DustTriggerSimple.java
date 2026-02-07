package thaumcraft.common.lib.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.lib.events.ServerEvents;

public class DustTriggerSimple implements IDustTrigger {
   Block target;
   ItemStack result;
   String research;

   public DustTriggerSimple(String research, Block target, ItemStack result) {
      this.target = target;
      this.result = result;
      this.research = research;
   }

   public IDustTrigger.Placement getValidFace(World world, EntityPlayer player, BlockPos pos, EnumFacing face) {
      return world.func_180495_p(pos).func_177230_c() != this.target || this.research != null && !ThaumcraftCapabilities.getKnowledge(player).isResearchKnown(this.research) ? null : new IDustTrigger.Placement(0, 0, 0, (EnumFacing)null);
   }

   public void execute(final World world, final EntityPlayer player, final BlockPos pos, IDustTrigger.Placement placement, EnumFacing side) {
      FMLCommonHandler.instance().firePlayerCraftingEvent(player, this.result, new InventoryFake(new ItemStack[]{null}));
      final IBlockState state = world.func_180495_p(pos);
      ServerEvents.addRunnableServer(world, new Runnable() {
         public void run() {
            ServerEvents.addSwapper(world, pos, state, DustTriggerSimple.this.result, false, 0, player, true, true, -9999, false, false, 0, ServerEvents.DEFAULT_PREDICATE, 0.0F);
         }
      }, 50);
   }
}
