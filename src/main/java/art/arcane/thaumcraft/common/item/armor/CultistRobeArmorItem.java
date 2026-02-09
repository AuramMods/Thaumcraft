package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.item.WarpingGearItem;
import art.arcane.thaumcraft.common.item.VisDiscountGearItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class CultistRobeArmorItem extends ArmorItem implements WarpingGearItem, VisDiscountGearItem {
    // TODO(port): Port robe model/tint/overlay behavior and cultist robe textures.

    public CultistRobeArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public int getWarp(ItemStack stack, ServerPlayer player) {
        return 1;
    }

    @Override
    public int getVisDiscountPercent(ItemStack stack, ServerPlayer player) {
        return 1;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Thaumcraft.MODID + ":textures/models/armor/cultist_robe_armor.png";
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.IRON_INGOT) || super.isValidRepairItem(stack, repairCandidate);
    }
}
