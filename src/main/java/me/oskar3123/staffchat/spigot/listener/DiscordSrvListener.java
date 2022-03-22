package me.oskar3123.staffchat.spigot.listener;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import java.util.Optional;
import me.oskar3123.staffchat.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

public class DiscordSrvListener {

  private final Main plugin;
  private final DiscordSRV discordSrv;

  public DiscordSrvListener(Main plugin) {
    this.plugin = plugin;
    this.discordSrv = DiscordSRV.getPlugin();
  }

  @Subscribe(priority = ListenerPriority.MONITOR)
  public void onDiscordGuildMessageReceivedEvent(DiscordGuildMessageReceivedEvent event) {
    Configuration config = plugin.getConfig();
    if (!isDiscordSrvEnabled(config)) {
      return;
    }
    String channel = config.getString("discordsrv.channel", "staffchat");
    Optional.ofNullable(discordSrv.getDestinationGameChannelNameForTextChannel(event.getChannel()))
        .filter(gameChannel -> gameChannel.equals(channel))
        .ifPresent(
            gameChannel -> {
              String name =
                  Optional.ofNullable(event.getAuthor()).map(User::getName).orElse("Discord User");
              String message =
                  Optional.ofNullable(event.getMessage())
                      .map(Message::getContentDisplay)
                      .orElse("");
              Bukkit.getScheduler()
                  .runTask(
                      plugin,
                      () ->
                          plugin.staffChatHandler.sendStaffChatMessage(
                              plugin
                                  .getConfig()
                                  .getString("discordsrv.discord-to-minecraft-format", ""),
                              name,
                              message));
            });
  }

  private boolean isDiscordSrvEnabled(Configuration config) {
    return config.getBoolean("discordsrv.enable", false);
  }
}
