package thaumcraft.api.golems.seals;

public interface ISealConfigToggles {
   ISealConfigToggles.SealToggle[] getToggles();

   void setToggle(int var1, boolean var2);

   public static class SealToggle {
      public boolean value;
      public String key;
      public String name;

      public SealToggle(boolean value, String key, String name) {
         this.value = value;
         this.key = key;
         this.name = name;
      }

      public boolean getValue() {
         return this.value;
      }

      public void setValue(boolean value) {
         this.value = value;
      }

      public String getKey() {
         return this.key;
      }

      public String getName() {
         return this.name;
      }
   }
}
