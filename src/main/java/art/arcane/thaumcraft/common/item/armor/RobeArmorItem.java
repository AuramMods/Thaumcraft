package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import art.arcane.thaumcraft.common.item.VisDiscountGearItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class RobeArmorItem extends ArmorItem implements VisDiscountGearItem {
    // TODO(port): Port robe dye + overlay behavior and cauldron washing parity.
    // TODO(port): Port robe texture/model parity (`robes_1`, `robes_2`, overlay variants).

    private final int visDiscountPercent;

    public RobeArmorItem(ArmorMaterial material, Type type, int visDiscountPercent, Properties properties) {
        super(material, type, properties);
        this.visDiscountPercent = visDiscountPercent;
    }

    @Override
    public int getVisDiscountPercent(ItemStack stack, ServerPlayer player) {
        return this.visDiscountPercent;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String base = slot == EquipmentSlot.LEGS ? "robes_2" : "robes_1";
        String selected = type == null ? base : base + "_overlay";
        return Thaumcraft.MODID + ":textures/models/armor/" + selected + ".png";
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }
}
