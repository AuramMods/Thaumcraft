package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.block.CrucibleBlock;
import art.arcane.thaumcraft.common.menu.CrucibleMenu;
import art.arcane.thaumcraft.common.recipe.CrucibleRecipe;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModRecipes;
import art.arcane.thaumcraft.common.world.aura.AuraManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CrucibleBlockEntity extends StationBlockEntity {
    // TODO(port): Restore legacy crucible instability handling from TileCrucible:
    // TODO(port): over-capacity and long-boil behavior should spill random aspects into flux over time instead of only controlled dump behavior.
    // TODO(port): bind crucible recipe execution to research unlock requirements once the knowledge system is fully ported.

    public static final int MAX_WATER = 1000;
    public static final int MAX_HEAT = 200;
    public static final int BOILING_HEAT = 150;
    public static final int MAX_ESSENTIA = 1000;

    private static final int ITEM_PROCESS_INTERVAL_TICKS = 20;
    private static final int EVAPORATION_INTERVAL_TICKS = 80;
    private static final int JAR_TRANSFER_INTERVAL_TICKS = 20;
    private static final int JAR_TRANSFER_AMOUNT = 4;
    private static final int WATER_COST_PER_ITEM = 50;

    private int heat;
    private int waterAmount;
    private AspectList essentia = new AspectList();
    private int processCooldown;

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUCIBLE.get(), pos, state, "container.thaumcraft.crucible", 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CrucibleMenu(containerId, playerInventory, this);
    }

    @Override
    public void serverTick() {
        incrementServerTicks();
        updateHeat();

        if (this.processCooldown > 0) {
            this.processCooldown--;
        }

        processNearbyItemEntities();
        if ((getServerTicksValue() % JAR_TRANSFER_INTERVAL_TICKS) == 0) {
            transferToAdjacentJar();
        }

        if (isBoiling() && (getServerTicksValue() % EVAPORATION_INTERVAL_TICKS) == 0) {
            this.waterAmount = Math.max(0, this.waterAmount - 1);
        }

        updateVisualState();

        setActivityValue(this.heat);
        if ((getServerTicksValue() % 20) == 0) {
            setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("crucible_heat", this.heat);
        tag.putInt("crucible_water", this.waterAmount);
        tag.putInt("crucible_essentia", getEssentiaUnits());
        this.essentia.saveToTag(tag, "crucible_essentia_aspects");
        tag.putInt("crucible_cooldown", this.processCooldown);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.heat = tag.getInt("crucible_heat");
        this.waterAmount = tag.getInt("crucible_water");
        this.essentia = AspectList.loadFromTag(tag, "crucible_essentia_aspects");
        if (this.essentia.isEmpty()) {
            int legacyEssentia = tag.getInt("crucible_essentia");
            if (legacyEssentia > 0) {
                this.essentia.add(AspectType.PERDITIO, legacyEssentia);
            }
        }
        this.processCooldown = tag.getInt("crucible_cooldown");
        setActivityValue(this.heat);
    }

    public int getHeat() {
        return this.heat;
    }

    public int getWaterAmount() {
        return this.waterAmount;
    }

    public int getEssentiaUnits() {
        return this.essentia.totalAmount();
    }

    public boolean isBoiling() {
        return this.heat >= BOILING_HEAT && this.waterAmount > 0;
    }

    public boolean fillFromWaterBucket() {
        if (this.waterAmount >= MAX_WATER) {
            return false;
        }

        this.waterAmount = MAX_WATER;
        setChanged();
        syncToClient();
        return true;
    }

    public AspectList getEssentia() {
        return this.essentia.copy();
    }

    public int addEssentia(AspectType aspect, int amount) {
        if (aspect == null || amount <= 0) {
            return 0;
        }

        int current = this.essentia.totalAmount();
        int accepted = Math.min(amount, Math.max(0, MAX_ESSENTIA - current));
        if (accepted <= 0) {
            return 0;
        }

        this.essentia.add(aspect, accepted);
        setChanged();
        return accepted;
    }

    public int removeEssentia(AspectType aspect, int amount) {
        int removed = this.essentia.remove(aspect, amount);
        if (removed > 0) {
            setChanged();
        }
        return removed;
    }

    public @Nullable Map.Entry<AspectType, Integer> getDominantAspect() {
        Map.Entry<AspectType, Integer> best = null;
        for (Map.Entry<AspectType, Integer> entry : this.essentia.asMap().entrySet()) {
            if (best == null || entry.getValue() > best.getValue()) {
                best = entry;
            }
        }
        return best;
    }

    public boolean processOfferedItem(ItemStack offered) {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return false;
        }
        if (!isBoiling() || this.processCooldown > 0) {
            return false;
        }
        if (offered.isEmpty() || offered.is(Items.WATER_BUCKET) || offered.is(Items.BUCKET)) {
            return false;
        }

        ItemStack single = offered.copy();
        single.setCount(1);

        CrucibleRecipe recipe = findMatchingRecipe(serverLevel, single);
        if (recipe != null && craftRecipe(recipe, serverLevel, single)) {
            return true;
        }

        dissolveStack(single, serverLevel);
        return true;
    }

    public boolean dumpContentsToFlux() {
        int totalEssentia = this.essentia.totalAmount();
        if (this.waterAmount <= 0 && totalEssentia <= 0) {
            return false;
        }

        this.waterAmount = 0;
        this.essentia = new AspectList();
        this.processCooldown = 0;

        if (this.level instanceof ServerLevel serverLevel && totalEssentia > 0) {
            float flux = Math.max(0.25F, totalEssentia * 0.75F);
            AuraManager.addFlux(serverLevel, this.worldPosition, flux);
        }

        setChanged();
        syncToClient();
        return true;
    }

    public void syncToClient() {
        setChanged();
        if (this.level != null) {
            BlockState state = getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    private void updateHeat() {
        if (this.level == null) {
            return;
        }

        boolean heating = hasHeatSource();
        if (heating && this.waterAmount > 0) {
            if (this.heat < MAX_HEAT) {
                this.heat++;
            }
        } else if (this.heat > 0) {
            this.heat--;
        }
    }

    private boolean hasHeatSource() {
        if (this.level == null) {
            return false;
        }
        return isHeatingBlock(this.level.getBlockState(this.worldPosition.below()));
    }

    private CrucibleRecipe findMatchingRecipe(ServerLevel level, ItemStack catalystStack) {
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, catalystStack);

        CrucibleRecipe best = null;
        int bestScore = -1;
        for (CrucibleRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.CRUCIBLE_TYPE)) {
            if (!recipe.matches(container, level)) {
                continue;
            }
            if (!this.essentia.containsAtLeast(recipe.getRequiredAspects())) {
                continue;
            }
            if (this.waterAmount < recipe.getWaterCost()) {
                continue;
            }

            int score = recipe.getRequiredAspects().totalAmount();
            if (score > bestScore) {
                bestScore = score;
                best = recipe;
            }
        }

        return best;
    }

    private boolean craftRecipe(CrucibleRecipe recipe, ServerLevel level, ItemStack catalyst) {
        if (!recipe.matchesCatalyst(catalyst)) {
            return false;
        }
        if (!this.essentia.containsAtLeast(recipe.getRequiredAspects())) {
            return false;
        }
        if (this.waterAmount < recipe.getWaterCost()) {
            return false;
        }

        if (!this.essentia.removeAll(recipe.getRequiredAspects())) {
            return false;
        }

        this.waterAmount = Math.max(0, this.waterAmount - recipe.getWaterCost());
        this.processCooldown = ITEM_PROCESS_INTERVAL_TICKS;

        spawnOutput(level, recipe.getResult());
        ItemStack remainder = catalyst.getCraftingRemainingItem();
        if (!remainder.isEmpty()) {
            spawnOutput(level, remainder);
        }

        setChanged();
        syncToClient();
        return true;
    }

    private void dissolveStack(ItemStack consumed, ServerLevel serverLevel) {
        AspectList aspects = AspectRegistry.getAspects(serverLevel, consumed);
        addAspectsClamped(aspects);

        this.waterAmount = Math.max(0, this.waterAmount - WATER_COST_PER_ITEM);
        this.processCooldown = ITEM_PROCESS_INTERVAL_TICKS;

        AuraManager.addFlux(serverLevel, this.worldPosition, estimateFluxContribution(aspects));

        ItemStack remainder = consumed.getCraftingRemainingItem();
        if (!remainder.isEmpty()) {
            spawnOutput(serverLevel, remainder);
        }

        setChanged();
        syncToClient();
    }

    private void addAspectsClamped(AspectList incoming) {
        for (Map.Entry<AspectType, Integer> entry : incoming.asMap().entrySet()) {
            addEssentia(entry.getKey(), entry.getValue());
        }
    }

    private void processNearbyItemEntities() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!isBoiling() || this.processCooldown > 0) {
            return;
        }

        AABB area = new AABB(
                this.worldPosition.getX() + 0.1D,
                this.worldPosition.getY() + 0.5D,
                this.worldPosition.getZ() + 0.1D,
                this.worldPosition.getX() + 0.9D,
                this.worldPosition.getY() + 1.4D,
                this.worldPosition.getZ() + 0.9D
        );

        for (ItemEntity itemEntity : serverLevel.getEntitiesOfClass(ItemEntity.class, area, ItemEntity::isAlive)) {
            ItemStack stack = itemEntity.getItem();
            if (stack.isEmpty()) {
                continue;
            }

            ItemStack single = stack.copy();
            single.setCount(1);
            if (!processOfferedItem(single)) {
                continue;
            }

            stack.shrink(1);
            if (stack.isEmpty()) {
                itemEntity.discard();
            } else {
                itemEntity.setItem(stack);
            }
            return;
        }
    }

    private void transferToAdjacentJar() {
        if (this.level == null || this.level.isClientSide || this.essentia.isEmpty()) {
            return;
        }

        Map.Entry<AspectType, Integer> dominant = getDominantAspect();
        if (dominant == null || dominant.getValue() <= 0) {
            return;
        }

        int transferAmount = Math.min(JAR_TRANSFER_AMOUNT, dominant.getValue());
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (!(blockEntity instanceof JarBlockEntity jar)) {
                continue;
            }

            int moved = jar.addAspect(dominant.getKey(), transferAmount);
            if (moved <= 0) {
                continue;
            }

            removeEssentia(dominant.getKey(), moved);
            jar.syncToClient();
            syncToClient();
            return;
        }
    }

    private void updateVisualState() {
        if (this.level == null) {
            return;
        }

        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof CrucibleBlock)) {
            return;
        }

        boolean water = this.waterAmount > 0;
        boolean heated = hasHeatSource();

        if (state.getValue(CrucibleBlock.WATER) == water && state.getValue(CrucibleBlock.HEATED) == heated) {
            return;
        }

        this.level.setBlock(this.worldPosition,
                state.setValue(CrucibleBlock.WATER, water).setValue(CrucibleBlock.HEATED, heated),
                3);
    }

    private void spawnOutput(ServerLevel level, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        ItemEntity itemEntity = new ItemEntity(
                level,
                this.worldPosition.getX() + 0.5D,
                this.worldPosition.getY() + 1.0D,
                this.worldPosition.getZ() + 0.5D,
                stack.copy()
        );
        itemEntity.setDeltaMovement(0.0D, 0.08D, 0.0D);
        level.addFreshEntity(itemEntity);
    }

    private static float estimateFluxContribution(AspectList aspects) {
        int total = Math.max(1, aspects.totalAmount());
        int vitium = aspects.get(AspectType.VITIUM);
        return Math.max(0.25F, (total * 0.05F) + (vitium * 0.15F));
    }

    private static boolean isHeatingBlock(BlockState state) {
        if (state.is(Blocks.FIRE)
                || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.MAGMA_BLOCK)
                || state.is(Blocks.LAVA)
                || state.is(Blocks.LAVA_CAULDRON)
                || state.is(ModBlocks.get("nitor").get())) {
            return true;
        }

        return state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT);
    }
}
