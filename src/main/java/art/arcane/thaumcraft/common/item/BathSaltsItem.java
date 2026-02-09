package art.arcane.thaumcraft.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BathSaltsItem extends Item {
    // Legacy ItemBathSalts expires quickly so dropped stacks can convert water into purifying fluid.
    private static final int ENTITY_LIFESPAN_TICKS = 200;

    public BathSaltsItem() {
        super(new Properties());
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return ENTITY_LIFESPAN_TICKS;
    }
}
