package thaumcraft.api.golems;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.tasks.Task;

public class GolemHelper {
   public static HashMap<Integer, ArrayList<ProvisionRequest>> provisionRequests = new HashMap();

   public static void registerSeal(ISeal seal) {
      ThaumcraftApi.internalMethods.registerSeal(seal);
   }

   public static ISeal getSeal(String key) {
      return ThaumcraftApi.internalMethods.getSeal(key);
   }

   public static ItemStack getSealStack(String key) {
      return ThaumcraftApi.internalMethods.getSealStack(key);
   }

   public static ISealEntity getSealEntity(int dim, SealPos pos) {
      return ThaumcraftApi.internalMethods.getSealEntity(dim, pos);
   }

   public static void addGolemTask(int dim, Task task) {
      ThaumcraftApi.internalMethods.addGolemTask(dim, task);
   }

   public static void requestProvisioning(World world, ISealEntity seal, ItemStack stack) {
      if (!provisionRequests.containsKey(world.field_73011_w.getDimension())) {
         provisionRequests.put(world.field_73011_w.getDimension(), new ArrayList());
      }

      ArrayList<ProvisionRequest> list = (ArrayList)provisionRequests.get(world.field_73011_w.getDimension());
      ProvisionRequest pr = new ProvisionRequest(seal, stack.func_77946_l());
      if (!list.contains(pr)) {
         list.add(pr);
      }

   }

   public static BlockPos getPosInArea(ISealEntity seal, int count) {
      int xx = 1 + (seal.getArea().func_177958_n() - 1) * (seal.getSealPos().face.func_82601_c() == 0 ? 2 : 1);
      int yy = 1 + (seal.getArea().func_177956_o() - 1) * (seal.getSealPos().face.func_96559_d() == 0 ? 2 : 1);
      int zz = 1 + (seal.getArea().func_177952_p() - 1) * (seal.getSealPos().face.func_82599_e() == 0 ? 2 : 1);
      int qx = seal.getSealPos().face.func_82601_c() != 0 ? seal.getSealPos().face.func_82601_c() : 1;
      int qy = seal.getSealPos().face.func_96559_d() != 0 ? seal.getSealPos().face.func_96559_d() : 1;
      int qz = seal.getSealPos().face.func_82599_e() != 0 ? seal.getSealPos().face.func_82599_e() : 1;
      int y = qy * (count / zz / xx) % yy + seal.getSealPos().face.func_96559_d();
      int x = qx * (count / zz) % xx + seal.getSealPos().face.func_82601_c();
      int z = qz * count % zz + seal.getSealPos().face.func_82599_e();
      BlockPos p = seal.getSealPos().pos.func_177982_a(x - (seal.getSealPos().face.func_82601_c() == 0 ? xx / 2 : 0), y - (seal.getSealPos().face.func_96559_d() == 0 ? yy / 2 : 0), z - (seal.getSealPos().face.func_82599_e() == 0 ? zz / 2 : 0));
      return p;
   }

   public static AxisAlignedBB getBoundsForArea(ISealEntity seal) {
      return (new AxisAlignedBB((double)seal.getSealPos().pos.func_177958_n(), (double)seal.getSealPos().pos.func_177956_o(), (double)seal.getSealPos().pos.func_177952_p(), (double)(seal.getSealPos().pos.func_177958_n() + 1), (double)(seal.getSealPos().pos.func_177956_o() + 1), (double)(seal.getSealPos().pos.func_177952_p() + 1))).func_72317_d((double)seal.getSealPos().face.func_82601_c(), (double)seal.getSealPos().face.func_96559_d(), (double)seal.getSealPos().face.func_82599_e()).func_72321_a(seal.getSealPos().face.func_82601_c() != 0 ? (double)((seal.getArea().func_177958_n() - 1) * seal.getSealPos().face.func_82601_c()) : 0.0D, seal.getSealPos().face.func_96559_d() != 0 ? (double)((seal.getArea().func_177956_o() - 1) * seal.getSealPos().face.func_96559_d()) : 0.0D, seal.getSealPos().face.func_82599_e() != 0 ? (double)((seal.getArea().func_177952_p() - 1) * seal.getSealPos().face.func_82599_e()) : 0.0D).func_72314_b(seal.getSealPos().face.func_82601_c() == 0 ? (double)(seal.getArea().func_177958_n() - 1) : 0.0D, seal.getSealPos().face.func_96559_d() == 0 ? (double)(seal.getArea().func_177956_o() - 1) : 0.0D, seal.getSealPos().face.func_82599_e() == 0 ? (double)(seal.getArea().func_177952_p() - 1) : 0.0D);
   }
}
