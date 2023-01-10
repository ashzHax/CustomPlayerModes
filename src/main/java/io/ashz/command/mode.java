package io.ashz.command;

import io.ashz.CustomPlayerModes;
import io.ashz.utility.ExF;
import io.ashz.utility.PlayerFileManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mode implements CommandExecutor {
    private CustomPlayerModes plugin;

    public mode(CustomPlayerModes p) {
        this.plugin = p;
    }

    private boolean setModeSurvival(Player p) {
        PlayerFileManager playerProfile = new PlayerFileManager(this.plugin, p.getPlayer().getUniqueId().toString());
        String modeString = playerProfile.getConfiguration().getString("current_mode");

        if(modeString == "survival") {
            p.sendMessage("already in mode");
            return true;
        }

        // if mode not found
        if(modeString == null) {
            p.sendMessage("mode not found");
            playerProfile.getConfiguration().set("current_mode", "survival");
            modeString = "survival";
        }

        // save current inventory
        {
            playerProfile.getConfiguration().set(modeString + ".level", p.getLevel());
            playerProfile.getConfiguration().set(modeString + ".exp", p.getExp());
            playerProfile.getConfiguration().set(modeString + ".food", p.getFoodLevel());
            playerProfile.getConfiguration().set(modeString + ".health", p.getHealth());

            for(int i = 0; i < p.getInventory().getContents().length; i++) {
                playerProfile.getConfiguration().set(modeString+".inventory."+i, p.getInventory().getContents()[i]);
            }
            playerProfile.getConfiguration().set("current_mode", "survival");
            playerProfile.save();
        }

        modeString = "survival";

        // get new inventory
        {
            p.setLevel(playerProfile.getConfiguration().getInt(modeString + ".level"));
            p.setExp((float)playerProfile.getConfiguration().getDouble(modeString + ".exp"));
            p.setFoodLevel(playerProfile.getConfiguration().getInt(modeString + ".food"));
            p.setHealth(playerProfile.getConfiguration().getInt(modeString + ".health"));

            for(int i = 0; i < p.getInventory().getContents().length; i++) {
                p.getInventory().setItem(i, playerProfile.getConfiguration().getItemStack(modeString+".inventory."+i));
            }

            p.setGameMode(GameMode.SURVIVAL);
        }

        return true;
    }

    private boolean setModeBuild(Player p) {
        PlayerFileManager playerProfile = new PlayerFileManager(this.plugin, p.getPlayer().getUniqueId().toString());
        String modeString = playerProfile.getConfiguration().getString("current_mode");

        if(modeString == "build") {
            p.sendMessage("You are already in BUILD mode!");
            return true;
        }

        // if mode not found
        if(modeString == null) {
            p.sendMessage("mode not found");
            playerProfile.getConfiguration().set("current_mode", "survival");
            modeString = "survival";
        }

        // save current inventory
        {
            playerProfile.getConfiguration().set(modeString + ".level", p.getLevel());
            playerProfile.getConfiguration().set(modeString + ".exp", p.getExp());
            playerProfile.getConfiguration().set(modeString + ".food", p.getFoodLevel());
            playerProfile.getConfiguration().set(modeString + ".health", p.getHealth());

            for(int i = 0; i < p.getInventory().getContents().length; i++) {
                playerProfile.getConfiguration().set(modeString+".inventory."+i, p.getInventory().getContents()[i]);
            }
            playerProfile.getConfiguration().set("current_mode", "build");
            playerProfile.save();
        }

        modeString = "build";

        // get new inventory
        {
            p.setLevel(playerProfile.getConfiguration().getInt(modeString + ".level"));
            p.setExp((float)playerProfile.getConfiguration().getDouble(modeString + ".exp"));
            p.setFoodLevel(playerProfile.getConfiguration().getInt(modeString + ".food"));
            p.setHealth(playerProfile.getConfiguration().getInt(modeString + ".health"));

            for(int i = 0; i < p.getInventory().getContents().length; i++) {
                p.getInventory().setItem(i, playerProfile.getConfiguration().getItemStack(modeString+".inventory."+i));
            }

            p.setGameMode(GameMode.CREATIVE);
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // TODO: targeted command should be valid
        if(!(commandSender instanceof Player)) {
            if(strings.length < 2) {
                return false;
            }
        } else {
            if(!commandSender.isOp()) {
                commandSender.sendMessage("no permission to use this command");
                return false;
            }
        }

        switch(strings.length) {
            case 1: { // commandSender is the target
                switch(strings[0]) {
                    case "survival": {
                        return setModeSurvival((Player)commandSender);
                    }
                    case "build": {
                        return setModeBuild((Player)commandSender);
                    }
                }

                break;
            }
            case 2: { // strings[1] is the target
                Player targetPlayer = ExF.getStringPlayer(this.plugin, strings[1]);

                if(targetPlayer == null) {
                    commandSender.sendMessage("target player not found");
                    return false;
                }

                switch(strings[0]) {
                    case "survival": {
                        return setModeSurvival((Player)commandSender);
                    }
                    case "build": {
                        return setModeBuild((Player)commandSender);
                    }
                }

                break;
            }
            default: {
                return false;
            }
        }

        return true;
    }
}
