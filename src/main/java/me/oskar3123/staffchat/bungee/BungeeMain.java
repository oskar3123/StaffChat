package me.oskar3123.staffchat.bungee;

import me.oskar3123.staffchat.bungee.command.BungeeStaffChatCommand;
import me.oskar3123.staffchat.bungee.event.BungeeChatListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeMain extends Plugin
{

    public final String usePerm = "staffchat.use";
    public final String seePerm = "staffchat.see";
    public final String commandPerm = "staffchat.command";
    public final String reloadPerm = "staffchat.reload";
    private Configuration config;

    public void onEnable()
    {
        new MetricsLite(this);
        saveDefaultConfig();
        if (!reloadConfig())
        {
            getLogger().severe("Could not load config file, using defaults.");
        }
        registerCommands();
        registerEvents();
    }

    private void registerCommands()
    {
        getProxy().getPluginManager().registerCommand(this, new BungeeStaffChatCommand(this));
    }

    private void registerEvents()
    {
        getProxy().getPluginManager().registerListener(this, new BungeeChatListener(this));
    }

    public boolean reloadConfig()
    {
        try
        {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            return true;
        }
        catch (IOException e)
        {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getResourceAsStream("config.yml"));
            return false;
        }
    }

    public Configuration getConfig()
    {
        return config;
    }

    private void saveDefaultConfig()
    {
        if (!getDataFolder().exists())
        {
            //noinspection ResultOfMethodCallIgnored
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists())
        {
            try (InputStream in = getResourceAsStream("config.yml"))
            {
                Files.copy(in, file.toPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
