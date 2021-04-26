package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.module.AuthorizationProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public final class WindowManager {

  private static WindowManager instance;
  private final Stage root;
  private ModuleProvider<?> currentModuleProvider;
  private Cacheable currentCacheableController;
  private final Stage authorizationStage = new Stage();
  private final Stage mainStage = new Stage();
  private final List<Stage> stages = new ArrayList<>();
  private final Set<Class<? extends Cacheable>> cachedControllers = new HashSet<>();

  private WindowManager(final Stage root) {
    this.root = root;
  }

  public static WindowManager getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Window Manager is not initialized");
    }
    return instance;
  }

  public static void initialize(final Stage stage) {
    if (instance != null) {
      throw new IllegalStateException("Window Manager is already initialized");
    }
    instance = new WindowManager(stage);
  }

  public void showAuthorization() {
    var user = getUserFromContext();
    if (user.isEmpty()) {
      configureAuthorizationStage();
      currentModuleProvider = null;
      currentCacheableController = null;
      cachedControllers.clear();
      mainStage.close();
      authorizationStage.show();
    } else {
      showDialog(AlertType.WARNING, "Do logout before login"); // TODO i18n
    }
  }

  public <T> void show(ModuleProvider<? super T> provider) {
    if (mainStage.getOwner() == null) {
      configureMainStage();
    }
    if (isAccessGranted(provider.getAccess())) {
      closeSecondaryStagesIfPresent();
      authorizationStage.close();
      currentModuleProvider = provider;
      var module = provider.create();
      checkAbilityToCache(module.getController());
      mainStage.setTitle(prepareStageName(provider.getTitle()));
      mainStage.setScene(module.getScene());
      mainStage.show();
    } else {
      showAccessWarning();
    }
  }

  public <T, E> Optional<E> show(FeedbackModuleProvider<? super T, E> provider) {
    if (isAccessGranted(provider.getAccess())) {
      var module = provider.create();
      var stage = createNewStage(provider.getTitle(), module.getScene());
      stages.add(stage);
      stage.showAndWait();
      return Optional.ofNullable(module.getController().take());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  public ModuleProvider<?> getCurrentModuleProvider() {
    return Objects.requireNonNull(currentModuleProvider, "Any module was not shown");
  }

  public void shutdown() {
    root.close();
    Platform.exit();
  }


  // TODO push notifications (will be super to find lib to show native notifications)
  public Optional<ButtonType> showDialog(AlertType type, final String message) {
    return createNewAlertDialog(type, message).showAndWait();
  }

  public void showDialog(final Exception exception) {
    var alert = createNewAlertDialog(AlertType.ERROR, exception.getMessage());
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

    alert.getDialogPane().setExpandableContent(gridPane);
    alert.showAndWait();
  }

  public boolean isAccessGranted(Access access) {
    if (access == Access.GUEST) {
      return true;
    }
    var user = getUserFromContext();
    if (user.isEmpty()) {
      return false;
    }
    return user.get().getAccess().level <= access.level;
  }

  private void checkAbilityToCache(Controller controller) {
    if (currentCacheableController != null && currentCacheableController.isReady()) {
      cachedControllers.add(currentCacheableController.getClass());
      currentCacheableController.backup();
    }
    if (controller instanceof Cacheable cacheableController) {
      currentCacheableController = cacheableController;
      var clazz = cacheableController.getClass();
      if (cachedControllers.contains(clazz)) {
        cachedControllers.remove(clazz);
        cacheableController.recovery();
      }
    } else {
      currentCacheableController = null;
    }
  }

  private Alert createNewAlertDialog(AlertType type, String message) {
    var alert = new Alert(type);
    alert.setTitle("Notification"); // TODO i18n
    alert.setHeaderText(null);
    var defaultMessage = "Message is not provided"; // TODO i18n
    var messageNullOrBlank = message == null || message.isBlank();
    alert.setContentText(messageNullOrBlank ? defaultMessage : message);
    return alert;
  }

  private String getStacktrace(Exception exception) {
    var result = new StringWriter();
    try (var printStream = new PrintWriter(result)) {
      exception.printStackTrace(printStream);
    }
    return result.toString();
  }

  private Optional<User> getUserFromContext() {
    var user = (User) SessionManager.get("currentUser").orElse(null);
    return Optional.ofNullable(user);
  }

  private void closeSecondaryStagesIfPresent() {
    if (stages.size() > 0) {
      stages.get(0).close();
      stages.clear();
    }
  }

  private void showAccessWarning() {
    showDialog(AlertType.WARNING, "Access denied!"); // TODO i18n
  }

  private Stage createNewStage(String title, Scene scene) {
    var stage = new Stage();
    EventHandler<WindowEvent> onCloseEvent = event -> stages.remove(stage);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(stages.isEmpty() ? mainStage : stages.get(stages.size() - 1));
    stage.setTitle(prepareStageName(title));
    stage.setScene(scene);
    stage.setOnCloseRequest(onCloseEvent);
    stage.setOnHidden(onCloseEvent);
    return stage;
  }

  private void configureMainStage() {
    var viewSettings = ViewSettings.getInstance();
    var onCloseAction = getOnCloseActions();
    mainStage.initOwner(root);
    mainStage.setMinWidth(viewSettings.getDefaultWidth());
    mainStage.setMinHeight(viewSettings.getDefaultHeight());
    mainStage.setWidth(viewSettings.getWidth());
    mainStage.setHeight(viewSettings.getHeight());
    mainStage.setMaximized(viewSettings.isMaximized());
    mainStage.setOnCloseRequest(onCloseAction);
    mainStage.setOnHidden(onCloseAction);
  }

  private void configureAuthorizationStage() {
    if (authorizationStage.getOwner() == null) {
      authorizationStage.initOwner(root);
      authorizationStage.setResizable(false);
    }
    var provider = AuthorizationProvider.INSTANCE;
    var module = provider.create();
    applyFadeAnimation(module.getParent());
    authorizationStage.setTitle(prepareStageName(provider.getTitle()));
    authorizationStage.setScene(module.getScene());
  }

  private String prepareStageName(String moduleName) {
    return String.format("Warehouse - %s", moduleName);
  }

  private void applyFadeAnimation(Parent parent) {
    var fadeTransition = new FadeTransition(Duration.seconds(3), parent);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.setCycleCount(1);
    fadeTransition.playFromStart();
  }

  private EventHandler<WindowEvent> getOnCloseActions() {
    return event -> {
      var viewSettings = ViewSettings.getInstance();
      viewSettings.setWidth(mainStage.getWidth());
      viewSettings.setHeight(mainStage.getHeight());
      viewSettings.setMaximized(mainStage.isMaximized());
      viewSettings.save();
    };
  }
}
