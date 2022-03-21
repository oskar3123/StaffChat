package me.oskar3123.staffchat.spigot.event;

import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StaffChatEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final Player player;

  private boolean cancelled = false;
  private String format;
  private String message;

  public StaffChatEvent(
      boolean async, @NotNull Player player, @NotNull String format, @NotNull String message) {
    super(async);
    this.player = player;
    this.format = format;
    this.message = message;
  }

  /**
   * Required by BungeeCord
   *
   * @return the handler list
   */
  @SuppressWarnings("unused")
  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public @NotNull String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = Objects.requireNonNull(format);
  }

  public @NotNull String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = Objects.requireNonNull(message);
  }

  public @NotNull Player getPlayer() {
    return player;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
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
