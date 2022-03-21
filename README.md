[![Travis-CI](https://img.shields.io/travis/com/oskar3123/StaffChat.svg?style=flat)](https://app.travis-ci.com/github/oskar3123/StaffChat)
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

Simple and highly configurable staffchat

## Download
You can download the plugin from the [Spigot resource page](https://www.spigotmc.org/resources/37804/) or via the [GitHub releases](https://github.com/oskar3123/StaffChat/releases)

## Event API (For developers)

### Bukkit/Spigot

Because the event API in 1.14+ is now strict between sync and async events you should check whether this was called synchronously or asynchronously by using `event.isAsynchronous()`

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
