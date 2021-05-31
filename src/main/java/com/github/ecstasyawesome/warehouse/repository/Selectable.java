package com.github.ecstasyawesome.warehouse.repository;

public interface Selectable<T, E> {

  T select(E criteria);
}
