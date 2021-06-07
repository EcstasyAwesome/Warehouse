package com.github.ecstasyawesome.warehouse.controller.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditCategoryController extends AbstractConfiguredController<Category> {

  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(EditCategoryController.class);
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
      category.setName(getFieldText(nameField));
      category.setDescription(getFieldText(descriptionArea));
      if (!category.equals(categoryCopy)) {
        try {
          categoryRepository.update(category);
          logger.info("Edited a category with id={}", category.getId());
          closeCurrentStage(event);
        } catch (NullPointerException exception) {
          category.recover(categoryCopy);
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @Override
  public void apply(Category category) {
    this.category = category;
    setFieldText(nameField, category.getName());
    setFieldText(descriptionArea, category.getDescription());
  }
}
