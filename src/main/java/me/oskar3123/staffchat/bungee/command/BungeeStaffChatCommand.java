package me.oskar3123.staffchat.bungee.command;

import me.oskar3123.staffchat.bungee.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class BungeeStaffChatCommand extends Command
{

    private BungeeMain main;
    private Configuration config;

    public BungeeStaffChatCommand(BungeeMain main)
    {
        super("staffchat");
        this.main = main;
        config = main.getConfig();
    }

    public void execute(CommandSender sender, String[] args)
    {
        BaseComponent[] noPerm = txt(config.getString("messages.prefix") + config.getString("messages.nopermission"));
        if (!sender.hasPermission(main.commandPerm))
        {
            sender.sendMessage(noPerm);
            return;
        }
        if (args.length < 1)
        {
            help(sender, getName());
            return;
        }
        if (args[0].equalsIgnoreCase("reload"))
        {
            if (sender.hasPermission(main.reloadPerm))
            {
                reload(sender);
            }
            else
            {
                sender.sendMessage(noPerm);
            }
            return;
        }
        help(sender, getName());
    }

    private void help(CommandSender sender, String label)
    {
        String prefix = config.getString("messages.prefix");
        sender.sendMessage(txt(prefix + "Version " + main.getDescription().getVersion() + ", made by oskar3123"));
        if (sender.hasPermission(main.reloadPerm))
        {
            sender.sendMessage(txt(prefix + "/" + label + " reload - Reloads the config file"));
        }
    }

    private void reload(CommandSender sender)
    {
        main.reloadConfig();
        config = main.getConfig();
        sender.sendMessage(txt(config.getString("messages.prefix") + config.getString("messages.reloaded")));
    }

    private BaseComponent[] txt(String text)
    {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
    }

}
