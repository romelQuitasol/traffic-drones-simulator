package com.bayview.exam.service;

import com.bayview.exam.model.TrafficReport;
import java.util.List;

/**
 * Created by romelquitasol on 23/08/2018.
 */
public interface TrafficReportService {

  void saveTrafficReport(TrafficReport trafficReport);

  List<TrafficReport> getAllTrafficReports();
}
