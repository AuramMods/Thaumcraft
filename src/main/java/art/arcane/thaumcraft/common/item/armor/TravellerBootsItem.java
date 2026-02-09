package art.arcane.thaumcraft.common.item.armor;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TravellerBootsItem extends ArmorItem {
    // TODO(port): Replace potion-based movement assist with legacy charge-driven movement/step-height system and HUD display.
    // TODO(port): Hook into recharge mechanics when vis charging is implemented.

    public TravellerBootsItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Thaumcraft.MODID + ":textures/models/armor/bootstraveler.png";
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        if (player.isCrouching()) {
            return;
        }

        if (player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4D) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5, 0, true, false, true));
        }

        if (player.fallDistance > 0.0F) {
            player.fallDistance = Math.max(0.0F, player.fallDistance - 0.25F);
        }
    }
}
