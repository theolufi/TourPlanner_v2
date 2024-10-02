package com.project.tourplanner.tour;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tourlogs")
public class TourLogController {
    
    @Autowired
    private TourLogRepository tourLogRepository;

    @GetMapping
    public List<TourLog> getAllTourLogs() {
        return tourLogRepository.findAll();
    }

    @GetMapping("tourlogs/{id}")
    public TourLog getTourLogByID(@PathVariable Long id) {
        return tourLogRepository.findById(id).orElse(null);
    }
    
    @PostMapping("/createTourLog")
    public TourLog createTourLog(@RequestBody TourLog tourLog) {
        return tourLogRepository.save(tourLog);
    }

    @PutMapping("/{id}")
    public TourLog updateTourLog(@PathVariable Long id, @RequestBody TourLog tourLog) {
        TourLog existingTourLog = tourLogRepository.findById(id).orElse(null);
        if (existingTourLog != null) {
            existingTourLog.setTourID(tourLog.getTourID());
            existingTourLog.setDate(tourLog.getDate());
            existingTourLog.setTime(tourLog.getTime());
            existingTourLog.setComment(tourLog.getComment());
            existingTourLog.setDifficulty(tourLog.getDifficulty());
            existingTourLog.setTotalDistance(tourLog.getTotalDistance());
            existingTourLog.setTotalTime(tourLog.getTotalTime());
            existingTourLog.setRating(tourLog.getRating());

            return tourLogRepository.save(existingTourLog);
        } else {
            // Handle the case when TourLog is not found
            return null;
        }
    }

    @DeleteMapping("deleteTourLog/{id}")
    public String deleteTourLog(@PathVariable Long id) {
        try {
            tourLogRepository.deleteById(id);
            return "TourLog deleted successfully";
        } catch (Exception e) {
            return "TourLog not found";
        }
    }
}
