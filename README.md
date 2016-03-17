Order of the stone
==================
Order of the stone is a voxel game that we make, it's so far pretty similar to Minecraft.

Features
--------------
* Infinite, randomly generated worlds that are split into chunks of 16x128x16
* World generation using simplex noise and some magic
* Clever rendering (only visible block faces are being rendered)
* Automatic building of texture atlas
* Placing and breaking blocks
* Transparent/translucent blocks
* 65 536 possible block types (Gotta use 'em all!)
* Epic secret features included!
* It can turn itself on (with a little bit of help from [launcher](https://github.com/dax105/launcher))
* Nearest/Linear (broken) texture filtering
* World loading and saving with on the fly lzma compression
* Sometimes it can shut down on its own (dunno why)
* Loading and saving of all settings
* Console with some useful commands
* Fake ambient occlusion (block shading)
* Explosions!
* Survival (kinda - only hp implemented)
* Sounds and background music (well, until the soundengine crashes)
* Basic, but smooth physics
* Font managing (we are using Roboto font by Google)
* [Authentification](https://github.com/dax105/blocks/wiki/Auth-system) ~~(our/default implementation: [Register](http://ondryasondra.aspone.cz/Register.html))~~
* AABB Collisions
* Saving of custom data for blocks
* Really nice debug graph!
* Uses the most amazing library known to man - slick-util (it's crap)

WIP
---
* Blocks of different shapes (slabs!)
* Biomes (we only have 2 atm)
* AA, AF (mipmaps)
* Shaders ~~(mostly unused)~~
* Voxel models (works but unused)

Controls
--------
* WASD, mouse - movement
* Shift - boost
* Ctrl+click - click action on block - try it on stone!
* Mouse buttons - placing/breaking blocks
* F - fullscreen toggle (broken :D)
* Z - zoom
* ESC - menu
* ;/C - console
* mouse wheel - block selection

Libraries
---------
* LWJGL (lwjgl, lwjgl_util)
* SLICK-UTIL
* SNAPPY-JAVA
* PAULSCODE SOUNDSYSTEM (SoundSystem, CodecWav, CodecJOrbis, LibraryLWJGLOpenAL)

#####Made by Dax105 and LeOndryasO, textures by Tomix

**Background music:**

Game of Thrones music by Ramin Djawadi, we do not claim ownership over these songs nor are we profiting by them. Owned by HBO.  
Other music is made by us
