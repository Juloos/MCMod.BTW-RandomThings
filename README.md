# Random Things

This repository is based on the [BTW-Gradle](https://github.com/BTW-Community/BTW-gradle) and the [Example Mod](https://github.com/minecraft-cursed-legacy/Example-Mod)
repositories, combining them to enable fabric development for the Better Than Wolves mod.
**This repository was only tested for client-side development yet.** 

(THIS ADDON ADDS A TIMER AND THREE KEYS FOR CAMERA VIEW INSTEAD OF A SINGLE BUTTON, MORE IS TO BE ADDED)  

## Questions

### How do i disable the F5 change?
1.launch the game and quit once its fully done launching  
2.click on the instance in multimc (or your fork of choice)  
3.click "folder"  
4.open ".minecraft" in the folder opened just then  
5.open "config" in the folder you just opened  
6.open up "Rand.properties" in your text editor of choice  
7.change this true to a false  
8.save and relaunch and it should take affect!  

## Quick Start

* Clone this repository
* Acquire the full BTW sources and put them under `src/btw/java` 
* (Optional) Put the BTW resources (textures, etc.) under `src/btw/resources`
* Run the gradle task *btwJar*
* Run the gradle task *build* and then *runClient*
* (Optional) Put the vanilla MC resources (sounds) under `run/resources`

## BTW Source Code

To get access to the Better Than Wolves source code, please refer to the [BTW-Gradle repository](https://github.com/BTW-Community/BTW-gradle)
 or (alternatively) the [BTW-Public repository](https://github.com/BTW-Community/BTW-Public), which also offers a way to generate the sources.

## Development

Similar to the [BTW-Gradle](https://github.com/BTW-Community/BTW-gradle) project, this repository comes with a few configuration files for IntelliJ IDEA.

In this repository, there is an example implementation of a fabric mod that is a BTW-Addon at the same time. Base class overwrites,
either for initializing your BTW-Addon or for changes to the functionality of MC are in many cases not needed 
anymore. (But they are still possible, put the overwriting-sources under `main/java/net/minecraft/src`.
 This requires your mod to be loaded as a coremod, which is currently not supported in a dev-environment - only in production.) The
addon initialization is taken care of by the PreLaunchInitializer.
 
For functionality changes to base classes, please have a look at mixins, which enable you
to inject code at runtime, offering much better compatibility. Most fabric-mixin tutorials should apply here, but keep in
mind that no fabric-api is available yet, just bare mixins. An even more powerful alternative is fabric-asm, but this has not
been tested yet.

If you use reflection, please keep in mind that it is now, in many cases at least, not needed anymore, in addition to the fact that fabric
remaps Minecraft at runtime into an intermediary form, which is different from the obfuscated one. To get the intermediary names
of classes, fields, and methods, have a look at the mappings under `custom_mappings`.

## Releasing Mods/Addons

If you want to run fabric mods with BTW in a non-dev environment, you have to either use 
the [BTW-fabric MultiMC instance](https://github.com/BTW-Community/cursed-fabric-loader/releases/latest) (recommended) or 
follow the [installation instructions for the Vanilla launcher](https://github.com/BTW-Community/legacy-fabric-installer/releases/latest).


Drop on by the discord server if you need help: [BTW Discord](https://discord.gg/fhMK5kx). 

The mod file for the release is generated by the Gradle task *remapJar* and then put into `release`.

After successfully importing the MultiMC instance, you can put your mod file into the mods folder of your installation. 
If it is a coremod, put it into coremods.

## Issues & Troubleshooting

* How do I obtain the BTW-sources? *Please refer to [BTW-Gradle](https://github.com/BTW-Community/BTW-gradle) or [BTW-Public](https://github.com/BTW-Community/BTW-Public).* 

More troubleshooting is still todo. Feel free to message me on Discord.

## MultiMC Remarks
* The MultiMC instance should support most BTW versions and their addons, install them normally via `Add to Minecraft.jar`.
* Addons developed outside of the fabric environment that use 
Java reflection might not work if they reference obfuscated names via Strings (as mentioned above). 
Porting those addons is a simple process though, as only the new intermediary names have to be adopted.

## License
This project incorporates:
* A modified version of [Fabric Loom](https://github.com/FabricMC/fabric-loom) (MIT)
* A precompiled version of [Tiny Remapper](https://github.com/FabricMC/tiny-remapper) (LGPL-3.0)
