package me.oskar3123.staffchat.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import me.oskar3123.staffchat.bungee.command.BungeeStaffChatCommand;
import me.oskar3123.staffchat.bungee.listener.BungeeChatListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

public class BungeeMain extends Plugin {

  private static final int BSTATS_PLUGIN_ID = 836;

  public final BungeeChatListener chatListener = new BungeeChatListener(this);
  private Configuration config;

  public void onEnable() {
    new Metrics(this, BSTATS_PLUGIN_ID);
    saveDefaultConfig();
    if (!reloadConfig()) {
      getLogger().severe("Could not load config file, using defaults.");
    }
    registerCommands();
    registerEvents();
  }

  private void registerCommands() {
    getProxy().getPluginManager().registerCommand(this, new BungeeStaffChatCommand(this));
  }

  private void registerEvents() {
    getProxy().getPluginManager().registerListener(this, chatListener);
  }

  public boolean reloadConfig() {
    try {
      config =
          ConfigurationProvider.getProvider(YamlConfiguration.class)
              .load(new File(getDataFolder(), "config.yml"));
      loadDefaultValues();
      return true;
    } catch (IOException e) {
      config =
          ConfigurationProvider.getProvider(YamlConfiguration.class)
              .load(getResourceAsStream("config.yml"));
      loadDefaultValues();
      return false;
    }
  }

  private void loadDefaultValues() {
    String[] keys =
        new String[] {
          "settings.character",
          "settings.format",
          "messages.prefix",
          "messages.nopermission",
          "messages.playeronly",
          "messages.reloaded",
          "messages.toggled",
          "messages.onstring",
          "messages.offstring",
        };
    Configuration def =
        ConfigurationProvider.getProvider(YamlConfiguration.class)
            .load(getResourceAsStream("config.yml"));
    for (String key : keys) {
      String v = config.getString(key);
      if (v == null || v.isEmpty()) {
        config.set(key, def.getString(key));
      }
    }
    saveConfig();
  }

  public Configuration getConfig() {
    return config;
  }

  private void saveDefaultConfig() {
    if (!getDataFolder().exists()) {
      //noinspection ResultOfMethodCallIgnored
      getDataFolder().mkdir();
    }

    File file = new File(getDataFolder(), "config.yml");

    if (!file.exists()) {
      try (InputStream in = getResourceAsStream("config.yml")) {
        Files.copy(in, file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void saveConfig() {
    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class)
          .save(config, new File(getDataFolder(), "config.yml"));
    } catch (IOException e) {
      e.printStackTrace();
      getLogger().severe("Failed to save config");
    }
  }
}
