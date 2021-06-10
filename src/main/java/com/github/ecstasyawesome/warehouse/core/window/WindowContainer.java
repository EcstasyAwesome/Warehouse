package com.github.ecstasyawesome.warehouse.core.window;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class WindowContainer extends WindowNode {

  protected final Stage owner;

  public WindowContainer() {
    owner = null;
  }

  public WindowContainer(Stage owner) {
    this.owner = owner;
  }

  public WindowContainer(StageBasedWindow owner) {
    this(owner.stage);
  }

  public abstract void showWindow(String title, Scene scene);

  public abstract int getActiveWindows();
}
