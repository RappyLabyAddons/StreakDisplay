package com.rappytv.streakdisplay.core;

import com.rappytv.streakdisplay.api.StreakCacheController;
import com.rappytv.streakdisplay.api.generated.ReferenceStorage;
import com.rappytv.streakdisplay.core.listener.PlayerInfoListener;
import com.rappytv.streakdisplay.core.ui.nametag.StreakNameTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class StreakDisplayAddon extends LabyAddon<StreakDisplayConfig> {

  private static StreakDisplayAddon instance;

  @Override
  protected void enable() {
    instance = this;

    this.registerSettingCategory();
    this.registerListener(new PlayerInfoListener());
    this.labyAPI().tagRegistry().register(
        "streakdisplay",
        PositionType.BELOW_NAME,
        new StreakNameTag(this)
    );
  }

  @Override
  protected Class<? extends StreakDisplayConfig> configurationClass() {
    return StreakDisplayConfig.class;
  }

  public static StreakCacheController cacheController() {
    return ((ReferenceStorage) instance.referenceStorageAccessor()).streakCacheController();
  }
}
