package com.project.tourplanner.tour;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TourLogDtoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public TourLogDto mapTourLogToTourLogDto(TourLog tourLog){
        TourLogDto tourLogDto = modelMapper.map(tourLog, TourLogDto.class);
        return tourLogDto;
    }
    
    public TourLog mapTourLogDtoToTourLog(TourLogDto tourLogDto){
        TourLog tourLog = modelMapper.map(tourLogDto, TourLog.class);
        return tourLog;
    }
}
