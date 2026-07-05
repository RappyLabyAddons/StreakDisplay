package com.rappytv.streakdisplay.api;

import java.util.UUID;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface StreakCacheController {

  void resolve(UUID uuid);

  boolean has(UUID uuid);

  StreakData get(UUID uuid);

  void remove(UUID uuid);

  void clear();

}
