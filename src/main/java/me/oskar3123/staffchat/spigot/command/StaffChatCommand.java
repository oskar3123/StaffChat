package me.oskar3123.staffchat.spigot.command;

import java.util.Objects;
import me.oskar3123.staffchat.spigot.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StaffChatCommand implements CommandExecutor {

  private final Main plugin;
  private FileConfiguration config;

  public StaffChatCommand(@NotNull Main plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {
    String noPerm =
        clr(config.getString("messages.prefix") + config.getString("messages.nopermission"));
    if (!sender.hasPermission(plugin.commandPerm)) {
      sender.sendMessage(noPerm);
      return true;
    }
    if (args.length < 1) {
      help(sender, label);
      return true;
    }
    if (args[0].equalsIgnoreCase("reload")) {
      if (sender.hasPermission(plugin.reloadPerm)) {
        reload(sender);
      } else {
        sender.sendMessage(noPerm);
      }
      return true;
    } else if (args[0].equalsIgnoreCase("toggle")) {
      if (sender.hasPermission(plugin.usePerm)) {
        toggle(sender);
      } else {
        sender.sendMessage(noPerm);
      }
      return true;
    }
    help(sender, label);
    return true;
  }

  private void playerOnly(@NotNull CommandSender sender) {
    sender.sendMessage(
        clr(config.getString("messages.prefix") + config.getString("messages.playeronly")));
  }

  private void help(@NotNull CommandSender sender, String label) {
    String prefix = clr(Objects.requireNonNull(config.getString("messages.prefix", "")));
    sender.sendMessage(
        prefix + "Version " + plugin.getDescription().getVersion() + ", made by oskar3123");
    if (sender.hasPermission(plugin.usePerm)) {
      sender.sendMessage(prefix + "Message prefix: " + config.getString("settings.character"));
      sender.sendMessage(prefix + "/" + label + " toggle - Toggles auto staffchat");
    }
    if (sender.hasPermission(plugin.reloadPerm)) {
      sender.sendMessage(prefix + "/" + label + " reload - Reloads the config file");
    }
  }

  private void reload(@NotNull CommandSender sender) {
    plugin.reloadConfig();
    config = plugin.getConfig();
    sender.sendMessage(
        clr(config.getString("messages.prefix") + config.getString("messages.reloaded")));
  }

  private void toggle(@NotNull CommandSender sender) {
    if (!(sender instanceof Player)) {
      playerOnly(sender);
      return;
    }
    boolean toggled = plugin.chatListener.togglePlayer(((Player) sender).getUniqueId());
    String state =
        toggled ? config.getString("messages.onstring") : config.getString("messages.offstring");
    sender.sendMessage(
        clr(
            config.getString("messages.prefix")
                + String.format(
                    Objects.requireNonNull(config.getString("messages.toggled", "")), state)));
  }

  @Contract("_ -> new")
  private @NotNull String clr(@NotNull String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }
}
