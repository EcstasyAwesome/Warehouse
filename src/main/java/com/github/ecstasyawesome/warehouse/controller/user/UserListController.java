package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.user.EditUserModule;
import com.github.ecstasyawesome.warehouse.module.user.NewUserModule;
import com.github.ecstasyawesome.warehouse.module.user.ShowUserModule;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
  private final ShowUserModule showUserModule = ShowUserModule.getInstance();
  private final NewUserModule newUserModule = NewUserModule.getInstance();
  private final EditUserModule editUserModule = EditUserModule.getInstance();
  private final Logger logger = LogManager.getLogger(UserListController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addButton;

  @FXML
  private Button showButton;

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
    addButton.setDisable(!isAccessGranted(sessionUser, newUserModule.getAccess()));

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
            showButton.setDisable(!isAccessGranted(sessionUser, showUserModule.getAccess()));
            editButton.setDisable(condition
                                  || !isAccessGranted(sessionUser, editUserModule.getAccess()));
            deleteButton.setDisable(condition || !isAccessGranted(sessionUser, Access.ADMIN));
          } else {
            showButton.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setDisable(true);
          }
        });

    getUsersFromDatabase();
  }

  @FXML
  private void doOnSortTable() {
    userTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void doOnKeyReleased(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showButton.isDisable()) {
      show();
    }
  }

  @FXML
  private void doOnMouseClick(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showButton.isDisable()) {
      show();
    }
  }

  @FXML
  private void add() {
    var result = windowManager.showAndGet(newUserModule);
    result.ifPresent(userTable.getItems()::add);
  }

  @FXML
  private void show() {
    var model = userTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(showUserModule, model.getSelectedItem());
    }
  }

  @FXML
  private void edit() {
    var model = userTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(editUserModule, model.getSelectedItem());
    }
  }

  @FXML
  private void delete() {
    deleteSelectedTableRecord(userTable, userRepository, windowManager, logger);
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
