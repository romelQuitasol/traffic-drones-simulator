package com.bayview.exam.model;

import com.opencsv.bean.CsvBindByPosition;

/**
 * Created by romelquitasol on 23/08/2018.
 */
public class TubeStation {

  @CsvBindByPosition(position = 0)
  private String station;

  @CsvBindByPosition(position = 1)
  private double latitude;

  @CsvBindByPosition(position = 2)
  private double longitude;

  public String getStation() {
    return station;
  }

  public void setStation(String station) {
    this.station = station;
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

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("TubeModel{");
    sb.append("station='").append(station).append('\'');
    sb.append(", latitude=").append(latitude);
    sb.append(", longitude=").append(longitude);
    sb.append('}');
    return sb.toString();
  }
}
