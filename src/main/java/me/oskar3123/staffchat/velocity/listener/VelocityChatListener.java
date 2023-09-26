package me.oskar3123.staffchat.velocity.listener;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import me.oskar3123.staffchat.bungee.util.BungeeFormatUtils;
import me.oskar3123.staffchat.common.permission.Permission;
import me.oskar3123.staffchat.velocity.VelocityMain;
import me.oskar3123.staffchat.velocity.config.Configuration;
import me.oskar3123.staffchat.velocity.util.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class VelocityChatListener implements EventHandler<PlayerChatEvent> {

  private final Set<UUID> toggledPlayers = new HashSet<>();

  private final VelocityMain plugin;

  public VelocityChatListener(VelocityMain plugin) {
    this.plugin = plugin;
  }

  @Override
  public void execute(PlayerChatEvent event) {
    if (event.getMessage().startsWith("/")) {
      return;
    }

    Player player = event.getPlayer();
    if (!player.hasPermission(Permission.USE.getPermission())) {
      return;
    }

    Configuration config = plugin.getConfig();
    String character = Objects.requireNonNull(config.getCharacter());
    boolean isToggled = toggledPlayers.contains(player.getUniqueId());
    if (!event.getMessage().startsWith(character) && !isToggled) {
      return;
    }

    String format = Objects.requireNonNull(config.getFormat());
    String message = event.getMessage().substring(isToggled ? 0 : character.length()).trim();

    //    BungeeStaffChatEvent chatEvent = new BungeeStaffChatEvent(player, format, message);
    //    plugin.getProxy().getPluginManager().callEvent(chatEvent);
    //    if (chatEvent.isCancelled()) {
    //      return;
    //    }
    //    format = chatEvent.getFormat();
    //    message = chatEvent.getMessage();

    String appliedFormat =
        BungeeFormatUtils.replacePlaceholders(
            format,
            s -> ColorUtils.translateAlternateColorCodes('&', s),
            () ->
                player
                    .getCurrentServer()
                    .map(ServerConnection::getServerInfo)
                    .map(ServerInfo::getName)
                    .orElse(""),
            player::getUsername,
            message);
    final Component messageComponent = txt(appliedFormat);

    plugin.getServer().getAllPlayers().stream()
        .filter(p -> p.hasPermission(Permission.SEE.getPermission()))
        .forEach(p -> p.sendMessage(messageComponent));
    plugin.getLogger().info(ColorUtils.stripColor(appliedFormat));

    event.setResult(ChatResult.denied());
  }

  private Component txt(String text) {
    return LegacyComponentSerializer.legacySection().deserialize(text);
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
