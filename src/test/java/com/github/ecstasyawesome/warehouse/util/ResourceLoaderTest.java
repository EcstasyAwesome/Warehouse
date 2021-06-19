package com.github.ecstasyawesome.warehouse.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ecstasyawesome.warehouse.util.ResourceLoader.Language;
import java.io.IOException;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.fxml.LoadException;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class ResourceLoaderTest {

  private static final URL RESOURCE = ResourceLoader.class.getResource("/VBoxTemplate.fxml");
  private static final URL BROKEN_RESOURCE =
      ResourceLoader.class.getResource("/VBoxBrokenTemplate.fxml");

  @Test
  public void loadValidResource() {
    var actual = ResourceLoader.load(RESOURCE);
    assertNotNull(actual);
    assertEquals(VBox.class, actual.getClass());
  }

  @Test
  public void loadThrowsIllegalWhenLoadInvalidResource() {
    assertThrows(IllegalArgumentException.class, () -> ResourceLoader.load(BROKEN_RESOURCE));
  }

  @Test
  public void loadThrowsNpeWhenLoadAbsentResource() {
    assertThrows(NullPointerException.class, () -> ResourceLoader.load(null));
  }

  @Test
  public void createFxmlLoaderThrowsNpeWhenLoadAbsentResource() {
    assertThrows(NullPointerException.class, () -> ResourceLoader.createFxmlLoader(null));
  }

  @Test
  public void createFxmlLoaderThrowsLoadExceptionWhenLoadInvalidResource() {
    var fxmlLoader = ResourceLoader.createFxmlLoader(BROKEN_RESOURCE);
    assertNotNull(fxmlLoader);
    assertThrows(LoadException.class, fxmlLoader::load);
  }

  @Test
  public void createFxmlLoaderReturnsValidLoader() throws IOException {
    var fxmlLoader = ResourceLoader.createFxmlLoader(RESOURCE);
    assertNotNull(fxmlLoader);

    var expectedResourceBundle = ResourceLoader.getLanguageBundle();
    var actualResourceBundle = fxmlLoader.getResources();
    assertNotNull(actualResourceBundle);
    assertNotNull(expectedResourceBundle);
    assertEquals(expectedResourceBundle, actualResourceBundle);

    var parent = fxmlLoader.load();
    assertNotNull(parent);
    assertEquals(VBox.class, parent.getClass());

    var controller = fxmlLoader.getController();
    assertNotNull(controller);
    assertEquals(VboxTemplateController.class, controller.getClass());
  }

  @Test
  public void testThatLanguageOrigNameEqualToString() {
    for (var language : Language.values()) {
      assertEquals(language.originalName, language.toString());
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void getLanguagesReturnsObservableList() {
    var actual = Language.getLanguages();
    assertNotNull(actual);
    assertTrue(actual instanceof ObservableList);
  }
}