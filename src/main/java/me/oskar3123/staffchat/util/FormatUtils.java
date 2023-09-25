package me.oskar3123.staffchat.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class FormatUtils {

  private FormatUtils() {}

  public static String replacePlaceholders(
      String string,
      Function<String, String> colorize,
      Supplier<String> nameSupplier,
      String message) {
    string = colorize.apply(string);
    string =
        string.replaceAll("\\{NAME}", StringUtils.sanitize(colorize.apply(nameSupplier.get())));
    string = string.replaceAll("\\{MESSAGE}", StringUtils.sanitize(message));
    return string;
  }
}
