package com.github.ecstasyawesome.warehouse.controller;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class BottomBar {

  private final Timer timer = new Timer(true);

  @FXML
  private Text notificationTextField;

  void show(String message) {
    if (message != null && message.isBlank()) {
      timer.purge();
      timer.schedule(createTask(), TimeUnit.SECONDS.toMillis(3));
      notificationTextField.setText(message);
    }
  }

  private TimerTask createTask() {
    return new TimerTask() {
      @Override
      public void run() {
        notificationTextField.setText("Ready"); // TODO i18n
      }
    };
  }
}
