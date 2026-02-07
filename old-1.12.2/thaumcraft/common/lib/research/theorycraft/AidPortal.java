package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.init.Blocks;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;

public class AidPortal implements ITheorycraftAid {
   Object portal;

   public AidPortal(Object o) {
      this.portal = o;
   }

   public Object getAidObject() {
      return this.portal;
   }

   public Class<TheorycraftCard>[] getCards() {
      return new Class[]{CardPortal.class};
   }

   public static class AidPortalCrimson extends AidPortal {
      public AidPortalCrimson() {
         super(EntityCultistPortalLesser.class);
      }
   }

   public static class AidPortalNether extends AidPortal {
      public AidPortalNether() {
         super(Blocks.field_150427_aO);
      }
   }

   public static class AidPortalEnd extends AidPortal {
      public AidPortalEnd() {
         super(Blocks.field_150384_bq);
      }
   }
}
