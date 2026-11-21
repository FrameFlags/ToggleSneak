# Toggle Sneak — Itamio Port — Fabric 1.20.1

Client-side toggle sneak for Minecraft 1.20.1 / Fabric.

1.20.1 has no native toggle-sneak feature, so the upstream "revert" approach
has nothing to act on. This variant adds toggle sneak instead. Tap the sneak
key to latch crouching on; tap it again to stand up. Opening a GUI or losing
focus resets the latch. Uses a `ClientTickEvents.START_CLIENT_TICK` handler and
public key APIs (`KeyBinding.setPressed`, `InputConstants.isKeyDown`) - no
mixins required. Rebound sneak keys are supported.

Build with Gradle 8.x and JDK 17+: `gradle build`.
Output: `build/libs/togglesneak-1.20.1-fabric-1.0.0.jar`.

See the parent folder's `README.md` and `CREDITS.md` for the full rationale,
credits (Mobilelize) and MIT license.
