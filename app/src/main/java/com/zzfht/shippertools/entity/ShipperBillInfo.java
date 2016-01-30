package com.zzfht.shippertools.entity;

/**
 * Created by think on 2016-01-12.
 */
public class ShipperBillInfo {
    private String plateNumber;
    private String destination;
    private String media;
    private String date;

    public ShipperBillInfo() {
    }

    public ShipperBillInfo(String plateNumber, String destination, String media, String date) {
        this.plateNumber = plateNumber;
        this.destination = destination;
        this.media = media;
        this.date = date;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
