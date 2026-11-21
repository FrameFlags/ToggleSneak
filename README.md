# Toggle Sneak — Itamio Port

Client-side toggle-sneak for Minecraft **1.12.2 (Forge)**, **1.20.1 (Fabric)** and
**1.20.1 (Forge)**.

## Why the name differs from the upstream "Revert Toggle Sneak"

The upstream project
[Mobilelize/Revert-Toggle-Sneak](https://github.com/Mobilelize/Revert-Toggle-Sneak)
(MIT) targets a Minecraft version in which Mojang added a *native* toggle-sneak
accessibility feature. It "reverts" that feature by intercepting
`KeyBinding.unpressAll` and skipping the reset of `key.sneak` when its
`StickyKeyBinding` toggle getter is set.

Neither **1.12.2** nor **1.20.1** contains that Mojang feature — there is no
`StickyKeyBinding` for sneak and no toggle state to revert. Porting the upstream
logic literally would produce a jar that does nothing on these versions. Therefore
this port implements the meaningful **inverse**: it *adds* a small client-side
Toggle Sneak. The display name is deliberately
`Toggle Sneak — Itamio Port` so it does not claim to revert a feature that does
not exist on the supported targets.

Credit for the concept/reference goes to Mobilelize (see `CREDITS.md`); the code
here is an independent implementation.

## Behaviour (all variants)

- Tap the **sneak key** (default: Left Shift) once: sneaking turns on and **stays
  on** even after you release the key.
- Tap it again: sneaking turns off.
- Opening a GUI or losing window focus **resets** the toggle to off and releases
  the key, so it can never get stuck.
- Works with rebound sneak keys.
- Fully client-side; no server, no network packets, no mixins required.

Implementation detail: because the game itself clears the sneak key state whenever
the physical key is released, the mod re-applies the desired state every client
tick. The "tap" edge is detected from the real physical key state (GLFW on 1.20.1,
`Keyboard.isKeyDown` on 1.12.2) rather than from the key binding's own pressed
flag, which the mod drives itself.

## Variants

| Directory | Loader | MC | Java | Gradle |
| --- | --- | --- | --- | --- |
| `1.12.2-forge/` | Forge 14.23.5.2847 | 1.12.2 | 8 | 4.9 / 4.10.3 |
| `1.20.1-fabric/` | Fabric Loader 0.15.11 + Fabric API | 1.20.1 | 17+ | 8.x |
| `1.20.1-forge/` | Forge 47.4.20 | 1.20.1 | 17+ | 8.x |

Each variant folder contains its own `build.gradle`, `gradle.properties`,
`settings.gradle`, `LICENSE`, `CREDITS.md`, `README.md` and mod metadata.

## Build

Run the Gradle build inside the variant you want (the templates do not ship a
wrapper, so use a compatible local Gradle distribution):

```sh
cd 1.20.1-fabric && gradle build
cd 1.20.1-forge  && gradle build
cd 1.12.2-forge  && gradle build   # Gradle 4.9/4.10.3 + JDK 8
```

The output jar is written to `<variant>/build/libs/`.

## License

MIT — see `LICENSE` and `CREDITS.md`.
