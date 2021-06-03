package com.github.ecstasyawesome.warehouse.controller.impl.user;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.provider.impl.user.EditUserProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.user.NewUserProvider;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserListController extends AbstractController {

  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final NewUserProvider newUserProvider = NewUserProvider.getInstance();
  private final EditUserProvider editUserProvider = EditUserProvider.getInstance();
  private final Logger logger = LogManager.getLogger(UserListController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addButton;

  @FXML
  private Button editButton;

  @FXML
  private Button deleteButton;

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
  private TableColumn<User, String> emailColumn;

  @FXML
  private TableColumn<User, Access> accessColumn;

  @FXML
  private void initialize() {
    addButton.setDisable(!isAccessGranted(sessionUser, newUserProvider.getAccess()));

    idColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    surnameColumn.setCellValueFactory(entry -> entry.getValue().surnameProperty());
    nameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    secondNameColumn.setCellValueFactory(entry -> entry.getValue().secondNameProperty());
    phoneColumn.setCellValueFactory(entry -> entry.getValue().getPersonContact().phoneProperty());
    emailColumn.setCellValueFactory(entry -> entry.getValue().getPersonContact().emailProperty());
    accessColumn.setCellValueFactory(entry ->
        entry.getValue().getPersonSecurity().accessProperty());

    userTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevUser, currentUser) -> {
          if (currentUser != null) {
            var currentUserAccessLevel = currentUser.getPersonSecurity().getAccess();
            var condition = isAccessGranted(sessionUser, currentUserAccessLevel);
            editButton.setDisable(condition
                                  || !isAccessGranted(sessionUser, editUserProvider.getAccess()));
            deleteButton.setDisable(condition || !isAccessGranted(sessionUser, Access.ADMIN));
          } else {
            editButton.setDisable(true);
            deleteButton.setDisable(true);
          }
        });

    getUsersFromDatabase();
  }

  @FXML
  private void onSortTable() {
    var selected = userTable.getSelectionModel();
    if (!selected.isEmpty()) {
      selected.clearSelection();
    }
  }

  @FXML
  private void onKeyReleased(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !editButton.isDisable()) {
      edit();
    }
  }

  @FXML
  private void onMouseClick(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !editButton.isDisable()) {
      edit();
    }
  }

  @FXML
  private void add() {
    var result = windowManager.showAndGet(newUserProvider);
    result.ifPresent(userTable.getItems()::add);
  }

  @FXML
  private void edit() {
    var model = userTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(editUserProvider, model.getSelectedItem());
    }
  }

  @FXML
  private void delete() {
    var model = userTable.getSelectionModel();
    if (!model.isEmpty()) {
      var confirmation = windowManager.showDialog(AlertType.CONFIRMATION,
          "All related data will be removed too, are you sure?"); // TODO i18n
      if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        var user = model.getSelectedItem();
        try {
          userRepository.delete(user);
          userTable.getItems().remove(user);
          logger.info("Deleted a user with id={}", user.getId());
        } catch (NullPointerException exception) {
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @FXML
  private void refresh() {
    getUsersFromDatabase();
  }

  private void getUsersFromDatabase() {
    try {
      userTable.setItems(userRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}
