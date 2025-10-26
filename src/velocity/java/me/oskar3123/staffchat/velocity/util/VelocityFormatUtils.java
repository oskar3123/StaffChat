package me.oskar3123.staffchat.velocity.util;

import java.util.function.Function;
import java.util.function.Supplier;
import me.oskar3123.staffchat.util.FormatUtils;
import me.oskar3123.staffchat.util.StringUtils;

public final class VelocityFormatUtils {

  private VelocityFormatUtils() {}

  public static String replacePlaceholders(
      String string,
      Function<String, String> colorize,
      Supplier<String> serverSupplier,
      Supplier<String> nameSupplier,
      String message) {
    string =
        string.replace(
            "{SERVER}", StringUtils.sanitize(colorize.apply(serverSupplier.get())));
    return FormatUtils.replacePlaceholders(string, colorize, nameSupplier, message);
  }
}
