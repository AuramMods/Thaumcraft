package thaumcraft.api.capabilities;

import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ThaumcraftCapabilities {
   @CapabilityInject(IPlayerKnowledge.class)
   public static final Capability<IPlayerKnowledge> KNOWLEDGE = null;
   @CapabilityInject(IPlayerWarp.class)
   public static final Capability<IPlayerWarp> WARP = null;

   public static IPlayerKnowledge getKnowledge(@Nonnull EntityPlayer player) {
      return (IPlayerKnowledge)player.getCapability(KNOWLEDGE, (EnumFacing)null);
   }

   public static boolean knowsResearch(@Nonnull EntityPlayer player, @Nonnull String... research) {
      String[] var2 = research;
      int var3 = research.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String r = var2[var4];
         if (!getKnowledge(player).isResearchKnown(r)) {
            return false;
         }
      }

      return true;
   }

   public static boolean knowsResearchStrict(@Nonnull EntityPlayer player, @Nonnull String... research) {
      String[] var2 = research;
      int var3 = research.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String r = var2[var4];
         if (r.contains("@")) {
            if (!getKnowledge(player).isResearchKnown(r)) {
               return false;
            }
         } else if (!getKnowledge(player).isResearchComplete(r)) {
            return false;
         }
      }

      return true;
   }

   public static IPlayerWarp getWarp(@Nonnull EntityPlayer player) {
      return (IPlayerWarp)player.getCapability(WARP, (EnumFacing)null);
   }
}
