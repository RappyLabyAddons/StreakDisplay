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

  private final Map<UUID, Integer> cache = new HashMap<>();
  private final Set<UUID> resolving = new HashSet<>();

  @Override
  public void resolve(UUID uuid) {
    if (this.resolving.contains(uuid)) {
      return;
    }
    this.resolving.add(uuid);
    Request.ofGson(JsonElement.class)
        .url("https://streaks.rappytv.com/streaks/" + uuid.toString())
        .handleErrorStream()
        .async()
        .execute(response -> {
          if (response.hasException() || response.getStatusCode() != 200) {
            this.cache.put(uuid, null);
            this.resolving.remove(uuid);
            return;
          }
          JsonObject body = response.get().getAsJsonObject();
          if (!body.has("streak") || !body.get("streak").isJsonPrimitive()) {
            this.cache.put(uuid, null);
            this.resolving.remove(uuid);
            return;
          }
          this.cache.put(uuid, body.get("streak").getAsInt());
          this.resolving.remove(uuid);
        });
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
