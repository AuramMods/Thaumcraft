package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.common.item.ThaumcraftItemBehaviors;
import art.arcane.thaumcraft.common.item.VisDiscountGearItem;
import art.arcane.thaumcraft.common.item.WarpingGearItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidArmorItem extends ArmorItem implements WarpingGearItem, VisDiscountGearItem {
    // TODO(port): Add legacy revealer behavior where applicable by armor family.
    // TODO(port): Wire legacy repair material and texture/model parity once dedicated armor classes/materials are ported.

    private final int warpValue;
    private final int visDiscountPercent;

    public VoidArmorItem(ArmorMaterial material, Type type, int warpValue, int visDiscountPercent, Properties properties) {
        super(material, type, properties);
        this.warpValue = warpValue;
        this.visDiscountPercent = visDiscountPercent;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        ThaumcraftItemBehaviors.selfRepair(stack, level, entity, 20);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        ThaumcraftItemBehaviors.selfRepair(stack, level, player, 20);
    }

    @Override
    public int getWarp(ItemStack stack, ServerPlayer player) {
        return this.warpValue;
    }

    @Override
    public int getVisDiscountPercent(ItemStack stack, ServerPlayer player) {
        return this.visDiscountPercent;
    }
}
