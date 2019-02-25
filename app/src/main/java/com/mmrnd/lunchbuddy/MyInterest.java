package com.mmrnd.lunchbuddy;

/**
 * Created by Esteban Sosa
 */

public class MyInterest {

    // Variables
    private String title;
    private int level;
    private String details;

    // Constructor
    public MyInterest(String title, int level, String details) {
        this.title = title;
        this.level = level;
        this.details = details;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
