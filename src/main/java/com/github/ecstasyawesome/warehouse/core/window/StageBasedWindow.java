package com.github.ecstasyawesome.warehouse.core.window;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class StageBasedWindow extends WindowNode {

  protected final Stage stage = new Stage();

  public StageBasedWindow() {
  }

  public StageBasedWindow(Stage owner) {
    stage.initOwner(owner);
  }

  public StageBasedWindow(StageBasedWindow owner) {
    this(owner.stage);
  }

  public final Stage getStage() {
    return stage;
  }

  public void close() {
    stage.close();
  }

  public boolean isActive() {
    return stage.isShowing();
  }

  public final void setActionBeforeClosing(EventHandler<WindowEvent> handler) {
    stage.setOnHiding(handler);
  }

  protected final void configureStage(final String title, final Scene scene) {
    stage.setTitle(title);
    stage.setScene(scene);
  }
}
