package thaumcraft.common.items.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomArmorHelper {
   protected static ModelBiped getCustomArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped model, ModelBiped model1, ModelBiped model2) {
      if (model == null) {
         EntityEquipmentSlot type = ((ItemArmor)itemStack.func_77973_b()).field_77881_a;
         if (type != EntityEquipmentSlot.CHEST && type != EntityEquipmentSlot.FEET) {
            model = model2;
         } else {
            model = model1;
         }
      }

      if (model != null) {
         model.field_78116_c.field_78806_j = armorSlot == EntityEquipmentSlot.HEAD;
         model.field_178720_f.field_78806_j = armorSlot == EntityEquipmentSlot.HEAD;
         model.field_78115_e.field_78806_j = armorSlot == EntityEquipmentSlot.CHEST || armorSlot == EntityEquipmentSlot.LEGS;
         model.field_178723_h.field_78806_j = armorSlot == EntityEquipmentSlot.CHEST;
         model.field_178724_i.field_78806_j = armorSlot == EntityEquipmentSlot.CHEST;
         model.field_178721_j.field_78806_j = armorSlot == EntityEquipmentSlot.LEGS;
         model.field_178722_k.field_78806_j = armorSlot == EntityEquipmentSlot.LEGS;
         model.field_78117_n = entityLiving.func_70093_af();
         model.field_78093_q = entityLiving.func_184218_aH();
         model.field_78091_s = entityLiving.func_70631_g_();
         ItemStack itemstack = entityLiving.func_184614_ca();
         ItemStack itemstack1 = entityLiving.func_184592_cb();
         ArmPose modelbiped$armpose = ArmPose.EMPTY;
         ArmPose modelbiped$armpose1 = ArmPose.EMPTY;
         EnumAction enumaction1;
         if (itemstack != null) {
            modelbiped$armpose = ArmPose.ITEM;
            if (entityLiving.func_184605_cv() > 0) {
               enumaction1 = itemstack.func_77975_n();
               if (enumaction1 == EnumAction.BLOCK) {
                  modelbiped$armpose = ArmPose.BLOCK;
               } else if (enumaction1 == EnumAction.BOW) {
                  modelbiped$armpose = ArmPose.BOW_AND_ARROW;
               }
            }
         }

         if (itemstack1 != null) {
            modelbiped$armpose1 = ArmPose.ITEM;
            if (entityLiving.func_184605_cv() > 0) {
               enumaction1 = itemstack1.func_77975_n();
               if (enumaction1 == EnumAction.BLOCK) {
                  modelbiped$armpose1 = ArmPose.BLOCK;
               }
            }
         }

         if (entityLiving.func_184591_cq() == EnumHandSide.RIGHT) {
            model.field_187076_m = modelbiped$armpose;
            model.field_187075_l = modelbiped$armpose1;
         } else {
            model.field_187076_m = modelbiped$armpose1;
            model.field_187075_l = modelbiped$armpose;
         }
      }

      return model;
   }
}
