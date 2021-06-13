package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.DialogWindow;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExceptionDialogWindow extends DialogWindow {

  private final Logger logger = LogManager.getLogger(ExceptionDialogWindow.class);
  private final Exception exception;

  public ExceptionDialogWindow(Exception exception) {
    super(AlertType.ERROR, exception.getMessage());
    alert.getDialogPane().setContent(createContainer(exception));
    this.exception = exception;
  }

  @Override
  public Optional<ButtonType> showAndGet() {
    logger.debug("Showed an error dialog with exception '{}' and message '{}'",
        exception.getClass(), alert.getContentText());
    return super.showAndGet();
  }

  private GridPane createContainer(Exception exception) {
    var stacktrace = getStacktrace(exception);
    var textArea = new TextArea(stacktrace);
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);
    var gridPane = new GridPane();
    gridPane.setMaxWidth(Double.MAX_VALUE);
    gridPane.add(textArea, 0, 0);
    return gridPane;
  }

  private String getStacktrace(Exception exception) {
    var result = new StringWriter();
    try (var printStream = new PrintWriter(result)) {
      exception.printStackTrace(printStream);
    }
    return result.toString();
  }
}
