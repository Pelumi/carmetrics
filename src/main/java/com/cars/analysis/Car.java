package com.cars.analysis;

/**
 * @author Pelumi<pelumi@maven.ai>
 *         Created on 28/06/15 at 18:46.
 */
public class Car {
    private String name;
    private String price ;
    private String mileage = null;
    private String tankCapacity = null;
    private String gearType = null;
    private String fuelType = null;
    private String year = null;
    private String url;

    public Car(String name, String price, String mileage, String tankCapacity, String gearType, String fuelType, String year, String url) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.mileage = mileage;
        this.tankCapacity = tankCapacity;
        this.gearType = gearType;
        this.fuelType = fuelType;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(String tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public String getGearType() {
        return gearType;
    }

    public void setGearType(String gearType) {
        this.gearType = gearType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
