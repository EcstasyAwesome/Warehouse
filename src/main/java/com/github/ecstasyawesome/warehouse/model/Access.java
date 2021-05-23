package com.github.ecstasyawesome.warehouse.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum Access {

  // TODO i18n
  ROOT(0, "Root"),
  ADMIN(1, "Administrator"),
  USER(2, "User"),
  GUEST(3, "Guest");

  public final int level;
  public final String name;

  Access(int level, String name) {
    this.level = level;
    this.name = name;
  }

  public static ObservableList<Access> getAccesses() {
    return Stream.of(values()).
        filter(access -> access != ROOT)
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
  }

  @Override
  public String toString() {
    return name;
  }
}
