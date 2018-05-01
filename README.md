[![Travis-CI](https://img.shields.io/travis/oskar3123/StaffChat.svg?style=flat)](https://travis-ci.org/oskar3123/StaffChat)
[![All Release](https://img.shields.io/github/downloads/oskar3123/StaffChat/total.svg?style=flat)](https://github.com/oskar3123/StaffChat/releases/latest)
[![Latest Release](https://img.shields.io/github/release/oskar3123/StaffChat.svg?style=flat)](https://github.com/oskar3123/StaffChat/releases/latest)
[![Github commits (since latest release)](https://img.shields.io/github/commits-since/oskar3123/StaffChat/latest.svg?style=flat)](https://github.com/oskar3123/StaffChat/commits/master)

# [StaffChat](https://oskar3123.github.io/StaffChat)

Simple and highly configurable staffchat

## Download
You can download the plugin from the [Spigot resource page](https://www.spigotmc.org/resources/37804/) or via the [GitHub releases](https://github.com/oskar3123/StaffChat/releases)

## Event API (For developers)

### Bukkit/Spigot

```java
public class StaffChatListener implements Listener
{

    @EventHandler
    public void onStaffChat(StaffChatEvent event)
    {
        // String format = event.getFormat();
        // event.setFormat("&b{NAME} >> {MESSAGE}");
        // String message = event.getMessage();
        // Player player = event.getPlayer();
        // event.setCancelled(true);
    }

}
```
Register the listener with
```java
getServer().getPluginManager().registerEvents(new StaffChatListener(), this);
```
in your plugin onEnable.

### BungeeCord

```java
public class StaffChatListener implements Listener
{

    @EventHandler
    public void onStaffChat(BungeeStaffChatEvent event)
    {
        // String format = event.getFormat();
        // event.setFormat("&b{NAME} >> {MESSAGE}");
        // String message = event.getMessage();
        // Player player = event.getPlayer();
        // event.setCancelled(true);
    }

}
```
Register the listener with
```java
getProxy().getPluginManager().registerListener(this, new StaffChatListener());
```
in your plugin onEnable.
