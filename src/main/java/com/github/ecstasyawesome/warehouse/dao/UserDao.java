package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.User;

public abstract class UserDao extends RecordRepository<User> implements UniqueField {

  public abstract User get(String login);

  public abstract boolean hasTableRecords();
}
