package com.github.ecstasyawesome.warehouse.core.window;

public abstract class WindowNode {

  public abstract void close();

  public abstract boolean isActive();

  protected final void closeWindowNodes(WindowNode... windowNodes) {
    for (var window : windowNodes) {
      window.close();
    }
  }
}
