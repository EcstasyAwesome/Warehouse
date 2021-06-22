package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createPersonSecurity;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ecstasyawesome.warehouse.model.Access;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class PersonSecurityTest {

  @Test
  public void testCopyConstructor() {
    var security1 = createPersonSecurity();
    var security2 = new PersonSecurity(security1);
    assertNotSame(security1, security2);
    assertEquals(security1, security2);
  }

  @Test
  public void testRecover() {
    var security1 = createPersonSecurity();
    var security2 = PersonSecurity.Builder.create()
        .setId(7)
        .setLogin("l")
        .setPassword("p")
        .setAccess(Access.USER)
        .build();
    assertNotEquals(security1, security2);
    security2.recover(security1);
    assertEquals(security1, security2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(PersonSecurity.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var security = PersonSecurity.Builder.create()
        .setLogin("l")
        .setPassword("p")
        .setAccess(Access.USER)
        .build();
    assertEquals(-1, security.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var security = PersonSecurity.Builder.create();
    assertThrows(NullPointerException.class, security::build);
    security.setLogin("l")
        .setPassword("p")
        .setAccess(Access.USER)
        .build();
    assertDoesNotThrow(security::build);

    security.setLogin(null);
    assertThrows(NullPointerException.class, security::build);
    security.setLogin("l");

    security.setPassword(null);
    assertThrows(NullPointerException.class, security::build);
    security.setPassword("p");

    security.setAccess(null);
    assertThrows(NullPointerException.class, security::build);
  }
}