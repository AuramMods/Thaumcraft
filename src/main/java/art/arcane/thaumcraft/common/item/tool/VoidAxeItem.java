package art.arcane.thaumcraft.common.item.tool;

import art.arcane.thaumcraft.common.item.ThaumcraftItemBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class VoidAxeItem extends AxeItem {
    public VoidAxeItem(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        ThaumcraftItemBehaviors.selfRepair(stack, level, entity, 20);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        ThaumcraftItemBehaviors.applyWither(target, 80);
        return super.hurtEnemy(stack, target, attacker);
    }
}
