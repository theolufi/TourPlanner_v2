package com.project.tourplanner;

import com.project.tourplanner.tour.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TourplannerApplicationTests {

    @Autowired
    TourRepository tourRepository;

    @Autowired
    TourLogRepository tourLogRepository;

    @Autowired
    TourDtoMapper tourDtoMapper;

    @Autowired
    TourLogDtoMapper tourLogDtoMapper;

    private Tour testTour;
    private TourLog testTourLog;

    @BeforeEach
    void setUp() {
        // Creating a valid Tour and TourLog for testing
        testTour = new Tour(
                "Sample Tour",
                "This is a description.",
                "Start Location",
                "End Location",
                "Bus",
                "5km",
                "1h",
                "Sample Route Information"
        );

        testTourLog = new TourLog(
                1, // Assuming tour ID is valid for the test
                "Sample Log Entry",
                "example",
                "example",
                "example",
                "example",
                "Notes",
                "2024-10-01" // Using a valid date format
        );
    }

    // Tour Tests
    @Test
    void shouldReturnTourRowCount() {
        System.out.println("There are " + tourRepository.count() + " tours!");
    }

    @Test
    void shouldCreateTourSuccessfully() {
        tourRepository.save(testTour);
        assertNotNull(testTour.getID()); // Assuming the ID is auto-generated
        assertEquals("Sample Tour", testTour.getName());
        System.out.println("Now there are " + tourRepository.count() + " tours!");
    }

    @Test
    public void shouldMapTourToTourDtoSuccessfully() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);

        assertEquals(testTour.getName(), tourDto.getName());
        assertEquals(testTour.getTourDescription(), tourDto.getTourDescription());
    }

    @Test
    public void shouldMapTourDtoToTourSuccessfully() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        Tour tour = tourDtoMapper.mapTourDtoToTour(tourDto);

        assertEquals(tour.getName(), tourDto.getName());
        assertEquals(tour.getTourDescription(), tourDto.getTourDescription());
    }

    @Test
    public void shouldGetAllTours() {
        List<Tour> tours = tourRepository.findAll();
        for (Tour tour : tours) {
            System.out.println("--- Tour: " + tour.getName() + " ---");
        }
    }

    @Test
    public void shouldHaveValidName() {
        assertNotNull(testTour.getName());
    }

    @Test
    public void shouldHaveSameNameWithTourDto() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getName(), tourDto.getName());
    }

    @Test
    public void shouldHaveValidFrom() {
        assertNotNull(testTour.getTourFrom());
    }

    @Test
    public void shouldHaveValidTo() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertNotNull(tourDto.getTourTo());
    }

    @Test
    public void shouldHaveSameTransportType() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getTransportType(), tourDto.getTransportType());
    }

    @Test
    public void shouldHaveValidTourDistance() {
        assertNotNull(testTour.getTourDistance());
    }

    @Test
    public void shouldHaveValidEstimatedTime() {
        assertNotNull(testTour.getEstTime());
    }

    @Test
    public void shouldHaveSameTourDistanceWithDto() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getTourDistance(), tourDto.getTourDistance());
    }

    @Test
    public void shouldHaveSameEstimatedTimeWithDto() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getEstTime(), tourDto.getEstTime());
    }

    @Test
    public void shouldHaveSameFromWithDto() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getTourFrom(), tourDto.getTourFrom());
    }

    @Test
    public void shouldHaveSameToWithDto() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getTourTo(), tourDto.getTourTo());
    }

    @Test
    public void shouldHaveValidTransportType() {
        assertNotNull(testTour.getTransportType());
    }

    @Test
    public void shouldHaveSameTourDescriptionWithDto() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(testTour);
        assertEquals(testTour.getTourDescription(), tourDto.getTourDescription());
    }

    // Tour Log Tests
    @Test
    void shouldReturnTourLogRowCount() {
        System.out.println("There are " + tourLogRepository.count() + " tour logs!");
    }

    @Test
    void shouldCreateTourLogSuccessfully() {
        tourLogRepository.save(testTourLog);
        assertNotNull(testTourLog.getId()); // Assuming the ID is auto-generated
        System.out.println("Now there are " + tourLogRepository.count() + " tour logs!");
    }

    @Test
    public void shouldMapTourLogToTourLogDtoSuccessfully() {
        TourLogDto tourLogDto = tourLogDtoMapper.mapTourLogToTourLogDto(testTourLog);

        assertEquals(testTourLog.getId(), tourLogDto.getId());
        assertEquals(testTourLog.getComment(), tourLogDto.getComment());
    }

    @Test
    public void shouldMapTourLogDtoToTourLogSuccessfully() {
        TourLogDto tourLogDto = tourLogDtoMapper.mapTourLogToTourLogDto(testTourLog);
        TourLog tourLog = tourLogDtoMapper.mapTourLogDtoToTourLog(tourLogDto);

        assertEquals(tourLog.getId(), tourLogDto.getId());
        assertEquals(tourLog.getComment(), tourLogDto.getComment());
    }

    @Test
    public void shouldGetAllTourLogs() {
        List<TourLog> tourLogs = tourLogRepository.findAll();
        for (TourLog tourLog : tourLogs) {
            System.out.println("--- TourLog: " + tourLog.getDate() + " ---");
        }
    }

    @Test
    public void shouldHaveValidTourId() {
        assertNotNull(testTourLog.getId());
    }

    @Test
    public void shouldHaveSameTourIdWithDto() {
        TourLogDto tourLogDto = tourLogDtoMapper.mapTourLogToTourLogDto(testTourLog);
        assertEquals(testTourLog.getId(), tourLogDto.getId());
    }

    @Test
    public void shouldHaveValidDate() {
        assertNotNull(testTourLog.getDate());
    }

    @Test
    public void shouldHaveSameDateWithDto() {
        TourLogDto tourLogDto = tourLogDtoMapper.mapTourLogToTourLogDto(testTourLog);
        assertEquals(testTourLog.getDate(), tourLogDto.getDate());
    }

    @Test
    public void shouldHaveValidNotes() {
        assertNotNull(testTourLog.getComment());
    }

    @Test
    public void shouldHaveValidComment() {
        TourLogDto tourLogDto = tourLogDtoMapper.mapTourLogToTourLogDto(testTourLog);
        assertNotNull(tourLogDto.getComment());
    }

    @Test
    public void shouldHaveSameNotesWithDto() {
        TourLogDto tourLogDto = tourLogDtoMapper.mapTourLogToTourLogDto(testTourLog);
        assertEquals(testTourLog.getComment(), tourLogDto.getComment());
    }
}
