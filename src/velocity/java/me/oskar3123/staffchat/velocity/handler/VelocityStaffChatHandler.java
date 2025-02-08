package me.oskar3123.staffchat.velocity.handler;

import com.velocitypowered.api.proxy.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import me.oskar3123.staffchat.util.FormatUtils;
import me.oskar3123.staffchat.util.Permissions;
import me.oskar3123.staffchat.velocity.VelocityStaffChat;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityStaffChatHandler {

  private static final String DEFAULT_FORMAT = "&b{NAME}: {MESSAGE}";

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

  public void broadcastStaffMessage(Player sender, String message) {
    if (message.isEmpty()) {
      return;
    }

    String formattedMessage =
        FormatUtils.replacePlaceholders(
            DEFAULT_FORMAT, Function.identity(), sender::getUsername, message);

    TextComponent component =
        LegacyComponentSerializer.legacyAmpersand().deserialize(formattedMessage);
    plugin.getServer().getAllPlayers().stream()
        .filter(p -> p.hasPermission(Permissions.SEE.permission()))
        .forEach(p -> p.sendMessage(component));
    plugin.getServer().getConsoleCommandSource().sendMessage(component);
  }
}
