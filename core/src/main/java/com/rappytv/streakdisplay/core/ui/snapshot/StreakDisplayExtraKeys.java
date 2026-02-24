package com.rappytv.streakdisplay.core.ui.snapshot;

import net.labymod.api.laby3d.renderer.snapshot.ExtraKey;

public class StreakDisplayExtraKeys {

  public static final ExtraKey<StreakUserSnapshot> STREAK_USER = ExtraKey.of(
      "streak_user",
      StreakUserSnapshot.class
  );

}
