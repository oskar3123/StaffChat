package me.oskar3123.staffchat.spigot.listener;

import me.oskar3123.staffchat.spigot.Main;
import me.oskar3123.staffchat.spigot.event.StaffChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{

    private Main plugin;

    public ChatListener(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
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
        String message = event.getMessage().substring(character.length()).trim();

        StaffChatEvent chatEvent = new StaffChatEvent(event.getPlayer(), format, message);
        Bukkit.getServer().getPluginManager().callEvent(chatEvent);
        if (chatEvent.isCancelled())
        {
            return;
        }
        format = chatEvent.getFormat();

        format = format.replaceAll("\\{NAME\\}", sanitize(event.getPlayer().getName()));
        format = format.replaceAll("\\{MESSAGE\\}", sanitize(message));
        format = ChatColor.translateAlternateColorCodes('&', format);
        final String finalMessage = format;

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(plugin.seePerm))
                .forEach(p -> p.sendMessage(finalMessage));
        plugin.getLogger().info(ChatColor.stripColor(finalMessage));

        event.setCancelled(true);
    }

    private String sanitize(String string)
    {
        string = string.replaceAll("\\\\", "\\\\\\\\");
        string = string.replaceAll("\\$", "\\\\\\$");
        return string;
    }

}
