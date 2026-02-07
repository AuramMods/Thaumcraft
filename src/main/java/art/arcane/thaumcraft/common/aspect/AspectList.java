package art.arcane.thaumcraft.common.aspect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class AspectList {

    private static final String ASPECT_KEY = "key";
    private static final String ASPECT_AMOUNT = "amount";

    private final EnumMap<AspectType, Integer> aspects = new EnumMap<>(AspectType.class);

    public AspectList add(AspectType aspect, int amount) {
        if (aspect == null || amount <= 0) {
            return this;
        }

        this.aspects.merge(aspect, amount, Integer::sum);
        return this;
    }

    public AspectList addAll(AspectList other) {
        if (other == null || other.isEmpty()) {
            return this;
        }

        for (Map.Entry<AspectType, Integer> entry : other.aspects.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public int get(AspectType aspect) {
        return this.aspects.getOrDefault(aspect, 0);
    }

    public int remove(AspectType aspect, int amount) {
        if (aspect == null || amount <= 0) {
            return 0;
        }

        int current = this.aspects.getOrDefault(aspect, 0);
        int removed = Math.min(current, amount);
        if (removed <= 0) {
            return 0;
        }

        int updated = current - removed;
        if (updated <= 0) {
            this.aspects.remove(aspect);
        } else {
            this.aspects.put(aspect, updated);
        }

        return removed;
    }

    public boolean containsAtLeast(AspectList required) {
        if (required == null || required.isEmpty()) {
            return true;
        }

        for (Map.Entry<AspectType, Integer> entry : required.aspects.entrySet()) {
            if (get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    public boolean removeAll(AspectList required) {
        if (!containsAtLeast(required)) {
            return false;
        }

        for (Map.Entry<AspectType, Integer> entry : required.aspects.entrySet()) {
            remove(entry.getKey(), entry.getValue());
        }

        return true;
    }

    public int totalAmount() {
        int total = 0;
        for (int amount : this.aspects.values()) {
            total += amount;
        }
        return total;
    }

    public boolean isEmpty() {
        return this.aspects.isEmpty();
    }

    public AspectList copy() {
        AspectList copy = new AspectList();
        copy.aspects.putAll(this.aspects);
        return copy;
    }

    public AspectList scaled(int factor) {
        if (factor <= 1) {
            return copy();
        }

        AspectList scaled = new AspectList();
        for (Map.Entry<AspectType, Integer> entry : this.aspects.entrySet()) {
            scaled.add(entry.getKey(), entry.getValue() * factor);
        }
        return scaled;
    }

    public Map<AspectType, Integer> asMap() {
        return Collections.unmodifiableMap(this.aspects);
    }

    public void saveToTag(CompoundTag parent, String key) {
        ListTag listTag = new ListTag();
        for (Map.Entry<AspectType, Integer> entry : this.aspects.entrySet()) {
            CompoundTag aspectTag = new CompoundTag();
            aspectTag.putString(ASPECT_KEY, entry.getKey().getTag());
            aspectTag.putInt(ASPECT_AMOUNT, entry.getValue());
            listTag.add(aspectTag);
        }
        parent.put(key, listTag);
    }

    public static AspectList loadFromTag(CompoundTag parent, String key) {
        AspectList list = new AspectList();
        ListTag listTag = parent.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag aspectTag = listTag.getCompound(i);
            AspectType aspect = AspectType.byTag(aspectTag.getString(ASPECT_KEY));
            if (aspect != null) {
                list.add(aspect, aspectTag.getInt(ASPECT_AMOUNT));
            }
        }
        return list;
    }
}
