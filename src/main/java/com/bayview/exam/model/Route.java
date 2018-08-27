package com.bayview.exam.model;

import com.opencsv.bean.CsvBindByPosition;

/**
 * Created by romelquitasol on 22/08/2018.
 */
public class Route {
  @CsvBindByPosition(position = 0)
  private String droneId;

  @CsvBindByPosition(position = 1)
  private double latitude;

  @CsvBindByPosition(position = 2)
  private double longitude;

  @CsvBindByPosition(position = 3)
  private String time;

  public String getDroneId() {
    return droneId;
  }

  public void setDroneId(String droneId) {
    this.droneId = droneId;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Route{");
    sb.append("droneId='").append(droneId).append('\'');
    sb.append(", latitude=").append(latitude);
    sb.append(", longitude=").append(longitude);
    sb.append(", time='").append(time).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
