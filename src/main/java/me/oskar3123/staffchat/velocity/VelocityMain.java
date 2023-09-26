package me.oskar3123.staffchat.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;
import me.oskar3123.staffchat.velocity.command.VelocityStaffChatCommand;
import me.oskar3123.staffchat.velocity.config.Configuration;
import me.oskar3123.staffchat.velocity.config.StaticConfiguration;
import me.oskar3123.staffchat.velocity.listener.VelocityChatListener;
import org.slf4j.Logger;

@Plugin(
    id = "staffchat",
    name = "StaffChat",
    version = VelocityMain.VERSION,
    authors = {"oskar3123"})
public class VelocityMain {

  public static final String VERSION = "SNAPSHOT";

  private final ProxyServer server;
  private final Logger logger;
  private final Path dataDirectory;

  @Inject
  public VelocityMain(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
    this.server = server;
    this.logger = logger;
    this.dataDirectory = dataDirectory;
  }

  public ProxyServer getServer() {
    return server;
  }

  public Logger getLogger() {
    return logger;
  }

  public Configuration getConfig() {
    return StaticConfiguration.getInstance();
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    VelocityChatListener chatListener = new VelocityChatListener(this);
    server.getEventManager().register(this, PlayerChatEvent.class, chatListener);
    server
        .getCommandManager()
        .register("staffchat", new VelocityStaffChatCommand(this, chatListener));
  }

  public void reloadConfig() {
    // TODO implement
  }
}
