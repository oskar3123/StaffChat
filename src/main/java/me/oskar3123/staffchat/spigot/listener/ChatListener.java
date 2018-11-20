package me.oskar3123.staffchat.spigot.listener;

import me.oskar3123.staffchat.spigot.Main;
import me.oskar3123.staffchat.spigot.event.StaffChatEvent;
import me.oskar3123.staffchat.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatListener implements Listener
{

    private Main plugin;
    private Set<UUID> toggledPlayers = new HashSet<>();

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
        boolean isToggled = toggledPlayers.contains(event.getPlayer().getUniqueId());
        if (!event.getMessage().startsWith(character) && !isToggled)
        {
            return;
        }

        String format = config.getString("settings.format");
        String message = event.getMessage().substring(isToggled ? 0 : character.length()).trim();
        if (config.getBoolean("settings.replaceplaceholdersinmessage"))
        {
            message = plugin.replacePlaceholders(event.getPlayer(), message);
        }

        StaffChatEvent chatEvent = new StaffChatEvent(event.getPlayer(), format, message);
        Bukkit.getServer().getPluginManager().callEvent(chatEvent);
        if (chatEvent.isCancelled())
        {
            return;
        }
        format = chatEvent.getFormat();

        format = format.replaceAll("\\{NAME\\}", StringUtils.sanitize(event.getPlayer().getName()));
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = plugin.replacePlaceholders(event.getPlayer(), format);
        format = format.replaceAll("\\{MESSAGE\\}", StringUtils.sanitize(message));

        final String finalMessage = format;

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(plugin.seePerm))
                .forEach(p -> p.sendMessage(finalMessage));
        plugin.getLogger().info(ChatColor.stripColor(finalMessage));

        event.setCancelled(true);
    }

    public boolean togglePlayer(UUID player)
    {
        if (toggledPlayers.contains(player))
        {
            toggledPlayers.remove(player);
            return false;
        }
        toggledPlayers.add(player);
        return true;
    }

}
