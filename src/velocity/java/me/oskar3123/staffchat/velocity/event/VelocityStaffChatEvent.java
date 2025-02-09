package me.oskar3123.staffchat.velocity.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import me.oskar3123.staffchat.velocity.event.VelocityStaffChatEvent.StaffChatResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired after deciding to broadcast a staff message.
 *
 * <p>Denying this event will still block the chat message.
 */
public class VelocityStaffChatEvent implements ResultedEvent<StaffChatResult> {

  private final Player player;
  private final String format;
  private final String message;

  private StaffChatResult result = StaffChatResult.allowed();

  @Contract(pure = true)
  public VelocityStaffChatEvent(
      @NotNull Player player, @NotNull String format, @NotNull String message) {
    this.player = Objects.requireNonNull(player, "player must not be null");
    this.format = Objects.requireNonNull(format, "format must not be null");
    this.message = Objects.requireNonNull(message, "message must not be null");
  }

  /**
   * Gets the player that is sending the staff chat message.
   *
   * @return the player
   */
  @NotNull
  @Contract(pure = true)
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the initial format decided by the plugin.
   *
   * <p>Use {@link #getResult()} to get the potentially modified format.
   *
   * @return the initial format
   */
  @NotNull
  @Contract(pure = true)
  public String getFormat() {
    return format;
  }

  /**
   * Gets the initial message entered by the player.
   *
   * <p>Use {@link #getResult()} to get the potentially modified message.
   *
   * @return the initial message
   */
  @NotNull
  @Contract(pure = true)
  public String getMessage() {
    return message;
  }

  /**
   * Gets the current result.
   *
   * <p>This will by default be an allowed result with no modifications.
   *
   * @return the current result
   */
  @NotNull
  @Contract(pure = true)
  @Override
  public StaffChatResult getResult() {
    return result;
  }

  /**
   * Sets the current result.
   *
   * <p>You can {@linkplain StaffChatResult#allowed() allow}, {@linkplain StaffChatResult#denied()
   * deny}, {@linkplain StaffChatResult#format(String) modify the format}, {@linkplain
   * StaffChatResult#message(String) modify the message}, or {@linkplain
   * StaffChatResult#formatAndMessage(String, String) modify both the format and message}.
   *
   * @param result the new result
   */
  @Override
  public void setResult(@NotNull StaffChatResult result) {
    this.result = Objects.requireNonNull(result, "result must not be null");
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    VelocityStaffChatEvent that = (VelocityStaffChatEvent) o;
    return Objects.equals(player, that.player)
        && Objects.equals(format, that.format)
        && Objects.equals(message, that.message)
        && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, format, message, result);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", VelocityStaffChatEvent.class.getSimpleName() + "[", "]")
        .add("player=" + player)
        .add("format='" + format + "'")
        .add("message='" + message + "'")
        .add("result=" + result)
        .toString();
  }

  /** Result of the {@linkplain VelocityStaffChatEvent}. */
  public static class StaffChatResult implements Result {

    private static final StaffChatResult ALLOWED = new StaffChatResult(true, null, null);
    private static final StaffChatResult DENIED = new StaffChatResult(false, null, null);

    private final boolean allowed;
    private final String format;
    private final String message;

    private StaffChatResult(boolean allowed, String format, String message) {
      this.allowed = allowed;
      this.format = format;
      this.message = message;
    }

    /**
     * Allows the message to be sent, without modifying anything.
     *
     * @return the allowed result
     */
    @NotNull
    @Contract(pure = true)
    public static StaffChatResult allowed() {
      return ALLOWED;
    }

    /**
     * Prevents the message from being sent.
     *
     * @return the denied result
     */
    @NotNull
    @Contract(pure = true)
    public static StaffChatResult denied() {
      return DENIED;
    }

    /**
     * Allows the message to be sent, modifying the format.
     *
     * @param format the format
     * @return the modify format result
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StaffChatResult format(@NotNull String format) {
      return new StaffChatResult(
          true, Objects.requireNonNull(format, "format must not be null"), null);
    }

    /**
     * Allows the message to be sent, modifying the message.
     *
     * @param message the message
     * @return the modify message result
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StaffChatResult message(@NotNull String message) {
      return new StaffChatResult(
          true, null, Objects.requireNonNull(message, "message must not be null"));
    }

    /**
     * Allows the message to be sent, modifying the format and message.
     *
     * @param format the format
     * @param message the message
     * @return the modify format and message result
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static StaffChatResult formatAndMessage(
        @NotNull String format, @NotNull String message) {
      return new StaffChatResult(
          true,
          Objects.requireNonNull(format, "format must not be null"),
          Objects.requireNonNull(message, "message must not be null"));
    }

    @Contract(pure = true)
    @Override
    public boolean isAllowed() {
      return allowed;
    }

    /**
     * Gets the potentially modified format.
     *
     * <p>An empty optional means that the format has not been modified.
     *
     * @return the potentially modified format
     */
    @NotNull
    @Contract(pure = true)
    public Optional<String> getFormat() {
      return Optional.ofNullable(format);
    }

    /**
     * Gets the potentially modified message.
     *
     * <p>An empty optional means that the message has not been modified.
     *
     * @return the potentially modified message
     */
    @NotNull
    @Contract(pure = true)
    public Optional<String> getMessage() {
      return Optional.ofNullable(message);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      StaffChatResult that = (StaffChatResult) o;
      return allowed == that.allowed
          && Objects.equals(format, that.format)
          && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(allowed, format, message);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", StaffChatResult.class.getSimpleName() + "[", "]")
          .add("allowed=" + allowed)
          .add("format='" + format + "'")
          .add("message='" + message + "'")
          .toString();
    }
  }
}
