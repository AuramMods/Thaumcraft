# Thaumcraft Porting Memory (Context Compression Handoff)

This file captures migration state so work can continue safely after context compression.

## What Is Done

### Foundation (Phase 0)

- Added plan document: `PORTING.md`
- Added migration inventory: `PORTING_MANIFEST.md`
- Refactored bootstrap into modular registries:
  - `ModBlocks`
  - `ModItems`
  - `ModBlockEntities`
  - `ModCreativeTabs`
- Added datagen framework:
  - blockstates provider
  - item model provider
  - language provider
  - block tags provider
  - recipes provider
- Verified:
  - `./gradlew compileJava -q` passes
  - `./gradlew runData --no-daemon` passes

### Phase 1 (Static Content Registration + Textured Baseline)

- Registered placeholder legacy content in 1.20.1:
  - 97 block IDs
  - 99 item IDs
  - block-items for all migrated blocks
- Added `LegacyContentRegistryIds` as source-of-truth ID list.
- Copied legacy textures:
  - 187 block textures into `assets/thaumcraft/textures/block`
  - 215 item textures into `assets/thaumcraft/textures/item`
- Datagen now produces:
  - 97 blockstates
  - 97 block models
  - 196 item models
  - large language entry set for migrated IDs
- Added texture alias logic in datagen so generated models do not fall back to:
  - `minecraft:block/stone`
  - `minecraft:item/paper`
- User validated in-game:
  - names/items register
  - content appears in creative tab
  - non-cube blocks still visually incorrect (expected at this stage)

### Phase 2 (Functional Station Scaffolding)

- Replaced these IDs with `BaseEntityBlock` station blocks:
  - `arcane_workbench`
  - `research_table`
  - `crucible`
  - `infusion_matrix`
- Added block entities and registrations:
  - `ArcaneWorkbenchBlockEntity`
  - `ResearchTableBlockEntity`
  - `CrucibleBlockEntity`
  - `InfusionMatrixBlockEntity`
- Added generic station framework:
  - `StationBlock`
  - `StationBlockEntity`
  - `TickingStationBlockEntity`
- Added menu/screen stack baseline:
  - `ModMenus` + `StationMenu` (initial shared station container)
  - `ThaumcraftClientEvents` + `StationScreen` (initial shared UI)
- Completed Phase 2B menu split:
  - replaced shared `station` menu with per-station menu types:
    - `arcane_workbench`
    - `research_table`
    - `crucible`
    - `infusion_matrix`
  - added per-station menu classes:
    - `ArcaneWorkbenchMenu`
    - `ResearchTableMenu`
    - `CrucibleMenu`
    - `InfusionMatrixMenu`
  - added shared abstract base:
    - `AbstractStationMenu`
  - bound each block entity to its own menu creation path
  - registered per-menu screens:
    - `ArcaneWorkbenchScreen`
    - `ResearchTableScreen`
    - `CrucibleScreen`
    - `InfusionMatrixScreen`
- Added first Phase 2 mechanics slice for Arcane Workbench:
  - output slot is now recipe-driven from a 3x3 input grid
  - taking result consumes crafting ingredients
  - crafting remainders are handled using recipe manager remainder logic
  - shift-click into the workbench targets input slots only (not output)
  - added vis-gated arcane behavior:
    - output is only craftable when chunk aura has enough vis for computed craft cost
    - taking output drains vis from `AuraManager`
    - output availability re-checks periodically while menu is open (not only on slot changes)
  - added crystal slot + crystal consumption:
    - dedicated crystal slot on workbench menu
    - required crystal type is derived from dominant ingredient aspects
    - craft output is gated by both vis availability and required crystal presence
    - one crystal is consumed per successful craft
  - added parity-tuning pass for generic crafting behavior:
    - vis/crystal costs now apply only when crafted output belongs to `thaumcraft` namespace
    - non-Thaumcraft crafting no longer hard-requires vis/crystal and behaves as normal 3x3 crafting
  - restored arcane workbench charger vis behavior baseline:
    - when `arcane_workbench_charger` is above the table, available vis now sums surrounding 3x3 chunks
    - vis drain now distributes across the same 3x3 chunk region instead of draining only the local chunk
- Added Research Table baseline slot rules:
  - slot 0 accepts `scribing_tools` only (single-stack)
  - slot 1 accepts `paper` only
- Added Research Table theory-session baseline actions:
  - new screen/menu action buttons (`Draft`, `Complete`) routed through menu button click handling
  - drafting consumes paper + scribing context, spends observation knowledge, and writes persisted note totals per research category
  - drafting now consumes inspiration from an active theory session (session is created on first draft with a baseline available-inspiration value)
  - completing a spent-inspiration session converts note totals into theory knowledge rewards (plus highest-category bonus), then clears the session
- Added Research Table menu/session sync baseline:
  - dedicated menu data sync now mirrors theory session state to clients (active, inspiration current/start, draft count, category count, completion-ready flag)
  - research table screen now gates `Draft`/`Complete` buttons from server-synced session state and renders live session stats
- Added Crucible baseline mechanics:
  - heat rises/falls based on heat-source block beneath crucible
  - water can be filled by placing a water bucket in the crucible slot
  - when hot + filled, input items are consumed and converted into placeholder essentia units
  - state is persisted (`heat`, `water`, `essentia`, `cooldown`) and exposed through menu activity sync (`activity = heat`)
  - crucible slot max stack size is now 1
- Added menu/container localization keys in language provider for the four stations.
- Added infusion matrix baseline structure state:
  - scans pedestal layout every second (center + primary/extended ring positions)
  - marks structure-ready when baseline altar shape is present
  - syncs readiness into menu activity value for immediate in-game debugging
- Extended infusion matrix with pedestal ownership/capture baseline:
  - maintains a deterministic owned pedestal set from detected altar layout
  - pulls one item from each owned pedestal into internal persisted capture slots
  - tracks source `BlockPos` per captured slot and returns items to pedestal or drops them if ownership/structure changes
- Added infusion cycle state-machine scaffold:
  - cycle start is requested by placing an item in matrix slot (`MATRIX_SLOT`)
  - while active, cycle drains vis periodically from local chunk aura
  - low-vis or broken structure aborts the cycle and adds flux penalty
  - cycle completion now consumes matrix/captured ingredients and emits a result on the center pedestal (placeholder output baseline)
- Added initial infusion stability hooks:
  - stability score is computed from altar layout symmetry and pedestal coverage
  - active cycles roll periodic instability failure checks using effective stability
  - failed instability checks now spill captured items, add flux, and abort the cycle
- Promoted `pedestal` from placeholder block to functional block entity:
  - single-slot persisted inventory (`PedestalBlockEntity`)
  - right-click interaction to place/retrieve one item
  - item drops on block break
- Verified:
  - `./gradlew compileJava -q` passes
  - `./gradlew runData --no-daemon` passes
- User validated in-game:
  - all four station tile entities open
  - shift-click into top slot works
  - tick/activity bars are visible and updating

### Phase 3 (Core Progression Foundations Started)

- Added persistent aura chunk data model:
  - `AuraChunkData` (`base`, `vis`, `flux`, clamped values)
- Added dimension save integration:
  - `AuraSavedData` (Forge `SavedData`, per-chunk NBT storage)
- Added aura API facade:
  - `AuraManager` (`getBase/getVis/getFlux`, `addVis/addFlux`, `drainVis/drainFlux`)
  - auto-creates missing chunk aura entries with baseline defaults (`base=200`, `vis=200`, `flux=0`)
- Added initial aspect scaffolding:
  - `AspectType` enum for core/practical aspect keys
  - `AspectList` value container with optional NBT read/write helpers
  - `AspectRegistry` with exact-item overrides and path/trait fallback heuristics
- Expanded data-driven aspect selector support:
  - `AspectObjectRegistry` now supports strict `path_contains_all` matching in addition to exact item/tag/path-any selectors
- Imported a large parseable subset of legacy `ConfigAspects` into data:
  - generated `data/thaumcraft/thaumcraft/aspects/legacy_config_aspects.json`
  - includes 18 exact Thaumcraft item mappings and 80 strict legacy path rules
  - removed duplicate broad `path_contains_any` fallback rules from `base.json` to avoid double-counting with the imported legacy rules
- Added first Salis Mundus trigger scaffold:
  - right-clicking a vanilla crafting table with `salis_mundus` converts it into `arcane_workbench`
- Expanded Salis Mundus simple triggers with progression gating:
  - bookshelf grants `thaumonomicon` and unlocks first Salis/knowledge state
  - crafting table conversion to `arcane_workbench` now requires unlocked baseline knowledge
  - cauldron conversion to `crucible` now requires unlocked baseline knowledge
- Added first-pass Salis Mundus multiblock trigger handling:
  - `infusion_matrix` + stone ring now assembles baseline infusion altar pieces (`pillar` corners + center `pedestal`)
  - `crucible` + `metal` stack now assembles `thaumatorium` + `thaumatorium_top`
  - infernal furnace blueprint baseline now converts valid lava-core structures into `infernal_furnace`
  - golem press blueprint baseline now converts valid piston-core structures into `golem_builder` (with iron bars cap)
- Added player knowledge persistence baseline:
  - `PlayerKnowledgeSavedData` stores per-player unlock flags, scanned blocks, and discovered aspects
  - `PlayerKnowledgeManager` provides server-side read/write API
- Added first thaumometer scan hook:
  - right-click block with thaumometer to record scan and discover aspect tags from scanned block item
  - scan usage is currently gated behind Salis Mundus unlock baseline
- Expanded thaumometer scan surfaces:
  - added entity scan hook (`EntityInteract`) with spawn-egg/fallback aspect derivation
  - added item scan hook (`RightClickItem`) for offhand item scanning while holding thaumometer
  - extended `PlayerKnowledgeSavedData` to persist unique scanned item/entity IDs in addition to block IDs
  - added `PlayerKnowledgeManager` APIs for item/entity scan recording
- Wired crucible processing into aura:
  - each processed crucible input now adds a small amount of chunk flux
- Wired crucible baseline processing into aspects:
  - item essentia gain now comes from `AspectRegistry` total values instead of generic per-item placeholder math
- Notes:
  - this is persistence/API scaffolding, not final aura simulation behavior
  - legacy aspect coverage is now substantially expanded but not complete; direct vanilla `ItemStack(...)` mappings and final parity review still remain
  - aura generation curves, regen, spread, and flux events are still pending

### Phase 4 / Insanity Loop Baselines (Recent)

- Added registered `warp_ward` mob effect scaffold:
  - new effect type + registry (`ModMobEffects`, `WarpWardMobEffect`)
  - mod bootstrap now registers mob effects during startup
- Updated purifying fluid behavior toward legacy parity:
  - touching purifying fluid now grants `warp_ward`
  - ward duration baseline now scales from permanent warp (`min(32000, 200000 / sqrt(permanent_warp_or_1))`)
  - source block is consumed after ward application
- Expanded warp debug command tooling:
  - `/thaumcraft debug warp add <pool> <amount>`
  - `/thaumcraft debug warp set <pool> <value>`
  - `/thaumcraft debug warp clear [pool]`
  - `/thaumcraft debug warp counter set|reset`
  - added direct warp setters in player knowledge data/manager for test/debug flows
- Extended baseline warp ticker toward legacy Warp Ward interaction:
  - warp ticker now skips event accumulation/rolls while `warp_ward` is active
  - temporary warp now decays by `1` every `2000` ticks when the player is not warded
- Extended baseline warp ticker cadence/probability toward legacy `checkWarpEvent`:
  - warp checks now run every `2000` ticks (legacy cadence shape) instead of per-second threshold accumulation
  - event trigger chance now rolls from `sqrt(warp_counter)` (0-99 roll), with effective-warp scaling and legacy-shaped counter decay floor (`max(5, sqrt(counter) * 2)`)
- Extended baseline warp ticker with legacy-shaped gear and research-unlock hooks:
  - warp event effective total now includes warping gear from main-hand + armor (plus legacy `TC.WARP` item NBT support)
  - baseline warping values are now wired for `void_*` tools/armor, `void_robe_*`, `crimson_blade`, and `primal_crusher`
  - warp counter decay now follows legacy-shaped gear pressure (`max(5, sqrt(counter) * 2 - gearWarp * 2)`)
  - player knowledge now persists baseline research keys and warp progression unlocks legacy keys (`!BATHSALTS`, `ELDRITCHMINOR`, `ELDRITCHMAJOR`)
  - player-knowledge load now migrates old milestone tags (`bath_salts_hint`, `eldritch_minor`, `eldritch_major`) into those research keys for compatibility
  - player knowledge now persists baseline research stage/flag scaffolding (`stage`, `PAGE`/`RESEARCH`/`POPUP`) keyed by research id
  - warp-triggered research unlocks now set baseline legacy-shaped research flags (`RESEARCH`, plus `POPUP` on `!BATHSALTS`)
  - `/thaumcraft debug warp` now prints gear warp totals and research-key states for validation
- Added baseline research debug command tooling:
  - `/thaumcraft debug research`
  - `/thaumcraft debug research list`
  - `/thaumcraft debug research unlock|remove <key>`
  - `/thaumcraft debug research stage <key> [value]`
  - `/thaumcraft debug research flag set|clear|check <key> <flag>`
- Added baseline research knowledge-point scaffolding:
  - player knowledge now persists legacy-shaped knowledge totals by type (`THEORY`, `OBSERVATION`, `EPIPHANY`) with category-aware storage for fielded types
  - knowledge debug tooling added under `/thaumcraft debug research knowledge` (`list`, `get`, `add`, `set`) with raw + projected points output
- Wired first gameplay gain path for research knowledge:
  - thaumometer first-time scans now grant baseline observation raw across core research categories (`BASICS`, `THAUMATURGY`, `ALCHEMY`, `ARTIFICE`, `GOLEMANCY`, `ELDRITCH`)
  - scan flow now grants baseline epiphany raw when a scan discovers new aspects
- Added dedicated cultist/crimson warping armor baselines:
  - `crimson_boots` now maps to `CultistBootsItem` (warp `1`, uncommon rarity, iron-ingot repair parity baseline)
  - `crimson_robe_chest` / `crimson_robe_helm` / `crimson_robe_legs` now map to `CultistRobeArmorItem` (warp `1`, uncommon rarity, iron-ingot repair parity baseline)
  - removed temporary fallback mapping for crimson armor warp in `WarpGearManager` now that dedicated classes exist
- Added vis-discount gear baseline scaffolding and first gear mappings:
  - new `VisDiscountGearItem` + `VisDiscountManager` collect armor vis-discount values (legacy-shaped baseline, armor-only for now)
  - added dedicated `GogglesItem` baseline with vis discount `5`
  - added dedicated `RobeArmorItem` baseline and mapped `cloth_*` to legacy-shaped discounts (`boots=2`, `chest/legs=3`)
  - `VoidArmorItem` now carries vis-discount value; `void_robe_*` mapped to discount `5` while `void_*` remains `0`
  - cultist/crimson armor classes now provide vis-discount `1` (boots + robe pieces)
- Extended arcane workbench vis flow toward legacy parity:
  - workbench vis spend now applies player-specific vis discount at craft-take time
  - workbench preview/result gating now no longer hard-hides output on raw vis shortage; final pickup validation uses discounted vis for the taking player
- Expanded warp debug tooling:
  - `/thaumcraft debug warp gear` now shows per-slot warp breakdown (main hand + armor), which improves parity validation when porting additional warping equipment
- Expanded vis debug tooling:
  - `/thaumcraft debug vis discount` now shows total armor discount and per-slot discount contributions
- Added dedicated `bath_salts` item class baseline:
  - dropped entity lifespan now matches legacy quick-expire behavior (`200` ticks)
  - expired bath salts now convert source water blocks and full water cauldrons into `purifying_fluid` placeholders
- Promoted `spa` from placeholder block to functional baseline block entity:
  - accepts water buckets into internal water storage (`5000` max) and accepts `bath_salts` as consumable input
  - every `40` ticks (when not redstone-powered), consumes `bath_salts` + water (`1000`) to place/spread `purifying_fluid`
  - supports first-pass spread behavior around existing purifying-fluid cells in a 5x5 area above the spa
- Extended spa baseline with initial legacy mix/non-mix behavior:
  - shift-right-click with empty hand toggles spa mix mode
  - non-mix mode can consume loaded fluid directly (`water`, `bucket_pure`, `bucket_death`) for placement/spreading
  - empty bucket extraction now returns the loaded fluid as the corresponding bucket item
- Added quartz-sliver catalyst migration baseline toward nugget parity:
  - registered split-ID nugget variants (`nugget_brass`, `nugget_copper`, `nugget_iron`, `nugget_lead`, `nugget_quartz`, `nugget_quicksilver`, `nugget_silver`, `nugget_thaumium`, `nugget_tin`, `nugget_void`)
  - moved quartz catalyst canonical path to `thaumcraft:nugget_quartz`
  - retained `thaumcraft:quartz_sliver` compatibility in `thaumcraft:catalysts/quartz_slivers`
  - updated quartz conversion recipes so quartz recombination accepts the catalyst tag and added legacy-to-canonical migration recipe (`quartz_sliver` -> `nugget_quartz`)
  - mirrored quartz nugget aspect mapping so catalyst dissolution behavior remains consistent
- Restored armor/goggle model asset baseline:
  - imported legacy armor render textures into `assets/thaumcraft/textures/models/armor`
  - added explicit armor texture routing per armor family (`thaumium`, `void`, `void_robe`, `cloth_robe`, `cultist`, `fortress`, `traveller`, `goggles`) to replace vanilla-material placeholder renders
  - added legacy armor/goggle item model JSONs in main resources (including multi-layer robe/void-robe overlays)
  - updated item-model datagen to skip custom armor/goggle ids and removed generated duplicates to avoid resource copy collisions
- Verified:
  - `./gradlew compileJava -q` passes
  - `./gradlew classes -q` passes
  - `./gradlew runData --no-daemon` passes

## Overall Plan (Condensed)

1. Phase 0: Foundation + migration map (complete)
2. Phase 1: Blocks/items in-game textured baseline (complete baseline, visual fidelity still pending)
3. Phase 2: Functional block entities + menus/screens for core stations
4. Phase 3: Core progression systems (aspects, aura, salis mundus/scanning)
5. Phase 4: Alchemy + essentia pipeline
6. Phase 5: Infusion system
7. Phase 6: Research + Thaumonomicon UX
8. Phase 7: Remaining systems + polish

## Immediate Next Steps

- Continue variant-container parity after quartz catalyst migration:
  - design canonical `thaumcraft:nuggets` subtype/data-component model and migration path from split-ID baseline
  - expand the same split-ID-to-variant strategy to other containers (`ingots`, `plate`, `gear`) or move directly to unified component-backed containers
  - retire `thaumcraft:quartz_sliver` compatibility item once save/datapack migration handling is in place
- Continue armor visual parity after texture/model-asset pass:
  - port custom equipped-geometry layers for robe/fortress/leader/goggles families (1.20.1 equivalents of legacy `ModelRobe`/`ModelFortressArmor`/`ModelLeaderArmor`)
  - restore robe/void-robe tint + overlay behavior parity on equipped models (beyond current baseline texture routing)
- Finalize bath-salts + spa parity audit against legacy behavior:
  - validate exact conversion surfaces (source-water/cauldron expectations) and tune to intended TC6 semantics
  - port full spa UI/container interactions and fluid capability parity beyond the current baseline counters model
- Extend Warp Ward integration beyond current baseline:
  - add localization/icon/UX polish for `warp_ward`
  - expand current key/stage/flag/knowledge scaffolding into full research progression parity (auto-unlock graph + real theorycraft card draw/aid selection + stage reward consumption)
  - expand remaining legacy warp parity (full event table, client FX/audio/sync, and accessory-slot warp sources)
- Continue research table parity after session baseline:
  - expand client sync from current session counters to full note/card payloads (drawn choices, selected card, aid mutators, blocked categories)
  - implement legacy card-choice draw/activate flow and surrounding-aid discovery/selection hooks
- Continue armor parity after cultist warp baseline:
  - add remaining non-baseline vis-discount sources (baubles/curios equivalents and any item NBT-driven modifiers)
  - port cultist/void/fortress model + texture parity and set/slot behavior
- Continue arcane workbench parity after vis-discount baseline:
  - move preview/cost state to per-menu-player context for multi-viewer parity
  - refine charger-area drain policy against legacy edge cases (chunk preservation/drain order tuning)
  - tune vis/crystal balance and exact recipe parity against legacy arcane recipe definitions
- Finish remaining `ConfigAspects` migration gaps:
  - map non-parseable direct vanilla `ItemStack(...)` object tags and audit imported rule parity against legacy behavior
- Extend Arcane Workbench from vanilla crafting to arcane crafting constraints:
  - crystal/vis cost parity tuning against legacy behavior
- Port remaining Salis Mundus trigger parity from legacy dust triggers:
  - complete variant/semantic parity (`infusion` ancient/eldritch material variants and legacy placeholder transformation behavior)
- Implement infusion recipe resolution:
  - add infusion recipe type/serializer and bind cycle outputs to real recipe matching
  - expand instability events beyond item spill + flux baseline (effects and broader failure outcomes)
- Expand thaumometer/scanning beyond block-right-click baseline:
  - proper scan UI/feedback
  - tie discoveries into research unlock graph

## Wide-First Backlog Map

- High visual/model fidelity backlog: `infusion_matrix`, `pedestal`, `jar_normal`, `jar_void`, `jar_brain`, `alembic`, `tube*`, `bellows`, `dioptra`, `mirror`, `thaumatorium_top`.
- Alchemy chain backlog: crucible recipes, alembic extraction, jar storage semantics, smelter/centrifuge flows, thaumatorium recipe processing.
- Transport/network backlog: tube routing rules, suction/priority logic, input/output node behavior, mirror essentia transfer.
- Progression/data backlog: aspect registry, player knowledge/warp persistence, research tree loading/unlocks, Thaumonomicon pages/navigation.

## Lessons Learned / Constraints

- This environment may require escalated runs for Gradle wrapper lock access.
- Avoid parallel Gradle tasks; run compile/datagen sequentially.
- Existing legacy textures rarely match direct ID names exactly; alias mapping is required.
- Datagen duplicate file conflicts can occur if same asset exists in both main and generated resources.
- Keep placeholder implementation simple and stable first; fidelity can come after mechanics.
- Forge API event names vary by patchline; `FMLClientSetupEvent` + `MenuScreens.register` was compatible here, while `RegisterMenuScreensEvent` was not.
