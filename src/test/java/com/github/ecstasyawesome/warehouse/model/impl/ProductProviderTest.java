package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductProvider;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class ProductProviderTest {

  @Test
  public void testCopyConstructor() {
    var provider1 = createProductProvider("provider");
    var provider2 = new ProductProvider(provider1);

    assertNotSame(provider1.getAddress(), provider2.getAddress());
    assertNotSame(provider1.getBusinessContact(), provider2.getBusinessContact());

    assertNotSame(provider1, provider2);
    assertEquals(provider1, provider2);
  }

  @Test
  public void testRecover() {
    var provider1 = createProductProvider("provider");
    var provider2 = ProductProvider.Builder.create()
        .setId(7)
        .setName("n")
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .build();

    assertNotEquals(provider1, provider2);
    provider2.recover(provider1);
    assertEquals(provider1, provider2);

    assertNotSame(provider1.getBusinessContact(), provider2.getBusinessContact());
    assertNotSame(provider1.getAddress(), provider2.getAddress());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(ProductProvider.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var provider = ProductProvider.Builder.create()
        .setName("n")
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .build();
    assertEquals(-1, provider.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var provider = ProductProvider.Builder.create();
    assertThrows(NullPointerException.class, provider::build);
    provider.setId(7)
        .setName("n")
        .setAddress(getAddress())
        .setBusinessContact(getBusinessContact())
        .build();
    assertDoesNotThrow(provider::build);

    provider.setName(null);
    assertThrows(NullPointerException.class, provider::build);
    provider.setName("n");

    provider.setAddress(null);
    assertThrows(NullPointerException.class, provider::build);
    provider.setAddress(getAddress());

    provider.setBusinessContact(null);
    assertThrows(NullPointerException.class, provider::build);
  }

  private Address getAddress() {
    return Address.Builder.create()
        .setRegion("r")
        .setTown("t")
        .setStreet("s")
        .setNumber("n")
        .build();
  }

  private BusinessContact getBusinessContact() {
    return BusinessContact.Builder.create()
        .setPhone("p")
        .setExtraPhone("e")
        .setEmail("e")
        .setSite("s")
        .build();
  }
}