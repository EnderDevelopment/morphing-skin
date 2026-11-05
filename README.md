# ShapeShifter – Minecraft Skin Morphing Plugin

ShapeShifter is a Minecraft plugin that lets players morph their skin into different predefined forms. With an intuitive `/skin` command and a clickable GUI menu, players can instantly switch between custom skin presets, restore their original skin at any time, and have their choices automatically remembered when they rejoin the server. The plugin is built on Paper 1.21 and uses persistent profiles to apply skin textures seamlessly.

## Install

1. Download the ShapeShifter plugin JAR file.
2. Stop your Minecraft server.
3. Place the JAR file into your server's `plugins/` folder.
4. Start your server once to generate the default `config.yml` file.
5. Stop the server to edit the configuration.

## Use

1. **Configure skins**: Open `plugins/MorphingSkin/config.yml` and fill in the `texture` and optionally `signature` values for each skin preset. You can obtain these values from skin sites like mineskin.org. You can also add new skin presets by copying an existing block under `skins:`.

2. **Start the server**: Launch your server to load the plugin and your configured skins.

3. **Open the skin menu**: In-game, run `/skin` (requires the default `morphingskin.use` permission) to open the morphing skin selection GUI.

4. **Apply a skin**: Click any skin icon in the GUI to equip it. The effect appears after you rejoin or respawn.

5. **Apply directly**: Use `/skin <name>` to apply a specific skin by its configuration name.

6. **Reset your skin**: Use `/skin reset` or click the red barrier item in the GUI to restore your original skin.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/eb689df0-3d11-4ecc-b1ae-02d4d3e8623d?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=eb689df0-3d11-4ecc-b1ae-02d4d3e8623d
