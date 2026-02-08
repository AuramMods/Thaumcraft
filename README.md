# Thaumcraft (Forge 1.20.1 Port)

This repository ports **Thaumcraft 6** from **Minecraft 1.12.2** to **Minecraft 1.20.1 (Forge)**.

## What This Project Is For

- Preserve and modernize Thaumcraft gameplay on a current Forge target.
- Rebuild legacy systems in a maintainable 1.20.1 architecture.
- Keep the mod playable during migration by shipping stable baselines first, then tightening parity.

Legacy source of truth:
- `old-1.12.2`

Active port source:
- `src/main`

## Porting Strategy

The port follows a **wide-first, then parity** workflow:
1. Register and render all legacy IDs/resources so the whole content surface exists in-game.
2. Restore core systems in vertical slices (stations, aura/aspects, alchemy, infusion, research).
3. Replace placeholder implementations with legacy-equivalent behavior.
4. Finish model/render parity, missing mechanics, and polish.

## Current Migration Snapshot

Based on `PORTING.md`, `PORTING_MEMORY.md`, and `PORTING_MANIFEST.md`:

- Legacy scope inventory captured: 823 Java files, 97 block IDs, 99 item handles, and full asset/research counts.
- Foundation and migration scaffolding are complete (registries, datagen, migration manifest, compile/data checkpoints).
- Legacy block/item baselines are registered and visible in-game.
- Core station framework is functional (`arcane_workbench`, `research_table`, `crucible`, `infusion_matrix`).
- Aura/aspect/scanning persistence foundations are in place.
- Alchemy has a working baseline (crucible recipes, jars, quartz-sliver catalyst path, base vis crystal crucible recipes).
- Bucket of Pure / Bucket of Death now have baseline placement/pickup behavior with placeholder fluid interactions.
- Full parity still pending for major systems (essentia transport network, full infusion/research parity, warp/insanity loop, armor/tool behavior, model fidelity backlog).

## How To Check Progress

Use this checklist each time you want a progress read:

1. Read phase checkboxes in `PORTING.md`.
2. Read implementation handoff notes and recent direction in `PORTING_MEMORY.md`.
3. Use `PORTING_MANIFEST.md` to confirm migration scope and legacy inventory references.
4. Run compile checkpoint:
`./gradlew compileJava -q`
5. Run datagen checkpoint:
`./gradlew runData`
6. Validate in-game smoke tests for the systems touched in the current phase (station interaction, crafting loops, item/block behavior).

Progress is considered real when:
- the phase checkbox/status is updated in `PORTING.md`,
- the implementation is reflected in source (or source TODOs where intentionally deferred),
- compile/data checkpoints pass.

## Porting Documents

- Main plan and phase tracking: [`PORTING.md`](PORTING.md)
- Context-compressed handoff memory: [`PORTING_MEMORY.md`](PORTING_MEMORY.md)
- Legacy migration inventory/IDs: [`PORTING_MANIFEST.md`](PORTING_MANIFEST.md)
