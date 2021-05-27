package com.github.ecstasyawesome.warehouse.controller.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditCategory extends ConfiguredFeedbackController<Category> {

  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(EditCategory.class);
  private Category category;

  @FXML
  private TextField nameField;

  @FXML
  private TextArea descriptionArea;

  @FXML
  private void save(ActionEvent event) {
    if (isFieldValid(nameField, category.getName(), STRICT_NAME, categoryRepository)
        & isFieldValid(descriptionArea, WILDCARD, true)) {
      var categoryCopy = new Category(category);
      category.setName(nameField.getText());
      category
          .setDescription(descriptionArea.getText().isEmpty() ? null : descriptionArea.getText());
      try {
        categoryRepository.update(category);
        logger.info("Edited a category with id={}", category.getId());
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        category.setName(categoryCopy.getName());
        category.setDescription(categoryCopy.getDescription());
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public void accept(Category category) {
    this.category = category;
    nameField.setText(category.getName());
    descriptionArea.setText(Objects.requireNonNullElse(category.getDescription(), ""));
  }

  @Override
  public Category get() {
    return category;
  }
}
