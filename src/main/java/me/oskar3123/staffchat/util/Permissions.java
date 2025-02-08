package me.oskar3123.staffchat.util;

import org.jetbrains.annotations.NotNull;

public enum Permissions {
  USE("staffchat.use"),
  SEE("staffchat.see"),
  COMMAND("staffchat.command"),
  RELOAD("staffchat.reload");

  private final String permission;

  Permissions(@NotNull String permission) {
    this.permission = permission;
  }

  @NotNull
  public String permission() {
    return permission;
  }
}
