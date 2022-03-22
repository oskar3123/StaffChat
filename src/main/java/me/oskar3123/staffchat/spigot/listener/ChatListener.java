package me.oskar3123.staffchat.spigot.listener;

import me.oskar3123.staffchat.spigot.handler.StaffChatHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener {

  private final StaffChatHandler staffChatHandler;

  public ChatListener(@NotNull StaffChatHandler staffChatHandler) {
    this.staffChatHandler = staffChatHandler;
  }

  @EventHandler
  public void chat(@NotNull AsyncPlayerChatEvent event) {
    staffChatHandler.onChatEvent(event);
  }
}
