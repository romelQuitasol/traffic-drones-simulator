package com.bayview.exam.threads;

import com.bayview.exam.enums.CommandType;
import com.bayview.exam.model.Command;
import com.bayview.exam.model.Route;
import com.bayview.exam.model.TrafficReport;
import com.bayview.exam.model.TubeStation;
import com.bayview.exam.service.TrafficReportService;
import com.bayview.exam.util.CSVUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by romelquitasol on 24/08/2018.
 */
@Component
@Scope("prototype")
public class Dispatcher implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

  private static final String TRAFFIC_REPORT_FILE_NAME = "traffic-report";
  private static final String DRONE_SHUT_DOWN_TIME = "8:10";

  @Autowired
  private TrafficReportService trafficReportService;

  private String id;
  private List<String> droneIds;
  private BlockingQueue<Command> dispatcherCommands;
  private List<Drone> activeDrones = new ArrayList<>();
  private List<Route> droneRoutes;
  private List<TubeStation> tubeStations;

  public String getId() {
    return id;
  }

  /**
   * Dispatcher constructor
   */
  public Dispatcher(String id, List<String> droneIds, List<Route> droneRoutes,
      List<TubeStation> tubeStations)
      throws Exception {
    this.id = id;
    this.dispatcherCommands = new LinkedBlockingQueue<>();
    this.droneIds = droneIds;
    this.droneRoutes = droneRoutes;
    this.tubeStations = tubeStations;
  }

  /**
   * Run dispatcher methods
   */
  public void run() {
    LOGGER.info("Dispatcher has started");
    startDrones();
    while (!isDispatcherTerminable()) {
      processDispatcherCommands();
      shutDownDrones();
    }
    generateTrafficReports();
    LOGGER.info("Dispatcher has shut down");
  }

  /**
   * Generate traffic reports after drones finish reporting
   */
  public void generateTrafficReports() {
    try {
      List<TrafficReport> trafficReports = trafficReportService.getAllTrafficReports();
      File file = CSVUtil.generateCSVBy(trafficReports, TRAFFIC_REPORT_FILE_NAME);
      LOGGER.info("Dispatcher has finished generating reports and file path location is {}",
          file.getPath());
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private boolean isDispatcherTerminable() {
    boolean isTerminable = dispatcherCommands.isEmpty() && activeDrones.isEmpty();
    return isTerminable;
  }

  /**
   * Start drones when dispatcher starts.
   */
  public void startDrones() {
    addDrones(droneIds, dispatcherCommands);
    for (Drone drone : activeDrones) {
      new Thread(drone, "Drone(" + drone.getDroneId() + ")").start();
    }
  }

  /**
   * Add drones to activeDrones list
   */
  public void addDrones(List<String> droneIds, BlockingQueue<Command> dispatcherCommands) {
    for (String droneId : droneIds) {
      Drone drone = new Drone(droneId, dispatcherCommands, tubeStations);
      activeDrones.add(drone);
    }
  }

  /**
   * Process commands received from drones
   */
  public void processDispatcherCommands() {
    Drone drone;
    try {
      Command command = dispatcherCommands.poll(5L, TimeUnit.MILLISECONDS);
      if (command != null) {
        drone = getDrone(command.getSender());
        switch (command.getCommandType()) {
          case REQUEST_ROUTES:
            if (drone.isOn() && isDroneOutOfRoutes(drone)) {
              sendCommandToDrone(drone, CommandType.SHUTDOWN);
            } else {
              dispatchRoutesToDrone(drone);
            }
            break;

          case REPORT_TRAFFIC_CONDITION:
            if (command.getData() instanceof TrafficReport) {
              TrafficReport trafficReport = (TrafficReport) command.getData();
              LOGGER.info("Dispatcher has received traffic report condition {} from Drone({})",
                  trafficReport.getTrafficCondition(), trafficReport.getDroneId());
              trafficReportService.saveTrafficReport(trafficReport);
            }
            break;

          case IS_SHUTTING_DOWN:
            activeDrones.remove(drone);
            break;

          default:
            break;
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  /**
   * Send different commands to drone by command type
   */
  public void sendCommandToDrone(Drone drone, CommandType commandType) {
    Command command;
    switch (commandType) {
      case CONFIRM_ROUTE_RECEIVED:
        LOGGER.info("Dispatcher is sending routes to Drone({}).", drone.getDroneId());
        if (drone.isOn()) {
          command = new Command.Builder()
              .sender(getId())
              .commandType(CommandType.CONFIRM_ROUTE_RECEIVED)
              .build();
          drone.getDroneCommands().offer(command);
        }
        break;

      case SHUTDOWN:
        LOGGER.info("Dispatcher is requesting Drone({}) to shutdown.", drone.getDroneId());
        command = new Command.Builder()
            .sender(getId())
            .commandType(CommandType.SHUTDOWN)
            .build();
        drone.getDroneCommands().offer(command);
        drone.turnOff();
        break;
      default:
        break;
    }
  }

  /**
   * Get current drone.
   */
  public Drone getDrone(String droneId) {
    Drone drone = null;
    for (Drone d : activeDrones) {
      if (d.getDroneId().equals(droneId)) {
        drone = d;
        break;
      }
    }
    return drone;
  }

  /**
   * Dispatch routes to drone.
   */
  public void dispatchRoutesToDrone(Drone drone) {
    if (drone.hasAvailableMemory()) {
      BlockingQueue<Route> droneRoutesQueue = getDroneRoutes(drone);
      for (Route route : droneRoutesQueue) {
        if (drone.addRouteToDrone(route)) {
          droneRoutes.remove(route);
        }
      }
      sendCommandToDrone(drone, CommandType.CONFIRM_ROUTE_RECEIVED);
    }
  }

  /**
   * Get drone routes
   */
  public BlockingQueue<Route> getDroneRoutes(Drone drone) {
    BlockingQueue<Route> droneRoutesQueue = new LinkedBlockingQueue<>();
    for (Iterator<Route> iterator = droneRoutes.iterator(); iterator.hasNext(); ) {
      Route droneRoute = iterator.next();
      if (drone.getDroneId().equals(droneRoute.getDroneId())) {
        droneRoutesQueue.add(droneRoute);
      }
    }
    return droneRoutesQueue;
  }

  private void shutDownDrones() {
    for (Drone drone : activeDrones) {
      if (drone.isOn() && isShutDownTime(drone.getTime())) {
        sendCommandToDrone(drone, CommandType.SHUTDOWN);
      }
    }
  }

  private boolean isShutDownTime(String time) {
    if (StringUtils.isNotBlank(time) && time.equals(DRONE_SHUT_DOWN_TIME)) {
      return true;
    }
    return false;
  }

  private boolean isDroneOutOfRoutes(Drone drone) {
    if (drone.hasAvailableMemory() && getDroneRoutes(drone).isEmpty()) {
      return true;
    }
    return false;
  }

}
