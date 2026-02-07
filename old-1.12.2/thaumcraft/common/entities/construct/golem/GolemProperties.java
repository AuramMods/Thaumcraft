package thaumcraft.common.entities.construct.golem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.golems.parts.GolemAddon;
import thaumcraft.api.golems.parts.GolemArm;
import thaumcraft.api.golems.parts.GolemHead;
import thaumcraft.api.golems.parts.GolemLeg;
import thaumcraft.api.golems.parts.GolemMaterial;
import thaumcraft.api.golems.parts.PartModel;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.renderers.entity.construct.PartModelBreakers;
import thaumcraft.client.renderers.entity.construct.PartModelClaws;
import thaumcraft.client.renderers.entity.construct.PartModelDarts;
import thaumcraft.client.renderers.entity.construct.PartModelHauler;
import thaumcraft.client.renderers.entity.construct.PartModelWheel;
import thaumcraft.common.entities.construct.golem.parts.GolemArmDart;
import thaumcraft.common.entities.construct.golem.parts.GolemLegLevitator;
import thaumcraft.common.entities.construct.golem.parts.GolemLegWheels;

public class GolemProperties implements IGolemProperties {
   private long data = 0L;
   private Set<EnumGolemTrait> traitCache = null;

   public Set<EnumGolemTrait> getTraits() {
      if (this.traitCache == null) {
         this.traitCache = new HashSet();
         EnumGolemTrait[] var1 = this.getMaterial().traits;
         int var2 = var1.length;

         int var3;
         EnumGolemTrait trait;
         for(var3 = 0; var3 < var2; ++var3) {
            trait = var1[var3];
            this.addTraitSmart(trait);
         }

         var1 = this.getHead().traits;
         var2 = var1.length;

         for(var3 = 0; var3 < var2; ++var3) {
            trait = var1[var3];
            this.addTraitSmart(trait);
         }

         var1 = this.getArms().traits;
         var2 = var1.length;

         for(var3 = 0; var3 < var2; ++var3) {
            trait = var1[var3];
            this.addTraitSmart(trait);
         }

         var1 = this.getLegs().traits;
         var2 = var1.length;

         for(var3 = 0; var3 < var2; ++var3) {
            trait = var1[var3];
            this.addTraitSmart(trait);
         }

         var1 = this.getAddon().traits;
         var2 = var1.length;

         for(var3 = 0; var3 < var2; ++var3) {
            trait = var1[var3];
            this.addTraitSmart(trait);
         }
      }

      return this.traitCache;
   }

   private void addTraitSmart(EnumGolemTrait trait) {
      if (trait.opposite != null && this.traitCache.contains(trait.opposite)) {
         this.traitCache.remove(trait.opposite);
      } else {
         this.traitCache.add(trait);
      }

   }

   public boolean hasTrait(EnumGolemTrait trait) {
      return this.getTraits().contains(trait);
   }

   public void setMaterial(GolemMaterial mat) {
      this.data = ThaumcraftApiHelper.setByteInLong(this.data, mat.id, 0);
      this.traitCache = null;
   }

   public GolemMaterial getMaterial() {
      return GolemMaterial.getMaterials()[ThaumcraftApiHelper.getByteInLong(this.data, 0)];
   }

   public void setHead(GolemHead mat) {
      this.data = ThaumcraftApiHelper.setByteInLong(this.data, mat.id, 1);
      this.traitCache = null;
   }

   public GolemHead getHead() {
      return GolemHead.getHeads()[ThaumcraftApiHelper.getByteInLong(this.data, 1)];
   }

   public void setArms(GolemArm mat) {
      this.data = ThaumcraftApiHelper.setByteInLong(this.data, mat.id, 2);
      this.traitCache = null;
   }

   public GolemArm getArms() {
      return GolemArm.getArms()[ThaumcraftApiHelper.getByteInLong(this.data, 2)];
   }

   public void setLegs(GolemLeg mat) {
      this.data = ThaumcraftApiHelper.setByteInLong(this.data, mat.id, 3);
      this.traitCache = null;
   }

   public GolemLeg getLegs() {
      return GolemLeg.getLegs()[ThaumcraftApiHelper.getByteInLong(this.data, 3)];
   }

   public void setAddon(GolemAddon mat) {
      this.data = ThaumcraftApiHelper.setByteInLong(this.data, mat.id, 4);
      this.traitCache = null;
   }

   public GolemAddon getAddon() {
      return GolemAddon.getAddons()[ThaumcraftApiHelper.getByteInLong(this.data, 4)];
   }

   public void setRank(int rank) {
      this.data = ThaumcraftApiHelper.setByteInLong(this.data, (byte)rank, 5);
   }

   public int getRank() {
      return ThaumcraftApiHelper.getByteInLong(this.data, 5);
   }

   public static IGolemProperties fromLong(long d) {
      GolemProperties out = new GolemProperties();
      out.data = d;
      return out;
   }

   public long toLong() {
      return this.data;
   }

   public ItemStack[] generateComponents() {
      ArrayList<ItemStack> comps = new ArrayList();
      ItemStack base = this.getMaterial().componentBase;
      ItemStack mech = this.getMaterial().componentMechanism;
      addToList(comps, base, 2);
      addToList(comps, mech, 1);
      addToListFromComps(comps, this.getArms().components, this.getMaterial());
      addToListFromComps(comps, this.getLegs().components, this.getMaterial());
      addToListFromComps(comps, this.getHead().components, this.getMaterial());
      addToListFromComps(comps, this.getAddon().components, this.getMaterial());
      return (ItemStack[])comps.toArray(new ItemStack[0]);
   }

   private static void addToListFromComps(ArrayList<ItemStack> comps, Object[] objs, GolemMaterial mat) {
      Object[] var3 = objs;
      int var4 = objs.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object o = var3[var5];
         if (o instanceof ItemStack) {
            addToList(comps, (ItemStack)o, 1);
         } else if (o instanceof String) {
            String s = (String)o;
            if (s.equalsIgnoreCase("base")) {
               addToList(comps, mat.componentBase, 1);
            } else if (s.equalsIgnoreCase("mech")) {
               addToList(comps, mat.componentMechanism, 1);
            }
         }
      }

   }

   private static void addToList(ArrayList<ItemStack> comps, ItemStack newItem, int mult) {
      Iterator var3 = comps.iterator();

      ItemStack stack;
      do {
         if (!var3.hasNext()) {
            ItemStack stack = newItem.func_77946_l();
            stack.field_77994_a *= mult;
            comps.add(stack);
            return;
         }

         stack = (ItemStack)var3.next();
      } while(!stack.func_77969_a(newItem) || !ItemStack.func_77970_a(stack, newItem));

      stack.field_77994_a += newItem.field_77994_a * mult;
   }

   static {
      GolemMaterial.register(new GolemMaterial("WOOD", new String[]{"MATSTUDWOOD"}, new ResourceLocation("thaumcraft", "textures/models/golems/mat_wood.png"), 5059370, 6, 2, 1, new ItemStack(BlocksTC.plank, 1, 0), new ItemStack(ItemsTC.gear), new EnumGolemTrait[]{EnumGolemTrait.LIGHT}));
      GolemMaterial.register(new GolemMaterial("IRON", new String[]{"MATSTUDIRON"}, new ResourceLocation("thaumcraft", "textures/models/golems/mat_iron.png"), 16777215, 20, 8, 3, new ItemStack(ItemsTC.plate, 1, 1), new ItemStack(ItemsTC.gear), new EnumGolemTrait[]{EnumGolemTrait.HEAVY, EnumGolemTrait.FIREPROOF, EnumGolemTrait.BLASTPROOF}));
      GolemMaterial.register(new GolemMaterial("CLAY", new String[]{"MATSTUDCLAY"}, new ResourceLocation("thaumcraft", "textures/models/golems/mat_clay.png"), 13071447, 10, 4, 2, new ItemStack(Blocks.field_150405_ch, 1, 0), new ItemStack(ItemsTC.gear), new EnumGolemTrait[]{EnumGolemTrait.FIREPROOF}));
      GolemMaterial.register(new GolemMaterial("BRASS", new String[]{"MATSTUDBRASS"}, new ResourceLocation("thaumcraft", "textures/models/golems/mat_brass.png"), 15638812, 16, 6, 3, new ItemStack(ItemsTC.plate, 1, 0), new ItemStack(ItemsTC.gear), new EnumGolemTrait[]{EnumGolemTrait.LIGHT}));
      GolemMaterial.register(new GolemMaterial("THAUMIUM", new String[]{"MATSTUDTHAUMIUM"}, new ResourceLocation("thaumcraft", "textures/models/golems/mat_thaumium.png"), 5257074, 24, 10, 4, new ItemStack(ItemsTC.plate, 1, 2), new ItemStack(ItemsTC.gear, 1, 1), new EnumGolemTrait[]{EnumGolemTrait.HEAVY, EnumGolemTrait.FIREPROOF, EnumGolemTrait.BLASTPROOF}));
      GolemMaterial.register(new GolemMaterial("VOID", new String[]{"MATSTUDVOID"}, new ResourceLocation("thaumcraft", "textures/models/golems/mat_void.png"), 1445161, 20, 6, 4, new ItemStack(ItemsTC.plate, 1, 3), new ItemStack(ItemsTC.gear, 1, 2), new EnumGolemTrait[]{EnumGolemTrait.REPAIR}));
      GolemHead.register(new GolemHead("BASIC", new String[]{"MINDCLOCKWORK"}, new ResourceLocation("thaumcraft", "textures/misc/golem/head_basic.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_head_basic.obj"), (ResourceLocation)null, PartModel.EnumAttachPoint.HEAD), new Object[]{new ItemStack(ItemsTC.mind, 1, 0)}, new EnumGolemTrait[0]));
      GolemHead.register(new GolemHead("SMART", new String[]{"MINDBIOTHAUMIC"}, new ResourceLocation("thaumcraft", "textures/misc/golem/head_smart.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_head_smart.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_head_other.png"), PartModel.EnumAttachPoint.HEAD), new Object[]{new ItemStack(ItemsTC.mind, 1, 1)}, new EnumGolemTrait[]{EnumGolemTrait.SMART, EnumGolemTrait.FRAGILE}));
      GolemHead.register(new GolemHead("SMART_ARMORED", new String[]{"MINDBIOTHAUMIC", "GOLEMCOMBATADV"}, new ResourceLocation("thaumcraft", "textures/misc/golem/head_smartarmor.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_head_smart_armor.obj"), (ResourceLocation)null, PartModel.EnumAttachPoint.HEAD), new Object[]{new ItemStack(ItemsTC.mind, 1, 1), new ItemStack(ItemsTC.plate), "base", new ItemStack(Blocks.field_150325_L)}, new EnumGolemTrait[]{EnumGolemTrait.SMART}));
      GolemHead.register(new GolemHead("SCOUT", new String[]{"GOLEMVISION"}, new ResourceLocation("thaumcraft", "textures/misc/golem/head_scout.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_head_scout.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_head_other.png"), PartModel.EnumAttachPoint.HEAD), new Object[]{new ItemStack(ItemsTC.mind, 1, 0), new ItemStack(ItemsTC.modules)}, new EnumGolemTrait[]{EnumGolemTrait.SCOUT, EnumGolemTrait.FRAGILE}));
      GolemHead.register(new GolemHead("SMART_SCOUT", new String[]{"GOLEMVISION", "MINDBIOTHAUMIC"}, new ResourceLocation("thaumcraft", "textures/misc/golem/head_smartscout.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_head_scout_smart.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_head_other.png"), PartModel.EnumAttachPoint.HEAD), new Object[]{new ItemStack(ItemsTC.mind, 1, 1), new ItemStack(ItemsTC.modules)}, new EnumGolemTrait[]{EnumGolemTrait.SCOUT, EnumGolemTrait.SMART, EnumGolemTrait.FRAGILE}));
      GolemArm.register(new GolemArm("BASIC", new String[]{"MINDCLOCKWORK"}, new ResourceLocation("thaumcraft", "textures/misc/golem/arms_basic.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_arms_basic.obj"), (ResourceLocation)null, PartModel.EnumAttachPoint.ARMS), new Object[0], new EnumGolemTrait[0]));
      GolemArm.register(new GolemArm("FINE", new String[]{"MATSTUDBRASS"}, new ResourceLocation("thaumcraft", "textures/misc/golem/arms_fine.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_arms_fine.obj"), (ResourceLocation)null, PartModel.EnumAttachPoint.ARMS), new Object[]{new ItemStack(ItemsTC.gear, 2), "base"}, new EnumGolemTrait[]{EnumGolemTrait.DEFT, EnumGolemTrait.FRAGILE}));
      GolemArm.register(new GolemArm("CLAWS", new String[]{"GOLEMCOMBATADV"}, new ResourceLocation("thaumcraft", "textures/misc/golem/arms_claws.png"), new PartModelClaws(new ResourceLocation("thaumcraft", "models/obj/golem_arms_claws.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_arms_claws.png"), PartModel.EnumAttachPoint.ARMS), new Object[]{new ItemStack(ItemsTC.modules, 1, 1), new ItemStack(Items.field_151097_aZ, 2), "base"}, new EnumGolemTrait[]{EnumGolemTrait.FIGHTER, EnumGolemTrait.CLUMSY, EnumGolemTrait.BRUTAL}));
      GolemArm.register(new GolemArm("BREAKERS", new String[]{"GOLEMBREAKER"}, new ResourceLocation("thaumcraft", "textures/misc/golem/arms_breakers.png"), new PartModelBreakers(new ResourceLocation("thaumcraft", "models/obj/golem_arms_breakers.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_arms_breakers.png"), PartModel.EnumAttachPoint.ARMS), new Object[]{new ItemStack(Items.field_151045_i, 2), "base", new ItemStack(Blocks.field_150331_J, 2)}, new EnumGolemTrait[]{EnumGolemTrait.BREAKER, EnumGolemTrait.CLUMSY, EnumGolemTrait.BRUTAL}));
      GolemArm.register(new GolemArm("DARTS", new String[]{"GOLEMCOMBATADV"}, new ResourceLocation("thaumcraft", "textures/misc/golem/arms_darts.png"), new PartModelDarts(new ResourceLocation("thaumcraft", "models/obj/golem_arms_darter.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_arms_darter.png"), PartModel.EnumAttachPoint.ARMS), new Object[]{new ItemStack(ItemsTC.modules, 1, 1), new ItemStack(Blocks.field_150367_z, 2), new ItemStack(Items.field_151032_g, 32), "mech"}, new GolemArmDart(), new EnumGolemTrait[]{EnumGolemTrait.FIGHTER, EnumGolemTrait.CLUMSY, EnumGolemTrait.RANGED, EnumGolemTrait.FRAGILE}));
      GolemLeg.register(new GolemLeg("WALKER", new String[]{"MINDCLOCKWORK"}, new ResourceLocation("thaumcraft", "textures/misc/golem/legs_walker.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_legs_walker.obj"), (ResourceLocation)null, PartModel.EnumAttachPoint.LEGS), new Object[]{"base", "mech"}, new EnumGolemTrait[0]));
      GolemLeg.register(new GolemLeg("ROLLER", new String[]{"MINDCLOCKWORK"}, new ResourceLocation("thaumcraft", "textures/misc/golem/legs_roller.png"), new PartModelWheel(new ResourceLocation("thaumcraft", "models/obj/golem_legs_wheel.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_legs_wheel.png"), PartModel.EnumAttachPoint.BODY), new Object[]{new ItemStack(Items.field_151054_z, 2), new ItemStack(Items.field_151116_aA), "mech"}, new GolemLegWheels(), new EnumGolemTrait[]{EnumGolemTrait.WHEELED}));
      GolemLeg.register(new GolemLeg("CLIMBER", new String[]{"GOLEMCLIMBER"}, new ResourceLocation("thaumcraft", "textures/misc/golem/legs_climber.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_legs_climber.obj"), new ResourceLocation("thaumcraft", "textures/blocks/base_metal.png"), PartModel.EnumAttachPoint.LEGS), new Object[]{new ItemStack(Items.field_151145_ak, 4), "base", "mech", "mech"}, new EnumGolemTrait[]{EnumGolemTrait.CLIMBER}));
      GolemLeg.register(new GolemLeg("FLYER", new String[]{"GOLEMFLYER"}, new ResourceLocation("thaumcraft", "textures/misc/golem/legs_flyer.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_legs_floater.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_legs_floater.png"), PartModel.EnumAttachPoint.BODY), new Object[]{new ItemStack(BlocksTC.levitator), new ItemStack(ItemsTC.plate, 4), new ItemStack(Items.field_151123_aH), "mech"}, new GolemLegLevitator(), new EnumGolemTrait[]{EnumGolemTrait.FLYER, EnumGolemTrait.FRAGILE}));
      GolemAddon.register(new GolemAddon("NONE", new String[]{"MINDCLOCKWORK"}, new ResourceLocation("thaumcraft", "textures/blocks/blank.png"), (PartModel)null, new Object[0], new EnumGolemTrait[0]));
      GolemAddon.register(new GolemAddon("ARMORED", new String[]{"GOLEMCOMBATADV"}, new ResourceLocation("thaumcraft", "textures/misc/golem/addon_armored.png"), new PartModel(new ResourceLocation("thaumcraft", "models/obj/golem_armor.obj"), (ResourceLocation)null, PartModel.EnumAttachPoint.BODY), new Object[]{"base", "base", "base", "base"}, new EnumGolemTrait[]{EnumGolemTrait.ARMORED, EnumGolemTrait.HEAVY}));
      GolemAddon.register(new GolemAddon("FIGHTER", new String[]{"SEALGUARD"}, new ResourceLocation("thaumcraft", "textures/misc/golem/addon_fighter.png"), (PartModel)null, new Object[]{new ItemStack(ItemsTC.modules, 1, 1), "mech"}, new EnumGolemTrait[]{EnumGolemTrait.FIGHTER}));
      GolemAddon.register(new GolemAddon("HAULER", new String[]{"MINDCLOCKWORK"}, new ResourceLocation("thaumcraft", "textures/misc/golem/addon_hauler.png"), new PartModelHauler(new ResourceLocation("thaumcraft", "models/obj/golem_hauler.obj"), new ResourceLocation("thaumcraft", "textures/models/golems/golem_hauler.png"), PartModel.EnumAttachPoint.BODY), new Object[]{new ItemStack(Items.field_151116_aA), new ItemStack(Blocks.field_150486_ae)}, new EnumGolemTrait[]{EnumGolemTrait.HAULER}));
   }
}
