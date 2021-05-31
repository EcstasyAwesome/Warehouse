package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.User;

public abstract class UserRepository extends AbstractRepository<User> implements Producible<User>,
    Readable<User>, Updatable<User>, Deletable<User>, Selectable<User, String>, Observable<User>,
    UniqueField {

  public abstract boolean hasTableRecords();
}
