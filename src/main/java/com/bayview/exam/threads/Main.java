package com.bayview.exam.threads;

import com.bayview.exam.TrafficDronesExamApplication;
import com.bayview.exam.model.Route;
import com.bayview.exam.model.TubeStation;
import com.bayview.exam.service.DroneRouteService;
import com.bayview.exam.service.TubeStationService;
import com.bayview.exam.util.CSVUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by romelquitasol on 25/08/2018.
 */
@Component
public class Main implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrafficDronesExamApplication.class);
  private static final String DRONE_FOLDER_PATH = "drones";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private DroneRouteService droneRouteService;

  @Autowired
  private TubeStationService tubeStationService;

  @Autowired
  private ThreadPoolTaskExecutor dispatcherTaskExecutor;

  /**
   * Start dispatcher upon spring boot run
   * @param event
   */
  public void onApplicationEvent(ContextRefreshedEvent event) {
    try {
      List<String> droneIds = new ArrayList<>();
      File[] files = CSVUtil.getFiles(DRONE_FOLDER_PATH);
      for (File file : files) {
        String fileName = file.getName();
        droneIds.add(fileName.substring(0, fileName.lastIndexOf('.')));
      }
      List<Route> routes = droneRouteService.getDroneRoutes();
      List<TubeStation> tubeStations = tubeStationService.getTubeStations();

      dispatcherTaskExecutor.execute(applicationContext
          .getBean(Dispatcher.class, "Dispatcher", droneIds, routes, tubeStations));
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

}
