package com.mmrnd.lunchbuddy;

/**
 * Created by Esteban Sosa
 */

public class User {
    private String id;
    private String name;
    private String email;
    private String userPhoto;
    private String details;
    private int experience;

    public User(String id, String name, String email, String userPhoto, String details, int experience) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userPhoto = userPhoto;
        this.details = details;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
