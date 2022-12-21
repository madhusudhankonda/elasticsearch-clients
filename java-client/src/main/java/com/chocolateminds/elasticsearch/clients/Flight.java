package com.chocolateminds.elasticsearch.clients;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
public class Flight implements Serializable {
    private String name;
    private String route;
    private float duration_hours;
    private String airline;

    public Flight(){

    }
    Flight(String name, String route, String airline, float durationHours){
        this.name = name;
        this.route = route;
        this.airline = airline;
        this.duration_hours = durationHours;
    }
}
