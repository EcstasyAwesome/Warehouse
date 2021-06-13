package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.SingleSceneWindow;
import com.github.ecstasyawesome.warehouse.core.window.StageBasedWindow;
import com.github.ecstasyawesome.warehouse.core.window.WindowNode;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtraModalWindow extends SingleSceneWindow {

  private final Logger logger = LogManager.getLogger(ExtraModalWindow.class);

  {
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setOnHidden(event -> logger.debug("Closed '{}'", stage.getTitle()));
    logger.debug("Initialized '{}'", stage.getTitle());
  }

  public ExtraModalWindow(String title, Scene scene) {
    super(title, scene);
  }

  public ExtraModalWindow(Stage owner, String title, Scene scene) {
    super(owner, title, scene);
  }

  public ExtraModalWindow(StageBasedWindow owner, String title, Scene scene) {
    super(owner, title, scene);
  }

  @Override
  public void show(WindowNode... windowNodes) {
    closeWindowNodes(windowNodes);
    logger.debug("Showed a window titled with '{}'", stage.getTitle());
    stage.showAndWait();
  }
}
