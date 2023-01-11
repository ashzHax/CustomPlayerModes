package io.ashz.command;

import io.ashz.CustomPlayerModes;
import io.ashz.utility.ExF;
import io.ashz.utility.Message;
import io.ashz.utility.PlayerFileManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class mode implements CommandExecutor {
    private CustomPlayerModes plugin;

    public mode(CustomPlayerModes p) {
        this.plugin = p;
    }

    private boolean setModeSurvival(Player p) {
        PlayerFileManager playerProfile = new PlayerFileManager(this.plugin, p.getUniqueId().toString());
        String modeString = playerProfile.getConfiguration().getString("current_mode");
        ItemStack[] playerStack;

        // check if user is already in target mode
        if(Objects.equals(modeString, "survival")) {
            p.sendMessage(Message.error+"이미 survival 모드 입니다.");
            return true;
        }

        // if mode not found
        if(modeString == null) {
            p.sendMessage(Message.warning+"설정을 못찾았습니다! 새 설정을 생성하겠습니다");
            playerProfile.getConfiguration().set("current_mode", "survival");
            modeString = "survival";
        }

        playerStack = p.getInventory().getContents();

        // save current inventory
        {
            playerProfile.getConfiguration().set(modeString + ".level", p.getLevel());
            playerProfile.getConfiguration().set(modeString + ".exp", p.getExp());
            playerProfile.getConfiguration().set(modeString + ".food", p.getFoodLevel());
            playerProfile.getConfiguration().set(modeString + ".health", p.getHealth());

            for(int i = 0; i < playerStack.length; i++) {
                if(playerStack[i] != null) {
                    playerProfile.getConfiguration().set(modeString + ".inventory." + i, playerStack[i]);
                }
            }
        }

        modeString = "survival";
        playerProfile.getConfiguration().set("current_mode", "survival");
        playerProfile.save();

        // get new inventory
        {
            int level = playerProfile.getConfiguration().getInt(modeString + ".level");
            float exp = (float)playerProfile.getConfiguration().getDouble(modeString + ".exp");
            int food = playerProfile.getConfiguration().getInt(modeString + ".food");
            int health = playerProfile.getConfiguration().getInt(modeString + ".health");

            if(health <= 0) {
                health = 20;
            }

            if(food <= 0) {
                food = 20;
            }

            p.setLevel(level);
            p.setExp(exp);
            p.setFoodLevel(food);
            p.setHealth(health);

            for(int i = 0; i < playerStack.length; i++) {
                p.getInventory().setItem(i, playerProfile.getConfiguration().getItemStack(modeString + ".inventory." + i));
            }

            p.setGameMode(GameMode.SURVIVAL);
        }

        p.sendMessage(Message.notification+ChatColor.AQUA+"survival"+ChatColor.GRAY+" 모드로 변경되었습니다");

        return true;
    }

    private boolean setModeBuild(Player p) {
        PlayerFileManager playerProfile = new PlayerFileManager(this.plugin, p.getUniqueId().toString());
        String modeString = playerProfile.getConfiguration().getString("current_mode");
        ItemStack[] playerStack;

        // check if user is already in target mode
        if(Objects.equals(modeString, "build")) {
            p.sendMessage(Message.error+"이미 build 모드 입니다.");
            return true;
        }

        // if mode not found
        if(modeString == null) {
            p.sendMessage(Message.warning+"설정을 못찾았습니다! 새 설정을 생성하겠습니다");
            playerProfile.getConfiguration().set("current_mode", "survival");
            modeString = "survival";
        }

        playerStack = p.getInventory().getContents();

        // save current inventory
        {
            playerProfile.getConfiguration().set(modeString + ".level", p.getLevel());
            playerProfile.getConfiguration().set(modeString + ".exp", p.getExp());
            playerProfile.getConfiguration().set(modeString + ".food", p.getFoodLevel());
            playerProfile.getConfiguration().set(modeString + ".health", p.getHealth());

            for(int i = 0; i < playerStack.length; i++) {
                if(playerStack[i] != null) {
                    playerProfile.getConfiguration().set(modeString + ".inventory." + i, playerStack[i]);
                }
            }
        }

        modeString = "build";
        playerProfile.getConfiguration().set("current_mode", "build");
        playerProfile.save();

        // get new inventory
        {
            int level = playerProfile.getConfiguration().getInt(modeString + ".level");
            float exp = (float)playerProfile.getConfiguration().getDouble(modeString + ".exp");
            int food = playerProfile.getConfiguration().getInt(modeString + ".food");
            int health = playerProfile.getConfiguration().getInt(modeString + ".health");

            if(health <= 0) {
                health = 20;
            }

            if(food <= 0) {
                food = 20;
            }

            p.setLevel(level);
            p.setExp(exp);
            p.setFoodLevel(food);
            p.setHealth(health);

            for(int i = 0; i < playerStack.length; i++) {
                p.getInventory().setItem(i, playerProfile.getConfiguration().getItemStack(modeString + ".inventory." + i));
            }

            p.setGameMode(GameMode.CREATIVE);
        }

        p.sendMessage(Message.notification+ChatColor.AQUA+"build"+ ChatColor.GRAY+" 모드로 변경되었습니다");

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
