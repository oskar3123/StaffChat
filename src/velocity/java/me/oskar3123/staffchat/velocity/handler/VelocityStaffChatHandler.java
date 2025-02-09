package me.oskar3123.staffchat.velocity.handler;

import com.velocitypowered.api.proxy.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import me.oskar3123.staffchat.util.FormatUtils;
import me.oskar3123.staffchat.util.Permissions;
import me.oskar3123.staffchat.velocity.VelocityStaffChat;
import me.oskar3123.staffchat.velocity.event.VelocityStaffChatEvent;
import me.oskar3123.staffchat.velocity.event.VelocityStaffChatEvent.StaffChatResult;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityStaffChatHandler {

  private final VelocityStaffChat plugin;
  private final Set<UUID> toggledPlayers;

  public VelocityStaffChatHandler(VelocityStaffChat plugin) {
    this.plugin = plugin;
    this.toggledPlayers = new HashSet<>();
  }

  public boolean isToggled(UUID uuid) {
    return toggledPlayers.contains(uuid);
  }

  public boolean togglePlayer(UUID uuid) {
    if (toggledPlayers.contains(uuid)) {
      toggledPlayers.remove(uuid);
      return false;
    } else {
      toggledPlayers.add(uuid);
      return true;
    }
  }

  public void broadcastStaffMessage(Player player, String message) {
    String format = plugin.getConfig().node("settings", "format").getString("&b{NAME}: {MESSAGE}");

    plugin
        .getServer()
        .getEventManager()
        .fire(new VelocityStaffChatEvent(player, format, message))
        .thenAccept(
            staffChatEvent -> {
              StaffChatResult result = staffChatEvent.getResult();

              if (result.isAllowed()) {
                plugin
                    .getStaffChatHandler()
                    .broadcastStaffMessage(
                        player,
                        result.getFormat().orElse(format),
                        result.getMessage().orElse(message));
              }
            });
  }

  private void broadcastStaffMessage(Player player, String format, String message) {
    String formattedMessage =
        FormatUtils.replacePlaceholders(format, Function.identity(), player::getUsername, message);

    TextComponent component =
        LegacyComponentSerializer.legacyAmpersand().deserialize(formattedMessage);
    plugin.getServer().getAllPlayers().stream()
        .filter(p -> p.hasPermission(Permissions.SEE.permission()))
        .forEach(p -> p.sendMessage(component));
    plugin.getServer().getConsoleCommandSource().sendMessage(component);
  }
}
