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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WindowManager {

  private static final Logger LOGGER = LogManager.getLogger(WindowManager.class);
  private static WindowManager instance;
  private final Stage root;
  private final Stage authorizationStage = new Stage();
  private final Stage mainStage = new Stage();
  private final List<Stage> stages = new ArrayList<>();
  private final Set<Class<? extends Cacheable>> cachedControllers = new HashSet<>();
  private ModuleProvider<? extends Controller> currentModuleProvider;
  private Cacheable currentCachedController;

  private WindowManager(final Stage root) {
    this.root = root;
  }

  public static WindowManager getInstance() {
    if (instance == null) {
      var exception = new IllegalStateException("Window Manager is not initialized");
      LOGGER.fatal(exception);
      throw exception;
    }
    return instance;
  }

  public static void initialize(final Stage stage) {
    if (instance != null) {
      var exception = new IllegalStateException("Window Manager is already initialized");
      LOGGER.fatal(exception);
      throw exception;
    }
    instance = new WindowManager(stage);
    LOGGER.debug("Initialized");
  }

  public void showAuthorization() {
    LOGGER.debug("Request to show the authorization stage");
    var user = getUserFromContext();
    if (user.isEmpty()) {
      configureAuthorizationStage();
      LOGGER.trace("Showed the authorization stage");
      authorizationStage.show();
    } else {
      showDialog(AlertType.WARNING, "Do logout before login"); // TODO i18n
    }
  }

  public <T> void show(final ModuleProvider<? super T> provider) {
    LOGGER.debug("Request to show the module provider '{}'", provider.getClass().getName());
    if (isAccessGranted(provider.getAccess())) {
      configureMainStage(provider);
      var module = provider.create();
      checkAbilityToCache(null);
      var title = prepareStageName(provider.getTitle());
      mainStage.setTitle(title);
      mainStage.setScene(module.getScene());
      LOGGER.trace("Showed the stage '{}'", title);
      mainStage.show();
    } else {
      showAccessWarning();
    }
  }

  public <T> void show(final CachedModuleProvider<? super T> provider) {
    LOGGER.debug("Request to show the cached module provider '{}'", provider.getClass().getName());
    if (isAccessGranted(provider.getAccess())) {
      configureMainStage(provider);
      var module = provider.create();
      checkAbilityToCache(module.getController());
      var title = prepareStageName(provider.getTitle());
      mainStage.setTitle(title);
      mainStage.setScene(module.getScene());
      LOGGER.trace("Showed the stage '{}'", title);
      mainStage.show();
    } else {
      showAccessWarning();
    }
  }

  public <T, E> Optional<E> showAndGet(final FeedbackModuleProvider<? super T, E> provider) {
    LOGGER.debug("Request to show the feedback module provider '{}'",
        provider.getClass().getName());
    if (isAccessGranted(provider.getAccess())) {
      var module = provider.create();
      var stage = createNewExtraStage(provider.getTitle(), module.getScene());
      stages.add(stage);
      LOGGER.trace("Showed the extra stage '{}'", stage.getTitle());
      stage.showAndWait();
      return Optional.ofNullable(module.getController().get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  public <T, E> Optional<E> showAndGet(
      final ConfiguredFeedbackModuleProvider<? super T, E> provider, final E data) {
    LOGGER.debug("Request to show the configured feedback module provider '{}'",
        provider.getClass().getName());
    if (isAccessGranted(provider.getAccess())) {
      var module = provider.create();
      var controller = module.getController();
      controller.accept(data);
      var stage = createNewExtraStage(provider.getTitle(), module.getScene());
      stages.add(stage);
      LOGGER.trace("Showed the extra stage '{}'", stage.getTitle());
      stage.showAndWait();
      return Optional.ofNullable(controller.get());
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
    var alert = createNewDialog(type, message);
    LOGGER.trace("Showed a dialog");
    return alert.showAndWait();
  }

  public void showDialog(final Exception exception) {
    var alert = createNewDialog(AlertType.ERROR, exception.getMessage());
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
    LOGGER.trace("Showed a dialog with an exception");
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

  private void checkAbilityToCache(CachedController controller) {
    if (currentCachedController != null && currentCachedController.isReady()) {
      var clazz = currentCachedController.getClass();
      cachedControllers.add(clazz);
      currentCachedController.backup();
      LOGGER.debug("Backup the controller '{}'", clazz.getName());
    }
    if (controller != null) {
      currentCachedController = controller;
      var clazz = controller.getClass();
      LOGGER.debug("Set the controller '{}' like a current cached controller", clazz.getName());
      if (cachedControllers.contains(clazz)) {
        cachedControllers.remove(clazz);
        controller.recover();
        LOGGER.debug("Recovered the controller '{}'", clazz.getName());
      }
    } else {
      currentCachedController = null;
      LOGGER.debug("A current controller is not cacheable");
    }
  }

  private Alert createNewDialog(AlertType type, String message) {
    var alert = new Alert(type);
    alert.setTitle("Notification"); // TODO i18n
    alert.setHeaderText(null);
    var defaultMessage = "Message is not provided"; // TODO i18n
    var messageNullOrBlank = message == null || message.isBlank();
    alert.setContentText(messageNullOrBlank ? defaultMessage : message);
    LOGGER.debug("Created an dialog with type '{}' and message '{}'", type.name(), message);
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

  private void closeAllExtraStages() {
    if (stages.size() > 0) {
      stages.get(0).close();
      stages.clear();
    }
  }

  private void showAccessWarning() {
    showDialog(AlertType.WARNING, "Access denied!"); // TODO i18n
  }

  private Stage createNewExtraStage(String title, Scene scene) {
    var stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(stages.isEmpty() ? root : stages.get(stages.size() - 1));
    stage.setTitle(prepareStageName(title));
    stage.setScene(scene);
    stage.setOnHidden(event -> {
      stages.remove(stage);
      LOGGER.debug("Closed the extra stage with title '{}'", stage.getTitle());
    });
    LOGGER.debug("Created the extra stage with title '{}'", stage.getTitle());
    return stage;
  }

  private void configureMainStage(ModuleProvider<? extends Controller> provider) {
    if (mainStage.getOwner() == null) {
      var viewSettings = ViewSettings.getInstance();
      mainStage.initOwner(root);
      mainStage.setMinWidth(viewSettings.getDefaultWidth());
      mainStage.setMinHeight(viewSettings.getDefaultHeight());
      mainStage.setWidth(viewSettings.getWidth());
      mainStage.setHeight(viewSettings.getHeight());
      mainStage.setMaximized(viewSettings.isMaximized());
      mainStage.setOnHidden(getOnCloseActions());
      LOGGER.debug("Configured main stage for the first time");
    }
    closeAllExtraStages();
    authorizationStage.close();
    currentModuleProvider = provider;
  }

  private void configureAuthorizationStage() {
    if (authorizationStage.getOwner() == null) {
      authorizationStage.initOwner(root);
      authorizationStage.setResizable(false);
      authorizationStage.setOnCloseRequest(event ->
          LOGGER.trace("Application closed without authorized user"));
      LOGGER.debug("Configured authorization stage for the first time");
    }
    closeAllExtraStages();
    currentModuleProvider = null;
    currentCachedController = null;
    cachedControllers.clear();
    mainStage.close();
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
      var applicationSettings = ApplicationSettings.getInstance();
      viewSettings.setWidth(mainStage.getWidth());
      viewSettings.setHeight(mainStage.getHeight());
      viewSettings.setMaximized(mainStage.isMaximized());
      viewSettings.save();
      applicationSettings.save();
      getUserFromContext().ifPresent(user -> {
        LOGGER.info("Logged out '{}'", user.getLogin());
        LOGGER.trace("Application closed");
      });
    };
  }
}
