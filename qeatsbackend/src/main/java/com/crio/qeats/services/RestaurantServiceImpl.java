
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

  LocalTime localTime;
  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  private boolean isTimeWithInRange(LocalTime now, LocalTime start_Time, LocalTime end_Time) {
    return now.isAfter(start_Time) && now.isBefore(end_Time);
  }

  public boolean isPeakHours(LocalTime timeNow) {
    return isTimeWithInRange(timeNow, localTime.of(7, 59, 59), localTime.of(10, 00, 01))
    || isTimeWithInRange(timeNow, localTime.of(12, 59, 59), localTime.of(14, 00, 01))
    || isTimeWithInRange(timeNow, localTime.of(18, 59, 59), localTime.of(21, 00, 01));
  }

  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
      Double servingRadiusInKms = 0.0;

      servingRadiusInKms = isPeakHours(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;
      List<Restaurant> restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getRLatitude(), getRestaurantsRequest.getRLongitude(), currentTime, servingRadiusInKms);
      
     return new GetRestaurantsResponse(restaurants);
  }
}

