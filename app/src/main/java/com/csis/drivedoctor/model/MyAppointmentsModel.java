package com.csis.drivedoctor.model;

import com.csis.drivedoctor.Helpers.ModelHelper;

import java.io.Serializable;

public class MyAppointmentsModel implements Serializable {

    String userId = ModelHelper.getInstance().getUser().getUserId();
    String userName = ModelHelper.getInstance().getUser().getFirstName() + " " + ModelHelper.getInstance().getUser().getLastName();

    String providerName;
    String providerID;//

    String carID;//
    String carmodel;
    String carNamePlate;
    String carCompany;


    String appointmentID;//
    String date;//
    String description;//
    double amount = 0;//
    String documentId = "";
    String serviceType = "";
    boolean isServiceCompleted = false;
    boolean isDropOffNeeded = false;
    boolean isPickupNeeded = false;

    public boolean isDropOffNeeded() {
        return isDropOffNeeded;
    }

    public void setDropOffNeeded(boolean dropOffNeeded) {
        isDropOffNeeded = dropOffNeeded;
    }

    public boolean isPickupNeeded() {
        return isPickupNeeded;
    }

    public void setPickupNeeded(boolean pickupNeeded) {
        isPickupNeeded = pickupNeeded;
    }

    public MyAppointmentsModel() {
    }

    public MyAppointmentsModel(String providerName,
                               String providerID,
                               String carID,
                               String carmodel,
                               String carComapny,
                               String carNamePlate,
                               String appointmentID,
                               String date,
                               String description,
                               String serviceType) {
        this.userName = userName;
        this.providerName = providerName;
        this.providerID = providerID;
        this.carID = carID;
        this.carCompany = carComapny;
        this.carmodel = carmodel;
        this.carNamePlate = carNamePlate;
        this.appointmentID = appointmentID;
        this.date = date;
        this.description = description;
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public String getCarCompany() {
        return carCompany;
    }

    public void setCarCompany(String carCompany) {
        this.carCompany = carCompany;
    }

    public String getCarNamePlate() {
        return carNamePlate;
    }

    public void setCarNamePlate(String carNamePlate) {
        this.carNamePlate = carNamePlate;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getProviderID() {
        return providerID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getDescription() {
        return description;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isServiceCompleted() {
        return isServiceCompleted;
    }

    public void setServiceCompleted(boolean serviceCompleted) {
        isServiceCompleted = serviceCompleted;
    }
}
