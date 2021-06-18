package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.MultiSceneWindow;
import com.github.ecstasyawesome.warehouse.core.window.StageBasedWindow;
import com.github.ecstasyawesome.warehouse.core.window.WindowNode;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkWindow extends MultiSceneWindow {

  private final Settings settings = ApplicationSettings.getInstance();
  private final ViewSettings viewSettings = ViewSettings.getInstance();
  private final Logger logger = LogManager.getLogger(WorkWindow.class);

  {
    stage.setMinWidth(viewSettings.getDefaultWidth());
    stage.setMinHeight(viewSettings.getDefaultHeight());
    stage.setWidth(viewSettings.getWidth());
    stage.setHeight(viewSettings.getHeight());
    stage.setMaximized(viewSettings.isMaximized());
    stage.setOnHidden(getOnCloseAction());
    logger.debug("Initialized");
  }

  public WorkWindow() {
  }

  public WorkWindow(Stage owner) {
    super(owner);
  }

  public WorkWindow(StageBasedWindow owner) {
    super(owner);
  }

  @Override
  public void show(String title, Scene scene, WindowNode... windowNodes) {
    closeWindowNodes(windowNodes);
    configureStage(prepareWindowName(title), scene);
    logger.debug("Showed a scene titled with '{}'", title);
    stage.show();
  }

  private String prepareWindowName(String title) {
    return String.format("%s - Warehouse", title);
  }

  private EventHandler<WindowEvent> getOnCloseAction() {
    return event -> {
      viewSettings.setWidth(stage.getWidth());
      viewSettings.setHeight(stage.getHeight());
      viewSettings.setMaximized(stage.isMaximized());
      viewSettings.save();
      settings.save();
      logger.debug("Closed");
    };
  }
}
