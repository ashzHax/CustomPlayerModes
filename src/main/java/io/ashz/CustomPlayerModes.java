package io.ashz;

import io.ashz.command.mode;
import io.ashz.utility.Message;
import io.ashz.utility.PlayerFileManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomPlayerModes extends JavaPlugin {
    public String pluginName = "CustomPlayerModes";
    private void handleCommand(String command, CommandExecutor handleFunc) {
        PluginCommand p = this.getCommand(command);

        if(p != null) {
            Message.sendConsoleMessage(this, "no handler found for command \""+command+"\", creating new handler...");
        } else {
            Message.sendConsoleMessage(this, "handler found for command \""+command+"\", trying to overwrite it...");
        }

        p.setExecutor(handleFunc);
    }

    @Override
    public void onEnable() {
        handleCommand("mode", new mode(this));
        Message.sendConsoleMessage(this, Message.notification + "starting plugin: "+this.pluginName);
    }

    @Override
    public void onDisable() {
        Message.sendConsoleMessage(this, Message.warning + "ending plugin: "+this.pluginName);
    }
}