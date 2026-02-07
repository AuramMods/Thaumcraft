package thaumcraft.common.lib.capabilities;

import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;

public class PlayerWarp {
   public static void preInit() {
      CapabilityManager.INSTANCE.register(IPlayerWarp.class, new IStorage<IPlayerWarp>() {
         public NBTTagCompound writeNBT(Capability<IPlayerWarp> capability, IPlayerWarp instance, EnumFacing side) {
            return (NBTTagCompound)instance.serializeNBT();
         }

         public void readNBT(Capability<IPlayerWarp> capability, IPlayerWarp instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
               instance.deserializeNBT((NBTTagCompound)nbt);
            }

         }
      }, () -> {
         return new PlayerWarp.DefaultImpl();
      });
   }

   public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
      public static final ResourceLocation NAME = new ResourceLocation("thaumcraft", "warp");
      private final PlayerWarp.DefaultImpl warp = new PlayerWarp.DefaultImpl();

      public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
         return capability == ThaumcraftCapabilities.WARP;
      }

      public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
         return capability == ThaumcraftCapabilities.WARP ? ThaumcraftCapabilities.WARP.cast(this.warp) : null;
      }

      public NBTTagCompound serializeNBT() {
         return this.warp.serializeNBT();
      }

      public void deserializeNBT(NBTTagCompound nbt) {
         this.warp.deserializeNBT(nbt);
      }
   }

   private static class DefaultImpl implements IPlayerWarp {
      private int[] warp;
      private int counter;

      private DefaultImpl() {
         this.warp = new int[IPlayerWarp.EnumWarpType.values().length];
      }

      public void clear() {
         this.warp = new int[IPlayerWarp.EnumWarpType.values().length];
         this.counter = 0;
      }

      public int get(@Nonnull IPlayerWarp.EnumWarpType type) {
         return this.warp[type.ordinal()];
      }

      public void set(IPlayerWarp.EnumWarpType type, int amount) {
         this.warp[type.ordinal()] = MathHelper.func_76125_a(amount, 0, 500);
      }

      public int add(@Nonnull IPlayerWarp.EnumWarpType type, int amount) {
         this.warp[type.ordinal()] = MathHelper.func_76125_a(this.warp[type.ordinal()] + amount, 0, 500);
         return this.warp[type.ordinal()];
      }

      public int reduce(@Nonnull IPlayerWarp.EnumWarpType type, int amount) {
         this.warp[type.ordinal()] = MathHelper.func_76125_a(this.warp[type.ordinal()] - amount, 0, 500);
         return this.warp[type.ordinal()];
      }

      public void sync(@Nonnull EntityPlayerMP player) {
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player), player);
      }

      public NBTTagCompound serializeNBT() {
         NBTTagCompound properties = new NBTTagCompound();
         properties.func_74783_a("warp", this.warp);
         properties.func_74768_a("counter", this.getCounter());
         return properties;
      }

      public void deserializeNBT(NBTTagCompound properties) {
         if (properties != null) {
            this.clear();
            int[] ba = properties.func_74759_k("warp");
            if (ba != null) {
               int l = IPlayerWarp.EnumWarpType.values().length;
               if (ba.length < l) {
                  l = ba.length;
               }

               for(int a = 0; a < l; ++a) {
                  this.warp[a] = ba[a];
               }
            }

            this.setCounter(properties.func_74762_e("counter"));
         }
      }

      public int getCounter() {
         return this.counter;
      }

      public void setCounter(int amount) {
         this.counter = amount;
      }

      // $FF: synthetic method
      DefaultImpl(Object x0) {
         this();
      }
   }
}
