package com.bayview.exam.service.impl;

import com.bayview.exam.model.TubeStation;
import com.bayview.exam.service.TubeStationService;
import com.bayview.exam.util.CSVUtil;
import java.io.File;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by romelquitasol on 23/08/2018.
 */
@Service("tubeStationService")
public class TubeStationServiceImpl implements TubeStationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TubeStationServiceImpl.class);
  private static final String TUBE_STATION_FILE_NAME = "tube.csv";

  @Override
  public List<TubeStation> getTubeStations() {
    try {
      File file = CSVUtil.getFile(TUBE_STATION_FILE_NAME);
      if(file.exists() && file.length() > 0){
        List<TubeStation> tubeStations = CSVUtil
            .parseCSVToBean(file.getPath(), TubeStation.class).parse();
        return tubeStations;
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    return Collections.EMPTY_LIST;
  }

}
