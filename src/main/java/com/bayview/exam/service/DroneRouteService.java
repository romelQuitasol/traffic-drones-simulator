package com.bayview.exam.service;

import com.bayview.exam.model.Route;
import java.util.List;

/**
 * Created by romelquitasol on 22/08/2018.
 */
public interface DroneRouteService {

  List<Route> getDroneRoutes() throws Exception;

}
