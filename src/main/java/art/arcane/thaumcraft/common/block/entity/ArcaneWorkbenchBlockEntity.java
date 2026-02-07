package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.menu.ArcaneWorkbenchMenu;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModItems;
import art.arcane.thaumcraft.common.world.aura.AuraManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ArcaneWorkbenchBlockEntity extends StationBlockEntity {

    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOT_START = 1;
    public static final int INPUT_SLOT_COUNT = 9;
    public static final int CRYSTAL_SLOT = INPUT_SLOT_START + INPUT_SLOT_COUNT;
    public static final int TOTAL_SLOTS = CRYSTAL_SLOT + 1;

    private static final String DEFAULT_CRYSTAL_ID = "crystal_ordo";
    private static final Set<String> SUPPORTED_CRYSTAL_IDS = Set.of(
            "crystal_aer",
            "crystal_aqua",
            "crystal_ignis",
            "crystal_terra",
            "crystal_ordo",
            "crystal_perditio",
            "crystal_vitium"
    );

    private static final AbstractContainerMenu DUMMY_CRAFTING_MENU = new AbstractContainerMenu(null, -1) {
        @Override
        public ItemStack quickMoveStack(Player player, int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean stillValid(Player player) {
            return false;
        }
    };

    private boolean suppressCraftingUpdates;
    private int currentVisCost;
    private String currentRequiredCrystalId = "";

    public ArcaneWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARCANE_WORKBENCH.get(), pos, state, "container.thaumcraft.arcane_workbench", TOTAL_SLOTS);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        recalculateCraftingResult();
        return new ArcaneWorkbenchMenu(containerId, playerInventory, this);
    }

    @Override
    public void serverTick() {
        incrementServerTicks();

        if (this.level instanceof ServerLevel serverLevel) {
            int vis = Mth.floor(AuraManager.getVis(serverLevel, this.worldPosition));
            setActivityValue(Math.min(200, vis));

            if ((getServerTicksValue() % 20) == 0) {
                recalculateCraftingResult();
                setChanged();
            }
        } else {
            setActivityValue(0);
        }
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = super.removeItem(slot, amount);
        if (!this.suppressCraftingUpdates && isCraftingRelevantSlot(slot) && !result.isEmpty()) {
            recalculateCraftingResult();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = super.removeItemNoUpdate(slot);
        if (!this.suppressCraftingUpdates && isCraftingRelevantSlot(slot) && !result.isEmpty()) {
            recalculateCraftingResult();
        }
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        if (!this.suppressCraftingUpdates && isCraftingRelevantSlot(slot)) {
            recalculateCraftingResult();
        }
    }

    @Override
    public void clearContent() {
        super.clearContent();
        if (!this.suppressCraftingUpdates) {
            recalculateCraftingResult();
        }
    }

    public boolean onResultTaken(Player player) {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return false;
        }

        int visCost = this.currentVisCost;
        String requiredCrystalId = this.currentRequiredCrystalId;
        if (visCost <= 0 || requiredCrystalId.isEmpty()) {
            recalculateCraftingResult();
            return false;
        }
        if (!canAffordVis(visCost) || !hasRequiredCrystal(requiredCrystalId) || !consumeVis(serverLevel, visCost)) {
            recalculateCraftingResult();
            return false;
        }
        consumeRequiredCrystal(requiredCrystalId);

        TransientCraftingContainer craftingInput = createCraftingInput();
        NonNullList<ItemStack> remainders = this.level.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, craftingInput, this.level);

        this.suppressCraftingUpdates = true;
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            int slotIndex = INPUT_SLOT_START + i;
            ItemStack currentInput = super.getItem(slotIndex);
            if (!currentInput.isEmpty()) {
                super.removeItem(slotIndex, 1);
            }

            ItemStack remainder = remainders.get(i);
            if (!remainder.isEmpty()) {
                ItemStack remainingInSlot = super.getItem(slotIndex);
                if (remainingInSlot.isEmpty()) {
                    super.setItem(slotIndex, remainder);
                } else if (ItemStack.isSameItemSameTags(remainingInSlot, remainder)) {
                    remainder.grow(remainingInSlot.getCount());
                    super.setItem(slotIndex, remainder);
                } else if (!player.getInventory().add(remainder)) {
                    player.drop(remainder, false);
                }
            }
        }
        this.suppressCraftingUpdates = false;

        recalculateCraftingResult();
        return true;
    }

    public void recalculateCraftingResult() {
        if (this.suppressCraftingUpdates || this.level == null || this.level.isClientSide || this.level.getServer() == null) {
            return;
        }

        TransientCraftingContainer craftingInput = createCraftingInput();
        Optional<CraftingRecipe> recipe = this.level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInput, this.level);

        ItemStack result = ItemStack.EMPTY;
        this.currentVisCost = 0;
        this.currentRequiredCrystalId = "";
        if (recipe.isPresent()) {
            int visCost = calculateVisCost(craftingInput);
            String requiredCrystalId = determineRequiredCrystal(craftingInput);
            this.currentVisCost = visCost;
            this.currentRequiredCrystalId = requiredCrystalId;
            if (canAffordVis(visCost) && hasRequiredCrystal(requiredCrystalId)) {
                result = recipe.get().assemble(craftingInput, this.level.registryAccess());
            }
        }

        super.setItem(OUTPUT_SLOT, result);
    }

    public boolean canTakeCurrentResult() {
        if (!(this.level instanceof ServerLevel)) {
            return false;
        }

        if (super.getItem(OUTPUT_SLOT).isEmpty()) {
            return false;
        }

        int visCost = this.currentVisCost;
        return visCost > 0 && canAffordVis(visCost) && hasRequiredCrystal(this.currentRequiredCrystalId);
    }

    public int getCurrentVisCost() {
        return this.currentVisCost;
    }

    public String getCurrentRequiredCrystalId() {
        return this.currentRequiredCrystalId;
    }

    public static boolean isCrystalItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        for (String crystalId : SUPPORTED_CRYSTAL_IDS) {
            if (isCrystalStack(stack, crystalId)) {
                return true;
            }
        }
        return false;
    }

    private TransientCraftingContainer createCraftingInput() {
        NonNullList<ItemStack> inputItems = NonNullList.withSize(INPUT_SLOT_COUNT, ItemStack.EMPTY);
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            inputItems.set(i, super.getItem(INPUT_SLOT_START + i).copy());
        }
        return new TransientCraftingContainer(DUMMY_CRAFTING_MENU, 3, 3, inputItems);
    }

    private int calculateVisCost(TransientCraftingContainer craftingInput) {
        int totalAspects = 0;
        int occupied = 0;

        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            occupied++;
            ItemStack single = stack.copy();
            single.setCount(1);
            totalAspects += AspectRegistry.getTotalAspectValue(single);
        }

        int baseline = Math.max(1, totalAspects / 2);
        return Mth.clamp(baseline + occupied, 1, 250);
    }

    private String determineRequiredCrystal(TransientCraftingContainer craftingInput) {
        Map<AspectType, Integer> totals = new EnumMap<>(AspectType.class);

        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            ItemStack single = stack.copy();
            single.setCount(1);
            AspectList aspects = AspectRegistry.getAspects(single);
            for (Map.Entry<AspectType, Integer> entry : aspects.asMap().entrySet()) {
                totals.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        AspectType dominant = null;
        int dominantAmount = -1;
        for (Map.Entry<AspectType, Integer> entry : totals.entrySet()) {
            if (entry.getValue() > dominantAmount && crystalIdForAspect(entry.getKey()) != null) {
                dominant = entry.getKey();
                dominantAmount = entry.getValue();
            }
        }

        String mapped = dominant == null ? null : crystalIdForAspect(dominant);
        return mapped == null ? DEFAULT_CRYSTAL_ID : mapped;
    }

    private boolean canAffordVis(int visCost) {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return false;
        }

        return AuraManager.drainVis(serverLevel, this.worldPosition, visCost, true) >= visCost;
    }

    private boolean consumeVis(ServerLevel serverLevel, int visCost) {
        return AuraManager.drainVis(serverLevel, this.worldPosition, visCost, false) >= visCost;
    }

    private boolean hasRequiredCrystal(String crystalId) {
        return crystalId != null && !crystalId.isEmpty() && isCrystalStack(super.getItem(CRYSTAL_SLOT), crystalId);
    }

    private void consumeRequiredCrystal(String crystalId) {
        if (hasRequiredCrystal(crystalId)) {
            super.removeItem(CRYSTAL_SLOT, 1);
        }
    }

    private static boolean isCrystalStack(ItemStack stack, String crystalId) {
        if (stack.isEmpty()) {
            return false;
        }

        var crystalObject = ModItems.BLOCK_ITEMS_BY_ID.get(crystalId);
        return crystalObject != null && stack.is(crystalObject.get());
    }

    private static String crystalIdForAspect(AspectType aspect) {
        return switch (aspect) {
            case AER -> "crystal_aer";
            case AQUA -> "crystal_aqua";
            case IGNIS -> "crystal_ignis";
            case TERRA -> "crystal_terra";
            case ORDO, COGNITIO -> "crystal_ordo";
            case PERDITIO, MOTUS -> "crystal_perditio";
            case VITIUM -> "crystal_vitium";
            case PRAECANTATIO, METALLUM, VICTUS, HERBA -> null;
            default -> null;
        };
    }

    private static boolean isCraftingRelevantSlot(int slot) {
        return isInputSlot(slot) || slot == CRYSTAL_SLOT;
    }

    private static boolean isInputSlot(int slot) {
        return slot >= INPUT_SLOT_START && slot < INPUT_SLOT_START + INPUT_SLOT_COUNT;
    }
}
