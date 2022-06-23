package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Application implements Serializable {

    private String ApplicationID;
    private String firstName;
    private String lastName;
    private int age;
    private Address address;
    private String email;
    private String phone;
    private String question1;
    private String question2;
    private ApplicationStatus status;
    private String UserID;
    private String DogID;

    public Application(String applicationID, String firstName, String lastName, int age, Address address, String email, String phone, String question1, String question2, ApplicationStatus status, String userID, String dogID) {
        ApplicationID = applicationID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.question1 = question1;
        this.question2 = question2;
        this.status = status;
        UserID = userID;
        DogID = dogID;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDogID() {
        return DogID;
    }

    public void setDogID(String dogID) {
        DogID = dogID;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "ApplicationID='" + ApplicationID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", question1='" + question1 + '\'' +
                ", question2='" + question2 + '\'' +
                ", status=" + status +
                ", UserID='" + UserID + '\'' +
                ", DogID='" + DogID + '\'' +
                '}';
    }
}
