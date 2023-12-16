package com.example.joyjourney.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String uid;
    private String name;
    private String date;
    private Gender gender;
    private String phoneNumber;
    private Boolean isAdmin;
    private List<Pesanan> pesananList;

    public User() {
        // Required empty public constructor for Firestore
    }

    public User(String uid, String name, String date, Gender gender, String phoneNumber, Boolean isAdmin, List<Pesanan> pesananList) {
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
        this.pesananList = pesananList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public List<Pesanan> getPesananList() {
        return pesananList;
    }

    public void setPesananList(List<Pesanan> pesananList) {
        this.pesananList = pesananList;
    }

    public enum Gender {
        MALE, FEMALE
    }
}
