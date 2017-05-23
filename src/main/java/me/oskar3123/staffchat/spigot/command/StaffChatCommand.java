package me.oskar3123.staffchat.spigot.command;

import me.oskar3123.staffchat.spigot.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class StaffChatCommand implements CommandExecutor
{

    private Main plugin;
    private FileConfiguration config;

    public StaffChatCommand(Main plugin)
    {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        String noPerm = clr(config.getString("messages.prefix") + config.getString("messages.nopermission"));
        if (!sender.hasPermission(plugin.commandPerm))
        {
            sender.sendMessage(noPerm);
            return true;
        }
        if (args.length < 1)
        {
            help(sender, label);
            return true;
        }
        if (args[0].equalsIgnoreCase("reload"))
        {
            if (sender.hasPermission(plugin.reloadPerm))
            {
                reload(sender);
            }
            else
            {
                sender.sendMessage(noPerm);
            }
            return true;
        }
        help(sender, label);
        return true;
    }

    private void help(CommandSender sender, String label)
    {
        String prefix = clr(config.getString("messages.prefix"));
        sender.sendMessage(prefix + "Version " + plugin.getDescription().getVersion() + ", made by oskar3123");
        if (sender.hasPermission(plugin.reloadPerm))
        {
            sender.sendMessage(prefix + "/" + label + " reload - Reloads the config file");
        }
    }

    private void reload(CommandSender sender)
    {
        plugin.reloadConfig();
        config = plugin.getConfig();
        sender.sendMessage(clr(config.getString("messages.prefix") + config.getString("messages.reloaded")));
    }

    private String clr(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
