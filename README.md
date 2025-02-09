[![Downloads](https://img.shields.io/github/downloads/oskar3123/StaffChat/total.svg?style=flat)](https://github.com/oskar3123/StaffChat/releases/latest)

[![Latest Release](https://img.shields.io/github/v/release/oskar3123/StaffChat.svg?style=flat)](https://github.com/oskar3123/StaffChat/releases/latest)
[![Github commits (since latest release)](https://img.shields.io/github/commits-since/oskar3123/StaffChat/latest.svg?style=flat)](https://github.com/oskar3123/StaffChat/commits/master)
[![Latest Pre-release](https://img.shields.io/github/v/release/oskar3123/StaffChat.svg?style=flat&include_prereleases)](https://github.com/oskar3123/StaffChat/releases)
[![Github commits (since latest pre-release)](https://img.shields.io/github/commits-since/oskar3123/StaffChat/latest.svg?style=flat&include_prereleases)](https://github.com/oskar3123/StaffChat/commits/master)

[![Open Issues](https://img.shields.io/github/issues/oskar3123/StaffChat)](https://github.com/oskar3123/StaffChat/issues?q=is%3Aopen+is%3Aissue)
[![Closed Issues](https://img.shields.io/github/issues-closed/oskar3123/StaffChat)](https://github.com/oskar3123/StaffChat/issues?q=is%3Aissue+is%3Aclosed)
[![Open Pull requests](https://img.shields.io/github/issues-pr/oskar3123/StaffChat)](https://github.com/oskar3123/StaffChat/pulls?q=is%3Aopen+is%3Apr)
[![Closed Pull requests](https://img.shields.io/github/issues-pr-closed/oskar3123/StaffChat)](https://github.com/oskar3123/StaffChat/pulls?q=is%3Apr+is%3Aclosed)

# [StaffChat](https://oskar3123.github.io/StaffChat)

Simple and highly configurable staff chat.

## Download

You can download the plugin from
the [Spigot resource page](https://www.spigotmc.org/resources/37804/) or via
the [GitHub releases](https://github.com/oskar3123/StaffChat/releases).

## License

This plugin is licensed with the MIT License, for more information see
the [LICENSE file](https://github.com/oskar3123/StaffChat/blob/master/LICENSE).

## Building

To build this yourself just clone the repository and run the `shadowJar` task with the Gradle
Wrapper.

The plugin jar-file can be found inside the `build/libs` folder after building.

### Windows

```bat
git clone git@github.com:oskar3123/StaffChat.git
cd StaffChat
gradlew.bat shadowJar
```

### *nix (Linux, FreeBSD, macOS, ...)

```bash
git clone git@github.com:oskar3123/StaffChat.git
cd StaffChat
./gradlew shadowJar
```

## Event API (For developers)

### Bukkit/Spigot

Because the event API in 1.14+ is now strict between sync and async events you should check whether
this was called synchronously or asynchronously by using `event.isAsynchronous()`.

```java
public class StaffChatListener implements Listener {

  @EventHandler
  public void onStaffChat(StaffChatEvent event) {
    // Player player = event.getPlayer();
    // String format = event.getFormat();
    // String message = event.getMessage();
    // event.setFormat("&b{NAME} >> {MESSAGE}");
    // event.setMessage(filterMessage(message));
    // event.setCancelled(true);
  }

  private static String filterMessage(String message) {
    // ...
    return message;
  }
}
```

Register the listener in the `onEnable` method of your plugin.

```java
public class YourPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new StaffChatListener(), this);
  }
}
```

### BungeeCord

```java
public class StaffChatListener implements Listener {

  @EventHandler
  public void onStaffChat(BungeeStaffChatEvent event) {
    // ProxiedPlayer player = event.getPlayer();
    // String format = event.getFormat();
    // String message = event.getMessage();
    // event.setFormat("&b{NAME} >> {MESSAGE}");
    // event.setMessage(filterMessage(message));
    // event.setCancelled(true);
  }

  private static String filterMessage(String message) {
    // ...
    return message;
  }
}
```

Register the listener in the `onEnable` method of your plugin.

```java
public class YourPlugin extends Plugin {

  @Override
  public void onEnable() {
    getProxy().getPluginManager().registerListener(this, new StaffChatListener());
  }
}
```

### Velocity

```java
public class StaffChatListener {

  @Subscribe
  public void onStaffChat(VelocityStaffChatEvent event) {
    // Player player = event.getPlayer();
    // String format = event.getFormat();
    // String message = event.getMessage();
    // StaffChatResult result = StaffChatResult.denied();
    // StaffChatResult result = StaffChatResult.format("&b{NAME} >> {MESSAGE}");
    // StaffChatResult result = StaffChatResult.message(filterMessage(message));
    // StaffChatResult result = StaffChatResult.formatAndMessage("&b{NAME} >> {MESSAGE}", filterMessage(message));
    // event.setResult(result);
  }

  private static String filterMessage(String message) {
    // ...
    return message;
  }
}
```

Register the listener on the `ProxyInitializeEvent` in your plugin.

```java
@Plugin
public class YourPlugin {

  private final ProxyServer server;

  @Inject
  public YourPlugin(ProxyServer server) {
    this.server = server;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    server.getEventManager().register(this, new StaffChatListener());
  }
}
```
