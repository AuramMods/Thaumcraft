package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.menu.InfusionMatrixMenu;
import art.arcane.thaumcraft.common.recipe.InfusionRecipe;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModRecipes;
import art.arcane.thaumcraft.common.world.aura.AuraManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfusionMatrixBlockEntity extends StationBlockEntity {
    // TODO(port): Reach legacy TileInfusionMatrix parity:
    // TODO(port): consume required essentia aspects during craft cycles (via jars/transport network) instead of vis-only draining.
    // TODO(port): gate recipes by research and support advanced outputs (NBT/object outputs, infusion enchantments, XP-aware variants).
    // TODO(port): expand instability event table (ejection, zaps, explosions, warp side effects) and visual/network FX parity.

    public static final int MATRIX_SLOT = 0;
    private static final int CAPTURE_SLOT_START = 1;
    private static final int CAPTURE_SLOT_COUNT = 16;
    private static final int TOTAL_SLOTS = CAPTURE_SLOT_START + CAPTURE_SLOT_COUNT;
    private static final int STRUCTURE_SCAN_INTERVAL_TICKS = 20;
    private static final int PEDESTAL_CAPTURE_INTERVAL_TICKS = 10;
    private static final int INFUSION_VIS_DRAIN_INTERVAL_TICKS = 20;
    private static final int INFUSION_COOLDOWN_TICKS = 40;
    private static final int MAX_STABILITY = 100;

    private static final String PEDESTAL_COUNT_TAG = "pedestal_count";
    private static final String STRUCTURE_READY_TAG = "structure_ready";
    private static final String STABILITY_TAG = "stability";
    private static final String OWNED_PEDESTALS_TAG = "owned_pedestals";
    private static final String CAPTURE_SOURCES_TAG = "capture_sources";
    private static final String CAPTURE_SLOT_TAG = "slot";
    private static final String CAPTURE_POS_TAG = "pos";
    private static final String INFUSION_ACTIVE_TAG = "infusion_active";
    private static final String INFUSION_TICKS_TAG = "infusion_ticks";
    private static final String INFUSION_DURATION_TAG = "infusion_duration";
    private static final String INFUSION_COOLDOWN_TAG = "infusion_cooldown";
    private static final String INFUSION_REQUESTED_TAG = "infusion_requested";

    private static final int[][] PRIMARY_RING_OFFSETS = {
            {2, 0}, {-2, 0}, {0, 2}, {0, -2},
            {2, 2}, {-2, 2}, {2, -2}, {-2, -2}
    };
    private static final int[][] EXTENDED_RING_OFFSETS = {
            {4, 0}, {-4, 0}, {0, 4}, {0, -4},
            {4, 2}, {-4, 2}, {4, -2}, {-4, -2},
            {2, 4}, {-2, 4}, {2, -4}, {-2, -4}
    };

    private int pedestalCount;
    private boolean structureReady;
    private int stability;
    private final List<BlockPos> ownedPedestals = new ArrayList<>();
    private final Map<Integer, BlockPos> capturedSourceBySlot = new LinkedHashMap<>();
    private boolean infusionActive;
    private int infusionTicks;
    private int infusionDurationTicks;
    private int infusionCooldownTicks;
    private boolean infusionRequested;

    public InfusionMatrixBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFUSION_MATRIX.get(), pos, state, "container.thaumcraft.infusion_matrix", TOTAL_SLOTS);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = super.removeItem(slot, amount);
        if (slot == MATRIX_SLOT && !result.isEmpty()) {
            this.infusionRequested = !super.getItem(MATRIX_SLOT).isEmpty();
            if (this.infusionActive) {
                abortInfusionCycle(false);
            }
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = super.removeItemNoUpdate(slot);
        if (slot == MATRIX_SLOT && !result.isEmpty()) {
            this.infusionRequested = !super.getItem(MATRIX_SLOT).isEmpty();
            if (this.infusionActive) {
                abortInfusionCycle(false);
            }
        }
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        if (slot == MATRIX_SLOT) {
            this.infusionRequested = !stack.isEmpty();
            if (this.infusionActive) {
                abortInfusionCycle(false);
            }
        }
    }

    @Override
    public void serverTick() {
        incrementServerTicks();
        boolean changed = false;

        if ((getServerTicksValue() % STRUCTURE_SCAN_INTERVAL_TICKS) == 0) {
            changed |= scanStructureAndOwnership();
        }
        if (this.structureReady && !this.infusionActive && (getServerTicksValue() % PEDESTAL_CAPTURE_INTERVAL_TICKS) == 0) {
            changed |= capturePedestalItems();
        }
        if (this.infusionCooldownTicks > 0) {
            this.infusionCooldownTicks--;
            changed = true;
        }
        if (this.infusionActive) {
            changed |= tickInfusionCycle();
        } else {
            changed |= tryStartInfusionCycle();
        }

        int activity = calculateActivity();
        if (activity != getActivityValue()) {
            setActivityValue(activity);
            changed = true;
        }

        if (changed) {
            setChanged();
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new InfusionMatrixMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(PEDESTAL_COUNT_TAG, this.pedestalCount);
        tag.putBoolean(STRUCTURE_READY_TAG, this.structureReady);
        tag.putInt(STABILITY_TAG, this.stability);
        tag.putBoolean(INFUSION_ACTIVE_TAG, this.infusionActive);
        tag.putInt(INFUSION_TICKS_TAG, this.infusionTicks);
        tag.putInt(INFUSION_DURATION_TAG, this.infusionDurationTicks);
        tag.putInt(INFUSION_COOLDOWN_TAG, this.infusionCooldownTicks);
        tag.putBoolean(INFUSION_REQUESTED_TAG, this.infusionRequested);

        ListTag ownedTag = new ListTag();
        for (BlockPos ownedPedestal : this.ownedPedestals) {
            ownedTag.add(NbtUtils.writeBlockPos(ownedPedestal));
        }
        tag.put(OWNED_PEDESTALS_TAG, ownedTag);

        ListTag captureSourceTag = new ListTag();
        for (Map.Entry<Integer, BlockPos> entry : this.capturedSourceBySlot.entrySet()) {
            CompoundTag sourceTag = new CompoundTag();
            sourceTag.putInt(CAPTURE_SLOT_TAG, entry.getKey());
            sourceTag.put(CAPTURE_POS_TAG, NbtUtils.writeBlockPos(entry.getValue()));
            captureSourceTag.add(sourceTag);
        }
        tag.put(CAPTURE_SOURCES_TAG, captureSourceTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.pedestalCount = tag.getInt(PEDESTAL_COUNT_TAG);
        this.structureReady = tag.getBoolean(STRUCTURE_READY_TAG);
        this.stability = tag.getInt(STABILITY_TAG);
        this.infusionActive = tag.getBoolean(INFUSION_ACTIVE_TAG);
        this.infusionTicks = tag.getInt(INFUSION_TICKS_TAG);
        this.infusionDurationTicks = tag.getInt(INFUSION_DURATION_TAG);
        this.infusionCooldownTicks = tag.getInt(INFUSION_COOLDOWN_TAG);
        this.infusionRequested = tag.getBoolean(INFUSION_REQUESTED_TAG);

        this.ownedPedestals.clear();
        ListTag ownedTag = tag.getList(OWNED_PEDESTALS_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < ownedTag.size(); i++) {
            this.ownedPedestals.add(NbtUtils.readBlockPos(ownedTag.getCompound(i)));
        }

        this.capturedSourceBySlot.clear();
        ListTag captureSources = tag.getList(CAPTURE_SOURCES_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < captureSources.size(); i++) {
            CompoundTag sourceTag = captureSources.getCompound(i);
            int slot = sourceTag.getInt(CAPTURE_SLOT_TAG);
            BlockPos sourcePos = NbtUtils.readBlockPos(sourceTag.getCompound(CAPTURE_POS_TAG));
            this.capturedSourceBySlot.put(slot, sourcePos);
        }
        sanitizeCaptureSources();
    }

    public int getPedestalCount() {
        return this.pedestalCount;
    }

    public boolean isStructureReady() {
        return this.structureReady;
    }

    public int getCapturedItemCount() {
        return this.capturedSourceBySlot.size();
    }

    private int calculateActivity() {
        if (this.infusionActive && this.infusionDurationTicks > 0) {
            int progress = Mth.floor((this.infusionTicks / (float) this.infusionDurationTicks) * 120.0F);
            return Mth.clamp(60 + progress, 0, 200);
        }
        if (!this.structureReady) {
            return Math.min(200, this.pedestalCount * 4);
        }
        return Math.min(200, 40 + (this.pedestalCount * 8) + (getCapturedItemCount() * 6) + (this.stability / 5));
    }

    private boolean scanStructureAndOwnership() {
        if (this.level == null) {
            boolean changed = this.pedestalCount != 0 || this.structureReady || !this.ownedPedestals.isEmpty() || !this.capturedSourceBySlot.isEmpty() || this.infusionActive;
            this.pedestalCount = 0;
            this.structureReady = false;
            this.stability = 0;
            this.ownedPedestals.clear();
            this.capturedSourceBySlot.clear();
            this.infusionActive = false;
            this.infusionTicks = 0;
            this.infusionDurationTicks = 0;
            return changed;
        }

        Block pedestalBlock = ModBlocks.get("pedestal").get();
        BlockPos pedestalPlane = this.worldPosition.below(2);

        boolean centerPedestal = this.level.getBlockState(pedestalPlane).is(pedestalBlock);
        List<BlockPos> primaryPedestals = collectPedestalsAtOffsets(pedestalBlock, pedestalPlane, PRIMARY_RING_OFFSETS);
        List<BlockPos> extendedPedestals = collectPedestalsAtOffsets(pedestalBlock, pedestalPlane, EXTENDED_RING_OFFSETS);
        int primaryCount = primaryPedestals.size();
        int newPedestalCount = (centerPedestal ? 1 : 0) + primaryCount + extendedPedestals.size();
        boolean newStructureReady = centerPedestal && primaryCount >= 4;
        int newStability = computeStability(centerPedestal, primaryPedestals, extendedPedestals);

        boolean changed = false;
        if (this.pedestalCount != newPedestalCount) {
            this.pedestalCount = newPedestalCount;
            changed = true;
        }
        if (this.structureReady != newStructureReady) {
            this.structureReady = newStructureReady;
            changed = true;
        }
        if (this.stability != newStability) {
            this.stability = newStability;
            changed = true;
        }

        if (newStructureReady) {
            List<BlockPos> owned = new ArrayList<>(primaryPedestals.size() + extendedPedestals.size());
            owned.addAll(primaryPedestals);
            owned.addAll(extendedPedestals);
            changed |= setOwnedPedestals(owned);
        } else {
            changed |= clearOwnershipAndCapturedItems();
        }

        return changed;
    }

    private List<BlockPos> collectPedestalsAtOffsets(Block pedestalBlock, BlockPos center, int[][] offsets) {
        List<BlockPos> positions = new ArrayList<>();
        for (int[] offset : offsets) {
            BlockPos testPos = center.offset(offset[0], 0, offset[1]);
            if (this.level != null && this.level.getBlockState(testPos).is(pedestalBlock)) {
                positions.add(testPos.immutable());
            }
        }
        return positions;
    }

    private boolean setOwnedPedestals(List<BlockPos> updatedOwnedPedestals) {
        if (this.ownedPedestals.equals(updatedOwnedPedestals)) {
            return false;
        }

        if (this.infusionActive) {
            abortInfusionCycle(true);
        }
        releaseCapturedItems();
        this.ownedPedestals.clear();
        this.ownedPedestals.addAll(updatedOwnedPedestals);
        return true;
    }

    private boolean clearOwnershipAndCapturedItems() {
        boolean changed = false;

        if (!this.ownedPedestals.isEmpty()) {
            this.ownedPedestals.clear();
            changed = true;
        }
        if (this.infusionActive) {
            abortInfusionCycle(true);
            changed = true;
        }
        changed |= releaseCapturedItems();

        return changed;
    }

    private boolean capturePedestalItems() {
        if (this.level == null || this.ownedPedestals.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (BlockPos sourcePos : this.ownedPedestals) {
            if (isSourceCaptured(sourcePos)) {
                continue;
            }

            int captureSlot = findFirstEmptyCaptureSlot();
            if (captureSlot < 0) {
                break;
            }

            if (!(this.level.getBlockEntity(sourcePos) instanceof PedestalBlockEntity pedestal)) {
                continue;
            }

            ItemStack extracted = pedestal.removeItem(0, 1);
            if (extracted.isEmpty()) {
                continue;
            }

            super.setItem(captureSlot, extracted);
            this.capturedSourceBySlot.put(captureSlot, sourcePos.immutable());
            changed = true;
        }

        return changed;
    }

    private boolean tryStartInfusionCycle() {
        if (!this.structureReady || this.infusionCooldownTicks > 0 || !this.infusionRequested || this.level == null) {
            return false;
        }
        if (super.getItem(MATRIX_SLOT).isEmpty() || getCapturedItemCount() <= 0) {
            return false;
        }

        this.infusionActive = true;
        this.infusionTicks = 0;
        this.infusionDurationTicks = calculateInfusionDurationTicks();
        this.infusionRequested = false;
        return true;
    }

    private boolean tickInfusionCycle() {
        if (!this.infusionActive) {
            return false;
        }

        if (!this.structureReady || this.level == null) {
            abortInfusionCycle(true);
            return true;
        }

        this.infusionTicks++;

        if (this.level instanceof ServerLevel serverLevel && (this.infusionTicks % INFUSION_VIS_DRAIN_INTERVAL_TICKS) == 0) {
            float required = calculateInfusionVisDrain();
            float drained = AuraManager.drainVis(serverLevel, this.worldPosition, required, false);
            if (drained < required) {
                AuraManager.addFlux(serverLevel, this.worldPosition, 1.0F + ((required - drained) * 0.75F));
                abortInfusionCycle(true);
                return true;
            }

            if (rollStabilityFailure(serverLevel)) {
                applyInstabilityFailureEffects(serverLevel);
                AuraManager.addFlux(serverLevel, this.worldPosition, 1.0F + (getCapturedItemCount() * 0.4F));
                abortInfusionCycle(true);
                return true;
            }
        }

        if (this.infusionTicks >= this.infusionDurationTicks) {
            finishInfusionCycle();
            return true;
        }

        return true;
    }

    private void finishInfusionCycle() {
        ItemStack result = ItemStack.EMPTY;
        if (this.level instanceof ServerLevel serverLevel) {
            result = executeRecipeInfusionCycle(serverLevel);
        }
        if (result.isEmpty()) {
            result = consumeMatrixCatalystForFallbackResult();
            consumeCapturedIngredients();
        }
        if (!result.isEmpty()) {
            BlockPos centerPedestalPos = this.worldPosition.below(2);
            returnItemToSourceOrDrop(centerPedestalPos, result);
        }
        this.infusionActive = false;
        this.infusionTicks = 0;
        this.infusionDurationTicks = 0;
        this.infusionCooldownTicks = INFUSION_COOLDOWN_TICKS;
    }

    private void abortInfusionCycle(boolean addFluxPenalty) {
        if (!this.infusionActive && this.infusionTicks == 0 && this.infusionDurationTicks == 0) {
            return;
        }

        if (addFluxPenalty && this.level instanceof ServerLevel serverLevel) {
            AuraManager.addFlux(serverLevel, this.worldPosition, 0.5F + (getCapturedItemCount() * 0.25F));
        }
        releaseCapturedItems();
        this.infusionActive = false;
        this.infusionTicks = 0;
        this.infusionDurationTicks = 0;
        this.infusionCooldownTicks = Math.max(this.infusionCooldownTicks, INFUSION_COOLDOWN_TICKS / 2);
    }

    private int calculateInfusionDurationTicks() {
        int captured = Math.max(1, getCapturedItemCount());
        int duration = 80 + (captured * 16) - (this.pedestalCount * 2);
        return Mth.clamp(duration, 60, 240);
    }

    private float calculateInfusionVisDrain() {
        return 1.0F + (Math.max(1, getCapturedItemCount()) * 0.4F);
    }

    private int computeStability(boolean centerPedestal, List<BlockPos> primaryPedestals, List<BlockPos> extendedPedestals) {
        int stabilityScore = centerPedestal ? 25 : 0;
        stabilityScore += primaryPedestals.size() * 7;
        stabilityScore += extendedPedestals.size() * 3;
        stabilityScore += countBalancedPairs(primaryPedestals, PRIMARY_RING_OFFSETS) * 4;
        stabilityScore += countBalancedPairs(extendedPedestals, EXTENDED_RING_OFFSETS) * 2;
        return Mth.clamp(stabilityScore, 0, MAX_STABILITY);
    }

    private int countBalancedPairs(List<BlockPos> positions, int[][] offsets) {
        int balanced = 0;
        for (int i = 0; i < offsets.length; i += 2) {
            BlockPos a = this.worldPosition.below(2).offset(offsets[i][0], 0, offsets[i][1]);
            BlockPos b = this.worldPosition.below(2).offset(offsets[i + 1][0], 0, offsets[i + 1][1]);
            if (positions.contains(a) && positions.contains(b)) {
                balanced++;
            }
        }
        return balanced;
    }

    private boolean rollStabilityFailure(ServerLevel serverLevel) {
        int capturedPenalty = getCapturedItemCount() * 4;
        int effectiveStability = Mth.clamp(this.stability - capturedPenalty, 5, 95);
        int roll = serverLevel.random.nextInt(100);
        return roll >= effectiveStability;
    }

    private void applyInstabilityFailureEffects(ServerLevel serverLevel) {
        if (this.level == null || this.capturedSourceBySlot.isEmpty()) {
            return;
        }

        int spillCount = Mth.clamp(Math.max(1, getCapturedItemCount() / 4), 1, 2);
        List<Map.Entry<Integer, BlockPos>> capturedEntries = new ArrayList<>(this.capturedSourceBySlot.entrySet());
        for (int i = 0; i < spillCount && !capturedEntries.isEmpty(); i++) {
            int index = serverLevel.random.nextInt(capturedEntries.size());
            Map.Entry<Integer, BlockPos> entry = capturedEntries.remove(index);
            int slot = entry.getKey();
            ItemStack stack = super.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            Containers.dropItemStack(
                    this.level,
                    entry.getValue().getX() + 0.5D,
                    entry.getValue().getY() + 1.0D,
                    entry.getValue().getZ() + 0.5D,
                    stack.copy()
            );
            super.setItem(slot, ItemStack.EMPTY);
            this.capturedSourceBySlot.remove(slot);
        }
    }

    private ItemStack executeRecipeInfusionCycle(ServerLevel serverLevel) {
        ItemStack matrixStack = super.getItem(MATRIX_SLOT);
        if (matrixStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        RecipeMatch recipeMatch = findBestMatchingRecipe(serverLevel, matrixStack);
        if (recipeMatch == null) {
            return ItemStack.EMPTY;
        }

        consumeMatrixCatalyst();
        consumeCapturedSlots(recipeMatch.matchedSlots());
        releaseCapturedItems();
        return recipeMatch.recipe().getResult();
    }

    private RecipeMatch findBestMatchingRecipe(ServerLevel serverLevel, ItemStack matrixStack) {
        RecipeMatch bestMatch = null;

        for (InfusionRecipe recipe : serverLevel.getRecipeManager().getAllRecipesFor(ModRecipes.INFUSION_TYPE)) {
            if (!recipe.matchesCatalyst(matrixStack)) {
                continue;
            }

            List<Integer> matchedSlots = matchComponentSlots(recipe.getComponents());
            if (matchedSlots == null) {
                continue;
            }

            if (bestMatch == null || recipe.getComponentCount() > bestMatch.recipe().getComponentCount()) {
                bestMatch = new RecipeMatch(recipe, matchedSlots);
            }
        }

        return bestMatch;
    }

    private List<Integer> matchComponentSlots(List<Ingredient> components) {
        if (components.isEmpty()) {
            return List.of();
        }

        List<Integer> availableSlots = new ArrayList<>(this.capturedSourceBySlot.keySet());
        availableSlots.sort(Integer::compareTo);
        boolean[] usedSlots = new boolean[availableSlots.size()];
        List<Integer> matchedSlots = new ArrayList<>(components.size());

        for (Ingredient component : components) {
            int matchedIndex = -1;
            for (int i = 0; i < availableSlots.size(); i++) {
                if (usedSlots[i]) {
                    continue;
                }
                ItemStack capturedStack = super.getItem(availableSlots.get(i));
                if (!capturedStack.isEmpty() && component.test(capturedStack)) {
                    matchedIndex = i;
                    break;
                }
            }
            if (matchedIndex < 0) {
                return null;
            }

            usedSlots[matchedIndex] = true;
            matchedSlots.add(availableSlots.get(matchedIndex));
        }

        return matchedSlots;
    }

    private void consumeCapturedSlots(List<Integer> slots) {
        for (Integer slot : slots) {
            ItemStack stack = super.getItem(slot);
            if (!stack.isEmpty()) {
                ItemStack updated = stack.copy();
                updated.shrink(1);
                super.setItem(slot, updated.isEmpty() ? ItemStack.EMPTY : updated);
            }
            this.capturedSourceBySlot.remove(slot);
        }
    }

    private void consumeMatrixCatalyst() {
        ItemStack matrixStack = super.getItem(MATRIX_SLOT);
        if (matrixStack.isEmpty()) {
            return;
        }

        ItemStack updatedMatrixStack = matrixStack.copy();
        updatedMatrixStack.shrink(1);
        super.setItem(MATRIX_SLOT, updatedMatrixStack.isEmpty() ? ItemStack.EMPTY : updatedMatrixStack);
    }

    private ItemStack consumeMatrixCatalystForFallbackResult() {
        ItemStack matrixStack = super.getItem(MATRIX_SLOT);
        if (matrixStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = matrixStack.copy();
        result.setCount(1);

        ItemStack updatedMatrixStack = matrixStack.copy();
        updatedMatrixStack.shrink(1);
        super.setItem(MATRIX_SLOT, updatedMatrixStack.isEmpty() ? ItemStack.EMPTY : updatedMatrixStack);
        return result;
    }

    private void consumeCapturedIngredients() {
        if (this.capturedSourceBySlot.isEmpty()) {
            return;
        }

        for (Integer slot : new ArrayList<>(this.capturedSourceBySlot.keySet())) {
            super.setItem(slot, ItemStack.EMPTY);
        }
        this.capturedSourceBySlot.clear();
    }

    private boolean isSourceCaptured(BlockPos sourcePos) {
        for (BlockPos capturedSource : this.capturedSourceBySlot.values()) {
            if (capturedSource.equals(sourcePos)) {
                return true;
            }
        }
        return false;
    }

    private int findFirstEmptyCaptureSlot() {
        for (int slot = CAPTURE_SLOT_START; slot < TOTAL_SLOTS; slot++) {
            if (super.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    private boolean releaseCapturedItems() {
        if (this.level == null || this.capturedSourceBySlot.isEmpty()) {
            return false;
        }

        boolean changed = false;
        List<Map.Entry<Integer, BlockPos>> capturedEntries = new ArrayList<>(this.capturedSourceBySlot.entrySet());
        for (Map.Entry<Integer, BlockPos> entry : capturedEntries) {
            int slot = entry.getKey();
            ItemStack stack = super.getItem(slot);
            if (!stack.isEmpty()) {
                returnItemToSourceOrDrop(entry.getValue(), stack.copy());
                super.setItem(slot, ItemStack.EMPTY);
                changed = true;
            }
        }

        if (!this.capturedSourceBySlot.isEmpty()) {
            this.capturedSourceBySlot.clear();
            changed = true;
        }

        return changed;
    }

    private void returnItemToSourceOrDrop(BlockPos sourcePos, ItemStack stack) {
        if (this.level == null || stack.isEmpty()) {
            return;
        }

        if (this.level.getBlockEntity(sourcePos) instanceof PedestalBlockEntity pedestal) {
            ItemStack existing = pedestal.getItem(0);
            if (existing.isEmpty()) {
                pedestal.setItem(0, stack);
                return;
            }

            int maxStack = Math.min(pedestal.getMaxStackSize(), existing.getMaxStackSize());
            if (ItemStack.isSameItemSameTags(existing, stack) && existing.getCount() < maxStack) {
                int merge = Math.min(maxStack - existing.getCount(), stack.getCount());
                if (merge > 0) {
                    ItemStack merged = existing.copy();
                    merged.grow(merge);
                    pedestal.setItem(0, merged);
                    stack.shrink(merge);
                }
            }
        }

        if (!stack.isEmpty()) {
            Containers.dropItemStack(this.level, sourcePos.getX() + 0.5D, sourcePos.getY() + 1.0D, sourcePos.getZ() + 0.5D, stack);
        }
    }

    private void sanitizeCaptureSources() {
        this.capturedSourceBySlot.entrySet().removeIf(entry ->
                entry.getKey() < CAPTURE_SLOT_START
                        || entry.getKey() >= TOTAL_SLOTS
                        || super.getItem(entry.getKey()).isEmpty()
        );
    }

    private record RecipeMatch(InfusionRecipe recipe, List<Integer> matchedSlots) {
    }
}
