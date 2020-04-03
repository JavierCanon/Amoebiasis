﻿# Amoebiasis

Is a mod for the web game [Agario](https://agar.io) It included several unique features found in no other mods. Please note that the Agario community at large considers many of the features contained in this mod hacks or cheats as it could be argued that they give you an unfair advantage over others.

Also includes a bot we like to call the "grazer" which helps with the grunt work of collecting food pellets in the early game.

## Install instructions:
**Note: Zeach Cobbler is currently for Chrome Only. It does not work with Firefox+Greasemonkey.**

1) Install TamperMonkey addon for Chrome 
* https://chrome.google.com/webstore/detail/tampermonkey/dhdgffkkebhmkfjojejmpbldmpobfkfo

2) Visit the raw version of the script 
* https://github.com/JavierCanon/Amoebiasis/raw/master/src/ZeachCobbler.user.js

3) Click 'Install' on the script install confirmation page

##Key Reference
* TAB - Switch selected blob
* 8 - Self destruct (spectacular when you're really big)
* A - Toggle Acid mode
* C - Toggles display of visual cues
* D - Cycle through various debug levels
* G - Grazing mode
  * P - Enable/Disable Target Fixaton (see explanation below)
  * T - Switch target (if Target Fixation is on)
  * V - Visualization of Target paths evaluated
* E - fire at virus near cursor
* R - Fire at virus near selected blob (virus is highlighted in red)
* O - Enable/Disable firing at virus by left-mouse click (LMB duplicates 'E' functionality)

####All keys are changeable in options!

##Feature List
1. Auto-grazing (bot-like feeding on pellets) with optional target analysis visualization
   * P will enable target-fixation. With fixation on new targets are calculated only after previously chosen one is destroyed. With it off, targets are recalculated every new update, which is roughly 25 times per second, which is resource intensive and can eat up laptop battery life. 
   * With fixation on you can press 'T' to retarget to the cluster of cells nearest to the mouse cursor.
   * You can press 'V' to toggle display of target analysis lines.
2. Split Kill range indicator
   * Inner ring displays base jump distance for all cells. Your split cell will land so its center is half-in/half-out of inner circle. Outer circle shows how far out beyond the center circle your cell will land.
3. Viruses now display number of times it needs to be fed mass to make it split.
4. Display blob danger/edibility via color
   * Red - it can split and eat you
   * Orange - it can eat you, but not if it splits
   * Yellow - Same size as you
   * Green - you can eat it, but not if you split
   * Purple - you can split and still eat it
5. Each blob will display percent based on how much bigger or smaller than you it is.
6. Each of your blobs displays number of times you can use eject mass(if display mass is enabled)
7. press R to target virus (outlined in red) nearest to blob 0 and shoot exact # of shots to split.
8. press E to target nearest virus o cursor and shoot exact # of shots to split
   * Optionally, press 'O' to make left-click do same as 'E', allowing you to split viruses with a mouse click.
9. Time-To-Remerge (TTR) timer displayed on each blob that estimates when your cell will be allowed to remerge.
   * After a split one cell may already display a negative value. This is by design
10. Z to zoom in/out. Mouse scroll wheel can also be used to activate.
11. Server Select UI (Thanks to Angal for use of his code)
12. Automatically save and autofill last-used username
13. Agariomods.com skins support http://skins.agariomods.com/ 
14. Connect.Agariomods.com skins support http://connect.agariomods.com/
15. Agariomods.com style imgur-based skin support 
    * For example use i/m68ZpaW to get http://i.imgur.com/m68ZpaW.jpg as your skin.
16. GitHub avatar skins support
    * For example use 'PepinCZ to get https://avatars.githubusercontent.com/pepincz as your skin.
17. X/Y coordinate display
18. 'Time alive' tracker
19. 'Highest score' tracker
20. Acid mode support
21. Option to change keyboard binds


## Features

### Current Features:

- [X] ...


### Future Features:

- [ ] ...


## Screenshots

![](docs/img/screenshoot1.png?raw=true)


## Philosophy of Javier Cañon
* KISS by design and programming. An acronym for "keep it simple, stupid" or "keep it stupid simple", is a design principle. The KISS principle states that most systems work best if they are kept simple rather than made complicated; therefore, simplicity should be a key goal in design, and unnecessary complexity should be avoided. Variations on the phrase include: "Keep it simple, silly", "keep it short and simple", "keep it simple and straightforward", "keep it small and simple", or "keep it stupid simple".

* Select the best tools for the job, use tools that take less time to finish the job.
* Productivity over complexity and avoid unnecessary complexity for elegant or beauty code.

* Computers are machines, more powerful every year, give them hard work, concentrate on being productive.


## Issues and Bug Traking 
Please submit *bug reports* or *feature requests* on GitHub:
* [Issue | Request Features](https://github.com/JavierCanon/Amoebiasis/issues)

## Requeriments 

* ...  

## Install 

* [Download Last Build](https://github.com/JavierCanon/Amoebiasis/releases/latest)
* [Old Installers](https://github.com/JavierCanon/Amoebiasis/releases)

## Development Dependencies and Licenses Requirements 

* Microsoft Windows 8.1, 10.
* Microsoft .Net Framework Target 4.8
* Devexpress 19.2.6

## Contributing

Please read [CONTRIBUTING.md](/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.


## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

### Contributors and Used code
* RealDebugMonkey - https://github.com/RealDebugMonkey/ZeachCobbler
* albel727 - For the complete rewrite of the grazer into what is now called the 'new grazer'
* Angal - For the original Server select UI (now unused), click-to-lock multiblob feature, and multiblob grazer feature 
* Apostolique - debug text output derived from Apostolique's bot code -- https://github.com/Apostolique/Agar.io-bot
* Electronoob - Imgur skins, Agariomods.com skins, connect.agariomod.com skins
* Ephemerality - Code review
* GamerLio - Minimap from his awesome bot -- https://github.com/leomwu/agario-bot
* Gjum - Bug fixes
* Pepin - Keyboard Binds Settings, Advanced zoom function
* Incompetech - For KSP soundtrack music - http://incompetech.com/music/royalty-free/most/kerbalspaceprogram.php
* Mikeyk730 - stats screen code - https://greasyfork.org/en/scripts/10154-agar-chart-and-stats-screen
* posixphreak - Advanced zoom function improvements
* White Light - Grazer concept and enemy avoidance code

* **Javier Cañon** - *Customization work* - [JavierCanon.com](https://javiercanon.com)

See also the list of [contributors](/AUTHORS.md) who participated in this project.

## Supported by, thanks to 


![Caphyon](https://raw.githubusercontent.com/JavierCanon/Social-Office-Browser/master/docs/img/advanced-installer-iconNavLogo.png)

[Caphyon Advanced Installer](https://www.advancedinstaller.com) 
-- 

![Softcanon](https://github.com/JavierCanon/Social-Office-Webackeitor/raw/master/docs/images/logo_softcanon_200x75.gif) 

[Softcanon](https://www.softcanon.com) 
-- 

## License

This project is licensed under the GNU AFFERO GENERAL PUBLIC LICENSE Version 3 - see the [LICENSE.md](/LICENSE.md) file for details.

---
Made with ❤️ by **[Javier Cañon](https://javiercanon.com)**.