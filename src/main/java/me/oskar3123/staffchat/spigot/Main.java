package me.oskar3123.staffchat.spigot;

import java.util.Optional;
import me.clip.placeholderapi.PlaceholderAPI;
import me.oskar3123.staffchat.spigot.command.StaffChatCommand;
import me.oskar3123.staffchat.spigot.listener.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

  public final String usePerm = "staffchat.use";
  public final String seePerm = "staffchat.see";
  public final String commandPerm = "staffchat.command";
  public final String reloadPerm = "staffchat.reload";
  public final ChatListener chatListener = new ChatListener(this);

  public void onEnable() {
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", chatListener);
    new MetricsLite(this);
    saveDefaultConfig();
    registerCommands();
    registerEvents();
  }

  @Override
  public void saveDefaultConfig() {
    super.saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

  private void registerCommands() {
    Optional.ofNullable(getCommand("staffchat"))
        .ifPresent(command -> command.setExecutor(new StaffChatCommand(this)));
  }

  private void registerEvents() {
    Bukkit.getPluginManager().registerEvents(this.chatListener, this);
  }

  private boolean isPlaceholderApiEnabled() {
    return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
  }

  public String replacePlaceholders(Player player, String string) {
    if (!isPlaceholderApiEnabled()) {
      return string;
    }
    return PlaceholderAPI.setPlaceholders(player, string);
  }
}
