package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;

public abstract class CategoryDao extends GenericDao<Category> {

  public abstract boolean isNamePresent(String name);
}