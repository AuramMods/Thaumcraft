package thaumcraft.common.tiles;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileThaumcraft extends TileEntity {
   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.readSyncNBT(nbt);
   }

   public void readSyncNBT(NBTTagCompound nbt) {
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      return this.writeSyncNBT(super.func_189515_b(nbt));
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
      return nbt;
   }

   public void syncTile(boolean rerender) {
      IBlockState state = this.field_145850_b.func_180495_p(this.field_174879_c);
      this.field_145850_b.func_184138_a(this.field_174879_c, state, state, 2 + (rerender ? 4 : 0));
   }

   @Nullable
   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, -9, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.readSyncNBT(pkt.func_148857_g());
   }

   public NBTTagCompound func_189517_E_() {
      return this.writeSyncNBT(this.setupNbt());
   }

   private NBTTagCompound setupNbt() {
      NBTTagCompound nbt = super.func_189515_b(new NBTTagCompound());
      nbt.func_82580_o("ForgeData");
      nbt.func_82580_o("ForgeCaps");
      return nbt;
   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
      return oldState.func_177230_c() != newState.func_177230_c();
   }

   public EnumFacing getFacing() {
      try {
         return EnumFacing.func_82600_a(this.func_145832_p() & 7);
      } catch (Exception var2) {
         return EnumFacing.UP;
      }
   }

   public boolean gettingPower() {
      return this.field_145850_b.func_175640_z(this.func_174877_v());
   }
}
