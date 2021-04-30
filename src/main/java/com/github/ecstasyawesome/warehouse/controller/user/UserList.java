package com.github.ecstasyawesome.warehouse.controller.user;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.module.user.NewUserProvider;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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

public class UserList extends Controller {

  private final Pattern phonePattern = Pattern.compile("^((\\+?3)?8)?\\d{10}$");
  private final Pattern stringPattern = Pattern.compile("^[A-ZА-Я][A-zА-я]+$");
  private final ObservableList<User> users = FXCollections.observableArrayList();
  private final UserDaoService userDaoService = UserDaoService.INSTANCE;
  private final WindowManager windowManager = WindowManager.getInstance();
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
    userTable.setItems(users);

    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
    surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    surnameColumn.setOnEditCommit(editSurnameAction());
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    nameColumn.setOnEditCommit(editNameAction());
    secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
    secondNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    secondNameColumn.setOnEditCommit(editSecondNameAction());
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    phoneColumn.setOnEditCommit(editPhoneAction());
    loginColumn.setCellValueFactory(getPropertyValueFactoryWithGag("login"));
    passwordColumn.setCellValueFactory(getPropertyValueFactoryWithGag("password"));
    accessColumn.setCellValueFactory(new PropertyValueFactory<>("access"));
    accessColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(Access.getAccesses()));
    accessColumn.setOnEditCommit(editAccessAction());

    userTable.pressedProperty().addListener(observable -> {
      var model = userTable.getSelectionModel();
      if (!model.isEmpty()) {
        var access = model.getSelectedItem().getAccess();
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
  void add() {
    var result = windowManager.showAndGet(NewUserProvider.INSTANCE);
    result.ifPresent(users::add);
  }

  @FXML
  void delete() {
    var model = userTable.getSelectionModel();
    if (!model.isEmpty()) {
      var confirmation = windowManager.showDialog(
          AlertType.CONFIRMATION,
          "All related data will be removed too, are you sure?");
      if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        var user = model.getSelectedItem();
        if ((rootAccess && user.getAccess() != Access.ROOT)
            || user.getAccess().level > Access.ADMIN.level) {
          try {
            userDaoService.delete(user.getId());
            users.remove(user);
          } catch (UnsupportedOperationException exception) {
            windowManager.showDialog(exception);
          }
        } else {
          windowManager.showDialog(AlertType.WARNING, "You cannot delete this user"); // TODO i18n
        }
      }
    } else {
      windowManager.showDialog(AlertType.INFORMATION, "First choose some item");
    }
  }

  @FXML
  void refresh() {
    users.clear();
    getUsersFromDatabase();
  }

  private void getUsersFromDatabase() {
    try {
      users.addAll(userDaoService.getAll());
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

  private EventHandler<CellEditEvent<User, Access>> editAccessAction() {
    return event -> {
      var user = event.getRowValue();
      try {
        user.setAccess(event.getNewValue());
        userDaoService.update(user);
        userTable.refresh();
      } catch (UnsupportedOperationException exception) {
        user.setAccess(event.getOldValue());
        userTable.refresh();
        windowManager.showDialog(exception);
      }
    };
  }

  private EventHandler<CellEditEvent<User, String>> editNameAction() {
    return event -> {
      var user = event.getRowValue();
      var newValue = event.getNewValue();
      if (newValue.matches(stringPattern.pattern())) {
        try {
          user.setName(newValue);
          userDaoService.update(user);
        } catch (UnsupportedOperationException exception) {
          user.setName(event.getOldValue());
          userTable.refresh();
          windowManager.showDialog(exception);
        }
      } else {
        userTable.refresh();
        windowManager.showDialog(AlertType.WARNING, "Incorrect name"); // TODO i18n
      }
    };
  }

  private EventHandler<CellEditEvent<User, String>> editPhoneAction() {
    return event -> {
      var user = event.getRowValue();
      var newValue = event.getNewValue();
      if (newValue.matches(phonePattern.pattern())) {
        try {
          user.setPhone(newValue);
          userDaoService.update(user);
        } catch (UnsupportedOperationException exception) {
          user.setPhone(event.getOldValue());
          userTable.refresh();
          windowManager.showDialog(exception);
        }
      } else {
        userTable.refresh();
        windowManager.showDialog(AlertType.WARNING, "Incorrect phone number"); // TODO i18n
      }
    };
  }

  private EventHandler<CellEditEvent<User, String>> editSecondNameAction() {
    return event -> {
      var user = event.getRowValue();
      var newValue = event.getNewValue();
      if (newValue.matches(stringPattern.pattern())) {
        try {
          user.setSecondName(newValue);
          userDaoService.update(user);
        } catch (UnsupportedOperationException exception) {
          user.setSecondName(event.getOldValue());
          userTable.refresh();
          windowManager.showDialog(exception);
        }
      } else {
        userTable.refresh();
        windowManager.showDialog(AlertType.WARNING, "Incorrect second name"); // TODO i18n
      }
    };
  }

  private EventHandler<CellEditEvent<User, String>> editSurnameAction() {
    return event -> {
      var user = event.getRowValue();
      var newValue = event.getNewValue();
      if (newValue.matches(stringPattern.pattern())) {
        try {
          user.setSurname(newValue);
          userDaoService.update(user);
        } catch (UnsupportedOperationException exception) {
          user.setSurname(event.getOldValue());
          userTable.refresh();
          windowManager.showDialog(exception);
        }
      } else {
        userTable.refresh();
        windowManager.showDialog(AlertType.WARNING, "Incorrect surname"); // TODO i18n
      }
    };
  }
}
