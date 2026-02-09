package art.arcane.thaumcraft.common.item.armor;

public final class ArmorPortingNotes {
    // TODO(port): Replace placeholder item registrations for armor ids with real ArmorItem classes:
    // TODO(port): thaumium_* , cloth_* , traveller_boots , fortress_* , void_* , void_robe_* , crimson_* , goggles.
    // TODO(port): current baseline has dedicated classes for `void_*`, `void_robe_*`, `crimson_boots`, and `crimson_robe_*` with legacy-shaped warp values.

    // TODO(port): Port legacy armor behavior by class family:
    // TODO(port): ItemThaumiumArmor -> standard equipable armor, thaumium repair material, thaumium_1/thaumium_2 textures.
    // TODO(port): ItemRobeArmor -> dyeable robes, robe overlay textures, vis discount values, cauldron color washing.
    // TODO(port): ItemGoggles -> vis discount + revealer behavior (nodes/popups) and head-slot render parity.
    // TODO(port): ItemBootsTraveller -> rechargeable movement assist (speed/step/fall handling) with charge HUD support.
    // TODO(port): ItemVoidArmor -> passive self-repair tick and warping gear value.
    // TODO(port): ItemVoidRobeArmor -> self-repair + vis discount + revealer behavior + warping value + robe tint/overlay behavior.
    // TODO(port): ItemFortressArmor -> custom model, set bonus mitigation logic, optional goggles flag and mask metadata behavior.
    // TODO(port): ItemCultistBoots / ItemCultistRobeArmor -> vis discount + warping values and cultist textures.
    // TODO(port): ItemCultistPlateArmor / ItemCultistLeaderArmor -> equipable armor parity and custom model/texture wiring.

    // TODO(port): Migrate legacy render/model sources:
    // TODO(port): item models from old-1.12.2/assets/thaumcraft/models/item/*.json
    // TODO(port): armor textures from old-1.12.2/assets/thaumcraft/textures/models/armor/*.png

    private ArmorPortingNotes() {
    }
}
