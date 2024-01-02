// SPDX-License-Identifier: AGPL-3.0-or-later
// SPDX-FileCopyrightText: 2020 JosieToolkit contributors
// SPDX-FileCopyrightText: 2020 lordpipe
package josie.giveitem;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class JosieGiveItem extends JavaPlugin {
    public final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(this), this);
    }

    @Override
    public void onDisable() {}
}
