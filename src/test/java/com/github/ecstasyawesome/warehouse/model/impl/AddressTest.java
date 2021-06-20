package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createAddress;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class AddressTest {

  @Test
  public void testCopyConstructor() {
    var address1 = createAddress();
    var address2 = new Address(address1);
    assertNotSame(address1, address2);
    assertEquals(address1, address2);
  }

  @Test
  public void testRecover() {
    var address1 = createAddress();
    var address2 = Address.Builder.create()
        .setId(2)
        .setRegion("sfsdfsdfsd")
        .setTown("sadasdasd")
        .setStreet("fsdfsdfsd")
        .setNumber("1231241")
        .build();
    assertNotEquals(address1, address2);
    address2.recover(address1);
    assertEquals(address1, address2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Address.class).usingGetClass().suppress(Warning.NULL_FIELDS).verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var address = Address.Builder.create()
        .setRegion("r")
        .setTown("t")
        .setStreet("s")
        .setNumber("n")
        .build();
    assertEquals(-1, address.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var address = Address.Builder.create();
    assertThrows(NullPointerException.class, address::build);

    address.setRegion("r").setTown(null);
    assertThrows(NullPointerException.class, address::build);

    address.setRegion(null).setTown("r");
    assertThrows(NullPointerException.class, address::build);

    address.setRegion("r").setTown("t");
    assertDoesNotThrow(address::build);

    address.setStreet("s").setNumber(null);
    assertDoesNotThrow(address::build);

    address.setStreet(null).setNumber("n");
    assertThrows(NullPointerException.class, address::build);
  }
}