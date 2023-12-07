package com.csis.drivedoctor.model;

import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;

import java.io.Serializable;

public class MyUserData implements Serializable {

    public MyUserData(boolean isServiceProvider) {
        this.isServiceProvider = isServiceProvider;
    }

    public MyUserData() {
    }

    String userId = DatabaseHelper.getInstance().getPref().getString("userId", "");
    String firstName;
    String lastName;
    String profileURL = "";
    String email;
    String documentId = "";
    String phone;
    String city;
    String address;
    boolean isServiceProvider;


    public String getEmail() {
        return email;
    }

    public boolean isServiceProvider() {
        return isServiceProvider;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileURL() {
        return profileURL;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //SETTER
    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public void setServiceProvider(boolean serviceProvider) {
        isServiceProvider = serviceProvider;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
