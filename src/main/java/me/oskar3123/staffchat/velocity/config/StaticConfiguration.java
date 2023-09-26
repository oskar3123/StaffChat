package me.oskar3123.staffchat.velocity.config;

public class StaticConfiguration implements Configuration {

  private static final String PREFIX = "&7StaffChat&8> &7";
  private static final String NO_PERM = "&cYou don't have permission to do that";
  private static final String PLAYER_ONLY = "&cPlayer only command";
  private static final String RELOADED = "Reloaded the config file";
  private static final String TOGGLED_FORMAT = "You toggled auto staffchat %s";
  private static final String ON_STRING = "on";
  private static final String OFF_STRING = "off";
  private static final String CHARACTER = "@";
  private static final String FORMAT = "&b[{SERVER}] {NAME}: {MESSAGE}";

  private StaticConfiguration() {}

  public static StaticConfiguration getInstance() {
    return InstanceHolder.INSTANCE;
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }

  @Override
  public String getNoPerm() {
    return NO_PERM;
  }

  @Override
  public String getPlayerOnly() {
    return PLAYER_ONLY;
  }

  @Override
  public String getReloaded() {
    return RELOADED;
  }

  @Override
  public String getToggledFormat() {
    return TOGGLED_FORMAT;
  }

  @Override
  public String getOnString() {
    return ON_STRING;
  }

  @Override
  public String getOffString() {
    return OFF_STRING;
  }

  @Override
  public String getCharacter() {
    return CHARACTER;
  }

  @Override
  public String getFormat() {
    return FORMAT;
  }

  private static class InstanceHolder {
    private static final StaticConfiguration INSTANCE = new StaticConfiguration();
  }
}
