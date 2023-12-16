package com.example.joyjourney.model;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Pesanan {
    private String uid;
    private String wahanaId;
    private String wahanaImage;
    private boolean isUsed;
    private boolean isRefundable;
    private String name;
    private String wahanaName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String reservationHour;
    private String date;
    private int visitors;
    private String paymentMethod;
    private int totalPrice;

    public Pesanan(){}

    public String getUid() {
        return uid;
    }

    public void setIsRefundable(boolean isRefundable){
        this.isRefundable = isRefundable;
    }

    public boolean getIsRefundable(){
        return isRefundable;
    }

    public void setWahanaImage(String wahanaImage){
        this.wahanaImage = wahanaImage;
    }
    public String getWahanaImage(){
        return wahanaImage;
    }

    public void setWahanaName(String wahanaName){
        this.wahanaName = wahanaName;
    }
    public String getWahanaName(){
        return wahanaName;
    }
    public void setIsUsed(boolean isUsed){
        this.isUsed = isUsed;
    }
    public boolean getIsUsed(){
        return  isUsed;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWahanaId() {
        return wahanaId;
    }

    public void setWahanaId(String wahanaId) {
        this.wahanaId = wahanaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getReservationHour() {
        return reservationHour;
    }

    public void setReservationHour(String reservationHour) {
        this.reservationHour = reservationHour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
