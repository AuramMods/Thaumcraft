package art.arcane.thaumcraft.common.world.aura;

import net.minecraft.util.Mth;

public final class AuraChunkData {

    public static final int MAX_BASE = 500;
    public static final float MAX_AURA_VALUE = 32766.0F;

    private int base;
    private float vis;
    private float flux;

    public AuraChunkData(int base, float vis, float flux) {
        this.base = clampBase(base);
        this.vis = clampAuraValue(vis);
        this.flux = clampAuraValue(flux);
    }

    public static AuraChunkData withDefaults(int base) {
        int clampedBase = clampBase(base);
        return new AuraChunkData(clampedBase, clampedBase, 0.0F);
    }

    public int getBase() {
        return this.base;
    }

    public void setBase(int base) {
        this.base = clampBase(base);
    }

    public float getVis() {
        return this.vis;
    }

    public void setVis(float vis) {
        this.vis = clampAuraValue(vis);
    }

    public float getFlux() {
        return this.flux;
    }

    public void setFlux(float flux) {
        this.flux = clampAuraValue(flux);
    }

    public float addVis(float amount) {
        float next = clampAuraValue(this.vis + amount);
        float delta = next - this.vis;
        this.vis = next;
        return delta;
    }

    public float addFlux(float amount) {
        float next = clampAuraValue(this.flux + amount);
        float delta = next - this.flux;
        this.flux = next;
        return delta;
    }

    public static int clampBase(int value) {
        return Mth.clamp(value, 0, MAX_BASE);
    }

    public static float clampAuraValue(float value) {
        return Mth.clamp(value, 0.0F, MAX_AURA_VALUE);
    }
}
