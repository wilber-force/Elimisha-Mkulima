package com.elimishamkulima.model;

import java.io.Serializable;

public class Trainings implements Serializable {
    String title, start_date, end_date, published_on, description;

    public Trainings() {
        //Need empty constructor
    }

    public Trainings(String title, String start_date, String end_date, String published_on, String description) {
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.published_on = published_on;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPublished_on() {
        return published_on;
    }

    public void setPublished_on(String published_on) {
        this.published_on = published_on;
    }
}
