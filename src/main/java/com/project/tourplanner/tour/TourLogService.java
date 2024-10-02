package com.project.tourplanner.tour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourLogService {

    @Autowired
    private TourLogDtoMapper tourLogDtoMapper;

    @Autowired
    private TourLogRepository tourLogRepository;

    public TourLogDto getTourLogs() {
        TourLog tourLog = new TourLog(
                755L,           // Tour ID
                "12.05.2024",   // Date
                "12:56",        // Time
                "Very good tour", // Comment
                "Easy",         // Difficulty
                "10km",         // Total Distance
                "1h",           // Total Time
                "5"             // Rating
        );
    
        return tourLogDtoMapper.mapTourLogToTourLogDto(tourLog);
    }
    
}