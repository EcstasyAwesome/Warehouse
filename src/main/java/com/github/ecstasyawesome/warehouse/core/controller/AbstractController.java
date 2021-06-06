package com.github.ecstasyawesome.warehouse.core.controller;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import com.github.ecstasyawesome.warehouse.repository.Deletable;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;

public abstract class AbstractController {

  protected final <T extends AbstractRecord> void deleteSelectedTableRecord(
      final TableView<T> table, final Deletable<T> deletable, final WindowManager windowManager,
      final Logger logger) {
    var selectionModel = table.getSelectionModel();
    if (!selectionModel.isEmpty()) {
      var confirmation = windowManager.showDialog(AlertType.CONFIRMATION,
          "All related data will be removed too, are you sure?"); // TODO i18n
      if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        var record = selectionModel.getSelectedItem();
        try {
          deletable.delete(record);
          table.getItems().remove(record);
          logger.info("Deleted a {} with id={}", record.getClass().getSimpleName(), record.getId());
        } catch (NullPointerException exception) {
          windowManager.showDialog(exception);
        }
      }
    }
  }

  protected final void closeCurrentStage(final Event event) {
    var parent = (Parent) event.getSource();
    var stage = (Stage) parent.getScene().getWindow();
    stage.close();
  }
}
