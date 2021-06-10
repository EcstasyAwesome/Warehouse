package com.github.ecstasyawesome.warehouse.core.window;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class MultiSceneWindow extends StageBasedWindow {

  public MultiSceneWindow() {
  }

  public MultiSceneWindow(Stage owner) {
    super(owner);
  }

  public MultiSceneWindow(StageBasedWindow owner) {
    super(owner);
  }

  public abstract void show(String title, Scene scene, WindowNode... windowNodes);
}
