package com.HanifNurIlhamSanjayaJBusBR.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bus implements Serializable {
    public int id; // Assuming there is an ID field
    public int accountId;
    public String name;
    public List<Facility> facilities;
    public Price price;
    public int capacity;
    public BusType busType;
    public Station departure;
    public Station arrival;
    public List<Schedule> schedules;

    // Getter methods for assumed fields
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station getDepartureStation() {
        return departure;
    }

    public Station getDestination() {
        return arrival;
    }

    // Assuming these methods exist
    public int getAccountId() {
        return accountId;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public Price getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public BusType getBusType() {
        return busType;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public static List<Bus> sampleBusList(int size) {
        List<Bus> busList = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            Bus bus = new Bus();
            bus.id = i; // Set an ID for the sample buses
            bus.name = "Bus " + i;
            // Assuming other fields are initialized accordingly
            busList.add(bus);
        }

        return busList;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
