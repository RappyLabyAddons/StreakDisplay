package com.rappytv.streakdisplay.core.ui.snapshot;

import com.rappytv.streakdisplay.api.StreakCacheController;
import com.rappytv.streakdisplay.core.StreakDisplayAddon;
import com.rappytv.streakdisplay.core.StreakDisplayConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import org.jetbrains.annotations.Nullable;

public class StreakUserSnapshot extends AbstractLabySnapshot {

  private static final Component ZERO_COMPONENT = Component.text("0", NamedTextColor.DARK_GRAY);
  private static final Component HIDDEN_COMPONENT = Component.translatable(
      "streakdisplay.nametag.hiddenStreak",
      NamedTextColor.RED
  );

  private final boolean enabled;
  private final Component streakComponent;

  public StreakUserSnapshot(Player player, Extras extras, StreakDisplayAddon addon) {
    super(extras);
    StreakDisplayConfig config = addon.configuration();
    StreakCacheController controller = StreakDisplayAddon.cacheController();
    Integer streak = controller.get(player.getUniqueId());

    this.enabled = config.enabled().get();
    this.streakComponent = switch (streak) {
      case null -> null;
      case -1 -> config.hideHiddenStreaks().get() ? null : HIDDEN_COMPONENT;
      case 0 -> config.hideZeroStreaks().get() ? null : ZERO_COMPONENT;
      default -> Component.text(streak.toString());
    };
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  @Nullable
  public Component streakComponent() {
    return this.streakComponent;
  }
}
