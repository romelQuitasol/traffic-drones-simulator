package com.bayview.exam.threads;

import com.bayview.exam.enums.CommandType;
import com.bayview.exam.enums.TrafficCondition;
import com.bayview.exam.model.Command;
import com.bayview.exam.model.Route;
import com.bayview.exam.model.TrafficReport;
import com.bayview.exam.model.TubeStation;
import com.bayview.exam.util.DateUtil;
import com.bayview.exam.util.DistanceUtil;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by romelquitasol on 24/08/2018.
 */
@Component
@Scope("prototype")
public class Drone implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(Drone.class);

  private static final int MAXIMUM_IN_MEMORY_ROUTES = 10;

  private String droneId;
  private double latitude;
  private double longitude;
  private String time;
  private BlockingQueue<Route> inMemoryRoute;
  private BlockingQueue<Command> dispatcherCommands;
  private BlockingQueue<Command> droneCommands;
  private List<TubeStation> tubeStations;
  private Route currentRoute;
  private boolean power = true;
  private boolean requestRoutes = false;

  public boolean isOn() {
    return power;
  }

  public void turnOff() {
    this.power = false;
  }

  public String getDroneId() {
    return droneId;
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

  public Route getCurrentRoute() {
    return currentRoute;
  }

  public BlockingQueue<Command> getDroneCommands() {
    return droneCommands;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Drone{");
    sb.append("droneId='").append(droneId).append('\'');
    sb.append(", time='").append(time).append('\'');
    sb.append('}');
    return sb.toString();
  }

  /**
   *
   */
  public Drone(String droneId, BlockingQueue<Command> dispatcherCommands,
      List<TubeStation> tubeStations) {
    this.droneId = droneId;
    this.inMemoryRoute = new LinkedBlockingQueue<>(MAXIMUM_IN_MEMORY_ROUTES);
    this.dispatcherCommands = dispatcherCommands;
    this.tubeStations = tubeStations;
    this.droneCommands = new LinkedBlockingQueue<>();
  }


  /**
   * Start drone
   */
  public void run() {
    LOGGER.info("Drone({}) has started.", getDroneId());
    while (isOn()) {
      processDroneCommands();
      getRouteFromMemory();
      moveToRoute();
    }
    LOGGER.info("Drone({}) has shut down.", getDroneId());
  }

  /**
   * Process commands received from dispatcher
   */
  public void processDroneCommands() {
    try {
      if (!droneCommands.isEmpty() || requestRoutes) {
        Command command = droneCommands.take();
        if (command != null) {
          switch (command.getCommandType()) {
            case CONFIRM_ROUTE_RECEIVED:
              LOGGER.info("Drone({}) has received {} routes", getDroneId(), inMemoryRoutesUsed());
              requestRoutes = false;
              break;

            case SHUTDOWN:
              command = new Command.Builder()
                  .sender(getDroneId())
                  .commandType(CommandType.IS_SHUTTING_DOWN)
                  .build();
              dispatcherCommands.offer(command);
              turnOff();
              break;

            default:
              break;
          }
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get route from inMemoryRoute
   */
  public void getRouteFromMemory() {
    Route route = null;
    try {
      if (hasAvailableMemory()) {
        if (!requestRoutes) {
          sendCommandToDispatcher(CommandType.REQUEST_ROUTES);
        }
      } else {
        route = inMemoryRoute.take();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    currentRoute = route;

  }

  /**
   * Function to move to certain route
   */
  private void moveToRoute() {
    if (currentRoute != null && isOn()) {
      for (TubeStation tubeStation : tubeStations) {
        if (DistanceUtil.isRouteCloseToStation(currentRoute, tubeStation)) {
          this.setLatitude(currentRoute.getLatitude());
          this.setLongitude(currentRoute.getLongitude());
          this.setTime(DateUtil.getTimeStr(currentRoute.getTime()));
          sendCommandToDispatcher(CommandType.REPORT_TRAFFIC_CONDITION);
        }
      }
    }
  }

  /**
   * Add route to drone
   */
  public boolean addRouteToDrone(Route route) {
    return inMemoryRoute.offer(route);
  }

  private int inMemoryRoutesUsed() {
    return inMemoryRoute.size();
  }

  /**
   * Check if mermory is available
   */
  public boolean hasAvailableMemory() {
    if (inMemoryRoute.isEmpty()) {
      return true;
    }
    return false;
  }

  /**
   * Send different commands to dispatcher by command type
   */
  public void sendCommandToDispatcher(CommandType commandType) {
    Command command;
    switch (commandType) {
      case REQUEST_ROUTES:
        if (isOn()) {
          LOGGER.info("Drone({}) requesting for additional routes", getDroneId());
          command = new Command.Builder()
              .sender(droneId)
              .commandType(CommandType.REQUEST_ROUTES)
              .build();
          dispatcherCommands.offer(command);
          requestRoutes = true;
        }
        break;

      case REPORT_TRAFFIC_CONDITION:
        if (isOn()) {
          TrafficReport trafficReport = buildTrafficReport();
          LOGGER
              .info("Drone({}) located at lat:{} and long:{} on {} reporting traffic is {}",
                  getDroneId(), getLatitude(), getLongitude(), getTime(),
                  trafficReport.getTrafficCondition());
          command = new Command.Builder()
              .sender(getDroneId())
              .commandType(CommandType.REPORT_TRAFFIC_CONDITION)
              .data(trafficReport)
              .build();
          dispatcherCommands.offer(command);
        }
        break;

      default:
        break;
    }
  }

  /**
   * Builds a new traffic report;
   */
  private TrafficReport buildTrafficReport() {
    TrafficReport trafficReport = new TrafficReport();
    trafficReport.setDroneId(getDroneId());
    trafficReport.setTime(getCurrentRoute().getTime());
    trafficReport.setSpeed(new Random().nextInt(50));
    trafficReport.setTrafficCondition(TrafficCondition.randomCondition());
    return trafficReport;
  }

}
