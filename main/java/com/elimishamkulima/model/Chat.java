package com.elimishamkulima.model;

public class Chat {
    public String timeStamp;
    public String name;

    public Chat(){

    }

    public Chat(String timeStamp, String name) {
        this.timeStamp = timeStamp;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
