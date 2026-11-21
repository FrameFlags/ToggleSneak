# Toggle Sneak — Itamio Port — Forge 1.12.2

Client-side toggle sneak for Minecraft 1.12.2 / Forge 14.23.5.2847.

1.12.2 has no native toggle-sneak feature, so the upstream "revert" approach
has nothing to act on. This variant adds toggle sneak instead. Tap the sneak
key to latch crouching on; tap it again to stand up. Opening a GUI resets the
latch. Uses the public `KeyBinding.setKeyBindState(int, boolean)` API — no
mixins required.

Build with Gradle 4.9/4.10.3 and JDK 8: `gradle build`.
Output: `build/libs/togglesneak-1.12.2-forge-1.0.0.jar`.

See the parent folder's `README.md` and `CREDITS.md` for the full rationale,
credits (Mobilelize) and MIT license.
