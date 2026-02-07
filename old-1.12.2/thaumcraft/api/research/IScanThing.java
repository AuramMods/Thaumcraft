package thaumcraft.api.research;

import net.minecraft.entity.player.EntityPlayer;

public interface IScanThing {
   boolean checkThing(EntityPlayer var1, Object var2);

   String getResearchKey(EntityPlayer var1, Object var2);

   default void onSuccess(EntityPlayer player, Object object) {
   }
}
