package com.github.ecstasyawesome.warehouse.core;

import javafx.application.Application;
import javafx.stage.Stage;

public class Warehouse extends Application {

  @Override
  public void start(Stage primaryStage) {
    WindowManager.initialize(primaryStage);
    WindowManager.getInstance().showAuthorization();
  }
}
