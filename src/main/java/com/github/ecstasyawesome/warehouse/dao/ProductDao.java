package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import java.util.List;

public abstract class ProductDao extends GenericDao<Product> {

  public abstract boolean isNamePresent(String name);

  public abstract List<Product> getAll(Category category);
}
