package com.project.tourplanner.tour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourLogRepository extends JpaRepository<TourLog, Long>{


}
    
