package com.rappytv.streakdisplay.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rappytv.streakdisplay.api.StreakApiController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.labymod.api.util.io.web.request.Request;

@Singleton
@Implements(StreakApiController.class)
public class DefaultStreakApiController implements StreakApiController {

  private static final String STREAK_ENDPOINT = "https://streaks.rappytv.com/streaks/%s";
  private final Map<UUID, Integer> cache = new HashMap<>();
  private final Set<UUID> resolving = new HashSet<>();

  @Override
  public void resolve(UUID uuid) {
    if (this.resolving.contains(uuid)) {
      return;
    }
    this.resolving.add(uuid);
    Request.ofGson(JsonElement.class)
        .url(String.format(STREAK_ENDPOINT, uuid))
        .handleErrorStream()
        .async()
        .execute(response -> {
          if (response.hasException() || response.getStatusCode() != 200) {
            this.cacheFailure(uuid);
            return;
          }
          JsonObject body = response.get().getAsJsonObject();
          if (!body.has("streak") || !body.get("streak").isJsonPrimitive()) {
            this.cacheFailure(uuid);
            return;
          }
          this.cacheStreak(uuid, body.get("streak").getAsInt());
        });
  }

  private void cacheFailure(UUID uuid) {
    this.cacheStreak(uuid, null);
  }

  private void cacheStreak(UUID uuid, Integer value) {
    this.cache.put(uuid, value);
    this.resolving.remove(uuid);
  }

  @Override
  public boolean has(UUID uuid) {
    return this.cache.containsKey(uuid);
  }

  @Override
  public Integer get(UUID uuid) {
    return this.cache.get(uuid);
  }

  @Override
  public void clear() {
    this.cache.clear();
  }
}
