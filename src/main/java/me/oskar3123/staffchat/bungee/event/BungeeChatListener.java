package me.oskar3123.staffchat.bungee.event;

import me.oskar3123.staffchat.bungee.BungeeMain;
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

    private BungeeMain main;

    public BungeeChatListener(BungeeMain main)
    {
        this.main = main;
    }

    @EventHandler
    public void chat(ChatEvent event)
    {
        if (!(event.getSender() instanceof ProxiedPlayer))
        {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (!player.hasPermission(main.usePerm))
        {
            return;
        }

        Configuration config = main.getConfig();
        String character = config.getString("settings.character");
        if (!event.getMessage().startsWith(character))
        {
            return;
        }

        String format = config.getString("settings.format");
        format = format.replaceAll("\\{NAME\\}", player.getName());
        format = format.replaceAll("\\{MESSAGE\\}", event.getMessage().substring(character.length()).trim());
        final BaseComponent[] message = txt(format);

        main.getProxy().getPlayers().stream()
                .filter(p -> p.hasPermission(main.seePerm))
                .forEach(p -> p.sendMessage(message));
        main.getLogger().info(ChatColor.stripColor(clr(format)));

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
