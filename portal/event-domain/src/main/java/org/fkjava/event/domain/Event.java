package org.fkjava.event.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class Event {

    private String event;
    private String userId;
    private Date time;

    public Event(){
    }

    public Event(String event) {
        this(event, null, null);
    }

    public Event(String event, Date time) {
        this(event, time, null);
    }

    public Event(String event, Date time, String userId) {
        this.event = event;
        this.time = time;
        this.userId = userId;
    }
}
