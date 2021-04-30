package com.github.ecstasyawesome.warehouse.core;

import java.util.stream.Stream;

public enum Access {

  ROOT(0), ADMIN(1), USER(2), GUEST(3);

  public final int level;

  Access(int level) {
    this.level = level;
  }

  public static Access[] getAccesses() {
    return Stream.of(values()).filter(access -> access != ROOT).toArray(Access[]::new);
  }
}
