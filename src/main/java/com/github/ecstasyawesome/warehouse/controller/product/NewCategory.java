package com.github.ecstasyawesome.warehouse.controller.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.CategoryDao;
import com.github.ecstasyawesome.warehouse.dao.impl.CategoryDaoService;
import com.github.ecstasyawesome.warehouse.model.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewCategory extends FeedbackController<Category> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryDao categoryDao = CategoryDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(NewCategory.class);
  private Category result;

  @FXML
  private TextField nameField;

  @FXML
  private TextArea descriptionArea;

  @FXML
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, null, STRICT_NAME, categoryDao)
        & isFieldValid(descriptionArea, WILDCARD, true)) {
      var category = Category.getBuilder()
          .setName(nameField.getText())
          .setDescription(descriptionArea.getText().isEmpty() ? null : descriptionArea.getText())
          .build();
      try {
        categoryDao.create(category);
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
