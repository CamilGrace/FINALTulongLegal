package com.example.tulonglegal;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private String fullName;
    private String contactNo;
    private String email;
    private String username;
    private String address;  // New field for address

    // Default constructor (required for Firestore)
    public UserProfile() {
    }

    // Parameterized constructor
    public UserProfile(String fullName, String contactNo, String email, String username, String address) {
        this.fullName = fullName;
        this.contactNo = contactNo;
        this.email = email;
        this.username = username;
        this.address = address;  // Initialize address
    }

    // Getter and Setter methods
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;  // Getter for address
    }

    public void setAddress(String address) {
        this.address = address;  // Setter for address
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "fullName='" + fullName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +  // Include address in toString
                '}';
    }
}
