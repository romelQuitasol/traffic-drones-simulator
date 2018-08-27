package com.bayview.exam.model;

import com.bayview.exam.enums.TrafficCondition;
import com.opencsv.bean.CsvBindByPosition;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by romelquitasol on 22/08/2018.
 */
@Entity
public class TrafficReport {

  @Id
  @GeneratedValue
  private Long id;

  @CsvBindByPosition(position = 0)
  private String droneId;

  @CsvBindByPosition(position = 1)
  private String time;

  @CsvBindByPosition(position = 2)
  private Integer speed;

  @CsvBindByPosition(position = 3)
  private TrafficCondition trafficCondition;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDroneId() {
    return droneId;
  }

  public void setDroneId(String droneId) {
    this.droneId = droneId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Integer getSpeed() {
    return speed;
  }

  public void setSpeed(Integer speed) {
    this.speed = speed;
  }

  public TrafficCondition getTrafficCondition() {
    return trafficCondition;
  }

  public void setTrafficCondition(TrafficCondition trafficCondition) {
    this.trafficCondition = trafficCondition;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("TrafficReportModel{");
    sb.append("id=").append(id);
    sb.append(", droneId='").append(droneId).append('\'');
    sb.append(", time='").append(time).append('\'');
    sb.append(", speed=").append(speed);
    sb.append(", trafficCondition=").append(trafficCondition);
    sb.append('}');
    return sb.toString();
  }
}
