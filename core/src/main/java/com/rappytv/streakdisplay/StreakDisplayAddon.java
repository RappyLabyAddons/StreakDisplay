package com.rappytv.streakdisplay;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class StreakDisplayAddon extends LabyAddon<StreakDisplayConfig> {

  @Override
  protected void enable() {
    this.registerSettingCategory();
  }

  @Override
  protected Class<? extends StreakDisplayConfig> configurationClass() {
    return StreakDisplayConfig.class;
  }
}
