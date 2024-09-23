package com.project.tourplanner.tour;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TourDtoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public TourDto mapTourToTourDto(Tour tour){
        TourDto tourDto = modelMapper.map(tour, TourDto.class);
        return tourDto;
    }
    
    public Tour mapTourDtoToTour(TourDto tourDto){
        Tour tour = modelMapper.map(tourDto, Tour.class);
        return tour;
    }
}
