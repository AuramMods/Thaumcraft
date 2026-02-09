package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.common.item.ThaumcraftItemBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidArmorItem extends ArmorItem {
    // TODO(port): Add legacy warping/vis-discount/revealer behavior where applicable by armor family.
    // TODO(port): Wire legacy repair material and texture/model parity once dedicated armor classes/materials are ported.

    public VoidArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
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
}
