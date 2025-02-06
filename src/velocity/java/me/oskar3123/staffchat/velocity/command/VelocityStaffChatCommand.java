package me.oskar3123.staffchat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.io.IOException;
import me.oskar3123.staffchat.velocity.VelocityStaffChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class VelocityStaffChatCommand implements SimpleCommand {

  private final VelocityStaffChat plugin;
  private CommentedConfigurationNode config;

  public VelocityStaffChatCommand(VelocityStaffChat plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();
  }

  @Override
  public void execute(Invocation invocation) {
    CommandSource sender = invocation.source();
    String label = invocation.alias();
    String[] args = invocation.arguments();

    Component noPerm =
        txt(
            config.node("messages", "prefix").getString()
                + config.node("messages", "nopermission").getString());
    if (!sender.hasPermission("staffchat.command")) {
      sender.sendMessage(noPerm);
      return;
    }
    if (args.length < 1) {
      help(sender, label);
      return;
    }
    if (args[0].equalsIgnoreCase("reload")) {
      if (sender.hasPermission("staffchat.reload")) {
        reload(sender);
      } else {
        sender.sendMessage(noPerm);
      }
      return;
    } else if (args[0].equalsIgnoreCase("toggle")) {
      if (sender.hasPermission("staffchat.use")) {
        toggle(sender);
      } else {
        sender.sendMessage(noPerm);
      }
      return;
    }
    help(sender, label);
  }

  @Override
  public boolean hasPermission(Invocation invocation) {
    return invocation.source().hasPermission("staffchat.command");
  }

  private void playerOnly(@NotNull CommandSource sender) {
    sender.sendMessage(
        txt(
            config.node("messages", "prefix").getString()
                + config.node("messages", "playeronly").getString()));
  }

  private void help(@NotNull CommandSource sender, String label) {
    String prefix = config.node("messages", "prefix").getString();
    sender.sendMessage(
        txt(prefix + "Version " + VelocityStaffChat.PLUGIN_VERSION + ", made by oskar3123"));
    if (sender.hasPermission("staffchat.use")) {
      sender.sendMessage(
          txt(prefix + "Message prefix: " + config.node("settings", "character").getString()));
      sender.sendMessage(txt(prefix + "/" + label + " toggle - Toggles auto staffchat"));
    }
    if (sender.hasPermission("staffchat.reload")) {
      sender.sendMessage(txt(prefix + "/" + label + " reload - Reloads the config file"));
    }
  }

  private void reload(@NotNull CommandSource sender) {
    try {
      plugin.reloadConfig();
      config = plugin.getConfig();
      sender.sendMessage(
          txt(
              config.node("messages", "prefix").getString()
                  + config.node("messages", "reloaded").getString()));
    } catch (IOException e) {
      sender.sendMessage(
          txt(
              config.node("messages", "prefix").getString()
                  + config
                      .node("messages", "reload-failed")
                      .getString("Failed to reload the config file")));
    }
  }

  private void toggle(@NotNull CommandSource sender) {
    if (!(sender instanceof Player player)) {
      playerOnly(sender);
      return;
    }
    boolean toggled = plugin.getStaffChatHandler().togglePlayer(player.getUniqueId());
    String state =
        toggled
            ? config.node("messages", "onstring").getString()
            : config.node("messages", "offstring").getString();
    sender.sendMessage(
        txt(
            config.node("messages", "prefix").getString()
                + String.format(config.node("messages", "toggled").getString(""), state)));
  }

  private @NotNull Component txt(@NotNull String text) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
  }
}
