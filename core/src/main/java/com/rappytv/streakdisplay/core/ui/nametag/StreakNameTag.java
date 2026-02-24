package com.rappytv.streakdisplay.core.ui.nametag;

import com.rappytv.streakdisplay.api.StreakDisplayTextures;
import com.rappytv.streakdisplay.core.StreakDisplayAddon;
import com.rappytv.streakdisplay.core.StreakDisplayConfig;
import com.rappytv.streakdisplay.core.ui.snapshot.StreakDisplayExtraKeys;
import com.rappytv.streakdisplay.core.ui.snapshot.StreakUserSnapshot;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.labymod.api.laby3d.render.queue.SubmissionCollector;
import net.labymod.api.laby3d.render.queue.submissions.IconSubmission.DisplayMode;
import org.jetbrains.annotations.NotNull;

public class StreakNameTag extends ComponentNameTag {

  private static final Icon STREAK_ICON = Icon.texture(StreakDisplayTextures.STREAK);

  private final StreakDisplayConfig config;
  private float iconSize;

  public StreakNameTag(StreakDisplayAddon addon) {
    this.config = addon.configuration();
  }

  @Override
  public void begin(EntitySnapshot snapshot) {
    super.begin(snapshot);
    this.iconSize = this.fontRenderer.getLineHeight();
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
    if (snapshot.isDiscrete()
        || snapshot.isInvisible()
        || !snapshot.has(StreakDisplayExtraKeys.STREAK_USER)) {
      return super.buildComponents(snapshot);
    }
    StreakUserSnapshot streakUser = snapshot.get(StreakDisplayExtraKeys.STREAK_USER);
    if (!streakUser.isEnabled()) {
      return super.buildComponents(snapshot);
    }
    Component streakComponent = streakUser.streakComponent();

    return streakComponent != null
        ? Collections.singletonList(streakComponent)
        : super.buildComponents(snapshot);
  }

  @Override
  public void render(Stack stack, SubmissionCollector submissionCollector,
      EntitySnapshot snapshot) {
    super.render(stack, submissionCollector, snapshot);

    submissionCollector.submitIcon(
        stack,
        STREAK_ICON,
        DisplayMode.NORMAL,
        -11,
        -1f,
        this.iconSize,
        this.iconSize,
        -1
    );
  }

  @Override
  protected float calculateWidth(Collection<Component> components) {
    return super.calculateWidth(components) - this.iconSize;
  }

  @Override
  protected int getBackgroundColor(EntitySnapshot snapshot) {
    return 0;
  }

  @Override
  public float getScale() {
    return this.config.size().get() / 10f;
  }
}
