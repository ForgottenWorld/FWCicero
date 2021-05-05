package it.forgottenworld.fwcicero;

import it.forgottenworld.fwcicero.command.CiceroCommandExecutor;
import it.forgottenworld.fwcicero.command.ProtectCommandExecutor;
import it.forgottenworld.fwcicero.config.ConfigManager;
import it.forgottenworld.fwcicero.listeners.BecomeResidentListener;
import it.forgottenworld.fwcicero.listeners.DisbandTownListener;
import it.forgottenworld.fwcicero.listeners.NewPlayerListener;
import it.forgottenworld.fwcicero.object.managers.CiceroLocationManager;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class FWCicero extends JavaPlugin {

    static FileConfiguration defaultConfig;
    private static ConfigManager database;
    private static FWCicero instance;

    public static FWCicero getInstance() {
        return instance;
    }

    // Metodi statici per semplificare il logging
    public static void info(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"[FWCicero] " + ChatColor.GREEN + msg);
    }

    public static void warning(String msg)
    {
        Bukkit.getLogger().log(Level.WARNING,"[FWCicero] " + ChatColor.GOLD + msg);
    }

    public static void error(String msg)
    {
        Bukkit.getLogger().log(Level.SEVERE,"[FWCicero] " + ChatColor.RED + msg);
    }

    public static void debug(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"[FWCicero] [Debug]" + ChatColor.AQUA + msg);
    }

    @Override
    public void onEnable() {
        info("Enabled!");

        FWCicero.instance = this;

        info("Loading configuration...");
        loadConfiguration();

        // Caricamento da file
        info("Loading infos...");
        loadConfigManager();
        loadData();
        info("Data loaded");

        // Registrazione CommandExecutor
        info("Registering commands...");
        this.getCommand("cicero").setExecutor(new CiceroCommandExecutor());
        this.getCommand("protect").setExecutor(new ProtectCommandExecutor());

        // Registrazione event-listeners
        info("Registering listeners...");
        this.getServer().getPluginManager().registerEvents(new NewPlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new DisbandTownListener(), this);
        this.getServer().getPluginManager().registerEvents(new BecomeResidentListener(), this);
    }

    @Override
    public void onDisable() {
        info("Saving infos...");
        saveData();
        FWCicero.info("Data Saved");
        info("Disabled");
    }

    public static void loadConfigManager(){
        database = new ConfigManager();
        database.setup("database.yml");
    }

    public ConfigManager getDatabase(){
        return database;
    }

    public static void loadData() {
        info("Loading Configurations...");
        instance.reloadConfig();

        info("Loading Database...");
        CiceroLocationManager.load();
        CiceroManager.load();
    }

    public static void saveData() {
        info("Saving Configurations...");

        info("Saving Database");
        CiceroLocationManager.save();
        CiceroManager.save();
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
