package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.DialogWindow;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleDialogWindow extends DialogWindow {

  private final Logger logger = LogManager.getLogger(SimpleDialogWindow.class);

  public SimpleDialogWindow(AlertType alertType, String message) {
    super(alertType, message);
  }

  @Override
  public Optional<ButtonType> showAndGet() {
    logger.debug("Showed a dialog with type '{}' and message '{}'", alert.getAlertType(),
        alert.getContentText());
    return super.showAndGet();
  }
}
