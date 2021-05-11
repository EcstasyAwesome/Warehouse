package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;

public abstract class ProductDao extends GenericDao<Product> {

  public abstract boolean isNamePresent(String name);

  public abstract Product get(Category category);
}
