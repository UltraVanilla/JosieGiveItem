// SPDX-License-Identifier: AGPL-3.0-or-later
// SPDX-FileCopyrightText: 2020 JosieToolkit contributors
// SPDX-FileCopyrightText: 2020 lordpipe
package josie.giveitem;

import java.util.*;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinHandler implements Listener {
    private final JosieGiveItem plugin;

    public PlayerJoinHandler(JosieGiveItem pl) {
        this.plugin = pl;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var mm = plugin.mm;
        var player = event.getPlayer();
        var playerId = player.getUniqueId().toString();

        var config = plugin.getConfig();
        var items = config.getMapList("items");

        for (var item : items) {
            @SuppressWarnings("unchecked")
            Map<String, Object> itemMap = (Map<String, Object>) item;

            @SuppressWarnings("unchecked")
            List<String> players = (List<String>) itemMap.get("players");
            @SuppressWarnings("unchecked")
            List<String> playersReceived = (List<String>) itemMap.get("players-received");

            boolean everyone = (boolean) itemMap.get("for-everyone");

            if (everyone && !playersReceived.contains(playerId) && !players.contains(playerId)) {
                players.add(playerId);
            }

            if (players.contains(playerId) && !playersReceived.contains(playerId)) {
                if (player.getInventory().firstEmpty() != -1) {
                    itemMap.putIfAbsent("given", 0);
                    itemMap.putIfAbsent("available", Integer.MAX_VALUE);

                    var given = (int) itemMap.get("given");
                    var available = (int) itemMap.get("available");
                    if (available == 0) continue;

                    var itemId = (String) itemMap.get("id");
                    var itemStack = new ItemStack(Material.getMaterial(itemId.toUpperCase()));
                    var meta = itemStack.getItemMeta();

                    var displayName = (String) itemMap.get("display-name");
                    var lore = (String) itemMap.get("lore");
                    meta.displayName(mm.deserialize(displayName));
                    meta.lore(Arrays.asList(mm.deserialize(lore)));

                    itemStack.setItemMeta(meta);
                    player.getInventory().addItem(itemStack);

                    players.remove(playerId);
                    playersReceived.add(playerId);

                    itemMap.replace("given", given + 1);
                    itemMap.replace("available", available - 1);

                    plugin.saveConfig();
                }
            }
        }
    }
}
