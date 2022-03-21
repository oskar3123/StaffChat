package me.oskar3123.staffchat.bungee.listener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import me.oskar3123.staffchat.bungee.BungeeMain;
import me.oskar3123.staffchat.bungee.event.BungeeStaffChatEvent;
import me.oskar3123.staffchat.util.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BungeeChatListener implements Listener {

  private final BungeeMain plugin;
  private final Set<UUID> toggledPlayers = new HashSet<>();

  public BungeeChatListener(@NotNull BungeeMain plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void chat(@NotNull ChatEvent event) {
    if (event.getMessage().startsWith("/")) {
      return;
    }
    if (!(event.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) event.getSender();
    if (!player.hasPermission(plugin.usePerm)) {
      return;
    }

    Configuration config = plugin.getConfig();
    String character = Objects.requireNonNull(config.getString("settings.character", "@"));
    boolean isToggled = toggledPlayers.contains(player.getUniqueId());
    if (!event.getMessage().startsWith(character) && !isToggled) {
      return;
    }

    String format = Objects.requireNonNull(config.getString("settings.format", ""));
    String message = event.getMessage().substring(isToggled ? 0 : character.length()).trim();

    BungeeStaffChatEvent chatEvent = new BungeeStaffChatEvent(player, format, message);
    plugin.getProxy().getPluginManager().callEvent(chatEvent);
    if (chatEvent.isCancelled()) {
      return;
    }
    format = chatEvent.getFormat();
    message = chatEvent.getMessage();

    format = format.replaceAll("\\{NAME}", StringUtils.sanitize(player.getName()));
    format = format.replaceAll("\\{MESSAGE}", StringUtils.sanitize(message));
    final BaseComponent[] messageComponents = txt(format);

    plugin.getProxy().getPlayers().stream()
        .filter(p -> p.hasPermission(plugin.seePerm))
        .forEach(p -> p.sendMessage(messageComponents));
    plugin.getLogger().info(ChatColor.stripColor(clr(format)));

    event.setCancelled(true);
  }

  @Contract("_ -> new")
  private @NotNull String clr(@NotNull String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  @Contract("_ -> new")
  private @NotNull BaseComponent[] txt(@NotNull String text) {
    return TextComponent.fromLegacyText(clr(text));
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
