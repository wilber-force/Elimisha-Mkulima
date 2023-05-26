package com.elimishamkulima.model;


import java.io.Serializable;

public class Farmers implements Serializable {

    String email, phone_number, full_name, id_number, farm_activities, location, profile, uid;

    public Farmers() {
    }

    public Farmers(String email, String uid, String phone_number, String full_name, String id_number, String farm_activities, String location, String profile) {
        this.email = email;
        this.uid = uid;
        this.phone_number = phone_number;
        this.full_name = full_name;
        this.id_number = id_number;
        this.farm_activities = farm_activities;
        this.location = location;
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getFarm_activities() {
        return farm_activities;
    }

    public void setFarm_activities(String farm_activities) {
        this.farm_activities = farm_activities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
