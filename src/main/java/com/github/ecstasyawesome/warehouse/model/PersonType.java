package com.github.ecstasyawesome.warehouse.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum PersonType {

  // TODO i18n
  INDIVIDUAL("Individual"),
  LEGAL_ENTITY("Legal entity");

  public final String name;

  PersonType(String name) {
    this.name = name;
  }

  public static ObservableList<PersonType> getPersonTypes() {
    return FXCollections.observableArrayList(PersonType.values());
  }

  @Override
  public String toString() {
    return name;
  }
}
