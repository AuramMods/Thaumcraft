package art.arcane.thaumcraft.common.aspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AspectObjectRegistry extends SimpleJsonResourceReloadListener {
    // TODO(port): Complete legacy ConfigAspects coverage:
    // TODO(port): support selectors equivalent to direct ItemStack registrations (metadata-sensitive and wildcard entries), not only id/tag/path heuristics.
    // TODO(port): add optional NBT-sensitive matching for edge-case object tags used by legacy complex registrations.

    private static final Gson GSON = new GsonBuilder().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String RESOURCE_PATH = "thaumcraft/aspects";
    private static final AspectObjectRegistry INSTANCE = new AspectObjectRegistry();

    private volatile Snapshot snapshot = Snapshot.empty();

    private AspectObjectRegistry() {
        super(GSON, RESOURCE_PATH);
    }

    public static AspectObjectRegistry getInstance() {
        return INSTANCE;
    }

    public AspectList resolveAspects(ItemStack stack) {
        if (stack.isEmpty()) {
            return new AspectList();
        }

        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return new AspectList();
        }

        Snapshot current = this.snapshot;
        AspectList exact = current.itemAspects().get(itemId);
        if (exact != null) {
            return exact.copy();
        }

        AspectList resolved = new AspectList();
        for (TagRule rule : current.tagRules()) {
            if (stack.is(rule.tag())) {
                resolved.addAll(rule.aspects());
            }
        }

        String path = itemId.getPath();
        for (PathRule rule : current.pathRules()) {
            if (path.contains(rule.pathContains())) {
                resolved.addAll(rule.aspects());
            }
        }
        for (PathAllRule rule : current.pathAllRules()) {
            boolean matches = true;
            for (String needle : rule.pathContainsAll()) {
                if (!path.contains(needle)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                resolved.addAll(rule.aspects());
            }
        }

        return resolved;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, AspectList> itemAspects = new LinkedHashMap<>();
        List<TagRule> tagRules = new ArrayList<>();
        List<PathRule> pathRules = new ArrayList<>();
        List<PathAllRule> pathAllRules = new ArrayList<>();

        List<Map.Entry<ResourceLocation, JsonElement>> sortedEntries = new ArrayList<>(objects.entrySet());
        sortedEntries.sort(Comparator.comparing(entry -> entry.getKey().toString()));

        for (Map.Entry<ResourceLocation, JsonElement> dataEntry : sortedEntries) {
            JsonObject root = GsonHelper.convertToJsonObject(dataEntry.getValue(), dataEntry.getKey().toString());
            JsonArray entries = GsonHelper.getAsJsonArray(root, "entries", new JsonArray());
            for (int i = 0; i < entries.size(); i++) {
                JsonObject entry = GsonHelper.convertToJsonObject(entries.get(i), "entries[" + i + "]");
                AspectList aspects = parseAspectList(entry, dataEntry.getKey() + " entries[" + i + "]");
                if (aspects.isEmpty()) {
                    continue;
                }

                if (entry.has("item")) {
                    registerSingleItem(itemAspects, GsonHelper.getAsString(entry, "item"), aspects, dataEntry.getKey(), i);
                    continue;
                }
                if (entry.has("items")) {
                    JsonArray items = GsonHelper.getAsJsonArray(entry, "items");
                    for (int j = 0; j < items.size(); j++) {
                        String itemId = GsonHelper.convertToString(items.get(j), "items[" + j + "]");
                        registerSingleItem(itemAspects, itemId, aspects, dataEntry.getKey(), i);
                    }
                    continue;
                }
                if (entry.has("tag")) {
                    String tagName = GsonHelper.getAsString(entry, "tag");
                    TagKey<Item> tag = parseItemTag(tagName);
                    if (tag != null) {
                        tagRules.add(new TagRule(tag, aspects.copy()));
                    } else {
                        LOGGER.warn("Ignoring invalid item tag '{}' in {}", tagName, dataEntry.getKey());
                    }
                    continue;
                }
                if (entry.has("path_contains")) {
                    String needle = GsonHelper.getAsString(entry, "path_contains");
                    if (!needle.isBlank()) {
                        pathRules.add(new PathRule(needle.toLowerCase(), aspects.copy()));
                    }
                    continue;
                }
                if (entry.has("path_contains_any")) {
                    JsonArray needles = GsonHelper.getAsJsonArray(entry, "path_contains_any");
                    for (int j = 0; j < needles.size(); j++) {
                        String needle = GsonHelper.convertToString(needles.get(j), "path_contains_any[" + j + "]");
                        if (!needle.isBlank()) {
                            pathRules.add(new PathRule(needle.toLowerCase(), aspects.copy()));
                        }
                    }
                    continue;
                }
                if (entry.has("path_contains_all")) {
                    JsonArray needles = GsonHelper.getAsJsonArray(entry, "path_contains_all");
                    List<String> required = new ArrayList<>();
                    for (int j = 0; j < needles.size(); j++) {
                        String needle = GsonHelper.convertToString(needles.get(j), "path_contains_all[" + j + "]");
                        if (!needle.isBlank()) {
                            required.add(needle.toLowerCase());
                        }
                    }
                    if (!required.isEmpty()) {
                        pathAllRules.add(new PathAllRule(List.copyOf(required), aspects.copy()));
                    }
                    continue;
                }

                LOGGER.warn("Ignoring aspect entry without selector in {} entries[{}]", dataEntry.getKey(), i);
            }
        }

        this.snapshot = new Snapshot(
                Map.copyOf(itemAspects),
                List.copyOf(tagRules),
                List.copyOf(pathRules),
                List.copyOf(pathAllRules)
        );
        AspectRegistry.invalidateCaches();
        LOGGER.info("Loaded aspect registry: {} exact item entries, {} tag rules, {} path-any rules, {} path-all rules",
                itemAspects.size(), tagRules.size(), pathRules.size(), pathAllRules.size());
    }

    private static AspectList parseAspectList(JsonObject entry, String context) {
        if (!entry.has("aspects")) {
            LOGGER.warn("Ignoring aspect entry without aspects object at {}", context);
            return new AspectList();
        }

        JsonObject aspectsObject = GsonHelper.getAsJsonObject(entry, "aspects");
        AspectList list = new AspectList();
        for (Map.Entry<String, JsonElement> aspectEntry : aspectsObject.entrySet()) {
            AspectType aspect = AspectType.byTag(aspectEntry.getKey());
            if (aspect == null) {
                LOGGER.warn("Unknown aspect '{}' in {}", aspectEntry.getKey(), context);
                continue;
            }

            int amount = GsonHelper.convertToInt(aspectEntry.getValue(), aspectEntry.getKey());
            if (amount > 0) {
                list.add(aspect, amount);
            }
        }
        return list;
    }

    private static void registerSingleItem(Map<ResourceLocation, AspectList> itemAspects, String itemName, AspectList aspects,
                                           ResourceLocation source, int entryIndex) {
        ResourceLocation itemId = ResourceLocation.tryParse(itemName);
        if (itemId == null) {
            LOGGER.warn("Ignoring invalid item id '{}' in {} entries[{}]", itemName, source, entryIndex);
            return;
        }
        itemAspects.put(itemId, aspects.copy());
    }

    private static TagKey<Item> parseItemTag(String rawTag) {
        String cleaned = rawTag.startsWith("#") ? rawTag.substring(1) : rawTag;
        ResourceLocation id = ResourceLocation.tryParse(cleaned);
        if (id == null) {
            return null;
        }
        return TagKey.create(Registries.ITEM, id);
    }

    private record TagRule(TagKey<Item> tag, AspectList aspects) {
    }

    private record PathRule(String pathContains, AspectList aspects) {
    }

    private record PathAllRule(List<String> pathContainsAll, AspectList aspects) {
    }

    private record Snapshot(Map<ResourceLocation, AspectList> itemAspects,
                            List<TagRule> tagRules,
                            List<PathRule> pathRules,
                            List<PathAllRule> pathAllRules) {
        private static Snapshot empty() {
            return new Snapshot(new HashMap<>(), List.of(), List.of(), List.of());
        }
    }
}
