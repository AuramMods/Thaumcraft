# Thaumcraft 1.12.2 -> 1.20.1 Porting Plan

This file tracks the migration from legacy source in `old-1.12.2` into the active Forge 1.20.1 project.

Migration manifest file: `PORTING_MANIFEST.md`

## Current Baseline

- Legacy source root: `old-1.12.2`
- Active source root: `src/main`
- 1.20.1 bootstrap class: `src/main/java/art/arcane/thaumcraft/Thaumcraft.java`
- Compile checkpoint command: `./gradlew compileJava -q`
- Data checkpoint command: `./gradlew runData`
- Last updated snapshot: `2026-02-07`

## Legacy Scope Snapshot

- Java files in `old-1.12.2/thaumcraft`: 823
- Legacy blockstate json files: 119
- Legacy item model json files: 126
- Legacy block textures: 187
- Legacy item textures: 215
- Legacy registered block ids (from `ConfigBlocks`): 97 (94 init blocks + 3 fluid-style blocks)
- Legacy registered item handles (from `ConfigItems`): 99
- Legacy research json files: 7

## Phases

### Phase 0: Foundation and Migration Map

Goal: make the 1.20.1 project structurally ready for high-volume porting.

- [x] Create and commit this migration plan (`PORTING.md`)
- [x] Align namespace and resource/data layout to `thaumcraft`
- [x] Split bootstrap registration into dedicated registries (items, blocks, block entities, creative tabs)
- [x] Add datagen scaffolding (models, blockstates, lang, tags, recipes)
- [x] Generate migration manifest from `old-1.12.2` registration surfaces
- [x] Verify compile and datagen checkpoints

Exit criteria:

- `compileJava` passes
- `runData` passes
- No stale bootstrap-only registration structure
- Manifest exists and is usable as migration inventory

### Phase 1: Blocks and Items in Game (Textured)

Goal: bring large amounts of static content into 1.20.1 quickly.

- Port static/simple blocks and items first (resources, ores, base materials, wood/stone families)
- Register block items and creative tab population
- Reuse legacy textures and models where compatible
- Use generated placeholders for missing models/lang entries

Exit criteria:

- First content batch appears in-game in creative tab
- No missing model spam for migrated batch
- Compile + datagen checkpoints pass per batch

Phase 1 status:

- [x] Registered 97 legacy block IDs as placeholder 1.20.1 blocks
- [x] Registered 99 legacy item IDs as placeholder 1.20.1 items
- [x] Registered block items for all migrated block IDs
- [x] Copied legacy block/item texture sets into `assets/thaumcraft/textures`
- [x] Generated blockstates, block models, item models, and language entries via datagen
- [x] Added texture alias mapping so generated models resolve to Thaumcraft textures (no paper/stone fallback)
- [x] In-client smoke test for creative tab registration and item names
- [x] Known visual gap logged: many non-cube legacy models render incorrectly (expected before custom geometry/model pass)
- [x] First non-cube model conversion pass completed for: `pedestal`, `infusion_matrix`, `arcane_workbench`, `crucible`, `jar_normal`, `jar_void`, `jar_brain`
- [x] Added first-pass item display transforms for converted non-cube block items (inventory + hand sizing baseline)
- [ ] Remaining non-cube model parity and advanced render polish are still pending

### Phase 2: Functional Block Entities and Menus

Goal: restore core stations before deeper systems.

- Arcane Workbench
- Research Table
- Crucible
- Infusion Matrix

Phase 2 status:

- [x] Replaced target block IDs with `StationBlock` (`BaseEntityBlock`) implementations
- [x] Added block entities for all four stations and registered `BlockEntityType`s
- [x] Added per-station menu types/classes and packet constructor wiring (`arcane_workbench`, `research_table`, `crucible`, `infusion_matrix`)
- [x] Added client screen registration for all four menu types using station-specific screen constructors
- [x] Added server tick + container data sync baseline for station UIs
- [x] Compile/data checkpoints pass (`./gradlew compileJava -q`, `./gradlew runData --no-daemon`)
- [x] In-client verification for placement, opening, and slot transfer behavior on all four stations
- [x] Phase 2B complete: split shared station container/menu path into per-station menu and block-entity menu bindings
- [x] Arcane Workbench now has functional 3x3 crafting flow (computed output slot + ingredient consumption + remainder handling)
- [x] Research Table baseline slot constraints added (`scribing_tools` slot + `paper` slot)
- [x] Crucible baseline tick mechanics added (heat detection, water bucket fill, hot-state item processing, persisted water/heat/essentia state)
- [x] Arcane Workbench now gates recipe output by local aura vis and spends vis when crafting output is taken
- [x] Infusion Matrix now performs baseline altar structure scans (central + ring pedestal detection) and syncs readiness/activity state
- [x] `pedestal` is now a block entity with one-item place/take interaction and persisted inventory
- [x] Infusion Matrix now tracks owned pedestals and pulls one item per owned pedestal into persisted internal capture slots (with source-position tracking)
- [x] Infusion Matrix now runs a baseline infusion state machine (requested start via matrix slot, timed cycle, vis drain per cycle tick, abort/fail handling)
- [x] Infusion Matrix now computes a structural stability score and can fail active cycles based on instability (with flux penalty)
- [x] Arcane Workbench now requires and consumes a crystal item via dedicated crystal slot (required crystal derived from dominant recipe aspects)
- [x] Arcane Workbench parity-tuning pass: vis/crystal costs now apply only to Thaumcraft-output crafting results; non-Thaumcraft crafting no longer hard-requires arcane resources
- [ ] Arcane Workbench crystal/vis balancing and exact legacy parity tuning are still pending
- [ ] Exact legacy parity for station mechanics is still pending (arcane workbench balancing, full infusion recipe parity, full research table progression)

Exit criteria:

- Placement, ticking, menu opening, and sync behavior work in singleplayer

### Phase 3: Core Progression Systems

Goal: establish progression backbone.

- Aspects
- Aura storage and world/chunk persistence
- Salis Mundus triggers
- Initial scanning hooks

Phase 3 status:

- [x] Added chunk-scoped aura persistence baseline (`AuraSavedData`) with `base/vis/flux` saved per chunk
- [x] Added 1.20.1 aura API surface (`AuraManager`) for `get/add/drain` of vis and flux
- [x] Added aura chunk value clamps aligned with legacy ceilings (`base <= 500`, `vis/flux <= 32766`)
- [x] Added initial aspect model/registry scaffolding (`AspectType`, `AspectList`, `AspectRegistry`) with 1.20.1-friendly fallback heuristics
- [x] Added reloadable data-driven aspect object registry (`data/*/thaumcraft/aspects/*.json`) with exact item, item-tag, and path selector rules
- [x] Expanded `AspectType` to full legacy tag catalog so registry data can represent legacy aspect families directly
- [x] Seeded first data pack for aspect object mappings (`data/thaumcraft/thaumcraft/aspects/base.json`) plus initial item aspect tag groups
- [x] Added `path_contains_all` selector support to aspect data loading for stricter legacy object-tag matching
- [x] Imported a large parseable subset of legacy `ConfigAspects` into data (`legacy_config_aspects.json`: 18 exact item entries + 80 strict path rules) and removed duplicate broad path fallback rules from `base.json`
- [x] Hooked crucible baseline processing to generate flux into chunk aura
- [x] Hooked crucible baseline processing to derive essentia from aspect totals instead of generic item heuristics
- [x] Added first Salis Mundus progression trigger scaffold (RMB crafting table -> arcane workbench conversion)
- [x] Expanded Salis Mundus simple trigger set (bookshelf -> thaumonomicon grant/unlock, crafting table -> arcane workbench, cauldron -> crucible) with progression gating
- [x] Added first-pass Salis Mundus multiblock trigger handling for `infusion` altar assembly, `thaumatorium`, `infernal_furnace`, and `golem_builder`
- [x] Added player knowledge persistence baseline (`PlayerKnowledgeSavedData`) and first thaumometer scan hook for block scanning
- [x] Expanded thaumometer scanning baseline to entity and item surfaces, including persisted unique scan tracking for blocks/items/entities
- [ ] Full legacy object-tag migration from `ConfigAspects` is still pending (remaining direct vanilla `ItemStack(...)` mappings and edge-case parity audit)
- [ ] Salis Mundus trigger set is still incomplete (legacy multiblock variant parity is still pending, especially infusion altar stone/pillar/pedestal variants and exact placeholder transformation semantics)
- [ ] Full research/knowledge progression model is still pending (current hook is baseline-only)

Exit criteria:

- Early thaumaturgy progression path is functional and persisted

### Phase 4: Alchemy and Essentia Pipeline

Goal: make alchemy gameplay loop functional.

- Crucible recipe type and serializer
- Essentia storage/transport baseline
- Jars and first alchemy recipe set

Phase 4 status:

- [x] Added custom `thaumcraft:crucible` recipe type + serializer (JSON + network sync)
- [x] Wired crucible processing to execute matching crucible recipes before fallback dissolution
- [x] Switched crucible essentia storage from scalar units to persisted per-aspect pool
- [x] Added starter crucible recipe set (`alumentum`, `nitor`, `salis_mundus`)
- [x] Crucible interaction now follows in-world flow (no station GUI path): item offering, bottle/phial transfer, and shift-right-click dump-to-flux
- [x] Crucible now exposes visual state overlays for water + heat (`water`/`heated` blockstate) and accepts `nitor` as a heat source
- [x] Added jar block-entity baseline for `jar_normal`, `jar_void`, and `jar_brain` with persisted aspect storage
- [x] Added manual phial transfer path between crucible and jars (glass bottle <-> phial <-> container, no direct item-to-jar dissolution)
- [x] Jar interaction now matches baseline TC flow (storage/transfer; no direct held-item dissolution path)
- [x] Added baseline passive transfer from crucible to adjacent jars (wide-first automation scaffold)
- [x] Added transitional quartz-sliver catalyst path (`thaumcraft:quartz_sliver` + `thaumcraft:catalysts/quartz_slivers` tag) and migrated Salis Mundus catalyst off raw `minecraft:quartz`.
- [x] Restored baseline vis-crystal crucible recipes (`crystal_aer`, `crystal_aqua`, `crystal_ignis`, `crystal_terra`, `crystal_ordo`, `crystal_perditio`, `crystal_vitium`) using quartz-sliver catalyst tag.
- [ ] Quartz sliver full legacy parity is still pending:
- [ ] legacy uses `nuggetQuartz` catalyst (quartz variant of `thaumcraft:nuggets`), not a dedicated standalone `quartz_sliver` item id.
- [ ] migrate compatibility item path to variant-container parity for nuggets/data components while preserving catalyst tag compatibility.
- [ ] Essentia transport automation is still pending (`alembic`, tubes, smelters, thaumatorium)

Exit criteria:

- Alchemy loop is craftable end-to-end

## Research Notes (2026-02-07 Direction Reset)

### Crucible + Quartz Sliver

- Legacy TC6 does not use a standalone `quartz_sliver` item id.
- Legacy quartz sliver behavior comes from `thaumcraft:nuggets` quartz subtype (`nuggetQuartz` ore dictionary entry, index 9 in legacy nugget variants).
- Legacy base crystal alchemy recipes use quartz sliver (`nuggetQuartz`) as the catalyst with per-aspect essentia costs.
- Current 1.20.1 port now uses transitional `thaumcraft:quartz_sliver` + catalyst tag (`thaumcraft:catalysts/quartz_slivers`) for salis mundus and base vis crystal crucible recipes.
- TODO(port): implement nugget subtype parity for `thaumcraft:nuggets` (or explicit split ids/data components) so quartz sliver semantics are canonical again.
- TODO(port): migrate compatibility item path back to nugget-variant-backed catalyst semantics while keeping datapack-facing catalyst tags stable.

### Bucket of Pure / Bucket of Death

- Legacy bucket items place custom source fluids and return an empty bucket (`bucket_pure` -> `purifying_fluid`, `bucket_death` -> `liquid_death`).
- Legacy allows source pickup into custom buckets via bucket fill event hooks.
- Legacy `purifying_fluid` source behavior grants Warp Ward to players touching it and then consumes the source block.
- Legacy `liquid_death` damages living entities on contact, scaled by fluid level.
- Legacy bath-salts path includes a cauldron conversion to `purifying_fluid` and Spa-driven spreading/mixing behavior.
- TODO(port): replace placeholder fluid blocks with real 1.20.1 fluid + bucket implementations for `purifying_fluid` and `liquid_death`.
- TODO(port): add bucket pickup hooks for both fluids (source-only behavior parity).
- TODO(port): wire purifying fluid to warp/insanity systems (`Warp Ward` interaction) once warp capability is ported.
- TODO(port): port bath-salts conversion and Spa mixing flow that produces/propagates purifying fluid.

### Model Parity Backlog (Explicit IDs)

- Legacy blockstate references are under `old-1.12.2/assets/thaumcraft/blockstates/<id>.json`.
- Activator Rail: `activator_rail`
- Alembic: `alembic`
- Arcane Ear: `arcane_ear`
- Workbench Charger: `arcane_workbench_charger`
- Bellows: `bellows`
- Brain Box: `brain_box`
- Candle: `candle`
- Centrifuge: `centrifuge`
- Dioptra: `dioptra`
- Essentia Input: `essentia_input`
- Essentia Output: `essentia_output`
- Everfull Urn: `everfull_urn`
- Golem Builder: `golem_builder`
- Infernal Furnace: `infernal_furnace`
- Lamp of Arcana: `lamp_arcane`
- Lamp of Fertility: `lamp_fertility`
- Lamp of Growth: `lamp_growth`
- Levitator: `levitator`
- Loot Crate: `loot_crate`
- Loot Urn: `loot_urn`
- Mirror: `mirror`
- Essentia Mirror: `mirror_essentia`
- Pattern Crafter: `pattern_crafter`
- Pillar: `pillar`
- Recharge Pedestal: `recharge_pedestal`
- Redstone Relay: `redstone_relay`
- Research Table: `research_table`
- Sapling: `sapling`
- Shimmerleaf: `shimmerleaf`
- Vishroom: `vishroom`
- Smelter Aux: `smelter_aux`
- Smelter Basic: `smelter_basic`
- Smelter Thaumium: `smelter_thaumium`
- Smelter Vent: `smelter_vent`
- Smelter Void: `smelter_void`
- Spa: `spa`
- Taint Fibre: `taint_fibre`
- Tube: `tube`
- Tube Buffer: `tube_buffer`
- Tube Filter: `tube_filter`
- Tube Oneway: `tube_oneway`
- Tube Restrict: `tube_restrict`
- Tube Valve: `tube_valve`
- Wand Workbench: `wand_workbench`

### Armor Porting Backlog

- Current 1.20.1 registration still uses generic placeholder items for armor ids; none currently equip as functional armor with TC-specific behavior.
- Legacy armor textures live under `old-1.12.2/assets/thaumcraft/textures/models/armor`.
- Legacy armor item models live under `old-1.12.2/assets/thaumcraft/models/item`.
- TODO(port): implement real armor classes and registration wiring for:
- TODO(port): `thaumium_*`, `cloth_*`, `traveller_boots`, `fortress_*`, `void_*`, `void_robe_*`, `crimson_*`, `goggles`.
- TODO(port): port key armor behavior from legacy:
- TODO(port): vis discount gear (`goggles`, robes, void robe, cultist pieces), revealer/node visibility (`goggles`, some helms), warping gear values (void/cultist/void robe), self-repair on void gear, fortress set/mask modifiers, traveller boots movement + fall handling + recharge path.
- TODO(port): preserve dye/overlay behavior for robe/void-robe textures and cauldron washing interaction where applicable.

### Tool Porting Backlog

- Current 1.20.1 registration still uses generic placeholder items for tool ids; no real `TieredItem` implementations are active yet.
- Legacy tool textures/models are in `old-1.12.2/assets/thaumcraft/textures/items` and `old-1.12.2/assets/thaumcraft/models/item`.
- TODO(port): implement real tool classes and registration wiring for:
- TODO(port): `thaumium_*`, `void_*`, `elemental_*`, `crimson_blade`, `primal_crusher`.
- TODO(port): port key behavior families:
- TODO(port): void tools self-repair + warp + on-hit debuffs, elemental tool active abilities and default infusion enchant setups, crimson blade debuff profile + warp, primal crusher dual-tool behavior and self-maintaining infusion enchantments.

### Insanity / Warp Backlog

- Legacy insanity is implemented as a multi-type warp system (`PERMANENT`, `NORMAL`, `TEMPORARY`) with periodic warp event rolls and effects.
- Legacy sanity checker drives dedicated warp HUD readout; sanity soap removes temporary warp and can reduce normal warp; purifying fluid and Warp Ward interact with this loop.
- TODO(port): add warp persistence capability with all three warp pools and counters.
- TODO(port): add server-side warp event scheduler/effects parity and progression triggers tied to warp thresholds.
- TODO(port): add sanity checker UI/HUD behavior and synchronize values client-side.
- TODO(port): port sanity soap, bath salts, and purifying fluid interactions against the new warp system.

### Phase 5: Infusion

Goal: restore infusion crafting loop.

- Infusion recipe type
- Altar structure checks
- Stability and failure logic
- Pedestal/item routing

Phase 5 status:

- [x] Added infusion world-state baseline for cycle outcomes: successful cycles now consume matrix/captured ingredients and emit a result onto the center pedestal
- [x] Added explicit instability side effects on failure: failed stability rolls now spill captured items before cycle abort
- [ ] Infusion recipe type/serializer and real recipe-driven output resolution are still pending
- [ ] Full legacy instability event parity is still pending (broader side effects, effects/FX, and failure variants)

Exit criteria:

- Core infusion recipes can be completed reliably

### Phase 6: Research and Thaumonomicon

Goal: restore long-form progression and research UX.

- Player knowledge/warp data
- Research category and entry loading
- Unlock progression and gating
- Thaumonomicon screens/navigation

Exit criteria:

- Research controls unlock flow and book navigation works

### Phase 7: Remaining Content and Polish

Goal: move toward feature parity and playable beta.

- Remaining entities/golems/worldgen/taint systems
- Sound, FX, and visual polish
- Compat and balancing passes
- Regression and performance fixes

Exit criteria:

- Stable content-complete beta for prioritized systems

## Wide-First System Expansion Map

- Station mechanics backlog (priority): `arcane_workbench` vis/crystal costs, `research_table` note/ink/research progression, `crucible` real aspect decomposition + recipes, `infusion_matrix` altar logic/stability/crafting loop
- Alchemy/essentia block line: `jar_normal`, `jar_void`, `jar_brain`, `alembic`, `centrifuge`, `smelter_basic`, `smelter_thaumium`, `smelter_void`, `smelter_vent`, `smelter_aux`, `thaumatorium`, `thaumatorium_top`
- Essentia transport line: `tube`, `tube_buffer`, `tube_filter`, `tube_oneway`, `tube_restrict`, `tube_valve`, `essentia_input`, `essentia_output`, `mirror_essentia`
- Infusion and support blocks: `pedestal`, `recharge_pedestal`, `wand_workbench`, `arcane_workbench_charger`
- Utility/device line: `golem_builder`, `pattern_crafter`, `hungry_chest`, `dioptra`, `spa`, `lamp_arcane`, `lamp_fertility`, `lamp_growth`, `redstone_relay`, `levitator`, `mirror`
- High-visual backlog (custom blockstates/models/shapes): `infusion_matrix`, `pedestal`, all jars, `alembic`, all tube variants, `crucible`, `bellows`, `dioptra`, `mirror`, `thaumatorium_top`
- World/progression systems: aura regen/transfer mechanics, flux events/cleanup, aspect definitions, player knowledge + warp persistence, insanity/warp events, sanity-checker HUD, research data load/unlock flow, Thaumonomicon UI/navigation
- Equipment parity systems: full armor/tool class migration, TC-specific attributes/effects, and model/texture parity for wearable/tool content

## Recommended Workflow

1. Implement in small vertical batches.
2. Run `./gradlew compileJava -q` after each code batch.
3. Run `./gradlew runData` after each data/resource batch.
4. Launch client periodically for missing-model and behavior smoke tests.
