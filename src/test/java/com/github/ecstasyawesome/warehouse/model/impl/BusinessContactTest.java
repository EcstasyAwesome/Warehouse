package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createBusinessContact;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class BusinessContactTest {

  @Test
  public void testCopyConstructor() {
    var contact1 = createBusinessContact();
    var contact2 = new BusinessContact(contact1);
    assertNotSame(contact1, contact2);
    assertEquals(contact1, contact2);
  }

  @Test
  public void testRecover() {
    var contact1 = createBusinessContact();
    var contact2 = BusinessContact.Builder.create()
        .setId(123)
        .setPhone("21321312321")
        .setExtraPhone("12312312312")
        .setEmail("dsadasd")
        .setSite("sdfsdfsdaf")
        .build();
    assertNotEquals(contact1, contact2);
    contact2.recover(contact1);
    assertEquals(contact1, contact2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(BusinessContact.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var contact = BusinessContact.Builder.create()
        .setPhone("3123123")
        .setExtraPhone("32423423423")
        .setEmail("sdfcsadfsdf")
        .setSite("asdasdas")
        .build();
    assertEquals(-1, contact.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var contact = BusinessContact.Builder.create();
    assertThrows(NullPointerException.class, contact::build);

    contact.setPhone("1312312");
    assertDoesNotThrow(contact::build);
  }
}