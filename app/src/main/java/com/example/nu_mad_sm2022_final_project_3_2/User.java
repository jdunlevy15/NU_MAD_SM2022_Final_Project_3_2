package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Basic profile information for authenticated users of the app
 */

@Keep
public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private int Age;
    private Address address;
    private String phoneNumber;
    private String email;
    private String username;

    public String getUsername() {
        return username;
    }

    public User(String id, String firstName, String lastName, int age, Address address,
                String phoneNumber, String email, String username, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        Age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public User () {}

    public void setUsername(String username) {
        this.username = username;
    }

    private Role role;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
