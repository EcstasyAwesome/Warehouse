package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import javafx.collections.ObservableList;

public abstract class ProductRepository extends AbstractRepository<Product> implements
    Readable<Product>, Producible<Product>, Updatable<Product>, Deletable<Product>, UniqueField,
    Collectable<Product, Category>, Observable<Product> {

  public abstract ObservableList<Product> search(String name);
}
