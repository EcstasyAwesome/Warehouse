package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.impl.User;

public abstract class UserDao extends GenericDao<User> implements UniqueField {

  public abstract User get(String login);

  public abstract boolean hasTableRecords();
}
