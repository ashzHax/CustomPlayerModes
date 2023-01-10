package io.ashz.utility;

import io.ashz.CustomPlayerModes;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;

public class ExF {
    public static Player getStringPlayer(CustomPlayerModes plugin, String playerName) {
        Collection<? extends Player> playerCollection = plugin.getServer().getOnlinePlayers();

        for(Player indexPlayer : playerCollection) {
            if(indexPlayer.getName() == playerName) {
                return indexPlayer;
            }
        }

        return null;
    }

    // initialize a directory within the plugin configuration directory
    public static void initCustomPluginDirectory(CustomPlayerModes p, String s) {
        File dir = new File(p.getDataFolder(), s);

        if(!dir.exists()) {
            try {
                if(dir.mkdirs()) {
                    Message.sendConsoleMessage(p, "created directory ["+s+"], skipping");
                } else {
                    Message.sendConsoleMessage(p, "unable to create directory ["+s+"], skipping");
                }
            } catch(SecurityException e) {
                Message.sendConsoleMessage(p, "failed to create directory ["+s+"], skipping");
            }
        }
    }
}
