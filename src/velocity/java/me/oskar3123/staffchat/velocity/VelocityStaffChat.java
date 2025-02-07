package me.oskar3123.staffchat.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import me.oskar3123.staffchat.velocity.command.VelocityStaffChatCommand;
import me.oskar3123.staffchat.velocity.handler.VelocityStaffChatHandler;
import me.oskar3123.staffchat.velocity.listener.VelocityStaffChatListener;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@Plugin(
    id = "staffchat",
    name = "StaffChat",
    version = VelocityStaffChat.PLUGIN_VERSION,
    description = "A staff chat plugin",
    authors = {"oskar3123"})
public class VelocityStaffChat {

  public static final String PLUGIN_VERSION = "SNAPSHOT";

  private static final int BSTATS_PLUGIN_ID = 24679;

  private final ProxyServer server;
  private final Logger logger;
  private final Path dataDirectory;
  private final Metrics.Factory metricsFactory;
  private VelocityStaffChatHandler staffChatHandler;
  private CommentedConfigurationNode config;

  @Inject
  public VelocityStaffChat(
      ProxyServer server,
      Logger logger,
      @DataDirectory Path dataDirectory,
      Metrics.Factory metricsFactory) {
    this.server = server;
    this.logger = logger;
    this.dataDirectory = dataDirectory;
    this.metricsFactory = metricsFactory;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    try {
      config = loadConfig();

      // Initialize metrics
      metricsFactory.make(this, BSTATS_PLUGIN_ID);

      // Initialize handler
      staffChatHandler = new VelocityStaffChatHandler(this);

      // Register commands
      server
          .getCommandManager()
          .register(
              server.getCommandManager().metaBuilder("staffchat").build(),
              new VelocityStaffChatCommand(this));

      // Register listeners
      server.getEventManager().register(this, new VelocityStaffChatListener(this));

      logger.info("StaffChat has been enabled!");
    } catch (IOException e) {
      logger.error("Failed to start StaffChat!", e);
    }
  }

  @Subscribe
  public void onProxyShutdown(ProxyShutdownEvent event) {
    logger.info("StaffChat has been disabled!");
  }

  public VelocityStaffChatHandler getStaffChatHandler() {
    return staffChatHandler;
  }

  public ProxyServer getServer() {
    return server;
  }

  public void reloadConfig() throws IOException {
    config = loadConfig();
  }

  public CommentedConfigurationNode getConfig() {
    return config;
  }

  private CommentedConfigurationNode loadConfig() throws IOException {
    if (Files.notExists(dataDirectory)) {
      Files.createDirectory(dataDirectory);
    }
    Path config = dataDirectory.resolve("config.yml");
    if (Files.notExists(config)) {
      try (InputStream stream = getClass().getClassLoader().getResourceAsStream("config.yml")) {
        if (stream == null) {
          throw new IOException("no default config.yml");
        }
        Files.copy(stream, config);
      }
    }
    YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(config).build();
    return loader.load();
  }
}
