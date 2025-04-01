package com.rappytv.streakdisplay.nametag;

import com.rappytv.streakdisplay.StreakDisplayAddon;
import com.rappytv.streakdisplay.StreakDisplayConfig;
import com.rappytv.streakdisplay.api.StreakApiController;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.RenderPipeline;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StreakNameTag extends NameTag {

  private static final Icon STREAK = Icon.texture(ResourceLocation.create(
      "streakdisplay",
      "textures/streak.png"
  ));
  private final StreakDisplayConfig config;
  private final StreakApiController controller;

  public StreakNameTag(StreakDisplayAddon addon) {
    this.config = addon.configuration();
    this.controller = StreakDisplayAddon.references().streakApiController();
  }

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if (!(this.entity instanceof Player)) {
      return null;
    }
    UUID uuid = this.entity.getUniqueId();

    Integer streak = null;
    if (this.controller.has(uuid)) {
      streak = this.controller.get(uuid);
    } else {
      this.controller.resolve(uuid);
    }

    if (streak == null) {
      return null;
    } else if (streak == -1) {
      if (this.config.hideHiddenStreaks().get()) {
        return null;
      }
      return RenderableComponent.of(Component.translatable(
          "streakdisplay.nametag.hiddenStreak",
          NamedTextColor.RED
      ));
    } else if (streak == 0) {
      if (this.config.hideZeroStreaks().get()) {
        return null;
      }
      return RenderableComponent.of(Component.text("0", NamedTextColor.DARK_GRAY));
    } else {
      return RenderableComponent.of(Component.text(streak.toString()));
    }
  }

  @Override
  public float getScale() {
    return this.config.size().get() / 10f;
  }

  @Override
  public float getWidth() {
    return super.getWidth() + this.getHeight();
  }

  @SuppressWarnings("deprecation")
  @Override
  public void renderText(Stack stack, RenderableComponent component, boolean discrete,
      int textColor, int backgroundColor, float x, float y) {
    if (component == null) {
      return;
    }

    RenderPipeline renderPipeline = Laby.labyAPI().renderPipeline();

    renderPipeline.rectangleRenderer().renderRectangle(
        stack,
        x,
        y,
        this.getWidth(),
        this.getHeight(),
        this.config.showBackground().get() ? backgroundColor : 0
    );
    renderPipeline.renderSeeThrough(this.entity, () -> STREAK.render(
        stack,
        x + 1,
        y + 1.5f,
        this.getHeight() - 3
    ));

    super.renderText(
        stack,
        component,
        discrete,
        textColor,
        0,
        x + this.getHeight(),
        y + 1
    );
  }

  @Override
  public boolean isVisible() {
    return this.config.enabled().get()
        && !this.entity.isCrouching()
        && super.isVisible();
  }
}
