package me.oskar3123.staffchat.spigot;

import github.scarsz.discordsrv.DiscordSRV;
import java.util.Optional;
import me.clip.placeholderapi.PlaceholderAPI;
import me.oskar3123.staffchat.spigot.command.StaffChatCommand;
import me.oskar3123.staffchat.spigot.handler.StaffChatHandler;
import me.oskar3123.staffchat.spigot.listener.ChatListener;
import me.oskar3123.staffchat.spigot.listener.DiscordSrvListener;
import me.oskar3123.staffchat.spigot.listener.StaffChatPml;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Main extends JavaPlugin {

  private static final int BSTATS_PLUGIN_ID = 835;

  public final StaffChatHandler staffChatHandler = new StaffChatHandler(this);
  public final ChatListener chatListener = new ChatListener(staffChatHandler);
  public final StaffChatPml staffChatPml = new StaffChatPml(staffChatHandler);

  public void onEnable() {
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", staffChatPml);
    new Metrics(this, BSTATS_PLUGIN_ID);
    saveDefaultConfig();
    registerCommands();
    registerEvents();
    if (Bukkit.getPluginManager().isPluginEnabled("DiscordSRV")) {
      DiscordSRV.api.subscribe(new DiscordSrvListener(this));
    }
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

  public @NotNull String replacePlaceholders(Player player, String string) {
    if (!isPlaceholderApiEnabled()) {
      return string;
    }
    return PlaceholderAPI.setPlaceholders(player, string);
  }
}
