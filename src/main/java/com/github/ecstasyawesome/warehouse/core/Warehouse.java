package com.github.ecstasyawesome.warehouse.core;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Warehouse extends Application {

  private final Logger logger = LogManager.getLogger(Warehouse.class);

  @Override
  public void start(Stage primaryStage) {
    logger.trace("Application started");
    WindowManager.initialize(primaryStage);
    WindowManager.getInstance().showAuthorization();
  }
}
