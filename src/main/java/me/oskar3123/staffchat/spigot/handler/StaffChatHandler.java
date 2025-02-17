package me.oskar3123.staffchat.spigot.handler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Level;
import me.oskar3123.staffchat.spigot.Main;
import me.oskar3123.staffchat.spigot.event.StaffChatEvent;
import me.oskar3123.staffchat.util.FormatUtils;
import me.oskar3123.staffchat.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class StaffChatHandler {

  private final Main plugin;
  private final Set<UUID> toggledPlayers = new HashSet<>();

  public StaffChatHandler(Main plugin) {
    this.plugin = plugin;
  }

  public void onChatEvent(@NotNull AsyncPlayerChatEvent event) {
    if (!event.getPlayer().hasPermission(Permissions.USE.permission())) {
      return;
    }

    FileConfiguration config = plugin.getConfig();
    String character = Objects.requireNonNull(config.getString("settings.character", "@"));
    boolean isToggled = toggledPlayers.contains(event.getPlayer().getUniqueId());
    if (!event.getMessage().startsWith(character) && !isToggled) {
      return;
    }

    event.setCancelled(true);

    String format = Objects.requireNonNull(config.getString("settings.format", ""));
    String message = event.getMessage().substring(isToggled ? 0 : character.length()).trim();
    if (config.getBoolean("settings.replaceplaceholdersinmessage")) {
      message = plugin.replacePlaceholders(event.getPlayer(), message);
    }

    StaffChatEvent chatEvent =
        new StaffChatEvent(event.isAsynchronous(), event.getPlayer(), format, message);
    Bukkit.getServer().getPluginManager().callEvent(chatEvent);
    if (chatEvent.isCancelled()) {
      return;
    }
    format = chatEvent.getFormat();
    message = chatEvent.getMessage();

    format = plugin.replacePlaceholders(event.getPlayer(), format);

    sendStaffChatMessage(format, event.getPlayer()::getName, message);

    if (plugin.getConfig().getBoolean("discordsrv.enable")) {
      sendDiscordMessage(event.getPlayer(), message);
    }
  }

  public void sendStaffChatMessage(String format, Supplier<String> nameSupplier, String message) {
    final String finalMessage =
        FormatUtils.replacePlaceholders(
            format, s -> ChatColor.translateAlternateColorCodes('&', s), nameSupplier, message);

    if (plugin.getConfig().getBoolean("settings.sendmessagestoallservers")) {
      // Find any player and send using that object
      // (which player sends the plugin message is not important)
      Bukkit.getOnlinePlayers().stream()
          .findAny()
          .ifPresent(player -> sendForwardPluginMessage(player, finalMessage));
    }
    sendStaffChatMessage(finalMessage);
  }

  public void onPluginMessageReceived(@NotNull String channel, byte[] message) {
    if (!channel.equals("BungeeCord")) {
      return;
    }
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String subChannel = in.readUTF();
    if (!subChannel.equals("StaffChat")) {
      return;
    }
    short len = in.readShort();
    byte[] msgbytes = new byte[len];
    in.readFully(msgbytes);

    DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
    String actualMessage;
    try {
      actualMessage = msgin.readUTF();
    } catch (IOException e) {
      plugin.getLogger().log(Level.SEVERE, "Failed to read plugin message from bungeecord", e);
      return;
    }
    sendStaffChatMessage(actualMessage);
  }

  private void sendForwardPluginMessage(@NotNull Player player, @NotNull String message) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Forward");
    out.writeUTF("ONLINE");
    out.writeUTF("StaffChat");
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    DataOutputStream dataStream = new DataOutputStream(byteStream);
    try {
      dataStream.writeUTF(message);
    } catch (IOException e) {
      plugin.getLogger().log(Level.SEVERE, "Failed to send plugin message to bungeecord", e);
      return;
    }
    byte[] data = byteStream.toByteArray();
    out.writeShort(data.length);
    out.write(data);
    player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
  }

  private void sendStaffChatMessage(@NotNull String message) {
    Bukkit.getOnlinePlayers().stream()
        .filter(p -> p.hasPermission(Permissions.SEE.permission()))
        .forEach(p -> p.sendMessage(message));
    plugin.getLogger().info(ChatColor.stripColor(message));
  }

  private void sendDiscordMessage(Player player, String message) {
    if (Bukkit.getPluginManager().isPluginEnabled("DiscordSRV")) {
      String format =
          plugin.replacePlaceholders(
              player, plugin.getConfig().getString("discordsrv.minecraft-to-discord-format", ""));
      format =
          FormatUtils.replacePlaceholders(
              format,
              s -> ChatColor.translateAlternateColorCodes('&', s),
              player::getName,
              message);
      DiscordUtil.sendMessage(
          DiscordSRV.getPlugin()
              .getOptionalTextChannel(
                  plugin.getConfig().getString("discordsrv.channel", "staffchat")),
          format);
    }
  }

  public boolean togglePlayer(@NotNull UUID player) {
    if (toggledPlayers.contains(player)) {
      toggledPlayers.remove(player);
      return false;
    }
    toggledPlayers.add(player);
    return true;
  }
}
