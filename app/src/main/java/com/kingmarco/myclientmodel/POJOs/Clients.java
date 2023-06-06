package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**The class Clients that is used to organize the information of the client and parcel it to pass to others fragments*/
public class Clients {

    private String id;
    private int messagingId;
    //Personal information
    private String imageUrl;
    private String name;
    private String lastName;
    private String documentID;
    private String documentType;
    private int age;
    private String phoneNumber;

    //Location Data
    private Double latitude;
    private Double longitude;
    private String directions;

    public Clients() {
    }

    public Clients(String id, int messagingId, String imageUrl, String name, String lastName, String documentID, String documentType, int age, String phoneNumber, Double latitude, Double longitude, String directions) {
        this.id = id;
        this.messagingId = messagingId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.lastName = lastName;
        this.documentID = documentID;
        this.documentType = documentType;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.directions = directions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMessagingId() {
        return messagingId;
    }

    public void setMessagingId(int messagingId) {
        this.messagingId = messagingId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }
}
