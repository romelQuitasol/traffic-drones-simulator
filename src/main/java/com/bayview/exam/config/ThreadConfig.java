package com.bayview.exam.config;

import com.bayview.exam.threads.Dispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by romelquitasol on 25/08/2018.
 */
@Configuration
@EnableAsync
public class ThreadConfig {

  @Bean
  public ThreadPoolTaskExecutor dispatcherTaskExecutor() {
    return new ThreadPoolTaskExecutor();
  }

}
