package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class PopupNotificationController extends AbstractConfiguredController<Popup> {

  private Popup popup;

  @FXML
  private Text titleText;

  @FXML
  private Label messageLabel;

  @FXML
  private void close(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY) {
      popup.hide();
    }
  }

  @Override
  public void apply(Popup popup) {
    this.popup = popup;
  }

  public void setTitle(String title) {
    if (title != null && !title.isBlank()) {
      titleText.setText(title);
    }
  }

  public void setMessage(String message) {
    if (message != null && !message.isBlank()) {
      messageLabel.setText(message);
    }
  }
}
