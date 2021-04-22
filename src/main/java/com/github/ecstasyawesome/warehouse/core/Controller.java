package com.github.ecstasyawesome.warehouse.core;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class Controller {

  protected final void closeCurrentStage(ActionEvent event) {
    var parent = (Parent) event.getSource();
    var stage = (Stage) parent.getScene().getWindow();
    stage.close();
  }
}
