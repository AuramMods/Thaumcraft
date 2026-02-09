package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

public class LegacyTexturedArmorItem extends ArmorItem {
    private final String layer1Texture;
    @Nullable
    private final String layer2Texture;
    @Nullable
    private final Rarity rarity;

    public LegacyTexturedArmorItem(
            ArmorMaterial material,
            Type type,
            String layer1Texture,
            @Nullable String layer2Texture,
            @Nullable Rarity rarity,
            Properties properties
    ) {
        super(material, type, properties);
        this.layer1Texture = layer1Texture;
        this.layer2Texture = layer2Texture;
        this.rarity = rarity;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String selected = slot == EquipmentSlot.LEGS && this.layer2Texture != null ? this.layer2Texture : this.layer1Texture;
        return Thaumcraft.MODID + ":textures/models/armor/" + selected + ".png";
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.rarity == null ? super.getRarity(stack) : this.rarity;
    }
}
