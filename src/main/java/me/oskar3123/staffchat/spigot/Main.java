package me.oskar3123.staffchat.spigot;

import me.oskar3123.staffchat.spigot.command.StaffChatCommand;
import me.oskar3123.staffchat.spigot.event.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

    public final String usePerm = "staffchat.use";
    public final String seePerm = "staffchat.see";
    public final String commandPerm = "staffchat.command";
    public final String reloadPerm = "staffchat.reload";

    public void onEnable()
    {
        saveDefaultConfig();
        registerCommands();
        registerEvents();
    }

    private void registerCommands()
    {
        getCommand("staffchat").setExecutor(new StaffChatCommand(this));
    }

    private void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
    }

}
