package com.github.ecstasyawesome.warehouse.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class EventManager {

  private static final Map<LocalDate, String> EVENTS = new HashMap<>();

  private EventManager() {
  }

  public static Map<LocalDate, String> getEventList() {
    return Collections.unmodifiableMap(EVENTS);
  }

  // TODO push notifications (will be super to find lib to show native notifications)

  public static void showPopUpWindow(AlertType type, final String message) {
    var text = "Message is not provided"; // TODO i18n
    if (message != null && message.isBlank()) {
      text = message;
    }
    EVENTS.put(LocalDate.now(), message);
    var alert = new Alert(type);
    alert.setTitle("Title"); // TODO i18n
    alert.setHeaderText(null);
    alert.setContentText(text);
    alert.showAndWait();
  }
}
