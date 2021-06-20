package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class AbstractPersonRecord extends AbstractNamedRecord {

  private final StringProperty surname = new SimpleStringProperty();
  private final StringProperty secondName = new SimpleStringProperty();

  public String getSurname() {
    return surname.get();
  }

  public void setSurname(String surname) {
    this.surname.set(surname);
  }

  public StringProperty surnameProperty() {
    return surname;
  }

  public String getSecondName() {
    return secondName.get();
  }

  public void setSecondName(String secondName) {
    this.secondName.set(secondName);
  }

  public StringProperty secondNameProperty() {
    return secondName;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }

    var that = (AbstractPersonRecord) obj;
    return Objects.equals(getSurname(), that.getSurname())
           && Objects.equals(getSecondName(), that.getSecondName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getSurname(), getSecondName());
  }
}
