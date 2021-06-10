package com.github.ecstasyawesome.warehouse.core.window;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class SingleSceneWindow extends StageBasedWindow {

  public SingleSceneWindow(String title, Scene scene) {
    configureStage(title, scene);
  }

  public SingleSceneWindow(Stage owner, String title, Scene scene) {
    super(owner);
    configureStage(title, scene);
  }

  public SingleSceneWindow(StageBasedWindow owner, String title, Scene scene) {
    this(owner.stage, title, scene);
  }

  public abstract void show(WindowNode... windowNodes);
}
