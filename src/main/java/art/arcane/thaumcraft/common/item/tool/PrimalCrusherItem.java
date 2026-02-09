package art.arcane.thaumcraft.common.item.tool;

import art.arcane.thaumcraft.common.item.ThaumcraftItemBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class PrimalCrusherItem extends PickaxeItem {
    // TODO(port): replace this baseline with dedicated dual-tool mining logic + persistent infusion enchant behavior.

    public PrimalCrusherItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        ThaumcraftItemBehaviors.selfRepair(stack, level, entity, 20);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return super.canPerformAction(stack, toolAction)
                || toolAction == ToolActions.SHOVEL_DIG
                || toolAction == ToolActions.SHOVEL_FLATTEN;
    }
}
