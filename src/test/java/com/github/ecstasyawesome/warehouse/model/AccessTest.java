package com.github.ecstasyawesome.warehouse.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

public class AccessTest {

  @Test
  public void testName() {
    for (var access : Access.values()) {
      assertNotNull(access.name);
      assertFalse(access.name.isBlank());
    }
  }

  @Test
  public void testLevel() {
    var index = 0;
    for (var access : Access.values()) {
      assertEquals(index++, access.level);
    }
  }

  @Test
  public void testNameEqualToString() {
    for (var access : Access.values()) {
      assertEquals(access.name, access.toString());
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testGetAccesses() {
    var expected = Stream.of(Access.values())
        .filter(access -> access != Access.ROOT && access != Access.GUEST)
        .toArray(Access[]::new);
    var actual = Access.getAccesses();
    assertTrue(actual instanceof ObservableList);
    assertArrayEquals(expected, actual.toArray());
  }

  @Test
  public void testIsAccessGranted() {
    var user = DefaultRecordRepository.createUser("login");
    for (var userAccess : Access.values()) {
      user.getPersonSecurity().setAccess(userAccess);
      for (var currentAccess : Access.values()) {
        var expected = currentAccess == Access.GUEST || userAccess.level <= currentAccess.level;
        assertEquals(expected, Access.isAccessGranted(user, currentAccess));
      }
    }
  }

  @Test
  public void testIsAccessGrantedNullUser() {
    assertFalse(Access.isAccessGranted(null, Access.USER));
  }
}