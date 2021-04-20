package com.github.ecstasyawesome.warehouse.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SessionManager {

  private static final Map<String, Object> STORAGE = new HashMap<>();

  private SessionManager() {
  }

  public static void store(final String key, final Object obj) {
    STORAGE.put(key, obj);
  }

  public static Optional<Object> get(final String key) {
    var obj = STORAGE.get(key);
    return Optional.ofNullable(obj);
  }

  public static boolean delete(final String key) {
    return STORAGE.remove(key) != null;
  }

  public static void erase() {
    STORAGE.clear();
  }
}
