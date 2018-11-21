package me.oskar3123.staffchat.bungee.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class BungeeStaffChatEvent extends Event implements Cancellable
{

    private final ProxiedPlayer player;
    private boolean cancelled = false;
    private String format;
    private String message;

    public BungeeStaffChatEvent(ProxiedPlayer player, String format, String message)
    {
        this.player = player;
        this.format = format;
        this.message = message;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public ProxiedPlayer getPlayer()
    {
        return player;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

}
