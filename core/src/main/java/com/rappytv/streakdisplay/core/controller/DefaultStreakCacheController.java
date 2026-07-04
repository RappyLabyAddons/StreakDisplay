package com.rappytv.streakdisplay.core.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rappytv.streakdisplay.api.StreakCacheController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.TokenStorage.Purpose;
import net.labymod.api.labyconnect.TokenStorage.Token;
import net.labymod.api.models.Implements;
import net.labymod.api.util.Debounce;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.Nullable;

@Singleton
@Implements(StreakCacheController.class)
public class DefaultStreakCacheController implements StreakCacheController {

  private static final String BULK_STREAK_ENDPOINT = "https://laby.net/api/v3/bulk/streak";
  private static final String DEBOUNCE_KEY = "streakdisplay-request-streaks";
  private static final int MAX_BATCH_SIZE = 100;
  private static final Logging LOGGER = Logging.getLogger();

  private final Map<UUID, Integer> cache = new ConcurrentHashMap<>();
  private final Set<UUID> pendingQueue = ConcurrentHashMap.newKeySet();

  @Override
  public void resolve(UUID uuid) {
    if (this.cache.containsKey(uuid) || !this.pendingQueue.add(uuid)) {
      return;
    }
    Debounce.of(DEBOUNCE_KEY, 5000, this::flush);
  }

  private void flush() {
    if (this.pendingQueue.isEmpty()) {
      return;
    }

    List<UUID> snapshot = new ArrayList<>(this.pendingQueue);
    snapshot.forEach(this.pendingQueue::remove);

    for (int start = 0; start < snapshot.size(); start += MAX_BATCH_SIZE) {
      int end = Math.min(start + MAX_BATCH_SIZE, snapshot.size());
      this.resolveBulk(new ArrayList<>(snapshot.subList(start, end)));
    }
  }

  private void resolveBulk(List<UUID> uuids) {
    String token = this.getAuth();
    if (token == null) {
      this.pendingQueue.addAll(uuids);
      return;
    }

    JsonArray body = new JsonArray();
    for (UUID uuid : uuids) {
      body.add(uuid.toString());
    }

    Request.ofGson(JsonElement.class)
        .url(BULK_STREAK_ENDPOINT)
        .method(Method.POST)
        .authorization("Bearer", token)
        .json(body)
        .handleErrorStream()
        .async()
        .execute(response -> {
          if (response.hasException()) {
            LOGGER.error("Failed to bulk-resolve streaks", response.exception());
            Task.builder(() -> this.resolveBulk(uuids))
                .delay(10, TimeUnit.SECONDS)
                .build()
                .execute();
            return;
          }

          if (response.getStatusCode() != 200) {
            String error = "Unknown status code " + response.getStatusCode();
            if (response.getStatusCode() == 429) {
              error = "Ratelimited";
              Task.builder(() -> this.resolveBulk(uuids))
                  .delay(10, TimeUnit.SECONDS)
                  .build()
                  .execute();
            }
            LOGGER.error("Failed to bulk-resolve streaks: " + error);
            return;
          }

          JsonElement result = response.get();
          if (!result.isJsonObject()) {
            LOGGER.error("Failed to bulk-resolve streaks: Unexpected response body");
            return;
          }

          JsonObject object = result.getAsJsonObject();
          int cached = 0;
          for (UUID uuid : uuids) {
            JsonObject entry = object.getAsJsonObject(uuid.toString());
            if (entry == null || !entry.has("current") || !entry.get("current").isJsonPrimitive()) {
              this.cacheFailure(uuid);
              continue;
            }
            this.cacheStreak(uuid, entry.get("current").getAsInt());
            cached++;
          }
          LOGGER.debug("Cached " + cached + " streaks.");
        });
  }

  @Nullable
  private String getAuth() {
    LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return null;
    }

    Token token = session.tokenStorage().getToken(Purpose.JWT, session.self().getUniqueId());
    if (token == null || token.isExpired()) {
      return null;
    }

    return token.getToken();
  }

  private void cacheStreak(UUID uuid, Integer value) {
    this.cache.put(uuid, value);
  }

  private void cacheFailure(UUID uuid) {
    this.cache.put(uuid, -1);
  }

  @Override
  public boolean has(UUID uuid) {
    return this.cache.containsKey(uuid);
  }

  @Override
  public Integer get(UUID uuid) {
    Integer value = this.cache.get(uuid);
    return value == null || value == -1 ? null : value;
  }

  @Override
  public void remove(UUID uuid) {
    this.cache.remove(uuid);
  }

  @Override
  public void clear() {
    this.cache.clear();
  }
}