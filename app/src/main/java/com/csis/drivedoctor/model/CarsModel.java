package com.csis.drivedoctor.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;

import java.io.InputStream;
import java.io.Serializable;

public class CarsModel implements Serializable {


    String company, carNo, carName;
    String modelyear, carId;
    String userId = DatabaseHelper.getInstance().getPref().getString("userId", "");
    String documentId;

    public CarsModel(String company, String carNo, String carName, String modelyear) {
        this.company = company;
        this.carNo = carNo;
        this.carName = carName;
        this.modelyear = modelyear;
        this.carId = userId +"car" + DatabaseHelper.getInstance().carsList.size()+1;
    }

    public CarsModel() {
    }


    public void setModelyear(String modelyear) {
        this.modelyear = modelyear;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    //Getters
    public String getCarId() {
        return carId;
    }

    public String getCarNo() {
        return carNo;
    }

    public String getCarName() {
        return carName;
    }

    public String getCompany() {
        return company;
    }

    public String getModelyear() {
        return modelyear;
    }

    public String getUserId() {
        return userId;
    }


    public Bitmap getCarImage(Context cntxt) {
        try {
            InputStream inputStream =  cntxt.getAssets().open(company+".png");
            return BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

}
