package com.github.ecstasyawesome.warehouse.repository;

import javafx.collections.ObservableList;

public interface Searchable<T, E> {

  ObservableList<T> search(E criterion);
}
