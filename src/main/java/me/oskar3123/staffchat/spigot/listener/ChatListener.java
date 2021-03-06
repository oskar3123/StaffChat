package me.oskar3123.staffchat.spigot.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.oskar3123.staffchat.spigot.Main;
import me.oskar3123.staffchat.spigot.event.StaffChatEvent;
import me.oskar3123.staffchat.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatListener implements Listener, PluginMessageListener
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

        StaffChatEvent chatEvent = new StaffChatEvent(event.isAsynchronous(), event.getPlayer(), format, message);
        Bukkit.getServer().getPluginManager().callEvent(chatEvent);
        if (chatEvent.isCancelled())
        {
            return;
        }
        format = chatEvent.getFormat();
        message = chatEvent.getMessage();

        format = plugin.replacePlaceholders(event.getPlayer(), format);
        format = format.replaceAll("\\{NAME\\}", StringUtils.sanitize(event.getPlayer().getName()));
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replaceAll("\\{MESSAGE\\}", StringUtils.sanitize(message));
        final String finalMessage = format;

        if (plugin.getConfig().getBoolean("settings.sendmessagestoallservers"))
        {
            sendForwardPluginMessage(event.getPlayer(), finalMessage);
        }
        sendStaffChatMessage(finalMessage);

        event.setCancelled(true);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message)
    {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (!subChannel.equals("StaffChat"))
        {
            return;
        }
        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        String actualMessage;
        try
        {
            actualMessage = msgin.readUTF();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            plugin.getLogger().severe("Failed to read plugin message from bungeecord");
            return;
        }
        sendStaffChatMessage(actualMessage);
    }

    private void sendStaffChatMessage(final String message)
    {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(plugin.seePerm))
                .forEach(p -> p.sendMessage(message));
        plugin.getLogger().info(ChatColor.stripColor(message));
    }

    private void sendForwardPluginMessage(Player player, final String finalMessage)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF("StaffChat");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        try
        {
            dataStream.writeUTF(finalMessage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            plugin.getLogger().severe("Failed to send plugin message to bungeecord");
            return;
        }
        byte[] data = byteStream.toByteArray();
        out.writeShort(data.length);
        out.write(data);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
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
