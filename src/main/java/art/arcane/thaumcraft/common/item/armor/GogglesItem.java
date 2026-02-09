package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.common.item.VisDiscountGearItem;
import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class GogglesItem extends ArmorItem implements VisDiscountGearItem {
    // TODO(port): Port full goggles reveal behavior (nodes/popups) and bauble slot parity.
    // TODO(port): Port legacy goggles model/texture behavior (`goggles.png`) and repair-material parity.

    public GogglesItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public int getVisDiscountPercent(ItemStack stack, ServerPlayer player) {
        return 5;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Thaumcraft.MODID + ":textures/models/armor/goggles.png";
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }
}
