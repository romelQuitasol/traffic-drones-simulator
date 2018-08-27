package com.bayview.exam.util;

import com.bayview.exam.model.Route;
import com.bayview.exam.model.TubeStation;

/**
 * Created by romelquitasol on 24/08/2018.
 */
public class DistanceUtil {

  private static final int MAX_DISTANCE_TO_STATION = 350;

  /**
   * Validation if route location is close to station by max distance
   * @param route
   * @param station
   * @return
   */
  public static boolean isRouteCloseToStation(Route route, TubeStation station) {
    return calculateDistance(route.getLatitude(), route.getLongitude(), station.getLatitude(),
        station.getLongitude()) <= MAX_DISTANCE_TO_STATION;
  }

  /**
   * Calculates distance between 2 locations by latitude and longitude
   * @param latitude1
   * @param longitude1
   * @param latitude2
   * @param longitude2
   * @return
   */
  public static double calculateDistance(double latitude1, double longitude1, double latitude2,
      double longitude2) {
    double earthRadius = 6371000;
    double distanceLatitude = Math.toRadians(latitude2 - latitude1);
    double distanceLongitude = Math.toRadians(longitude2 - longitude1);
    double a = Math.sin(distanceLatitude / 2) * Math.sin(distanceLatitude / 2)
        + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
        * Math.sin(distanceLongitude / 2) * Math.sin(distanceLongitude / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double dist = earthRadius * c;
    return dist;
  }

}
