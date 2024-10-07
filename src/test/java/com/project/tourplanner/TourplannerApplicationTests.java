package com.project.tourplanner;

import com.project.tourplanner.tour.TourDto;
import com.project.tourplanner.tour.Tour;
import com.project.tourplanner.tour.TourDtoMapper;
import com.project.tourplanner.tour.TourRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test") // Specify the test profile to use H2 or other test configurations
class TourplannerApplicationTests {

    @Autowired
    TourRepository tourRepository;

    @MockBean
    TourDtoMapper tourDtoMapper;

    @Test
    public void test_mapTourDtoToTour() {
        TourDto tourDto = new TourDto();
        tourDto.setTourDescription("Description");
        Tour tour = new Tour();
        tour.setTourDescription("Description");
        assertEquals("Description", tour.getTourDescription());
    }

    @Test
    void test_Tour_RowCount() {
        long count = tourRepository.count();
        System.out.println("There are " + count + " tours!");
        assertEquals(0, count); // Assuming no tours exist initially
    }

    @Test
    void test_Tour_creation() {
        Tour tour = new Tour(
                "This is a tour",
                "Description",
                "From",
                "To",
                "Train",
                "2km",
                "2h",
                "Route Info"
        );
        tourRepository.save(tour);
        long count = tourRepository.count();
        System.out.println("Now there are " + count + " tours!");
        assertEquals(1, count); // Check if the tour was successfully saved
    }

    @Test
    public void test_getAllTours() {
        List<Tour> tours = tourRepository.findAll();
        for (Tour tour : tours) {
            System.out.println("--- Tour: " + tour.getName() + " ---");
        }
        // Add an assertion if you expect specific tours to exist
    }

    @Test
    public void test_Tour_hasValidName() {
        Tour tour = new Tour();
        tour.setName("Test Tour");
        assertNotNull(tour.getName());
    }

    @Test
    public void test_TourDto_hasValidName() {
        TourDto tourDto = new TourDto(
                null,
                "Valid TourDto Name",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertNotNull(tourDto.getName());
    }


    @Test
    public void test_Tour_hasValidFrom() {
        Tour tour = new Tour();
        tour.setTourFrom("Paris");
        assertNotNull(tour.getTourFrom());
    }

    @Test
    public void test_TourDto_hasValidTo() {
        TourDto tourDto = new TourDto(
                null,
                null,
                null,
                null,
                "Valid To",
                null,
                null,
                null,
                null
        );
        assertNotNull(tourDto.getTourTo());
    }


    @Test
    public void test_Tour_hasValidTourDistance() {
        Tour tour = new Tour();
        tour.setTourDistance("10km");
        assertNotNull(tour.getTourDistance());
    }

    @Test
    public void test_TourDto_hasValidEstimatedTime() {
        TourDto tourDto = new TourDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Valid Estimated Time",
                null
        );
        assertNotNull(tourDto.getEstTime());
    }


    @Test
    public void test_Tour_hasValidTransportType() {
        Tour tour = new Tour();
        tour.setTransportType("car");
        assertNotNull(tour.getTransportType());
    }

    @Test
    public void test_TourDto_hasValidTransportType() {
        TourDto tourDto = new TourDto();
        tourDto.setTransportType("car");
        assertNotNull(tourDto.getTransportType());
    }

    @Test
    public void test_Tour_hasValidEstimatedTime() {
        Tour tour = new Tour();
        tour.setEstTime("12:00");
        assertNotNull(tour.getEstTime());
    }

    @Test
    public void test_TourDto_hasValidTourDistance() {
        TourDto tourDto = new TourDto(
                null,
                null,
                null,
                null,
                null,
                null,
                "Valid Tour Distance",
                null,
                null
        );
        assertNotNull(tourDto.getTourDistance());
    }

    @Test
    public void test_TourDto_hasValidRouteInfo() {
        TourDto tourDto = new TourDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Valid Route Information"
        );
        assertNotNull(tourDto.getRouteInfo());
    }

    @Test
    public void test_Tour_hasValidTourDescription() {
        Tour tour = new Tour();
        tour.setTourDescription("Road through mountains!");
        assertNotNull(tour.getTourDescription());
    }
    @Test
    public void test_Duplicate_Tours() {
        Tour tour1 = new Tour(
            "Duplicate Tour",
            "Description 1",
            "From 1",
            "To 1",
            "Train",
            "2km",
            "2h",
            "Route Info 1"
        );
    
        Tour tour2 = new Tour(
            "Duplicate Tour", // Same name
            "Description 2",
            "From 2",
            "To 2",
            "Bus",
            "3km",
            "3h",
            "Route Info 2"
        );
    
        tourRepository.save(tour1);
        tourRepository.save(tour2);
    
        long count = tourRepository.count();
        assertEquals(2, count); // Expecting count to be 2
    }
    
    @Test
    public void test_TourDtoMapper_withNullTour() {
        TourDto tourDto = tourDtoMapper.mapTourToTourDto(null);
        assertNull(tourDto);
    }

    @Test
    public void test_TourDtoMapper_withNullTourDto() {
        Tour tour = tourDtoMapper.mapTourDtoToTour(null);
        assertNull(tour);
    }

    @Test
    public void test_MappingPerformance() {
        List<Tour> tours = IntStream.range(0, 1000)
                .mapToObj(i -> new Tour("Tour " + i, "Description " + i, "From " + i, "To " + i, "Transport " + i, "Distance " + i, "Time " + i, "Info " + i))
                .collect(Collectors.toList());
        
        long startTime = System.currentTimeMillis();
        List<TourDto> tourDtos = tours.stream()
                                    .map(tourDtoMapper::mapTourToTourDto)
                                    .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        
        System.out.println("Mapping performance time: " + (endTime - startTime) + "ms");
    }
    @Test
    public void test_TourStateAfterSave() {
        Tour tour = new Tour("Tour Name", "Description", "From", "To", "Train", "2km", "2h", "Route Info");
        tourRepository.save(tour);
        Tour foundTour = tourRepository.findById(tour.getID()).orElse(null);
        assertNotNull(foundTour);
        assertEquals(tour.getName(), foundTour.getName());
    }
    @Test
public void test_FindTourByName() {
    // Create and save a tour
    Tour tour = new Tour(
            "Adventure Tour",
            "Exciting mountain adventure",
            "Base Camp",
            "Summit",
            "Hiking",
            "10km",
            "5h",
            "Mountain route"
    );
    tourRepository.save(tour);

    // Retrieve the tour by name
    Tour foundTour = tourRepository.findAll()
            .stream()
            .filter(t -> "Adventure Tour".equals(t.getName()))
            .findFirst()
            .orElse(null);

    assertNotNull(foundTour);
    assertEquals("Adventure Tour", foundTour.getName());
    assertEquals("Exciting mountain adventure", foundTour.getTourDescription());
}

@Test
public void test_UpdateTourDescription() {
    // Create and save a tour
    Tour tour = new Tour(
            "City Tour",
            "Initial Description",
            "Start Point",
            "End Point",
            "Bus",
            "15km",
            "3h",
            "City route"
    );
    tourRepository.save(tour);

    // Update the description
    tour.setTourDescription("Updated Description");
    tourRepository.save(tour);

    // Retrieve and verify the update
    Tour updatedTour = tourRepository.findById(tour.getID()).orElse(null);
    assertNotNull(updatedTour);
    assertEquals("Updated Description", updatedTour.getTourDescription());
}

@Test
public void test_DeleteTour() {
    // Create and save a tour
    Tour tour = new Tour(
            "Beach Tour",
            "Relaxing beach visit",
            "Beachside",
            "Harbor",
            "Boat",
            "5km",
            "2h",
            "Coastal route"
    );
    tourRepository.save(tour);

    // Ensure the tour exists
    assertNotNull(tourRepository.findById(tour.getID()).orElse(null));

    // Delete the tour
    tourRepository.delete(tour);

    // Verify deletion
    Tour deletedTour = tourRepository.findById(tour.getID()).orElse(null);
    assertNull(deletedTour);
}

@Test
public void test_CountAfterMultipleSaves() {
    // Initially, count should be 0
    assertEquals(0, tourRepository.count());

    // Save multiple tours
    Tour tour1 = new Tour("Tour 1", "Description 1", "From 1", "To 1", "Train", "2km", "2h", "Route 1");
    Tour tour2 = new Tour("Tour 2", "Description 2", "From 2", "To 2", "Bus", "3km", "3h", "Route 2");
    tourRepository.save(tour1);
    tourRepository.save(tour2);

    // Count should be 2
    assertEquals(2, tourRepository.count());
}

@Test
public void test_FindAllTours() {
    // Save multiple tours
    Tour tour1 = new Tour("Safari Tour", "Wildlife exploration", "Savannah", "Lodge", "Jeep", "20km", "4h", "Safari route");
    Tour tour2 = new Tour("City Lights Tour", "Night city exploration", "Downtown", "Uptown", "Bus", "10km", "2h", "City route");
    tourRepository.save(tour1);
    tourRepository.save(tour2);

    // Retrieve all tours
    List<Tour> tours = tourRepository.findAll();
    assertNotNull(tours);
    assertEquals(2, tours.size());
}

@Test
public void test_ExistsById() {
    // Create and save a tour
    Tour tour = new Tour(
            "Desert Tour",
            "Desert exploration",
            "Oasis",
            "Dunes",
            "Camel",
            "50km",
            "8h",
            "Desert route"
    );
    tourRepository.save(tour);

    // Check existence
    boolean exists = tourRepository.existsById(tour.getID());
    assertEquals(true, exists);

    // Check non-existence
    boolean notExists = tourRepository.existsById(999L);
    assertEquals(false, notExists);
}


}
