package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.EditProductController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class EditProductProvider extends
    AbstractConfiguredModuleProvider<EditProductController, Product> {

  private static final EditProductProvider INSTANCE = new EditProductProvider();
  private final URL fxml = getClass().getResource("/model/product/EditProduct.fxml");

  private EditProductProvider() {
  }

  public static EditProductProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractConfiguredModule<EditProductController, Product> create() {
    return new AbstractConfiguredModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Edit product"; // TODO i18n
  }
}
