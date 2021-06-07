package com.github.ecstasyawesome.warehouse.core.controller;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.Deletable;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;

public abstract class AbstractController {

  protected final String getTextOrEmpty(final String text) {
    return text == null ? "" : text;
  }

  protected final String getNullOrText(final String text) {
    return text == null || text.isEmpty() ? null : text;
  }

  protected final void setFieldText(final TextInputControl field, final String text) {
    field.setText(getTextOrEmpty(text));
  }

  protected final String getFieldText(final TextInputControl field) {
    return getNullOrText(field.getText());
  }

  protected final void configureButton(final Button button, final User user, final Access access) {
    button.setDisable(!Access.isAccessGranted(user, access));
  }

  protected final void configureButton(final Button button, final User user,
      final boolean primaryCondition, final Access access) {
    button.setDisable(primaryCondition || !isAccessGranted(user, access));
  }

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
