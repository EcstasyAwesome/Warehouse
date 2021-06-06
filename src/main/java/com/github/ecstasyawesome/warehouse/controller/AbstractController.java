package com.github.ecstasyawesome.warehouse.controller;

import javafx.event.Event;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class AbstractController {

  protected final void closeCurrentStage(Event event) {
    var parent = (Parent) event.getSource();
    var stage = (Stage) parent.getScene().getWindow();
    stage.close();
  }
}
