package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.ProductList;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class ProductListProvider extends ModuleProvider<ProductList> {

  public static final ProductListProvider INSTANCE = new ProductListProvider();
  public final URL fxml = getClass().getResource("/model/product/ProductList.fxml");

  private ProductListProvider() {
  }

  @Override
  public Module<ProductList> create() {
    return new Module<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Product list"; // TODO i18n
  }
}
