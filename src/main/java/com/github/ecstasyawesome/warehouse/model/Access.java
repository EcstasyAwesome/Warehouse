package com.github.ecstasyawesome.warehouse.model;

import java.util.stream.Stream;

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


  public static Access[] getAccesses() {
    return Stream.of(values()).filter(access -> access != ROOT).toArray(Access[]::new);
  }

  @Override
  public String toString() {
    return name;
  }
}
