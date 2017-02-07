package com.skybasestudios.infoaround;

/**
 * Created by Code on 18/01/2017.
 */

public class Event {
    String event_id, event_creator_id, event_start_time, event_end_time,
            event_type, event_timestamp, event_title, event_description;
    double event_latitude;
    double event_longitude;


    public Event() {
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_creator_id() {
        return event_creator_id;
    }

    public void setEvent_creator_id(String event_creator_id) {
        this.event_creator_id = event_creator_id;
    }

    public String getEvent_start_time() {
        return event_start_time;
    }

    public void setEvent_start_time(String event_start_time) {
        this.event_start_time = event_start_time;
    }

    public String getEvent_end_time() {
        return event_end_time;
    }

    public void setEvent_end_time(String event_end_time) {
        this.event_end_time = event_end_time;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(String event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public double getEvent_latitude() {
        return event_latitude;
    }

    public void setEvent_latitude(double event_latitude) {
        this.event_latitude = event_latitude;
    }

    public double getEvent_longitude() {
        return event_longitude;
    }

    public void setEvent_longitude(double event_longitude) {
        this.event_longitude = event_longitude;
    }
}


