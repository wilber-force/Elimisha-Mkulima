package com.elimishamkulima.model;

import java.io.Serializable;

public class Farm implements Serializable {
    String title, publish_date, description;


    public Farm() {
        //Need empty constructor
    }

    public Farm(String title, String publish_date, String description) {
        this.title = title;
        this.publish_date = publish_date;
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

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }
}
