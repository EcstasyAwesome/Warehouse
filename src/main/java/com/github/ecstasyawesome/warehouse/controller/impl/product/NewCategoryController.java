package com.github.ecstasyawesome.warehouse.controller.impl.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewCategoryController extends AbstractFeedbackController<Category> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewCategoryController.class);
  private Category result;

  @FXML
  private TextField nameField;

  @FXML
  private TextArea descriptionArea;

  @FXML
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, null, STRICT_NAME, categoryRepository)
        & isFieldValid(descriptionArea, WILDCARD, true)) {
      var category = Category.Builder.create()
          .setName(nameField.getText())
          .setDescription(descriptionArea.getText().isEmpty() ? null : descriptionArea.getText())
          .build();
      try {
        categoryRepository.create(category);
        logger.info("Added a category with id={}", category.getId());
        result = category;
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public Category get() {
    return result;
  }
}
