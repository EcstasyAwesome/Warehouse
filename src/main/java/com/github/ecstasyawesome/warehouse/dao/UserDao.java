package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.User;

public abstract class UserDao extends GenericDao<User> {

  public abstract User get(String login);

  public abstract boolean isEmptyTable();

  public abstract boolean isLoginExist(String login);
}
