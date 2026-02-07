package art.arcane.thaumcraft.common.aspect;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AspectType {
    AER("aer", 0xFFFF7E),
    TERRA("terra", 0x56C840),
    IGNIS("ignis", 0xFF5A01),
    AQUA("aqua", 0x3CDDFF),
    ORDO("ordo", 0xD5D4EC),
    PERDITIO("perditio", 0x404040),
    VACUOS("vacuos", 0x888888),
    LUX("lux", 0xFFFFC0),
    VITIUM("vitium", 0x800080),
    TENEBRAE("tenebrae", 0x222222),
    ALIENIS("alienis", 0x805080),
    VOLATUS("volatus", 0xE7E7D7),
    PRAECANTATIO("praecantatio", 0xCE78F2),
    AURAM("auram", 0xFFC0C0),
    ALKIMIA("alkimia", 0x23ADBD),
    POTENTIA("potentia", 0xC100FF),
    PERMUTATIO("permutatio", 0x578357),
    GELUM("gelum", 0xE1FFFF),
    VITREUS("vitreus", 0x80FFFF),
    METALLUM("metallum", 0xA9A9A9),
    VICTUS("victus", 0xE6A85F),
    MORTUUS("mortuus", 0x6A6A6A),
    HERBA("herba", 0x1B8E2D),
    MOTUS("motus", 0xA5A5FF),
    INSTRUMENTUM("instrumentum", 0x4040CE),
    FABRICO("fabrico", 0x8080A0),
    MACHINA("machina", 0x8083A0),
    VINCULUM("vinculum", 0x9A8080),
    SPIRITUS("spiritus", 0xE7D7BB),
    COGNITIO("cognitio", 0xF8D847),
    SENSUS("sensus", 0xC0FFC0),
    AVERSIO("aversio", 0xC05050),
    PRAEMUNIO("praemunio", 0x00C0C0),
    DESIDERIUM("desiderium", 0xE0E0A0),
    EXANIMIS("exanimis", 0x3A3A80),
    BESTIA("bestia", 0x9F6409),
    HUMANUS("humanus", 0xFFDCC0);

    private static final Map<String, AspectType> BY_TAG = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(AspectType::getTag, Function.identity()));

    private final String tag;
    private final int color;

    AspectType(String tag, int color) {
        this.tag = tag;
        this.color = color;
    }

    public String getTag() {
        return this.tag;
    }

    public int getColor() {
        return this.color;
    }

    public static AspectType byTag(String tag) {
        return BY_TAG.get(tag);
    }
}
