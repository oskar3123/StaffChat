package me.oskar3123.staffchat.util;

public class StringUtils {

  private StringUtils() {}

  public static String sanitize(String string) {
    string = string.replaceAll("\\\\", "\\\\\\\\");
    string = string.replaceAll("\\$", "\\\\\\$");
    return string;
  }
}
