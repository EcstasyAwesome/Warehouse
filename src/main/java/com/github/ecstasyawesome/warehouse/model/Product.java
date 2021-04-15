package com.github.ecstasyawesome.warehouse.model;

public class Product {  // TODO change me (here for test only)

  private long id;
  private String name;
  private String category;
  private String unit;

  public Product(long id, String name, String category, String unit) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.unit = unit;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String units) {
    this.unit = units;
  }
}
