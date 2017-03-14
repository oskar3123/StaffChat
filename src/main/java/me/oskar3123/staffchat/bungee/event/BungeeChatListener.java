package me.oskar3123.staffchat.bungee.event;

import me.oskar3123.staffchat.bungee.BungeeMain;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeChatListener implements Listener
{

    BungeeMain plugin;

    public BungeeChatListener(BungeeMain plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void chat(ChatEvent event)
    {

    }

}
