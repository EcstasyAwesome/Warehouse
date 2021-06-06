package com.github.ecstasyawesome.warehouse.core;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.controller.Cacheable;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.module.impl.user.AuthorizationModule;
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
import org.apache.logging.log4j.Level;
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
  private AbstractModule<? extends AbstractController> currentModule;
  private Cacheable currentCachedController;

  private WindowManager(final Stage root) {
    this.root = root;
  }

  public static WindowManager getInstance() {
    if (instance == null) {
      var exception = new IllegalStateException("Window Manager is not initialized");
      throw LOGGER.throwing(Level.FATAL, exception);
    }
    return instance;
  }

  public static void initialize(final Stage stage) {
    if (instance != null) {
      var exception = new IllegalStateException("Window Manager is already initialized");
      throw LOGGER.throwing(Level.FATAL, exception);
    }
    instance = new WindowManager(stage);
    LOGGER.debug("Initialized");
  }

  public void showAuthorization() {
    LOGGER.debug("Request to show the authorization stage");
    var user = getUserFromContext();
    if (user.isEmpty()) {
      currentModule = null;
      currentCachedController = null;
      var module = AuthorizationModule.getInstance();
      var fxmlBundle = module.create();
      applyFadeAnimation(fxmlBundle.getParent());
      configureAuthorizationStageAndShow(module.getTitle(), fxmlBundle.getScene());
    } else {
      showDialog(AlertType.WARNING, "Do logout before login"); // TODO i18n
    }
  }

  public <T extends AbstractController> void show(final AbstractModule<T> module) {
    LOGGER.debug("Request to show the module at the main stage");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, module.getAccess())) {
      currentModule = module;
      var fxmlBundle = module.create();
      checkAbilityToCache(fxmlBundle.getController());
      configureMainStageAndShow(module.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractConfiguredController<E>, E> void show(
      final AbstractConfiguredModule<T, E> configuredModule, E instance) {
    LOGGER.debug("Request to show the configured module at the main stage");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, configuredModule.getAccess())) {
      currentModule = configuredModule;
      var fxmlBundle = configuredModule.create();
      var controller = fxmlBundle.getController();
      checkAbilityToCache(controller);
      controller.apply(instance);
      configureMainStageAndShow(configuredModule.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractController> void showAndWait(final AbstractModule<T> module) {
    LOGGER.debug("Request to show the module");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, module.getAccess())) {
      var fxmlBundle = module.create();
      checkAbilityToCache(fxmlBundle.getController());
      createNewExtraStageAndShow(module.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractConfiguredController<E>, E> void showAndWait(
      final AbstractConfiguredModule<T, E> configuredModule, E instance) {
    LOGGER.debug("Request to show the configured module");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, configuredModule.getAccess())) {
      var fxmlBundle = configuredModule.create();
      var controller = fxmlBundle.getController();
      checkAbilityToCache(controller);
      controller.apply(instance);
      createNewExtraStageAndShow(configuredModule.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractFeedbackController<E>, E> Optional<E> showAndGet(
      final AbstractFeedbackModule<T, E> feedbackModule) {
    LOGGER.debug("Request to show the feedback module");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, feedbackModule.getAccess())) {
      var fxmlBundle = feedbackModule.create();
      var controller = fxmlBundle.getController();
      checkAbilityToCache(controller);
      createNewExtraStageAndShow(feedbackModule.getTitle(), fxmlBundle.getScene());
      return Optional.ofNullable(controller.get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  public <T extends AbstractConfiguredFeedbackController<E, R>, E, R> Optional<R> showAndGet(
      final AbstractConfiguredFeedbackModule<T, E, R> configuredFeedbackModule, final E instance) {
    LOGGER.debug("Request to show the configured feedback module");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, configuredFeedbackModule.getAccess())) {
      var fxmlBundle = configuredFeedbackModule.create();
      var controller = fxmlBundle.getController();
      checkAbilityToCache(controller);
      controller.apply(instance);
      createNewExtraStageAndShow(configuredFeedbackModule.getTitle(), fxmlBundle.getScene());
      return Optional.ofNullable(controller.get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  @SuppressWarnings("rawtypes")
  public Class<? extends AbstractModule> getCurrentModuleClass() {
    return Objects.requireNonNull(currentModule.getClass(), "Any module was not shown");
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

  private void checkAbilityToCache(AbstractController controller) {
    if (currentCachedController != null && currentCachedController.isReady()) {
      var clazz = currentCachedController.getClass();
      cachedControllers.add(clazz);
      currentCachedController.backup();
      LOGGER.debug("Backup the current controller '{}'", clazz.getSimpleName());
    }
    if (controller instanceof Cacheable cacheable) {
      currentCachedController = cacheable;
      var clazz = cacheable.getClass();
      LOGGER.debug("The controller '{}' is cacheable", clazz.getSimpleName());
      if (cachedControllers.contains(clazz)) {
        cachedControllers.remove(clazz);
        cacheable.recover();
        LOGGER.debug("Recovered the controller '{}'", clazz.getSimpleName());
      }
    } else {
      currentCachedController = null;
      LOGGER.debug("The controller '{}' is not cacheable", controller.getClass().getSimpleName());
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

  private void createNewExtraStageAndShow(String title, Scene scene) {
    var preparedTitle = prepareStageName(title);
    var stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(stages.isEmpty() ? root : stages.get(stages.size() - 1));
    stage.setTitle(preparedTitle);
    stage.setScene(scene);
    stage.setOnHidden(event -> {
      stages.remove(stage);
      LOGGER.debug("Closed the extra stage '{}'", preparedTitle);
    });
    stages.add(stage);
    LOGGER.debug("Created the extra stage '{}'", preparedTitle);
    LOGGER.trace("Showed the extra stage '{}'", preparedTitle);
    stage.showAndWait();
  }

  private void configureMainStageAndShow(String title, Scene scene) {
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
    var preparedTitle = prepareStageName(title);
    mainStage.setTitle(preparedTitle);
    mainStage.setScene(scene);
    LOGGER.trace("Showed the main stage '{}'", preparedTitle);
    mainStage.show();
  }

  private void configureAuthorizationStageAndShow(String title, Scene scene) {
    if (authorizationStage.getOwner() == null) {
      authorizationStage.initOwner(root);
      authorizationStage.setResizable(false);
      authorizationStage.setOnCloseRequest(event ->
          LOGGER.trace("Application closed without authorized user"));
      LOGGER.debug("Configured authorization stage for the first time");
    }
    closeAllExtraStages();
    cachedControllers.clear();
    mainStage.close();
    var preparedTitle = prepareStageName(title);
    authorizationStage.setTitle(preparedTitle);
    authorizationStage.setScene(scene);
    LOGGER.trace("Showed the authorization stage '{}'", preparedTitle);
    authorizationStage.show();
  }

  private String prepareStageName(String moduleName) {
    return String.format("%s - Warehouse", moduleName);
  }

  private void applyFadeAnimation(Parent parent) {
    var fadeTransition = new FadeTransition(Duration.seconds(2), parent);
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
        LOGGER.info("Logged out '{}'", user.getPersonSecurity().getLogin());
        LOGGER.trace("Application closed");
      });
    };
  }
}
