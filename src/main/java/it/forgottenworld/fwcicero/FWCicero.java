package it.forgottenworld.fwcicero;

import it.forgottenworld.fwcicero.command.CiceroCommandExecutor;
import it.forgottenworld.fwcicero.command.tab.CiceroTabCompleter;
import it.forgottenworld.fwcicero.listeners.DisbandTownListener;
import it.forgottenworld.fwcicero.listeners.NewPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class FWCicero extends JavaPlugin {

    static FileConfiguration defaultConfig;
    public static FWCicero instance;

    //Metodi statici per semplificare il logging
    public static void info(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"["+"FWCicero"+"] " + msg);
    }

    @Override
    public void onEnable() {
        info("Enabled!");

        FWCicero.instance = this;

        info("Loading configuration...");
        loadConfiguration();

        // Registrazione CommandExecutor
        info("Registering commands...");
        this.getCommand("cicero").setExecutor(new CiceroCommandExecutor());
        this.getCommand("cicero").setTabCompleter(new CiceroTabCompleter());

        // Registrazione event-listeners
        info("Registering listeners...");
        this.getServer().getPluginManager().registerEvents(new NewPlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new DisbandTownListener(), this);

        // Caricamento delle aree da file
        info("Loading infos...");
        CiceroCommandExecutor.loadHash();
        info("Data loaded");
    }

    @Override
    public void onDisable() {
        info("Saving infos...");
        CiceroCommandExecutor.saveHash();
        FWCicero.info("Data Saved");
        info("Disabled");
    }

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        defaultConfig = getConfig();
    }

    public static FileConfiguration getDefaultConfig() {
        return defaultConfig;
    }
}
