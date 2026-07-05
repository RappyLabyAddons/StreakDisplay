package com.rappytv.streakdisplay.core.listener;

import com.rappytv.streakdisplay.api.StreakCacheController;
import com.rappytv.streakdisplay.core.StreakDisplayAddon;
import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoAddEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class PlayerInfoListener {

  private final StreakCacheController controller;

  public PlayerInfoListener() {
    this.controller = StreakDisplayAddon.cacheController();
  }

  @Subscribe
  public void onPlayerInfoAdd(PlayerInfoAddEvent event) {
    this.controller.resolve(event.playerInfo().profile().getUniqueId());
  }

  @Subscribe
  public void onPlayerInfoRemove(PlayerInfoRemoveEvent event) {
    this.controller.remove(event.playerInfo().profile().getUniqueId());
  }

  @Subscribe
  public void onClientDisconnect(ServerDisconnectEvent event) {
    this.controller.clear();
  }

}
