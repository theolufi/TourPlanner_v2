package com.project.tourplanner.tour;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;;

@Entity
@Table(name = "tourLogs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TourLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tourID")
    private Long tourID;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String time;

    @Column(name = "comment")
    private String comment;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "totalDistance")
    private String totalDistance;

    @Column(name = "totalTime")
    private String totalTime;

    @Column(name = "rating")
    private String rating;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTourID() {
        return tourID;
    }

    public void setTourID(Long tourID) {
        this.tourID = tourID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
