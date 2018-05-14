package me.oskar3123.staffchat.bungee.event;

import me.oskar3123.staffchat.TestProxiedPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BungeeStaffChatEventTest
{

    private ProxiedPlayer player;
    private String format;
    private String message;
    private BungeeStaffChatEvent event;

    @Before
    public void before()
    {
        this.player = new TestProxiedPlayer();
        this.format = "Format";
        this.message = "Message";
        this.event = new BungeeStaffChatEvent(player, format, message);
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
    public void getMessage()
    {
        assertEquals(message, event.getMessage());
    }

    @Test
    public void getPlayer()
    {
        assertEquals(player, event.getPlayer());
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