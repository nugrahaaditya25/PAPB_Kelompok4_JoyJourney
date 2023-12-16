package com.example.joyjourney.model;

import com.google.firebase.firestore.PropertyName;
import java.io.Serializable;

import java.util.List;

public class Wahana implements Serializable {
    private String name;
    private String imageUrl;
    private Double rating;
    private String id;
    private String description;
    private String address;
    private String mapsUrl;
    private List<String> facilities; // Change to List<String> for simplicity
    private int price;
    private int openDate;
    private int closedDate;
    private boolean isRefundable;

    public Wahana() {
        // Default constructor required for Firestore
    }

    public Wahana(String name, String imageUrl, Double rating, String description, String address,
                  String mapsUrl, List<String> facilities, int price, int openDate, int closedDate,
                  String wahanaId,
                  boolean isRefundable) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = wahanaId;
        this.rating = rating;
        this.description = description;
        this.address = address;
        this.mapsUrl = mapsUrl;
        this.facilities = facilities;
        this.price = price;
        this.openDate = openDate;
        this.closedDate = closedDate;
        this.isRefundable = isRefundable;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("id")
    public  String getId(){
        return id;
    }

    public void setId(String firestoreId){
        id = firestoreId;
    }

    @PropertyName("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setIimageUrl(String url){
        this.imageUrl = url;
    }

    @PropertyName("rating")
    public Double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMapsUrl(String mapsUrl) {
        this.mapsUrl = mapsUrl;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("address")
    public String getAddress() {
        return address;
    }

    @PropertyName("mapsUrl")
    public String getMapsUrl() {
        return mapsUrl;
    }

    @PropertyName("facilities")
    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    @PropertyName("price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @PropertyName("openDate")
    public int getOpenDate() {
        return openDate;
    }

    public void setOpenDate(int openDate) {
        this.openDate = openDate;
    }

    @PropertyName("closedDate")
    public int getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(int closedDate) {
        this.closedDate = closedDate;
    }

    @PropertyName("isRefundable")
    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }
}
