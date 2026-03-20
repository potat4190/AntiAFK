# AntiAFK (Fabric)

A lightweight client-side Fabric mod that helps prevent AFK kicks by simulating random movement input.

## Features

- Client command: `/antiafk <true|false>`
- Randomized movement using WASD-style key presses
- Optional jump input while moving
- Continues operating when Minecraft loses focus (until disabled)
- Built for modern Fabric + Java 21 toolchain

## Supported Versions

From the current project config:

- **Minecraft:** `1.21.11`
- **Fabric Loader:** `0.18.4+`
- **Fabric API:** `0.141.2+1.21.11`
- **Java:** `21`

## Installation (Users)

1. Install **Fabric Loader** for your Minecraft version.
2. Install **Fabric API** in your mods folder.
3. Download the AntiAFK mod `.jar`.
4. Place the `.jar` into your `.minecraft/mods` folder.
5. Launch Minecraft with the Fabric profile.

## Usage

In-game chat command:

```text
/antiafk true
```
  - Enables AntiAFK movement simulation.
```text
/antiafk false
```
  - Disables AntiAFK and releases keys.

How it works:
When enabled, the client periodically chooses random movement key states (W/A/S/D combinations) for short durations, with occasional pauses and jumps to vary behavior.
It is designed to continue while unfocused, and stops only when disabled.
