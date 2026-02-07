package thaumcraft.common.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotGhost extends Slot {
   int limit = Integer.MAX_VALUE;

   public SlotGhost(IInventory par1iInventory, int par2, int par3, int par4, int par5) {
      super(par1iInventory, par2, par3, par4);
      this.limit = par5;
   }

   public SlotGhost(IInventory par1iInventory, int par2, int par3, int par4) {
      super(par1iInventory, par2, par3, par4);
   }

   public int func_75219_a() {
      return this.limit;
   }

   public boolean func_82869_a(EntityPlayer par1EntityPlayer) {
      return false;
   }
}
