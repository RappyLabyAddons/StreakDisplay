package com.rappytv.streakdisplay.core.ui.snapshot;

import com.rappytv.streakdisplay.core.StreakDisplayAddon;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class StreakUserSnapshotFactory extends LabySnapshotFactory<Player, StreakUserSnapshot> {

  private final StreakDisplayAddon addon;

  public StreakUserSnapshotFactory(StreakDisplayAddon addon) {
    super(StreakDisplayExtraKeys.STREAK_USER);
    this.addon = addon;
  }

  @Override
  public StreakUserSnapshot create(Player player, Extras extras) {
    return new StreakUserSnapshot(player, extras, this.addon);
  }
}
