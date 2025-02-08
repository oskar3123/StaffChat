package me.oskar3123.staffchat.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import me.oskar3123.staffchat.util.Permissions;
import me.oskar3123.staffchat.velocity.VelocityStaffChat;

public class VelocityStaffChatListener {

  private final VelocityStaffChat plugin;

  public VelocityStaffChatListener(VelocityStaffChat plugin) {
    this.plugin = plugin;
  }

  @Subscribe
  public void onPlayerChat(PlayerChatEvent event) {
    Player player = event.getPlayer();

    if (!player.hasPermission(Permissions.USE.permission())) {
      return;
    }

    String character = plugin.getConfig().node("settings", "character").getString("@");
    String message = event.getMessage();
    boolean toggled = plugin.getStaffChatHandler().isToggled(player.getUniqueId());

    // Check if player has staff chat toggled on or message starts with @
    if (toggled || message.startsWith(character)) {
      String staffMessage = toggled ? message : message.substring(character.length()).trim();
      plugin.getStaffChatHandler().broadcastStaffMessage(player, staffMessage);
      event.setResult(PlayerChatEvent.ChatResult.denied());
    }
  }
}
