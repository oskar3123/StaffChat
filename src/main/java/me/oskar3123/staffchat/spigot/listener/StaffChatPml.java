package me.oskar3123.staffchat.spigot.listener;

import me.oskar3123.staffchat.spigot.handler.StaffChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class StaffChatPml implements PluginMessageListener {

  private final StaffChatHandler staffChatHandler;

  public StaffChatPml(StaffChatHandler staffChatHandler) {
    this.staffChatHandler = staffChatHandler;
  }

  @Override
  public void onPluginMessageReceived(
      @NotNull String channel, @NotNull Player player, byte[] message) {
    staffChatHandler.onPluginMessageReceived(channel, message);
  }
}
