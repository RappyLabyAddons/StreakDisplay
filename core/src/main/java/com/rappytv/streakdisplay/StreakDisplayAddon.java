package com.rappytv.streakdisplay;

import com.rappytv.streakdisplay.api.generated.ReferenceStorage;
import com.rappytv.streakdisplay.nametag.StreakNameTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class StreakDisplayAddon extends LabyAddon<StreakDisplayConfig> {

  private static StreakDisplayAddon INSTANCE;

  @Override
  protected void enable() {
    INSTANCE = this;

    this.registerSettingCategory();

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

  public static ReferenceStorage references() {
    return INSTANCE.referenceStorageAccessor();
  }
}
