package com.rappytv.streakdisplay.api;

import java.util.UUID;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.NotNull;

@Referenceable
public interface StreakCacheController {

  void resolve(@NotNull UUID uuid);

  @NotNull StreakData get(UUID uuid);

  void remove(@NotNull UUID uuid);

  void clear();

}
