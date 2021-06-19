package com.github.ecstasyawesome.warehouse.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

public class PersonTypeTest {

  @Test
  public void testName() {
    for (var personType : PersonType.values()) {
      assertNotNull(personType.name);
      assertFalse(personType.name.isBlank());
    }
  }

  @Test
  public void testNameEqualToString() {
    for (var personType : PersonType.values()) {
      assertEquals(personType.name, personType.toString());
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testGetPersonTypes() {
    var actual = PersonType.getPersonTypes();
    assertTrue(actual instanceof ObservableList);
    assertArrayEquals(PersonType.values(), actual.toArray());
  }
}