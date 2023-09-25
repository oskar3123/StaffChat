package me.oskar3123.staffchat.bungee.command;

import me.oskar3123.staffchat.bungee.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BungeeStaffChatCommand extends Command {

  private final BungeeMain plugin;
  private Configuration config;

  public BungeeStaffChatCommand(@NotNull BungeeMain plugin) {
    super("staffchat");
    this.plugin = plugin;
    config = plugin.getConfig();
  }

  public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
    BaseComponent[] noPerm =
        txt(config.getString("messages.prefix") + config.getString("messages.nopermission"));
    if (!sender.hasPermission(plugin.commandPerm)) {
      sender.sendMessage(noPerm);
      return;
    }
    if (args.length < 1) {
      help(sender, getName());
      return;
    }
    if (args[0].equalsIgnoreCase("reload")) {
      if (sender.hasPermission(plugin.reloadPerm)) {
        reload(sender);
      } else {
        sender.sendMessage(noPerm);
      }
      return;
    } else if (args[0].equalsIgnoreCase("toggle")) {
      if (sender.hasPermission(plugin.usePerm)) {
        toggle(sender);
      } else {
        sender.sendMessage(noPerm);
      }
      return;
    }
    help(sender, getName());
  }

  private void playerOnly(@NotNull CommandSender sender) {
    sender.sendMessage(
        txt(config.getString("messages.prefix") + config.getString("messages.playeronly")));
  }

  private void help(@NotNull CommandSender sender, String label) {
    String prefix = config.getString("messages.prefix");
    sender.sendMessage(
        txt(prefix + "Version " + plugin.getDescription().getVersion() + ", made by oskar3123"));
    if (sender.hasPermission(plugin.usePerm)) {
      sender.sendMessage(txt(prefix + "Message prefix: " + config.getString("settings.character")));
      sender.sendMessage(txt(prefix + "/" + label + " toggle - Toggles auto staffchat"));
    }
    if (sender.hasPermission(plugin.reloadPerm)) {
      sender.sendMessage(txt(prefix + "/" + label + " reload - Reloads the config file"));
    }
  }

  private void reload(@NotNull CommandSender sender) {
    plugin.reloadConfig();
    config = plugin.getConfig();
    sender.sendMessage(
        txt(config.getString("messages.prefix") + config.getString("messages.reloaded")));
  }

  private void toggle(@NotNull CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      playerOnly(sender);
      return;
    }
    boolean toggled = plugin.chatListener.togglePlayer(((ProxiedPlayer) sender).getUniqueId());
    String state =
        toggled ? config.getString("messages.onstring") : config.getString("messages.offstring");
    sender.sendMessage(
        txt(
            config.getString("messages.prefix")
                + String.format(config.getString("messages.toggled", ""), state)));
  }

  @Contract("_ -> new")
  private @NotNull BaseComponent[] txt(@NotNull String text) {
    return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
  }
}
