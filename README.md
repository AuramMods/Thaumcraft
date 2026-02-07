# Thaumcraft (Forge 1.20.1 Port)

This repository is a direct port of **Thaumcraft 6 (Minecraft 1.12.2)** to **Minecraft 1.20.1 (Forge)**.

The legacy source of truth is in:
- `old-1.12.2`

The active 1.20.1 code is in:
- `src/main`

## What We Are Doing

We are porting in a **wide-first, then parity** sequence:
1. Bring all legacy IDs/content into the 1.20.1 project with stable registration and assets.
2. Restore core gameplay loops (stations, aura/aspects, alchemy, infusion, research).
3. Replace placeholder content with feature-complete classes and legacy-equivalent behavior.
4. Finish visual/model parity, remaining systems, and polish.

This keeps the project playable and testable while deeper TC6 mechanics are reintroduced in controlled phases.

## Porting Status Summary (from `PORTING.md`)

- Foundation and migration scaffolding are complete (registries, datagen, manifest, compile/data checkpoints).
- Legacy block/item ID baselines are registered and visible in-game.
- Core station scaffolding exists (`arcane_workbench`, `research_table`, `crucible`, `infusion_matrix`) with functional block entities/menus.
- Aura + aspect + scanning foundations are in place and persisted.
- Crucible + early alchemy flow works with starter recipes and jar storage/transfer baseline.
- Infusion has a baseline cycle/stability framework, but full recipe/failure parity is still pending.

Current major parity backlog includes:
- Quartz sliver catalyst parity (`nuggetQuartz` semantics) and full crystal alchemy path.
- Essentia transport chain (`alembic`, tubes, smelters, thaumatorium).
- Bucket/fluid behavior parity (`purifying_fluid`, `liquid_death`).
- Armor/tool class migration from placeholders to real equipable/functional implementations.
- Insanity/warp loop, sanity checker HUD, sanity soap/bath salts interactions.
- Remaining non-cube model/blockstate parity for many legacy blocks.

## Quick Commands

- Compile checkpoint: `./gradlew compileJava -q`
- Datagen checkpoint: `./gradlew runData`

## Porting Documents

- Main plan and phase tracking: [`PORTING.md`](PORTING.md)
- Context-compressed handoff memory: [`PORTING_MEMORY.md`](PORTING_MEMORY.md)
- Legacy inventory/ID manifest: [`PORTING_MANIFEST.md`](PORTING_MANIFEST.md)
