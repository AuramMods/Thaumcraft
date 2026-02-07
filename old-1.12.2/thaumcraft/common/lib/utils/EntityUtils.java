package thaumcraft.common.lib.utils;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.mods.ChampionModifier;

public class EntityUtils {
   public static final IAttribute CHAMPION_MOD = (new RangedAttribute((IAttribute)null, "tc.mobmod", -2.0D, -2.0D, 100.0D)).func_111117_a("Champion modifier").func_111112_a(true);
   public static final AttributeModifier CHAMPION_HEALTH = new AttributeModifier(UUID.fromString("a62bef38-48cc-42a6-ac5e-ef913841c4fd"), "Champion health buff", 100.0D, 0);
   public static final AttributeModifier CHAMPION_DAMAGE = new AttributeModifier(UUID.fromString("a340d2db-d881-4c25-ac62-f0ad14cd63b0"), "Champion damage buff", 2.0D, 2);
   public static final AttributeModifier BOLDBUFF = new AttributeModifier(UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037"), "Bold speed boost", 0.3D, 1);
   public static final AttributeModifier MIGHTYBUFF = new AttributeModifier(UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3"), "Mighty damage boost", 2.0D, 2);
   public static final AttributeModifier[] HPBUFF = new AttributeModifier[]{new AttributeModifier(UUID.fromString("54d621c1-dd4d-4b43-8bd2-5531c8875797"), "HEALTH BUFF 1", 50.0D, 0), new AttributeModifier(UUID.fromString("f51257dc-b7fa-4f7a-92d7-75d68e8592c4"), "HEALTH BUFF 2", 50.0D, 0), new AttributeModifier(UUID.fromString("3d6b2e42-4141-4364-b76d-0e8664bbd0bb"), "HEALTH BUFF 3", 50.0D, 0), new AttributeModifier(UUID.fromString("02c97a08-801c-4131-afa2-1427a6151934"), "HEALTH BUFF 4", 50.0D, 0), new AttributeModifier(UUID.fromString("0f354f6a-33c5-40be-93be-81b1338567f1"), "HEALTH BUFF 5", 50.0D, 0)};
   public static final AttributeModifier[] DMGBUFF = new AttributeModifier[]{new AttributeModifier(UUID.fromString("534f8c57-929a-48cf-bbd6-0fd851030748"), "DAMAGE BUFF 1", 0.5D, 0), new AttributeModifier(UUID.fromString("d317a76e-0e7c-4c61-acfd-9fa286053b32"), "DAMAGE BUFF 2", 0.5D, 0), new AttributeModifier(UUID.fromString("ff462d63-26a2-4363-830e-143ed97e2a4f"), "DAMAGE BUFF 3", 0.5D, 0), new AttributeModifier(UUID.fromString("cf1eb39e-0c67-495f-887c-0d3080828d2f"), "DAMAGE BUFF 4", 0.5D, 0), new AttributeModifier(UUID.fromString("3cfab9da-2701-43d8-ac07-885f16fa4117"), "DAMAGE BUFF 5", 0.5D, 0)};

   public static Vec3d posToHand(Entity e, EnumHand hand) {
      double px = e.field_70165_t;
      double py = e.func_174813_aQ().field_72338_b + (double)(e.field_70131_O / 2.0F) + 0.25D;
      double pz = e.field_70161_v;
      float m = hand == EnumHand.MAIN_HAND ? 0.0F : 180.0F;
      px += (double)(-MathHelper.func_76134_b((e.field_70177_z + m) / 180.0F * 3.141593F) * 0.3F);
      pz += (double)(-MathHelper.func_76126_a((e.field_70177_z + m) / 180.0F * 3.141593F) * 0.3F);
      Vec3d vec3d = e.func_70676_i(1.0F);
      px += vec3d.field_72450_a * 0.3D;
      py += vec3d.field_72448_b * 0.3D;
      pz += vec3d.field_72449_c * 0.3D;
      return new Vec3d(px, py, pz);
   }

   public static boolean hasGoggles(Entity e) {
      if (!(e instanceof EntityPlayer)) {
         return false;
      } else {
         EntityPlayer viewer = (EntityPlayer)e;
         if (viewer.func_184614_ca() != null && viewer.func_184614_ca().func_77973_b() instanceof IGoggles && showPopups(viewer.func_184614_ca(), viewer)) {
            return true;
         } else {
            for(int a = 0; a < 4; ++a) {
               if (viewer.field_71071_by.field_70460_b[a] != null && viewer.field_71071_by.field_70460_b[a].func_77973_b() instanceof IGoggles && showPopups(viewer.field_71071_by.field_70460_b[a], viewer)) {
                  return true;
               }
            }

            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(viewer);

            for(int a = 0; a < baubles.getSlots(); ++a) {
               if (baubles.getStackInSlot(a) != null && baubles.getStackInSlot(a).func_77973_b() instanceof IGoggles && showPopups(baubles.getStackInSlot(a), viewer)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static boolean showPopups(ItemStack stack, EntityPlayer player) {
      return ((IGoggles)((IGoggles)stack.func_77973_b())).showIngamePopups(stack, player);
   }

   public static boolean hasRevealer(Entity e) {
      if (!(e instanceof EntityPlayer)) {
         return false;
      } else {
         EntityPlayer viewer = (EntityPlayer)e;
         if (viewer.func_184614_ca() != null && viewer.func_184614_ca().func_77973_b() instanceof IRevealer && reveals(viewer.func_184614_ca(), viewer)) {
            return true;
         } else if (viewer.func_184592_cb() != null && viewer.func_184592_cb().func_77973_b() instanceof IRevealer && reveals(viewer.func_184592_cb(), viewer)) {
            return true;
         } else {
            for(int a = 0; a < 4; ++a) {
               if (viewer.field_71071_by.field_70460_b[a] != null && viewer.field_71071_by.field_70460_b[a].func_77973_b() instanceof IRevealer && reveals(viewer.field_71071_by.field_70460_b[a], viewer)) {
                  return true;
               }
            }

            IInventory baubles = BaublesApi.getBaubles(viewer);

            for(int a = 0; a < baubles.func_70302_i_(); ++a) {
               if (baubles.func_70301_a(a) != null && baubles.func_70301_a(a).func_77973_b() instanceof IRevealer && reveals(baubles.func_70301_a(a), viewer)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static boolean reveals(ItemStack stack, EntityPlayer player) {
      return ((IRevealer)((IRevealer)stack.func_77973_b())).showNodes(stack, player);
   }

   public static Entity getPointedEntity(World world, Entity entityplayer, double minrange, double range, float padding) {
      return getPointedEntity(world, entityplayer, minrange, range, padding, false);
   }

   public static Entity getPointedEntity(World world, Entity entityplayer, double minrange, double range, float padding, boolean nonCollide) {
      Entity pointedEntity = null;
      Vec3d vec3d = new Vec3d(entityplayer.field_70165_t, entityplayer.field_70163_u + (double)entityplayer.func_70047_e(), entityplayer.field_70161_v);
      Vec3d vec3d1 = entityplayer.func_70040_Z();
      Vec3d vec3d2 = vec3d.func_72441_c(vec3d1.field_72450_a * range, vec3d1.field_72448_b * range, vec3d1.field_72449_c * range);
      List list = world.func_72839_b(entityplayer, entityplayer.func_174813_aQ().func_72321_a(vec3d1.field_72450_a * range, vec3d1.field_72448_b * range, vec3d1.field_72449_c * range).func_72314_b((double)padding, (double)padding, (double)padding));
      double d2 = 0.0D;

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (!((double)entity.func_70032_d(entityplayer) < minrange) && (entity.func_70067_L() || nonCollide) && world.func_147447_a(new Vec3d(entityplayer.field_70165_t, entityplayer.field_70163_u + (double)entityplayer.func_70047_e(), entityplayer.field_70161_v), new Vec3d(entity.field_70165_t, entity.field_70163_u + (double)entity.func_70047_e(), entity.field_70161_v), false, true, false) == null) {
            float f2 = Math.max(0.8F, entity.func_70111_Y());
            AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)f2, (double)f2, (double)f2);
            RayTraceResult RayTraceResult = axisalignedbb.func_72327_a(vec3d, vec3d2);
            if (axisalignedbb.func_72318_a(vec3d)) {
               if (0.0D < d2 || d2 == 0.0D) {
                  pointedEntity = entity;
                  d2 = 0.0D;
               }
            } else if (RayTraceResult != null) {
               double d3 = vec3d.func_72438_d(RayTraceResult.field_72307_f);
               if (d3 < d2 || d2 == 0.0D) {
                  pointedEntity = entity;
                  d2 = d3;
               }
            }
         }
      }

      return pointedEntity;
   }

   public static Entity getPointedEntity(World world, EntityLivingBase player, double range, Class<?> clazz) {
      Entity pointedEntity = null;
      Vec3d vec3d = new Vec3d(player.field_70165_t, player.field_70163_u + (double)player.func_70047_e(), player.field_70161_v);
      Vec3d vec3d1 = player.func_70040_Z();
      Vec3d vec3d2 = vec3d.func_72441_c(vec3d1.field_72450_a * range, vec3d1.field_72448_b * range, vec3d1.field_72449_c * range);
      float f1 = 1.1F;
      List list = world.func_72839_b(player, player.func_174813_aQ().func_72321_a(vec3d1.field_72450_a * range, vec3d1.field_72448_b * range, vec3d1.field_72449_c * range).func_72314_b((double)f1, (double)f1, (double)f1));
      double d2 = 0.0D;

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (entity.func_70067_L() && world.func_147447_a(new Vec3d(player.field_70165_t, player.field_70163_u + (double)player.func_70047_e(), player.field_70161_v), new Vec3d(entity.field_70165_t, entity.field_70163_u + (double)entity.func_70047_e(), entity.field_70161_v), false, true, false) == null && !clazz.isInstance(entity)) {
            float f2 = Math.max(0.8F, entity.func_70111_Y());
            AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)f2, (double)f2, (double)f2);
            RayTraceResult RayTraceResult = axisalignedbb.func_72327_a(vec3d, vec3d2);
            if (axisalignedbb.func_72318_a(vec3d)) {
               if (0.0D < d2 || d2 == 0.0D) {
                  pointedEntity = entity;
                  d2 = 0.0D;
               }
            } else if (RayTraceResult != null) {
               double d3 = vec3d.func_72438_d(RayTraceResult.field_72307_f);
               if (d3 < d2 || d2 == 0.0D) {
                  pointedEntity = entity;
                  d2 = d3;
               }
            }
         }
      }

      return pointedEntity;
   }

   public static boolean canEntityBeSeen(Entity entity, TileEntity te) {
      return te.func_145831_w().func_72901_a(new Vec3d((double)te.func_174877_v().func_177958_n() + 0.5D, (double)te.func_174877_v().func_177956_o() + 1.25D, (double)te.func_174877_v().func_177952_p() + 0.5D), new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v), false) == null;
   }

   public static boolean canEntityBeSeen(Entity entity, double x, double y, double z) {
      return entity.field_70170_p.func_72901_a(new Vec3d(x, y, z), new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v), false) == null;
   }

   public static boolean canEntityBeSeen(Entity entity, Entity entity2) {
      return entity.field_70170_p.func_72901_a(new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v), new Vec3d(entity2.field_70165_t, entity2.field_70163_u, entity2.field_70161_v), false) == null;
   }

   public static void resetFloatCounter(EntityPlayerMP player) {
      player.field_71135_a.field_147365_f = 0;
   }

   public static <T extends Entity> List<T> getEntitiesInRange(World world, BlockPos pos, Entity entity, Class<? extends T> classEntity, double range) {
      return getEntitiesInRange(world, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, entity, classEntity, range);
   }

   public static <T extends Entity> List<T> getEntitiesInRange(World world, double x, double y, double z, Entity entity, Class<? extends T> classEntity, double range) {
      ArrayList<T> out = new ArrayList();
      List list = world.func_72872_a(classEntity, (new AxisAlignedBB(x, y, z, x, y, z)).func_72314_b(range, range, range));
      if (list.size() > 0) {
         Iterator var13 = list.iterator();

         while(true) {
            Entity ent;
            do {
               if (!var13.hasNext()) {
                  return out;
               }

               Object e = var13.next();
               ent = (Entity)e;
            } while(entity != null && entity.func_145782_y() == ent.func_145782_y());

            out.add(ent);
         }
      } else {
         return out;
      }
   }

   public static boolean isVisibleTo(float fov, Entity ent, Entity ent2, float range) {
      double[] x = new double[]{ent2.field_70165_t, ent2.func_174813_aQ().field_72338_b + (double)(ent2.field_70131_O / 2.0F), ent2.field_70161_v};
      double[] t = new double[]{ent.field_70165_t, ent.func_174813_aQ().field_72338_b + (double)ent.func_70047_e(), ent.field_70161_v};
      Vec3d q = ent.func_70040_Z();
      q = new Vec3d(q.field_72450_a * (double)range, q.field_72448_b * (double)range, q.field_72449_c * (double)range);
      Vec3d l = q.func_72441_c(ent.field_70165_t, ent.func_174813_aQ().field_72338_b + (double)ent.func_70047_e(), ent.field_70161_v);
      double[] b = new double[]{l.field_72450_a, l.field_72448_b, l.field_72449_c};
      return Utils.isLyingInCone(x, t, b, fov);
   }

   public static boolean isVisibleTo(float fov, Entity ent, double xx, double yy, double zz, float range) {
      double[] x = new double[]{xx, yy, zz};
      double[] t = new double[]{ent.field_70165_t, ent.func_174813_aQ().field_72338_b + (double)ent.func_70047_e(), ent.field_70161_v};
      Vec3d q = ent.func_70040_Z();
      q = new Vec3d(q.field_72450_a * (double)range, q.field_72448_b * (double)range, q.field_72449_c * (double)range);
      Vec3d l = q.func_72441_c(ent.field_70165_t, ent.func_174813_aQ().field_72338_b + (double)ent.func_70047_e(), ent.field_70161_v);
      double[] b = new double[]{l.field_72450_a, l.field_72448_b, l.field_72449_c};
      return Utils.isLyingInCone(x, t, b, fov);
   }

   public static EntityItem entityDropSpecialItem(Entity entity, ItemStack stack, float dropheight) {
      if (stack.field_77994_a != 0 && stack.func_77973_b() != null) {
         EntitySpecialItem entityitem = new EntitySpecialItem(entity.field_70170_p, entity.field_70165_t, entity.field_70163_u + (double)dropheight, entity.field_70161_v, stack);
         entityitem.func_174869_p();
         entityitem.field_70181_x = 0.10000000149011612D;
         entityitem.field_70159_w = 0.0D;
         entityitem.field_70179_y = 0.0D;
         if (entity.captureDrops) {
            entity.capturedDrops.add(entityitem);
         } else {
            entity.field_70170_p.func_72838_d(entityitem);
         }

         return entityitem;
      } else {
         return null;
      }
   }

   public static void makeChampion(EntityMob entity, boolean persist) {
      try {
         if (entity.func_110148_a(CHAMPION_MOD).func_111126_e() > -2.0D) {
            return;
         }
      } catch (Exception var7) {
         return;
      }

      int type = entity.field_70170_p.field_73012_v.nextInt(ChampionModifier.mods.length);
      if (entity instanceof EntityCreeper) {
         type = 0;
      }

      IAttributeInstance modai = entity.func_110148_a(CHAMPION_MOD);
      modai.func_111124_b(ChampionModifier.mods[type].attributeMod);
      modai.func_111121_a(ChampionModifier.mods[type].attributeMod);
      IAttributeInstance sai;
      IAttributeInstance mai;
      if (!(entity instanceof EntityThaumcraftBoss)) {
         sai = entity.func_110148_a(SharedMonsterAttributes.field_111267_a);
         sai.func_111124_b(CHAMPION_HEALTH);
         sai.func_111121_a(CHAMPION_HEALTH);
         mai = entity.func_110148_a(SharedMonsterAttributes.field_111264_e);
         mai.func_111124_b(CHAMPION_DAMAGE);
         mai.func_111121_a(CHAMPION_DAMAGE);
         entity.func_70691_i(25.0F);
         entity.func_96094_a(ChampionModifier.mods[type].getModNameLocalized() + " " + entity.func_70005_c_());
      } else {
         ((EntityThaumcraftBoss)entity).generateName();
      }

      if (persist) {
         entity.func_110163_bv();
      }

      switch(type) {
      case 0:
         sai = entity.func_110148_a(SharedMonsterAttributes.field_111263_d);
         sai.func_111124_b(BOLDBUFF);
         sai.func_111121_a(BOLDBUFF);
         break;
      case 3:
         mai = entity.func_110148_a(SharedMonsterAttributes.field_111264_e);
         mai.func_111124_b(MIGHTYBUFF);
         mai.func_111121_a(MIGHTYBUFF);
         break;
      case 5:
         int bh = (int)entity.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() / 2;
         entity.func_110149_m(entity.func_110139_bj() + (float)bh);
      }

   }
}
