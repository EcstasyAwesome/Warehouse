package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import java.util.List;

public abstract class ProductDao extends GenericDao<Product> implements UniqueField {

  public abstract List<Product> getAll(Category category);

  public abstract List<Product> search(String name);
}
