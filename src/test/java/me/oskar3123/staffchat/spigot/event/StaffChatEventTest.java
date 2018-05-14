package me.oskar3123.staffchat.spigot.event;

import me.oskar3123.staffchat.TestPlayer;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StaffChatEventTest
{

    private Player player;
    private String format;
    private String message;
    private StaffChatEvent event;

    @Before
    public void before()
    {
        this.player = new TestPlayer();
        this.format = "Format";
        this.message = "Message";
        this.event = new StaffChatEvent(player, format, message);
    }

    @Test
    public void testFormat()
    {
        assertEquals(format, event.getFormat());
        String newFormat = "New Format";
        event.setFormat(newFormat);
        assertEquals(newFormat, event.getFormat());
    }

    @Test
    public void getPlayer()
    {
        assertEquals(player, event.getPlayer());
    }

    @Test
    public void getMessage()
    {
        assertEquals(message, event.getMessage());
    }

    @Test
    public void testCancelled()
    {
        event.setCancelled(true);
        assertTrue(event.isCancelled());
        event.setCancelled(false);
        assertFalse(event.isCancelled());
    }

}