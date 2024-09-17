package com.project.tourplanner.tour;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tours")
public class TourController {
    
    @Autowired
    private TourRepository tourRepository;

    @GetMapping
    public List<Tour> getAllTours(){
        return tourRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tour getTourByID(@PathVariable Long id){
        return tourRepository.findById(id).get();
    }
    
    @PostMapping
    public Tour createTour(@RequestBody Tour tour){
        return tourRepository.save(tour);
    }

    @PutMapping("/{id}")
    public Tour updateTour(@PathVariable Long id, @RequestBody Tour tour){
        Tour extistingTour = tourRepository.findById(id).get();
        extistingTour.setName(tour.getName());
        extistingTour.setTourDescription(tour.getTourDescription());
        extistingTour.setTourFrom(tour.getTourFrom());
        extistingTour.setTourTo(tour.getTourTo());
        extistingTour.setTransportType(tour.getTransportType());
        extistingTour.setTourDistance(tour.getTourDistance());
        extistingTour.setEstTime(tour.getEstTime());
        extistingTour.setRouteInfo(tour.getRouteInfo());

        return tourRepository.save(extistingTour);
    }

    @DeleteMapping("/{id}")
    public String deleteTour(@PathVariable Long id){
        try {
            tourRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {
            return "User not found";
        }
    }
}
