package com.project.tourplanner.tour;

import jakarta.persistence.*;;

@Entity
@Table(name = "tours")
public class Tour {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tourDescription")
    private String tourDescription;

    @Column(name = "tourFrom")
    private String tourFrom;

    @Column(name = "tourTo")
    private String tourTo;

    @Column(name = "transportType")
    private String transportType;

    @Column(name = "tourDistance")
    private String tourDistance;

    @Column(name = "estTime")
    private String estTime;

    @Column(name = "routeInfo")
    private String routeInfo;

    //getters and setters

    public Long getID(){
        return id;
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
