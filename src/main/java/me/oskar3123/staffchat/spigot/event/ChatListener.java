package me.oskar3123.staffchat.spigot.event;

import me.oskar3123.staffchat.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{

    private Main plugin;

    public ChatListener(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event)
    {
        if (!event.getPlayer().hasPermission(plugin.usePerm))
        {
            return;
        }

        FileConfiguration config = plugin.getConfig();
        String character = config.getString("settings.character");
        if (!event.getMessage().startsWith(character))
        {
            return;
        }

        String format = config.getString("settings.format");
        format = format.replaceAll("\\{NAME\\}", event.getPlayer().getName());
        format = format.replaceAll("\\{MESSAGE\\}", event.getMessage().substring(character.length()).trim());
        format = clr(format);
        final String message = format;

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(plugin.seePerm))
                .forEach(p -> p.sendMessage(message));
        plugin.getLogger().info(ChatColor.stripColor(message));

        event.setCancelled(true);
    }

    private String clr(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
