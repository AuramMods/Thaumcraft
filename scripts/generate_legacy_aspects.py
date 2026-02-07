#!/usr/bin/env python3
"""Generate data-driven aspect entries from legacy 1.12.2 ConfigAspects declarations.

This script extracts the subset that can be mapped with high confidence into the
current 1.20.1 placeholder registry:
- Direct Thaumcraft item/block selectors from ItemsTC / BlocksTC.
- Legacy string-object selectors as strict path rules (path_contains_all).
"""

from __future__ import annotations

import json
import re
from collections import defaultdict
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, Iterable, List

ROOT = Path(__file__).resolve().parents[1]
CONFIG_PATH = ROOT / "old-1.12.2/thaumcraft/common/config/ConfigAspects.java"
LEGACY_IDS_PATH = ROOT / "src/main/java/art/arcane/thaumcraft/common/registry/LegacyContentRegistryIds.java"
BASE_ASPECTS_PATH = ROOT / "src/main/resources/data/thaumcraft/thaumcraft/aspects/base.json"
OUTPUT_PATH = ROOT / "src/main/resources/data/thaumcraft/thaumcraft/aspects/legacy_config_aspects.json"

ASPECT_MAP = {
    "AIR": "aer",
    "EARTH": "terra",
    "FIRE": "ignis",
    "WATER": "aqua",
    "ORDER": "ordo",
    "ENTROPY": "perditio",
    "VOID": "vacuos",
    "LIGHT": "lux",
    "FLUX": "vitium",
    "DARKNESS": "tenebrae",
    "ELDRITCH": "alienis",
    "FLIGHT": "volatus",
    "MAGIC": "praecantatio",
    "AURA": "auram",
    "ALCHEMY": "alkimia",
    "ENERGY": "potentia",
    "EXCHANGE": "permutatio",
    "COLD": "gelum",
    "CRYSTAL": "vitreus",
    "METAL": "metallum",
    "LIFE": "victus",
    "DEATH": "mortuus",
    "PLANT": "herba",
    "MOTION": "motus",
    "TOOL": "instrumentum",
    "CRAFT": "fabrico",
    "MECHANISM": "machina",
    "TRAP": "vinculum",
    "SOUL": "spiritus",
    "MIND": "cognitio",
    "SENSES": "sensus",
    "AVERSION": "aversio",
    "PROTECT": "praemunio",
    "DESIRE": "desiderium",
    "UNDEAD": "exanimis",
    "BEAST": "bestia",
    "MAN": "humanus",
}

FIELD_ID_OVERRIDES = {
    "crystal_air": "crystal_aer",
    "crystal_fire": "crystal_ignis",
    "crystal_water": "crystal_aqua",
    "crystal_earth": "crystal_terra",
    "crystal_order": "crystal_ordo",
    "crystal_entropy": "crystal_perditio",
    "crystal_taint": "crystal_vitium",
    "taint_block": "taint",
}

# Only mappings with a stable 1:1 ID in the current placeholder registry.
STRING_TO_TC_ID = {
    "oreQuartz": "ore_quartz",
    "oreCinnabar": "ore_cinnabar",
    "oreAmber": "ore_amber",
    "gemAmber": "amber",
    "quicksilver": "quicksilver",
}

SPECIAL_WORD_SPLITS = {
    "enderpearl": ["ender", "pearl"],
    "netherstar": ["nether", "star"],
    "sugarcane": ["sugar", "cane"],
    "slimeball": ["slime", "ball"],
    "cobblestone": ["cobble", "stone"],
}

SELECTOR_RE = re.compile(r"register(?:Complex)?ObjectTag\((.+?),\s*\(new AspectList")
ASPECT_RE = re.compile(r"\.(?:add|merge)\(Aspect\.([A-Z_]+),\s*([0-9]+)\)")
ITEMSTACK_META_RE = re.compile(r"ItemStack\([^,]+,\s*\d+\s*,\s*(\d+)\)")
FIELD_RE = re.compile(r"(?:ItemsTC|BlocksTC)\.([A-Za-z0-9_]+)")
STRING_RE = re.compile(r'"([^"]+)"')


@dataclass(frozen=True)
class ItemCandidate:
    item_id: str
    meta: int | None
    aspects: Dict[str, int]
    line_no: int


def parse_legacy_ids() -> set[str]:
    text = LEGACY_IDS_PATH.read_text(encoding="utf-8")
    return set(re.findall(r'"([a-z0-9_]+)"', text))


def read_existing_item_ids() -> set[str]:
    root = json.loads(BASE_ASPECTS_PATH.read_text(encoding="utf-8"))
    existing = set()
    for entry in root.get("entries", []):
        item = entry.get("item")
        if isinstance(item, str) and item.startswith("thaumcraft:"):
            existing.add(item.split(":", 1)[1])
    return existing


def camel_to_snake(name: str) -> str:
    # Handles legacy field names such as crimsonPlateChest -> crimson_plate_chest.
    return re.sub(r"(?<!^)(?=[A-Z])", "_", name).lower()


def parse_aspects(line: str) -> Dict[str, int]:
    aspects: Dict[str, int] = {}
    for legacy_name, amount_raw in ASPECT_RE.findall(line):
        mapped = ASPECT_MAP.get(legacy_name)
        if mapped is None:
            continue
        amount = int(amount_raw)
        if amount <= 0:
            continue
        aspects[mapped] = aspects.get(mapped, 0) + amount
    return aspects


def extract_selector(line: str) -> str | None:
    match = SELECTOR_RE.search(line)
    return match.group(1).strip() if match else None


def extract_meta(selector: str) -> int | None:
    match = ITEMSTACK_META_RE.search(selector)
    return int(match.group(1)) if match else None


def normalize_needles_from_key(key: str) -> List[str]:
    lowered = key.lower()
    if lowered in SPECIAL_WORD_SPLITS:
        words = SPECIAL_WORD_SPLITS[lowered][:]
    else:
        words = re.findall(r"[A-Z]+(?=[A-Z][a-z]|$)|[A-Z]?[a-z]+|[0-9]+", key)
        words = [w.lower() for w in words]

    if not words:
        return []

    if words[0] in {"block", "item", "crop", "tree"} and len(words) > 1:
        words = words[1:]

    if words == ["log", "wood"]:
        words = ["log"]

    words = ["leaf" if w == "leaves" else w for w in words]

    # Remove immediate duplicates while preserving order.
    deduped: List[str] = []
    for word in words:
        if not word:
            continue
        if deduped and deduped[-1] == word:
            continue
        deduped.append(word)
    return deduped


def choose_candidate(candidates: Iterable[ItemCandidate]) -> ItemCandidate:
    # Prefer wildcard/unspecified metadata selectors for parity.
    # Fallback to strongest total-aspect selector; tie-break by earliest declaration.
    def score(candidate: ItemCandidate) -> tuple[int, int, int]:
        wildcard_priority = 1 if candidate.meta in (None, 32767) else 0
        total = sum(candidate.aspects.values())
        return (wildcard_priority, total, -candidate.line_no)

    return max(candidates, key=score)


def ordered_aspect_map(aspects: Dict[str, int]) -> Dict[str, int]:
    return dict(sorted(aspects.items(), key=lambda kv: kv[0]))


def main() -> None:
    all_ids = parse_legacy_ids()
    existing_ids = read_existing_item_ids()
    lines = CONFIG_PATH.read_text(encoding="utf-8").splitlines()

    item_candidates: dict[str, list[ItemCandidate]] = defaultdict(list)
    path_rules: dict[tuple[str, ...], Dict[str, int]] = {}

    skipped_unknown_fields = 0
    skipped_unknown_aspect_lines = 0

    for line_no, raw in enumerate(lines, start=1):
        line = raw.strip()
        if "registerObjectTag(" not in line and "registerComplexObjectTag(" not in line:
            continue

        aspects = parse_aspects(line)
        if not aspects:
            skipped_unknown_aspect_lines += 1
            continue

        selector = extract_selector(line)
        if not selector:
            continue

        field_match = FIELD_RE.search(selector)
        if field_match:
            item_id = camel_to_snake(field_match.group(1))
            item_id = FIELD_ID_OVERRIDES.get(item_id, item_id)
            if item_id not in all_ids:
                skipped_unknown_fields += 1
                continue

            item_candidates[item_id].append(
                ItemCandidate(
                    item_id=item_id,
                    meta=extract_meta(selector),
                    aspects=aspects,
                    line_no=line_no,
                )
            )
            continue

        string_match = STRING_RE.match(selector)
        if string_match:
            key = string_match.group(1)

            direct_id = STRING_TO_TC_ID.get(key)
            if direct_id:
                item_candidates[direct_id].append(
                    ItemCandidate(
                        item_id=direct_id,
                        meta=None,
                        aspects=aspects,
                        line_no=line_no,
                    )
                )
                continue

            needles = normalize_needles_from_key(key)
            if not needles:
                continue

            selector_key = tuple(needles)
            merged = path_rules.get(selector_key)
            if merged is None:
                path_rules[selector_key] = dict(aspects)
            else:
                for aspect, amount in aspects.items():
                    merged[aspect] = max(merged.get(aspect, 0), amount)

    entries: list[dict] = []

    for item_id in sorted(item_candidates):
        if item_id in existing_ids:
            continue
        chosen = choose_candidate(item_candidates[item_id])
        entries.append(
            {
                "item": f"thaumcraft:{item_id}",
                "aspects": ordered_aspect_map(chosen.aspects),
            }
        )

    for needles in sorted(path_rules.keys()):
        entries.append(
            {
                "path_contains_all": list(needles),
                "aspects": ordered_aspect_map(path_rules[needles]),
            }
        )

    out = {"entries": entries}
    OUTPUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT_PATH.write_text(json.dumps(out, indent=2) + "\n", encoding="utf-8")

    print(f"Generated {OUTPUT_PATH}")
    print(f"  item entries: {sum(1 for e in entries if 'item' in e)}")
    print(f"  path_contains_all entries: {sum(1 for e in entries if 'path_contains_all' in e)}")
    print(f"  skipped unknown field mappings: {skipped_unknown_fields}")
    print(f"  lines without parseable aspects: {skipped_unknown_aspect_lines}")


if __name__ == "__main__":
    main()
