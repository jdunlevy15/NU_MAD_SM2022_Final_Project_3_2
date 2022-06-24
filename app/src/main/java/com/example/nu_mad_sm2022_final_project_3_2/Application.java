package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Application implements Serializable {

    private String applicationID;
    private DogStatus applicationType;
    private String firstName;
    private String lastName;
    private int age;
    private Address address;
    private String email;
    private String phone;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private ApplicationStatus status;
    private String userID;
    private String dogID;

    public Application() {}


    public Application(String applicationID, DogStatus applicationType, String firstName, String lastName, int age, Address address, String email, String phone, String question1, String question2, String question3, String question4, String question5, ApplicationStatus status, String userID, String dogID) {
        this.applicationID = applicationID;
        this.applicationType = applicationType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.question4 = question4;
        this.question5 = question5;
        this.status = status;
        this.userID = userID;
        this.dogID = dogID;
    }

    public String getApplicationID() {
        return this.applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public DogStatus getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(DogStatus applicationType) {
        this.applicationType = applicationType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDogID() {
        return dogID;
    }

    public void setDogID(String dogID) {
        this.dogID = dogID;
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

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
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
                "applicationID='" + applicationID + '\'' +
                ", applicationType='" + applicationType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", question1='" + question1 + '\'' +
                ", question2='" + question2 + '\'' +
                ", question3='" + question3 + '\'' +
                ", question4='" + question4 + '\'' +
                ", question5='" + question5 + '\'' +
                ", status=" + status +
                ", UserID='" + userID + '\'' +
                ", DogID='" + dogID + '\'' +
                '}';
    }
}
