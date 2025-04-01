package com.rappytv.streakdisplay.api;

import java.util.UUID;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface StreakApiController {

  void resolve(UUID uuid);

  boolean has(UUID uuid);

  Integer get(UUID uuid);

  void clear();

}
