package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import art.arcane.thaumcraft.common.registry.ModItems;
import art.arcane.thaumcraft.common.menu.ResearchTableMenu;
import net.minecraft.ChatFormatting;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResearchTableBlockEntity extends StationBlockEntity {
    // TODO(port): Port legacy TileResearchTable theorycraft flow:
    // TODO(port): replace this simplified note/session model with full legacy card draw, aid selection, and card activation state.
    // TODO(port): split server-authoritative session state into explicit container sync packets once the full research-table UI is ported.
    // TODO(port): add nearby aid scanning (blocks/entities) so theory options depend on surrounding setup as in 1.12.2.

    private static final int SCRIBING_SLOT = 0;
    private static final int PAPER_SLOT = 1;
    private static final int BASE_INSPIRATION = 5;
    private static final int MAX_INSPIRATION = 15;
    private static final int MAX_OBSERVATION_CONSUME_PER_CATEGORY = 8;
    private static final int OBSERVATION_TO_CATEGORY_TOTAL = 20;
    private static final int BASELINE_CATEGORY_TOTAL = 40;
    private static final String TAG_THEORY_ACTIVE = "theory_active";
    private static final String TAG_THEORY_INSPIRATION = "theory_inspiration";
    private static final String TAG_THEORY_INSPIRATION_START = "theory_inspiration_start";
    private static final String TAG_THEORY_DRAFT_COUNT = "theory_draft_count";
    private static final String TAG_THEORY_CATEGORY_TOTALS = "theory_category_totals";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TOTAL = "total";
    public static final int MENU_DATA_COUNT = 8;
    public static final int DATA_INDEX_SESSION_ACTIVE = 2;
    public static final int DATA_INDEX_INSPIRATION = 3;
    public static final int DATA_INDEX_INSPIRATION_START = 4;
    public static final int DATA_INDEX_DRAFT_COUNT = 5;
    public static final int DATA_INDEX_CATEGORY_COUNT = 6;
    public static final int DATA_INDEX_COMPLETE_READY = 7;

    private final Map<String, Integer> theoryCategoryTotals = new LinkedHashMap<>();
    private boolean theorySessionActive;
    private int theoryInspiration;
    private int theoryInspirationStart;
    private int theoryDraftCount;
    private final ContainerData researchMenuData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ResearchTableBlockEntity.this.getServerTicksValue();
                case 1 -> ResearchTableBlockEntity.this.getActivityValue();
                case DATA_INDEX_SESSION_ACTIVE -> ResearchTableBlockEntity.this.theorySessionActive ? 1 : 0;
                case DATA_INDEX_INSPIRATION -> ResearchTableBlockEntity.this.theoryInspiration;
                case DATA_INDEX_INSPIRATION_START -> ResearchTableBlockEntity.this.theoryInspirationStart;
                case DATA_INDEX_DRAFT_COUNT -> ResearchTableBlockEntity.this.theoryDraftCount;
                case DATA_INDEX_CATEGORY_COUNT -> ResearchTableBlockEntity.this.theoryCategoryTotals.size();
                case DATA_INDEX_COMPLETE_READY -> ResearchTableBlockEntity.this.hasCompletableTheory() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> ResearchTableBlockEntity.this.setServerTicksValue(value);
                case 1 -> ResearchTableBlockEntity.this.setActivityValue(value);
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return MENU_DATA_COUNT;
        }
    };

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE.get(), pos, state, "container.thaumcraft.research_table", 2);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ResearchTableMenu(containerId, playerInventory, this);
    }

    @Override
    public void serverTick() {
        incrementServerTicks();
        setActivityValue(this.theorySessionActive ? this.theoryInspiration : 0);
        if ((getServerTicksValue() % 20) == 0) {
            setChanged();
        }
    }

    public boolean draftTheory(ServerPlayer player) {
        ItemStack scribingTools = getItem(SCRIBING_SLOT);
        ItemStack paper = getItem(PAPER_SLOT);
        if (!isUsableScribingTools(scribingTools) || paper.isEmpty()) {
            return false;
        }
        ensureTheorySession(player);
        if (this.theoryInspiration <= 0) {
            player.displayClientMessage(
                    Component.literal("No inspiration left. Complete theory to claim progress.")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        if (!consumePaper()) {
            return false;
        }
        consumeScribingInkBaseline(scribingTools);

        int totalObservationSpent = 0;
        int totalCategoryTotalGained = 0;

        for (String category : theoryCategories()) {
            int observationRaw = PlayerKnowledgeManager.getResearchKnowledgeRaw(
                    player,
                    PlayerKnowledgeManager.ResearchKnowledgeType.OBSERVATION,
                    category
            );
            if (observationRaw <= 0) {
                continue;
            }

            int spend = Math.min(observationRaw, MAX_OBSERVATION_CONSUME_PER_CATEGORY);
            if (spend <= 0) {
                continue;
            }

            boolean spent = PlayerKnowledgeManager.addResearchKnowledge(
                    player,
                    PlayerKnowledgeManager.ResearchKnowledgeType.OBSERVATION,
                    category,
                    -spend
            );
            if (!spent) {
                continue;
            }

            int categoryGain = spend * OBSERVATION_TO_CATEGORY_TOTAL;
            addTheoryCategoryTotal(category, categoryGain);

            totalObservationSpent += spend;
            totalCategoryTotalGained += categoryGain;
        }

        if (totalCategoryTotalGained <= 0) {
            addTheoryCategoryTotal(PlayerKnowledgeManager.RESEARCH_CATEGORY_BASICS, BASELINE_CATEGORY_TOTAL);
            totalCategoryTotalGained = BASELINE_CATEGORY_TOTAL;
        }

        if (totalObservationSpent >= 12) {
            PlayerKnowledgeManager.addResearchKnowledge(
                    player,
                    PlayerKnowledgeManager.ResearchKnowledgeType.EPIPHANY,
                    "",
                    1
            );
        }

        this.theoryDraftCount++;
        this.theoryInspiration = Math.max(0, this.theoryInspiration - 1);
        setChanged();

        player.displayClientMessage(
                Component.literal("Drafted theory: +" + totalCategoryTotalGained
                        + " note total, -" + totalObservationSpent + " observation raw"
                        + " | inspiration " + this.theoryInspiration + "/" + this.theoryInspirationStart)
                        .withStyle(ChatFormatting.AQUA),
                true
        );
        return true;
    }

    public boolean completeTheory(ServerPlayer player) {
        if (!this.theorySessionActive) {
            return false;
        }
        if (this.theoryInspiration > 0) {
            player.displayClientMessage(
                    Component.literal("You need to use remaining inspiration before completing theory.")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        int totalTheoryRaw = 0;
        int highestCategoryRaw = 0;
        String highestCategory = "";
        int progression = PlayerKnowledgeManager.ResearchKnowledgeType.THEORY.getProgression();
        for (Map.Entry<String, Integer> entry : this.theoryCategoryTotals.entrySet()) {
            int total = entry.getValue();
            if (total <= 0) {
                continue;
            }

            int rawGain = Math.max(1, Math.round((total / 100.0F) * progression));
            PlayerKnowledgeManager.addResearchKnowledge(
                    player,
                    PlayerKnowledgeManager.ResearchKnowledgeType.THEORY,
                    entry.getKey(),
                    rawGain
            );
            totalTheoryRaw += rawGain;
            if (rawGain > highestCategoryRaw) {
                highestCategoryRaw = rawGain;
                highestCategory = entry.getKey();
            }
        }

        if (highestCategoryRaw > 0 && !highestCategory.isBlank()) {
            int bonusRaw = Math.max(1, highestCategoryRaw / 4);
            PlayerKnowledgeManager.addResearchKnowledge(
                    player,
                    PlayerKnowledgeManager.ResearchKnowledgeType.THEORY,
                    highestCategory,
                    bonusRaw
            );
            totalTheoryRaw += bonusRaw;
        }

        clearTheorySession();
        setChanged();
        player.displayClientMessage(
                Component.literal("Completed theory: +" + totalTheoryRaw + " theory raw.")
                        .withStyle(ChatFormatting.GOLD),
                true
        );
        return true;
    }

    public boolean hasCompletableTheory() {
        return this.theorySessionActive && this.theoryInspiration <= 0 && !this.theoryCategoryTotals.isEmpty();
    }

    public ContainerData getResearchMenuData() {
        return this.researchMenuData;
    }

    private void ensureTheorySession(ServerPlayer player) {
        if (this.theorySessionActive) {
            return;
        }

        this.theorySessionActive = true;
        this.theoryInspirationStart = computeAvailableInspiration(player);
        this.theoryInspiration = this.theoryInspirationStart;
        this.theoryDraftCount = 0;
        this.theoryCategoryTotals.clear();
        setChanged();
    }

    private static int computeAvailableInspiration(ServerPlayer player) {
        int unlockedResearch = PlayerKnowledgeManager.getResearchKeys(player).size();
        int bonus = Math.min(10, unlockedResearch / 8);
        return Math.max(1, Math.min(MAX_INSPIRATION, BASE_INSPIRATION + bonus));
    }

    private void addTheoryCategoryTotal(String category, int value) {
        if (category == null || category.isBlank() || value <= 0) {
            return;
        }
        int current = this.theoryCategoryTotals.getOrDefault(category, 0);
        this.theoryCategoryTotals.put(category, current + value);
    }

    private void clearTheorySession() {
        this.theorySessionActive = false;
        this.theoryInspiration = 0;
        this.theoryInspirationStart = 0;
        this.theoryDraftCount = 0;
        this.theoryCategoryTotals.clear();
    }

    private static String[] theoryCategories() {
        return new String[]{
                PlayerKnowledgeManager.RESEARCH_CATEGORY_BASICS,
                PlayerKnowledgeManager.RESEARCH_CATEGORY_THAUMATURGY,
                PlayerKnowledgeManager.RESEARCH_CATEGORY_ALCHEMY,
                PlayerKnowledgeManager.RESEARCH_CATEGORY_ARTIFICE,
                PlayerKnowledgeManager.RESEARCH_CATEGORY_GOLEMANCY,
                PlayerKnowledgeManager.RESEARCH_CATEGORY_ELDRITCH
        };
    }

    private static boolean isUsableScribingTools(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = getScribingToolsItem();
        return item != null && stack.is(item);
    }

    private boolean consumePaper() {
        ItemStack paper = getItem(PAPER_SLOT);
        if (paper.isEmpty()) {
            return false;
        }
        removeItem(PAPER_SLOT, 1);
        return true;
    }

    private void consumeScribingInkBaseline(ItemStack scribingTools) {
        // Baseline: placeholder scribing tools are currently not damageable in this port.
        if (!scribingTools.isDamageableItem()) {
            return;
        }

        int damage = scribingTools.getDamageValue() + 1;
        if (damage >= scribingTools.getMaxDamage()) {
            setItem(SCRIBING_SLOT, ItemStack.EMPTY);
            return;
        }

        scribingTools.setDamageValue(damage);
        setItem(SCRIBING_SLOT, scribingTools);
    }

    private static Item getScribingToolsItem() {
        if (!ModItems.ITEMS_BY_ID.containsKey("scribing_tools")) {
            return null;
        }
        return ModItems.ITEMS_BY_ID.get("scribing_tools").get();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean(TAG_THEORY_ACTIVE, this.theorySessionActive);
        tag.putInt(TAG_THEORY_INSPIRATION, this.theoryInspiration);
        tag.putInt(TAG_THEORY_INSPIRATION_START, this.theoryInspirationStart);
        tag.putInt(TAG_THEORY_DRAFT_COUNT, this.theoryDraftCount);

        ListTag totalsTag = new ListTag();
        for (Map.Entry<String, Integer> entry : this.theoryCategoryTotals.entrySet()) {
            if (entry.getValue() <= 0) {
                continue;
            }
            CompoundTag totalTag = new CompoundTag();
            totalTag.putString(TAG_CATEGORY, entry.getKey());
            totalTag.putInt(TAG_TOTAL, entry.getValue());
            totalsTag.add(totalTag);
        }
        tag.put(TAG_THEORY_CATEGORY_TOTALS, totalsTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.theorySessionActive = tag.getBoolean(TAG_THEORY_ACTIVE);
        this.theoryInspiration = Math.max(0, tag.getInt(TAG_THEORY_INSPIRATION));
        this.theoryInspirationStart = Math.max(0, tag.getInt(TAG_THEORY_INSPIRATION_START));
        this.theoryDraftCount = Math.max(0, tag.getInt(TAG_THEORY_DRAFT_COUNT));
        this.theoryCategoryTotals.clear();

        ListTag totalsTag = tag.getList(TAG_THEORY_CATEGORY_TOTALS, Tag.TAG_COMPOUND);
        for (int i = 0; i < totalsTag.size(); i++) {
            CompoundTag totalTag = totalsTag.getCompound(i);
            String category = totalTag.getString(TAG_CATEGORY);
            int total = totalTag.getInt(TAG_TOTAL);
            if (!category.isBlank() && total > 0) {
                this.theoryCategoryTotals.put(category, total);
            }
        }

        if (!this.theorySessionActive && (!this.theoryCategoryTotals.isEmpty() || this.theoryInspirationStart > 0 || this.theoryInspiration > 0)) {
            this.theorySessionActive = true;
        }
        if (this.theorySessionActive && this.theoryInspirationStart <= 0) {
            this.theoryInspirationStart = BASE_INSPIRATION;
            this.theoryInspiration = Math.max(this.theoryInspiration, this.theoryInspirationStart);
        }
        if (this.theoryInspiration > this.theoryInspirationStart) {
            this.theoryInspiration = this.theoryInspirationStart;
        }
    }
}
