package com.github.ecstasyawesome.warehouse.core.window;

import java.util.Objects;

public abstract class WindowNode {

  public abstract void close();

  public abstract boolean isActive();

  protected final void closeWindowNodes(WindowNode... windowNodes) {
    Objects.requireNonNull(windowNodes);
    for (var window : windowNodes) {
      Objects.requireNonNull(window);
      window.close();
    }
  }
}
