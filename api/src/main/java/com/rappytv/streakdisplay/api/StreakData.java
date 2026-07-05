package com.rappytv.streakdisplay.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StreakData {

  private final int streak;
  private final State state;

  public StreakData(@Nullable Integer streak) {
    this.streak = streak != null ? streak : -1;
    this.state = switch(streak) {
      case -1 -> State.HIDDEN;
      case 0 -> State.ZERO;
      case null -> State.NULL;
      default -> State.PRESENT;
    };
  }

  public int getStreak() {
    return this.streak;
  }

  @NotNull
  public State getState() {
    return this.state;
  }

  public boolean isNull() {
    return this.state == State.NULL;
  }

  public enum State {
    PRESENT,
    HIDDEN,
    ZERO,
    NULL
  }

}
