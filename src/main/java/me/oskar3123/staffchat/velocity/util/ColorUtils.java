package me.oskar3123.staffchat.velocity.util;

import java.util.regex.Pattern;
import org.bukkit.ChatColor;

public class ColorUtils {

  @SuppressWarnings("UnnecessaryUnicodeEscape")
  private static final char COLOR_CHAR = '\u00a7';

  private static final Pattern STRIP_COLOR_PATTERN =
      Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-ORX]");

  private ColorUtils() {}

  public static String translateAlternateColorCodes(char colorCodeChar, String string) {
    char[] b = string.toCharArray();
    for (int i = 0; i < b.length - 1; i++) {
      if (b[i] == colorCodeChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
        b[i] = ChatColor.COLOR_CHAR;
        b[i + 1] = Character.toLowerCase(b[i + 1]);
      }
    }
    return new String(b);
  }

  public static String stripColor(String string) {
    return STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
  }
}
