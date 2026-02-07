package thaumcraft.common.entities.ai.pech;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.SoundCategory;
import thaumcraft.common.entities.monster.EntityPech;

public class AIPechItemEntityGoto extends EntityAIBase {
   private EntityPech pech;
   private Entity targetEntity;
   float maxTargetDistance = 16.0F;
   private int count;
   private int failedPathFindingPenalty;

   public AIPechItemEntityGoto(EntityPech par1EntityCreature) {
      this.pech = par1EntityCreature;
      this.func_75248_a(3);
   }

   public boolean func_75250_a() {
      if (--this.count > 0) {
         return false;
      } else {
         double range = Double.MAX_VALUE;
         List<Entity> targets = this.pech.field_70170_p.func_72839_b(this.pech, this.pech.func_174813_aQ().func_72314_b((double)this.maxTargetDistance, (double)this.maxTargetDistance, (double)this.maxTargetDistance));
         if (targets.size() == 0) {
            return false;
         } else {
            Iterator var4 = targets.iterator();

            while(true) {
               Entity e;
               String username;
               do {
                  do {
                     do {
                        if (!var4.hasNext()) {
                           if (this.targetEntity == null) {
                              return false;
                           }

                           return true;
                        }

                        e = (Entity)var4.next();
                     } while(!(e instanceof EntityItem));
                  } while(!this.pech.canPickup(((EntityItem)e).func_92059_d()));

                  NBTTagCompound itemData = ((EntityItem)e).getEntityData();
                  username = ((EntityItem)e).func_145800_j();
               } while(username != null && username.equals("PechDrop"));

               double distance = e.func_70092_e(this.pech.field_70165_t, this.pech.field_70163_u, this.pech.field_70161_v);
               if (distance < range && distance <= (double)(this.maxTargetDistance * this.maxTargetDistance)) {
                  range = distance;
                  this.targetEntity = e;
               }
            }
         }
      }
   }

   public boolean func_75253_b() {
      return this.targetEntity == null ? false : (!this.targetEntity.func_70089_S() ? false : !this.pech.func_70661_as().func_75500_f() && this.targetEntity.func_70068_e(this.pech) < (double)(this.maxTargetDistance * this.maxTargetDistance));
   }

   public void func_75251_c() {
      this.targetEntity = null;
   }

   public void func_75249_e() {
      this.pech.func_70661_as().func_75484_a(this.pech.func_70661_as().func_75494_a(this.targetEntity), this.pech.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e() * 1.5D);
      this.count = 0;
   }

   public void func_75246_d() {
      this.pech.func_70671_ap().func_75651_a(this.targetEntity, 30.0F, 30.0F);
      if (this.pech.func_70635_at().func_75522_a(this.targetEntity) && --this.count <= 0) {
         this.count = this.failedPathFindingPenalty + 4 + this.pech.func_70681_au().nextInt(4);
         this.pech.func_70661_as().func_75497_a(this.targetEntity, this.pech.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e() * 1.5D);
         if (this.pech.func_70661_as().func_75505_d() != null) {
            PathPoint finalPathPoint = this.pech.func_70661_as().func_75505_d().func_75870_c();
            if (finalPathPoint != null && this.targetEntity.func_70092_e((double)finalPathPoint.field_75839_a, (double)finalPathPoint.field_75837_b, (double)finalPathPoint.field_75838_c) < 1.0D) {
               this.failedPathFindingPenalty = 0;
            } else {
               this.failedPathFindingPenalty += 10;
            }
         } else {
            this.failedPathFindingPenalty += 10;
         }
      }

      double distance = this.pech.func_70092_e(this.targetEntity.field_70165_t, this.targetEntity.func_174813_aQ().field_72338_b, this.targetEntity.field_70161_v);
      if (distance <= 1.5D) {
         this.count = 0;
         int am = ((EntityItem)this.targetEntity).func_92059_d().field_77994_a;
         ItemStack is = this.pech.pickupItem(((EntityItem)this.targetEntity).func_92059_d());
         if (is != null && is.field_77994_a > 0) {
            ((EntityItem)this.targetEntity).func_92058_a(is);
         } else {
            this.targetEntity.func_70106_y();
         }

         if (is == null || is.field_77994_a != am) {
            this.targetEntity.field_70170_p.func_184148_a((EntityPlayer)null, this.targetEntity.field_70165_t, this.targetEntity.field_70163_u, this.targetEntity.field_70161_v, SoundEvents.field_187638_cR, SoundCategory.NEUTRAL, 0.2F, ((this.targetEntity.field_70170_p.field_73012_v.nextFloat() - this.targetEntity.field_70170_p.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         }
      }

   }
}
