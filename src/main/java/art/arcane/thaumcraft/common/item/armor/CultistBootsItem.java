package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.common.item.WarpingGearItem;
import art.arcane.thaumcraft.common.item.VisDiscountGearItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class CultistBootsItem extends ArmorItem implements WarpingGearItem, VisDiscountGearItem {
    // TODO(port): Wire cultist armor model/texture parity (`cultistboots`) after armor model pipeline is in place.

    public CultistBootsItem(ArmorMaterial material, Type type, Properties properties) {
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
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.IRON_INGOT) || super.isValidRepairItem(stack, repairCandidate);
    }
}
