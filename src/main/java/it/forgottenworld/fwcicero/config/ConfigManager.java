package it.forgottenworld.fwcicero.config;

import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.utility.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private FWCicero plugin = FWCicero.getPlugin(FWCicero.class);

    public FileConfiguration cfg;
    public File file;

    public void setup(String name){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), name);

        if(!file.exists()){
            try{
                file.createNewFile();
                FWCicero.info(Messages.FILE_CREATED + " " + name);
            } catch (IOException e){
                FWCicero.error(Messages.FILE_NOT_CREATED + " " + name);
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFile(){
        return cfg;
    }

    public void saveFile(){
        try{
            cfg.save(file);
            //FWObbiettivi.info(Messages.FILE_SAVED + " " + getFile().getName());
        } catch (IOException e){
            FWCicero.error(Messages.FILE_NOT_SAVED + " " + getFile().getName());
        }
    }

    public void reloadFile(){
        cfg = YamlConfiguration.loadConfiguration(file);
        FWCicero.info(Messages.FILE_RELOADED + " " + getFile().getName());
    }
}
