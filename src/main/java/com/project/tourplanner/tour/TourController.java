package com.project.tourplanner.tour;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.MediaType;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tours")
public class TourController {
    private static final Logger logger = Logger.getLogger(TourController.class.getName());

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
    
    @PostMapping("/createTour")
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

    @DeleteMapping("/deleteTour/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        logger.info("Tour with id " + id + " deleted!");
        tourRepository.deleteById(id);
        return "Tour deleted";
    }

    @GetMapping("/tourReport/{id}")
    public ResponseEntity<InputStreamResource> generateReportForSingleTour(@PathVariable Long id) throws IOException {
        Optional<Tour> tourOptional = tourRepository.findById(id);
        if (!tourOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Tour tour = tourOptional.get();
        byte[] pdfBytes = TourService.tourReport(tour);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TourReport_" + tour.getID() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/summarizeReport")
    public ResponseEntity<InputStreamResource> generateReport() throws IOException {
        List<Tour> tourList = tourRepository.findAll();
        byte[] pdfBytes = TourService.summarizeReport(tourList);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TourReport.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
