package com.github.ecstasyawesome.warehouse.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class SessionManagerTest {

  private static boolean eraseWorksFine = false;

  @BeforeEach
  public void setUp() {
    SessionManager.erase();
  }

  @Order(1)
  @Test
  public void testErasing() {
    var key = "key";
    var expected = new Object();
    SessionManager.store(key, expected);
    var actual = SessionManager.get(key);
    assertNotNull(actual);
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());

    SessionManager.erase();

    var actual2 = SessionManager.get(key);
    assertNotNull(actual2);
    assertTrue(actual2.isEmpty());

    eraseWorksFine = true;
  }

  @Test
  public void testAddingAndGettingObject() {
    assumeTrue(eraseWorksFine);
    var key = "key";
    var expected = new Object();
    SessionManager.store(key, expected);
    var actual = SessionManager.get(key);
    assertNotNull(actual);
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());
  }

  @Test
  public void testReplacingAndGettingObject() {
    assumeTrue(eraseWorksFine);
    var key = "key";

    var expected1 = new Object();
    SessionManager.store(key, expected1);
    var actual1 = SessionManager.get(key);
    assertNotNull(actual1);
    assertTrue(actual1.isPresent());
    assertEquals(expected1, actual1.get());

    var expected2 = new Object();
    SessionManager.store(key, expected2);
    var actual2 = SessionManager.get(key);
    assertNotNull(actual2);
    assertTrue(actual2.isPresent());
    assertEquals(expected2, actual2.get());

    assertNotEquals(expected1, expected2);
    assertNotEquals(actual1, actual2);
  }

  @Test
  public void testGettingAbsentObject() {
    assumeTrue(eraseWorksFine);
    var actual = SessionManager.get("key");
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testDeletingAbsentKey() {
    assumeTrue(eraseWorksFine);
    assertFalse(SessionManager.delete("key"));
  }

  @Test
  public void testAddingAndDeletingObject() {
    assumeTrue(eraseWorksFine);
    var key = "key";
    var expected = new Object();
    SessionManager.store(key, expected);
    var actual = SessionManager.get(key);
    assertNotNull(actual);
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());

    assertTrue(SessionManager.delete(key));

    var actual2 = SessionManager.get(key);
    assertNotNull(actual2);
    assertTrue(actual2.isEmpty());
  }

}