package com.rappytv.streakdisplay;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;

@SpriteTexture("settings.png")
public class StreakDisplayConfig extends AddonConfig {

  @SpriteSlot
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("tag")
  @SpriteSlot(x = 1)
  @SliderSetting(min = 5, max = 10)
  private final ConfigProperty<Integer> size = new ConfigProperty<>(7);

  @SpriteSlot(x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> showBackground = new ConfigProperty<>(true);

  @SettingSection("streaks")
  @SpriteSlot(x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideZeroStreaks = new ConfigProperty<>(false);

  @SpriteSlot(x = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideHiddenStreaks = new ConfigProperty<>(false);

  @SettingSection("cache")
  @SpriteSlot(x = 5)
  @MethodOrder(after = "hideHiddenStreaks")
  @ButtonSetting
  public void clearCache() {
    StreakDisplayAddon.references().streakApiController().clear();
  }

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Integer> size() {
    return this.size;
  }

  public ConfigProperty<Boolean> showBackground() {
    return this.showBackground;
  }

  public ConfigProperty<Boolean> hideZeroStreaks() {
    return this.hideZeroStreaks;
  }

  public ConfigProperty<Boolean> hideHiddenStreaks() {
    return this.hideHiddenStreaks;
  }
}
