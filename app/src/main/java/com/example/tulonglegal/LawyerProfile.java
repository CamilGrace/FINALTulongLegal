package com.example.tulonglegal;

import java.io.Serializable;

public class LawyerProfile implements Serializable {
    private String fullName;
    private String contactNo;
    private String email;
    private String username;
    private String address;  // New field for address
    private String gender;  // New field for gender
    private int age;  // New field for age
    private String availability;  // New field for availability
    private String legalspecialization;  // New field for legal specialization
    private String prelawdegree;  // New field for pre-law degree
    private String lawschool;  // New field for law school
    private int yearsofexp;  // New field for years of experience
    private int consultationfee;  // New field for consultation fee

    // Default constructor (required for Firestore)
    public LawyerProfile() {
    }

    // Parameterized constructor
    public LawyerProfile(String fullName, String contactNo, String email, String username, String address,
                         String gender, int age, String availability, String legalspecialization,
                         String prelawdegree, String lawschool, int yearsofexp, int consultationfee) {
        this.fullName = fullName;
        this.contactNo = contactNo;
        this.email = email;
        this.username = username;
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.availability = availability;
        this.legalspecialization = legalspecialization;
        this.prelawdegree = prelawdegree;
        this.lawschool = lawschool;
        this.yearsofexp = yearsofexp;
        this.consultationfee = consultationfee;
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLegalspecialization() {
        return legalspecialization;
    }

    public void setLegalspecialization(String legalspecialization) {
        this.legalspecialization = legalspecialization;
    }

    public String getPrelawdegree() {
        return prelawdegree;
    }

    public void setPrelawdegree(String prelawdegree) {
        this.prelawdegree = prelawdegree;
    }

    public String getLawschool() {
        return lawschool;
    }

    public void setLawschool(String lawschool) {
        this.lawschool = lawschool;
    }

    public int getYearsofexp() {
        return yearsofexp;
    }

    public void setYearsofexp(int yearsofexp) {
        this.yearsofexp = yearsofexp;
    }

    public int getConsultationfee() {
        return consultationfee;
    }

    public void setConsultationfee(int consultationfee) {
        this.consultationfee = consultationfee;
    }

    @Override
    public String toString() {
        return "LawyerProfile{" +
                "fullName='" + fullName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", availability='" + availability + '\'' +
                ", legalspecialization='" + legalspecialization + '\'' +
                ", prelawdegree='" + prelawdegree + '\'' +
                ", lawschool='" + lawschool + '\'' +
                ", yearsofexp=" + yearsofexp +
                ", consultationfee=" + consultationfee +
                '}';
    }
}
