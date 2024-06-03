![Blendium icon](assets/icon/icon_128x128.png)

# Blendium

Alpha blending for Distant Horizons and Sodium!

Blendium is archived and obsolete, because Iris shaderpacks are able to create a seamless transition between Minecraft chunks and DH LODs.  
Blendium's method of blending Minecraft chunks and DH LODs causes too many rendering artifacts that are jarring to look at.  
Voxy also has a seamless water transition without Iris shaderpacks, thanks to [MCRcortex/voxy #52](https://github.com/MCRcortex/voxy/pull/52) by [Xenthio](https://github.com/Xenthio).

## Dependencies

### Required

- [Fabric API](https://modrinth.com/mod/fabric-api) or [Quilt Standard Libraries](https://modrinth.com/mod/qsl)
- [Sodium](https://modrinth.com/mod/sodium)

### Optional

- [Distant Horizons](https://modrinth.com/mod/distanthorizons): blends nicely with the LODs from Distant Horizons
- [Iris](https://modrinth.com/mod/iris): Blendium has shaderpack presets for using Distant Horizons with Iris shaderpacks. See [Distant Horizons shader compatibility info](https://gist.github.com/Steveplays28/52db568f297ded527da56dbe6deeec0e) for shaderpacks that are compatible with Distant Horizons (and have blending), since Blendium only works with Sodium without a shaderpack enabled. Currently supports Distant Horizons' LOD brightness and saturation settings, as well as a water reflection color shader modification for Distant Horizons. When Iris isn't present the options for the shaderpack `(off)` in the config file are used

## Incompatibilities

- Forge forks of Sodium (Rubidium, Magnesium, Embeddium, etc.)
- Forge forks of Iris (Oculus)

[Create an issue](https://github.com/Steveplays28/blendium/issues/new) on the issue tracker if you've found an incompatibility!

## Download

[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/github_vector.svg)](https://github.com/Steveplays28/blendium)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/blendium)
[![curseforge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/blendium)

![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/supported/fabric_vector.svg)
![quilt](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/supported/quilt_vector.svg)

See the version info in the filename for the supported Minecraft versions.  
Made for the Fabric and Quilt modloaders.  
Client side.

## FAQ

![forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/unsupported/forge_vector.svg)

- Q: Will you be backporting this to lower Minecraft versions?  
  A: No.

- Q: Forge pls?  
  A: Also no.

## License

This project is licensed under LGPLv3, see [LICENSE](LICENSE).
