package com.bayview.exam;

import com.bayview.exam.model.Route;
import com.bayview.exam.model.TubeStation;
import com.bayview.exam.service.DroneRouteService;
import com.bayview.exam.service.TubeStationService;
import com.bayview.exam.threads.Dispatcher;
import com.bayview.exam.util.CSVUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@ComponentScan(basePackages = "com.bayview.exam")
@EnableJpaRepositories(basePackages = "com.bayview.exam.repository")
@EnableAutoConfiguration
@EnableAsync
public class TrafficDronesExamApplication {
  /**
   * Springboot Initializer
   */
  public static void main(String[] args) {
    SpringApplication.run(TrafficDronesExamApplication.class, args);
  }

}
