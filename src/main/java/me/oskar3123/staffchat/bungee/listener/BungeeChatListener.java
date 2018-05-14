package me.oskar3123.staffchat.bungee.listener;

import me.oskar3123.staffchat.bungee.BungeeMain;
import me.oskar3123.staffchat.bungee.event.BungeeStaffChatEvent;
import me.oskar3123.staffchat.util.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class BungeeChatListener implements Listener
{

    private BungeeMain plugin;

    public BungeeChatListener(BungeeMain plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void chat(ChatEvent event)
    {
        if (!(event.getSender() instanceof ProxiedPlayer))
        {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (!player.hasPermission(plugin.usePerm))
        {
            return;
        }

        Configuration config = plugin.getConfig();
        String character = config.getString("settings.character");
        if (!event.getMessage().startsWith(character))
        {
            return;
        }

        String format = config.getString("settings.format");
        String message = event.getMessage().substring(character.length()).trim();

        BungeeStaffChatEvent chatEvent = new BungeeStaffChatEvent(player, format, message);
        plugin.getProxy().getPluginManager().callEvent(chatEvent);
        if (chatEvent.isCancelled())
        {
            return;
        }
        format = chatEvent.getFormat();

        format = format.replaceAll("\\{NAME\\}", StringUtils.sanitize(player.getName()));
        format = format.replaceAll("\\{MESSAGE\\}", StringUtils.sanitize(message));
        final BaseComponent[] messageComponents = txt(format);

        plugin.getProxy().getPlayers().stream()
                .filter(p -> p.hasPermission(plugin.seePerm))
                .forEach(p -> p.sendMessage(messageComponents));
        plugin.getLogger().info(ChatColor.stripColor(clr(format)));

        event.setCancelled(true);
    }

    private String clr(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private BaseComponent[] txt(String text)
    {
        return TextComponent.fromLegacyText(clr(text));
    }

}
