package com.lelininkas.budgetjourney;

public class PointOfInterest {
    private String name;
    private String info;
    private int cost;
    private int distance;
    private String mapUrl;

    public PointOfInterest(String name, String info, int cost, int distance, String mapUrl) {
        this.name = name;
        this.info = info;
        this.cost = cost;
        this.distance = distance;
        this.mapUrl = mapUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public int getDistance() {
        return distance;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    @Override
    public String toString() {
        return "PointOfInterest [name=" + name + ", info=" + info + ", cost=" + cost + "]";
    }
}

