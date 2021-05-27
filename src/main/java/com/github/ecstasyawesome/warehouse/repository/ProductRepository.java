package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import javafx.collections.ObservableList;

public abstract class ProductRepository extends RecordRepository<Product> implements UniqueField {

  public abstract ObservableList<Product> getAll(Category category);

  public abstract ObservableList<Product> search(String name);
}
