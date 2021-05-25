package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import javafx.collections.ObservableList;

public abstract class ProductDao extends GenericDao<Product> implements UniqueField {

  public abstract ObservableList<Product> getAll(Category category);

  public abstract ObservableList<Product> search(String name);
}
