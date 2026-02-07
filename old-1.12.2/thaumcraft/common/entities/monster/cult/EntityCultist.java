package thaumcraft.common.entities.monster.cult;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.monster.boss.EntityCultistLeader;

public class EntityCultist extends EntityMob {
   public static final ResourceLocation LOOT = LootTableList.func_186375_a(new ResourceLocation("thaumcraft", "cultist"));

   public EntityCultist(World p_i1745_1_) {
      super(p_i1745_1_);
      this.func_70105_a(0.6F, 1.8F);
      this.field_70728_aV = 10;
      ((PathNavigateGround)this.func_70661_as()).func_179688_b(true);
      this.func_184642_a(EntityEquipmentSlot.CHEST, 0.05F);
      this.func_184642_a(EntityEquipmentSlot.FEET, 0.05F);
      this.func_184642_a(EntityEquipmentSlot.HEAD, 0.05F);
      this.func_184642_a(EntityEquipmentSlot.LEGS, 0.05F);
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(32.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.3D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(4.0D);
   }

   protected void func_70088_a() {
      super.func_70088_a();
   }

   public boolean func_98052_bS() {
      return false;
   }

   protected boolean func_70814_o() {
      return true;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected ResourceLocation func_184647_J() {
      return LOOT;
   }

   protected void setLoot(DifficultyInstance diff) {
   }

   protected void func_180483_b(DifficultyInstance diff) {
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      this.setLoot(diff);
      this.func_180483_b(diff);
      return super.func_180482_a(diff, data);
   }

   protected boolean func_70692_ba() {
      return true;
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (nbt.func_74764_b("HomeD")) {
         this.func_175449_a(new BlockPos(nbt.func_74762_e("HomeX"), nbt.func_74762_e("HomeY"), nbt.func_74762_e("HomeZ")), nbt.func_74762_e("HomeD"));
      }

   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      if (this.func_180486_cf() != null && this.func_110174_bM() > 0.0F) {
         nbt.func_74768_a("HomeD", (int)this.func_110174_bM());
         nbt.func_74768_a("HomeX", this.func_180486_cf().func_177958_n());
         nbt.func_74768_a("HomeY", this.func_180486_cf().func_177956_o());
         nbt.func_74768_a("HomeZ", this.func_180486_cf().func_177952_p());
      }

   }

   public boolean func_184191_r(Entity el) {
      return el instanceof EntityCultist || el instanceof EntityCultistLeader;
   }

   public boolean func_70686_a(Class clazz) {
      return clazz != EntityCultistCleric.class && clazz != EntityCultistLeader.class && clazz != EntityCultistKnight.class ? super.func_70686_a(clazz) : false;
   }

   public void func_70656_aK() {
      if (this.field_70170_p.field_72995_K) {
         for(int i = 0; i < 20; ++i) {
            double d0 = this.field_70146_Z.nextGaussian() * 0.05D;
            double d1 = this.field_70146_Z.nextGaussian() * 0.05D;
            double d2 = this.field_70146_Z.nextGaussian() * 0.05D;
            double d3 = 2.0D;
            FXDispatcher.INSTANCE.cultistSpawn(this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N + d0 * d3, this.field_70163_u + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O) + d1 * d3, this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N + d2 * d3, d0, d1, d2);
         }
      } else {
         this.field_70170_p.func_72960_a(this, (byte)20);
      }

   }
}
