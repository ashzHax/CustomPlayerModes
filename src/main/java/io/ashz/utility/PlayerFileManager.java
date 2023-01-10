package io.ashz.utility;

import io.ashz.CustomPlayerModes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.security.Security;

public class PlayerFileManager {
    private CustomPlayerModes plugin;
    private File dir;
    private String fileName;
    private File file;
    private FileConfiguration configuration;

    public PlayerFileManager(CustomPlayerModes p, String f) {
        this.plugin = p;
        this.fileName = f+".yml";
        this.dir = new File(this.plugin.getDataFolder(), "player");
        this.file = new File(dir, this.fileName);

        // check if directory exists
        if(!this.dir.exists()) {
            try {
                if(this.dir.mkdirs()) {
                    Message.sendConsoleMessage(this.plugin, "created directory ["+ this.dir.getPath() +"], skipping");
                } else {
                    Message.sendConsoleMessage(this.plugin, "unable to create directory ["+ this.dir.getPath() +"], skipping");
                }
            } catch(SecurityException e) {
                Message.sendConsoleMessage(this.plugin, "failed to create directory ["+ this.dir.getPath() +"], skipping");
            }
        }

        // check if file exists
        if(!this.file.exists()) {
            try {
                if(this.file.createNewFile()) {
                    Message.sendConsoleMessage(this.plugin, "created new file ["+this.file.getPath()+"]");
                } else { // this block should never run
                    Message.sendConsoleMessage(this.plugin, "critical logical error, created new file ["+this.file.getPath()+"]");
                }
            } catch(IOException e) {
                e.printStackTrace();
                Message.sendConsoleMessage(this.plugin, "failed to create file ["+this.file.getPath()+"], critical error");
           } catch(SecurityException e) {
                e.printStackTrace();
                Message.sendConsoleMessage(this.plugin, "unable to access file ["+this.file.getPath()+"], no permission");
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        this.save();
    }

    public void save() {
        try {
            configuration.save(this.file);
        } catch(IOException e) {
            e.printStackTrace();
            Message.sendConsoleMessage(this.plugin, "failed fo save file ["+this.file.getPath()+"]");
        }
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }
}