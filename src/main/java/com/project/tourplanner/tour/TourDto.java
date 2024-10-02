package com.project.tourplanner.tour;


public class TourDto {

private Long id;
private String name;
private String tourDescription;
private String tourFrom;
private String tourTo;
private String transportType;
private String tourDistance;
private String estTime;
private String routeInfo;

// Constructors
public TourDto() {
}

public TourDto(Long id, String name, String tourDescription, String tourFrom, String tourTo,
               String transportType, String tourDistance, String estTime, String routeInfo) {
    this.id = id;
    this.name = name;
    this.tourDescription = tourDescription;
    this.tourFrom = tourFrom;
    this.tourTo = tourTo;
    this.transportType = transportType;
    this.tourDistance = tourDistance;
    this.estTime = estTime;
    this.routeInfo = routeInfo;
}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public void setTourDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public String getTourFrom() {
        return tourFrom;
    }

    public void setTourFrom(String tourFrom) {
        this.tourFrom = tourFrom;
    }

    public String getTourTo() {
        return tourTo;
    }

    public void setTourTo(String tourTo) {
        this.tourTo = tourTo;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTourDistance() {
        return tourDistance;
    }

    public void setTourDistance(String tourDistance) {
        this.tourDistance = tourDistance;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }
}
    

