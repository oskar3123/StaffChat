package me.oskar3123.staffchat.common.permission;

public enum Permission {
  USE("staffchat.use"),
  SEE("staffchat.see"),
  COMMAND("staffchat.command"),
  RELOAD("staffchat.reload");

  private final String permission;

  Permission(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }
}
