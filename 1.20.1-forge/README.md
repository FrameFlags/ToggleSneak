# Toggle Sneak — Itamio Port — Forge 1.20.1

Client-side toggle sneak for Minecraft 1.20.1 / Forge 47.4.20.

1.20.1 has no native toggle-sneak feature, so the upstream "revert" approach
has nothing to act on. This variant adds toggle sneak instead. Tap the sneak
key to latch crouching on; tap it again to stand up. Opening a GUI or losing
focus resets the latch. Uses a `TickEvent.ClientTickEvent` handler in a
`@Mod.EventBusSubscriber(value = Dist.CLIENT)` class and public key APIs
(`KeyBinding.setPressed`, `InputConstants.isKeyDown`) - no mixins required.
Rebound sneak keys are supported.

No `mixins.json` is present because the public key API is sufficient on 1.20.1.

Build with Gradle 8.x and JDK 17+: `gradle build`.
Output: `build/libs/togglesneak-1.20.1-forge-1.0.0.jar`.

See the parent folder's `README.md` and `CREDITS.md` for the full rationale,
credits (Mobilelize) and MIT license.
