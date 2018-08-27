package com.bayview.exam.service.impl;

import com.bayview.exam.model.TrafficReport;
import com.bayview.exam.repository.TrafficReportRepository;
import com.bayview.exam.service.TrafficReportService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by romelquitasol on 23/08/2018.
 */
@Transactional
@Service("trafficReportService")
public class TrafficReportServiceImpl implements TrafficReportService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrafficReportServiceImpl.class);

  @Autowired
  private TrafficReportRepository trafficReportRepository;

  @Override
  public void saveTrafficReport(TrafficReport trafficReport) {
    trafficReportRepository.save(trafficReport);
  }

  @Override
  public List<TrafficReport> getAllTrafficReports() {
    return trafficReportRepository.findAll();
  }

}
