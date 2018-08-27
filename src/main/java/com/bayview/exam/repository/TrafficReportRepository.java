package com.bayview.exam.repository;

import com.bayview.exam.model.TrafficReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by romelquitasol on 23/08/2018.
 */
@Component
public interface TrafficReportRepository extends JpaRepository<TrafficReport, Long> {

}
