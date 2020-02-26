package com.example.spacexsynopsis;

import java.util.ArrayList;

public class Payload {
    private String payloadId;
    private String nationality;
    private String manufacturer;
    private String payloadType;
    private String payloadMass;
    private ArrayList<String> customers;

    public String getPayloadId() {
        return payloadId;
    }

    public String getNationality() {
        return nationality;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public String getPayloadMass() {
        return payloadMass;
    }

    public ArrayList<String> getCustomers() {
        return customers;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public void setPayloadMass(String payloadMass) {
        this.payloadMass = payloadMass;
    }

    public void setCustomers(ArrayList<String> customers) {
        this.customers = customers;
    }
}
