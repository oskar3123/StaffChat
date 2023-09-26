package me.oskar3123.staffchat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.oskar3123.staffchat.common.permission.Permission;
import me.oskar3123.staffchat.velocity.VelocityMain;
import me.oskar3123.staffchat.velocity.config.Configuration;
import me.oskar3123.staffchat.velocity.listener.VelocityChatListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityStaffChatCommand implements SimpleCommand {

  private final VelocityMain plugin;
  private final VelocityChatListener chatListener;
  private Configuration config;

  public VelocityStaffChatCommand(VelocityMain plugin, VelocityChatListener chatListener) {
    this.plugin = plugin;
    this.chatListener = chatListener;
    this.config = plugin.getConfig();
  }

  @Override
  public void execute(Invocation invocation) {
    Component noPerm = txtClr(config.getPrefix() + config.getNoPerm());

    CommandSource source = invocation.source();
    String[] args = invocation.arguments();

    if (!source.hasPermission(Permission.COMMAND.getPermission())) {
      source.sendMessage(noPerm);
      return;
    }
    if (args.length < 1) {
      help(source, invocation.alias());
      return;
    }
    if (args[0].equalsIgnoreCase("reload")) {
      if (source.hasPermission(Permission.RELOAD.getPermission())) {
        reload(source);
      } else {
        source.sendMessage(noPerm);
      }
      return;
    } else if (args[0].equalsIgnoreCase("toggle")) {
      if (source.hasPermission(Permission.USE.getPermission())) {
        toggle(source);
      } else {
        source.sendMessage(noPerm);
      }
      return;
    }
    help(source, invocation.alias());
  }

  private void playerOnly(CommandSource source) {
    source.sendMessage(txtClr(config.getPrefix() + config.getPlayerOnly()));
  }

  private void help(CommandSource source, String label) {
    source.sendMessage(
        txtClr(config.getPrefix() + "Version " + VelocityMain.VERSION + ", made by oskar3123"));
    if (source.hasPermission(Permission.USE.getPermission())) {
      source.sendMessage(txtClr(config.getPrefix() + "Message prefix: " + config.getCharacter()));
      source.sendMessage(
          txtClr(config.getPrefix() + "/" + label + " toggle - Toggles auto staffchat"));
    }
    if (source.hasPermission(Permission.RELOAD.getPermission())) {
      source.sendMessage(
          txtClr(config.getPrefix() + "/" + label + " reload - Reloads the config file"));
    }
  }

  private void reload(CommandSource source) {
    plugin.reloadConfig();
    config = plugin.getConfig();
    source.sendMessage(txtClr(config.getPrefix() + config.getReloaded()));
  }

  private void toggle(CommandSource source) {
    if (!(source instanceof Player)) {
      playerOnly(source);
      return;
    }
    boolean toggled = chatListener.togglePlayer(((Player) source).getUniqueId());
    String state = toggled ? config.getOnString() : config.getOffString();
    source.sendMessage(
        txtClr(config.getPrefix() + String.format(config.getToggledFormat(), state)));
  }

  private Component txtClr(String text) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
  }
}
