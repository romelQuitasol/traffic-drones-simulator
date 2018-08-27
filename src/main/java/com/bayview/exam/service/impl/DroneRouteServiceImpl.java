package com.bayview.exam.service.impl;

import com.bayview.exam.model.Route;
import com.bayview.exam.service.DroneRouteService;
import com.bayview.exam.util.CSVUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by romelquitasol on 22/08/2018.
 */
@Service("droneRouteService")
public class DroneRouteServiceImpl implements DroneRouteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DroneRouteServiceImpl.class);
  private static final String DRONE_FOLDER_PATH = "drones";

  @Override
  public List<Route> getDroneRoutes() throws Exception {
    List<Route> droneRoutes = new ArrayList<>();
    File[] files = CSVUtil.getFiles(DRONE_FOLDER_PATH);
    for (File file : files) {
      if(file.length() > 0){
        List<Route> tempRoutes = CSVUtil.parseCSVToBean(file.getPath(), Route.class)
            .parse();
        droneRoutes.addAll(tempRoutes);
      }
    }
    return droneRoutes;
  }

}
