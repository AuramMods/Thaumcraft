package thaumcraft.api.aspects;

public interface IAspectContainer {
   AspectList getAspects();

   void setAspects(AspectList var1);

   boolean doesContainerAccept(Aspect var1);

   int addToContainer(Aspect var1, int var2);

   boolean takeFromContainer(Aspect var1, int var2);

   /** @deprecated */
   @Deprecated
   boolean takeFromContainer(AspectList var1);

   boolean doesContainerContainAmount(Aspect var1, int var2);

   /** @deprecated */
   @Deprecated
   boolean doesContainerContain(AspectList var1);

   int containerContains(Aspect var1);
}
