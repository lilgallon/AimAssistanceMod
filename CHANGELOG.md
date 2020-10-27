## Version 1.4.0

- Smart aim assistance: it will aim the closest part of a mob (instead of its neck) - (it's optimized)
- "Stop when reached" option for mobs: if turned on, the mob aim assistance stops when you are looking at a mob

## Version 1.3.0

- Support for the best controller mod - [Controllable](https://mrcrayfish.com/mods?id=controllable)

## Version 1.2.1

- The config file is now reloaded automatically when updated

## Version 1.2.0

*Built for forge 1.16.3, works with 1.16.2 and 1.16.1*

- Smart block aim assistance

**BEFORE:**
![before](.github/images/1_2_0_before_update.gif)

**AFTER:**

It will recognize the block that you're trying to break.
![smart aim](.github/images/1_2_0_smart_aim.gif)

But, if you want to change the block that you're breaking, just move your mouse.
![smart aim](.github/images/1_2_0_smart_aim_captions.gif)

- Improved attack assistance (it puts your crosshair higher than before)
- Increased attack assistance duration (1 sec -> 1.7 sec)
- The assistance won't be running while you're in GUIs


## Version 1.1.0

- Built for forge 1.16.2
- Split aimForce into:
    - aimForceBlocks: the force of the aim assistance on blocks
    - aimForceMobs: the force of the aim assistance on mobs
- Changed default force values:
    - 4.0 for mobs
    - 2.5 for blocks
- When you're fighting a mob, the mod understands it and won't help you mining while you're fighting. Same thing if you're mining. It will help you to mine and not to fight.
- Mining assistance now turns off as soon as the player stops mining
- Added a version checker
- Fixed an issue where the assistance would shake

## Version 1.0.1

**Core:**
- Improved aim assistance on mobs

![before](.github/images/demo_before_1.0.1.gif)

![after](.github/images/demo_1.0.1.gif)

## Version 1.0.0

**Core:**
- Aim assistance for mobs (while fighting)
- Aim assistance for blocks (while mining)

**Settings:**
- Aim force customizable (0.1 -> 10.0)
- Assistance on mobs (on/off)
- Assistance on blocks (on/off)
