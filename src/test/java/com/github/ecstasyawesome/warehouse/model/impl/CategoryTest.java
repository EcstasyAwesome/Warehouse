package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class CategoryTest {

  @Test
  public void testCopyConstructor() {
    var category1 = createCategory("name");
    var category2 = new Category(category1);
    assertNotSame(category1, category2);
    assertEquals(category1, category2);
  }

  @Test
  public void testRecover() {
    var category1 = createCategory("name");
    var category2 = Category.Builder.create()
        .setId(2)
        .setName("n")
        .setDescription("d")
        .build();
    assertNotEquals(category1, category2);
    category2.recover(category1);
    assertEquals(category1, category2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Category.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var category = Category.Builder.create()
        .setName("n")
        .setDescription("d")
        .build();
    assertEquals(-1, category.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var category = Category.Builder.create();
    assertThrows(NullPointerException.class, category::build);

    category.setName("n");
    assertDoesNotThrow(category::build);
  }
}