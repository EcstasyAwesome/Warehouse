package com.github.ecstasyawesome.warehouse.repository;

import javafx.collections.ObservableList;

public interface Collectable<T, E> {

  ObservableList<T> getAll(E criteria);
}
