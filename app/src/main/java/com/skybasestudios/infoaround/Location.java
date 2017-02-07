package com.skybasestudios.infoaround;

/**
 * Created by Code on 18/01/2017.
 */

public class Location {
    String latitude;

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    String longitude;
    String id;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    String event_id;

    public Location() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
