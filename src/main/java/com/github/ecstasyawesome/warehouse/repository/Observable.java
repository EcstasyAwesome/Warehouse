package com.github.ecstasyawesome.warehouse.repository;

import javafx.collections.ObservableList;

public interface Observable<T> {

  ObservableList<T> getAll();
}
