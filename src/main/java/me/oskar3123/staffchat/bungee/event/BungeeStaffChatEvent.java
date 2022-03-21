package me.oskar3123.staffchat.bungee.event;

import java.util.Objects;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

public class BungeeStaffChatEvent extends Event implements Cancellable {

  private final ProxiedPlayer player;

  private boolean cancelled = false;
  private String format;
  private String message;

  public BungeeStaffChatEvent(
      @NotNull ProxiedPlayer player, @NotNull String format, @NotNull String message) {
    this.player = player;
    this.format = format;
    this.message = message;
  }

  public @NotNull String getFormat() {
    return format;
  }

  public void setFormat(@NotNull String format) {
    this.format = Objects.requireNonNull(format);
  }

  public @NotNull String getMessage() {
    return message;
  }

  public void setMessage(@NotNull String message) {
    this.message = Objects.requireNonNull(message);
  }

  public @NotNull ProxiedPlayer getPlayer() {
    return player;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
