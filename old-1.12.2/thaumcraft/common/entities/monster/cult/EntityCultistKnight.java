package thaumcraft.common.entities.monster.cult;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.ai.combat.AICultistHurtByTarget;

public class EntityCultistKnight extends EntityCultist {
   public EntityCultistKnight(World p_i1745_1_) {
      super(p_i1745_1_);
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(3, new EntityAIAttackMelee(this, 1.0D, false));
      this.field_70714_bg.func_75776_a(4, new EntityAIRestrictOpenDoor(this));
      this.field_70714_bg.func_75776_a(5, new EntityAIOpenDoor(this, true));
      this.field_70714_bg.func_75776_a(6, new EntityAIMoveTowardsRestriction(this, 0.8D));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 0.8D));
      this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new AICultistHurtByTarget(this, true));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(30.0D);
   }

   protected void setLoot(DifficultyInstance diff) {
      this.func_184201_a(EntityEquipmentSlot.HEAD, new ItemStack(ItemsTC.crimsonPlateHelm));
      this.func_184201_a(EntityEquipmentSlot.CHEST, new ItemStack(ItemsTC.crimsonPlateChest));
      this.func_184201_a(EntityEquipmentSlot.LEGS, new ItemStack(ItemsTC.crimsonPlateLegs));
      this.func_184201_a(EntityEquipmentSlot.FEET, new ItemStack(ItemsTC.crimsonBoots));
      if (this.field_70146_Z.nextFloat() < (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
         int i = this.field_70146_Z.nextInt(5);
         if (i == 0) {
            this.func_184611_a(this.func_184600_cs(), new ItemStack(ItemsTC.voidSword));
            this.func_184201_a(EntityEquipmentSlot.HEAD, new ItemStack(ItemsTC.crimsonRobeHelm));
         } else {
            this.func_184611_a(this.func_184600_cs(), new ItemStack(ItemsTC.thaumiumSword));
            if (this.field_70146_Z.nextBoolean()) {
               this.func_184201_a(EntityEquipmentSlot.HEAD, (ItemStack)null);
            }
         }
      } else {
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151040_l));
      }

   }

   protected void func_180483_b(DifficultyInstance diff) {
      float f = diff.func_180170_c();
      if (this.func_184614_ca() != null && this.field_70146_Z.nextFloat() < 0.25F * f) {
         EnchantmentHelper.func_77504_a(this.field_70146_Z, this.func_184614_ca(), (int)(5.0F + f * (float)this.field_70146_Z.nextInt(18)), false);
      }

   }
}
