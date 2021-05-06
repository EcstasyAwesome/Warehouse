package com.github.ecstasyawesome.warehouse.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SessionManager {

  private static final Map<String, Object> STORAGE = new HashMap<>();
  private static final Logger LOGGER = LogManager.getLogger(SessionManager.class);

  private SessionManager() {
  }

  public static void store(final String key, final Object obj) {
    STORAGE.put(key, obj);
    LOGGER.debug("Added a new value with key '{}'", key);
  }

  public static Optional<Object> get(final String key) {
    var obj = STORAGE.get(key);
    return Optional.ofNullable(obj);
  }

  public static boolean delete(final String key) {
    var result = STORAGE.remove(key) != null;
    if (result) {
      LOGGER.debug("Deleted a value with key '{}'", key);
    }
    return result;
  }

  public static void erase() {
    STORAGE.clear();
    LOGGER.debug("Session erased");
  }
}
