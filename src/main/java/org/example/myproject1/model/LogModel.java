package org.example.myproject1.model;

import lombok.*;


public class LogModel {

    private String firstName;

    private String lastName;

    private String middleName;

    private String birthday;

    private String birthPlace;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LogModel() {
    }

    public LogModel(String firstName, String lastName, String middleName, String birthday, String birthPlace) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
    }

    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                '}';
    }

}
