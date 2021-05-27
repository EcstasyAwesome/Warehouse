package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.User;

public abstract class UserRepository extends AbstractRecordRepository<User> implements UniqueField {

  public abstract User get(String login);

  public abstract boolean hasTableRecords();
}
