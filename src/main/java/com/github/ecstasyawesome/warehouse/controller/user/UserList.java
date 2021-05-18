package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.dao.impl.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.module.user.NewUserProvider;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList extends Controller {

  private final UserDao userDao = UserDaoService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(UserList.class);
  private final boolean rootAccess = windowManager.isAccessGranted(Access.ROOT);

  @FXML
  private TableView<User> userTable;

  @FXML
  private TableColumn<User, Long> idColumn;

  @FXML
  private TableColumn<User, String> surnameColumn;

  @FXML
  private TableColumn<User, String> nameColumn;

  @FXML
  private TableColumn<User, String> secondNameColumn;

  @FXML
  private TableColumn<User, String> phoneColumn;

  @FXML
  private TableColumn<User, String> loginColumn;

  @FXML
  private TableColumn<User, String> passwordColumn;

  @FXML
  private TableColumn<User, Access> accessColumn;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
    surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
    secondNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    loginColumn.setCellValueFactory(getPropertyValueFactoryWithGag("login"));
    passwordColumn.setCellValueFactory(getPropertyValueFactoryWithGag("password"));
    accessColumn.setCellValueFactory(new PropertyValueFactory<>("access"));
    accessColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(Access.getAccesses()));

    userTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevUser, currentUser) -> {
          if (currentUser != null) {
            var access = currentUser.getAccess();
            if (rootAccess) {
              userTable.setEditable(access != Access.ROOT);
            } else {
              userTable.setEditable(access.level > Access.ADMIN.level);
            }
          }
        });

    getUsersFromDatabase();
  }

  @FXML
  private void add() {
    var result = windowManager.showAndGet(NewUserProvider.INSTANCE);
    result.ifPresent(userTable.getItems()::add);
  }

  @FXML
  private void delete() {
    var model = userTable.getSelectionModel();
    if (!model.isEmpty()) {
      var confirmation = windowManager.showDialog(AlertType.CONFIRMATION,
          "All related data will be removed too, are you sure?"); // TODO i18n
      if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        var user = model.getSelectedItem();
        if ((rootAccess && user.getAccess() != Access.ROOT)
            || user.getAccess().level > Access.ADMIN.level) {
          try {
            userDao.delete(user.getId());
            userTable.getItems().remove(user);
            logger.info("Deleted a user with id={}", user.getId());
          } catch (NullPointerException exception) {
            windowManager.showDialog(exception);
          }
        } else {
          windowManager.showDialog(AlertType.WARNING, "You cannot delete this user"); // TODO i18n
        }
      }
    } else {
      windowManager.showDialog(AlertType.INFORMATION, "First choose some item"); // TODO i18n
    }
  }

  @FXML
  private void refresh() {
    getUsersFromDatabase();
  }

  @FXML
  private void editSurname(CellEditEvent<User, String> event) {
    var user = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      if (newValue.matches(STRICT_NAME.pattern())) {
        try {
          user.setSurname(newValue);
          userDao.update(user);
          logChanges("surname", oldValue, newValue, user);
        } catch (NullPointerException exception) {
          user.setSurname(oldValue);
          refreshAndShowError(exception);
        }
      } else {
        refreshAndShowDialog("Incorrect surname"); // TODO i18n
      }
    }
  }

  @FXML
  private void editName(CellEditEvent<User, String> event) {
    var user = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      if (newValue.matches(STRICT_NAME.pattern())) {
        try {
          user.setName(newValue);
          userDao.update(user);
          logChanges("name", oldValue, newValue, user);
        } catch (NullPointerException exception) {
          user.setName(oldValue);
          refreshAndShowError(exception);
        }
      } else {
        refreshAndShowDialog("Incorrect name"); // TODO i18n
      }
    }
  }

  @FXML
  private void editSecondName(CellEditEvent<User, String> event) {
    var user = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      if (newValue.matches(STRICT_NAME.pattern())) {
        try {
          user.setSecondName(newValue);
          userDao.update(user);
          logChanges("second name", oldValue, newValue, user);
        } catch (NullPointerException exception) {
          user.setSecondName(oldValue);
          refreshAndShowError(exception);
        }
      } else {
        refreshAndShowDialog("Incorrect second name"); // TODO i18n
      }
    }
  }

  @FXML
  private void editPhone(CellEditEvent<User, String> event) {
    var user = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      if (newValue.matches(PHONE.pattern())) {
        try {
          user.setPhone(newValue);
          userDao.update(user);
          logChanges("phone", oldValue, newValue, user);
        } catch (NullPointerException exception) {
          user.setPhone(oldValue);
          refreshAndShowError(exception);
        }
      } else {
        refreshAndShowDialog("Incorrect phone number"); // TODO i18n
      }
    }
  }

  @FXML
  private void editAccess(CellEditEvent<User, Access> event) {
    var user = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      try {
        user.setAccess(newValue);
        userDao.update(user);
        logChanges("access", oldValue, newValue, user);
        userTable.refresh();
      } catch (NullPointerException exception) {
        user.setAccess(oldValue);
        refreshAndShowError(exception);
      }
    }
  }

  private void getUsersFromDatabase() {
    try {
      userTable.setItems(userDao.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private PropertyValueFactory<User, String> getPropertyValueFactoryWithGag(String property) {
    return new PropertyValueFactory<>(property) {
      @Override
      public ObservableValue<String> call(CellDataFeatures<User, String> param) {
        if (!rootAccess && param.getValue().getAccess().level <= Access.ADMIN.level) {
          return new ObservableValueBase<>() {
            @Override
            public String getValue() {
              return "********";
            }
          };
        }
        return super.call(param);
      }
    };
  }

  private void refreshAndShowError(Exception exception) {
    userTable.refresh();
    windowManager.showDialog(exception);
  }

  private void refreshAndShowDialog(String message) {
    userTable.refresh();
    windowManager.showDialog(AlertType.WARNING, message);
  }

  private void logChanges(String field, Object oldValue, Object newValue, User user) {
    logger.info("Edited user's {} from '{}' to '{}' with id={}", field, oldValue, newValue,
        user.getId());
  }
}
