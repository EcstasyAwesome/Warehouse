package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import java.util.List;
import javafx.collections.ObservableList;

public abstract class ProductDao extends GenericDao<Product> implements UniqueField {

  public abstract ObservableList<Product> getAll(Category category);

  public abstract ObservableList<Product> search(String name);
}
