package me.oskar3123.staffchat.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StaffChatEvent extends Event implements Cancellable
{

    private static HandlerList handlerList = new HandlerList();
    private final Player player;
    private boolean cancelled = false;
    private String format;
    private String message;

    public StaffChatEvent(boolean async, Player player, String format, String message)
    {
        super(async);
        this.player = player;
        this.format = format;
        this.message = message;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlerList;
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
