package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import javafx.collections.ObservableList;

public abstract class AbstractRecordRepository<T extends AbstractRecord> extends AbstractRepository<T> {

  public abstract ObservableList<T> getAll();

  public abstract void create(T instance);

  public abstract T get(long id);

  public abstract void update(T instance);

  public abstract void delete(long id);
}
