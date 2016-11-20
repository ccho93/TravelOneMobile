package com.app.gillime.travelone;

/**
 * Created by Charles on 11/20/16.
 */

public class Request {
    private String hotel;
    private String location;
    private int price;

    public Request() {
    }

    public Request(String hotel, String location, int price) {
        this.hotel = hotel;
        this.location = location;
        this.price = price;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
