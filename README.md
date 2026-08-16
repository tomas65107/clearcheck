# Clearcheck
<img
  src="https://api.tomas65107.dev/cdn/mod_assets/clearcheck_icon.png"
  alt="Clearcheck"
  width="120"
  align="left"
  style="margin-right: 20px; margin-bottom: 10px;"
/>

**A server moderation tool providing rule enforcement and advanced account authentication.**

Clearcheck allows server administrators to monitor players' installed mods and packs and provides a secure, instant and convenient way to authenticate players even on offline-mode servers. All with transparency.

<br clear="left"/>
<p align="left">
  <a href="https://discord.gg/XaGFT5dAbh">
    <img src="https://api.tomas65107.dev/cdn/platformdecals/discord.png" alt="Discord server" width="242"/>
  </a>
    <a href="https://modrinth.com/mod/clearcheck">
    <img src="https://api.tomas65107.dev/cdn/platformdecals/modrinth.png" alt="Modrinth" width="242"/>
  </a>
      <a href="https://www.curseforge.com/minecraft/mc-mods/clearcheck">
    <img src="https://api.tomas65107.dev/cdn/platformdecals/curseforge.png" alt="Curseforge" width="242"/>
  </a>
      <a href="https://github.com/tomas65107/clearcheck">
    <img src="https://api.tomas65107.dev/cdn/platformdecals/github.png" alt="Github" width="242"/>
  </a>
</p>

> [!NOTE]
> Mod on Modrinth may not be available as due the Modrinth content verification!

> [!IMPORTANT]
> The mod is still in **alpha**. Expect bugs. If tokens or anything stops working, **do not hesitate** to report it! \
> You can [**report any bugs or crashes here**](https://github.com/tomas65107/clearcheck/issues), and try to [attach the log](https://minecraft.wiki/w/Tutorial:Obtaining_a_crash_report) and describe what you did

> [!TIP]
> **DOCS for this mod are [available HERE](https://docs.tomas65107.dev/clearcheck/)!**

## Main Features
- **Mod and texture pack monitoring** \
  Server owners can set up custom rules and keywords for what packs and mods to ban. [Learn how you can set this up](https://docs.tomas65107.dev/clearcheck/configuration)
- **Player authentication for offline and online servers** \
  Players on first join will receive a token, and they will need that token to connect to the server. [Learn more about tokens](https://docs.tomas65107.dev/clearcheck/tokens)
- **Player execute command watcher** \
  Set up rules in the config for what commands to watch for. Full command that the player executed will be shown in the server log.

## Start using Clearcheck in 5 minutes!
1. Install [supported NeoForge versions](https://neoforged.net/) on server if you haven't already.
2. Download Clearcheck from [Modrinth](https://modrinth.com/mod/clearcheck) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/clearcheck).
3. Place the `.jar` file in your server's `mods` folder.
4. Instruct players to also download the same mod on their client.
5. Token authentication works out-of-the-box, for asset monitoring and advanced setup, visit easy [documentation](https://docs.tomas65107.dev/clearcheck/).

## Compatibility
Currently, the mod is in Alpha and available only for NeoForge 1.21.1 (other versions not tested but may work) \
Also, plugin for server is for now not planned. I do not know how to make plugins.

If you have the expertise in making plugins for Paper MC, want to contribute, and want to make a quick wrapper (original logic will stay), feel free to contact me.

## Transparency and license
Clearcheck’s goal is to enforce server rules while minimizing unnecessary data collection. \
How we deal with this and what you can do can be [found here](https://docs.tomas65107.dev/clearcheck/transparency).

Like all my projects, Clearcheck is available under [LGPL-2.1 license](LICENSE)
