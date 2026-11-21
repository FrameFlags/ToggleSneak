# Credits

- **Sekai0NI0itamio** — author and maintainer of this port.
- **Mobilelize / [Revert-Toggle-Sneak](https://github.com/Mobilelize/Revert-Toggle-Sneak)** —
  credited for the original concept and as the reference implementation (MIT licensed).
  The upstream mod targets a Minecraft version where Mojang introduced a native
  toggle-sneak behaviour and *reverts* it.

## Important

This project is an **independent implementation**, not a copy of the upstream code.
None of the upstream source was copied: the approach here is a small client-side
key-state loop written specifically for Minecraft versions (1.12.2 and 1.20.1) that
have **no native toggle-sneak feature**, so there is nothing to "revert" on them.
Instead these variants add the useful inverse behaviour — a toggleable sneak key.

License: MIT (see `LICENSE`).
