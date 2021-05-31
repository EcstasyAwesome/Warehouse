package com.github.ecstasyawesome.warehouse.repository;

public interface Readable<T> {

  T read(long id);
}
